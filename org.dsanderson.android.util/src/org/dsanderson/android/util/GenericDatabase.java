/**
 * @author David S Anderson
 *
 *
 * Copyright (C) 2012 David S Anderson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dsanderson.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 */
public abstract class GenericDatabase<T> extends SQLiteOpenHelper {
	private SQLiteDatabase database;

	private final String tableName;
	private final String[] allColumns;
	private final String[] allTypes;

	protected static final String COLUMN_ID = "_id";
	protected static final String TYPE_ID = "integer primary key autoincrement";

	// private static final String DATABASE_CREATE = "create table " +
	// TABLE_TEST
	// + "( " + COLUMN_ID + " integer primary key autoincrement, "
	// + COLUMN_NAME + " text not null, " + COLUMN_VALUE
	// + " integer not null );";

	public GenericDatabase(Context context, String dataBaseName,
			int dataBaseVersion, String tableName, String[] allColumns,
			String[] allTypes) {
		super(context, dataBaseName, null, dataBaseVersion);
		assert (allColumns.length == allTypes.length);
		this.tableName = tableName;
		this.allColumns = allColumns;
		this.allTypes = allTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		String dataBaseCreate = "create table " + tableName + "( ";
		for (int i = 0; i < allColumns.length; i++) {
			if (i != 0)
				dataBaseCreate += ", ";

			dataBaseCreate += allColumns[i] + " " + allTypes[i];
		}
		dataBaseCreate += ");";

		db.execSQL(dataBaseCreate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(GenericDatabase.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(db);
	}

	public void open() throws SQLException {
		database = getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	abstract protected void buildContentValues(T object, ContentValues values);

	abstract protected T getObject(Cursor cursor);

	public void insert(T object) {
		ContentValues values = new ContentValues();
		buildContentValues(object, values);
		database.insert(tableName, null, values);
	}

	public void remove(Cursor cursor) {
		long id = cursor.getInt(0);
		remove(id);
	}

	public void remove(DatabaseObject<T> testObject) {
		long id = testObject.getId();
		remove(id);
	}

	private void remove(long id) {
		System.out.println("Comment deleted with id: " + id);
		database.delete(tableName, COLUMN_ID + " = " + id, null);
	}

	public List<DatabaseObject<T>> getAllObjects() {
		List<DatabaseObject<T>> objects = new ArrayList<DatabaseObject<T>>();
		Cursor cursor = getCursor();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DatabaseObject<T> object = new DatabaseObject<T>();
			object.setData(getObject(cursor));
			objects.add(object);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return objects;
	}

	public Cursor getCursor() {
		return database.query(tableName, allColumns, null, null, null, null,
				null);
	}

	public void update(DatabaseObject<T> object) {
		ContentValues values = new ContentValues();
		buildContentValues(object.getData(), values);
		long id = object.getId();
		database.update(tableName, values, COLUMN_ID + " = " + id, null);
	}

	public void clear() {
		database.delete(tableName, null, null);
	}

}
