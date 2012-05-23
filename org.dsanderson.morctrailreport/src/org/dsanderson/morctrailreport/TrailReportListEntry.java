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

import org.dsanderson.android.util.TextItem;
import org.dsanderson.util.IImageItem;
import org.dsanderson.util.ITextItem;
import org.dsanderson.xctrailreport.decorators.ITrailReportListEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 */
public class TrailReportListEntry implements ITrailReportListEntry {
	final ViewGroup viewGroup;
	TextItem trailNameItem = null;
	TextItem trailLocationItem = null;
	TextItem dateItem = null;
	TextItem briefItem = null;
	TextItem detailedItem = null;
	TextItem authorItem = null;
	ConditionsImageItem imageItem = null;

	public TrailReportListEntry(ViewGroup viewGroup) {
		this.viewGroup = viewGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#draw()
	 */
	public void draw() {
		getTrailNameTextItem().draw();
		getTrailLocationTextItem().draw();
		getConditionsImageItem().draw();
		getDateTextItem().draw();
		getBriefConditionsTextItem().draw();
		getDetailedConditionsTextItem().draw();
		getAuthorTextItem().draw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getTrailNameTextItem()
	 */
	public ITextItem getTrailNameTextItem() {
		if (trailNameItem == null) {
			trailNameItem = new TextItem(
					(TextView) viewGroup.findViewById(R.id.trailNameView));
		}
		return trailNameItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getTrailLocationTextItem()
	 */
	public ITextItem getTrailLocationTextItem() {
		if (trailLocationItem == null) {
			trailLocationItem = new TextItem(
					(TextView) viewGroup.findViewById(R.id.trailLocationView));
		}
		return trailLocationItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#getDateTextItem
	 * ()
	 */
	public ITextItem getDateTextItem() {
		if (dateItem == null)
			dateItem = new TextItem(
					(TextView) viewGroup.findViewById(R.id.dateView));
		return dateItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getBriefConditionsTextItem()
	 */
	public ITextItem getBriefConditionsTextItem() {
		if (briefItem == null)
			briefItem = new TextItem(
					(TextView) viewGroup.findViewById(R.id.briefConditionsView));
		return briefItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getDetailedConditionsTextItem()
	 */
	public ITextItem getDetailedConditionsTextItem() {
		if (detailedItem == null)
			detailedItem = new TextItem(
					(TextView) viewGroup
							.findViewById(R.id.detailedConditionsView));
		return detailedItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getAuthorTextItem()
	 */
	public ITextItem getAuthorTextItem() {
		if (authorItem == null)
			authorItem = new TextItem(
					(TextView) viewGroup.findViewById(R.id.authorView));
		return authorItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getPhotosetTextItem()
	 */
	public ITextItem getPhotosetTextItem() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * setTitleGroupVisible(boolean)
	 */
	public void setTitleGroupVisible(boolean visible) {
		int visibility = View.GONE;
		if (visible)
			visibility = View.VISIBLE;
		viewGroup.findViewById(R.id.titleGroup).setVisibility(visibility);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#
	 * getConditionsImageItem()
	 */
	public IImageItem getConditionsImageItem() {
		if (imageItem == null)
			imageItem = new ConditionsImageItem(
					(ImageView) viewGroup.findViewById(R.id.conditionImageView));
		return imageItem;
	}

	/* (non-Javadoc)
	 * @see org.dsanderson.xctrailreport.decorators.ITrailReportListEntry#getSourceTextItem()
	 */
	public ITextItem getSourceTextItem() {
		return null;
	}
}
