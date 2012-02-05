package org.dsanderson.xctrailreport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.core.ITrailInfoParser;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class BaseFeedParser implements ITrailInfoParser {
	InputStream inputStream;

	// names of the XML tags
	static final String TRAIL_INFO = "trailInfo";
	static final String TRAIL = "trail";
	static final String NAME = "name";
	static final String CITY = "city";
	static final String STATE = "state";
	static final String LOCATION = "location";

	static final String SKINNYSKI_SEARCH_TERM = "skinnyskiSearchTerm";
	static final String SKINNYSKI_URL = "skinnyskiUrl";

	static final String THREE_RIVERS_SEARCH_TERM = "threeRiversSearchTerm";

	List<TrailInfo> trailInfo = new ArrayList<TrailInfo>();

	public BaseFeedParser() {
	}

	protected InputStream getInputStream() {
		return inputStream;
	}

	public void parse() throws Exception {
		final TrailInfo currentMessage = new TrailInfo();
		RootElement root = new RootElement(TRAIL_INFO);
		final List<TrailInfo> messages = new ArrayList<TrailInfo>();
		Element item = root.getChild(TRAIL);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(NAME).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setName(body);
					}
				});
		item.getChild(CITY).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setCity(body);
					}
				});
		item.getChild(STATE).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setState(body);
					}
				});
		item.getChild(LOCATION).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setLocation(body);
					}
				});
		item.getChild(SKINNYSKI_SEARCH_TERM).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setSkinnyskiSearchTerm(body);
					}
				});
		item.getChild(SKINNYSKI_URL).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setSkinnyskiUrl(body);
					}
				});
		item.getChild(THREE_RIVERS_SEARCH_TERM).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.setThreeRiversSearchTerm(body);
					}
				});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,
					root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		trailInfo = messages;
	}

	public List<TrailInfo> getTrailInfo() {
		return trailInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dsanderson.xctrailreport.core.ITrailInfoParser#SetInputStream(java
	 * .io.InputStream)
	 */
	public void SetInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}