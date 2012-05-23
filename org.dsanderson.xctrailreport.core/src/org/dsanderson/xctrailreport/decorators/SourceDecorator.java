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
package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.ITextItem;
import org.dsanderson.xctrailreport.core.TrailReport;

/**
 * 
 */
public class SourceDecorator extends TrailReportDecorator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.decorators.TrailReportDecorator#decorate
	 * (org.dsanderson.xctrailreport.core.TrailReport,
	 * org.dsanderson.xctrailreport.decorators.ITrailReportListEntry)
	 */
	@Override
	public void decorate(TrailReport trailReport,
			ITrailReportListEntry listEntry) {
		ITextItem newTextItem = listEntry.getSourceTextItem();
		String text = "";

		if (trailReport.getSource().length() > 0) {
			text = "(" + trailReport.getSource() + ")";
		} else {
		}

		newTextItem.setText(text);
		if (next() != null)
			next().decorate(trailReport, listEntry);
	}

}
