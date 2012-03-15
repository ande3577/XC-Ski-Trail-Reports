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
package org.dsanderson.xctrailreport.threerivers.test;

import java.io.FileInputStream;

import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoPool;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.TrailReportPool;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversScanner;

/**
 * 
 */
public class ScannerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		FileInputStream fileStream = null;

		try {
			TrailReportPool trailReportPool = new TrailReportPool();
			TrailInfoPool trailInfoPool = new TrailInfoPool();

			fileStream = new FileInputStream("cc-ski-trail-conditions.aspx");

			ThreeRiversScanner threeRiversScanner = new ThreeRiversScanner(
					fileStream, trailReportPool, trailInfoPool);

			while (threeRiversScanner.scanRegion()) {
				printReportInfo(threeRiversScanner);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				fileStream.close();
			} catch (Exception e) {
			}
		}
	}

	private static void printReportInfo(ThreeRiversScanner threeRiversScanner) {
		TrailInfo trailInfo = threeRiversScanner.getTrailInfo();
		TrailReport trailReport = threeRiversScanner.getTrailReport();

		System.out.println("New Report:");
		System.out.println("Date: " + trailReport.getDate().formatDate());
		System.out.println("Name: " + trailInfo.getName());

		System.out.println("Summary: " + trailReport.getSummary());
		System.out.println("Detailed: " + trailReport.getDetail());
		System.out.println("");

	}

}
