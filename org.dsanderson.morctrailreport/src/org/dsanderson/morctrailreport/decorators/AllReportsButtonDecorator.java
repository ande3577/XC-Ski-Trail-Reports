package org.dsanderson.morctrailreport.decorators;

import org.dsanderson.morctrailreport.parser.MorcFactory;
import org.dsanderson.morctrailreport.parser.MorcSpecificTrailInfo;
import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.decorators.ButtonDecorator;
import org.dsanderson.xctrailreport.decorators.ITrailReportListEntry;

public class AllReportsButtonDecorator extends ButtonDecorator {

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;
		ISourceSpecificTrailInfo specificInfo = info
				.getSourceSpecificInfo(MorcFactory.SOURCE_NAME);
		if (specificInfo == null)
			return false;

		MorcSpecificTrailInfo morcInfo = (MorcSpecificTrailInfo) specificInfo;
		String allReportsUrl = morcInfo.getAllTrailReportUrl();
		return allReportsUrl != null && allReportsUrl.length() > 0;
	}

	@Override
	protected IImageItem getImageItem(ITrailReportListEntry listEntry) {
		return listEntry.getAllReportsImageItem();
	}

	@Override
	protected String getImageString(TrailReport trailReport) {
		return "AllReportsImage"; 
	}

}
