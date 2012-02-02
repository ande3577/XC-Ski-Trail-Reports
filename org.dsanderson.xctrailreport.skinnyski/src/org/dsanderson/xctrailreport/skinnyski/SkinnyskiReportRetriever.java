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
import java.util.ArrayList;
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
			try {
				BufferedReader reader = netConnection.getReader();
				String line;

				TrailReport newTrailReport = null;
				String reportName = null;

				while ((line = reader.readLine()) != null) {
					if (line.startsWith("<li>")) { // start of report
						newTrailReport = new TrailReport();
						reportName = "";
					} else if (newTrailReport == null) { // still waiting for
															// start of report
						// do nothing if new trail report is null
					} else if (line.startsWith("<img")) {
						// ignore images
					} else if (line.startsWith("<b>") && newTrailReport != null) { // date/name
						String dateString = line.substring(line.indexOf("<b>")
								+ "<b>".length());
						dateString = line.split("-")[0].trim();
						dateString = dateString.replace("<b>", "");
						newTrailReport.setDate(dateString);

						String splitStrings[] = line.split("-", 2);
						if (splitStrings.length >= 2) {
							reportName = splitStrings[1];
							if (reportName.indexOf("<a href=") != -1) {
								reportName = reportName.substring(reportName
										.indexOf('>'));
								int endingIndex = reportName.indexOf("</a>");
								if (endingIndex != -1)
									reportName = reportName.substring(0,
											endingIndex);
								reportName = reportName.replace(">", "").trim();
							} else {
								reportName = reportName.split("[(]", 1)[0];
								reportName = reportName.trim();
							}
						}
					} else if (line.startsWith("Conditions: ")) { // summary/detail
						String summaryString = line.substring("Conditions: "
								.length());
						String splitStrings[] = summaryString.split("<br>");
						summaryString = splitStrings[0].trim();

						newTrailReport.setSummary(summaryString);

						String detailString = "";

						for (int i = 1; i < splitStrings.length; i++) {
							if (detailString.length() > 0)
								detailString += "\r\n";

							detailString += splitStrings[i].trim();
						}
						newTrailReport.setDetail(detailString);
					} else if (line.startsWith("Photos: <a onClick=")) { // photos
						// ignore lines with photo
					} else if (line.startsWith("(")) { // author
						String authorString = line.replace("(", "").replace(
								")", "");
						authorString = authorString.split("<")[0];
						newTrailReport.setAuthor(authorString);

						newTrailReport.setSource("Skinnyski");
						for (TrailInfo info : trailInfos) {
							// look for a matching trail info and add the report
							if (reportName.indexOf(info
									.getSkinnyskiSearchTerm()) != -1)
								info.addReport(newTrailReport);
						}
						// reset to wait for start of next
						newTrailReport = null;
						reportName = "";
					} else // additional detailed report
					{
						String detailString = newTrailReport.getDetail();
						// replace brs with endlines, then eliminate them if at
						// beginning or end
						line.replace("<br>", "\r\n");
						line = line.trim();
						// if we're adding to a previous report, then add an
						// endline
						if (detailString.length() != 0 && line.length() != 0)
							detailString += "\r\n";

						detailString += line;
						newTrailReport.setDetail(detailString);
					}

				} // while ((line = reader.readLine()) != null)
			} catch (Exception e) {
				System.err.println(e);
				trailInfos = new ArrayList<TrailInfo>();
			} // catch
			finally {
				netConnection.disconnect();
			}

		} // if
			// (netConnection.connect("http://skinnyski.com/trails/reports.asp"))
	} // parseHtml
}
