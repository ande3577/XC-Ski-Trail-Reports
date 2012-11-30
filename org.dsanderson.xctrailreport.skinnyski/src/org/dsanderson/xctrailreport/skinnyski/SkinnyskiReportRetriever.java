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
package org.dsanderson.xctrailreport.skinnyski;

import java.io.BufferedInputStream;

import org.dsanderson.util.INetConnection;
import org.dsanderson.util.IProgressBar;
import org.dsanderson.util.LocationInfo;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.ITrailReportList;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class SkinnyskiReportRetriever implements IReportRetriever {

	private final IAbstractFactory factory;
	private final SkinnyskiFactory skinnySkiFactory;

	public SkinnyskiReportRetriever(IAbstractFactory factory,
			SkinnyskiFactory skinnyskiFactory) {
		this.factory = factory;
		this.skinnySkiFactory = skinnyskiFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.IReportRetriever#getReports(org.dsanderson.TrailInfo)
	 */
	public void getReports(ITrailReportList trailReports,
			ITrailInfoList trailInfos, IProgressBar progressBar)
			throws Exception {

		if (skinnySkiFactory.getRegions().getRegions().isEmpty())
			throw new Exception("No regions enabled.");

		INetConnection netConnection = factory.getNetConnection();
		try {
			netConnection.connect("http://skinnyski.com/trails/reports.asp");
			progressBar.incrementProgress();
			BufferedInputStream stream = new BufferedInputStream(
					netConnection.getStream());
			progressBar.incrementProgress();
			SkinnyskiScanner scanner = new SkinnyskiScanner(stream,
					factory.getTrailReportPool(), factory.getTrailInfoPool(),
					skinnySkiFactory.getTrailInfoPool());

			for (String region : skinnySkiFactory.getRegions().getRegions()) {

				if (scanner.findRegion(region)) {
					while (scanner.scanRegion()) {
						progressBar.incrementProgress();
						TrailReport newTrailReport = scanner.getTrailReport();
						TrailInfo newTrailInfo = scanner.getTrailInfo();
						SkinnyskiSpecificInfo newSkinnyskiInfo = scanner
								.getSkinnyskiSpecificInfo();

						newTrailInfo.addSourceSpecificInfo(newSkinnyskiInfo);

						TrailInfo existingInfo = trailInfos.find(newTrailInfo
								.getName());

						if (existingInfo == null) {
							LocationInfo locationInfo = factory
									.getLocationCoder().getLocation(
											newTrailInfo.getName() + ", "
													+ newTrailInfo.getCity()
													+ ", "
													+ newTrailInfo.getState());
							newTrailInfo.setLocation(locationInfo.location);
							newTrailInfo
									.setSpecificLocation(locationInfo.specificLocation);
						}

						newTrailInfo = trailInfos.mergeIntoList(newTrailInfo);

						newTrailReport.setTrailInfo(newTrailInfo);
						newTrailReport
								.setSource(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
						trailReports.add(newTrailReport);

						factory.getTrailReportPool().deleteItem(newTrailReport);
						factory.getTrailInfoPool().deleteItem(newTrailInfo);
						skinnySkiFactory.getTrailInfoPool().deleteItem(
								newSkinnyskiInfo);
					}
				}
			}
		} finally {
			netConnection.disconnect();
		}
	}
}
