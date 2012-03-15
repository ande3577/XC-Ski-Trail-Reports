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

import org.dsanderson.util.CompoundXmlParser;
import org.dsanderson.util.ICompoundXmlParserFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;

/**
 * 
 */
public class ThreeRiversParser implements ISourceSpecificInfoParser {
	private static final String INFO_URL = "infoUrl";

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
		String output = parser.getValue(INFO_URL);
		if (output != null && output.length() > 0) {
			ThreeRiversTrailInfo info = new ThreeRiversTrailInfo();
			info.setTrailInfoUrl(output);
			return info;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser#buildParser
	 * (org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo)
	 */
	@Override
	public CompoundXmlParser buildParser(ISourceSpecificTrailInfo info,
			ICompoundXmlParserFactory parserFactory) throws Exception {
		// three rivers cannot dynamically load trail info, so no point in
		// saving it off
		return null;
	}

}
