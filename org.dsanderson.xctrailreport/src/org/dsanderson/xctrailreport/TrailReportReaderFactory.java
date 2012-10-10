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
package org.dsanderson.xctrailreport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;

import org.dsanderson.xctrailreport.application.IReportReaderFactory;

import android.content.Context;

/**
 * 
 */
public class TrailReportReaderFactory implements IReportReaderFactory {
	private final Context context;

	private static TrailReportReaderFactory instance = null;

	private Date modifiedDate = null;

	public TrailReportReaderFactory(Context context) {
		assert (instance == null);
		this.context = context;
	}

	public static TrailReportReaderFactory getInstance() {
		assert (instance != null);
		return instance;
	}

	public Reader newDefaultTrailInfoReader() throws Exception {
		InputStream savedInfoStream = context.getAssets()
				.open("trail_info.xml");
		BufferedReader savedInfoReader = new BufferedReader(
				new InputStreamReader(savedInfoStream));
		return savedInfoReader;
	}

	public Reader newSavedTrailInfoReader() throws Exception {
		return newReader("saved_trail_info.xml");
	}

	public Writer newSavedTrailInfoWriter() throws Exception {
		return newWriter("saved_trail_info.xml");
	}

	public Reader newSavedTrailReportReader() throws Exception {
		return newReader("saved_trail_reports.xml");
	}

	public Writer newSavedTrailReportWriter() throws Exception {
		return newWriter("saved_trail_reports.xml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.application.IReportReaderFactory#
	 * getReportsRefreshedDate()
	 */
	@SuppressWarnings("unused")
	public Date getReportsRefreshedDate() {
		if (modifiedDate != null) {
			return modifiedDate;
		} else {
				File file = new File(getDir() + "saved_trail_reports.xml");
				if (file != null) {
					modifiedDate = new Date(file.lastModified());
					return modifiedDate;
			} 
			else 
				/// \bug for some reason this is flagged as dead code
				return null;
				}
			}

	public void setReportsRefreshedDate(Date date) {
		this.modifiedDate = date;
	}

	private Reader newReader(String filename) throws Exception {
			FileInputStream trailInfoStream = new FileInputStream(getDir()
					+ filename);
			return new BufferedReader(new InputStreamReader(trailInfoStream));
	}

	private Writer newWriter(String filename) throws Exception {
			FileOutputStream trailInfoStream = new FileOutputStream(getDir()
					+ filename);
			return new BufferedWriter(new OutputStreamWriter(trailInfoStream));
	}

	private String getDir() {
			return context.getCacheDir() + "/";
	}
}
