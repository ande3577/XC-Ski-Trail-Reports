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
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDistanceSource;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 
 */
public class DistanceSource implements IDistanceSource {
	IAbstractFactory factory;

	List<Integer> distances;
	List<Integer> durations;

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
	public boolean updateDistances(String src, List<String> dests) {

		if (src.length() == 0 || getMaxStringLength(dests) == 0)
			return false;

		boolean successful = false;

		// example
		// https://maps.googleapis.com/maps/api/distancematrix/json?origins=44.972691,-93.232541&destinations=45.1335,-93.441|44.992,-93.3222&sensor=false
		String url = "https://maps.googleapis.com/maps/api/distancematrix/xml?origins="
				+ src + "&destinations=";
		for (String dest : dests) {
			if (dest.length() > 0) {
				url += "|" + dest;
			}
		}

		url += "&sensor=false";

		INetConnection netConnection = factory.getNetConnection();

		if (netConnection.connect(url)) {
			try {
				parseXmlResponse(netConnection);
				successful = true;
				//
				// JSONObject distanceObject = legsObject
				// .getJSONObject("distance");
				// int distanceMeters = distanceObject.getInt("value");
				// distances.add(distanceMeters);
				//
				// JSONObject durationObject = legsObject
				// .getJSONObject("duration");
				// int durationSeconds = durationObject.getInt("value");
				// durations.add(durationSeconds);
				// successful = true;

			} catch (Exception e) {
				System.err.println(e);
				successful = false;
			} finally {
				netConnection.disconnect();
			}

		}

		return successful;

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
	public List<Integer> getDriveTimes() {
		return durations;
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
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		parserFactory.setNamespaceAware(false);
		XmlPullParser parser = parserFactory.newPullParser();

		parser.setInput(connection.getReader());

		CompoundTagParser tagParser = new CompoundTagParser();
		tagParser.parse(parser, "");

	}

}
