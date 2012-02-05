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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dsanderson.xctrailreport.core.INetConnection;

/**
 * 
 */
public class UrlConnection implements INetConnection {
	InputStream stream = null;
	String string = null;

	HttpURLConnection urlConnection = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#connect()
	 */
	public void connect(String address) throws Exception {
		try {
			string = null;
			URL url = new URL(address);
			urlConnection = (HttpURLConnection) url.openConnection();
			stream = urlConnection.getInputStream();

		} catch (Exception e) {
			urlConnection = null;
			stream = null;
			string = null;
			disconnect();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#getInputStream()
	 */
	public BufferedReader getReader() {
		if (stream == null)
			return null;
		else
			return new BufferedReader(new InputStreamReader(stream));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#disconnect()
	 */
	public void disconnect() {
		if (urlConnection != null) {
			urlConnection.disconnect();
		}
		urlConnection = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#getString()
	 */
	public String getString() {
		BufferedReader reader = getReader();
		// if we've already received the string
		if (string == null && reader != null) {
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					string += line + "\r\n";
				}
			} catch (Exception e) {
				string = null;
			}
		}
		return string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#getStream()
	 */
	public InputStream getStream() {
		return stream;
	}

}
