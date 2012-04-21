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
package org.dsanderson.xctrailreport.application;

import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.dsanderson.util.IDistanceSource;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoParser;

/**
 * 
 */
public class DefaultTrailInfoList implements ITrailInfoList {

	private final IAbstractFactory factory;
	private final IReportReaderFactory readerFactory;

	HashMap<String, TrailInfo> defaultTrailInfos = new HashMap<String, TrailInfo>();

	public DefaultTrailInfoList(IAbstractFactory factory,
			IReportReaderFactory readerFactory) {
		this.factory = factory;
		this.readerFactory = readerFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#add(java.lang.Object)
	 */
	@Override
	public void add(TrailInfo object) {
		defaultTrailInfos.put(object.getName(), object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(java.lang.Object)
	 */
	@Override
	public void remove(TrailInfo object) {
		defaultTrailInfos.remove(object.getName());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#remove(int)
	 */
	@Override
	public void remove(int index) {
		// cannot remove by index
		assert (false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#get(int)
	 */
	@Override
	public TrailInfo get(int index) {
		// cannot get by index
		assert (false);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#find(java.lang.String)
	 */
	@Override
	public TrailInfo find(String name) {
		return defaultTrailInfos.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#load()
	 */
	@Override
	public void load() throws Exception {
		Reader reader = readerFactory.newDefaultTrailInfoReader();
		TrailInfoParser parser = factory.newTrailInfoParser();

		parser.parse(reader);
		for (TrailInfo info : parser.getTrailInfo()) {
			add(info);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#save()
	 */
	@Override
	public void save() throws Exception {
		// default trail info is read only
		assert(false);
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
		// default timestamp always returns current date
		return new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#clear()
	 */
	@Override
	public void clear() {
		defaultTrailInfos.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IList#size()
	 */
	@Override
	public int size() {
		return defaultTrailInfos.size();
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
		// cannot merge
		assert(false);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITrailInfoList#getAllLocations()
	 */
	@Override
	public List<String> getAllLocations() {
		// cannot get locations directly, need to go through trail info
		assert(false);
		return null;
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
		// cannot set distances directly, need to go through trail info
		assert(false);
	}

}
