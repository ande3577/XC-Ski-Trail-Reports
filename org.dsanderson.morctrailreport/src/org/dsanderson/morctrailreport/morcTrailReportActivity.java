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
import org.dsanderson.xctrailreport.core.android.TrailInfoList;
import org.dsanderson.xctrailreport.core.android.TrailReportList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;

public class morcTrailReportActivity extends ListActivity {
	
	private TrailReportList trailReports;
	private TrailInfoList trailInfos;
	private TrailReportFactory factory = new TrailReportFactory(this);
	ReportListCreator listCreator = new ReportListCreator(factory);
	boolean forcedRefresh = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerForContextMenu(getListView());

		trailReports = (TrailReportList) factory.getTrailReportList();
		trailInfos = (TrailInfoList) factory.getTrailInfoList();

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

	private void loadTrailReports() throws Exception {

		try {
			listCreator
					.getTrailReports(trailReports, trailInfos, forcedRefresh);
		} catch (Exception e) {
			e.printStackTrace();
			trailReports.clear();
			throw e;
		} finally {
			forcedRefresh = false;
		}
	}

	private void printTrailReports() throws Exception {
		Date lastRefreshDate = trailReports.getTimestamp();
		String titleString = getString(R.string.app_name);
		if (lastRefreshDate != null) {
			Time time = new Time();
			time.set(lastRefreshDate.getTime());
			titleString += time.format(" (%b %e, %r)");
		}
		setTitle(titleString);

		trailReports.filter(factory.getUserSettings());
		trailReports.sort(factory.getUserSettings());

		Cursor cursor = ((TrailReportList) trailReports).getCursor();
		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row, cursor));
		factory.getUserSettings().setRedrawNeeded(false);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		TrailReport report = getObjectFromMenuItemInfo((AdapterView.AdapterContextMenuInfo) menuInfo);
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
		TrailReport trailReport = getObjectFromMenuItemInfo((AdapterView.AdapterContextMenuInfo) item
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
			e.printStackTrace();
			factory.newDialog(e);
		}
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

	private class LoadReportsTask extends AsyncTask<Integer, Integer, Integer> {

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
		protected Integer doInBackground(Integer... params) {
			Integer size = null;
			try {
				if (((MorcFactory) factory
						.getSourceSpecificFactory(MorcFactory.SOURCE_NAME))
						.getRegionManager().getRegions().isEmpty())
					throw new Exception("No regions enabled.");

				loadTrailReports();
				size = trailReports.size();
				if (size == 0)
					throw new Exception("No reports found.");
			} catch (Exception e) {
				this.e = e;
			}
			return size;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();

			try {
				if (e == null)
					printTrailReports();
			} catch (Exception e) {
				e.printStackTrace();
				e = this.e;
			} finally {
				if (e != null) {
					e.printStackTrace();
					factory.newDialog(e).show();
				}
			}
		}
	}

	private class TrailInfoAdapter extends CursorAdapter {

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public TrailInfoAdapter(Context context, int textViewResourceId,
				Cursor cursor) {
			super(context, cursor);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.CursorAdapter#newView(android.content.Context,
		 * android.database.Cursor, android.view.ViewGroup)
		 */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			final LayoutInflater inflater = LayoutInflater.from(context);
			View layout = inflater.inflate(R.layout.row, parent, false);

			return layout;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.CursorAdapter#bindView(android.view.View,
		 * android.content.Context, android.database.Cursor)
		 */
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TrailReport currentReport = (TrailReport) trailReports.get(cursor);

			if (currentReport != null) {
				ListEntry listEntry = new ListEntry((LinearLayout) view,
						context);

				boolean newTrail = false;
				if (cursor.moveToPrevious()) {
					TrailReport previousReport = (TrailReport) trailReports
							.get(cursor);

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

				factory.getTrailReportDecorators().decorate(currentReport,
						listEntry);

				listEntry.draw();
			}
		}

	}

}
