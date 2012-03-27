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
package org.dsanderson.morctrailreport.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dsanderson.util.IDistanceSource;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.TrailInfo;

/**
 * 
 */
public class SingleTrailInfoList implements ITrailInfoList {
	private TrailInfo info;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#add(java.lang.Object)
	 */
	@Override
	public void add(TrailInfo object) {
		info = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(java.lang.Object)
	 */
	@Override
	public void remove(TrailInfo object) {
		if (object == info)
			info = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(int)
	 */
	@Override
	public void remove(int index) {
		if (index == 0)
			info = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#get(int)
	 */
	@Override
	public TrailInfo get(int index) {
		if (index == 0)
			return info;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#find(java.lang.String)
	 */
	@Override
	public TrailInfo find(String name) {
		if (name.equals(info.getName()))
			return info;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#load()
	 */
	@Override
	public void load() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#save()
	 */
	@Override
	public void save() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#close()
	 */
	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#getTimestamp()
	 */
	@Override
	public Date getTimestamp() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#clear()
	 */
	@Override
	public void clear() {
		info = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#size()
	 */
	@Override
	public int size() {
		if (info == null)
			return 0;
		else
			return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#mergeIntoList(org.dsanderson
	 * .xctrailreport.core.TrailInfo)
	 */
	@Override
	public TrailInfo mergeIntoList(TrailInfo info) {
		if (info.getName().equals(info.getName())) {
			this.info.merge(info);
			return this.info;
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#getAllLocations()
	 */
	@Override
	public List<String> getAllLocations() {
		List<String> list = new ArrayList<String>();
		list.add(info.getLocation());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoList#updateDistances(org.
	 * dsanderson.util.IDistanceSource, java.util.List)
	 */
	@Override
	public void updateDistances(IDistanceSource distanceSource,
			List<String> locations) {
	}

}
