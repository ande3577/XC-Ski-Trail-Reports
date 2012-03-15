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
package org.dsanderson.xctrailreport.skinnyski;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;

/**
 * 
 */
public abstract class SkinnyskiFactory implements ISourceSpecificFactory {
	static SkinnyskiFactory instance = null;
	public static final String SKINNYSKI_SOURCE_NAME = "Skinnyski";
	static final String DEFAULT_SKINNYSKI_REPORT_URL = "http://skinnyski.com/trails/trailselect.asp?reportType=X";
	static final String DEFAULT_SKINNYSKI_REQUEST_URL = "http://www.skinnyski.com/trails/trailreportrequest.asp";
	static final String SKINNYSKI_XML_TAG = "skinnyski";
	private final IAbstractFactory factory;
	private boolean enabled = false;

	private SkinnyskiReportRetriever retriever = null;
	private SkinnyskiTrailInfoPool infoPool = null;
	private SkinnyskiInfoParser parser = null;
	RegionManager regions = new RegionManager();

	public SkinnyskiFactory(IAbstractFactory factory) {
		assert (instance == null);
		instance = this;
		this.factory = factory;
		
	}

	static public SkinnyskiFactory getInstance() {
		assert (instance != null);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getSourceName()
	 */
	@Override
	public String getSourceName() {
		return SKINNYSKI_SOURCE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getDefaultComposeUrl
	 * ()
	 */
	@Override
	public String getDefaultComposeUrl() {
		return DEFAULT_SKINNYSKI_REPORT_URL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getDefaultRequestUrl
	 * ()
	 */
	@Override
	public String getDefaultRequestUrl() {
		return DEFAULT_SKINNYSKI_REQUEST_URL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getReportRetriever
	 * ()
	 */
	@Override
	public IReportRetriever getReportRetriever() {
		if (retriever == null) {
			retriever = new SkinnyskiReportRetriever(factory, this);
		}
		return retriever;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#
	 * getSourceSpecificXmlKey()
	 */
	@Override
	public String getSourceSpecificXmlKey() {
		return SKINNYSKI_XML_TAG;
	}
	
	public SkinnyskiTrailInfoPool getTrailInfoPool() {
		if (infoPool == null)
			infoPool = new SkinnyskiTrailInfoPool();
		return infoPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#
	 * getSourceSpecificParser()
	 */
	@Override
	public ISourceSpecificInfoParser getSourceSpecificParser() {
		if (parser == null)
			parser = new SkinnyskiInfoParser(getTrailInfoPool());
		return parser;
	}
	
	abstract public IUserSettingsSource getUserSettingsSource();
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public RegionManager getRegions() {
		return regions;
	}
	
	

}
