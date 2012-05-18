package org.dsanderson.morctrailreport;

import java.util.Date;

import org.dsanderson.morctrailreport.R;
import org.dsanderson.morctrailreport.parser.MorcAllReportListCreator;
import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;
import org.dsanderson.morctrailreport.parser.SingleTrailInfoList;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.core.android.IAbstractListEntryFactory;
import org.dsanderson.xctrailreport.core.android.IPrinter;
import org.dsanderson.xctrailreport.core.android.LoadReportsTask;
import org.dsanderson.xctrailreport.core.android.TrailReportList;
import org.dsanderson.xctrailreport.decorators.ITrailReportListEntry;
import org.dsanderson.android.util.AndroidIntent;
import org.dsanderson.android.util.AndroidProgressBar;
import org.dsanderson.android.util.Dialog;
import org.dsanderson.android.util.Maps;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AllReportActivity extends ListActivity {

	private static final String databaseName = "all_reports_database";

	private TrailReportList trailReports = null;
	private TrailReportFactory factory;
	MorcAllReportListCreator listCreator;
	private AllTrailReportPrinter printer;
	String appName;
	boolean redraw = true;
	private AllTrailReportAdapter adapter;

	@Override
	protected void onStart() {
		super.onStart();
		int versionNumber = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		if (versionNumber >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appName = getString(R.string.app_name);

		registerForContextMenu(getListView());

		if (TrailReportFactory.exists()) {
			factory = TrailReportFactory.getInstance();
		} else {
			factory = new TrailReportFactory(getApplicationContext());
		}

		listCreator = new MorcAllReportListCreator(factory);

		if (trailReports == null) {
			trailReports = new TrailReportList(this,
					factory.getTrailReportDatabaseFactory(), databaseName,
					Integer.parseInt(getString(R.integer.databaseVersion)));
		}

		try {
			trailReports.open();
			factory.getUserSettingsSource().loadUserSettings();
		} catch (Exception e) {
			System.err.println(e);
		}

		printer = new AllTrailReportPrinter(this, factory, trailReports,
				appName, ListEntryFactory.getInstance());

		redraw = true;
		refresh(false, 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_reports_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.allReportRefresh:
			redraw = true;
			refresh(true, 1);
			return true;
		case R.id.openInBrowser: {
			MorcSpecificTrailInfo morcInfo = getMorcTrailInfo();

			if (morcInfo != null) {
				String allReportUrl = morcInfo.getAllTrailReportUrl();
				if (allReportUrl != null && allReportUrl.length() > 0)
					AndroidIntent.launchIntent(allReportUrl, this);
			}
		}
			return true;
		case R.id.aboutMenuItem:
			openAbout();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && factory.getUserSettings().getRedrawNeeded()) {
			try {
				factory.getUserSettings().setRedrawNeeded(false);
				printer.printTrailReports();
			} catch (Exception e) {
				e.printStackTrace();
				Dialog dialog = new Dialog(this, e);
				dialog.show();
			}
		}
	}

	// / adding this to prevent rescrolling on orientation changed
	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	protected void onDestroy() {
		trailReports.close();
		super.onDestroy();
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
			TrailInfo info = trailReport.getTrailInfo();

			switch (item.getItemId()) {

			case R.id.trailInfoMenu: {
				ISourceSpecificTrailInfo sourceSpecific = info
						.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
				if (sourceSpecific != null) {
					String trailInfoUrl = sourceSpecific.getTrailInfoUrl();
					if (trailInfoUrl != null && trailInfoUrl.length() > 0)
						AndroidIntent.launchIntent(trailInfoUrl, this);
				}
			}
				return true;
			case R.id.openMapMenu:
				Maps.launchMap(info.getLocation(), info.getName(),
						info.getSpecificLocation(), this);
				return true;
			case R.id.composeReportItem: {
				ISourceSpecificTrailInfo sourceSpecific = info
						.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
				if (sourceSpecific != null) {
					String composeUrl = sourceSpecific.getComposeUrl();
					if (composeUrl != null && composeUrl.length() > 0)
						AndroidIntent.launchIntent(composeUrl, this);
				}
			}
				return true;
			default:
				break;
			}
		}

		return super.onContextItemSelected(item);
	}

	private void refresh(boolean forced, int page) {
		factory.getUserSettings().setForcedRefresh(forced);
		factory.getLocationSource().updateLocation();

		TrailInfo info = getTrailInfo();
		listCreator.setPage(page);

		SingleTrailInfoList trailInfos = new SingleTrailInfoList();
		trailInfos.add(info);

		new LoadReportsTask(this, factory, listCreator, trailReports,
				trailInfos, printer, new AndroidProgressBar(this)).execute();
	}

	private class AllTrailReportPrinter implements IPrinter {

		private final ListActivity context;
		private final IAbstractFactory factory;
		private final TrailReportList trailReports;
		private final IAbstractListEntryFactory listEntryFactory;

		public AllTrailReportPrinter(ListActivity context,
				IAbstractFactory factory, TrailReportList trailReports,
				String appName, IAbstractListEntryFactory listEntryFactory) {
			this.context = context;
			this.factory = factory;

			this.trailReports = trailReports;
			this.listEntryFactory = listEntryFactory;

		}

		public void printTrailReports() throws Exception {
			Date lastRefreshDate = trailReports.getTimestamp();
			String titleString = appName;

			TrailInfo info = getTrailInfo();

			if (info == null)
				throw new Exception("Cannot get trail info.");

			titleString = info.getName();

			if (lastRefreshDate != null && lastRefreshDate.getTime() != 0) {
				Time time = new Time();
				time.set(lastRefreshDate.getTime());
				titleString += time.format(" (%b %e, %r)");
			}
			context.setTitle(titleString);

			Cursor cursor = ((TrailReportList) trailReports).getCursor();
			if (redraw || (adapter == null)) {
				adapter = new AllTrailReportAdapter(context, cursor, factory,
						trailReports, listEntryFactory);
				context.setListAdapter(adapter);
			} else {
				adapter.changeCursor(cursor);
			}
		}

	}

	private TrailInfo getTrailInfo() {
		TrailInfo allReportsInfo = MorcFactory.getInstance()
				.getAllReportsInfo();

		if (allReportsInfo == null) {
			try {
				allReportsInfo = trailReports.get(0).getTrailInfo();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return allReportsInfo;
	}

	private MorcSpecificTrailInfo getMorcTrailInfo() {
		MorcSpecificTrailInfo morcInfo = null;
		try {
			TrailInfo info = getTrailInfo();
			morcInfo = (MorcSpecificTrailInfo) info
					.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
		} catch (Exception e) {
		}
		return morcInfo;
	}

	private class AllTrailReportAdapter extends CursorAdapter {

		private final IAbstractFactory factory;
		private final TrailReportList trailReports;
		private final IAbstractListEntryFactory listEntryFactory;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public AllTrailReportAdapter(Context context, Cursor cursor,
				IAbstractFactory factory, TrailReportList trailReports,
				IAbstractListEntryFactory listEntryFactory) {
			super(context, cursor);
			this.factory = factory;
			this.trailReports = trailReports;
			this.listEntryFactory = listEntryFactory;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.CursorAdapter#newView(android.content.Context,
		 * android.database.Cursor, android.view.ViewGroup)
		 */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return listEntryFactory.inflate(context, parent);
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
				ITrailReportListEntry listEntry = listEntryFactory
						.newListEntry(view);

				boolean newTrail = false;
				boolean last = cursor.isLast();
				if (cursor.moveToPrevious()) {
					TrailReport previousReport = (TrailReport) trailReports
							.get(cursor);

					if (previousReport.getTrailInfo().getName()
							.compareTo(currentReport.getTrailInfo().getName()) != 0) {
						newTrail = true;
					}
					TrailInfo previousInfo = previousReport.getTrailInfo();
					for (ISourceSpecificTrailInfo specificInfo : previousInfo
							.getSourceSpecificInfos()) {
						specificInfo.deleteItem();
					}
					factory.getTrailInfoPool().deleteItem(previousInfo);
					factory.getTrailReportPool().deleteItem(previousReport);
				} else {
					newTrail = true;
				}

				listEntry.setTitleGroupVisible(newTrail);

				if (newTrail)
					factory.getTrailInfoDecorators().decorate(currentReport,
							listEntry);

				factory.getTrailReportDecorators().decorate(currentReport,
						listEntry);

				listEntry.draw();

				TrailInfo currentInfo = currentReport.getTrailInfo();
				for (ISourceSpecificTrailInfo specificInfo : currentInfo
						.getSourceSpecificInfos()) {
					specificInfo.deleteItem();
				}
				factory.getTrailInfoPool().deleteItem(currentInfo);
				factory.getTrailReportPool().deleteItem(currentReport);

				// if on last entry and we have another page
				if (last && (listCreator.getPage() < listCreator.getLastPage())) {
					redraw = false;
					refresh(true, listCreator.getPage() + 1);
				}
			}
		}

	}

	// / Launch about menu activity
	private void openAbout() {
		Intent i = new Intent(this, AboutActivity.class);
		startActivity(i);
	}

	private TrailReport getObjectFromMenuItemInfo(AdapterContextMenuInfo info) {
		if (trailReports.size() > 0) {
			return (TrailReport) trailReports.getById(info.id);
		} else {
			return null;
		}
	}

}
