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
package org.dsanderson.xctrailreport.application;

import java.util.Date;

import org.dsanderson.util.IDistanceSource;
import org.dsanderson.util.IProgressBar;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportListCreator;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.ITrailReportList;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.UserSettings.AutoRefreshMode;

/**
 * 
 */
public class ReportListCreator implements IReportListCreator {
	private final IAbstractFactory factory;

	public ReportListCreator(IAbstractFactory factory) {
		this.factory = factory;
	}

	public void getTrailReports(ITrailReportList trailReports,
			ITrailInfoList trailInfos, boolean forced, IProgressBar progressBar)
			throws Exception {
		boolean refreshNeeded = forced;

		UserSettings settings = factory.getUserSettings();

		if (settings.getAutoRefreshMode() == AutoRefreshMode.ALWAYS)
			refreshNeeded = true;

		if (!refreshNeeded
				&& settings.getAutoRefreshMode() == AutoRefreshMode.IF_OUT_OF_DATE) {
			try {
				Date lastModified = trailReports.getTimestamp();
				if (lastModified == null
						|| (new Date()).getTime() - lastModified.getTime() > factory
								.getUserSettings().getAutoRefreshCutoff())
					refreshNeeded = true;
			} catch (Exception e) {
				refreshNeeded = true;
				e.printStackTrace();
			}
		}

		try {
			loadSavedReports(trailReports, trailInfos, refreshNeeded);
		} catch (Exception e) {
			e.printStackTrace();
			refreshNeeded = true;
		}

		if (refreshNeeded) {
			Boolean sourceFound = false;
			// start by clearing out the existing trail reports
			trailReports.clear();
			trailReports.beginTransaction();
			try {
				for (ISourceSpecificFactory source : factory
						.getSourceSpecificFactories()) {
					if (source.getEnabled()) {
						source.getReportRetriever().getReports(trailReports,
								trailInfos, progressBar);
						sourceFound = true;
					}
				}

				if (!sourceFound)
					throw new Exception("No sources enabled.");

				getDistances(trailReports, progressBar);

				trailReports.endTransaction();
				trailReports.save();
			} catch (Exception e) {
				trailReports.cancelTransaction();
				throw (e);
			} finally {
				progressBar.close();
			}
		}
	}

	private void loadSavedReports(ITrailReportList trailReports,
			ITrailInfoList trailInfos, boolean refreshNeeded) throws Exception {
		// always load the included trail infos
		try {
			trailInfos.load();
		} catch (Exception e) {
			trailInfos.clear();
		}

		if (!refreshNeeded) {
			trailReports.load();
		}
	}

	private void getDistances(ITrailReportList trailReports,
			IProgressBar progressBar) {
		String location = factory.getLocationSource().getLocation();
		IDistanceSource distanceSource = factory.getDistanceSource();

		if (distanceSource != null) {
			try {
				trailReports.updateDistances(location, distanceSource,
						progressBar);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
