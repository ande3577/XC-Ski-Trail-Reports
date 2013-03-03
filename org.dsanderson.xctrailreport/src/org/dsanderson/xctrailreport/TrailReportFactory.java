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

import java.util.ArrayList;
import java.util.List;

import org.dsanderson.android.util.CompoundLocationSource;
import org.dsanderson.android.util.CompoundXmlPullParserFactory;
import org.dsanderson.android.util.Dialog;
import org.dsanderson.android.util.DistanceSource;
import org.dsanderson.android.util.GenericDatabase;
import org.dsanderson.android.util.IDatabaseObjectFactory;
import org.dsanderson.android.util.LocationCoder;
import org.dsanderson.android.util.QuickDistanceSource;
import org.dsanderson.android.util.TextItem;
import org.dsanderson.android.util.UrlConnection;
import org.dsanderson.util.ICompoundLocationSource;
import org.dsanderson.util.IDialog;
import org.dsanderson.util.IDistanceSource;
import org.dsanderson.util.ILocationCoder;
import org.dsanderson.util.INetConnection;
import org.dsanderson.util.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.ITrailReportList;
import org.dsanderson.xctrailreport.core.TrailInfoParser;
import org.dsanderson.xctrailreport.core.TrailInfoPool;
import org.dsanderson.xctrailreport.core.TrailReportParser;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.TrailReportPool;
import org.dsanderson.xctrailreport.core.android.TrailInfoDatabaseFactory;
import org.dsanderson.xctrailreport.core.android.TrailInfoList;
import org.dsanderson.xctrailreport.core.android.TrailReportDatabaseFactory;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversFactory;

import android.app.Activity;

/**
 * 
 */
public class TrailReportFactory implements IAbstractFactory {
	static TrailReportFactory factory = null;
	Activity context;
	SkinnyskiAndroidFactory skinnyskiFactory = null;
	ThreeRiversFactory threeRiversFactory = null;
	UrlConnection netConnection = null;
	CompoundLocationSource locationSource = null;
	UserSettings userSettings = null;
	UserSettingsSource settingsSource = null;
	LocationCoder locationCoder = null;
	TrailReportPool trailReportPool = null;
	TrailInfoPool trailInfoPool = null;
	GenericDatabase database = null;
	TrailReportList trailReportList = null;
	TrailInfoList trailInfoList = null;
	TrailInfoDatabaseFactory trailInfoDatabaseFactory = null;
	TrailReportReaderFactory trailReportReaderFactory = null;
	TrailReportDatabaseFactory trailReportDatabaseFactory = null;
	TextItem dateText = null;

	public TrailReportFactory(Activity context) {
		assert (factory == null);
		factory = this;
		this.context = context;
		skinnyskiFactory = new SkinnyskiAndroidFactory(context, this);
		threeRiversFactory = new ThreeRiversFactory(this);
	}

