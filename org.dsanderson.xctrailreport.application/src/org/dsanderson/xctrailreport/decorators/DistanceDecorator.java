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

import org.dsanderson.xctrailreport.core.IListEntry;
import org.dsanderson.xctrailreport.core.ITextItem;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoDecorator;

/**
 * 
 */
public class DistanceDecorator extends TrailInfoDecorator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.TrailInfoDecorator#decorate(org.dsanderson
	 * .xctrailreport.core.TrailInfo,
	 * org.dsanderson.xctrailreport.core.IListEntry)
	 */
	@Override
	public void decorate(TrailInfo trailInfo, IListEntry listEntry) {
		if (trailInfo.getDirectionsValid()) {
			// add to end of previous (City, State)
			ITextItem textItem = listEntry.getTextItem();
			if (textItem == null) {
				textItem = listEntry.newTextItem();
			}

			String text = textItem.getText() + " (" + trailInfo.getDistance()
					+ " mi, " + trailInfo.getTravelTime() + ")";
			textItem.setText(text);
		}

		if (next() != null) {
			next().decorate(trailInfo, listEntry);
		}
	}
}
