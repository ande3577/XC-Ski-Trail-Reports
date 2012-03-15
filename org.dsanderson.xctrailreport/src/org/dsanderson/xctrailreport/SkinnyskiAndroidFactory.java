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
import org.dsanderson.xctrailreport.core.IUserSettingsSource;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;

import android.content.Context;

/**
 * 
 */
public class SkinnyskiAndroidFactory extends SkinnyskiFactory {
	private static SkinnyskiAndroidFactory skinnyskiAndroidFactory = null;
	private SkinnyskiSettingsSource skinnyskiSettingsSource = null;
	private Context context;

	public SkinnyskiAndroidFactory(Context context, IAbstractFactory factory) {
		super(factory);
		assert (skinnyskiAndroidFactory == null);
		skinnyskiAndroidFactory = this;
		this.context = context;
	}

	static public SkinnyskiAndroidFactory getInstance() {
		assert (skinnyskiAndroidFactory != null);
		return skinnyskiAndroidFactory;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory#getUserSettingsSource()
	 */
	@Override
	public IUserSettingsSource getUserSettingsSource() {
		if (skinnyskiSettingsSource == null)
			skinnyskiSettingsSource = new SkinnyskiSettingsSource(context,
					getSettings());
		return skinnyskiSettingsSource;
	}

}
