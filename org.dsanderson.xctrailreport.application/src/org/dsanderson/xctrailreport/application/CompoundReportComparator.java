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
package org.dsanderson.xctrailreport.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.dsanderson.xctrailreport.core.TrailNameComparator;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.UserSettings.SortMethod;
import org.dsanderson.xctrailreport.decorators.PhotosetDecorator;

/**
 * 
 */
public class CompoundReportComparator implements Comparator<TrailReport> {
	List<Comparator<TrailReport>> comparators;

	public CompoundReportComparator(SortMethod sortMethod) {
		comparators = new ArrayList<Comparator<TrailReport>>();

		switch (sortMethod) {
		case SORT_BY_DISTANCE:
			comparators.add(new DistanceComparator());
			comparators.add(new DurationComparator());
			comparators.add(new TrailNameComparator());
			comparators.add(new DateComparator());
			comparators.add(new PhotoSetComparator());
			break;
		case SORT_BY_DATE:
			comparators.add(new DateComparator());
			comparators.add(new DistanceComparator());
			comparators.add(new DurationComparator());
			comparators.add(new TrailNameComparator());
			comparators.add(new PhotoSetComparator());
			break;
		case SORT_BY_DURATION:
			comparators.add(new DurationComparator());
			comparators.add(new DistanceComparator());
			comparators.add(new TrailNameComparator());
			comparators.add(new DateComparator());
			comparators.add(new PhotoSetComparator());
			break;
		case SORT_BY_PHOTOSET:
			comparators.add(new PhotoSetComparator());
			comparators.add(new DateComparator());
			comparators.add(new DistanceComparator());
			comparators.add(new DurationComparator());
			comparators.add(new TrailNameComparator());
			break;
			
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(TrailReport arg0, TrailReport arg1) {
		int comparisonResult = 0;
		for (Comparator<TrailReport> comparator : comparators) {
			if ((comparisonResult = comparator.compare(arg0, arg1)) != 0)
				break;
		}
		return comparisonResult;
	}
}
