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
package org.dsanderson.xctrailreport.core.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsanderson.xctrailreport.core.ReportDate;

/**
 * 
 */
public class DateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			List<ReportDate> dates = new ArrayList<ReportDate>();
			dates.add(new ReportDate(ReportDate.monthDayYearToTime("Mar", 6, 2012)));
			dates.add(new ReportDate());
			dates.add(new ReportDate(ReportDate.monthDayYearToTime("Feb", 4, 2012)));
			dates.add(new ReportDate(ReportDate.monthDayYearToTime("May", 23, 2011)));
			dates.add(new ReportDate(ReportDate.stringToDate("Mar 23")));
			Collections.sort(dates);

			for (ReportDate date : dates)
				System.out.println(date.formatDate());
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
