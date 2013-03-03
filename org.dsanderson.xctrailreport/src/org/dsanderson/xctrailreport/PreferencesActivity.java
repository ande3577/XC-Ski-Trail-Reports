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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.SearchRecentSuggestions;
import android.widget.Toast;

/**
 * 
 */
public class PreferencesActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.setDefaultValues(PreferencesActivity.this,
				R.xml.preferences, false);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			final Preference preference) {

		if (preference.getKey().equals("restoreDefaults")) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage("This will erase all user configured settings.\r\n\r\nContinue?");

			builder.setPositiveButton("Yes", onRestoreDefaultsClickListener);
			builder.setNegativeButton("No", onRestoreDefaultsClickListener);

			builder.show();

		} else if (preference.getKey().equals("sources")) {
			Intent i = new Intent(getApplicationContext(), SourceActivity.class);
			startActivity(i);
			return false;
		} else if (preference.getKey().equals("regions")) {
			Intent i = new Intent(getApplicationContext(), RegionActivity.class);
			startActivity(i);
			return false;
		} else if (preference.getKey().equals("clearSearchHistory")) {

			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SuggestionProvider.AUTHORITY,
					SuggestionProvider.MODE);
			suggestions.clearHistory();
			
			Toast.makeText(this, "Search history cleared.", Toast.LENGTH_SHORT).show();

		}
		return true;
	}

	private OnClickListener onRestoreDefaultsClickListener = new OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				TrailReportFactory.getInstance().getUserSettings()
						.setRedrawNeeded(true);

				Editor editor = PreferenceManager.getDefaultSharedPreferences(
						getApplication()).edit();
				editor.clear();
				editor.commit();
				PreferenceManager.setDefaultValues(PreferencesActivity.this,
						R.xml.preferences, true);

				PreferenceManager.setDefaultValues(PreferencesActivity.this,
						R.xml.region_preferences, true);

				PreferenceManager.setDefaultValues(PreferencesActivity.this,
						R.xml.source_preferences, true);

				PreferenceManager.setDefaultValues(PreferencesActivity.this,
						R.xml.hidden_preferences, true);

				TrailReportFactory.getInstance().getUserSettingsSource()
						.loadUserSettings();
				SkinnyskiAndroidFactory.getInstance().getUserSettingsSource()
						.loadUserSettings();

				Intent intent = getIntent();
				overridePendingTransition(0, 0);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				finish();

				overridePendingTransition(0, 0);
				startActivity(intent);

			}
		}
	};

}
