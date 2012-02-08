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

import java.util.Scanner;

/**
 * 
 */
public class RegexTest {

	static Scanner scanner;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String input = " <a href=\"traildetail.asp?Id=588\">Green Acres Recreation</a> (Lake Elmo):</b><br>";
//		String patternArray[] = { "<a\\b[^>]*href=\"", "\">", "[^>]*"};
		String patternArray[] = { "\\Q<a href=\"traildetail.asp?Id=\\E", "\">", "[\\d]*"};
		String pattern = patternArray[0] + patternArray[2] + patternArray[1];
		Scanner scanner = new Scanner(input);
		String result = input;
		if (scanner.findInLine(pattern) != null)
			result += " contains ";
		else
			result += " doesn't contain ";

		result += pattern;
		System.out.println(result);
	}
	
}
