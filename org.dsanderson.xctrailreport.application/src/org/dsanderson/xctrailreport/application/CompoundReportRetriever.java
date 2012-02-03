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

import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class CompoundReportRetriever implements IReportRetriever {

	List<IReportRetriever> reportRetrievers = new ArrayList<IReportRetriever>();

	public CompoundReportRetriever() {
	}

	public void addRetriever(IReportRetriever reportRetriever) {
		reportRetrievers.add(reportRetriever);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.IReportRetriever#getReports(org.dsanderson.TrailInfo)
	 */
	@Override
	public void getReports(List<TrailReport>  trailReports, List<TrailInfo> trailInfo) {
		for (int i = 0; i < reportRetrievers.size(); i++) {
			reportRetrievers.get(i).getReports(trailReports, trailInfo);
		}
	}

}
