package org.dsanderson.xctrailreport.core;

import org.dsanderson.xctrailreport.decorators.TrailReportDecorator;

public interface IDecoratorFactory {
	public TrailReportDecorator getTrailInfoDecorators();

	public TrailReportDecorator getTrailReportDecorators();
}
