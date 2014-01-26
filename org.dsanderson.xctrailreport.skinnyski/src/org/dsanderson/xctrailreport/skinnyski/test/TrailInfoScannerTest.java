package org.dsanderson.xctrailreport.skinnyski.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.dsanderson.xctrailreport.core.TrailInfo;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiSpecificInfo;
import org.dsanderson.xctrailreport.skinnyski.SkinnyskiTrailInfoScanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrailInfoScannerTest {
	FileInputStream stream;
	SkinnyskiTrailInfoScanner scanner;
	TrailInfo trailInfo;
	SkinnyskiSpecificInfo skinnySkiInfo;

	@Before
	public void setUp() throws Exception {
		stream = new FileInputStream("Trails.htm");
		scanner = new SkinnyskiTrailInfoScanner(stream);
		trailInfo = new TrailInfo();
		skinnySkiInfo = new SkinnyskiSpecificInfo();
	}

	@After
	public void tearDown() throws Exception {
		stream.close();
	}

	@Test
	public void scanFirstTrail() throws Exception {
		assertTrue(scan());
		assertEquals("East River Valley Park/Carr's Woods", trailInfo.getName());
		assertEquals("Ames", trailInfo.getCity());
		assertEquals("42.0387,-93.6037", trailInfo.getLocation());
		assertEquals(true, trailInfo.getSpecificLocation());
		assertEquals(331, skinnySkiInfo.getTrailIndex());
	}
	
	@Test
	public void scanSecondTrail() throws Exception {
		assertTrue(scan(2));
		assertEquals("Ames", trailInfo.getCity());
		assertEquals("42.0209,-93.6539", trailInfo.getLocation());
		assertEquals("Iowa State University, Cross Country Course", trailInfo.getName());
	}
	
	@Test
	public void scanTrailWithUrl() throws Exception {
		assertTrue(scan(3));
		assertEquals("Cedar Valley Trail", trailInfo.getName());
	}
	
	@Test
	public void scanLastTrail() throws Exception {
		int trails = 0;
		// scan until reaching end
		while(scan()) {
			trails++;
		}	
		
		assertEquals(451, trails);
		assertEquals("Raven Trails", trailInfo.getName());
	}
	
	private boolean scan() throws Exception {
		return scan(1);
	}
	
	private boolean scan(int trailNumber) throws Exception {
		boolean result = false;
		for(int i = 0; i < trailNumber; i++) {
			result = scanner.scan(trailInfo, skinnySkiInfo);
		}
		return result;
	}
	
}
