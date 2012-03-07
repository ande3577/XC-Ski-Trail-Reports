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

import org.dsanderson.xctrailreport.application.CompoundFilter;
import org.dsanderson.xctrailreport.application.CompoundReportRetriever;
import org.dsanderson.xctrailreport.application.DateFilter;
import org.dsanderson.xctrailreport.application.DistanceFilter;
import org.dsanderson.xctrailreport.application.DurationFilter;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDistanceSource;
import org.dsanderson.xctrailreport.core.IDialog;
import org.dsanderson.xctrailreport.core.ILocationCoder;
import org.dsanderson.xctrailreport.core.ILocationSource;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.dsanderson.xctrailreport.core.IReportFilter;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.TrailInfoParser;
import org.dsanderson.xctrailreport.core.TrailInfoPool;
import org.dsanderson.xctrailreport.core.TrailReportDecorator;
import org.dsanderson.xctrailreport.core.TrailReportParser;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.decorators.AuthorDecorator;
import org.dsanderson.xctrailreport.decorators.CityStateDecorator;
import org.dsanderson.xctrailreport.decorators.DateDecorator;
import org.dsanderson.xctrailreport.decorators.DetailedReportDecorator;
import org.dsanderson.xctrailreport.decorators.DistanceDecorator;
import org.dsanderson.xctrailreport.decorators.PhotosetDecorator;
import org.dsanderson.xctrailreport.decorators.SummaryDecorator;
import org.dsanderson.xctrailreport.decorators.TrailNameDecorator;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversReportRetriever;
import org.dsanderson.xctrailreport.core.TrailReportPool;
import org.dsanderson.xctrailreport.application.PhotosetFilter;

import android.content.Context;

/**
 * 
 */
public class TrailReportFactory implements IAbstractFactory {
	static TrailReportFactory factory = null;
	Context context;
	SkinnyskiFactory skinnyskiFactory;
	UrlConnection netConnection = null;
	TrailReportDecorator infoDecorator = null;
	TrailReportDecorator reportDecorator = null;
	LocationSource locationSource = null;
	UserSettings userSettings = null;
	UserSettingsSource settingsSource = null;
	LocationCoder locationCoder = null;
	TrailReportPool trailReportPool = null;
	TrailInfoPool trailInfoPool = null;

	public TrailReportFactory(Context context, SkinnyskiFactory skinnyskiFactory) {
		assert (factory == null);
		factory = this;
		this.context = context;
		this.skinnyskiFactory = skinnyskiFactory;
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
				factory.getTrailInfoPool());
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
	public ILocationSource getLocationSource() {
		if (locationSource == null) {
			locationSource = new LocationSource(context,
					userSettings.getDefaultLocation());
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
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getReportRetriever()
	 */
	public IReportRetriever getReportRetriever() {
		CompoundReportRetriever reportRetriever = new CompoundReportRetriever();

		if (userSettings.getSkinnyskiEnabled())
			reportRetriever.addRetriever(skinnyskiFactory
					.getSkinnyskiReportRetriever(this));
		if (userSettings.getThreeRiversEnabed())
			reportRetriever.addRetriever(new ThreeRiversReportRetriever(this));
		return reportRetriever;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoDecorators
	 * ()
	 */
	public TrailReportDecorator getTrailReportDecorators() {
		if (reportDecorator == null) {
			reportDecorator = new DateDecorator();
			reportDecorator.add(new SummaryDecorator());
			reportDecorator.add(new DetailedReportDecorator());
			reportDecorator.add(new PhotosetDecorator());
			reportDecorator.add(new AuthorDecorator());
		}

		return reportDecorator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getDirectionsSource()
	 */
	public IDistanceSource getDistanceSource() {
		return new DistanceSource(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoDecorators
	 * ()
	 */
	public TrailReportDecorator getTrailInfoDecorators() {
		if (infoDecorator == null) {
			infoDecorator = new TrailNameDecorator();
			infoDecorator.add(new CityStateDecorator());
			infoDecorator.add(new DistanceDecorator());
		}

		return infoDecorator;
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
	 * @see org.dsanderson.xctrailreport.core.IAbstractFactory#getFilter()
	 */
	public IReportFilter newFilter() {
		CompoundFilter filter = new CompoundFilter();
		if (userSettings.getDistanceFilterEnabled())
			filter.add(new DistanceFilter(userSettings.getFilterDistance()));
		if (userSettings.getDurationFilterEnabled())
			filter.add(new DurationFilter(userSettings.getDurationCutoff()));
		if (userSettings.getDateFilterEnabled())
			filter.add(new DateFilter(userSettings.getFilterAge()));
		if (userSettings.getPhotosetFilterEnabled())
			filter.add(new PhotosetFilter());
		return filter;
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

}
