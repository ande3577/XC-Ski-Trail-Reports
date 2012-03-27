package org.dsanderson.xctrailreport;

import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.application.ReportListCreator;
import org.dsanderson.xctrailreport.core.ISourceSpecificFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.android.LoadReportsTask;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.core.android.TrailReportPrinter;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class xctrailreportActivity extends ListActivity {

	private TrailReportList trailReports;
	private TrailReportFactory factory = new TrailReportFactory(this);
	ReportListCreator listCreator = new ReportListCreator(factory);
	private String appName;
	private TrailReportPrinter printer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appName = getString(R.string.app_name);

		registerForContextMenu(getListView());

		trailReports = (TrailReportList) factory.getTrailReportList();
		printer = new TrailReportPrinter(this, factory, trailReports, appName,
				R.layout.row);

		try {
			factory.getUserSettingsSource().loadUserSettings();

			@SuppressWarnings("unchecked")
			final List<TrailReport> savedTrailReports = (List<TrailReport>) getLastNonConfigurationInstance();
			if (savedTrailReports == null)
				refresh(false);

		} catch (Exception e) {
			System.err.println(e);
			factory.newDialog(e).show();
		}

		// refresh();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		TrailReport report = getObjectFromMenuItemInfo((AdapterView.AdapterContextMenuInfo) menuInfo);
		if (report != null) {
			menu.add(Menu.NONE, R.id.openMapMenu, Menu.NONE, "Open Map");
			ISourceSpecificTrailInfo skinnyskiInfo = report.getTrailInfo()
					.getSourceSpecificInfo(
							SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
			if (skinnyskiInfo != null
					&& skinnyskiInfo.getTrailInfoUrl() != null
					&& skinnyskiInfo.getTrailInfoUrl().length() != 0) {
				menu.add(Menu.NONE, R.id.trailInfoMenu, Menu.NONE, "Trail Info");
			}

			menu.add(Menu.NONE, R.id.composeReportItem, Menu.NONE,
					"Compose Report");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TrailReport trailReport = getObjectFromMenuItemInfo((AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo());
		if (trailReport != null) {
			switch (item.getItemId()) {
			case R.id.trailInfoMenu: {
				ISourceSpecificTrailInfo sourceSpecific = trailReport
						.getTrailInfo().getSourceSpecificInfo(
								SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
				if (sourceSpecific != null) {
					String trailInfoUrl = sourceSpecific.getTrailInfoUrl();
					if (trailInfoUrl != null && trailInfoUrl.length() > 0)
						launchIntent(trailInfoUrl);
				}
			}
				return true;
			case R.id.openMapMenu:
				launchIntent("geo:" + trailReport.getTrailInfo().getLocation()
						+ "?z=14");
				return true;
			case R.id.composeReportItem: {
				ISourceSpecificTrailInfo sourceSpecific = trailReport
						.getTrailInfo().getSourceSpecificInfo(
								SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
				if (sourceSpecific != null) {
					String composeUrl = sourceSpecific.getTrailInfoUrl();
					if (composeUrl != null && composeUrl.length() > 0)
						composeUrl = SkinnyskiFactory.getInstance()
								.getDefaultComposeUrl();

					launchIntent(composeUrl);
				}
			}
				return true;
			default:
				break;
			}
		}

		return super.onContextItemSelected(item);
	}

	private TrailReport getObjectFromMenuItemInfo(AdapterContextMenuInfo info) {
		if (trailReports.size() > 0) {
			return (TrailReport) trailReports.getById(info.id);
		} else {
			return null;
		}
	}

	private void launchIntent(String uriString) {
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferencesMenuItem: {
			// Launch Preference activity
			Intent i = new Intent(xctrailreportActivity.this,
					PreferencesActivity.class);
			startActivity(i);
		}
			break;
		case R.id.refresh:
			refresh(true);
			break;
		case R.id.aboutMenuItem: {
			Intent i = new Intent(xctrailreportActivity.this,
					AboutActivity.class);
			startActivity(i);
		}
			break;
		case R.id.composeMain: {
			ISourceSpecificFactory skinnyskiFactory = factory
					.getSourceSpecificFactory("Skinnyski");
			if (skinnyskiFactory != null)
				launchIntent(skinnyskiFactory.getDefaultComposeUrl());
		}
			break;
		case R.id.request: {
			ISourceSpecificFactory skinnyskiFactory = factory
					.getSourceSpecificFactory("Skinnyski");
			if (skinnyskiFactory != null)
				launchIntent(skinnyskiFactory.getDefaultRequestUrl());
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
		default:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		try {
			if (hasFocus && trailReports != null
					&& factory.getUserSettings().getRedrawNeeded())
				printer.printTrailReports();
		} catch (Exception e) {
			e.printStackTrace();
			factory.newDialog(e);
		}
	}

	private void refresh(boolean forced) {
		factory.getUserSettings().setForcedRefresh(forced);

		if (factory.getUserSettings().getLocationEnabled())
			factory.getLocationSource().updateLocation();
		else
			factory.getLocationSource().setLocation(
					factory.getUserSettings().getDefaultLocation());

		new LoadReportsTask(this, factory, listCreator, printer, trailReports,
				factory.getTrailInfoList()).execute();
	}

}
