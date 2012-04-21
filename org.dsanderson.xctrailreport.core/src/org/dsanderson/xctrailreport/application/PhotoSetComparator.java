package org.dsanderson.xctrailreport.application;
import java.util.Comparator;

import org.dsanderson.xctrailreport.core.TrailReport;

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

/**
 * 
 */
public class PhotoSetComparator implements Comparator<TrailReport> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(TrailReport arg0, TrailReport arg1) {
		if (arg0.getPhotosetUrl().length() > 0)
		{
			if (arg1.getPhotosetUrl().length() > 0)
				return 0;
			else
				return -1;
		}
		else if (arg1.getPhotosetUrl().length() > 0)
			return 1;
		else
			return 0;
	}

}
