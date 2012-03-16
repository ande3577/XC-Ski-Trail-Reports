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
package org.dsanderson.xctrailreport.test;

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
public class TestDataBase extends SQLiteOpenHelper {
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "test_database.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_TEST = "test";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_NAME = "name";

	private static final String DATABASE_CREATE = "create table " + TABLE_TEST
			+ "( " + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null);";

	private static final String[] allColumns = { COLUMN_ID, COLUMN_NAME };

	public TestDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		db.execSQL(DATABASE_CREATE);
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
		Log.w(TestDataBase.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
		onCreate(db);
	}

	public void open() throws SQLException {
		database = getWritableDatabase();
	}

	public void close() {
		super.close();
	}

	public TestDatabaseObject createTestObject(String name, int value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		long insertId = database.insert(TABLE_TEST, null, values);
		// To show how to query
		Cursor cursor = database.query(TABLE_TEST, allColumns, COLUMN_ID
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		return cursorToTestObject(cursor);
	}

	public void deleteComment(TestDatabaseObject testObject) {
		long id = testObject.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(TABLE_TEST, COLUMN_ID + " = " + id, null);
	}
	
	public List<TestDatabaseObject> getAllObjects() {
		List<TestDatabaseObject> comments = new ArrayList<TestDatabaseObject>();
		Cursor cursor = database.query(TABLE_TEST,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TestDatabaseObject testObject = cursorToTestObject(cursor);
			comments.add(testObject);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}
	
	private TestDatabaseObject cursorToTestObject(Cursor cursor) {
		TestDatabaseObject testObject = new TestDatabaseObject();
		testObject.setId(cursor.getLong(0));
		testObject.setName(cursor.getString(1));
		return testObject;
	}

}
