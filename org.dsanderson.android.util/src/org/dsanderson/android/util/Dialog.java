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

import org.dsanderson.util.IDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 
 */
public class Dialog implements IDialog {
	AlertDialog dialog;

	public Dialog(Context context, Exception e) {
		this(context, "Error: " + e.getLocalizedMessage());

	}

	public Dialog(Context context, String string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(string).setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		;
		dialog = builder.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IErrorDialog#open()
	 */
	public void show() {
		dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IDialog#isVisible()
	 */
	public boolean isShowing() {
		return dialog.isShowing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IDialog#dismiss()
	 */
	public void dismiss() {
		dialog.dismiss();
	}
}
