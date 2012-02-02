package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.core.ReportListCreator;
import org.dsanderson.xctrailreport.core.TrailInfo;

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

	private List<TrailInfo> trailInfo;

	private TrailReportFactory factory = new TrailReportFactory();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		trailInfo = loadTrailInfo();
		printTrailInfo(trailInfo);
	}

	private List<TrailInfo> loadTrailInfo() {
		List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();
		try {
			InputStream inputStream = getResources().openRawResource(
					R.raw.trail_info);
			ReportListCreator listCreator = new ReportListCreator(factory);
			trailInfo = listCreator.getTrailInfoReports(inputStream);
		} catch (Exception e) {
			Log.e("XCTrailReports", e.getMessage(), e);
		}

		return trailInfo;
	}

	private void printTrailInfo(List<TrailInfo> trailInfo) {
		this.setListAdapter(new TrailInfoAdapter(this, R.layout.row, trailInfo));
	}

	private class TrailInfoAdapter extends ArrayAdapter<TrailInfo> {
		private List<TrailInfo> trailInfos;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public TrailInfoAdapter(Context context, int textViewResourceId,
				List<TrailInfo> objects) {
			super(context, textViewResourceId, objects);
			this.trailInfos = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = convertView;
			if (layout == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = vi.inflate(R.layout.row, null);
			}

			TrailInfo trailInfo = trailInfos.get(position);
			factory.getTrailInfoDecorators().decorate(trailInfo,
					new ListEntry((LinearLayout) layout, getContext()));
			return layout;
		}

	}

}
