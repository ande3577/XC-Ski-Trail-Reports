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

import org.dsanderson.xctrailreport.core.ITextItem;

import android.graphics.Color;
import android.text.Html;
import android.widget.TextView;

/**
 * 
 */
public class TextItem implements ITextItem {
	TextView textView;
	boolean italic;
	boolean bold;
	String textString;

	public TextItem(TextView textView) {
		this.textView = textView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#getText()
	 */
	public String getText() {
		return textString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setText()
	 */
	public void setText(String text) {
		textString = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setSize(int)
	 */
	public void setSize(int size) {
		textView.setTextSize(size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITextItem#setColor(java.lang.String)
	 */
	public void setColor(String color) {
		setColor(Color.parseColor(color));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setColor(int)
	 */
	public void setColor(int color) {
		textView.setTextColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setItalic(boolean)
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setBold(boolean)
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setAlign(org.dsanderson.
	 * xctrailreport.core.ITextItem.Alignment_t)
	 */
	public void setAlign(Alignment_t alignment) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#draw()
	 */
	public void draw() {
		String htmlString = textString;
		if (bold)
			htmlString = "<b>" + htmlString + "</b>";
		if (italic)
			htmlString = "<i>" + htmlString + "</i>";
		textView.setText(Html.fromHtml(htmlString));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITextItem#setBackgroundColor(java.lang
	 * .String)
	 */
	public void setBackgroundColor(String color) {
		setBackgroundColor(Color.parseColor(color));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setBackgroundColor(int)
	 */
	public void setBackgroundColor(int color) {
		textView.setBackgroundColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITextItem#getTitleBackgroundColor()
	 */
	public int getTitleBackgroundColor() {
		return Color.rgb(0x81, 0x73, 0x39);
	}
}
