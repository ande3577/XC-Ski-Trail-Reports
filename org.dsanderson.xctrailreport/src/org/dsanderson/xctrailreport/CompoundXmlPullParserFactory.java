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
package org.dsanderson.xctrailreport;

import org.dsanderson.xctrailreport.core.CompoundXmlParser;
import org.dsanderson.xctrailreport.core.ICompoundXmlParserFactory;

/**
 * 
 */
public class CompoundXmlPullParserFactory implements ICompoundXmlParserFactory {
	static CompoundXmlPullParserFactory instance = null;

	static CompoundXmlPullParserFactory getInstance() {
		if (instance == null)
			instance = new CompoundXmlPullParserFactory();

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ICompoundParserFactory#newParser()
	 */
	public CompoundXmlParser newParser() {
		return new CompoundXmlPullParser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ICompoundParserFactory#newParser(java
	 * .lang.String)
	 */
	public CompoundXmlParser newParser(String name) {
		return new CompoundXmlPullParser(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ICompoundParserFactory#newParser(java
	 * .lang.String, java.lang.String)
	 */
	public CompoundXmlParser newParser(String name, String text) {
		return new CompoundXmlPullParser(name, text);
	}

}
