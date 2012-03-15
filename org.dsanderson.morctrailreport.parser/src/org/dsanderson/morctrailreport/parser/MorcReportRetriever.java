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
package org.dsanderson.morctrailreport.parser;

import java.io.BufferedInputStream;
import java.util.List;

import org.dsanderson.util.INetConnection;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class MorcReportRetriever implements IReportRetriever {

	private final IAbstractFactory factory;
	private final MorcFactory morcFactory;

	public MorcReportRetriever(IAbstractFactory factory, MorcFactory morcFactory) {
		this.factory = factory;
		this.morcFactory = morcFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.IReportRetriever#getReports(org.dsanderson.TrailInfo)
	 */
	public void getReports(List<TrailReport> trailReports,
			List<TrailInfo> trailInfos) throws Exception {
		parseHtml(trailReports, trailInfos);
	}

	private void parseHtml(List<TrailReport> trailReports,
			List<TrailInfo> trailInfos) throws Exception {
		INetConnection netConnection = factory.getNetConnection();
		try {
			netConnection
					.connect("http://www.morcmtb.org/forums/trailconditions.php");
			BufferedInputStream stream = new BufferedInputStream(
					netConnection.getStream());
			MorcScanner scanner = new MorcScanner(stream,
					factory.getTrailReportPool(), factory.getTrailInfoPool(),
					morcFactory.getTrailInfoPool());

			for (String region : morcFactory.getRegionManager().getRegions()) {

				if (scanner.findRegion(region)) {
					while (scanner.scanRegion()) {
						TrailReport newTrailReport = scanner.getTrailReport();
						TrailInfo newTrailInfo = scanner.getTrailInfo();
						MorcSpecificTrailInfo newMorcInfo = scanner.getMorcSpecificInfo();

						boolean existingTrail = false;
						boolean existingSkinnyskiTrail = false;
						TrailInfo trailInfo = null;
						for (TrailInfo info : trailInfos) {
							if (newTrailInfo.getName()
									.compareTo(info.getName()) == 0) {
								existingTrail = true;
								if (info.getSourceSpecificInfo(MorcFactory.SOURCE_NAME) == null)
									info.addSourceSpecificInfo(newMorcInfo);
								else
									existingSkinnyskiTrail = true;
								trailInfo = info;
							}
						}

						if (!existingTrail) {
							newTrailInfo.setLocation(factory.getLocationCoder()
									.getLocation(
											newTrailInfo.getName() + ", "
													+ newTrailInfo.getCity()
													+ ", "
													+ newTrailInfo.getState()));

							trailInfos.add(newTrailInfo);
							trailInfo = trailInfos.get(trailInfos.size() - 1);
							trailInfo.addSourceSpecificInfo(newMorcInfo);
						} else {
							factory.getTrailInfoPool().deleteItem(newTrailInfo);
							if (!existingSkinnyskiTrail)
								morcFactory.getTrailInfoPool().deleteItem(
										newMorcInfo);
						}

						newTrailReport.setTrailInfo(trailInfo);
						newTrailReport.setSource(MorcFactory.SOURCE_NAME);

						trailReports.add(newTrailReport);
					}
				}
			}
		} finally {
			netConnection.disconnect();
		}
	} // parseHtml
}
