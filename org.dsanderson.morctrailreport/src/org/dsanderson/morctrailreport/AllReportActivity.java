package org.dsanderson.morctrailreport;

import java.util.List;

import org.dsanderson.morctrailreport.R;
import org.dsanderson.morctrailreport.parser.MorcAllReportListCreator;
import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;
import org.dsanderson.morctrailreport.parser.SingleTrailInfoList;

import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.android.LoadReportsTask;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.core.android.TrailReportPrinter;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AllReportActivity extends ListActivity {

	private final String databaseName = "all_reports_database";

	private TrailReportList trailReports = null;
	private SingleTrailInfoList trailInfos = null;
	private TrailReportFactory factory = TrailReportFactory.getInstance();
	MorcAllReportListCreator listCreator = new MorcAllReportListCreator(factory);
	private TrailReportPrinter printer;
	String appName;
	private TrailInfo info;
	MorcSpecificTrailInfo morcInfo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appName = getString(R.string.app_name);

		if (trailReports == null) {
			trailReports = new TrailReportList(this,
					factory.getTrailReportDatabaseFactory(), databaseName,
					R.integer.databaseVersion);
			try {
				trailReports.open();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		printer = new TrailReportPrinter(this, factory, trailReports, appName,
				R.layout.row);

		if (trailInfos == null)
			trailInfos = new SingleTrailInfoList();

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
		case R.id.openInBrowser:
			if (morcInfo != null) {
				String allReportUrl = morcInfo.getAllTrailReportUrl();
				if (allReportUrl != null && allReportUrl.length() > 0)
					launchIntent(allReportUrl);
			}
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

		info = MorcFactory.getInstance().getAllReportsInfo();
		morcInfo = (MorcSpecificTrailInfo) info
				.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
		trailInfos.add(info);
		new LoadReportsTask(this, factory, listCreator, printer, trailReports,
				trailInfos).execute();
	}

	private void launchIntent(String uriString) {
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

}
