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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class TrailInfo {
	String name = "";
	String city = "";
	String state = "MN";
	String location = "";
	int distance = 0;
	int travelTime = 0;
	boolean directionsValid = false;
	List<ISourceSpecificTrailInfo> sourceSpecificInfo = new ArrayList<ISourceSpecificTrailInfo>();

	public TrailInfo() {
		reset();
	}

	public TrailInfo reset() {
		name = null;
		city = null;
		state = null;
		location = null;
		distance = 0;
		travelTime = 0;
		directionsValid = false;
		sourceSpecificInfo = new ArrayList<ISourceSpecificTrailInfo>();
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		if (name == null)
			return "";
		else
			return name;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		if (city == null)
			return "";
		else
			return city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		if (state == null)
			return "";
		else
			return state;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		if (location == null)
			return "";
		else
			return location;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setDuration(int travelTime) {
		this.travelTime = travelTime;
	}

	public int getDuration() {
		return travelTime;
	}

	public void setDistanceValid(boolean directionsValid) {
		this.directionsValid = directionsValid;
	}

	public boolean getDistanceValid() {
		return directionsValid;
	}
	
	public List<ISourceSpecificTrailInfo> getSourceSpecificInfos() {
		return sourceSpecificInfo;
	}
	
	public ISourceSpecificTrailInfo getSourceSpecificInfo(String source) {
		for(ISourceSpecificTrailInfo info : sourceSpecificInfo){
			if (info.getSourceName().equals(source)) {
				return info;
			}
		}
		return null;
	}
	
	public void addSourceSpecificInfo(ISourceSpecificTrailInfo info) {
		sourceSpecificInfo.add(info);
	}

}
