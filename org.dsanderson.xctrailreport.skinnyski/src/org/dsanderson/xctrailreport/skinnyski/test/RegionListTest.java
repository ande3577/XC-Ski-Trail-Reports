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

import org.dsanderson.xctrailreport.skinnyski.RegionManager;

/**
 * 
 */
public class RegionListTest {
	public static void main(String[] args) {
		try {
			RegionManager regions = new RegionManager();

			System.out.println("Adding regions:");
			regions.add("Minnesota Northwest");
			regions.add("Minnesota Metro Area");
			regions.add("Michigan Upper Peninsula");

			printRegions(regions);

			System.out.println("Removing region:");
			regions.remove("Minnesota Northwest");
			printRegions(regions);
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	static void printRegions(RegionManager regions) {
		for (String region : regions.getRegions()) {
			System.out.println(region);
		}
	}
}
