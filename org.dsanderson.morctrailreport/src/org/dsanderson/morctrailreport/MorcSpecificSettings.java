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

/**
 * 
 */
public class MorcSpecificSettings {
	boolean conditionsFilterEnabled = false;
	boolean dryEnabled = true;
	boolean tackyEnabled = true;
	boolean dampEnabled = true;
	boolean wetEnabled = true;
	boolean closedEnabled = true;
	boolean otherEnabled = true;
	
	public void setConditionsFilterEnabled(boolean conditionsFilterEnabled) {
		this.conditionsFilterEnabled = conditionsFilterEnabled;
	}
	
	public boolean getConditionsFilterEnabled() {
		return conditionsFilterEnabled;
	}
	
	public void setDryEnabled(boolean dryEnabled) {
		this.dryEnabled = dryEnabled;
	}
	
	public boolean getDryEnabled() {
		return dryEnabled;
	}
	
	public void setTackyEnabled(boolean tackyEnabled) {
		this.tackyEnabled = tackyEnabled;
	}
	
	public boolean getTackyEnabled() {
		return tackyEnabled;
	}
	
	public void setDampEnabled(boolean dampEnabled) {
		this.dampEnabled = dampEnabled;
	}
	
	public boolean getDampEnabled() {
		return dampEnabled;
	}
	
	public void setWetEnabled(boolean wetEnabled) {
		this.wetEnabled = wetEnabled;
	}
	
	public boolean getWetEnabled() {
		return wetEnabled;
	}
	
	public void setClosedEnabled(boolean closedEnabled) {
		this.closedEnabled = closedEnabled;
	}
	
	public boolean getClosedEnabled() {
		return closedEnabled;
	}
	
	public void setOtherEnabled(boolean otherEnabled) {
		this.otherEnabled = otherEnabled;
	}
	
	public boolean getOtherEnabled() {
		return otherEnabled;
	}

}
