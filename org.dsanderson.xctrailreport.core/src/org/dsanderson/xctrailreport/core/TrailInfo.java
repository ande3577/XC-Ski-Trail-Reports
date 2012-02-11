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

/**
 * 
 */
public class TrailInfo {
	String name = "";
	String city = "";
	String state = "MN";
	String location = "";
	String skinnyskiSearchTerm = "";
	int skinnyskiTrailIndex = -1;
	String threeRiversSearchTerm = "";
	int distance = 0;
	int travelTime = 0;
	boolean directionsValid = false;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setSkinnyskiSearchTerm(String skinnyskiSearchTerm) {
		this.skinnyskiSearchTerm = skinnyskiSearchTerm;
	}

	public String getSkinnyskiSearchTerm() {
		return skinnyskiSearchTerm;
	}

	public void setskinnyskiTrailIndex(int skinnyskiTrailIndex) {
		this.skinnyskiTrailIndex = skinnyskiTrailIndex;
	}

	public int getskinnyskiTrailIndex() {
		return skinnyskiTrailIndex;
	}

	public String getSkinnySkiUrl() {
		if (skinnyskiTrailIndex < 0)
			return "";
		else
			return "http://skinnyski.com/trails/traildetail.asp?Id="
					+ skinnyskiTrailIndex;

	}

	public String getSkinnySkiSubmitUrl() {
		if (skinnyskiTrailIndex < 0)
			return "";
		else
			return "http://skinnyski.com/trails/trailreport.asp?trailId="
					+ skinnyskiTrailIndex;
	}

	public void setThreeRiversSearchTerm(String threeRiversSearchTerm) {
		this.threeRiversSearchTerm = threeRiversSearchTerm;
	}

	public String getThreeRiversSearchTerm() {
		return threeRiversSearchTerm;
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

	public TrailInfo copy() {
		TrailInfo newCopy = new TrailInfo();

		newCopy.name = name;
		newCopy.city = city;
		newCopy.state = state;
		newCopy.location = location;
		newCopy.skinnyskiSearchTerm = skinnyskiSearchTerm;
		newCopy.skinnyskiTrailIndex = skinnyskiTrailIndex;
		newCopy.threeRiversSearchTerm = threeRiversSearchTerm;
		newCopy.distance = distance;
		newCopy.travelTime = travelTime;
		newCopy.directionsValid = directionsValid;

		return newCopy;
	}

}
