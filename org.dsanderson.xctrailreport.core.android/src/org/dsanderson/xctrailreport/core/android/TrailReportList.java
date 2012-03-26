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

import java.util.Date;

import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.xctrailreport.core.ITrailReportList;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.Units;
import org.dsanderson.xctrailreport.core.UserSettings;

import android.content.Context;

/**
 * 
 */
public class TrailReportList extends GenericDatabase implements
		ITrailReportList {

	public TrailReportList(Context context, TrailReportDatabaseFactory factory,
			int databaseVersion) {
		super(context, TrailReportDatabaseFactory.DATABASE_NAME,
				databaseVersion, TrailReportDatabaseFactory.TABLE_TEST,
				factory, TrailInfoDatabaseFactory.COLUMN_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IList#add(java.lang.Object)
	 */
	public void add(TrailReport report) {
		super.add(report);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IList#get(int)
	 */
	public TrailReport get(int index) {
		return (TrailReport) super.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IList#find(java.lang.String)
	 */
	public TrailReport find(String name) {
		return (TrailReport) super.find(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(java.lang.Object)
	 */
	public void remove(TrailReport arg0) {
		super.remove(arg0);
	}

	public void sort(UserSettings settings) {
		clearSortOrder();
		switch (settings.getSortMethod()) {
		case SORT_BY_DATE:
			addSortOrder(TrailReportDatabaseFactory.COLUMN_DATE, false);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DURATION, false);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DISTANCE, false);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_NAME, true);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_PHOTOSET, false);
			break;
		case SORT_BY_DISTANCE:
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DISTANCE, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DURATION, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_NAME, true);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_DATE, false);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_PHOTOSET, false);
			break;
		case SORT_BY_DURATION:
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DURATION, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DISTANCE, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_NAME, true);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_DATE, false);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_PHOTOSET, false);
			break;
		case SORT_BY_PHOTOSET:
			addSortOrder(TrailReportDatabaseFactory.COLUMN_PHOTOSET, false);
			addSortOrder(TrailReportDatabaseFactory.COLUMN_DATE, false);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DURATION, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_DISTANCE, true);
			addSortOrder(TrailInfoDatabaseFactory.COLUMN_NAME, true);
			break;
		}
		// always use the order added as a last tiebreaker
		addSortOrder(COLUMN_ID, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(long)
	 */
	public void remove(int arg0) {
		super.remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailReportList#filter(org.dsanderson
	 * .xctrailreport.core.UserSettings)
	 */
	public void filter(UserSettings settings) {
		clearFilter();
		if (settings.getDateFilterEnabled()) {
			Date date = new Date();
			addFilter(TrailReportDatabaseFactory.COLUMN_DATE
					+ ">"
					+ (date.getTime() - Units.daysToMilliseconds(settings
							.getFilterAge())));
		}
		if (settings.getDistanceFilterEnabled()) {
			addFilter(TrailInfoDatabaseFactory.COLUMN_DISTANCE + "<"
					+ settings.getFilterDistance());
		}
		if (settings.getDurationFilterEnabled()) {
			addFilter(TrailInfoDatabaseFactory.COLUMN_DURATION + "<"
					+ settings.getDurationCutoff());
		}
		if (settings.getPhotosetFilterEnabled()) {
			addFilter(TrailReportDatabaseFactory.COLUMN_PHOTOSET + "!=''");
		}
	}
}
