package org.dsanderson.morctrailreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dsanderson.android.util.ListEntry;
import org.dsanderson.morctrailreport.R;
import org.dsanderson.morctrailreport.AboutActivity;
import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;

import org.dsanderson.xctrailreport.application.ReportListCreator;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
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

public class morcTrailReportActivity extends ListActivity {
	private List<TrailReport> trailReports;
	private TrailReportFactory factory = new TrailReportFactory(this);
	TrailReportReaderFactory trailReportReaderFactory = new TrailReportReaderFactory(
			this);
	ReportListCreator listCreator = new ReportListCreator(factory,
			trailReportReaderFactory);
	boolean forcedRefresh = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	private List<TrailReport> loadTrailReports() throws Exception {

		try {
			trailReports = new ArrayList<TrailReport>();
			trailReports = listCreator.getTrailReports(trailReports,
					forcedRefresh);
		} catch (Exception e) {
			throw e;
		} finally {
			forcedRefresh = false;
		}

		return trailReports;
	}

	private void printTrailReports() throws Exception {
		Date lastRefreshDate = trailReportReaderFactory
				.getReportsRefreshedDate();
		String titleString = getString(R.string.app_name);
		if (lastRefreshDate != null) {
			Time time = new Time();
			time.set(lastRefreshDate.getTime());
			titleString += time.format(" (%b %e, %r)");
		}
		setTitle(titleString);

		trailReports = listCreator.sortTrailReports(trailReports);

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
			ISourceSpecificTrailInfo morcInfo = report.getTrailInfo()
					.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);

			if (morcInfo != null) {

				String allReportUrl = ((MorcSpecificTrailInfo) morcInfo)
						.getAllTrailReportUrl();

				if (allReportUrl != null && allReportUrl.length() != 0) {
					menu.add(Menu.NONE, R.id.viewAllReports, Menu.NONE,
							"View All Reports");
				}

				if (morcInfo.getComposeUrl() != null
						&& morcInfo.getComposeUrl().length() != 0) {
					menu.add(Menu.NONE, R.id.composeReportItem, Menu.NONE,
							"Compose Report");
				}

				if (morcInfo.getTrailInfoUrl() != null
						&& morcInfo.getTrailInfoUrl().length() != 0) {
					menu.add(Menu.NONE, R.id.trailInfoMenu, Menu.NONE,
							"Trail Info");
				}
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TrailReport trailReport = determineListItemFromMenuInfo((AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo());
		if (trailReport != null) {
			switch (item.getItemId()) {
			case R.id.viewAllReports: {
				ISourceSpecificTrailInfo sourceSpecific = trailReport
						.getTrailInfo().getSourceSpecificInfo(
								MorcFactory.SOURCE_NAME);
				if (sourceSpecific != null) {
					String viewAllReportUrl = ((MorcSpecificTrailInfo) sourceSpecific)
							.getAllTrailReportUrl();
					if (viewAllReportUrl != null
							&& viewAllReportUrl.length() > 0)
						launchIntent(viewAllReportUrl);
				}
			}
				return true;

			case R.id.trailInfoMenu: {
				ISourceSpecificTrailInfo sourceSpecific = trailReport
						.getTrailInfo().getSourceSpecificInfo(
								MorcFactory.SOURCE_NAME);
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
								MorcFactory.SOURCE_NAME);
				if (sourceSpecific != null) {
					String composeUrl = sourceSpecific.getComposeUrl();
					if (composeUrl != null && composeUrl.length() > 0)
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
			openPreferencesMenu();
			return true;
		case R.id.aboutMenuItem:
			openAbout();
			return true;
		case R.id.refresh:
			refresh(true);
			return true;
		case R.id.sortBy:
			MenuItem distance = item.getSubMenu().getItem(0).setChecked(false);
			MenuItem date = item.getSubMenu().getItem(1).setChecked(false);
			MenuItem duration = item.getSubMenu().getItem(2).setChecked(false);

			switch (factory.userSettings.getSortMethod()) {
			case SORT_BY_DISTANCE:
				distance.setChecked(true);
				return true;
			case SORT_BY_DATE:
				date.setChecked(true);
				return true;
			case SORT_BY_DURATION:
				duration.setChecked(true);
				return true;
			}
			return false;
		case R.id.sortByDuration:
		case R.id.sortByDate:
		case R.id.sortByDistance:

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
			}

			Editor edit = PreferenceManager.getDefaultSharedPreferences(
					getApplication()).edit();
			edit.putString("sortMethod", sortMethodString);
			edit.commit();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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
		try {
			if (hasFocus && trailReports != null
					&& factory.getUserSettings().getRedrawNeeded())
				printTrailReports();
		} catch (Exception e) {
			factory.newDialog(e);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final List<TrailReport> savedTrailReports = new ArrayList<TrailReport>();
		for (TrailReport report : trailReports) {
			savedTrailReports.add(report);
		}
		return savedTrailReports;
	}

	private void refresh(boolean forced) {
		forcedRefresh = forced;

		if (factory.getUserSettings().getLocationEnabled())
			factory.getLocationSource().updateLocation();
		else
			factory.getLocationSource().setLocation(
					factory.getUserSettings().getDefaultLocation());

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
				if (((MorcFactory) factory
						.getSourceSpecificFactory(MorcFactory.SOURCE_NAME))
						.getRegionManager().getRegions().isEmpty())
					throw new Exception("No regions enabled.");

				trailReports = loadTrailReports();
				if (trailReports.isEmpty())
					throw new Exception("No reports found.");
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