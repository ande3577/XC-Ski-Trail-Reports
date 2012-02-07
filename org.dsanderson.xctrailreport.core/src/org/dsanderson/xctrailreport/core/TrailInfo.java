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
	String skinnyskiUrl = "";
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

	public void setSkinnyskiUrl(String skinnyskiUrl) {
		this.skinnyskiUrl = skinnyskiUrl;
	}

	public String getSkinnySkiUrl() {
		return skinnyskiUrl;
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

	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}

	public int getDuration() {
		return travelTime;
	}

	public void setDirectionsValid(boolean directionsValid) {
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
		newCopy.skinnyskiUrl = skinnyskiUrl;
		newCopy.threeRiversSearchTerm = threeRiversSearchTerm;
		newCopy.distance = distance;
		newCopy.directionsValid = directionsValid;

		return newCopy;
	}

}
