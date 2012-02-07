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

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * 
 */
public class PreferencesActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference sources = findPreference("sources");
		sources.setOnPreferenceClickListener(sourceClickListener);

		Preference regions = findPreference("regions");
		regions.setOnPreferenceClickListener(regionClickListener);

	}

	private OnPreferenceClickListener sourceClickListener = new OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference pref) {

			Intent i = new Intent(getApplicationContext(), SourceActivity.class);
			startActivity(i);
			return false;
		}

	};

	private OnPreferenceClickListener regionClickListener = new OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference pref) {
			Intent i = new Intent(getApplicationContext(), RegionActivity.class);
			startActivity(i);
			return false;
		}

	};
	
}
