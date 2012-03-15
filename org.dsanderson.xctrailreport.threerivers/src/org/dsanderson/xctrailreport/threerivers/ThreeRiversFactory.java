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

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser;

/**
 * 
 */
public abstract class ThreeRiversFactory implements ISourceSpecificFactory {
	public static final String SOURCE_NAME = "Three Rivers Park District";
	private static final String XML_TAG = "threeRivers";
	
	private static ThreeRiversFactory instance = null;
	
	private final IAbstractFactory factory;
	private ThreeRiversReportRetriever retriever = null;
	private ThreeRiversParser parser = null;
	
	public ThreeRiversFactory(IAbstractFactory factory) {
		assert(instance == null);
		instance = this;
		this.factory = factory;
	}
	
	static public ThreeRiversFactory getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getSourceName()
	 */
	@Override
	public String getSourceName() {
		return SOURCE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getDefaultComposeUrl()
	 */
	@Override
	public String getDefaultComposeUrl() {
		// three rivers does not allow user trail reports.
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getDefaultRequestUrl()
	 */
	@Override
	public String getDefaultRequestUrl() {
		// three rivers does not allow user report requests
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getReportRetriever()
	 */
	@Override
	public IReportRetriever getReportRetriever() {
		if (retriever == null)
			retriever = new ThreeRiversReportRetriever(factory);
		return retriever;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getSourceSpecificXmlKey()
	 */
	@Override
	public String getSourceSpecificXmlKey() {
		return XML_TAG;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.core.ISourceSpecificFactory#getSourceSpecificParser()
	 */
	@Override
	public ISourceSpecificInfoParser getSourceSpecificParser() {
		if (parser == null)
			parser = new ThreeRiversParser();
		return parser;
	}
}
