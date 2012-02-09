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
import org.dsanderson.xctrailreport.core.ITrailInfoParser;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.TrailReportDecorator;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.decorators.AuthorDecorator;
import org.dsanderson.xctrailreport.decorators.CityStateDecorator;
import org.dsanderson.xctrailreport.decorators.DateDecorator;
import org.dsanderson.xctrailreport.decorators.DetailedReportDecorator;
import org.dsanderson.xctrailreport.decorators.DistanceDecorator;
import org.dsanderson.xctrailreport.decorators.SummaryDecorator;
import org.dsanderson.xctrailreport.decorators.TrailNameDecorator;

import android.content.Context;

/**
 * 
 */
public class TrailReportFactory implements IAbstractFactory {
	static TrailReportFactory factory = null;
	Context context;
	SkinnyskiFactory skinnyskiFactory;
	TrailInfoParser parser = null;
	CompoundReportRetriever reportRetriever = null;
	UrlConnection netConnection = null;
	TrailReportDecorator infoDecorator = null;
	TrailReportDecorator reportDecorator = null;
	LocationSource locationSource = null;
	DistanceSource directionsSource = null;
	UserSettings userSettings = null;
	UserSettingsSource settingsSource = null;
	LocationCoder locationCoder = null;

	public TrailReportFactory(Context context, SkinnyskiFactory skinnyskiFactory) {
		assert (factory == null);
		factory = this;
		this.context = context;
		this.skinnyskiFactory = skinnyskiFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoParser()
	 */
	public ITrailInfoParser getTrailInfoParser() {
		if (parser == null) {
			parser = new TrailInfoParser();
		}
		return parser;
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
		if (reportRetriever == null) {
			reportRetriever = new CompoundReportRetriever();
			reportRetriever.addRetriever(skinnyskiFactory
					.getSkinnyskiReportRetriever(this));
		}
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
		if (directionsSource == null) {
			directionsSource = new DistanceSource(this);
		}
		return directionsSource;
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
	public IReportFilter getFilter() {
		CompoundFilter filter = new CompoundFilter();
		if (userSettings.getDistanceFilterEnabled())
			filter.add(new DistanceFilter(userSettings.getFilterDistance()));
		if (userSettings.getDurationFilterEnabled())
			filter.add(new DurationFilter(userSettings.getDurationCutoff()));
		if (userSettings.getDateFilterEnabled())
			filter.add(new DateFilter(userSettings.getFilterAge()));
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getLocationCoder()
	 */
	public ILocationCoder getLocationCoder() {
		if (locationCoder == null)
			locationCoder = new LocationCoder(context);
		return locationCoder;
	}
}
