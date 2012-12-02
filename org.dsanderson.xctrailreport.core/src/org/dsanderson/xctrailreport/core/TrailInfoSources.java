package org.dsanderson.xctrailreport.core;

import java.util.HashMap;

public class TrailInfoSources {
	private HashMap<String, String> sources = new HashMap<String, String>();
	private final IAbstractFactory factory;

	public TrailInfoSources(IAbstractFactory factory) {
		this.factory = factory;
	}
	
	public TrailInfoSources(IAbstractFactory factory, TrailInfo info) {
		this.factory = factory;
		getTrailInfoSources(info);
	}

	public void addTrailInfoSource(String sourceName, String trailInfoUrl) {
		sources.put(sourceName, trailInfoUrl);
	}

	public String[] getTrailInfoSources() {
		String[] keys = new String[sources.size()];
		Object[] keysObjects = sources.keySet().toArray();
		for (int i = 0; i < keysObjects.length; i++) {
			keys[i] = (String) keysObjects[i];
		}
		return keys;
	}

	public String getTrailInfoUrl(String sourceName) {
		return sources.get(sourceName);
	}

	public Boolean isEmpty() {
		return sources.isEmpty();
	}

	public int size() {
		return sources.size();
	}

	public void getTrailInfoSources(TrailInfo info) {
		for (ISourceSpecificTrailInfo specificInfo : info
				.getSourceSpecificInfos()) {
			ISourceSpecificFactory sourceSpecificFactory = factory
					.getSourceSpecificFactory(specificInfo.getSourceName());
			if (sourceSpecificFactory != null
					&& sourceSpecificFactory.getEnabled()) {
				String trailInfoUrl = specificInfo.getTrailInfoUrl();
				if (trailInfoUrl != null && trailInfoUrl.length() > 0)
					addTrailInfoSource(specificInfo.getSourceName(),
							specificInfo.getTrailInfoUrl());
			}
		}
	}

}
