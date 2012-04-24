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

import java.text.DecimalFormat;

import org.dsanderson.util.IListEntry;
import org.dsanderson.util.ITextItem;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.Units;

/**
 * 
 */
public class DistanceDecorator extends TrailReportDecorator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.TrailInfoDecorator#decorate(org.dsanderson
	 * .xctrailreport.core.TrailInfo,
	 * org.dsanderson.xctrailreport.core.IListEntry)
	 */
	@Override
	public void decorate(TrailReport trailReportIter, ITrailReportListEntry listEntry) {
		TrailInfo trailInfo = trailReportIter.getTrailInfo();

		if (trailInfo.getDistanceValid()) {
			// add to end of previous (City, State)
			ITextItem textItem = listEntry.getTrailLocationTextItem();

			DecimalFormat formatter = new DecimalFormat("0.0");

			String text = textItem.getText()
					+ " ("
					+ formatter.format(Units.metersToMiles(trailInfo
							.getDistance())) + "mi , ";

			int minutes = (int) (Units
					.secondsToMinutes(trailInfo.getDuration()));

			if (minutes > 60) {
				text += (int) (minutes / 60) + " hrs ";
				minutes %= 60;
			}

			text += (int) minutes + " min)";

			textItem.setText(text);
		}

		if (next() != null) {
			next().decorate(trailReportIter, listEntry);
		}
	}
}
