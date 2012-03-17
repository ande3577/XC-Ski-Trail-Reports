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

import org.dsanderson.android.util.GenericDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * 
 */
public class TestDataBase extends GenericDatabase<TestDatabaseObject> {
	public static final String TABLE_TEST = "test";
	public static final String COLUMN_NAME = "name";
	private static final String TYPE_NAME = "text not null";
	public static final String COLUMN_VALUE = "value";
	private static final String TYPE_VALUE = "integer not null";

	public static final String DATABASE_NAME = "test_database.db";
	public static final int DATABASE_VERSION = 6;

	private static final String[] allColumns = { COLUMN_ID, COLUMN_NAME,
			COLUMN_VALUE };
	private static final String[] allTypes = { TYPE_ID, TYPE_NAME, TYPE_VALUE };

	public TestDataBase(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION, TABLE_TEST, allColumns,
				allTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.test.GenericDatabase#buildContentValues(
	 * java.lang.Object, android.content.ContentValues)
	 */
	@Override
	protected void buildContentValues(TestDatabaseObject object,
			ContentValues values) {
		values.put(COLUMN_NAME, object.getName());
		values.put(COLUMN_VALUE, object.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.test.GenericDatabase#cursorToTestObject(
	 * android.database.Cursor)
	 */
	@Override
	protected TestDatabaseObject getObject(Cursor cursor) {
		return new TestDatabaseObject(cursor.getString(1), cursor.getInt(2));
	}
}
