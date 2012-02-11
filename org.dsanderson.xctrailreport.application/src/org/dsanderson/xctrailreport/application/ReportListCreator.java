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

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportFilter;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoParser;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.TrailReportParser;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.UserSettings.AutoRefreshMode;

/**
 * 
 */
public class ReportListCreator {
	private final IAbstractFactory factory;
	private final IReportReaderFactory readerFactory;
	private List<TrailInfo> defaultTrailInfo = new ArrayList<TrailInfo>();

	public ReportListCreator(IAbstractFactory factory,
			IReportReaderFactory readerFactory) {
		this.factory = factory;
		this.readerFactory = readerFactory;
	}

	public List<TrailReport> getTrailReports(List<TrailReport> trailReports,
			boolean forced) throws Exception {
		boolean refreshNeeded = forced;

		UserSettings settings = factory.getUserSettings();

		if (settings.getAutoRefreshMode() == AutoRefreshMode.ALWAYS)
			refreshNeeded = true;

		if (!refreshNeeded
				&& settings.getAutoRefreshMode() == AutoRefreshMode.IF_OUT_OF_DATE) {
			Date lastModified = readerFactory.getReportsRefreshedDate();
			if (lastModified == null
					|| (new Date()).getTime() - lastModified.getTime() > factory
							.getUserSettings().getAutoRefreshCutoff())
				refreshNeeded = true;
		}

		try {
			loadSavedReports(trailReports, refreshNeeded);
		} catch (Exception e) {
			refreshNeeded = true;
		}

		if (refreshNeeded) {
			// / restore the list of default trail listings we got at startup
			List<TrailInfo> trailInfos = new ArrayList<TrailInfo>();
			for (TrailInfo info : defaultTrailInfo) {
				trailInfos.add(info.copy());
			}

			trailReports = new ArrayList<TrailReport>();

			IReportRetriever reportRetriever = factory.getReportRetriever();
			reportRetriever.getReports(trailReports, trailInfos);

			trailReports = filterTrailReports(trailReports);

			DistanceHandler directionHandler = new DistanceHandler(factory);
			directionHandler.getDistances(trailInfos);

			readerFactory.setReportsRefreshedDate(new Date());

			saveReports(trailInfos, trailReports);
		}

		return trailReports;
	}

	public List<TrailReport> sortTrailReports(List<TrailReport> trailReports) {
		Collections.sort(trailReports, new CompoundReportComparator(factory
				.getUserSettings().getSortMethod()));
		return trailReports;
	}

	private void loadSavedReports(List<TrailReport> trailReports,
			boolean refreshNeeded) throws Exception {
		// always load the included trail infos
		List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();
		TrailInfoParser parser = factory.newTrailInfoParser();

		try {
			loadTrailInfo(parser, readerFactory.newDefaultTrailInfoReader());
			for (TrailInfo info : parser.getTrailInfo()) {
				defaultTrailInfo.add(info.copy());
			}
		} catch (Exception e) {
			// if included trail reports creates error, we abort
			trailInfo.clear();
			trailReports.clear();
			throw (e);
		}

		if (!refreshNeeded) {
			try {
				loadTrailInfo(parser, readerFactory.newSavedTrailInfoReader());
			} catch (Exception e) {
				// don't care if saved info can't be read
			}
		}

		// info should be updated even if reports fail to load
		for (TrailInfo info : parser.getTrailInfo()) {
			trailInfo.add(info.copy());
		}

		if (!refreshNeeded) {
			TrailReportParser reportParser = factory.newTrailReportParser();
			loadTrailReports(reportParser,
					readerFactory.newSavedTrailReportReader(), trailInfo);
			for (TrailReport report : reportParser.getReports()) {
				trailReports.add(report.copy());
			}
		}
	}

	private void saveReports(List<TrailInfo> trailInfo,
			List<TrailReport> trailReports) throws Exception {
		TrailInfoParser infoParser = factory.newTrailInfoParser();
		infoParser.setTrailInfo(trailInfo);
		saveTrailInfo(infoParser, readerFactory.newSavedTrailInfoWriter());

		TrailReportParser reportParser = factory.newTrailReportParser();
		reportParser.setReports(trailReports);
		saveTrailReports(reportParser,
				readerFactory.newSavedTrailReportWriter());
	}

	private void loadTrailInfo(TrailInfoParser parser, Reader reader) {
		try {
			parser.parse(reader);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
				}
		}
	}

	private void saveTrailInfo(TrailInfoParser parser, Writer writer) {
		try {
			parser.write(writer);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception e) {
				}
		}
	}

	private void loadTrailReports(TrailReportParser parser, Reader reader,
			List<TrailInfo> trailInfo) {
		try {
			parser.parse(reader, trailInfo);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
				}
		}
	}

	private void saveTrailReports(TrailReportParser parser, Writer writer) {
		try {
			parser.write(writer);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception e) {
				}
		}
	}

	private List<TrailReport> filterTrailReports(List<TrailReport> trailReports) {
		IReportFilter filter = factory.newFilter();
		List<TrailReport> filteredReports = new ArrayList<TrailReport>();
		for (TrailReport report : trailReports) {
			if (filter.filterReport(report))
				filteredReports.add(report.copy());
		}

		return filteredReports;
	}

}
