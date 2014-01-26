package org.dsanderson.xctrailreport.skinnyski;

import java.io.InputStream;
import java.util.Scanner;

import org.dsanderson.xctrailreport.core.TrailInfo;

public class SkinnyskiTrailInfoScanner {
	Scanner scanner;
	boolean firstReportFound = false;
	
	public SkinnyskiTrailInfoScanner(InputStream stream) {
		scanner = new Scanner(stream);
		scanner.useDelimiter("\n");
	}
	
	public boolean scan(TrailInfo trailInfo, SkinnyskiSpecificInfo skinnyskiInfo) throws Exception {
		if (findStartOfTrailInfo()) {
			trailInfo.setCity(scanCity());
			trailInfo.setLocation(scanLocation());
			trailInfo.setSpecificLocation(true);
			trailInfo.setName(scanName());
			skinnyskiInfo.setTrailIndex(scanTrailIndex());
			return true;
		} else {
			return false;
		}
	}
	
	private boolean findStartOfTrailInfo() {
		if(!firstReportFound) {
			firstReportFound = true;
			return findStartOfTrails();
		} else {
			return findStartOfNextTrailInfo();
		}
	}
	
	private boolean findStartOfTrails() {
		return scanner.findWithinHorizon("<b>All[\t\n\r ]+Regions</b>", 0) != null;
	}
	
	private boolean findStartOfNextTrailInfo() {
		do {
		String line = scanner.next();
		if(line == null)
			return false;
		else if(line.contains("</table>"))
			return false;
		else if(line.contains("<tr>"))
			return true;
		} while(true);
	}
	
	private String scanCity() throws Exception {
		return find("<td>", "</td>", "city");
	}
	
	private String scanLocation() throws Exception {
		return find("GLatLng\\(", "\\)", "trail latitude").replace(" ", "");
	}
	
	private String scanName() throws Exception {
		String name = scanner.findWithinHorizon("<b>", 0);
		name = scanner.next();
		name = name.replace("</b>", "").trim();
		name = removeUrl(name);
		return name;
	}
	
	private String removeUrl(String string) throws Exception {
		if(string.contains("<a")) {
			string = string.split(">")[1];
			string = string.replace("</a", "").trim();
		}
		return string;
	}
	
	private int scanTrailIndex() throws Exception {
		String index = find("traildetail.asp\\?Id=", "\">", "trail index");
		try {
			return Integer.parseInt(index);			
		} catch (NumberFormatException e) {
			throw new Exception("Error: invalid trail index: " + index);
		}
	}
	
	private String find(String before, String after, String ItemName) throws Exception {
		String result = scanner.findWithinHorizon(before + "[a-zA-z0-9,: '/.-]+" + after, 0);
		try {
			result = result.replaceFirst(before, "").replaceFirst(after,"");	
		} catch (NullPointerException e) {
			throw new Exception("Error: Cannot find " + ItemName + ".");
		}
		
		return result.trim();
	}
	
}
