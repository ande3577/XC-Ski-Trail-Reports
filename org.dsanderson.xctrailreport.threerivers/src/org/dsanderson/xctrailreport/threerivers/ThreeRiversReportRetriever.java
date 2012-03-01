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
package org.dsanderson.xctrailreport.threerivers;

import java.io.BufferedInputStream;
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class ThreeRiversReportRetriever implements IReportRetriever {

	IAbstractFactory factory;

	public ThreeRiversReportRetriever(IAbstractFactory factory) {
		this.factory = factory;
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
					.connect("http://www.threeriversparks.org/news/news/cc-ski-trail-conditions.aspx");
			BufferedInputStream stream = new BufferedInputStream(
					netConnection.getStream());
			ThreeRiversScanner scanner = new ThreeRiversScanner(stream,
					factory.getTrailReportPool(), factory.getTrailInfoPool());

			while (scanner.scanRegion()) {
				TrailReport newTrailReport = scanner.getTrailReport();
				TrailInfo newTrailInfo = scanner.getTrailInfo();

				boolean existingTrail = false;
				TrailInfo trailInfo = null;
				for (TrailInfo info : trailInfos) {
					if (newTrailInfo.getThreeRiversSearchTerm().compareTo(
							info.getThreeRiversSearchTerm()) == 0) {
						existingTrail = true;
						trailInfo = info;
					}
				}

				if (!existingTrail) {
					newTrailInfo.setName(newTrailInfo.getSkinnyskiSearchTerm());
					newTrailInfo.setLocation(factory.getLocationCoder()
							.getLocation(
									newTrailInfo.getName() + ", "
											+ newTrailInfo.getCity() + ", "
											+ newTrailInfo.getState()));

					trailInfos.add(newTrailInfo);
					trailInfo = trailInfos.get(trailInfos.size() - 1);
				} else {
					factory.getTrailInfoPool().deleteTrailInfo(newTrailInfo);
				}

				newTrailReport.setTrailInfo(trailInfo);
				newTrailReport.setSource("ThreeRivers");

				trailReports.add(newTrailReport);
			}
		} finally {
			netConnection.disconnect();
		}
	} // parseHtml
}
