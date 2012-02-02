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
package org.dsanderson.xctrailreport.core;

/**
 * 
 */
public abstract class TrailInfoDecorator {

	private TrailInfoDecorator nextDecorator = null;

	public TrailInfoDecorator add(TrailInfoDecorator newDecorator) {
		if (nextDecorator == null) {
			nextDecorator = newDecorator;
			return nextDecorator;
		} else {
			return nextDecorator.add(newDecorator);
		}
	}

	public TrailInfoDecorator next() {
		return nextDecorator;
	}

	/**
	 * 
	 * @param trailInfo
	 * @param listEntry
	 * @return the next decorator in the list
	 */
	abstract public void decorate(TrailInfo trailInfo, IListEntry listEntry);
}
