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

import org.dsanderson.android.util.DatabaseObject;
import org.dsanderson.android.util.DatabaseObjectFactory;
import org.dsanderson.android.util.GenericDatabase;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 */
public class TestDatabaseFactory implements DatabaseObjectFactory {
	public static final String COLUMN_NAME = "name";
	private static final String TYPE_NAME = "text not null";
	public static final String COLUMN_VALUE = "value";
	private static final String TYPE_VALUE = "integer not null";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.DatabaseObjectFactory#registerColumns(org
	 * .dsanderson.android.util.GenericDatabase)
	 */
	public void registerColumns(GenericDatabase database) {
		database.addColumn(COLUMN_NAME, TYPE_NAME).//
				addColumn(COLUMN_VALUE, TYPE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.DatabaseObjectFactory#buildContentValues(
	 * org.dsanderson.android.util.DatabaseObject,
	 * android.content.ContentValues)
	 */
	public void buildContentValues(DatabaseObject object, ContentValues values) {
		TestDatabaseObject testObject = (TestDatabaseObject) object;
		values.put(COLUMN_NAME, testObject.getName());
		values.put(COLUMN_VALUE, testObject.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.DatabaseObjectFactory#getObject(android.database
	 * .Cursor)
	 */
	public DatabaseObject getObject(Cursor cursor, GenericDatabase database) {
		return new TestDatabaseObject(cursor.getString(database
				.findColumn(COLUMN_NAME)), cursor.getInt(database
				.findColumn(COLUMN_VALUE)));
	}
}
