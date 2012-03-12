package org.dsanderson.morctrailreport;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dsanderson.morctrailreport.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * 
 */
public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		InputStream readme = null;
		try {
			readme = getAssets().open("README.htm");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					readme));
			String readmeText = "";
			String lineText;
			while ((lineText = reader.readLine()) != null) {
				readmeText += lineText;
			}

			TextView textView = ((TextView) findViewById(R.id.aboutText));
			readmeText = replaceVariable(
					readmeText,
					"version",
					getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
			readmeText = replaceVariable(readmeText, "app_name",
					getString(R.string.app_name));
			textView.setText(Html.fromHtml(readmeText));
			textView.setMovementMethod(LinkMovementMethod.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				readme.close();
			} catch (Exception e) {
			}
		}
	}

	private String replaceVariable(String input, String variable, String newText) {
		String regex = "\\Q${" + variable + "}\\E";
		return input.replaceAll(regex, newText);
	}
}
