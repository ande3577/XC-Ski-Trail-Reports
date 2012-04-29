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

import org.dsanderson.util.Units;

/**
 * 
 */
public class UserSettings {
	public enum SortMethod {
		SORT_BY_DISTANCE, SORT_BY_DATE, SORT_BY_DURATION, SORT_BY_PHOTOSET, SORT_BY_CONDITION
	}

	public enum AutoRefreshMode {
		ALWAYS, NEVER, IF_OUT_OF_DATE
	}
	
	public enum DistanceMode {
		FULL, QUICK, DISABLED
	}

	private SortMethod sortMethod = SortMethod.SORT_BY_DATE;
	private boolean locationEnabled = false;
	private String defaultLocation = "Minneapolis, MN";
	private boolean distanceFilterEnabled = false;
	private int filterDistance = Units.milesToMeters(50);
	private boolean dateFilterEnabled = false;
	private int filterAge = 10;
	private boolean durationFilterEnabled = false;
	private int durationCutoff = 30;
	private boolean photosetFilterEnabled = false;
	private boolean redrawNeeded = false;
	private AutoRefreshMode autoRefreshMode = AutoRefreshMode.IF_OUT_OF_DATE;
	private long autoRefreshCutoff = Units.hoursToMilliseconds(2);
	private boolean forcedRefresh = false;
	private DistanceMode distanceMode = DistanceMode.FULL;

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
		redrawNeeded = true;
	}

	public boolean getDistanceFilterEnabled() {
		return distanceFilterEnabled;
	}

	public void setFilterDistance(int distance) {
		filterDistance = distance;
		redrawNeeded = true;
	}

	public int getFilterDistance() {
		return filterDistance;
	}

	public void setDateFilterEnabled(boolean enabled) {
		dateFilterEnabled = enabled;
		redrawNeeded = true;
	}

	public boolean getDateFilterEnabled() {
		return dateFilterEnabled;
	}

	public void setFilterAge(int age) {
		filterAge = age;
		redrawNeeded = true;
	}

	public int getFilterAge() {
		return filterAge;
	}

	public void setDurationFilterEnabled(boolean durationFilterEnabled) {
		this.durationFilterEnabled = durationFilterEnabled;
		redrawNeeded = true;
	}

	public boolean getDurationFilterEnabled() {
		return durationFilterEnabled;
	}

	public void setDurationCutoff(int durationCutoff) {
		this.durationCutoff = durationCutoff;
		redrawNeeded = true;
	}

	public int getDurationCutoff() {
		return durationCutoff;
	}
	
	public void setPhotsetFilterEnabled(boolean photosetFilterEnabled) {
		this.photosetFilterEnabled = photosetFilterEnabled;
		redrawNeeded = true;
	}
	
	public boolean getPhotosetFilterEnabled() {
		return photosetFilterEnabled;
	}

	public void setSortMethod(SortMethod sortMethod) {
		redrawNeeded = true;
		this.sortMethod = sortMethod;
	}

	public SortMethod getSortMethod() {
		return sortMethod;
	}

	public void setAutoRefreshMode(AutoRefreshMode autoRefreshMode) {
		this.autoRefreshMode = autoRefreshMode;
	}

	public AutoRefreshMode getAutoRefreshMode() {
		return autoRefreshMode;
	}

	public void setAutoRefreshCutoff(long autoRefreshCutoff) {
		this.autoRefreshCutoff = autoRefreshCutoff;
	}

	public long getAutoRefreshCutoff() {
		return autoRefreshCutoff;
	}

	public void setRedrawNeeded(boolean redrawNeeded) {
		this.redrawNeeded = redrawNeeded;
	}

	public boolean getRedrawNeeded() {
		return this.redrawNeeded;
	}
	
	public void setForcedRefresh(boolean forcedRefresh) {
		this.forcedRefresh = forcedRefresh;
	}
	
	public boolean getForcedRefresh() {
		return forcedRefresh;
	}

	public void setDistanceMode(DistanceMode distanceMode) {
		this.distanceMode = distanceMode;
	}
	
	public DistanceMode getDistanceMode() {
		return distanceMode;
	}
	
}
