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
import org.dsanderson.util.IList;
import org.dsanderson.xctrailreport.core.TrailReport;

import android.content.Context;

/**
 * 
 */
public class TrailReportList extends GenericDatabase {

	public TrailReportList(Context context, TrailReportDatabaseFactory factory) {
		super(context, TrailReportDatabaseFactory.DATABASE_NAME,
				TrailReportDatabaseFactory.DATABASE_VERSION,
				TrailReportDatabaseFactory.TABLE_TEST, factory,
				TrailInfoDatabaseFactory.COLUMN_NAME);
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
	public TrailReport get(long index) {
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

}
