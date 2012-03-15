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


import org.dsanderson.util.CompoundXmlParser;
import org.dsanderson.util.ICompoundXmlParserFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;

/**
 * 
 */
public class SkinnyskiInfoParser implements ISourceSpecificInfoParser {
	private static final String INDEX = "index";
	private final SkinnyskiTrailInfoPool pool;

	/**
	 * 
	 */
	public SkinnyskiInfoParser(SkinnyskiTrailInfoPool skinnyskiPool) {
		pool = skinnyskiPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificInfoParser#parse(org
	 * .dsanderson.util.CompoundXmlParser)
	 */
	public ISourceSpecificTrailInfo parse(CompoundXmlParser parser)
			throws Exception {
		String parserOutput = parser.getValue(INDEX);
		if (parserOutput != null) {
			SkinnyskiSpecificInfo info = pool.newItem();
			info.setTrailIndex(Integer.parseInt(parserOutput));
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
	public CompoundXmlParser buildParser(ISourceSpecificTrailInfo info,
			ICompoundXmlParserFactory parserFactory) throws Exception {
		CompoundXmlParser parser = parserFactory
				.newParser(SkinnyskiFactory.SKINNYSKI_XML_TAG);
		parser.addParser(INDEX, Integer.toString(((SkinnyskiSpecificInfo) info)
				.getTrailIndex()));
		return parser;
	}
}
