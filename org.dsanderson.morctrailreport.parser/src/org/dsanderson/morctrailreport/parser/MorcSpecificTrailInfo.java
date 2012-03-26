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

import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.Merge;

/**
 * 
 */
public class MorcSpecificTrailInfo implements ISourceSpecificTrailInfo {
	private static final String ALL_REPORT_PREFIX = "http://www.morcmtb.org/forums/showthread.php?";
	private static final String TRAIL_COMPOSE_PREFIX = "http://www.morcmtb.org/forums/newreply.php?p=";
	private static final String TRAIL_COMPOSE_SUFFIX = "&noquote=1";
	private static final String TRAIL_INFO_PREFIX = "http://www.morcmtb.org/wiki/index.php?title=";

	private String shortTrailInfoUrl = null;
	private String shortAllReportUrl = null;
	private String shortComposeUrl = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#getSourceName
	 * ()
	 */
	@Override
	public String getSourceName() {
		return MorcFactory.SOURCE_NAME;
	}

	public String getAllTrailReportUrl() {
		if (shortAllReportUrl == null || shortAllReportUrl.length() == 0)
			return "";
		else
			return ALL_REPORT_PREFIX + shortAllReportUrl;
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
		if (shortComposeUrl == null || shortComposeUrl.length() == 0)
			return "";
		else
			return TRAIL_COMPOSE_PREFIX + shortComposeUrl
					+ TRAIL_COMPOSE_SUFFIX;
	}

	public String getShortInfoUrl() {
		return shortAllReportUrl;
	}

	public void setAllReportShortUrl(String url) {
		shortAllReportUrl = url;
	}

	public String getAllReportShortUrl() {
		return shortAllReportUrl;
	}

	public void setComposeShortUrl(String url) {
		shortComposeUrl = url;
	}

	public String getComposeShortUrl() {
		return shortComposeUrl;
	}

	public void setTrailInfoShortUrl(String url) {
		shortTrailInfoUrl = url;
	}

	public String getTrailInfoShortUrl() {
		return shortTrailInfoUrl;
	}

	public MorcSpecificTrailInfo reset() {
		shortAllReportUrl = null;
		shortComposeUrl = null;
		return this;
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
		if (shortTrailInfoUrl == null || shortTrailInfoUrl.length() == 0)
			return "";
		else if (shortTrailInfoUrl.startsWith("http://"))
			return shortTrailInfoUrl;
		else
			return TRAIL_INFO_PREFIX + shortTrailInfoUrl;
	}

	public void setShortTrailInfoUrl(String shortTrailInfoUrl) {
		this.shortTrailInfoUrl = shortTrailInfoUrl;
	}

	public String getShortTrailInfoUrl() {
		return shortTrailInfoUrl;
	}

	public void setShortComposeUrl(String shortComposeUrl) {
		this.shortComposeUrl = shortComposeUrl;
	}

	public String getShortComposeUrl() {
		return shortComposeUrl;
	}

	public void setShortAllReportUrl(String shortAllReportUrl) {
		this.shortAllReportUrl = shortAllReportUrl;
	}

	public String getShortAllReportUrl() {
		return shortAllReportUrl;
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
		MorcSpecificTrailInfo morcSpecific = (MorcSpecificTrailInfo) newInfo;
		shortTrailInfoUrl = Merge.merge(shortTrailInfoUrl,
				morcSpecific.shortTrailInfoUrl);
		shortComposeUrl = Merge.merge(shortComposeUrl,
				morcSpecific.shortComposeUrl);
		shortAllReportUrl = Merge.merge(shortAllReportUrl,
				morcSpecific.shortAllReportUrl);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#newItem()
	 */
	@Override
	public ISourceSpecificTrailInfo newItem() {
		return MorcFactory.getInstance().getTrailInfoPool().newItem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#deleteItem()
	 */
	@Override
	public void deleteItem() {
		MorcFactory.getInstance().getTrailInfoPool().deleteItem(this);
	}

}
