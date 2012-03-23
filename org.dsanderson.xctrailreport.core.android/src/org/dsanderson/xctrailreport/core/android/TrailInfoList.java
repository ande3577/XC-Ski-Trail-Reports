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
import org.dsanderson.xctrailreport.core.TrailInfo;

import android.content.Context;

/**
 * 
 */
public class TrailInfoList extends GenericDatabase {

	public TrailInfoList(Context context, TrailInfoDatabaseFactory factory) {
		super(context, TrailInfoDatabaseFactory.DATABASE_NAME,
				TrailInfoDatabaseFactory.DATABASE_VERSION,
				TrailInfoDatabaseFactory.TABLE_TEST, factory,
				TrailInfoDatabaseFactory.COLUMN_NAME);
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
	public TrailInfo get(long index) {
		return (TrailInfo) super.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#find(java.lang.String)
	 */
	public TrailInfo find(String name) {
		return (TrailInfo) find(name);
	}

}
