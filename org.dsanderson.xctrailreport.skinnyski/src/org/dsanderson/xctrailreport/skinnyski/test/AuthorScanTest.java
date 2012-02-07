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
package org.dsanderson.xctrailreport.skinnyski.test;

/**
 * 
 */
public class AuthorScanTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String line = "(Jon Schaumann)";
		scanForAuthor(line);

		line = "(Dave Anderson, Elk River Nordic Ski Club)";
		scanForAuthor(line);

		line = "(David Byrne, <a href=\"http://www.mnrovers.org\" onclick=\"log_external_link(this, 'External Links', 'Section - Trail Reports');return false;\">Minnesota Rovers Outdoors Club</a>)";
		scanForAuthor(line);

	}

	static String scanForAuthor(String line) {
		String author = "";
		String url = "";

		if (line.startsWith("(")) {
			if (line.contains("a href=\"")) {
				String split[] = line.split("^\\(");
				if (split.length < 2)
					return author;

				author = split[1];
				split = author.split("\\Q<a href=\"\\E");
				author = split[0];

				if (split.length > 1) {
					url = split[1];
					split = url.split("[\\<\\>]");
					if (split.length > 1)
						author += split[1];

					split = url.split("\"");
					url = split[0];

				} else {
					split = author.split("\\)");
					author = split[0];
				}
			} else {
				String split[] = line.split("[\\(\\)]");
				if (split.length >= 2)
					author = split[1];
				else
					author = line;
			}
		}
		System.out.println(author + "; " + url);
		return author;
	}
}
