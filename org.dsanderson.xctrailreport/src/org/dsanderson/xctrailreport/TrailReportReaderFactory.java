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

	private boolean externalStorage;

	public TrailReportReaderFactory(Context context) {
		assert (instance == null);
		this.context = context;
	}

	public static TrailReportReaderFactory getInstance() {
		assert (instance != null);
		return instance;
	}

	public void setExternalStorage(boolean externalStorage) {
		this.externalStorage = externalStorage;
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
			if (!externalStorage || externalReadAccess()) {
				File file = new File(getDir() + "saved_trail_reports.xml");
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

	private Reader newReader(String filename) throws Exception {
		if (!externalStorage || externalReadAccess()) {
			FileInputStream trailInfoStream = new FileInputStream(getDir()
					+ filename);
			return new BufferedReader(new InputStreamReader(trailInfoStream));
		} else {
			return null;
		}
	}

	private Writer newWriter(String filename) throws Exception {
		if (!externalStorage || externalWriteAccess()) {
			FileOutputStream trailInfoStream = new FileOutputStream(getDir()
					+ filename);
			return new BufferedWriter(new OutputStreamWriter(trailInfoStream));
		} else {
			return null;
		}
	}

	private String getDir() {
		if (externalStorage) {
			return context.getExternalCacheDir() + "/";
		} else {
			return context.getCacheDir() + "/";
		}
	}
}
