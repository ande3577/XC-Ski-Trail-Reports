package org.dsanderson.xctrailreport.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dsanderson.TrailInfo;
import org.xml.sax.InputSource;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class BaseFeedParser {

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

	public BaseFeedParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	protected InputStream getInputStream() {
		return inputStream;
	}

	public List<TrailInfo> parse() {
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
		return messages;
	}
}