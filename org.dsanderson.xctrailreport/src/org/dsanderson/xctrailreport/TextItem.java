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

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 */
public class TextItem implements ITextItem {
	final ViewGroup layout;
	final Context context;
	boolean italic = false;
	boolean bold = false;
	String textString = "";
	Integer size = null;
	Integer backgroundColor = null;
	Integer textColor = null;
	TextView textView = null;

	public TextItem(Context context, ViewGroup layout) {
		this.context = context;
		this.layout = layout;
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
		this.size = new Integer(size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setColor(int)
	 */
	private void setColor(int color) {
		textColor = new Integer(color);
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
	 * @see org.dsanderson.xctrailreport.core.ITextItem#draw()
	 */
	public void draw() {
		if (textView == null)
			textView = new TextView(context);

		layout.addView(textView);

		if (size != null)
			textView.setTextSize(size);

		if (backgroundColor != null)
			textView.setBackgroundColor(backgroundColor);

		if (textColor != null)
			textView.setTextColor(textColor);

		String htmlString = textString;
		if (bold)
			htmlString = "<b>" + htmlString + "</b>";
		if (italic)
			htmlString = "<i>" + htmlString + "</i>";
		textView.setText(Html.fromHtml(htmlString));
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.ITextItem#setStyle(org.dsanderson.
	 * xctrailreport.core.ITextItem.FieldId_t)
	 */
	public void setStyle(FieldId_t fieldId) {
		switch (fieldId) {
		case NAME: {
			newTitleTextView();
			setSize(18);
		}
			break;
		case LOCATION: {
			newTitleTextView();
			textView.setTypeface(Typeface.DEFAULT);
		}
			break;
		case DATE:
			setColor(context.getResources().getColor(
					android.R.color.tertiary_text_dark));
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

	private void newTitleTextView() {
		LayoutInflater inflator = LayoutInflater.from(context);
		LinearLayout preferenceCategory = (LinearLayout) inflator.inflate(
				android.R.layout.preference_category, layout);
		// android.R.style.TextAppearance_Inverse);
		textView = ((TextView) preferenceCategory
				.findViewById(android.R.id.title));
		preferenceCategory.removeView(textView);
	}

}
