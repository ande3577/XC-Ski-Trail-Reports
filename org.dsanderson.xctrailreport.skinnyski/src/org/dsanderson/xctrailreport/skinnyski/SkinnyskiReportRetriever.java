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

import java.io.BufferedReader;
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class SkinnyskiReportRetriever implements IReportRetriever {

	IAbstractFactory factory;

	public SkinnyskiReportRetriever(IAbstractFactory factory) {
		this.factory = factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.IReportRetriever#getReports(org.dsanderson.TrailInfo)
	 */
	public void getReports(List<TrailInfo> trailInfos) {
		parseHtml(trailInfos);
	}

	private void parseHtml(List<TrailInfo> trailInfos) {
		INetConnection netConnection = factory.getNetConnection();
		if (netConnection.connect("http://skinnyski.com/trails/reports.asp")) {
			BufferedReader reader = netConnection.getReader();
			String pageSource = netConnection.getString();
			for (TrailInfo info : trailInfos) {
				String matches[] = pageSource.split(info
						.getSkinnyskiSearchTerm());
				if (matches.length > 1) {
					TrailReport newReport = new TrailReport();

					for (int i = 1; i < matches.length; i++) {
						String dateMatches[] = matches[i - 1].split("<b>");
						if (dateMatches.length > 0) {
							String date = dateMatches[dateMatches.length - 1];
							date = date.substring(0, date.indexOf('-')).trim();
							newReport.setDate(date);
						}

						String items[] = matches[i].split("<br>", 4);
						if (items.length == 4) {
							newReport.setSummary(items[1].trim());
							newReport.setDetail(items[2].trim());
							String author = items[3];
							author = author
									.substring(0, author.indexOf("<li>"));
							author = author.replace("(", "").replace(")", "")
									.trim();
							newReport.setAuthor(author);
						}
						newReport.setSource("Skinnyski");
						info.getReports().add(newReport.copy());
					} // for matches
				} // if (matches.length > 2)
			} // for TrailInfos

		} // if (connect())
	} // parseHtml

}
