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
package org.dsanderson.android.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.dsanderson.util.CompoundXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

/**
 * 
 */
public class CompoundXmlPullParser extends CompoundXmlParser {

	public CompoundXmlPullParser() {
		super();
	}

	public CompoundXmlPullParser(String name) {
		super(name);
	}

	public CompoundXmlPullParser(String name, String text) {
		super(name, text);
	}

	@Override
	public void addParser(String name, String text) {
		addParser(new CompoundXmlPullParser(name, text));
	}

	@Override
	public CompoundXmlPullParser copy() {
		CompoundXmlPullParser newParser = new CompoundXmlPullParser(getName());
		copy(newParser, this);
		return newParser;
	}

	@Override
	public void parse(Reader reader) throws XmlPullParserException, IOException {
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		parserFactory.setNamespaceAware(false);
		XmlPullParser parser = parserFactory.newPullParser();

		parser.setInput(reader);

		parse(parser, "");
	}

	private void parse(XmlPullParser parser, String target)
			throws XmlPullParserException, IOException {

		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				setName(null);
			} else if (eventType == XmlPullParser.START_TAG) {
				CompoundXmlPullParser newParser = new CompoundXmlPullParser(
						parser.getName());
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					newParser.addAttribute(parser.getAttributeName(i),
							parser.getAttributeValue(i));
				}
				parser.next();
				newParser.parse(parser, "");
				addParser(newParser);
			} else if (eventType == XmlPullParser.TEXT) {
				if (!parser.isWhitespace())
					setText(getText() + parser.getText());
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().compareTo(getName()) == 0) {
					break;
				}
			}

			eventType = parser.next();
		}
	}

	public void write(Writer writer) throws Exception {
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		parserFactory.setNamespaceAware(false);
		XmlSerializer serializer = parserFactory.newSerializer();

		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);
		write(serializer);
		serializer.endDocument();
	}

	private void write(XmlSerializer serializer) throws Exception {
		String name = getName();
		if (name != null)
			serializer.startTag("", name);
		for (Attribute attribute : getAttributes()) {
			serializer.attribute("", attribute.name, attribute.value);
		}
		String text = getText();
		if (text == null)
			text = "";

		serializer.text(text);

		for (CompoundXmlParser child : getParsers()) {
			((CompoundXmlPullParser) child).write(serializer);
		}
		if (name != null)
			serializer.endTag("", name);
	}

}
