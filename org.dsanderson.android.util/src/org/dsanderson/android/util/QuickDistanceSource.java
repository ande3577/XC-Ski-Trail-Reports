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

import org.dsanderson.util.IDistanceSource;
import org.dsanderson.util.IProgressBar;
import org.dsanderson.util.LocationInfo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

/**
 * 
 */
public class QuickDistanceSource implements IDistanceSource {

	List<Integer> distances = new ArrayList<Integer>();
	List<Boolean> distanceValids = new ArrayList<Boolean>();
	List<Integer> durations = new ArrayList<Integer>();
	List<Boolean> durationValids = new ArrayList<Boolean>();
	private final LocationCoder coder;

	public QuickDistanceSource(LocationCoder coder) {
		this.coder = coder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.util.IDistanceSource#updateDistances(java.lang.String,
	 * java.util.List, org.dsanderson.util.IProgressBar)
	 */
	public void updateDistances(String src, List<String> dests,
			IProgressBar progressBar) throws Exception {
		if (src.length() == 0 || getMaxStringLength(dests) == 0)
			return;

		coder.getLocation(src);
		Address sourceAddress = coder.getAddress();

		for (String dest : dests) {
			coder.getLocation(dest);
			Address destAddress = coder.getAddress();
			float distance[] = { 0 };
			Location.distanceBetween(sourceAddress.getLatitude(),
					sourceAddress.getLongitude(), destAddress.getLatitude(),
					destAddress.getLongitude(), distance);
			distances.add((int) distance[0]);
			distanceValids.add(true);
			durations.add(0);
			durationValids.add(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IDistanceSource#getDistances()
	 */
	public List<Integer> getDistances() {
		return distances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IDistanceSource#getDurations()
	 */
	public List<Integer> getDurations() {
		return durations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IDistanceSource#getDistanceValids()
	 */
	public List<Boolean> getDistanceValids() {
		return distanceValids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IDistanceSource#getDurationValids()
	 */
	public List<Boolean> getDurationValids() {
		return durationValids;
	}

	private int getMaxStringLength(List<String> strings) {
		int length = 0;
		for (String str : strings) {
			if (str.length() > length)
				length = str.length();
		}
		return length;
	}

}
