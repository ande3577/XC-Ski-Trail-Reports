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
public class DistanceHandler {
	IAbstractFactory factory;

	public DistanceHandler(IAbstractFactory factory) {
		this.factory = factory;
	}

	public void getDistances(List<TrailInfo> trailInfos) {
		String location = factory.getLocationSource().getLocation();
		IDistanceSource distanceSource = factory.getDistanceSource();

		List<String> destinations = new ArrayList<String>();
		for (TrailInfo info : trailInfos) {
			boolean duplicate = false;
			for (String dest : destinations) {
				if (dest.compareTo(info.location) == 0)
					duplicate = true;
			}

			if (!duplicate)
				destinations.add(info.location);
		}

		try {
			distanceSource.updateDistances(location, destinations);

			List<Integer> distances = distanceSource.getDistances();
			List<Integer> durations = distanceSource.getDurations();
			for (TrailInfo info : trailInfos) {
				if (info.getLocation().length() > 0) {
					for (int i = 0; i < destinations.size()
							&& !info.getDistanceValid(); i++) {
						if (destinations.get(i).compareTo(info.getLocation()) == 0) {
							info.setDistanceValid(true);
							info.setDistance(distances.get(i));
							info.setDuration(durations.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
