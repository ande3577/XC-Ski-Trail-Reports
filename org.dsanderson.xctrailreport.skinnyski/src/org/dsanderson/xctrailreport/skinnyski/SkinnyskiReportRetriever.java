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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

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
		List<TrailReport> trailReports = new ArrayList<TrailReport>();
		if (connected) {
			parseHtml(trailReports, trailInfo);
		}
		return trailReports;
	}

	private boolean connect() {
		pageSource = "";
		boolean retVal = false;
		try {
			URL url = new URL("http://skinnyski.com/trails/reports.asp");
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());

			byte b[] = new byte[in.available()];
			if (in.read(b) > 0) {
				String newString = new String(b);
				pageSource = newString;
				retVal = true;
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return retVal;
	}

	private void parseHtml(List<TrailReport> trailReports, TrailInfo trailInfo) {
		String matches[] = pageSource.split(trailInfo.getSkinnyskiSearchTerm());
		if (matches.length > 2) {
			TrailReport newReport = new TrailReport();
			for (int i = 1; i < matches.length; i++) {
				String dateMatches[] = matches[i - 1].split("<b>");
				if (dateMatches.length > 0) {
					String date = dateMatches[dateMatches.length - 1];
					date = date.split("<a", 1)[0];
					newReport.setDate(date);
				}

				String items[] = matches[i].split("<br>", 4);
				newReport.setSummary(items[0].trim());
				newReport.setDetail(items[1].trim());
				String author = items[2].split("<li>", 1)[0].trim();
				newReport.setAuthor(author);
			}
		}

	}

}
