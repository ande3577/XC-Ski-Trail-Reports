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

import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;

/**
 * 
 */
public class SkinnyskiSpecificInfo implements ISourceSpecificTrailInfo {
	int trailIndex = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo#getSourceName
	 * ()
	 */
	@Override
	public String getSourceName() {
		return SkinnyskiFactory.SKINNYSKI_SOURCE_NAME;
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
		if (trailIndex < 0)
			return "";
		else
			return "http://skinnyski.com/trails/traildetail.asp?Id="
					+ trailIndex;
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
		if (trailIndex < 0)
			return null;
		else
			return "http://skinnyski.com/trails/trailreport.asp?trailId="
					+ trailIndex;
	}

	public void setTrailIndex(int index) {
		trailIndex = index;
	}
	
	public int getTrailIndex() {
		return trailIndex;
	}
	
	public SkinnyskiSpecificInfo reset() {
		trailIndex = -1;
		return this;
	}

}
