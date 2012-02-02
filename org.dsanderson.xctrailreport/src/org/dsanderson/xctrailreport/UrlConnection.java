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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dsanderson.xctrailreport.core.INetConnection;

/**
 * 
 */
public class UrlConnection implements INetConnection {
	String string = "";

	HttpURLConnection urlConnection = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#connect()
	 */
	public boolean connect(String address) {
		boolean retVal = true;
		try {
			URL url = new URL(address);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = new BufferedInputStream(
					urlConnection.getInputStream());

			BufferedInputStream in = new BufferedInputStream(inputStream);

			byte b[] = new byte[200000];

			while (in.read(b) > 0) {
				String newString = new String(b);
				string += newString;
				retVal = true;
			}
			disconnect();

		} catch (Exception e) {
			System.err.println(e);
			retVal = false;
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.INetConnection#getInputStream()
	 */
	public String getString() {
		return string;
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

}
