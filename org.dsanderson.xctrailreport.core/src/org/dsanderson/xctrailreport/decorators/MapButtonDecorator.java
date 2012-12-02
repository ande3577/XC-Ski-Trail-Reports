package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

public class MapButtonDecorator extends ButtonDecorator {

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;
		String location = info.getLocation();
		return (location != null) && (location.length() > 0) && info.getSpecificLocation();
	}

	@Override
	protected IImageItem getImageItem(ITrailReportListEntry listEntry) {
		return listEntry.getMapImageItem();
	}

	@Override
	protected String getImageString(TrailReport trailReport) {
		return "MapButtonImage";
	}

}
