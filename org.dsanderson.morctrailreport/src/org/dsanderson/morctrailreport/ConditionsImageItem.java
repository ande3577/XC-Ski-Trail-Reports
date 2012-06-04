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

import org.dsanderson.util.IImageItem;

import android.view.View;
import android.widget.ImageView;

/**
 * 
 */
public class ConditionsImageItem implements IImageItem {
	private final ImageView imageView;

	/**
	 * 
	 */
	public ConditionsImageItem(ImageView imageView) {
		this.imageView = imageView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IImageItem#setImage(java.lang.String)
	 */
	public void setImage(String imageName) {

		int resId = -1;
		if (imageName.equals("good"))
			resId = R.drawable.presence_online;
		else if (imageName.equals("marginal"))
			resId = R.drawable.presence_away;
		else if (imageName.equals("closed"))
			resId = R.drawable.presence_busy;

		if (resId < 0) {
			imageView.setVisibility(View.INVISIBLE);
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(resId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.util.IImageItem#draw()
	 */
	public void draw() {
	}

}
