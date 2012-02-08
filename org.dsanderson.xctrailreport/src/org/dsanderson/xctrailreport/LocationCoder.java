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
package org.dsanderson.xctrailreport;

import java.util.List;

import org.dsanderson.xctrailreport.core.ILocationCoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

/**
 * 
 */
public class LocationCoder implements ILocationCoder {
	final Context context;

	public LocationCoder(Context context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ILocationCoder#getLocation(java.lang
	 * .String)
	 */
	public String getLocation(String locationName) {
		String location = locationName;
		try {
			Geocoder coder = new Geocoder(context);
			List<Address> addresses = coder.getFromLocationName(locationName, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				location = address.getLatitude() + "," + address.getLongitude();
			}
		} catch (Exception e) {
		}
		return location;
	}

}
