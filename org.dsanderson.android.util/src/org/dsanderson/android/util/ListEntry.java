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

import java.util.ArrayList;
import java.util.List;

import org.dsanderson.android.util.TextItem;
import org.dsanderson.util.IListEntry;
import org.dsanderson.util.ITextItem;

import android.content.Context;
import android.view.ViewGroup;

/**
 * 
 */
public class ListEntry implements IListEntry {
	ViewGroup layout;
	Context context;
	List<TextItem> textItems = new ArrayList<TextItem>();

	public ListEntry(ViewGroup layout, Context context) {

		layout.removeAllViews();

		this.layout = layout;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IListEntry#newListItem()
	 */
	public ITextItem newTextItem() {
		TextItem newTextItem = new TextItem(context, layout);
		textItems.add(newTextItem);
		return newTextItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IListEntry#getTextItem()
	 */
	public ITextItem getTextItem() {
		if (textItems.size() > 0)
			return textItems.get(textItems.size() - 1);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.IListEntry#draw()
	 */
	public void draw() {
		for (TextItem item : textItems) {
			item.draw();
		}

	}

}
