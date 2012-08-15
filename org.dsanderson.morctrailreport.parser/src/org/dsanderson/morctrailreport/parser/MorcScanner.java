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
package org.dsanderson.morctrailreport.parser;

import java.io.InputStream;
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
public class MorcScanner {
	private final TrailReportPool trailReportPool;
	private final TrailInfoPool trailInfoPool;
	private final MorcInfoPool morcInfoPool;
	private Scanner scanner;
	private TrailReport trailReport;
	private TrailInfo trailInfo;
	private MorcSpecificTrailInfo morcInfo;

	public final static String[] conditions = { "Dry", "Tacky", "Damp", "Wet", "Wet Do Not Ride",
			"Closed" };
	public final static int DEFAULT_CONDITIONS_INDEX = conditions.length;

	public MorcScanner(InputStream stream, TrailReportPool reportPool,
			TrailInfoPool infoPool, MorcInfoPool morcInfoPool) {
		trailReportPool = reportPool;
		trailInfoPool = infoPool;
		this.morcInfoPool = morcInfoPool;
		scanner = new Scanner(stream);
	}

	public TrailReport getTrailReport() {
		return trailReport;
	}

	public TrailInfo getTrailInfo() {
		return trailInfo;
	}

	public MorcSpecificTrailInfo getMorcSpecificInfo() {
		return morcInfo;
	}

	public boolean findRegion(String region) {
		if (findNext("\\Q<td><a name=\"" + region + "\"></a>\\E")) {
			return true;
		}
		return false;
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
			if (scanner.findInLine("\\Q<td class=\"alt0\" align=\"left\">\\E") != null)
				return false;
			else if (scanner.findInLine("\\Q<table width=\"100%\">\\E") != null)
				return true;
			else
				scanner.nextLine();
		}
		return true;
	}

	private void scanSingleReport() throws Exception {
		trailReport = trailReportPool.newItem();
		trailInfo = trailInfoPool.newItem();
		morcInfo = morcInfoPool.newItem();

		scanAllReportUrlandName();
		scanSummary();
		scanDetailed();
		scanAuthor();
		scanDate();
	}

	private void scanAllReportUrlandName() {

		if (!findNext("\\Q<a href=\"showthread.php?t=\\E"))
			return;
		String allReportUrl = scan("", "\"\\>", ".*");
		morcInfo.setAllReportShortUrl(allReportUrl);

		String name = scan("", "\\Q</a>\\E", ".*");
		trailInfo.setName(name);
		scanner.nextLine();
	}


	private void scanSummary() {
		if (!findNext("\\Q<a href=\"javascript:OpenWin\\E"))
			return;
		scanner.nextLine();
		String summary = scan("", "\\Q</a>\\E", ".*").trim();
		if (summary != null) {
			trailReport.setSummary(summary);
			trailReport.setSummaryPriority(getConditionPriority(summary));
		}
		scanner.nextLine();
	}

	private void scanDetailed() {
		if (!findNext("\\Q<td class=\"alt3\" align=\"left\">\\E"))
			return;

		String line;
		String detailed = "";
		if (!scanner.hasNextLine())
			return;

		do {
			line = scanner.nextLine();

			if (!line.isEmpty())
				detailed += line.trim() + " ";

		} while (scanner.hasNextLine() && !line.contains("</td>"));

		detailed = detailed.replaceAll("\\Q</td>\\E", "");
		trailReport.setDetail(detailed.trim());
	}

	private void scanAuthor() {
		if (!findNext("\\Q<a href=\"member.php?u=\\E"))
			return;
		String author = scan("\\Q\">\\E", "\\Q</a>\\E", ".*");
		trailReport.setAuthor(author);
		scanner.nextLine();
	}

	private void scanDate() {
		if (!findNext("\\Q<font size=\"-1\">\\E"))
			return;
		scanner.nextLine();
		String line = scanner.nextLine().trim();

		String split[] = line.split("[\\-\\:\\ ]", 6);
		if (split.length != 6)
			return;

		int month = Integer.parseInt(split[0]) - 1;
		assert (month >= 0);
		int day = Integer.parseInt(split[1]);
		int year = Integer.parseInt(split[2].replace(",", ""));

		int hour = Integer.parseInt(split[3]);
		int minute = Integer.parseInt(split[4]);
		String amPm = split[5];

		if (amPm.equals("PM")) {
			if (hour < 12)
				hour += 12;
		} else {
			if (hour == 12)
				hour = 0;
		}

		GregorianCalendar calendar = new GregorianCalendar(year, month, day,
				hour, minute);
		trailReport.setDate(new ReportDate(calendar.getTime()));

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

	private boolean findNext(String pattern) {
		return scanner.findWithinHorizon(pattern, 0) != null;
	}
	
	public static int getConditionPriority(String condition) {
		int priority = DEFAULT_CONDITIONS_INDEX;
		for (int i = 0; i < conditions.length; i++) {
			if (conditions[i].equals(condition)) {
				priority = i;
				break;
			}
		}
		return priority;
	}


}
