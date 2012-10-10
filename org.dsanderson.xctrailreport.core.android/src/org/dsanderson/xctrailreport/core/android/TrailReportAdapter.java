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

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IDecoratorFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.decorators.ITrailReportListEntry;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * 
 */
public class TrailReportAdapter extends CursorAdapter {

	private final IAbstractFactory factory;
	private final TrailReportList trailReports;
	private final IAbstractListEntryFactory listEntryFactory;
	private final IDecoratorFactory decoratorFactory;

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public TrailReportAdapter(Context context, Cursor cursor,
			IAbstractFactory factory, IDecoratorFactory decoratorFactory,
			TrailReportList trailReports,
			IAbstractListEntryFactory listEntryFactory) {
		super(context, cursor);
		this.factory = factory;
		this.decoratorFactory = decoratorFactory;
		this.trailReports = trailReports;
		this.listEntryFactory = listEntryFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		return listEntryFactory.inflate(context, parent);

		/*
		 * final LayoutInflater inflater = LayoutInflater.from(context); View
		 * layout = inflater.inflate(textViewResourceId, parent, false);
		 * 
		 * return layout;
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TrailReport currentReport = (TrailReport) trailReports.get(cursor);

		if (currentReport != null) {
			ITrailReportListEntry listEntry = listEntryFactory
					.newListEntry(view);

			boolean newTrail = false;
			if (cursor.moveToPrevious()) {
				TrailReport previousReport = (TrailReport) trailReports
						.get(cursor);

				if (previousReport.getTrailInfo().getName()
						.compareTo(currentReport.getTrailInfo().getName()) != 0) {
					newTrail = true;
				}
				TrailInfo previousInfo = previousReport.getTrailInfo();
				for (ISourceSpecificTrailInfo specificInfo : previousInfo
						.getSourceSpecificInfos()) {
					specificInfo.deleteItem();
				}
				factory.getTrailInfoPool().deleteItem(previousInfo);
				factory.getTrailReportPool().deleteItem(previousReport);
			} else {
				newTrail = true;
			}

			listEntry.setTitleGroupVisible(newTrail);
			if (newTrail)
				decoratorFactory.getTrailInfoDecorators().decorate(currentReport,
						listEntry);

			decoratorFactory.getTrailReportDecorators().decorate(currentReport,
					listEntry);

			listEntry.draw();

			TrailInfo currentInfo = currentReport.getTrailInfo();
			for (ISourceSpecificTrailInfo specificInfo : currentInfo
					.getSourceSpecificInfos()) {
				specificInfo.deleteItem();
			}
			factory.getTrailInfoPool().deleteItem(currentInfo);
			factory.getTrailReportPool().deleteItem(currentReport);
		}
	}

}