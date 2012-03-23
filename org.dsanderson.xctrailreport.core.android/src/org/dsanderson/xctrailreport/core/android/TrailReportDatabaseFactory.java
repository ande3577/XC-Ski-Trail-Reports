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

import org.dsanderson.util.DatabaseObject;
import org.dsanderson.android.util.IDatabaseObjectFactory;
import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.xctrailreport.core.ReportDate;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.TrailReportPool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * 
 */
public class TrailReportDatabaseFactory implements IDatabaseObjectFactory {
	public static final String COLUMN_SUMMARY = "summary";
	private static final String TYPE_SUMMARY = "text not null";

	public static final String COLUMN_AUTHOR = "author";
	private static final String TYPE_AUTHOR = "text not null";

	public static final String COLUMN_DATE = "date";
	private static final String TYPE_DATE = "integer not null";

	public static final String COLUMN_DETAIL = "detail";
	private static final String TYPE_DETAIL = "text not null";

	public static final String COLUMN_SOURCE = "source";
	private static final String TYPE_SOURCE = "text not null";

	public static final String COLUMN_PHOTOSET = "photosetURL";
	private static final String TYPE_PHOTOSET = "text not null";

	private final TrailReportPool pool;

	private final TrailInfoDatabaseFactory trailInfoFactory;

	public static final String DATABASE_NAME = "trail_report_database.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_TEST = "trail_report";

	/**
	 * 
	 */
	public TrailReportDatabaseFactory(TrailReportPool pool,
			TrailInfoDatabaseFactory trailInfoFactory) {
		this.pool = pool;
		this.trailInfoFactory = trailInfoFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.DatabaseObjectFactory#registerColumns(org
	 * .dsanderson.android.util.GenericDatabase)
	 */
	public void registerColumns(GenericDatabase database) {
		database.addColumn(COLUMN_SUMMARY, TYPE_SUMMARY) //
				.addColumn(COLUMN_AUTHOR, TYPE_AUTHOR) //
				.addColumn(COLUMN_DATE, TYPE_DATE) //
				.addColumn(COLUMN_DETAIL, TYPE_DETAIL) //
				.addColumn(COLUMN_SOURCE, TYPE_SOURCE) //
				.addColumn(COLUMN_PHOTOSET, TYPE_PHOTOSET);

		trailInfoFactory.registerColumns(database);

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
		TrailReport report = ((TrailReport) object);
		values.put(COLUMN_SUMMARY, report.getSummary());
		values.put(COLUMN_AUTHOR, report.getAuthor());
		values.put(COLUMN_DATE, report.getDate().getDate().getTime());
		values.put(COLUMN_DETAIL, report.getDetail());
		values.put(COLUMN_SOURCE, report.getSource());
		values.put(COLUMN_PHOTOSET, report.getPhotosetUrl());
		trailInfoFactory.buildContentValues(report.getTrailInfo(), values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.android.util.DatabaseObjectFactory#getObject(android.database
	 * .Cursor, org.dsanderson.android.util.GenericDatabase)
	 */
	public DatabaseObject getObject(Cursor cursor, DatabaseObject object) {
		TrailReport report = pool.newItem();

		report.setSummary(cursor.getString(cursor
				.getColumnIndex(COLUMN_SUMMARY)));
		report.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
		report.setDate(new ReportDate(cursor.getLong(cursor
				.getColumnIndex(COLUMN_DATE))));
		report.setDetail(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL)));
		report.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
		report.setPhotosetUrl(cursor.getString(cursor
				.getColumnIndex(COLUMN_PHOTOSET)));
		report.setTrailInfo((TrailInfo) trailInfoFactory.getObject(cursor,
				report.getTrailInfo()));
		return report;
	}

}
