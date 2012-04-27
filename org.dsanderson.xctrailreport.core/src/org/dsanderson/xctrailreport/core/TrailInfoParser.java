package org.dsanderson.xctrailreport.core;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.util.CompoundXmlParser;
import org.dsanderson.util.ICompoundXmlParserFactory;

public class TrailInfoParser {
	final ICompoundXmlParserFactory parserFactory;
	final TrailInfoPool infoPool;
	final List<ISourceSpecificFactory> sources;

	// names of the XML tags
	static final String TRAIL_INFO = "trailInfo";
	static final String TRAIL = "trail";
	static final String NAME = "name";
	static final String CITY = "city";
	static final String STATE = "state";
	static final String LOCATION = "location";
	static final String SPECIFIC_LOCATION = "specificLocation";
	static final String DISTANCE = "distance";
	static final String DURATION = "duration";
	static final String DISTANCE_VALID = "distanceValid";
	static final String DURATION_VALID = "durationValid";

	static final String SKINNYSKI_SEARCH_TERM = "skinnyskiSearchTerm";
	static final String SKINNYSKI_TRAIL_INDEX = "skinnyskiTrailIndex";

	static final String THREE_RIVERS_SEARCH_TERM = "threeRiversSearchTerm";

	List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();

	public TrailInfoParser(ICompoundXmlParserFactory parserFactory,
			TrailInfoPool infoPool, List<ISourceSpecificFactory> sources) {
		this.parserFactory = parserFactory;
		this.infoPool = infoPool;
		this.sources = sources;
	}

	public List<TrailInfo> getTrailInfo() {
		return trailInfo;
	}

	public void setTrailInfo(List<TrailInfo> trailInfo) {
		this.trailInfo.clear();
		for (TrailInfo info : trailInfo)
			this.trailInfo.add(info);
	}

	public void parse(Reader reader) throws Exception {
		try {
			CompoundXmlParser tagParser = parserFactory.newParser();
			tagParser.parse(reader);

			List<CompoundXmlParser> trailInfoParser = tagParser
					.getParsers(TRAIL_INFO + ":" + TRAIL);

			for (CompoundXmlParser parser : trailInfoParser) {
				TrailInfo info = infoPool.newItem();
				String parserOutput;
				if ((parserOutput = parser.getValue(NAME)) != null)
					info.setName(parserOutput);
				if ((parserOutput = parser.getValue(CITY)) != null)
					info.setCity(parserOutput);
				if ((parserOutput = parser.getValue(STATE)) != null)
					info.setState(parserOutput);
				if ((parserOutput = parser.getValue(LOCATION)) != null)
					info.setLocation(parserOutput);
				if ((parserOutput = parser.getValue(SPECIFIC_LOCATION)) != null)
					info.setSpecificLocation(parserOutput.equals("true"));
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
				if ((parserOutput = parser.getValue(DURATION_VALID)) != null) {
					if (parserOutput.length() > 0)
						info.setDurationValid(Boolean
								.parseBoolean(parserOutput));
				}

				for (ISourceSpecificFactory source : sources) {
					List<CompoundXmlParser> sourceSpecificParser = parser
							.getParsers(source.getSourceSpecificXmlKey());
					if (sourceSpecificParser.size() > 0) {
						info.addSourceSpecificInfo(source
								.getSourceSpecificParser().parse(
										sourceSpecificParser.get(0)));
					}
				}

				mergeTrailInfo(trailInfo, info);
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
			for (ISourceSpecificTrailInfo sourceSpecific : info
					.getSourceSpecificInfos()) {
				for (ISourceSpecificFactory source : sources) {
					if (source.getSourceName().equals(
							sourceSpecific.getSourceName())) {
						CompoundXmlParser newParser = source
								.getSourceSpecificParser().buildParser(
										sourceSpecific, parserFactory);
						if (newParser != null)
							infoBuilder.addParser(newParser);
					}
				}
			}
			infoBuilder.addParser(DISTANCE,
					Integer.toString(info.getDistance()));
			infoBuilder.addParser(DURATION,
					Integer.toString(info.getDuration()));
			infoBuilder.addParser(DISTANCE_VALID,
					Boolean.toString(info.getDistanceValid()));
			infoBuilder.addParser(DURATION_VALID,
					Boolean.toString(info.getDurationValid()));
			xmlBuilder.addParser(infoBuilder.copy());
		}
		xmlBuilder.write(writer);
	}

	public void buildTrailInfoListFromReports(List<TrailReport> trailReports) {
		trailInfo.clear();
		for (TrailReport report : trailReports) {
			// don't add info that's already in the list
			if (getTrailInfoByName(report.getTrailInfo().getName()) == null) {
				trailInfo.add(report.getTrailInfo());
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

	private void mergeTrailInfo(List<TrailInfo> currentInfos, TrailInfo newInfo) {
		boolean found = false;
		for (TrailInfo current : currentInfos) {
			if (current.getName().equals(newInfo.getName())) {
				found = true;
				if (current.getCity() == null || current.getState().isEmpty())
					current.setCity(newInfo.getCity());
				if (current.getState() == null || current.getState().isEmpty())
					current.setState(newInfo.getState());
				if (current.getLocation() == null
						|| current.getLocation().isEmpty())
					current.setLocation(newInfo.getLocation());
				if (!current.getSpecificLocation())
					current.setSpecificLocation(newInfo.specificLocation);
				for (ISourceSpecificTrailInfo sourceSpecific : newInfo
						.getSourceSpecificInfos()) {
					if (current.getSourceSpecificInfo(sourceSpecific
							.getSourceName()) == null) {
						current.addSourceSpecificInfo(sourceSpecific);
					}
				}

				if (!current.getDistanceValid() && !current.getDurationValid()) {
					current.setDistanceValid(newInfo.getDistanceValid());
					current.setDurationValid(newInfo.durationValid);
					current.setDistance(newInfo.getDistance());
					current.setDuration(newInfo.getDuration());
				} else if (!current.getDurationValid()) { // distance is valid,
															// duration isn't
					if (newInfo.getDistanceValid()
							&& newInfo.getDurationValid()) {
						current.setDistanceValid(newInfo.getDistanceValid());
						current.setDurationValid(newInfo.durationValid);
						current.setDistance(newInfo.getDistance());
						current.setDuration(newInfo.getDuration());
					}
				} else if (!current.getDistanceValid()) { // duration is valid,
															// distance isn't
					if (newInfo.getDistanceValid()
							&& newInfo.getDurationValid()) {
						current.setDistanceValid(newInfo.getDistanceValid());
						current.setDurationValid(newInfo.durationValid);
						current.setDistance(newInfo.getDistance());
						current.setDuration(newInfo.getDuration());
					}
				}
				break;
			}
		}
		if (!found) {
			currentInfos.add(newInfo);
		} else {
			infoPool.recycleItem(newInfo);
		}
	}
}