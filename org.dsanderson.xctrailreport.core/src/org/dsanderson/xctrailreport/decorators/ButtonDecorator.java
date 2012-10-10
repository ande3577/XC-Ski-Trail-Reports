package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.TrailReport;

public abstract class ButtonDecorator extends TrailReportDecorator {

	protected abstract boolean displayButton(TrailReport trailReport);

	protected abstract IImageItem getImageItem(ITrailReportListEntry listEntry);

	protected abstract String getImageString(TrailReport trailReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsanderson.xctrailreport.core.TrailReportDecorator#decorate(org.
	 * dsanderson.xctrailreport.core.TrailReport,
	 * org.dsanderson.xctrailreport.core.IListEntry)
	 */
	@Override
	public void decorate(TrailReport trailReport,
			ITrailReportListEntry listEntry) {

		IImageItem imageItem = getImageItem(listEntry);
		if (imageItem != null) {
			if (displayButton(trailReport))
				imageItem.setImage(getImageString(trailReport));
			else
				imageItem.setImage("");
		}

		if (next() != null)
			next().decorate(trailReport, listEntry);
	}

}
