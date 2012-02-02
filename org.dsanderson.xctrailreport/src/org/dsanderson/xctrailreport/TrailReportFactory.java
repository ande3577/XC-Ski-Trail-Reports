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

import org.dsanderson.xctrailreport.application.CompoundReportRetriever;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDistanceSource;
import org.dsanderson.xctrailreport.core.ILocationSource;
import org.dsanderson.xctrailreport.core.INetConnection;
import org.dsanderson.xctrailreport.core.IReportRetriever;
import org.dsanderson.xctrailreport.core.ITrailInfoParser;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.TrailInfoDecorator;
import org.dsanderson.xctrailreport.core.TrailReportDecorator;
import org.dsanderson.xctrailreport.decorators.AuthorDecorator;
import org.dsanderson.xctrailreport.decorators.CityStateDecorator;
import org.dsanderson.xctrailreport.decorators.DateDecorator;
import org.dsanderson.xctrailreport.decorators.DetailedReportDecorator;
import org.dsanderson.xctrailreport.decorators.DistanceDecorator;
import org.dsanderson.xctrailreport.decorators.SummaryDecorator;
import org.dsanderson.xctrailreport.decorators.TrailNameDecorator;
import org.dsanderson.xctrailreport.decorators.TrailReportDecorators;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiReportRetriever;

import android.content.Context;

/**
 * 
 */
public class TrailReportFactory implements IAbstractFactory {
	Context context;
	BaseFeedParser parser = null;
	CompoundReportRetriever reportRetriever = null;
	UrlConnection netConnection = null;
	TrailInfoDecorator infoDecorator = null;
	LocationSource locationSource = null;
	DistanceSource directionsSource = null;

	public TrailReportFactory(Context context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoParser()
	 */
	public ITrailInfoParser getTrailInfoParser() {
		if (parser == null) {
			parser = new BaseFeedParser();
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
			locationSource = new LocationSource(context);
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
		// TODO Auto-generated method stub
		return null;
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
			reportRetriever.addRetriever(new SkinnyskiReportRetriever(this));
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
	public TrailInfoDecorator getTrailInfoDecorators() {
		if (infoDecorator == null) {
			infoDecorator = new TrailNameDecorator();
			infoDecorator.add(new CityStateDecorator());
			infoDecorator.add(new DistanceDecorator());

			TrailReportDecorator trailReportDecorator = new DateDecorator();
			trailReportDecorator.add(new SummaryDecorator());
			trailReportDecorator.add(new DetailedReportDecorator());
			trailReportDecorator.add(new AuthorDecorator());

			infoDecorator.add(new TrailReportDecorators(trailReportDecorator));
		}

		return infoDecorator;
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

}
