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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

public class xctrailreportActivity extends ListActivity {

	private List<TrailReport> trailReports;
	private TrailReportFactory factory = new TrailReportFactory(this);
	ReportListCreator listCreator = new ReportListCreator(factory);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		factory.getUserSettingsSource().loadUserSettings();

		// refresh();
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
		displayedTrailReports = listCreator.sortTrailReports(trailReports);

		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row,
				displayedTrailReports));
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

			if (e == null) {
				printTrailReports(trailReports);
			} else {
				System.err.println(e);
				factory.newDialog(e).show();
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
			}
			return layout;
		}
	}

}
