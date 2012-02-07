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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class RegionManager {

	static public final String supportedRegions[] = { "Minnesota Metro Area",
			"Minnesota Northeast", "Minnesota Northwest", "Minnesota Central",
			"Minnesota Southern", "Wisconsin Northwest", "Wisconsin Northeast",
			"Wisconsin Southwest", "Wisconsin Southeast",
			"Michigan Upper Peninsula", "Iowa", "Canada", "Illinois",
			"North Dakota", "United States" };

	private List<String> regions = new ArrayList<String>();

	public List<String> getRegions() {
		return regions;
	}

	public void clear() {
		regions.clear();
	}

	public void add(String region) {
		// don't do anything if already in list
		if (!regions.contains(region)) {
			int supportedIndex = findSupportedIndex(region);
			if (supportedIndex == supportedRegions.length)
				throw new RuntimeException("Invalid region : " + region);
			for (int i = 0; i < regions.size(); i++) {
				if (supportedIndex < findSupportedIndex(regions.get(i))) {
					regions.add(i, region);
					return;
				}
			}
			regions.add(region);
		}
	}

	public void remove(String region) {
		regions.remove(region);
	}

	private int findSupportedIndex(String region) {
		for (int i = 0; i < supportedRegions.length; i++) {
			if (supportedRegions[i].compareTo(region) == 0) {
				return i;
			}
		}
		return supportedRegions.length;
	}
}
