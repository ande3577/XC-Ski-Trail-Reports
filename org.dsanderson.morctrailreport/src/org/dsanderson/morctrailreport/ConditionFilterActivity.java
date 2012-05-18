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


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 
 */
public class ConditionFilterActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.condition_filter_preferences);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.condition_filter_menu, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	    int versionNumber = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
	    if (versionNumber >= Build.VERSION_CODES.HONEYCOMB) {
	        ActionBar actionBar = this.getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	    }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.selectAll:
			applyToAllConditions(true);
			return true;

		case R.id.deselectAll:
			applyToAllConditions(false);
			return true;
		}
		return false;
	}

	private void applyToAllConditions(boolean newValue) {
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(getApplication());
		Editor editor = preference.edit();
		for (String condition : MorcSettingsSource.conditionsKeys) {
			editor.putBoolean(condition, newValue);
		}
		editor.commit();
		
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		
		overridePendingTransition(0, 0);
		startActivity(intent);
	}
}
