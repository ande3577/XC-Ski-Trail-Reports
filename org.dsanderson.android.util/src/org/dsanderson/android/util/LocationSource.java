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

import java.util.List;

import org.dsanderson.util.ILocationSource;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

/**
 * 
 */
public class LocationSource implements ILocationSource {
	Context context;
	String location;
	boolean hasNewLocation = false;

	public LocationSource(Context context, String defaultLocation) {
		this.context = context;
		location = defaultLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ILocation#updateLocation()
	 */
	public void updateLocation() {

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				handleNewLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

		};

		// Register the listener with the Location Manager to receive location
		// updates
		if (!Build.FINGERPRINT.startsWith("generic"))
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		List<String> providers = locationManager.getProviders(true);

		for (String provider : providers) {
			Location lastKnownLocation = locationManager
					.getLastKnownLocation(provider);

			handleNewLocation(lastKnownLocation);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ILocation#getLocation()
	 */
	public String getLocation() {
		return this.location;
	}

	private void handleNewLocation(Location location) {
		if (location != null) {
			hasNewLocation = true;
			this.location = Double.toString(location.getLatitude()) + ","
					+ Double.toString(location.getLongitude());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ILocationSource#getHasNewLocation()
	 */
	public boolean getHasNewLocation() {
		return hasNewLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ILocationSource#setLocation(java.lang
	 * .String)
	 */
	public void setLocation(String location) {
		this.location = location;
	}

}
