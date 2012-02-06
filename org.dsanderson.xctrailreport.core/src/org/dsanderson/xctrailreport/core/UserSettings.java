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
package org.dsanderson.xctrailreport.core;

import java.util.List;

/**
 * 
 */
public class UserSettings {
	public enum SortMethod {
		SORT_BY_DISTANCE, SORT_BY_DATE
	}

	private SortMethod sortMethod = null;
	private boolean locationEnabled = false;
	private String defaultLocation = "55455";
	private boolean distanceFilterEnabled;
	private int filterDistance = (int) (50 * 1609.33);
	private boolean dateFilterEnabled;
	private int filterAge = 10;
	RegionManager regions = new RegionManager();

	public void setLocationEnabled(boolean locationEnabled) {
		this.locationEnabled = locationEnabled;
	}

	public boolean getLocationEnabled() {
		return locationEnabled;
	}

	public void setDefaultLocation(String location) {
		this.defaultLocation = location;
	}

	public String getDefaultLocation() {
		return defaultLocation;
	}

	public void setDistanceFilterEnabled(boolean enabled) {
		distanceFilterEnabled = enabled;
	}

	public boolean getDistanceFilterEnabled() {
		return distanceFilterEnabled;
	}

	public void setFilterDistance(int distance) {
		filterDistance = distance;
	}

	public int getFilterDistance() {
		return filterDistance;
	}

	public void setDateFilterEnabled(boolean enabled) {
		dateFilterEnabled = enabled;
	}

	public boolean getDateFilterEnabled() {
		return dateFilterEnabled;
	}

	public void setFilterAge(int age) {
		filterAge = age;
	}

	public int getFilterAge() {
		return filterAge;
	}

	public RegionManager getRegions() {
		return regions;
	}

	public void setSortMethod(SortMethod sortMethod) {
		this.sortMethod = sortMethod;
	}

	public SortMethod getSortMethod() {
		return sortMethod;
	}

}
