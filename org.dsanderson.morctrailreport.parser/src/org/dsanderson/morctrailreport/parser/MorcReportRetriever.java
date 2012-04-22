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

import org.dsanderson.util.INetConnection;
import org.dsanderson.util.IProgressBar;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.ITrailReportList;
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
	@Override
	public void getReports(ITrailReportList trailReports,
			ITrailInfoList trailInfos, IProgressBar progressBar) throws Exception {
		INetConnection netConnection = factory.getNetConnection();
		progressBar.incrementProgress();
		try {
			netConnection
					.connect("http://www.morcmtb.org/forums/trailconditions.php");
			progressBar.incrementProgress();
			BufferedInputStream stream = new BufferedInputStream(
					netConnection.getStream());
			MorcScanner scanner = new MorcScanner(stream,
					factory.getTrailReportPool(), factory.getTrailInfoPool(),
					morcFactory.getTrailInfoPool());

			for (String region : morcFactory.getRegionManager().getRegions()) {

				if (scanner.findRegion(region)) {
					while (scanner.scanRegion()) {
						progressBar.incrementProgress();
						TrailReport newTrailReport = scanner.getTrailReport();
						TrailInfo newTrailInfo = scanner.getTrailInfo();
						MorcSpecificTrailInfo newMorcInfo = scanner
								.getMorcSpecificInfo();

						newTrailInfo.addSourceSpecificInfo(newMorcInfo);
						newTrailInfo = trailInfos.mergeIntoList(newTrailInfo);

						newTrailReport.setTrailInfo(newTrailInfo);
						newTrailReport.setSource(MorcFactory.SOURCE_NAME);
						trailReports.add(newTrailReport);

						factory.getTrailReportPool().deleteItem(newTrailReport);
						factory.getTrailInfoPool().deleteItem(newTrailInfo);
						morcFactory.getTrailInfoPool().deleteItem(newMorcInfo);
					}
				}
			}
		} finally {
			netConnection.disconnect();
		}
	}

}
