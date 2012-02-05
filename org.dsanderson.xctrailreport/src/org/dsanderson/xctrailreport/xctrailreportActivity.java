package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.core.ReportListCreator;
import org.dsanderson.xctrailreport.core.TrailReport;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
//			trailReports = loadTrailReports();
			printTrailReports(trailReports);
		} catch (Exception e) {
			Log.e("XCTrailReports", e.getMessage(), e);
			factory.newErrorDialog(e).show();
		}
	}

	private List<TrailReport> loadTrailReports() throws Exception {

		factory.getLocationSource().updateLocation();
		InputStream inputStream = getResources().openRawResource(
				R.raw.trail_info);
		ReportListCreator listCreator = new ReportListCreator(factory);
		trailReports = listCreator.getTrailReports(inputStream);

		return trailReports;
	}

	private void printTrailReports(List<TrailReport> trailReports) {
		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row,
				trailReports));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
		return true;
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
			return layout;
		}
	}

}
