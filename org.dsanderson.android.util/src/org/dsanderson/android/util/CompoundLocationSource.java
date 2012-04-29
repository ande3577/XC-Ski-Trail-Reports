package org.dsanderson.android.util;

import org.dsanderson.android.util.LocationCoder;
import org.dsanderson.android.util.LocationSource;
import org.dsanderson.util.ICompoundLocationSource;

import android.content.Context;

public class CompoundLocationSource implements ICompoundLocationSource {
	boolean locationEnabled;
	String defaultLocation;
	final LocationSource locationSource;
	final LocationCoder locationCoder;

	public CompoundLocationSource(Context context, boolean locationEnabled,
			String defaultLocation) {
		locationSource = new LocationSource(context, defaultLocation);
		locationCoder = new LocationCoder(context);
		this.defaultLocation = defaultLocation;
		this.locationEnabled = locationEnabled;
	}

	public void updateLocation() {
		if (locationEnabled)
			locationSource.updateLocation();
	}

	public String getLocation() {
		String location = defaultLocation;
		if (locationEnabled && locationSource.getHasNewLocation())
			location = locationSource.getLocation();
		else {
			try {
				location = locationCoder.getLocation(defaultLocation).location;
			} catch (Exception e) {
				// ignore exception
			}
		}
		return location;
	}

	public void setLocation(String location) {
		if (locationEnabled)
			locationSource.setLocation(location);
	}

	public boolean getHasNewLocation() {
		if (locationEnabled)
			return locationSource.getHasNewLocation();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.util.ICompoundLocationSource#setLocationEnabled(boolean)
	 */
	public void setLocationEnabled(boolean locationEnabled) {
		this.locationEnabled = locationEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.ICompoundLocationSource#getLocationEnabled()
	 */
	public boolean getLocationEnabled() {
		return locationEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.util.ICompoundLocationSource#setDefaultLocation(java.lang
	 * .String)
	 */
	public void setDefaultLocation(String defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.ICompoundLocationSource#getDefaultLocation()
	 */
	public String getDefaultLocation() {
		return defaultLocation;
	}

}
