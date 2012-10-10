package org.dsanderson.xctrailreport;

import org.dsanderson.xctrailreport.core.IDecoratorFactory;
import org.dsanderson.xctrailreport.decorators.AuthorDecorator;
import org.dsanderson.xctrailreport.decorators.CityStateDecorator;
import org.dsanderson.xctrailreport.decorators.ComposeButtonDecorator;
import org.dsanderson.xctrailreport.decorators.DateDecorator;
import org.dsanderson.xctrailreport.decorators.DetailedReportDecorator;
import org.dsanderson.xctrailreport.decorators.DistanceDecorator;
import org.dsanderson.xctrailreport.decorators.MapButtonDecorator;
import org.dsanderson.xctrailreport.decorators.PhotosetDecorator;
import org.dsanderson.xctrailreport.decorators.SummaryDecorator;
import org.dsanderson.xctrailreport.decorators.TimeDecorator;
import org.dsanderson.xctrailreport.decorators.TrailInfoButtonDecorator;
import org.dsanderson.xctrailreport.decorators.TrailNameDecorator;
import org.dsanderson.xctrailreport.decorators.TrailReportDecorator;

public class ReportDecoratorFactory implements IDecoratorFactory {
	static ReportDecoratorFactory instance = null;
	TrailReportDecorator infoDecorator = null;
	TrailReportDecorator reportDecorator = null;
	
	private ReportDecoratorFactory() {
	}
	
	static public ReportDecoratorFactory getInstance() {
		if (instance == null)
			instance = new ReportDecoratorFactory();
		
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoDecorators
	 * ()
	 */
	public TrailReportDecorator getTrailInfoDecorators() {
		if (infoDecorator == null) {
			infoDecorator = new TrailNameDecorator();
			infoDecorator.add(new CityStateDecorator());
			infoDecorator.add(new DistanceDecorator());
			infoDecorator.add(new MapButtonDecorator());
			infoDecorator.add(new ComposeButtonDecorator());
			infoDecorator.add(new TrailInfoButtonDecorator());
		}

		return infoDecorator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.IAbstractFactory#getTrailInfoDecorators
	 * ()
	 */
	public TrailReportDecorator getTrailReportDecorators() {
		if (reportDecorator == null) {
			reportDecorator = new DateDecorator();
			reportDecorator.add(new TimeDecorator());
			reportDecorator.add(new SummaryDecorator());
			reportDecorator.add(new DetailedReportDecorator());
			reportDecorator.add(new AuthorDecorator());
			reportDecorator.add(new PhotosetDecorator());
		}

		return reportDecorator;
	}

}
