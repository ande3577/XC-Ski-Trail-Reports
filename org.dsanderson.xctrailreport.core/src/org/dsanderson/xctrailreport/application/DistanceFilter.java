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

import org.dsanderson.xctrailreport.core.IReportFilter;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.Units;

/**
 * 
 */
public class DistanceFilter implements IReportFilter {
	int cutoff = Units.milesToMeters(50);

	public DistanceFilter() {
	}

	public DistanceFilter(int cutoff) {
		this.cutoff = cutoff;
	}

	public void setCutoff(int cutoff) {
		this.cutoff = cutoff;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IReportFilter#filterReport(org.dsanderson
	 * .xctrailreport.core.TrailReport)
	 */
	@Override
	public boolean filterReport(TrailReport report) {
		return report.getTrailInfo().getDistance() <= cutoff
				&& report.getTrailInfo().getDistanceValid();
	}

}
