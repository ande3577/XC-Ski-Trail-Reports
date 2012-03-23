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
package org.dsanderson.xctrailreport.threerivers;

import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.Merge;

/**
 * 
 */
public class ThreeRiversTrailInfo implements ISourceSpecificTrailInfo {
	String trailInfoUrl = null;
	private static final String TRAIL_URL_PREFIX = "http://www.threeriversparks.org/parks/";
	private static final String TRAIL_URL_EXTENSION = ".aspx";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#getSourceName
	 * ()
	 */
	@Override
	public String getSourceName() {
		return ThreeRiversFactory.SOURCE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#getTrailInfoUrl
	 * ()
	 */
	@Override
	public String getTrailInfoUrl() {
		if (trailInfoUrl == null)
			return "";
		else
			return TRAIL_URL_PREFIX + trailInfoUrl + TRAIL_URL_EXTENSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#getComposeUrl
	 * ()
	 */
	@Override
	public String getComposeUrl() {
		// three rivers does not allow user submitted reports
		return null;
	}

	public void setTrailInfoUrl(String url) {
		trailInfoUrl = url;
	}

	public String getTrailInfoShortUrl() {
		return trailInfoUrl;
	}

	public ThreeRiversTrailInfo reset() {
		trailInfoUrl = null;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#merge(org.
	 * dsanderson.xctrailreport.core.ISourceSpecificTrailInfo)
	 */
	@Override
	public void merge(ISourceSpecificTrailInfo newInfo) {
		trailInfoUrl = Merge.merge(trailInfoUrl,
				((ThreeRiversTrailInfo) newInfo).getTrailInfoShortUrl());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#newItem()
	 */
	@Override
	public ISourceSpecificTrailInfo newItem() {
		return ThreeRiversFactory.getInstance().getPool().newItem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#deleteItem()
	 */
	@Override
	public void deleteItem() {
		ThreeRiversFactory.getInstance().getPool().deleteItem(this);
	}

}
