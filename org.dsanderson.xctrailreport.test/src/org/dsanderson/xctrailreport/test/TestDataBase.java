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
public class TestDataBase extends GenericDatabase {
	public static final String DATABASE_NAME = "test_database.db";
	public static final int DATABASE_VERSION = 6;
	public static final String TABLE_TEST = "test";

	public TestDataBase(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION, TABLE_TEST, new TestDatabaseFactory());

	}

}
