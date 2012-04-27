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

import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.util.ILocationSource;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.Units;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.UserSettings.AutoRefreshMode;
import org.dsanderson.xctrailreport.core.UserSettings.DistanceMode;
import org.dsanderson.xctrailreport.core.UserSettings.SortMethod;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * 
 */
public class UserSettingsSource implements IUserSettingsSource {
	SharedPreferences preference;
	final UserSettings settings;
	final IAbstractFactory factory;
	final MorcSpecificSettings morcSpecificSettings;
	final Context context;

	public UserSettingsSource(Context context, IAbstractFactory factory,
			MorcSpecificSettings morcSpecificSettings) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference
				.registerOnSharedPreferenceChangeListener(preferenceChangedListener);
		this.settings = factory.getUserSettings();
		this.factory = factory;
		this.morcSpecificSettings = morcSpecificSettings;
		this.context = context;
	}

	private OnSharedPreferenceChangeListener preferenceChangedListener = new OnSharedPreferenceChangeListener() {

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.equals("enableLocation")) {
				boolean value = sharedPreferences.getBoolean(key,
						settings.getLocationEnabled());
				settings.setLocationEnabled(value);
			} else if (key.equals("defaultLocation")) {
				String locationString = sharedPreferences.getString(key,
						settings.getDefaultLocation());
				settings.setDefaultLocation(locationString);
				ILocationSource location = factory.getLocationSource();
				if (!location.getHasNewLocation())
					location.setLocation(locationString);
			} else if (key.equals("distanceFilterEnabled")) {
				settings.setDistanceFilterEnabled(sharedPreferences.getBoolean(
						key, settings.getDistanceFilterEnabled()));
			} else if (key.equals("filterDistance")) {
				settings.setFilterDistance(Units.milesToMeters(getDouble(
						sharedPreferences, key,
						Units.metersToMiles(settings.getFilterDistance()))));
			} else if (key.equals("dateFilterEnabled")) {
				settings.setDateFilterEnabled(sharedPreferences.getBoolean(key,
						settings.getDateFilterEnabled()));
			} else if (key.equals("filterAge")) {
				settings.setFilterAge(getInt(sharedPreferences, key,
						settings.getFilterAge()));
			} else if (key.equals("sortMethod")) {
				settings.setSortMethod(stringToSortMethod(sharedPreferences
						.getString(key,
								sortMethodToString(settings.getSortMethod()))));
			} else if (key.equals("durationFilterEnabled")) {
				settings.setDurationFilterEnabled(sharedPreferences.getBoolean(
						key, settings.getDateFilterEnabled()));
			} else if (key.equals("durationFilterCutoff")) {
				int cutoffMinutes = getInt(sharedPreferences, key,
						(int) Units.secondsToMinutes(settings
								.getDurationCutoff()));
				settings.setDurationCutoff(Units
						.minutesToSeconds(cutoffMinutes));
			} else if (key.equals("conditionFilterEnabled")) {
				morcSpecificSettings
						.setConditionsFilterEnabled(sharedPreferences
								.getBoolean(key, morcSpecificSettings
										.getConditionsFilterEnabled()));
			} else if (key.equals("dryEnabled")) {
				morcSpecificSettings.setDryEnabled(sharedPreferences
						.getBoolean(key, morcSpecificSettings.getDryEnabled()));
			} else if (key.equals("tackyEnabled")) {
				morcSpecificSettings
						.setTackyEnabled(sharedPreferences.getBoolean(key,
								morcSpecificSettings.getTackyEnabled()));
			} else if (key.equals("dampEnabled")) {
				morcSpecificSettings
						.setDampEnabled(sharedPreferences.getBoolean(key,
								morcSpecificSettings.getDampEnabled()));
			} else if (key.equals("wetEnabled")) {
				morcSpecificSettings.setWetEnabled(sharedPreferences
						.getBoolean(key, morcSpecificSettings.getWetEnabled()));
			} else if (key.equals("closedEnabled")) {
				morcSpecificSettings.setClosedEnabled(sharedPreferences
						.getBoolean(key,
								morcSpecificSettings.getClosedEnabled()));
			} else if (key.equals("otherEnabled")) {
				morcSpecificSettings
						.setOtherEnabled(sharedPreferences.getBoolean(key,
								morcSpecificSettings.getOtherEnabled()));
			} else if (key.equals("autoRefreshMode")) {
				settings.setAutoRefreshMode(stringToAutoRefreshMode(sharedPreferences
						.getString(key, autoRefreshModeToString(settings
								.getAutoRefreshMode()))));
			} else if (key.equals("autoRefreshCutoff")) {
				settings.setAutoRefreshCutoff(Units
						.hoursToMilliseconds(getDouble(sharedPreferences, key,
								settings.getAutoRefreshCutoff())));
			} else if (key.equals("distanceMode")) {
				settings.setDistanceMode(stringToDistanceMode(sharedPreferences
						.getString(key, distanceModeToString(DistanceMode.FULL))));
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
		settings.setSortMethod(stringToSortMethod(preference.getString(
				"sortMethod", sortMethodToString(settings.getSortMethod()))));
		settings.setDurationFilterEnabled(preference.getBoolean(
				"durationFilterEnabled", settings.getDurationFilterEnabled()));
		settings.setDurationCutoff(Units.minutesToSeconds(getInt(preference,
				"durationFilterCutoff",
				(int) Units.secondsToMinutes(settings.getDurationCutoff()))));
		settings.setAutoRefreshMode(stringToAutoRefreshMode(preference
				.getString("autoRefreshMode",
						autoRefreshModeToString(settings.getAutoRefreshMode()))));
		settings.setAutoRefreshCutoff(Units.hoursToMilliseconds(getDouble(
				preference, "autoRefreshCutoff",
				Units.millisecondsToHours(settings.getAutoRefreshCutoff()))));
		settings.setDistanceMode(stringToDistanceMode(preference.getString(
				"distanceMode", distanceModeToString(DistanceMode.FULL))));

		morcSpecificSettings.setConditionsFilterEnabled(preference.getBoolean(
				"conditionFilterEnabled",
				morcSpecificSettings.getConditionsFilterEnabled()));
		morcSpecificSettings.setDryEnabled(preference.getBoolean("dryEnabled",
				morcSpecificSettings.getDryEnabled()));
		morcSpecificSettings.setTackyEnabled(preference.getBoolean(
				"tackyEnabled", morcSpecificSettings.getTackyEnabled()));
		morcSpecificSettings.setDampEnabled(preference.getBoolean(
				"dampEnabled", morcSpecificSettings.getDampEnabled()));
		morcSpecificSettings.setWetEnabled(preference.getBoolean("wetEnabled",
				morcSpecificSettings.getWetEnabled()));
		morcSpecificSettings.setClosedEnabled(preference.getBoolean(
				"closedEnabled", morcSpecificSettings.getClosedEnabled()));
		morcSpecificSettings.setOtherEnabled(preference.getBoolean(
				"otherEnabled", morcSpecificSettings.getOtherEnabled()));

		ISourceSpecificFactory morcSource = factory
				.getSourceSpecificFactory(MorcFactory.SOURCE_NAME);
		if (morcSource != null)
			((MorcFactory) morcSource).getUserSettingsSource()
					.loadUserSettings();
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
		case SORT_BY_DURATION:
			return "sortByDuration";
		}
		return null;
	}

	private SortMethod stringToSortMethod(String string) {
		if (string.equals("sortByDistance"))
			return SortMethod.SORT_BY_DISTANCE;
		else if (string.equals("sortByDate"))
			return SortMethod.SORT_BY_DATE;
		else if (string.equals("sortByDuration"))
			return SortMethod.SORT_BY_DURATION;
		else
			return SortMethod.SORT_BY_DATE;
	}

	private String autoRefreshModeToString(AutoRefreshMode mode) {
		switch (mode) {
		case ALWAYS:
			return "always";
		case NEVER:
			return "never";
		case IF_OUT_OF_DATE:
			return "ifOutOfDate";
		}
		return null;
	}

	private AutoRefreshMode stringToAutoRefreshMode(String string) {
		if (string.equals("always"))
			return AutoRefreshMode.ALWAYS;
		else if (string.equals("never"))
			return AutoRefreshMode.NEVER;
		else
			return AutoRefreshMode.IF_OUT_OF_DATE;
	}

	private DistanceMode stringToDistanceMode(String string) {
		Resources res = context.getResources();
		String distanceModes[] = res.getStringArray(R.array.distanceModes);
		if (string.equals(distanceModes[1]))
			return DistanceMode.QUICK;
		else if (string.equals(distanceModes[2]))
			return DistanceMode.DISABLED;
		else
			return DistanceMode.FULL;
	}

	private String distanceModeToString(DistanceMode distanceMode) {
		Resources res = context.getResources();
		String distanceModes[] = res.getStringArray(R.array.distanceModes);
		switch (distanceMode) {
		default:
		case FULL:
			return distanceModes[0];
		case QUICK:
			return distanceModes[1];
		case DISABLED:
			return distanceModes[2];
		}
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
