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

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.xctrailreport.application.IReportReaderFactory;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoParser;
import org.dsanderson.xctrailreport.core.TrailInfoPool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 */
public class TrailInfoList extends GenericDatabase implements ITrailInfoList {
	private final TrailInfoPool infoPool;
	private final IAbstractFactory factory;
	private final IReportReaderFactory readerFactory;

	public TrailInfoList(Context context, IAbstractFactory factory,
			TrailInfoDatabaseFactory databasefactory,
			IReportReaderFactory readerFactory, TrailInfoPool infoPool,
			String databaseName, int databaseVersion) {
		super(context, databaseName, databaseVersion,
				TrailInfoDatabaseFactory.TABLE_TEST, databasefactory,
				TrailInfoDatabaseFactory.COLUMN_NAME);
		this.infoPool = infoPool;
		this.factory = factory;
		this.readerFactory = readerFactory;
	}

	public void open() throws Exception {
		super.open();

		Reader reader = readerFactory.newDefaultTrailInfoReader();
		TrailInfoParser parser = factory.newTrailInfoParser();

		parser.parse(reader);
		for (TrailInfo info : parser.getTrailInfo()) {
			addIfNew(info);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#add(org.dsanderson.
	 * xctrailreport.core.TrailInfo)
	 */
	public void add(TrailInfo info) {
		super.add(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#get(int)
	 */
	public TrailInfo get(int index) {
		return (TrailInfo) super.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#find(java.lang.String)
	 */
	public TrailInfo find(String name) {
		return (TrailInfo) super.find(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(java.lang.Object)
	 */
	public void remove(TrailInfo arg0) {
		super.remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#addIfNew(org.dsanderson
	 * .xctrailreport.core.TrailInfo)
	 */
	public void addIfNew(TrailInfo info) {
		if (find(info.getName()) == null)
			super.add(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#getAllLocations()
	 */
	public List<String> getAllLocations() {
		SQLiteDatabase database = getDatabase();

		String[] column = { TrailInfoDatabaseFactory.COLUMN_LOCATION };
		Cursor cursor = database.query(true,
				TrailInfoDatabaseFactory.TABLE_TEST, column, null, null, null,
				null, null, null);
		List<String> locations = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			do {
				locations
						.add(cursor.getString(cursor
								.getColumnIndex(TrailInfoDatabaseFactory.COLUMN_LOCATION)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return locations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#mergeIntoList(org.dsanderson
	 * .xctrailreport.core.TrailInfo)
	 */
	public TrailInfo mergeIntoList(TrailInfo newInfo) {
		TrailInfo existingInfo = find(newInfo.getName());
		// if a past report has already used this trail info, then me merge and
		// readd
		if (existingInfo != null) {
			existingInfo.merge(newInfo);
			infoPool.deleteItem(newInfo);
			return existingInfo;
		} else {
			// otherwise add the new item to the list
			add(newInfo);
		}
		return newInfo;
	}

}
