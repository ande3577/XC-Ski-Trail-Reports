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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDistanceSource;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 
 */
public class DistanceSource implements IDistanceSource {
	IAbstractFactory factory;

	List<Integer> distances = new ArrayList<Integer>();
	List<Integer> durations = new ArrayList<Integer>();
	List<Boolean> valids = new ArrayList<Boolean>();

	static final int ABSOLUTE_MAX_URL_LENGTH = 1000 - "&sensor=false".length();
	static final int MAX_URL_LENGTH_PADDING = 0;
	static final int MAX_URL_LENGTH = ABSOLUTE_MAX_URL_LENGTH
			- MAX_URL_LENGTH_PADDING;

	public DistanceSource(IAbstractFactory factory) {
		this.factory = factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IDirectionsSource#updateDirections(
	 * java.lang.String, java.lang.String)
	 */
	public void updateDistances(String src, List<String> dests)
			throws Exception {

		if (src.length() == 0 || getMaxStringLength(dests) == 0)
			return;

		distances.clear();
		durations.clear();
		valids.clear();

		int index = 0;

		while (index < dests.size()) {
			// example
			// https://maps.googleapis.com/maps/api/distancematrix/xml?origins=44.972691,-93.232541&destinations=45.1335,-93.441|44.992,-93.3222&sensor=false
			String url = "https://maps.googleapis.com/maps/api/distancematrix/xml?origins="
					+ src + "&destinations=";
			int i = 0;
			while (url.length() < MAX_URL_LENGTH && index < dests.size()) {
				String dest = dests.get(index++);
				if (dest.length() > 0) {
					if (i++ > 0)
						url += "|";
					url += dest;
				}
			}

			url += "&sensor=false";

			INetConnection netConnection = factory.getNetConnection();

			try {
				netConnection.connect(url);
				parseXmlResponse(netConnection);
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

	private void parseXmlResponse(INetConnection connection)
			throws XmlPullParserException, IOException {
		CompoundTagParser tagParser = new CompoundTagParser();
		tagParser.parse(connection.getReader());

		if (tagParser.getParsers("DistanceMatrixResponse:status").get(0)
				.getText().compareTo("OK") == 0) {
			List<CompoundTagParser> elementParsers = tagParser
					.getParsers("DistanceMatrixResponse:row:element");
			for (CompoundTagParser element : elementParsers) {
				valids.add(element.getParsers("status").get(0).getText()
						.compareTo("OK") == 0);
				String distanceText = element.getParsers("distance:value")
						.get(0).getText();
				distances.add(Integer.parseInt(distanceText));

				String durationText = element.getParsers("duration:value")
						.get(0).getText();
				durations.add(Integer.parseInt(durationText));

			}
		}

	}
}
