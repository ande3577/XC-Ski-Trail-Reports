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
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiSpecificInfo;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 */
public class SkinnyskiDatabaseFactory implements IDatabaseObjectFactory {
	private final SkinnyskiFactory factory;

	private static final String PREFIX = SkinnyskiFactory.SKINNYSKI_XML_TAG
			+ "_";

	public static final String COLUMN_TRAIL_INDEX = PREFIX + "index";
	private static final String TYPE_TRAIL_INDEX = "integer not null";

	/**
	 * 
	 */
	public SkinnyskiDatabaseFactory(SkinnyskiFactory factory) {
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
		database.addColumn(COLUMN_TRAIL_INDEX, TYPE_TRAIL_INDEX);
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
		ISourceSpecificTrailInfo specificInfo = info
				.getSourceSpecificInfo(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
		if (specificInfo != null) {
			values.put(COLUMN_TRAIL_INDEX,
					((SkinnyskiSpecificInfo) specificInfo).getTrailIndex());
		} else {
			values.put(COLUMN_TRAIL_INDEX,
					SkinnyskiSpecificInfo.NULL_TRAIL_INDEX);
		}
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
		ISourceSpecificTrailInfo sourceSpecific = info
				.getSourceSpecificInfo(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);

		SkinnyskiSpecificInfo skinnyskiInfo;
		boolean exists;
		if (exists = (sourceSpecific != null))
			skinnyskiInfo = (SkinnyskiSpecificInfo) sourceSpecific;
		else
			skinnyskiInfo = factory.getTrailInfoPool().newItem();

		skinnyskiInfo.setTrailIndex(cursor.getInt(cursor
				.getColumnIndex(COLUMN_TRAIL_INDEX)));

		if (!exists)
			info.addSourceSpecificInfo(skinnyskiInfo);

		return info;
	}

}
