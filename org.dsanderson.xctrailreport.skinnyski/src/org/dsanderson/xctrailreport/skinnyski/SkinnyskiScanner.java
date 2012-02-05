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

import java.io.InputStream;
import java.util.Scanner;

import org.dsanderson.xctrailreport.core.ReportDate;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class SkinnyskiScanner {
	Scanner scanner;
	TrailReport trailReport;
	TrailInfo trailInfo;

	public SkinnyskiScanner(InputStream stream) {
		scanner = new Scanner(stream);
	}

	public TrailReport getTrailReport() {
		return trailReport;
	}

	public TrailInfo getTrailInfo() {
		return trailInfo;
	}

	public boolean findRegion(String region) {
		while (findNext("\\<font\\ssize\\=\\+1\\>\\<b\\>")) {
			String foundRegion;
			if ((foundRegion = scan("", "\\<\\/b\\>", ".*")) != null) {
				if (region.compareTo(foundRegion) == 0)
					return true;
			}
		}
		return false;
	}

	public boolean scanRegion() {
		if (!endOfRegion()) {
			scanSingleReport();
			return true;
		} else {
			return false;
		}
	}

	public boolean scanReport() {
		if (findNextReport()) {
			scanSingleReport();
			return true;
		} else {
			return false;
		}
	}

	private boolean findNextReport() {
		return findNext("\\<li\\>");
	}

	private boolean endOfRegion() {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains("<li>"))
				return false;
			else if (line.contains("<P>&nbsp;<p>"))
				return true;
		}
		return true;
	}

	private void scanSingleReport() {
		trailReport = new TrailReport();
		trailInfo = new TrailInfo();

		trailReport.setDate(new ReportDate(scanDate()));
		trailInfo.setSkinnyskiUrl(scanUrl());
		trailInfo.setSkinnyskiSearchTerm(scanName());
		trailInfo.setCity(scanCity());
		trailInfo.setState(scanState());
		trailReport.setSummary(scanSummary());
		trailReport.setDetail(scanDetailed());
		trailReport.setAuthor(scanAuthor());
	}

	private String scanDate() {
		String date;
		while ((date = scan("\\<b\\>", "-", ".*")) == null)
			scanner.nextLine();

		if (date != null)
			date = date.trim();

		return date;
	}

	private String scanUrl() {
		String url;
		if ((url = scan("<a\\b[^>]*href=\"", "\">", "[^>]*")) != null)
			return url;
		else
			return "";
	}

	private String scanName() {
		String name = null;
		if ((name = scan("", "\\<\\/a\\> \\(", ".*")) == null)
			name = scan("", "\\(", ".*");
		if (name != null)
			name = name.trim();
		return name;
	}

	private String scanCity() {
		String city = scan("", "\\)", ".*");
		scanner.nextLine();
		if (city != null)
			city.trim();
		return city;
	}

	private String scanState() {
		// / TODO need to determine state from skinnyski somehow
		return "MN";
	}

	private String scanSummary() {
		String summary = scan("Conditions\\:", "\\<br\\>", "[^\\<]*");
		if (summary != null)
			return summary.trim();
		else
			return "";

	}

	private String scanDetailed() {
		String detailedString = "";

		while (true) {
			if (scan("\\(", "\\)", ".*") != null) {
				break;
			} else if (scan("Photos\\:", "", ".*") != null) {
				break;
			} else {
				detailedString += scanner.nextLine();
			}
		}

		if (detailedString != null) {
			detailedString.replaceAll("\\<br\\>", "\r\n");
			detailedString.trim();
		}

		return detailedString;
	}

	private String scanAuthor() {
		String author;
		while (!(author = scanner.nextLine()).startsWith("("))
			;
		if (author != null) {
			String results[] = author.split("\\(");
			if (results.length < 2)
				return null;

			author = results[1];
			results = author.split("\\)");
			if (results.length < 1)
				return null;

			author = results[0];
			author.trim();
		}
		return author;
	}

	private boolean findNext(String pattern) {
		do {
			if (scanner.findInLine(pattern) != null)
				return true;
			else if (scanner.hasNextLine())
				scanner.nextLine();
			else
				return false;
		} while (true);
	}

	private String scan(String start, String end, String target) {
		String result = null;

		result = scanner.findInLine(start + target + end);
		if (result != null) {
			if (start != null && start.length() > 0) {
				String results[] = result.split(start);
				if (results.length < 2)
					return null;

				result = results[1];
			}

			if (end != null && end.length() > 0) {
				String results[] = result.split(end);
				if (results.length < 1)
					return null;
				result = results[0];
			}
		}
		return result;
	}

}