	static public TrailReportFactory getInstance() {
		assert (factory != null);
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoParser()
	 */
	public TrailInfoParser newTrailInfoParser() {
		return new TrailInfoParser(CompoundXmlPullParserFactory.getInstance(),
				factory.getTrailInfoPool(), getSourceSpecificFactories());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#newTrailReportParser()
	 */
	public TrailReportParser newTrailReportParser() {
		return new TrailReportParser(
				CompoundXmlPullParserFactory.getInstance(),
				getTrailReportPool());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getNetConnection()
	 */
	public INetConnection getNetConnection() {
		if (netConnection == null) {
			netConnection = new UrlConnection();
		}
		return netConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IAbstractFactory#getLocation()
	 */
	public ICompoundLocationSource getLocationSource() {
		if (locationSource == null) {
			locationSource = new CompoundLocationSource(context, false,
					"Minneapolis, MN");
		}
		return locationSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getUserSettingsSource
	 * ()
	 */
	public IUserSettingsSource getUserSettingsSource() {
		if (settingsSource == null) {
			settingsSource = new UserSettingsSource(context, this);
		}
		return settingsSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getDirectionsSource()
	 */
	public IDistanceSource getDistanceSource() {
		switch (getUserSettings().getDistanceMode()) {
		default:
		case FULL:
			return new DistanceSource(getNetConnection());
		case QUICK:
			return new QuickDistanceSource();
		case DISABLED:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IAbstractFactory#getErrorDialog()
	 */
	public IDialog newDialog(Exception e) {
		e.printStackTrace();
		Dialog dialog = new Dialog(context, e);
		return dialog;
	}

	public IDialog newDialog(String string) {
		return new Dialog(context, string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IAbstractFactory#getUserSettings()
	 */
	public UserSettings getUserSettings() {
		if (userSettings == null) {
			userSettings = new UserSettings();
			userSettings
					.setSortMethod(UserSettings.SortMethod.SORT_BY_DISTANCE);
		}

		return userSettings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getLocationCoder()
	 */
	public ILocationCoder getLocationCoder() {
		return new LocationCoder(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailReportFactory
	 * ()
	 */
	public TrailReportPool getTrailReportPool() {
		if (trailReportPool == null)
			trailReportPool = new TrailReportPool();
		return trailReportPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoPool()
	 */
	public TrailInfoPool getTrailInfoPool() {
		if (trailInfoPool == null)
			trailInfoPool = new TrailInfoPool();
		return trailInfoPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getSourceSpecificFactory
	 * ()
	 */
	public List<ISourceSpecificFactory> getSourceSpecificFactories() {
		List<ISourceSpecificFactory> factories = new ArrayList<ISourceSpecificFactory>();
		factories.add(skinnyskiFactory);
		factories.add(threeRiversFactory);
		return factories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getSourceSpecificFactory
	 * (java.lang.String)
	 */
	public ISourceSpecificFactory getSourceSpecificFactory(String sourceName) {
		for (ISourceSpecificFactory factory : getSourceSpecificFactories()) {
			if (factory.getSourceName().compareTo(sourceName) == 0) {
				return factory;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailReportList()
	 */
	public ITrailReportList getTrailReportList() {
		if (trailReportList == null) {
			trailReportList = new TrailReportList(context,
					new TrailReportDatabaseFactory(
							factory.getTrailReportPool(),
							getTrailInfoDatabaseFactory()),
					factory.getTrailReportPool(), factory.getTrailInfoPool(),
					TrailReportDatabaseFactory.DATABASE_NAME,
					Integer.parseInt(context
							.getString(R.integer.databaseVersion)),
					factory.getTrailInfoList());
		}

		try {
			trailReportList.open();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return (ITrailReportList) trailReportList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoList()
	 */
	public ITrailInfoList getTrailInfoList() {
		if (trailInfoList == null)
			trailInfoList = new TrailInfoList(context, factory,
					getTrailInfoDatabaseFactory(), getReportReaderFactory(),
					getTrailInfoPool(), TrailInfoDatabaseFactory.TABLE_TEST,
					Integer.parseInt(context
							.getString(R.integer.databaseVersion)));

		try {
			trailInfoList.open();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return trailInfoList;
	}

	private TrailInfoDatabaseFactory getTrailInfoDatabaseFactory() {
		if (trailInfoDatabaseFactory == null) {
			List<IDatabaseObjectFactory> sourceSpecificFactories = new ArrayList<IDatabaseObjectFactory>();
			sourceSpecificFactories.add(new SkinnyskiDatabaseFactory(
					skinnyskiFactory));
			sourceSpecificFactories.add(new ThreeriversDatabaseFactory(
					threeRiversFactory));
			trailInfoDatabaseFactory = new TrailInfoDatabaseFactory(
					factory.getTrailInfoPool(), sourceSpecificFactories);
		}
		return trailInfoDatabaseFactory;
	}

	private TrailReportReaderFactory getReportReaderFactory() {
		if (trailReportReaderFactory == null) {
			trailReportReaderFactory = new TrailReportReaderFactory(context);
		}
		return trailReportReaderFactory;
	}

	public TrailReportDatabaseFactory getTrailReportDatabaseFactory() {
		if (trailReportDatabaseFactory == null) {
			trailReportDatabaseFactory = new TrailReportDatabaseFactory(
					getTrailReportPool(), getTrailInfoDatabaseFactory());
		}
		return trailReportDatabaseFactory;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#filterReports(org.
	 * dsanderson.xctrailreport.core.ITrailReportList)
	 */
	public void filterReports(ITrailReportList trailReports) {
		TrailReportList reports = (TrailReportList) trailReports;
		// apply default filters
		reports.filter(getUserSettings());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#sortReports(org.dsanderson
	 * .xctrailreport.core.ITrailReportList)
	 */
	public void sortReports(ITrailReportList trailReports) {
		UserSettings settings = getUserSettings();
		trailReports.sort(settings);
	}
	
}
