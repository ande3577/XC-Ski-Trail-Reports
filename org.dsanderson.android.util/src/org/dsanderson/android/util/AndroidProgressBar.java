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
package org.dsanderson.android.util;

import org.dsanderson.util.IProgressBar;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 */
public class AndroidProgressBar implements IProgressBar {
	ProgressDialog progressDialog = null;

	public AndroidProgressBar(Context context) {
		if (progressDialog == null)
			progressDialog = new ProgressDialog(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IProgressBar#show()
	 */
	public void show() {
		progressDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IProgressBar#setMessage(java.lang.String)
	 */
	public void setMessage(String message) {
		progressDialog.setMessage(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IProgressBar#setProgress(int)
	 */
	public void setProgress(int progress) {
		progressDialog.setProgress(progress);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IProgressBar#incrementProgress()
	 */
	public void incrementProgress() {
		progressDialog.incrementProgressBy(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IProgressBar#close()
	 */
	public void close() {
		if (progressDialog != null & progressDialog.isShowing())
			progressDialog.dismiss();
	}

}
