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
package org.dsanderson.xctrailreport;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ILocationSource;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.Units;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.UserSettings.SortMethod;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * 
 */
public class UserSettingsSource implements IUserSettingsSource {
	SharedPreferences preference;
	UserSettings settings;
	IAbstractFactory factory;

	public UserSettingsSource(Context context, IAbstractFactory factory) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference
				.registerOnSharedPreferenceChangeListener(preferenceChangedListener);
		this.settings = factory.getUserSettings();
		this.factory = factory;
	}

	private OnSharedPreferenceChangeListener preferenceChangedListener = new OnSharedPreferenceChangeListener() {

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.compareTo("enableLocation") == 0) {
				boolean value = sharedPreferences.getBoolean(key,
						settings.getLocationEnabled());
				settings.setLocationEnabled(value);
			} else if (key.compareTo("defaultLocation") == 0) {
				String locationString = sharedPreferences.getString(key,
						settings.getDefaultLocation());
				settings.setDefaultLocation(locationString);
				ILocationSource location = factory.getLocationSource();
				if (!location.getHasNewLocation())
					location.setLocation(locationString);
			} else if (key.compareTo("distanceFilterEnabled") == 0) {
				settings.setDistanceFilterEnabled(sharedPreferences.getBoolean(
						key, settings.getDistanceFilterEnabled()));
			} else if (key.compareTo("filterDistance") == 0) {
				settings.setFilterDistance(Units.milesToMeters(getDouble(
						sharedPreferences, key,
						Units.metersToMiles(settings.getFilterDistance()))));
			} else if (key.compareTo("dateFilterEnabled") == 0) {
				settings.setDateFilterEnabled(sharedPreferences.getBoolean(key,
						settings.getDateFilterEnabled()));
			} else if (key.compareTo("filterAge") == 0) {
				settings.setFilterAge(getInt(sharedPreferences, key,
						settings.getFilterAge()));
			} else if (key.compareTo("sortMethod") == 0) {
				settings.setSortMethod(stringToSortMethod(sharedPreferences
						.getString(key,
								sortMethodToString(settings.getSortMethod()))));
			}
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#loadUserSettings()
	 */
	public void loadUserSettings() {
		settings.setLocationEnabled(preference.getBoolean("enableLocation",
				settings.getLocationEnabled()));
		settings.setDefaultLocation(preference.getString("defaultLocation",
				settings.getDefaultLocation()));
		settings.setDistanceFilterEnabled(preference.getBoolean(
				"distanceFilterEnabled", settings.getDistanceFilterEnabled()));
		settings.setFilterDistance(Units.milesToMeters(getDouble(preference,
				"filterDistance", settings.getFilterDistance())));
		settings.setDateFilterEnabled(preference.getBoolean(
				"dateFilterEnabled", settings.getDateFilterEnabled()));
		settings.setFilterAge(getInt(preference, "filterAge",
				settings.getFilterAge()));
		settings.setSortMethod(stringToSortMethod(preference.getString(
				"filterMethod", sortMethodToString(settings.getSortMethod()))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#saveUserSettings()
	 */
	public void saveUserSettings() {
		assert (false); // all saving should be done automatically by menu
	}

	private String sortMethodToString(SortMethod sortMethod) {
		switch (sortMethod) {
		case SORT_BY_DATE:
			return "sortByDate";
		case SORT_BY_DISTANCE:
			return "sortByDistance";
		}
		return null;
	}

	private SortMethod stringToSortMethod(String string) {
		if (string.compareTo("sortByDistance") == 0)
			return SortMethod.SORT_BY_DISTANCE;
		else
			return SortMethod.SORT_BY_DATE;
	}

	private double getDouble(SharedPreferences preference, String key,
			double defValue) {
		String string = preference.getString(key, Double.toString(defValue));
		return Double.parseDouble(string);
	}

	private int getInt(SharedPreferences preference, String key, int defValue) {
		String string = preference.getString(key, Integer.toString(defValue));
		return Integer.parseInt(string);
	}

}
