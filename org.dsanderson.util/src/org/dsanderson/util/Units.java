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
package org.dsanderson.util;

/**
 * 
 */
public class Units {

	private static final double METERS_PER_MILE = 1609.344;

	public static double metersToMiles(long meters) {
		return ((double) meters) / METERS_PER_MILE;
	}

	public static int milesToMeters(double miles) {
		return (int) (miles * METERS_PER_MILE);
	}

	public static double secondsToMinutes(int seconds) {
		return ((double) seconds) / 60;
	}

	public static int minutesToSeconds(double minutes) {
		return (int) (minutes * 60);
	}

	public static double millisecondsToDays(long milliseconds) {
		return ((double) milliseconds) / 1000 / 60 / 60 / 24;
	}

	public static long hoursToMilliseconds(double hours) {
		return (long) (hours * 60 * 60 * 1000);
	}

	public static long daysToMilliseconds(double days) {
		return hoursToMilliseconds(days * 24);
	}

	public static double millisecondsToHours(long milliseconds) {
		return ((double) milliseconds) / 60 / 60 / 1000;
	}

}
