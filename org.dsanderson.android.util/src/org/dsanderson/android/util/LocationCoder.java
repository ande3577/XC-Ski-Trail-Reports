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
package org.dsanderson.android.util;

import java.util.ArrayList;
import java.util.List;

import org.dsanderson.util.ILocationCoder;
import org.dsanderson.util.LocationInfo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

/**
 * 
 */
public class LocationCoder implements ILocationCoder {
	final Context context;
	final Geocoder coder;
	Address address;

	public LocationCoder(Context context) {
		this.context = context;
		coder = new Geocoder(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ILocationCoder#getLocation(java.lang
	 * .String)
	 */
	public LocationInfo getLocation(String locationName) throws Exception {
		// geocoder unavailable in emulator
		if (Build.FINGERPRINT.startsWith("generic"))
			return new LocationInfo(locationName, false);
		
		// return empty string if cannot parse to valid location
		String location = "";
		List<Address> addresses = new ArrayList<Address>();
		boolean specificLocation = true;

		while (locationName.length() > 0) {
			try {
				addresses = coder.getFromLocationName(locationName, 1);
				if (addresses.size() == 0) {
					throw new Exception("Address not found");
				} else {
					break;
				}
			} catch (Exception ex) {
				locationName = lessSpecific(locationName);
				specificLocation = false;
			}
		}

		if (addresses.size() > 0) {
			address = addresses.get(0);
			location = address.getLatitude() + "," + address.getLongitude();
		}
		return new LocationInfo(location, specificLocation);
		
	}
	
	public Address getAddress() {
		return address;
	}

	private String lessSpecific(String locationName) {
		String splits[] = locationName.split("\\,");
		String str = "";
		for (int i = 1; i < splits.length; i++) {
			if (i != 1)
				str += ", ";
			str += splits[i];
		}
		return str.trim();
	}

}
