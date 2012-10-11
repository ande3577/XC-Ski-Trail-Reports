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
package org.dsanderson.xctrailreport.core.android;

import java.util.Date;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDecoratorFactory;

import android.app.ListActivity;
import android.database.Cursor;
import android.text.format.Time;

/**
 * 
 */
public class TrailReportPrinter implements IPrinter {

	private final ListActivity context;
	private final IAbstractFactory factory;
	private final IDecoratorFactory decoratorFactory;
	private final TrailReportList trailReports;
	private final String appName;
	private final IAbstractListEntryFactory listEntryFactory;

	public TrailReportPrinter(ListActivity context, IAbstractFactory factory,
			IDecoratorFactory decoratorFactory, TrailReportList trailReports,
			String appName, IAbstractListEntryFactory listEntryFactory) {
		this.context = context;
		this.factory = factory;
		this.decoratorFactory = decoratorFactory;
		this.appName = appName;
		this.listEntryFactory = listEntryFactory;

		this.trailReports = trailReports;
	}

	public void printTrailReports() throws Exception {
		Date lastRefreshDate = trailReports.getTimestamp();
		String titleString = appName;
		if (lastRefreshDate != null && lastRefreshDate.getTime() != 0) {
			Time time = new Time();
			time.set(lastRefreshDate.getTime());
			titleString += time.format(" (%b %e, %r)");
		}
		context.setTitle(titleString);

		factory.filterReports(trailReports);
		factory.sortReports(trailReports);

		Cursor cursor = ((TrailReportList) trailReports).getCursor();
		context.setListAdapter(new TrailReportAdapter(context, cursor, factory,
				decoratorFactory, trailReports, listEntryFactory));
	}
}
