package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.ISourceSpecificTrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailReport;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiFactory;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiSpecificInfo;

public class MoreButtonDecorator extends ButtonDecorator {

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;

		for (ISourceSpecificTrailInfo sourceSpecificInfo : info
				.getSourceSpecificInfos()) {
			String composeUrl = sourceSpecificInfo.getComposeUrl();
			if ((composeUrl != null) && (composeUrl.length() > 0))
				return true;

		}

		ISourceSpecificTrailInfo specInfo = info
				.getSourceSpecificInfo(SkinnyskiFactory.SKINNYSKI_SOURCE_NAME);
		if (specInfo != null) {
			SkinnyskiSpecificInfo skinnyskiInfo = (SkinnyskiSpecificInfo) specInfo;
			String requestUrl = skinnyskiInfo.getRequestUrl();
			if ((requestUrl != null) && (requestUrl.length() > 0))
				return true;
		}

		return false;
	}

	@Override
	protected IImageItem getImageItem(ITrailReportListEntry listEntry) {
		return listEntry.getMoreImageItem();
	}

	@Override
	protected String getImageString(TrailReport trailReport) {
		return "MoreButton";
	}

}
