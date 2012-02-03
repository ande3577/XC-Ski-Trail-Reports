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
package org.dsanderson.xctrailreport.core;

/**
 * 
 */
public class TrailReport {
	private String summary = "";
	private String author = "";
	private String date = "";
	private String detail = "";
	private String source = "";
	private TrailInfo trailInfo = null;

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return summary;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setTrailInfo(TrailInfo trailInfo) {
		this.trailInfo = trailInfo;
	}

	public TrailInfo getTrailInfo() {
		return trailInfo;
	}

	public TrailReport copy() {
		TrailReport newReport = new TrailReport();
		newReport.summary = summary;
		newReport.author = author;
		newReport.date = date;
		newReport.detail = detail;
		newReport.source = source;
		newReport.trailInfo = trailInfo;
		return newReport;
	}
}
