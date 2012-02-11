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
import android.os.Environment;

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
		if (externalReadAccess()) {
			FileInputStream trailInfoStream = new FileInputStream(
					context.getExternalCacheDir() + "/saved_trail_info.xml");
			return new BufferedReader(new InputStreamReader(trailInfoStream));
		} else {
			return null;
		}
	}

	public Writer newSavedTrailInfoWriter() throws Exception {
		if (externalWriteAccess()) {
			FileOutputStream trailInfoStream = new FileOutputStream(
					context.getExternalCacheDir() + "/saved_trail_info.xml");
			return new BufferedWriter(new OutputStreamWriter(trailInfoStream));
		} else {
			return null;
		}

	}

	public Reader newSavedTrailReportReader() throws Exception {
		try {
			if (externalReadAccess()) {
				String filePath = context.getExternalCacheDir()
						+ "/saved_trail_reports.xml";
				FileInputStream trailInfoStream = new FileInputStream(filePath);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(trailInfoStream));
				modifiedDate = new Date(new File(filePath).lastModified());
				return reader;
			} else {
				modifiedDate = null;
				return null;
			}
		} catch (Exception e) {
			modifiedDate = null;
			throw e;
		}
	}

	public Writer newSavedTrailReportWriter() throws Exception {
		if (externalWriteAccess()) {
			FileOutputStream trailInfoStream = new FileOutputStream(
					context.getExternalCacheDir() + "/saved_trail_reports.xml");
			return new BufferedWriter(new OutputStreamWriter(trailInfoStream));
		} else {
			return null;
		}
	}

	private boolean externalReadAccess() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	private boolean externalWriteAccess() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.application.IReportReaderFactory#
	 * getReportsRefreshedDate()
	 */
	public Date getReportsRefreshedDate() {
		if (modifiedDate != null) {
			return modifiedDate;
		} else {
			String filePath = context.getExternalCacheDir()
					+ "/saved_trail_reports.xml";
			if (externalReadAccess()) {
				File file = new File(filePath);
				if (file != null) {
					modifiedDate = new Date(file.lastModified());
					return modifiedDate;
				}
			}
		}
		return null;
	}

	public void setReportsRefreshedDate(Date date) {
		this.modifiedDate = date;
	}
}
