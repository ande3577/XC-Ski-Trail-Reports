package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.R;
import org.dsanderson.xctrailreport.core.TrailInfo;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class xctrailreportActivity extends ListActivity {

	private List<TrailInfo> trailInfo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		trailInfo = loadTrailInfo();
		loadReports(trailInfo);
		printTrailInfo(trailInfo);

	}

	private List<TrailInfo> loadTrailInfo() {
		List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();
		try {
			InputStream inputStream = getResources().openRawResource(
					R.raw.trail_info);
			BaseFeedParser parser = new BaseFeedParser(inputStream);
			trailInfo = parser.parse();
		} catch (Exception e) {
			Log.e("XCTrailReports", e.getMessage(), e);
		}

		return trailInfo;
	}

	private void printTrailInfo(List<TrailInfo> trailInfo) {
		List<String> names = new ArrayList<String>(trailInfo.size());
		for (TrailInfo info : trailInfo) {
			names.add(info.getName());
		}

		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.listentry,
				names));

	}

	private void loadReports(List<TrailInfo> trailInfo) {
		CompoundReportRetriever retriever = new CompoundReportRetriever();

		for (TrailInfo info : trailInfo) {
			info.setReports(retriever.getReports(info));
		}
	}
}