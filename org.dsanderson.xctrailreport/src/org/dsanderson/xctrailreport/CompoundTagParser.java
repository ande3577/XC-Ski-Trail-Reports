/**
 * @author David S Anderson
 *
 *
 * Copyright (C) 2012 David S Anderson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dsanderson.xctrailreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 
 */
public class CompoundTagParser {

	String name = null;
	String text = "";
	private List<CompoundTagParser> tagParsers = new ArrayList<CompoundTagParser>();

	public CompoundTagParser() {
	}

	public CompoundTagParser(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addParser(CompoundTagParser parser) {
		tagParsers.add(parser.copy());
	}

	public List<CompoundTagParser> getParsers() {
		return tagParsers;
	}

	public List<CompoundTagParser> getParsers(String name) {
		List<CompoundTagParser> returnParsers = new ArrayList<CompoundTagParser>();
		String remainingTarget = getRemainingTag(name);
		String topLevelTarget = getTopLevelTag(name);

		for (CompoundTagParser parser : tagParsers) {
			if (topLevelTarget.compareTo(parser.getName()) == 0) {
				if (remainingTarget.length() == 0) {
					// if we are looking for this child menu
					if (topLevelTarget.compareTo(name) == 0)
						returnParsers.add(parser);
				} else {
					// otherwise we are looking for one of its children
					for (CompoundTagParser childParser : parser
							.getParsers(remainingTarget)) {
						returnParsers.add(childParser);
					}
				}
			}
		}
		return returnParsers;
	}

	public void setText(String value) {
		this.text = value;
	}

	public String getText() {
		return text;
	}

	public CompoundTagParser copy() {
		CompoundTagParser newParser = new CompoundTagParser(name);
		newParser.setText(text);
		for (CompoundTagParser parser : tagParsers) {
			newParser.addParser(parser);
		}
		return newParser;
	}

	public void parse(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parse(parser, "");
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				name = null;
			} else if (eventType == XmlPullParser.START_TAG) {
				if (name == null) {
					name = parser.getName();
				} else {
					CompoundTagParser newParser = new CompoundTagParser(
							parser.getName());
					parser.next();
					newParser.parse(parser);
					addParser(newParser);
				}
			} else if (eventType == XmlPullParser.TEXT) {
				if (parser.getText().trim().length() > 0)
					text += parser.getText();
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().compareTo(name) == 0) {
					break;
				}
			}
			eventType = parser.next();
		}
	}

	public void parse(XmlPullParser parser, String target)
			throws XmlPullParserException, IOException {

		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				name = null;
			} else if (eventType == XmlPullParser.START_TAG) {
				if (target.length() > 0) {
					if (parser.getName().compareTo(getTopLevelTag(target)) == 0) {
						name = getTopLevelTag(target);
						enterChildTag(parser, getRemainingTag(target));
					}
				} else {
					CompoundTagParser newParser = new CompoundTagParser(
							parser.getName());
					parser.next();
					newParser.parse(parser);
					addParser(newParser);
				}
			} else if (eventType == XmlPullParser.TEXT) {
				if (parser.getText().trim().length() > 0)
					text += parser.getText();
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().compareTo(name) == 0) {
					break;
				}
			}

			if (target.length() > 0)
				eventType = parser.nextTag();
			else
				eventType = parser.next();
		}
	}

	private String getTopLevelTag(String target) {
		if (target.indexOf(':') != -1)
			return target.substring(0, target.indexOf(':')).replaceFirst(":",
					"");
		else
			return target;
	}

	private String getRemainingTag(String target) {
		if (target.indexOf(':') != -1) {
			return target.substring(target.indexOf(':'), target.length())
					.replaceFirst(":", "");
		} else {
			return "";
		}
	}

	private void enterChildTag(XmlPullParser parser, String target)
			throws XmlPullParserException, IOException {
		CompoundTagParser newParser = new CompoundTagParser(parser.getName());
		parser.next();
		newParser.parse(parser, target);
		addParser(newParser);
	}
}
