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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dsanderson.util.IDistanceSource;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoPool;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.TrailReportPool;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 */
public class TrailInfoList implements ITrailInfoList {
	private final TrailReportList reportList;
	private final TrailReportPool reportPool;
	private final TrailInfoPool infoPool;
	private final ITrailInfoList defaultTrailInfo;

	public TrailInfoList(TrailReportList reportList,
			TrailReportPool reportPool, TrailInfoPool infoPool,
			ITrailInfoList defaultTrailInfo) {
		this.reportList = reportList;
		this.reportPool = reportPool;
		this.infoPool = infoPool;
		this.defaultTrailInfo = defaultTrailInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#add(org.dsanderson.
	 * xctrailreport.core.TrailInfo)
	 */
	public void add(TrailInfo info) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#get(int)
	 */
	public TrailInfo get(int index) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#find(java.lang.String)
	 */
	public TrailInfo find(String name) {
		return ((TrailReport) reportList.find(name)).getTrailInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(java.lang.Object)
	 */
	public void remove(TrailInfo arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#clear()
	 */
	public void clear() {
		// do nothing here, rely on the report list to clear
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#close()
	 */
	public void close() {
		// do nothing here, rely on the report list to close
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#getTimestamp()
	 */
	public Date getTimestamp() {
		return reportList.getTimestamp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#load()
	 */
	public void load() throws Exception {
		reportList.load();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(int)
	 */
	public void remove(int arg0) {
		reportList.remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#save()
	 */
	public void save() throws Exception {
		reportList.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#size()
	 */
	public int size() {
		SQLiteDatabase database = reportList.getDatabase();

		String[] column = { TrailInfoDatabaseFactory.COLUMN_NAME };
		Cursor cursor = database.query(true,
				TrailReportDatabaseFactory.TABLE_TEST, column, null, null,
				null, null, null, null);
		int size = cursor.getCount();
		cursor.close();
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#addIfNew(org.dsanderson
	 * .xctrailreport.core.TrailInfo)
	 */
	public void addIfNew(TrailInfo info) {
		// only add trail infos if they have a corresponding report
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#getAllLocations()
	 */
	public List<String> getAllLocations() {
		SQLiteDatabase database = reportList.getDatabase();

		String[] column = { TrailInfoDatabaseFactory.COLUMN_LOCATION };
		Cursor cursor = database.query(true,
				TrailReportDatabaseFactory.TABLE_TEST, column, null, null,
				null, null, null, null);
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
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#updateDistances(org.
	 * dsanderson.util.IDistanceSource, java.util.List)
	 */
	public void updateDistances(IDistanceSource distanceSource,
			List<String> locations) {

		List<Integer> distances = distanceSource.getDistances();
		List<Integer> durations = distanceSource.getDurations();
		List<Boolean> valids = distanceSource.getValids();

		if (distances.size() != locations.size())
			return;

		Cursor cursor = reportList.getUnfilteredCursor();
		if (cursor.moveToFirst()) {
			do {
				TrailReport report = (TrailReport) reportList.get(cursor);
				TrailInfo info = report.getTrailInfo();

				int index = locations.indexOf(info.getLocation());
				if (index >= 0) {
					info.setDistanceValid(valids.get(index));
					info.setDistance(distances.get(index));
					info.setDuration(durations.get(index));
				}
				reportList.update(report);
				reportPool.deleteItem(report);
				infoPool.deleteItem(info);
			} while (cursor.moveToNext());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#mergeIntoList(org.dsanderson
	 * .xctrailreport.core.TrailInfo)
	 */
	public TrailInfo mergeIntoList(TrailInfo newInfo) {
		TrailReport existingReport = reportList.find(newInfo.getName());
		// if a past report has already used this trail info, then me merge and readd
		if (existingReport != null) {
			TrailInfo existingInfo = existingReport.getTrailInfo();
			existingInfo.merge(newInfo);
			reportPool.deleteItem(existingReport);
			infoPool.deleteItem(newInfo);
			return existingInfo;
		} else {
			// if this is present in default info, then we merge together and add
			TrailInfo existingInfo = defaultTrailInfo.find(newInfo.getName());
			if (existingInfo != null) {
				existingInfo.merge(newInfo);
				reportPool.deleteItem(existingReport);
				infoPool.deleteItem(newInfo);
				return existingInfo;
			}
		}
		// do nothing else here, do not actually store an instance of a trail
		// info list
		return newInfo;
	}
}
