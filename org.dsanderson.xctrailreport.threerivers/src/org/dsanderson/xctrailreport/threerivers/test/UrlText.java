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
package org.dsanderson.xctrailreport.threerivers.test;

/**
 * 
 */
public class UrlText {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/xml?origins=44.972691,-93.232541&destinations=45.1335,-93.441|44.992,-93.3222|Lake Elmo, MN|Minneapolis, MN&sensor=false";
		url = url.replaceAll("[ ]", "%20");
		System.out.println(url);
	}

}
