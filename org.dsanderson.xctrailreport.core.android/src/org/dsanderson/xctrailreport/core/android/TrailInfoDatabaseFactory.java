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
package org.dsanderson.xctrailreport.core.android;

import java.util.List;

import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.android.util.IDatabaseObjectFactory;
import org.dsanderson.util.DatabaseObject;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoPool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * 
 */
public class TrailInfoDatabaseFactory implements IDatabaseObjectFactory {
	private final TrailInfoPool pool;
	private final List<IDatabaseObjectFactory> sourceSpecificFactories;
	GenericDatabase database = null;

	public static final String COLUMN_NAME = "name";
	private static final String TYPE_NAME = "text not null";

	public static final String COLUMN_CITY = "city";
	private static final String TYPE_CITY = "text not null";

	public static final String COLUMN_STATE = "state";
	private static final String TYPE_STATE = "text not null";

	public static final String COLUMN_LOCATION = "location";
	private static final String TYPE_LOCATION = "text not null";

	public static final String COLUMN_DISTANCE = "distance";
	private static final String TYPE_DISTANCE = "integer not null";

	public static final String COLUMN_DURATION = "duration";
	private static final String TYPE_DURATION = "integer not null";

	public TrailInfoDatabaseFactory(TrailInfoPool pool,
			List<IDatabaseObjectFactory> sourceSpecificFactories) {
		this.pool = pool;
		this.sourceSpecificFactories = sourceSpecificFactories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.IDatabaseObjectFactory#registerColumns(org
	 * .dsanderson.android.util.GenericDatabase)
	 */
	public void registerColumns(GenericDatabase database) {
		database.addColumn(COLUMN_NAME, TYPE_NAME) //
				.addColumn(COLUMN_CITY, TYPE_CITY) //
				.addColumn(COLUMN_STATE, TYPE_STATE) //
				.addColumn(COLUMN_LOCATION, TYPE_LOCATION) //
				.addColumn(COLUMN_DISTANCE, TYPE_DISTANCE) //
				.addColumn(COLUMN_DURATION, TYPE_DURATION);

		for (IDatabaseObjectFactory factory : sourceSpecificFactories)
			factory.registerColumns(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.IDatabaseObjectFactory#buildContentValues
	 * (org.dsanderson.util.DatabaseObject, android.content.ContentValues)
	 */
	public void buildContentValues(DatabaseObject object, ContentValues values) {
		TrailInfo info = (TrailInfo) object;
		values.put(COLUMN_NAME, info.getName());
		values.put(COLUMN_CITY, info.getCity());
		values.put(COLUMN_STATE, info.getState());
		values.put(COLUMN_LOCATION, info.getLocation());

		int distance, duration;
		if (info.getDistanceValid()) {
			distance = info.getDistance();
			duration = info.getDuration();
		} else {
			distance = duration = Integer.MAX_VALUE;
		}

		values.put(COLUMN_DISTANCE, distance);
		values.put(COLUMN_DURATION, duration);

		for (IDatabaseObjectFactory factory : sourceSpecificFactories)
			factory.buildContentValues(info, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.IDatabaseObjectFactory#getObject(android.
	 * database.Cursor)
	 */
	public DatabaseObject getObject(Cursor cursor, DatabaseObject object) {
		TrailInfo info = (TrailInfo) object;
		if (object == null)
			info = pool.newItem();
		
		info.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
		info.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
		info.setState(cursor.getString(cursor.getColumnIndex(COLUMN_STATE)));
		info.setLocation(cursor.getString(cursor
				.getColumnIndex(COLUMN_LOCATION)));
		info.setDistance(cursor.getInt(cursor.getColumnIndex(COLUMN_DISTANCE)));
		info.setDuration(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION)));
		if (info.getDistance() != Integer.MAX_VALUE
				&& info.getDuration() != Integer.MAX_VALUE)
			info.setDistanceValid(true);
		else
			info.setDistanceValid(false);

		for (IDatabaseObjectFactory factory : sourceSpecificFactories)
			info = (TrailInfo) factory.getObject(cursor, info);

		return info;
	}

}
