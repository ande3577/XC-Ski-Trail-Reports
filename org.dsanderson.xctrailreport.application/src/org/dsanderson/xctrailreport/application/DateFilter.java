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

import java.util.Date;

import org.dsanderson.xctrailreport.core.IReportFilter;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.Units;

/**
 * 
 */
public class DateFilter implements IReportFilter {
	int cutoff = 10;

	public DateFilter() {
	}

	public DateFilter(int cutoff) {
		this.cutoff = cutoff;
	}
	
	public void setAgeCutoff(int cutoff) {
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
		Date currentDate = new Date();
		long age = currentDate.getTime() - report.getDate().getDate().getTime();
		double ageDays = Units.millisecondsToDays(age);
		// add one to assume report was filed at 11:59 PM
		return (Math.floor(ageDays) <= (double) cutoff + 1);
	}

}
