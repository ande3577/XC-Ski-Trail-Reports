package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;

public class ComposeButtonDecorator extends ButtonDecorator {

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;
		
		for (ISourceSpecificTrailInfo sourceSpecificInfo : info.getSourceSpecificInfos()) {
			String composeUrl = sourceSpecificInfo.getComposeUrl();
			if ((composeUrl != null) && (composeUrl.length() > 0))
				return true;
		}
		return false;
	}

	@Override
	protected IImageItem getImageItem(ITrailReportListEntry listEntry) {
		return listEntry.getComposeImageItem();
	}

	@Override
	protected String getImageString(TrailReport trailReport) {
		return "ComposeButton";
	}

}
