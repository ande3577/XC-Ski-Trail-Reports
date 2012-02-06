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

import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.UserSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * 
 */
public class UserSettingsSource implements IUserSettingsSource {
	SharedPreferences preference;
	UserSettings settings;

	public UserSettingsSource(Context context, UserSettings settings) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference
				.registerOnSharedPreferenceChangeListener(preferenceChangedListener);
		this.settings = settings;
	}

	private OnSharedPreferenceChangeListener preferenceChangedListener = new OnSharedPreferenceChangeListener() {

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			setValue(key, sharedPreferences);
		}

	};

	private void setValue(String key, SharedPreferences sharedPreference) {
		if (key.compareTo("enableLocation") == 0) {
			boolean value = sharedPreference.getBoolean(key,
					settings.getLocationEnabled());
			settings.setLocationEnabled(value);
		} else if (key.compareTo("defaultLocation") == 0) {
			settings.setDefaultLocation(sharedPreference.getString(key,
					settings.getDefaultLocation()));
		} else if (key.compareTo("distanceFilterEnabled") == 0) {
			settings.setDistanceFilterEnabled(sharedPreference.getBoolean(key,
					settings.getDistanceFilterEnabled()));
		} else if (key.compareTo("filterDistance") == 0) {
			settings.setFilterDistance(sharedPreference.getInt(key,
					settings.getFilterDistance()));
		} else if (key.compareTo("dateFilterEnabled") == 0) {
			settings.setDateFilterEnabled(sharedPreference.getBoolean(key,
					settings.getDateFilterEnabled()));
		} else if (key.compareTo("filterAge") == 0) {
			settings.setFilterAge(sharedPreference.getInt(key,
					settings.getFilterAge()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#loadUserSettings()
	 */
	public void loadUserSettings() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#saveUserSettings()
	 */
	public void saveUserSettings() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#getUserSettings()
	 */
	public UserSettings getUserSettings() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IUserSettingsSource#setUserSettings
	 * (org.dsanderson.xctrailreport.core.UserSettings)
	 */
	public void setUserSettings(UserSettings userSettings) {
	}

}
