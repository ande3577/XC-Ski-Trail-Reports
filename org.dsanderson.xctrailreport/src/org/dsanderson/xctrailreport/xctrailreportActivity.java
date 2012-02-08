package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.core.ReportListCreator;
import org.dsanderson.xctrailreport.core.TrailReport;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
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
				printTrailReports(trailReports);
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

		factory.getLocationSource().updateLocation();
		InputStream inputStream = getResources().openRawResource(
				R.raw.trail_info);
		trailReports = listCreator.getTrailReports(inputStream);

		if (trailReports.isEmpty())
			throw new RuntimeException("No reports found.");

		return trailReports;
	}

	private void printTrailReports(List<TrailReport> trailReports) {
		List<TrailReport> displayedTrailReports = listCreator
				.filterTrailReports(trailReports);
		displayedTrailReports = listCreator
				.sortTrailReports(displayedTrailReports);

		if (displayedTrailReports.isEmpty())
			throw new RuntimeException("No reports found.");

		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row,
				displayedTrailReports));
		registerForContextMenu(getListView());
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.trailInfoMenu:
			if (trailReports.size() > 0) {
				launchIntent(trailReports.get(0).getTrailInfo()
						.getSkinnySkiUrl());
			}
			return true;
		case R.id.openMapMenu:
			if (trailReports.size() > 0) {
				launchIntent("geo:"
						+ trailReports.get(0).getTrailInfo().getLocation());
			}
			return true;
		case R.id.composeReportItem:
			if (trailReports.size() > 0) {
				launchIntent(trailReports.get(0).getTrailInfo()
						.getSkinnySkiUrl());
			}
			return true;
		default:
			return super.onContextItemSelected(item);
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
		default:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && trailReports != null)
			printTrailReports(trailReports);
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
					printTrailReports(trailReports);
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
