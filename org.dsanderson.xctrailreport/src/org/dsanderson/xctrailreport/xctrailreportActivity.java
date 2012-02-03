package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.core.ReportListCreator;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class xctrailreportActivity extends ListActivity {

	private List<TrailReport> trailReports;

	private TrailReportFactory factory = new TrailReportFactory(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		trailReports = loadTrailReports();
		printTrailReports(trailReports);
	}

	private List<TrailReport> loadTrailReports() {

		factory.getLocationSource().updateLocation();
		try {
			InputStream inputStream = getResources().openRawResource(
					R.raw.trail_info);
			ReportListCreator listCreator = new ReportListCreator(factory);
			trailReports = listCreator.getTrailReports(inputStream);
		} catch (Exception e) {
			Log.e("XCTrailReports", e.getMessage(), e);
		}
		return trailReports;
	}

	private void printTrailReports(List<TrailReport> trailReports) {
		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row,
				trailReports));
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

			ListIterator<TrailReport> reportIter = trailReports
					.listIterator(position);
			factory.getTrailReportDecorators().decorate(reportIter,
					new ListEntry((LinearLayout) layout, getContext()));
			return layout;
		}

	}

}
