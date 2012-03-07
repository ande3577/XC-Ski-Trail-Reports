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
package org.dsanderson.xctrailreport.core;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class TrailReportParser {
	private List<TrailReport> trailReports;
	private final ICompoundXmlParserFactory parserFactory;
	private final TrailReportPool trailReportPool;

	private final String TRAIL_REPORTS = "trailReports";
	private final String REPORT = "report";

	private String SUMMARY = "summary";
	private String AUTHOR = "author";
	private String DATE = "date";
	private String DETAIL = "detail";
	private String SOURCE = "source";
	private String TRAIL_NAME = "trailName";
	private String PHOTO_URL = "photosetURL";

	/**
	 * 
	 */
	public TrailReportParser(ICompoundXmlParserFactory parserFactory,
			TrailReportPool trailReportPool) {
		this.parserFactory = parserFactory;
		this.trailReportPool = trailReportPool;
		trailReports = new ArrayList<TrailReport>();
	}

	public List<TrailReport> getReports() {
		return trailReports;
	}

	public void setReports(List<TrailReport> trailReports) {
		this.trailReports.clear();
		for (TrailReport report : trailReports) {
			this.trailReports.add(report);
		}
	}

	public void parse(Reader reader, List<TrailInfo> trailInfo)
			throws Exception {
		CompoundXmlParser parser = parserFactory.newParser();
		parser.parse(reader);
		List<CompoundXmlParser> reportParsers = parser.getParsers(TRAIL_REPORTS
				+ ":" + REPORT);
		for (CompoundXmlParser reportParser : reportParsers) {
			TrailReport report = trailReportPool.newTrailReport();
			String parserOutput;
			if ((parserOutput = reportParser.getValue(SUMMARY)) != null) {
				report.setSummary(parserOutput);
			}
			if ((parserOutput = reportParser.getValue(AUTHOR)) != null) {
				report.setAuthor(parserOutput);
			}
			if ((parserOutput = reportParser.getValue(DATE)) != null) {
				report.setDate(new ReportDate(Long.parseLong(parserOutput)));
			}
			if ((parserOutput = reportParser.getValue(DETAIL)) != null) {
				report.setDetail(parserOutput);
			}
			if ((parserOutput = reportParser.getValue(SOURCE)) != null) {
				report.setSource(parserOutput);
			}
			if((parserOutput = reportParser.getValue(PHOTO_URL)) != null) {
				report.setPhotosetUrl(parserOutput);
			}
			if ((parserOutput = reportParser.getValue(TRAIL_NAME)) != null) {
				for (TrailInfo info : trailInfo) {
					if (info.getName().equals(parserOutput)) {
						report.setTrailInfo(info);
						trailReports.add(report);
						break;
					}
				}
			}
		}
	}

	public void write(Writer writer) throws Exception {
		CompoundXmlParser xmlBuilder = parserFactory.newParser(TRAIL_REPORTS);
		for (TrailReport report : trailReports) {
			CompoundXmlParser reportParser = parserFactory.newParser(REPORT);
			reportParser.addParser(SUMMARY, report.getSummary());
			reportParser.addParser(AUTHOR, report.getAuthor());
			reportParser.addParser(DATE,
					Long.toString(report.getDate().getDate().getTime()));
			reportParser.addParser(DETAIL, report.getDetail());
			reportParser.addParser(SOURCE, report.getSource());
			reportParser.addParser(PHOTO_URL, report.getPhotosetUrl());
			reportParser.addParser(TRAIL_NAME, report.getTrailInfo().getName());
			xmlBuilder.addParser(reportParser);
		}
		xmlBuilder.write(writer);
	}

}
