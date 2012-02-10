package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.application.ReportListCreator;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiReportRetriever;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

public class xctrailreportActivity extends ListActivity {

	private List<TrailReport> trailReports;
	private SkinnyskiFactory skinnyskiFactory = new SkinnyskiFactory(this);
	private TrailReportFactory factory = new TrailReportFactory(this,
			skinnyskiFactory);
	ReportListCreator listCreator = new ReportListCreator(factory);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			factory.getUserSettingsSource().loadUserSettings();
			skinnyskiFactory.getSkinnyskiSettingsSource().loadUserSettings();

			@SuppressWarnings("unchecked")
			final List<TrailReport> savedTrailReports = (List<TrailReport>) getLastNonConfigurationInstance();
			if (savedTrailReports == null) {
				refresh();
			} else {
				trailReports = new ArrayList<TrailReport>();
				for (TrailReport report : savedTrailReports) {
					trailReports.add(report.copy());
				}
				printTrailReports();
			}

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

	private List<TrailReport> loadTrailReports() throws Exception {

		if (factory.getUserSettings().getLocationEnabled())
			factory.getLocationSource().updateLocation();

		InputStream inputStream = getAssets().open("trail_info.xml");
		trailReports = listCreator.getTrailReports(inputStream);
		trailReports = listCreator.filterTrailReports(trailReports);

		if (trailReports.isEmpty())
			throw new RuntimeException("No reports found.");

		return trailReports;
	}

	private void printTrailReports() {
		trailReports = listCreator.sortTrailReports(trailReports);

		if (trailReports.isEmpty())
			throw new RuntimeException("No reports found.");

		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row,
				trailReports));
		registerForContextMenu(getListView());
		factory.getUserSettings().setRedrawNeeded(false);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		TrailReport report = determineListItemFromMenuInfo((AdapterView.AdapterContextMenuInfo) menuInfo);
		if (report != null) {
			menu.add(Menu.NONE, R.id.openMapMenu, Menu.NONE, "Open Map");
			if (report.getTrailInfo().getSkinnySkiUrl() != null
					&& report.getTrailInfo().getSkinnySkiUrl().length() != 0) {
				menu.add(Menu.NONE, R.id.trailInfoMenu, Menu.NONE, "Trail Info");
			}

			menu.add(Menu.NONE, R.id.composeReportItem, Menu.NONE,
					"Compose Report");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TrailReport trailReport = determineListItemFromMenuInfo((AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo());
		if (trailReport != null) {
			switch (item.getItemId()) {
			case R.id.trailInfoMenu:
				String trailInfoUrl = trailReport.getTrailInfo()
						.getSkinnySkiUrl();
				if (trailInfoUrl != null && trailInfoUrl.length() > 0)
					launchIntent(trailReport.getTrailInfo().getSkinnySkiUrl());
				return true;
			case R.id.openMapMenu:
				launchIntent("geo:" + trailReport.getTrailInfo().getLocation()
						+ "?z=16");
				return true;
			case R.id.composeReportItem:
				String reportUrl = trailReport.getTrailInfo()
						.getSkinnySkiSubmitUrl();
				if (reportUrl == null || reportUrl.length() == 0)
					reportUrl = SkinnyskiReportRetriever.DEFAULT_SKINNYSKI_REPORT_URL;

				launchIntent(reportUrl);
				return true;
			default:
				break;
			}
		}

		return super.onContextItemSelected(item);
	}

	private TrailReport determineListItemFromMenuInfo(
			AdapterView.AdapterContextMenuInfo info) {
		if (trailReports.size() > 0) {
			return trailReports.get(info.position);
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
		case R.id.preferencesMenuItem:
			// Launch Preference activity
			Intent i = new Intent(xctrailreportActivity.this,
					PreferencesActivity.class);
			startActivity(i);
			break;
		case R.id.refresh:
			refresh();
			break;
		case R.id.aboutMenuItem:
			String aboutString = ProgramInfo.programName + "\r\n"
					+ ProgramInfo.programVersion + "\r\n" + ProgramInfo.author
					+ "\r\n" + ProgramInfo.copyright;
			factory.newDialog(aboutString).show();
			break;
		case R.id.composeMain:
			launchIntent(SkinnyskiReportRetriever.DEFAULT_SKINNYSKI_REPORT_URL);
		default:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && trailReports != null
				&& factory.getUserSettings().getRedrawNeeded())
			printTrailReports();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final List<TrailReport> savedTrailReports = new ArrayList<TrailReport>();
		for (TrailReport report : trailReports) {
			savedTrailReports.add(report.copy());
		}
		return savedTrailReports;
	}

	private void refresh() {
		new LoadReportsTask(this).execute();
	}

	private class LoadReportsTask extends
			AsyncTask<Integer, Integer, List<TrailReport>> {

		Context context = null;
		AlertDialog dialog = null;
		Exception e = null;

		/**
 * 
 */
		public LoadReportsTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			dialog = new AlertDialog.Builder(context).create();
			dialog.setMessage("Loading trail reports...");
			dialog.show();
			e = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<TrailReport> doInBackground(Integer... params) {
			List<TrailReport> trailReports = new ArrayList<TrailReport>();
			try {
				Looper.prepare();
				trailReports = loadTrailReports();
			} catch (Exception e) {
				this.e = e;
			}
			return trailReports;
		}

		@Override
		protected void onPostExecute(List<TrailReport> result) {
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();

			try {
				if (e == null)
					printTrailReports();
			} catch (Exception e) {
				e = this.e;
			} finally {
				if (e != null) {
					System.err.println(e);
					factory.newDialog(e).show();
				}
			}
		}
	}

	private class TrailInfoAdapter extends ArrayAdapter<TrailReport> {
		private List<TrailReport> trailReports;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public TrailInfoAdapter(Context context, int textViewResourceId,
				List<TrailReport> objects) {
			super(context, textViewResourceId, objects);
			this.trailReports = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = convertView;

			if (layout == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = vi.inflate(R.layout.row, null);
			}

			if (position < trailReports.size()) {
				ListEntry listEntry = new ListEntry((LinearLayout) layout,
						getContext());

				boolean newTrail = false;
				TrailReport currentReport = trailReports.get(position);
				if (position > 0) {
					TrailReport previousReport = trailReports.get(position - 1);

					if (previousReport.getTrailInfo().getName()
							.compareTo(currentReport.getTrailInfo().getName()) != 0) {
						newTrail = true;
					}
				} else {
					newTrail = true;
				}

				if (newTrail)
					factory.getTrailInfoDecorators().decorate(currentReport,
							listEntry);

				factory.getTrailReportDecorators().decorate(
						trailReports.get(position), listEntry);

				listEntry.draw();
			}
			return layout;
		}
	}

}
