/**
 * @author David S Anderson
 *
 *
 * Copyright (C) 2012 David S Anderson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dsanderson.xctrailreport.core.android;

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.IReportListCreator;
import org.dsanderson.xctrailreport.core.ITrailInfoList;
import org.dsanderson.xctrailreport.core.ITrailReportList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;

/**
 * 
 */
public class LoadReportsTask extends AsyncTask<Integer, Integer, Integer> {

	private final ListActivity context;
	private final IAbstractFactory factory;
	private final ITrailReportList trailReports;
	private final ITrailInfoList trailInfos;
	private final IReportListCreator listCreator;
	private final TrailReportPrinter printer;
	AlertDialog dialog = null;
	Exception e = null;

	/**
* 
*/
	public LoadReportsTask(ListActivity context, IAbstractFactory factory,
			IReportListCreator listCreator, TrailReportPrinter printer,
			ITrailReportList trailReports, ITrailInfoList trailInfos) {
		this.context = context;
		this.factory = factory;
		this.listCreator = listCreator;
		this.printer = printer;

		this.trailReports = trailReports;
		this.trailInfos = trailInfos;
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
				printer.printTrailReports();
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

	private void loadTrailReports() throws Exception {

		try {
			listCreator.getTrailReports(trailReports, trailInfos, factory
					.getUserSettings().getForcedRefresh());
		} catch (Exception e) {
			e.printStackTrace();
			trailReports.clear();
			throw e;
		} finally {
			factory.getUserSettings().setForcedRefresh(false);
		}
	}

}
