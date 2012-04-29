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
package org.dsanderson.morctrailreport;

import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.util.IUserSettingsSource;
import org.dsanderson.xctrailreport.core.IAbstractFactory;

import android.content.Context;

/**
 * 
 */
public class MorcAndroidFactory extends MorcFactory {
	private static MorcAndroidFactory morcAndroidFactory = null;
	private MorcSettingsSource morcSettingsSource = null;
	private Context context;

	public MorcAndroidFactory(Context context, IAbstractFactory factory) {
		super(factory);
		assert (morcAndroidFactory == null);
		morcAndroidFactory = this;
		this.context = context;
	}

	static public MorcAndroidFactory getInstance() {
		assert (morcAndroidFactory != null);
		return morcAndroidFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory#getUserSettingsSource
	 * ()
	 */
	@Override
	public IUserSettingsSource getUserSettingsSource() {
		if (morcSettingsSource == null)
			morcSettingsSource = new MorcSettingsSource(context, this);
		return morcSettingsSource;
	}

}
