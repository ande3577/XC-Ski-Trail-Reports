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

import org.dsanderson.util.CompoundXmlParser;
import org.dsanderson.util.ICompoundXmlParserFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;

/**
 * 
 */
public class MorcParser implements ISourceSpecificInfoParser {
	private static final String SHORT_TRAIL_INFO_URL_KEY = "trailInfo";
	private static final String SHORT_COMPOSE_URL_KEY = "compose";
	private static final String SHORT_ALL_REPORT_URL_KEY = "allReports";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser#parse(org
	 * .dsanderson.util.CompoundXmlParser)
	 */
	@Override
	public ISourceSpecificTrailInfo parse(CompoundXmlParser parser)
			throws Exception {
		MorcSpecificTrailInfo info = new MorcSpecificTrailInfo();
		String url = null;

		if ((url = parser.getValue(SHORT_TRAIL_INFO_URL_KEY)) != null)
			info.setTrailInfoShortUrl(url);
		if ((url = parser.getValue(SHORT_COMPOSE_URL_KEY)) != null)
			info.setComposeShortUrl(url);
		if ((url = parser.getValue(SHORT_ALL_REPORT_URL_KEY)) != null)
			info.setAllReportShortUrl(url);
		
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser#buildParser
	 * (org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo,
	 * org.dsanderson.util.ICompoundXmlParserFactory)
	 */
	@Override
	public CompoundXmlParser buildParser(ISourceSpecificTrailInfo info,
			ICompoundXmlParserFactory parserFactory) throws Exception {
		MorcSpecificTrailInfo morcInfo = (MorcSpecificTrailInfo) info;
		CompoundXmlParser parser = parserFactory.newParser(MorcFactory.XML_TAG);
		parser.addParser(SHORT_TRAIL_INFO_URL_KEY, morcInfo.getTrailInfoShortUrl());
		parser.addParser(SHORT_COMPOSE_URL_KEY, morcInfo.getComposeShortUrl());
		parser.addParser(SHORT_ALL_REPORT_URL_KEY, morcInfo.getAllReportShortUrl());
		return parser;
	}
	
}
