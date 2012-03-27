package org.dsanderson.morctrailreport;

import java.util.List;

import org.dsanderson.morctrailreport.R;
import org.dsanderson.morctrailreport.AboutActivity;
import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;

import org.dsanderson.xctrailreport.application.ReportListCreator;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.android.LoadReportsTask;
import org.dsanderson.xctrailreport.core.android.TrailInfoList;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.core.android.TrailReportPrinter;

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
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AllReportActivity extends ListActivity {

	private TrailReportList trailReports;
	private TrailReportFactory factory = new TrailReportFactory(this);
	ReportListCreator listCreator = new ReportListCreator(factory);
	private TrailReportPrinter printer;
	String appName;
	private TrailInfo info;
	MorcSpecificTrailInfo morcInfo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appName = getString(R.string.app_name);
		printer = new TrailReportPrinter(this, factory, appName, R.layout.row);

		trailReports = (TrailReportList) factory.getTrailReportList();

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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_reports_menu, menu);
		// TODO need to manually inflate this menu, so that I don't add trail
		// info if not available
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.allReportRefresh:
			refresh(true);
			return true;
		case R.id.allReportCompose: {
			String composeUrl = morcInfo.getComposeUrl();
			if (composeUrl != null && composeUrl.length() > 0)
				launchIntent(composeUrl);
		}
			return true;
		case R.id.allReportsMap:
			launchIntent("geo:" + info.getLocation() + "?z=14");
			return true;
		case R.id.allReportsTrailInfo:
			if (morcInfo != null) {
				String trailInfoUrl = morcInfo.getTrailInfoUrl();
				if (trailInfoUrl != null && trailInfoUrl.length() > 0)
					launchIntent(trailInfoUrl);
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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

	private void launchIntent(String uriString) {
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

}
