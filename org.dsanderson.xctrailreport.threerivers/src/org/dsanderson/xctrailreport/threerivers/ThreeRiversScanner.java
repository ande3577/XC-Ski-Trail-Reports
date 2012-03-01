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
package org.dsanderson.xctrailreport.threerivers;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import org.dsanderson.xctrailreport.core.ReportDate;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoPool;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.TrailReportPool;

/**
 * 
 */
public class ThreeRiversScanner {
	private final TrailReportPool trailReportPool;
	private final TrailInfoPool trailInfoPool;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
	private Scanner scanner;
	private TrailReport trailReport;
	private TrailInfo trailInfo;

	public ThreeRiversScanner(InputStream stream, TrailReportPool reportPool,
			TrailInfoPool infoPool) {
		trailReportPool = reportPool;
		trailInfoPool = infoPool;
		scanner = new Scanner(stream);
	}

	public TrailReport getTrailReport() {
		return trailReport;
	}

	public TrailInfo getTrailInfo() {
		return trailInfo;
	}

	public boolean scanRegion() throws Exception {
		if (!endOfRegion()) {
			scanSingleReport();
			return true;
		} else {
			return false;
		}
	}

	private boolean endOfRegion() {
		while (scanner.hasNextLine()) {
			if (scanner.findInLine("\\Q<h5>\\E") != null)
				return false;
			else if (scanner.findInLine("\\Q<strong>\\E") != null)
				return true;
			else
				scanner.nextLine();
		}
		return true;
	}

	private void scanSingleReport() throws Exception {
		trailReport = trailReportPool.newTrailReport();
		trailInfo = trailInfoPool.newTrailInfo();

		scanName();
		scanSummaryAndDate();
		// scanDetailed();
	}

	private void scanName() {
		String line = scanner.nextLine();
		String split[] = line.split("\\Q</h5>\\E");
		if (split.length > 0)
			trailInfo.setThreeRiversSearchTerm(split[0].trim());
	}

	private void scanSummaryAndDate() {
		String line;
		do {
			line = scanner.nextLine();
		} while (!line.contains("<p>"));
		String split[] = line.split("\\Q<p>\\E");
		if (split.length < 2)
			return;
		split = split[1].split("\\QUpdated \\E");
		if (split.length > 0)
			trailReport.setSummary(split[0]);
		if (split.length > 1) {
			split = split[1].split("\\Q</p>\\E");
			if (split.length > 0) {
				try {
					Date date = dateFormat.parse(split[0]);
					if (date != null)
						trailReport.setDate(new ReportDate(date.getTime()));
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}

	}

	private void scanDetailed() {

		while (scanner.hasNextLine()) {
			if (scanner.findInLine("\\Q<p>\\E") != null)
				scanner.nextLine();
			else if (scanner.findInLine("\\Q<em>\\E") != null) {
				String detailedString = scan("", "\\Q</em>\\E", ".*");
				trailReport.setDetail(detailedString);
				scanner.nextLine();
			} else
				break;
		}
	}

	private String scan(String start, String end, String target) {
		String result = null;

		result = scanner.findInLine(start + target + end);
		if (result != null) {
			if (start != null && !start.isEmpty()) {
				String results[] = result.split(start);
				if (results.length < 2)
					return null;

				result = results[1];
			}

			if (end != null && !end.isEmpty()) {
				String results[] = result.split(end);
				if (results.length < 1)
					return null;
				result = results[0];
			}
		}
		return result;
	}

}
