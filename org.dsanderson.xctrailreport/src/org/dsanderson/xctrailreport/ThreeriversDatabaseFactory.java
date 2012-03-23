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
package org.dsanderson.xctrailreport;

import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.android.util.IDatabaseObjectFactory;
import org.dsanderson.util.DatabaseObject;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversFactory;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversTrailInfo;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 */
public class ThreeriversDatabaseFactory implements IDatabaseObjectFactory {

	private static final String PREFIX = ThreeRiversFactory.XML_TAG + "_";

	public static final String COLUMN_INFO_URL = PREFIX + "info_url";
	private static final String TYPE_INFO_URL = "text not null";

	private final ThreeRiversFactory factory;

	ThreeriversDatabaseFactory(ThreeRiversFactory factory) {
		this.factory = factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.IDatabaseObjectFactory#registerColumns(org
	 * .dsanderson.android.util.GenericDatabase)
	 */
	public void registerColumns(GenericDatabase database) {
		database.addColumn(COLUMN_INFO_URL, TYPE_INFO_URL);
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
		ISourceSpecificTrailInfo sourceSpecific = info
				.getSourceSpecificInfo(ThreeRiversFactory.SOURCE_NAME);
		if (sourceSpecific != null) {
			ThreeRiversTrailInfo threeRiversInfo = (ThreeRiversTrailInfo) sourceSpecific;
			values.put(COLUMN_INFO_URL, threeRiversInfo.getTrailInfoShortUrl());
		}
		else {
			values.put(COLUMN_INFO_URL, "");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.IDatabaseObjectFactory#getObject(android.
	 * database.Cursor, org.dsanderson.util.DatabaseObject)
	 */
	public DatabaseObject getObject(Cursor cursor, DatabaseObject object) {
		TrailInfo info = (TrailInfo) object;
		ISourceSpecificTrailInfo sourceInfo = info
				.getSourceSpecificInfo(ThreeRiversFactory.SOURCE_NAME);
		ThreeRiversTrailInfo threeRiversInfo;
		boolean existing;
		if (existing = (sourceInfo != null))
			threeRiversInfo = (ThreeRiversTrailInfo) sourceInfo;
		else
			threeRiversInfo = factory.getPool().newItem();

		threeRiversInfo.setTrailInfoUrl(cursor.getString(cursor
				.getColumnIndex(COLUMN_INFO_URL)));

		if (!existing)
			info.addSourceSpecificInfo(threeRiversInfo);

		return info;
	}

}
