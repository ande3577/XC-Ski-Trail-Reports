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

import org.dsanderson.util.ICompoundLocationSource;
import org.dsanderson.util.ILocationSource;
import org.dsanderson.util.IUserSettingsSource;
import org.dsanderson.util.Units;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.UserSettings;
import org.dsanderson.xctrailreport.core.UserSettings.AutoRefreshMode;
import org.dsanderson.xctrailreport.core.UserSettings.DistanceMode;
import org.dsanderson.xctrailreport.core.UserSettings.SortMethod;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;
import org.dsanderson.xctrailreport.threerivers.ThreeRiversFactory;

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
	UserSettings settings;
	IAbstractFactory factory;
	final Context context;
	final ICompoundLocationSource locationSource;

	public UserSettingsSource(Context context, IAbstractFactory factory) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference
				.registerOnSharedPreferenceChangeListener(preferenceChangedListener);
		this.settings = factory.getUserSettings();
		this.factory = factory;
		this.context = context;
		this.locationSource = factory.getLocationSource();
	}

	private OnSharedPreferenceChangeListener preferenceChangedListener = new OnSharedPreferenceChangeListener() {

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			settings.setRedrawNeeded(true);
			
			if (key.equals("enableLocation")) {
				boolean value = sharedPreferences.getBoolean(key,
						locationSource.getLocationEnabled());
				locationSource.setLocationEnabled(value);
			} else if (key.equals("defaultLocation")) {
				String locationString = sharedPreferences.getString(key,
						locationSource.getDefaultLocation());
				locationSource.setDefaultLocation(locationString);
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
			} else if (key.equals("photosetFilterEnabled")) {
				settings.setPhotsetFilterEnabled(sharedPreferences.getBoolean(
						key, settings.getPhotosetFilterEnabled()));
			} else if (key.equals("autoRefreshMode")) {
				settings.setAutoRefreshMode(stringToAutoRefreshMode(sharedPreferences
						.getString(key, autoRefreshModeToString(settings
								.getAutoRefreshMode()))));
			} else if (key.equals("autoRefreshCutoff")) {
				settings.setAutoRefreshCutoff(Units
						.hoursToMilliseconds(getDouble(sharedPreferences, key,
								settings.getAutoRefreshCutoff())));
			} else if (key.equals("threeRiversEnabled")) {
				ISourceSpecificFactory threeRiversSource = factory
						.getSourceSpecificFactory(ThreeRiversFactory.SOURCE_NAME);
				if (threeRiversSource != null)
					threeRiversSource.setEnabled(sharedPreferences.getBoolean(
							key, threeRiversSource.getEnabled()));
			} else if (key.equals("distanceMode")) {
				settings.setDistanceMode(stringToDistanceMode(preference.getString(
						"distanceMode", distanceModeToString(DistanceMode.FULL))));
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
		locationSource.setLocationEnabled(preference.getBoolean("enableLocation",
				locationSource.getLocationEnabled()));
		locationSource.setDefaultLocation(preference.getString("defaultLocation",
				locationSource.getDefaultLocation()));
		settings.setDistanceFilterEnabled(preference.getBoolean(
				"distanceFilterEnabled", settings.getDistanceFilterEnabled()));
		settings.setFilterDistance(Units.milesToMeters(getDouble(preference,
				"filterDistance", settings.getFilterDistance())));
		settings.setDateFilterEnabled(preference.getBoolean(
				"dateFilterEnabled", settings.getDateFilterEnabled()));
		settings.setFilterAge(getInt(preference, "filterAge",
				settings.getFilterAge()));
		settings.setSortMethod(stringToSortMethod(preference.getString(
				"sortMethod", sortMethodToString(settings.getSortMethod()))));
		settings.setDurationFilterEnabled(preference.getBoolean(
				"durationFilterEnabled", settings.getDurationFilterEnabled()));
		settings.setDurationCutoff(Units.minutesToSeconds(getInt(preference,
				"durationFilterCutoff",
				(int) Units.secondsToMinutes(settings.getDurationCutoff()))));
		settings.setPhotsetFilterEnabled(preference.getBoolean(
				"photosetFilterEnabled", settings.getPhotosetFilterEnabled()));
		settings.setAutoRefreshMode(stringToAutoRefreshMode(preference
				.getString("autoRefreshMode",
						autoRefreshModeToString(settings.getAutoRefreshMode()))));
		settings.setAutoRefreshCutoff(Units.hoursToMilliseconds(getDouble(
				preference, "autoRefreshCutoff",
				Units.millisecondsToHours(settings.getAutoRefreshCutoff()))));
		settings.setDistanceMode(stringToDistanceMode(preference.getString(
				"distanceMode", distanceModeToString(DistanceMode.FULL))));

		ISourceSpecificFactory threeRiversSource = factory
				.getSourceSpecificFactory(ThreeRiversFactory.SOURCE_NAME);
		if (threeRiversSource != null)
			threeRiversSource.setEnabled(preference.getBoolean(
					"threeRiversEnabled", threeRiversSource.getEnabled()));

		ISourceSpecificFactory skinnyskiSource = factory
				.getSourceSpecificFactory(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
		if (skinnyskiSource != null)
			((SkinnyskiFactory) skinnyskiSource).getUserSettingsSource()
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
		case SORT_BY_PHOTOSET:
			return "sortByPhotoset";
		default:
			break;
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
		else if (string.equals("sortByPhotoset"))
			return SortMethod.SORT_BY_PHOTOSET;
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
