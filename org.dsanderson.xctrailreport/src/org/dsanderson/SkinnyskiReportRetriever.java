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
package org.dsanderson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 
 */
public class SkinnyskiReportRetriever implements IReportRetriever {

	String pageSource = "";
	boolean connected = false;

	public SkinnyskiReportRetriever() {
		connected = connect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.IReportRetriever#getReports(org.dsanderson.TrailInfo)
	 */
	public List<TrailReport> getReports(TrailInfo trailInfo) {
		if (!connected) {
			return null;
		}

		return null;
	}

	private boolean connect() {
		InputStream in;
		pageSource = "";
		boolean retVal = false;
		try {
			URL url = new URL("http://skinnyski.com/trails/reports.asp");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(2000);
			urlConnection.setReadTimeout(2000);
			in = new BufferedInputStream(urlConnection.getInputStream());
			if (in.read(pageSource.getBytes()) > 0) {
				retVal = true;
			}
			in.close();
		} catch (Exception e) {
			return false;
		}
		return retVal;
	}
}
