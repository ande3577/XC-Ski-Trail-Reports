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
package org.dsanderson.morctrailreport;

import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.android.util.IDatabaseObjectFactory;
import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;
import org.dsanderson.util.DatabaseObject;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 */
public class MorcDatabaseFactory implements IDatabaseObjectFactory {

	private static final String PREFIX = MorcFactory.XML_TAG + "_";

	public static final String COLUMN_INFO_URL = PREFIX + "info_url";
	private static final String TYPE_INFO_URL = "text not null";

	public static final String COLUMN_ALL_REPORTS_URL = PREFIX
			+ "all_resports_url";
	private static final String TYPE_ALL_REPORTS_URL = "text not null";

	public static final String COLUMN_COMPOSE_URL = PREFIX + "compuse_url";
	private static final String TYPE_COMPOSE_URL = "text not null";

	private final MorcFactory factory;
	
	MorcDatabaseFactory(MorcFactory factory) {
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
		database.addColumn(COLUMN_ALL_REPORTS_URL, TYPE_ALL_REPORTS_URL);
		database.addColumn(COLUMN_COMPOSE_URL, TYPE_COMPOSE_URL);
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
				.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
		if (sourceSpecific != null) {
			MorcSpecificTrailInfo morcInfo = (MorcSpecificTrailInfo) sourceSpecific;
			values.put(COLUMN_INFO_URL, morcInfo.getTrailInfoShortUrl());
			values.put(COLUMN_ALL_REPORTS_URL, morcInfo.getAllReportShortUrl());
			values.put(COLUMN_COMPOSE_URL, morcInfo.getComposeShortUrl());
		} else {
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
				.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
		MorcSpecificTrailInfo morcInfo;
		boolean existing;
		if (existing = (sourceInfo != null))
			morcInfo = (MorcSpecificTrailInfo) sourceInfo;
		else
			morcInfo = factory.getTrailInfoPool().newItem();

		morcInfo.setTrailInfoShortUrl(cursor.getString(cursor
				.getColumnIndex(COLUMN_INFO_URL)));
		morcInfo.setComposeShortUrl(cursor.getString(cursor
				.getColumnIndex(COLUMN_COMPOSE_URL)));
		morcInfo.setAllReportShortUrl(cursor.getString(cursor
				.getColumnIndex(COLUMN_ALL_REPORTS_URL)));

		if (!existing)
			info.addSourceSpecificInfo(morcInfo);

		return info;
	}

}
