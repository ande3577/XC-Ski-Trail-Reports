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

import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.skinnyski.RegionManager;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiReportRetriever;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiSettings;

import android.content.Context;

/**
 * 
 */
public class SkinnyskiFactory {
	private static SkinnyskiFactory skinnyskiFactory = null;
	private SkinnyskiSettings skinnyskiSettings = null;
	private SkinnyskiSettingsSource skinnyskiSettingsSource = null;
	private SkinnyskiReportRetriever skinnyskiReportRetriever = null;
	private Context context;

	public SkinnyskiFactory(Context context) {
		assert (skinnyskiFactory == null);
		skinnyskiFactory = this;
		this.context = context;
	}

	static public SkinnyskiFactory getInstance() {
		assert (skinnyskiFactory != null);
		return skinnyskiFactory;
	}

	public SkinnyskiSettings getSkinnySkiSettings() {
		if (skinnyskiSettings == null) {
			try {
				skinnyskiSettings = new SkinnyskiSettings();
				RegionManager regions = skinnyskiSettings.getRegions();
				regions.add("Minnesota Metro Area");
			} catch (Exception e) {
				return null;
			}
		}

		return skinnyskiSettings;
	}

	public SkinnyskiSettingsSource getSkinnyskiSettingsSource() {
		if (skinnyskiSettingsSource == null)
			skinnyskiSettingsSource = new SkinnyskiSettingsSource(context,
					getSkinnySkiSettings());
		return skinnyskiSettingsSource;
	}

	public SkinnyskiReportRetriever getSkinnyskiReportRetriever(
			IAbstractFactory factory) {
		if (skinnyskiReportRetriever == null) {
			skinnyskiReportRetriever = new SkinnyskiReportRetriever(
					skinnyskiSettings, factory);
		}
		return skinnyskiReportRetriever;
	}

}
