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
package org.dsanderson.morctrailreport;

import org.dsanderson.xctrailreport.core.android.IAbstractListEntryFactory;
import org.dsanderson.xctrailreport.decorators.ITrailReportListEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 */
public class ListEntryFactory implements IAbstractListEntryFactory {

	static ListEntryFactory instance = null;

	static ListEntryFactory getInstance() {
		if (instance == null)
			instance = new ListEntryFactory();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.android.IAbstractListEntryFactory#inflate
	 * (android.view.ViewGroup)
	 */
	public View inflate(Context context, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.relativerow, parent, false);
		return layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.android.IAbstractListEntryFactory#
	 * newListEntry(android.view.View)
	 */
	public ITrailReportListEntry newListEntry(View view) {
		return new TrailReportListEntry((ViewGroup) view);
	}

}
