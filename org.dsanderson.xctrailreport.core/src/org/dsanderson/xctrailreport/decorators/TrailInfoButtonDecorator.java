package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

public class TrailInfoButtonDecorator extends ButtonDecorator {

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;
		for(ISourceSpecificTrailInfo sourceSpecificInfo : info.getSourceSpecificInfos()) {
			String trailInfoUrl = sourceSpecificInfo.getTrailInfoUrl();
			if ((trailInfoUrl != null) && (trailInfoUrl.length() > 0))
				return true;
		}
		return false;
	}

	@Override
	protected IImageItem getImageItem(ITrailReportListEntry listEntry) {
		return listEntry.getTrailInfoImageItem();
	}

	@Override
	protected String getImageString(TrailReport trailReport) {
		return "TrailInfoButton";
	}

}
