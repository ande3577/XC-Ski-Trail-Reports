package org.dsanderson.xctrailreport.core;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TrailInfoParser {
	final ICompoundXmlParserFactory parserFactory;

	// names of the XML tags
	static final String TRAIL_INFO = "trailInfo";
	static final String TRAIL = "trail";
	static final String NAME = "name";
	static final String CITY = "city";
	static final String STATE = "state";
	static final String LOCATION = "location";
	static final String DISTANCE = "distance";
	static final String DURATION = "duration";
	static final String DISTANCE_VALID = "distanceValid";

	static final String SKINNYSKI_SEARCH_TERM = "skinnyskiSearchTerm";
	static final String SKINNYSKI_TRAIL_INDEX = "skinnyskiTrailIndex";

	static final String THREE_RIVERS_SEARCH_TERM = "threeRiversSearchTerm";

	List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();

	public TrailInfoParser(ICompoundXmlParserFactory parserFactory) {
		this.parserFactory = parserFactory;
	}

	public List<TrailInfo> getTrailInfo() {
		return trailInfo;
	}

	public void setTrailInfo(List<TrailInfo> trailInfo) {
		this.trailInfo.clear();
		for (TrailInfo info : trailInfo)
			this.trailInfo.add(info.copy());
	}

	public void parse(Reader reader) throws Exception {
		try {
			CompoundXmlParser tagParser = parserFactory.newParser();
			tagParser.parse(reader);

			List<CompoundXmlParser> trailInfoParser = tagParser
					.getParsers(TRAIL_INFO + ":" + TRAIL);

			for (CompoundXmlParser parser : trailInfoParser) {
				TrailInfo info = new TrailInfo();
				String parserOutput;
				if ((parserOutput = parser.getValue(NAME)) != null)
					info.setName(parserOutput);
				if ((parserOutput = parser.getValue(CITY)) != null)
					info.setCity(parserOutput);
				if ((parserOutput = parser.getValue(STATE)) != null)
					info.setState(parserOutput);
				if ((parserOutput = parser.getValue(LOCATION)) != null)
					info.setLocation(parserOutput);
				if ((parserOutput = parser.getValue(DISTANCE)) != null) {
					if (parserOutput.length() > 0)
						info.setDistance(Integer.parseInt(parserOutput));
				}
				if ((parserOutput = parser.getValue(DURATION)) != null) {
					if (parserOutput.length() > 0)
						info.setDuration(Integer.parseInt(parserOutput));
				}
				if ((parserOutput = parser.getValue(DISTANCE_VALID)) != null) {
					if (parserOutput.length() > 0)
						info.setDistanceValid(Boolean
								.parseBoolean(parserOutput));
				}
				if ((parserOutput = parser.getValue(SKINNYSKI_SEARCH_TERM)) != null)
					info.setSkinnyskiSearchTerm(parserOutput);
				if ((parserOutput = parser.getValue(SKINNYSKI_TRAIL_INDEX)) != null) {
					if (parserOutput.length() > 0)
						info.setskinnyskiTrailIndex(Integer
								.parseInt(parserOutput));
				}
				if ((parserOutput = parser.getValue(THREE_RIVERS_SEARCH_TERM)) != null)
					info.setThreeRiversSearchTerm(parserOutput);

				trailInfo.add(info.copy());
			}
		} catch (Exception e) {
			trailInfo.clear();
			throw e;
		}

	}

	public void write(Writer writer) throws Exception {
		CompoundXmlParser xmlBuilder = parserFactory.newParser(TRAIL_INFO);
		for (TrailInfo info : trailInfo) {
			CompoundXmlParser infoBuilder = parserFactory.newParser(TRAIL);
			infoBuilder.addParser(NAME, info.getName());
			infoBuilder.addParser(CITY, info.getCity());
			infoBuilder.addParser(STATE, info.getState());
			infoBuilder.addParser(LOCATION, info.getLocation());
			infoBuilder.addParser(SKINNYSKI_SEARCH_TERM,
					info.getSkinnyskiSearchTerm());
			infoBuilder.addParser(SKINNYSKI_TRAIL_INDEX,
					Integer.toString(info.getskinnyskiTrailIndex()));
			infoBuilder.addParser(THREE_RIVERS_SEARCH_TERM,
					info.getThreeRiversSearchTerm());
			infoBuilder.addParser(DISTANCE,
					Integer.toString(info.getDistance()));
			infoBuilder.addParser(DURATION,
					Integer.toString(info.getDuration()));
			infoBuilder.addParser(DISTANCE_VALID,
					Boolean.toString(info.getDistanceValid()));
			xmlBuilder.addParser(infoBuilder.copy());
		}
		xmlBuilder.write(writer);
	}

	public void buildTrailInfoListFromReports(List<TrailReport> trailReports) {
		trailInfo.clear();
		for (TrailReport report : trailReports) {
			// don't add info that's already in the list
			if (getTrailInfoByName(report.getTrailInfo().getName()) == null) {
				trailInfo.add(report.getTrailInfo().copy());
			}
		}
	}

	public void clear() {
		trailInfo.clear();
	}

	private TrailInfo getTrailInfoByName(String name) {
		for (TrailInfo info : trailInfo) {
			if (info.getName().equals(name))
				return info;
		}
		return null;
	}

}