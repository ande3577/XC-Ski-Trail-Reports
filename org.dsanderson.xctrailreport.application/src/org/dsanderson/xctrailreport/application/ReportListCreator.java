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
package org.dsanderson.xctrailreport.application;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportFilter;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ITrailInfoParser;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class ReportListCreator {
	private IAbstractFactory factory;

	public ReportListCreator(IAbstractFactory factory) {
		this.factory = factory;
	}

	public List<TrailReport> getTrailReports(InputStream inputStream)
			throws Exception {

		ITrailInfoParser parser = factory.getTrailInfoParser();
		parser.SetInputStream(inputStream);
		parser.parse();

		List<TrailInfo> trailInfos = parser.getTrailInfo();
		List<TrailReport> trailReports = new ArrayList<TrailReport>();

		IReportRetriever reportRetriever = factory.getReportRetriever();
		reportRetriever.getReports(trailReports, trailInfos);

		DistanceHandler directionHandler = new DistanceHandler(factory);
		directionHandler.getDistances(trailInfos);

		return trailReports;
	}

	public List<TrailReport> sortTrailReports(List<TrailReport> trailReports) {
		Collections.sort(trailReports, new CompoundReportComparator(factory
				.getUserSettings().getSortMethod()));
		return trailReports;
	}

	public List<TrailReport> filterTrailReports(List<TrailReport> trailReports) {
		IReportFilter filter = factory.getFilter();
		List<TrailReport> filteredReports = new ArrayList<TrailReport>();
		for (TrailReport report : trailReports) {
			if (filter.filterReport(report))
				filteredReports.add(report.copy());
		}

		return filteredReports;
	}

}
