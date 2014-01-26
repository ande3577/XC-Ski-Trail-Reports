package org.dsanderson.xctrailreport;

import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.application.ReportListCreator;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoSources;
import org.dsanderson.xctrailreport.core.UserSettings.DistanceMode;
import org.dsanderson.xctrailreport.core.android.LoadReportsTask;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.core.android.TrailReportPrinter;
import org.dsanderson.android.util.AndroidIntent;
import org.dsanderson.android.util.AndroidProgressBar;
import org.dsanderson.android.util.Dialog;
import org.dsanderson.android.util.Maps;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiSpecificInfo;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class xctrailreportActivity extends SherlockListActivity {

	private TrailReportList trailReports;
	private TrailReportFactory factory = new TrailReportFactory(this);
	ReportListCreator listCreator = new ReportListCreator(factory);
	private String appName;
	private TrailReportPrinter printer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		appName = getString(R.string.app_name);

		trailReports = (TrailReportList) factory.getTrailReportList();
		printer = new TrailReportPrinter(this, factory,
				ReportDecoratorFactory.getInstance(), trailReports, appName,
				ListEntryFactory.getInstance());
		factory.getUserSettingsSource().loadUserSettings();

		String query = null;
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
			suggestions.saveRecentQuery(query.trim(), null);
		}
		trailReports.searchString(query);

		refresh(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSherlock().getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		menu.findItem(R.id.sortByDistance).setVisible(showSortByDistanceMenu());
		menu.findItem(R.id.sortByDuration).setVisible(showSortByDurationMenu());
		return true;
	}
	
	private boolean showSortByDistanceMenu() {
		return factory.getUserSettings().getDistanceMode() != DistanceMode.DISABLED;
	}
	
	private boolean showSortByDurationMenu() {
		return factory.getUserSettings().getDistanceMode() == DistanceMode.FULL;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferencesMenuItem: {
			openPreferencesMenu();
		}
			break;
		case R.id.refresh:
			refresh(true);
			break;
		case R.id.aboutMenuItem: {
			openAbout();
		}
			break;
		case R.id.composeMain: {
			ISourceSpecificFactory skinnyskiFactory = factory
					.getSourceSpecificFactory("Skinnyski");
			if (skinnyskiFactory != null)
				launchUrl(skinnyskiFactory.getDefaultComposeUrl());
		}
			break;
		case R.id.request: {
			ISourceSpecificFactory skinnyskiFactory = factory
					.getSourceSpecificFactory("Skinnyski");
			if (skinnyskiFactory != null)
				launchUrl(skinnyskiFactory.getDefaultRequestUrl());
		}
			break;
		case R.id.sortBy:
			MenuItem distance = item.getSubMenu().getItem(0).setChecked(false);
			MenuItem date = item.getSubMenu().getItem(1).setChecked(false);
			MenuItem duration = item.getSubMenu().getItem(2).setChecked(false);
			MenuItem photoset = item.getSubMenu().getItem(3).setChecked(false);

			switch (factory.userSettings.getSortMethod()) {
			case SORT_BY_DISTANCE:
				distance.setChecked(true);
				break;
			case SORT_BY_DATE:
				date.setChecked(true);
				break;
			case SORT_BY_DURATION:
				duration.setChecked(true);
				break;
			case SORT_BY_PHOTOSET:
				photoset.setChecked(true);
				break;
			default:
				break;
			}
			break;
		case R.id.sortByDuration:
		case R.id.sortByDate:
		case R.id.sortByDistance:
		case R.id.sortByPhotoset:

			String sortMethodString = "";

			switch (item.getItemId()) {
			case R.id.sortByDuration:
				sortMethodString = "sortByDuration";
				break;
			case R.id.sortByDate:
				sortMethodString = "sortByDate";
				break;
			case R.id.sortByDistance:
				sortMethodString = "sortByDistance";
				break;
			case R.id.sortByPhotoset:
				sortMethodString = "sortByPhotoset";
				break;
			}

			Editor edit = PreferenceManager.getDefaultSharedPreferences(
					getApplication()).edit();
			edit.putString("sortMethod", sortMethodString);
			edit.commit();
			break;
		case R.id.search:
			onSearchRequested();
			break;
		default:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	// / Launch Preference activity
	private void openPreferencesMenu() {
		Intent i = new Intent(this, PreferencesActivity.class);
		startActivity(i);
	}

	// / Launch about menu activity
	private void openAbout() {
		Intent i = new Intent(this, AboutActivity.class);
		startActivity(i);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && factory.getUserSettings().getRedrawNeeded()) {
			try {
				factory.getUserSettings().setRedrawNeeded(false);
				printer.printTrailReports();
			} catch (Exception e) {
				e.printStackTrace();
				Dialog dialog = new Dialog(this, e);
				dialog.show();
			}
		}
	}

	// / adding this to prevent rescrolling on orientation changed
	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	protected void onDestroy() {
		trailReports.close();
		super.onDestroy();
	}

	private void refresh(boolean forced) {
		factory.getUserSettings().setForcedRefresh(forced);

		factory.getLocationSource().updateLocation();

		new LoadReportsTask(this, factory, listCreator, trailReports,
				factory.getTrailInfoList(), printer, new AndroidProgressBar(
						this)).execute();
	}

	private TrailInfo infoFromButton(View view) {
		ViewGroup buttonLayout = (ViewGroup) view.getParent();
		ViewGroup parentView = (ViewGroup) buttonLayout.getParent();

		String trailName = ((TextView) parentView
				.findViewById(R.id.trailNameView)).getText().toString();
		return trailReports.find(trailName).getTrailInfo();
	}

	public void onMapButtonClick(View view) {
		TrailInfo info = infoFromButton(view);
		if (info == null)
			return;
		Maps.launchMap(info.getLocation(), info.getName(),
				info.getSpecificLocation(), this);
	}

	public void onComposeButtonClick(View view) {
		TrailInfo info = infoFromButton(view);
		if (info == null)
			return;
		ISourceSpecificTrailInfo sourceSpecific = info
				.getSourceSpecificInfo(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
		if (sourceSpecific != null) {
			String composeUrl = sourceSpecific.getComposeUrl();
			if (composeUrl != null && composeUrl.length() > 0)
				AndroidIntent.launchIntent(composeUrl, this);
		}
	}

	public void onInfoButtonClick(View view) {
		TrailInfo info = infoFromButton(view);
		if (info == null)
			return;

		final TrailInfoSources trailInfoSources = new TrailInfoSources(factory,
				info);
		if (trailInfoSources.isEmpty())
			return;

		final String[] sourceNames = trailInfoSources.getTrailInfoSources();

		if (trailInfoSources.size() == 1) {
			String trailInfoUrl = trailInfoSources
					.getTrailInfoUrl(sourceNames[0]);
			if (trailInfoUrl != null && trailInfoUrl.length() > 0)
				launchUrl(trailInfoUrl);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(info.getName() + " Trail Info");
			builder.setItems(sourceNames, new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (which >= sourceNames.length)
						return;

					launchUrl(trailInfoSources
							.getTrailInfoUrl(sourceNames[which]));

					dialog.dismiss();
				}
			});
			builder.show();
		}
	}

	public void onReportMoreButtonClick(final View v) {
		final TrailInfo info = infoFromButton(v);

		ISourceSpecificTrailInfo sourceSpecificTrailInfo = info
				.getSourceSpecificInfo(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);

		if (sourceSpecificTrailInfo == null)
			return;

		SkinnyskiSpecificInfo skinnyskiInfo = (SkinnyskiSpecificInfo) sourceSpecificTrailInfo;

		final List<String> options = new ArrayList<String>();

		final String composeUrl = skinnyskiInfo.getComposeUrl();
		if (composeUrl != null && composeUrl.length() > 0)
			options.add("Compose Report");

		final String requestUrl = skinnyskiInfo.getRequestUrl();
		if (requestUrl != null && requestUrl.length() > 0)
			options.add("Request a Report");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(info.getName());
		String[] optionsArray = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			optionsArray[i] = options.get(i);
		}
		builder.setItems(optionsArray, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (which >= options.size())
					return;

				String url = null;
				String option = (String) options.get(which);
				if (option.equals("Compose Report")) {
					url = composeUrl;
				} else if (option.equals("Request a Report")) {
					url = requestUrl;
				} else {
					Toast.makeText(getApplicationContext(),
							(CharSequence) "Invalid selection: " + option,
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
				if (url != null)
					launchUrl(url);
			}
		});
		builder.show();

	}

	public void onPhotosetImageClick(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();
		TextView photosetTextView = (TextView) parent
				.findViewById(R.id.photosetView);
		String url = (String) photosetTextView.getText();
		launchUrl(url);

	}

	private void launchUrl(String url) {
		AndroidIntent.launchIntent(url, this);
	}

}
