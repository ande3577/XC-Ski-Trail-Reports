package org.dsanderson.xctrailreport.decorators;

import org.dsanderson.util.IImageItem;
import org.dsanderson.xctrailreport.core.IAbstractFactory;
import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.TrailInfoSources;
import org.dsanderson.xctrailreport.core.TrailReport;

public class TrailInfoButtonDecorator extends ButtonDecorator {
	private final IAbstractFactory factory;

	public TrailInfoButtonDecorator(IAbstractFactory factory) {
		this.factory = factory;
	}

	@Override
	protected boolean displayButton(TrailReport trailReport) {
		TrailInfo info = trailReport.getTrailInfo();
		if (info == null)
			return false;
		TrailInfoSources infoSources = new TrailInfoSources(factory, info);
		if (!infoSources.isEmpty())
			return true;
		else
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
