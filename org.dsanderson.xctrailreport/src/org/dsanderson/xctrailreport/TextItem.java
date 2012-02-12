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
import android.text.method.LinkMovementMethod;
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
		textView.setMovementMethod(LinkMovementMethod.getInstance());
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
	private void setSize(int size) {
		textView.setTextSize(size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITextItem#setColor(java.lang.String)
	 */
	private void setColor(String color) {
		setColor(Color.parseColor(color));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setColor(int)
	 */
	private void setColor(int color) {
		textView.setTextColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setItalic(boolean)
	 */
	private void setItalic(boolean italic) {
		this.italic = italic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setBold(boolean)
	 */
	private void setBold(boolean bold) {
		this.bold = bold;
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
	private void setBackgroundColor(String color) {
		setBackgroundColor(Color.parseColor(color));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setBackgroundColor(int)
	 */
	private void setBackgroundColor(int color) {
		textView.setBackgroundColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setStyle(org.dsanderson.
	 * xctrailreport.core.ITextItem.FieldId_t)
	 */
	public void setStyle(FieldId_t fieldId) {
		switch (fieldId) {
		case NAME:
			setSize(16);
			setBold(true);
			setColor("black");
			setBackgroundColor(getTitleBackgroundColor());
			break;
		case LOCATION:
			setColor("darkgray");
			setBackgroundColor(getTitleBackgroundColor());
			break;
		case DATE:
			setColor("gray");
			break;
		case SUMMARY:
			setItalic(true);
			break;
		case DETAIL:
			break;
		case AUTHOR:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITextItem#getTitleBackgroundColor()
	 */
	public int getTitleBackgroundColor() {
		return Color.rgb(0xE3, 0xDA, 0xC9);
	}

}
