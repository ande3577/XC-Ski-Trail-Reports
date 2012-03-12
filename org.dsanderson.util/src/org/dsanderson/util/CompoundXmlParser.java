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
package org.dsanderson.util;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public abstract class CompoundXmlParser {
	String name = null;
	String text = "";
	private List<CompoundXmlParser> tagParsers = new ArrayList<CompoundXmlParser>();
	private List<Attribute> attributes = new ArrayList<CompoundXmlParser.Attribute>();

	protected class Attribute {
		public final String name;
		public final String value;

		Attribute(String name, String value) {
			this.name = name;
			this.value = value;
		}

		Attribute copy() {
			return new Attribute(name, value);
		}
	}

	public abstract CompoundXmlParser copy();

	public abstract void parse(Reader reader) throws Exception;

	public abstract void write(Writer writer) throws Exception;

	public CompoundXmlParser() {
	}

	public CompoundXmlParser(String name) {
		this.name = name;
	}

	public CompoundXmlParser(String name, String text) {
		this.name = name;
		this.text = text;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addAttribute(String name, String value) {
		attributes.add(new Attribute(name, value));
	}

	public void addParser(CompoundXmlParser parser) {
		tagParsers.add(parser.copy());
	}

	public abstract void addParser(String name, String text);

	public String getAttribute(String name) {
		String topLevelTag = getTopLevelTag(name);
		String remainingTag = getRemainingTag(name);
		if (remainingTag.length() == 0) {
			for (Attribute attribute : attributes) {
				if (name.compareTo(attribute.name) == 0) {
					return attribute.value;
				}
			}
			return null;
		} else {
			for (CompoundXmlParser child : tagParsers) {
				if (topLevelTag.compareTo(child.getName()) == 0) {
					return getAttribute(remainingTag);
				}
			}
			return null;
		}
	}

	public String getValue(String name) {
		String attributeValue = getAttribute(name);
		if (attributeValue != null)
			return attributeValue;

		List<CompoundXmlParser> parsers = getParsers(name);
		if (parsers.size() > 0) {
			return parsers.get(0).getText();
		}

		return null;
	}

	public List<CompoundXmlParser> getParsers() {
		return tagParsers;
	}

	public List<CompoundXmlParser> getParsers(String name) {
		List<CompoundXmlParser> returnParsers = new ArrayList<CompoundXmlParser>();
		String remainingTarget = getRemainingTag(name);
		String topLevelTarget = getTopLevelTag(name);

		for (CompoundXmlParser parser : tagParsers) {
			if (topLevelTarget.compareTo(parser.getName()) == 0) {
				if (remainingTarget.length() == 0) {
					// if we are looking for this child menu
					if (topLevelTarget.compareTo(name) == 0)
						returnParsers.add(parser);
				} else {
					// otherwise we are looking for one of its children
					for (CompoundXmlParser childParser : parser
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

	public String getText(String name) {
		if (name.length() == 0)
			return getText();
		else {
			List<CompoundXmlParser> parsers = getParsers(name);
			if (parsers.size() == 0)
				return null;
			else
				return parsers.get(0).text;
		}
	}

	protected List<Attribute> getAttributes() {
		return attributes;
	}

	protected String getTopLevelTag(String target) {
		if (target.indexOf(':') != -1)
			return target.substring(0, target.indexOf(':')).replaceFirst(":",
					"");
		else
			return target;
	}

	protected String getRemainingTag(String target) {
		if (target.indexOf(':') != -1) {
			return target.substring(target.indexOf(':'), target.length())
					.replaceFirst(":", "");
		} else {
			return "";
		}
	}

	protected void copy(CompoundXmlParser dest, CompoundXmlParser src) {
		dest.name = src.name;
		dest.text = src.text;

		for (CompoundXmlParser child : src.getParsers()) {
			dest.addParser(child.copy());
		}

		for (Attribute attribute : attributes) {
			dest.addAttribute(attribute.copy());
		}

	}

	private void addAttribute(Attribute attribute) {
		attributes.add(attribute.copy());
	}

}
