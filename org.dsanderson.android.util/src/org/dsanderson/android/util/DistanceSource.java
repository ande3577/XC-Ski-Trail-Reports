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

import org.dsanderson.util.CompoundXmlParser;
import org.dsanderson.util.IDistanceSource;
import org.dsanderson.util.INetConnection;
import org.dsanderson.util.IProgressBar;

/**
 * 
 */
public class DistanceSource implements IDistanceSource {
	INetConnection netConnection;

	List<Integer> distances = new ArrayList<Integer>();
	List<Integer> durations = new ArrayList<Integer>();
	List<Boolean> valids = new ArrayList<Boolean>();

	static final int ABSOLUTE_MAX_URL_LENGTH = 1000 - "&sensor=false".length();
	static final int MAX_URL_LENGTH_PADDING = 0;
	static final int MAX_URL_LENGTH = ABSOLUTE_MAX_URL_LENGTH
			- MAX_URL_LENGTH_PADDING;

	public DistanceSource(INetConnection netConnection) {
		this.netConnection = netConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IDirectionsSource#updateDirections(
	 * java.lang.String, java.lang.String)
	 */
	public void updateDistances(String src, List<String> dests,
			IProgressBar progressBar) throws Exception {

		if (src.length() == 0 || getMaxStringLength(dests) == 0)
			return;

		distances.clear();
		durations.clear();
		valids.clear();

		int index = 0;
		int startingIndex = 0;

		while (index < dests.size()) {
			// example
			// https://maps.googleapis.com/maps/api/distancematrix/xml?origins=44.972691,-93.232541&destinations=45.1335,-93.441|44.992,-93.3222&sensor=false
			String url = "https://maps.googleapis.com/maps/api/distancematrix/xml?origins="
					+ src + "&destinations=";
			int i = 0;
			while (url.length() < MAX_URL_LENGTH && index < dests.size()
					&& index - startingIndex < 99) {
				String dest = dests.get(index++);
				if (dest.length() > 0) {
					if (i++ > 0)
						url += "|";
					url += dest;
				}
			}

			url += "&sensor=false";

			try {
				netConnection.connect(url);
				if (progressBar != null)
					progressBar.incrementProgress();
				parseXmlResponse(netConnection);
				if (progressBar != null)
					progressBar.incrementProgress();
				if (index < dests.size() && index - startingIndex == 99) {
					// delay so I can use google distance API again
					Thread.sleep(11000);
					startingIndex = index;
				}
			} finally {
				netConnection.disconnect();
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IDirectionsSource#getDistance()
	 */
	public List<Integer> getDistances() {
		return distances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IDirectionsSource#getDriveTime()
	 */
	public List<Integer> getDurations() {
		return durations;
	}

	public List<Boolean> getValids() {
		return valids;
	}

	private int getMaxStringLength(List<String> strings) {
		int length = 0;
		for (String str : strings) {
			if (str.length() > length)
				length = str.length();
		}
		return length;
	}

	private void parseXmlResponse(INetConnection connection) throws Exception {
		CompoundXmlParser tagParser = CompoundXmlPullParserFactory
				.getInstance().newParser();
		tagParser.parse(connection.getReader());

		if (tagParser.getParsers("DistanceMatrixResponse:status").get(0)
				.getText().compareTo("OK") == 0) {
			List<CompoundXmlParser> elementParsers = tagParser
					.getParsers("DistanceMatrixResponse:row:element");
			for (CompoundXmlParser element : elementParsers) {
				if (element.getParsers("status").get(0).getText()
						.compareTo("OK") == 0) {
					valids.add(true);
					String distanceText = element.getParsers("distance:value")
							.get(0).getText();
					distances.add(Integer.parseInt(distanceText));

					String durationText = element.getParsers("duration:value")
							.get(0).getText();
					durations.add(Integer.parseInt(durationText));
				} else {
					valids.add(false);
					distances.add(0);
					durations.add(0);
				}

			}
		}

	}
}
