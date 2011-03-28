package edu.benjones.fluid;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;


public class SimGridTest {

	@Test
	public void testLoadSave() {
		SimGrid testGrid = new SimGrid(4,5, .1);
		testGrid.zero();
		testGrid.writeToFile("zeroSave.dat");
		SimGrid readGrid = SimGrid.loadFromFile("zeroSave.dat");
		assertEquals("Loaded",testGrid.checkEquals(readGrid), true);
		
	}
	@Test
	public void testEquals() {
		SimGrid testGrid = new SimGrid(4,5, .1);
		testGrid.zero();
		SimGrid otherGrid = new SimGrid(4,6, .1);
		otherGrid.zero();
		assertEquals("Equals", testGrid.checkEquals(otherGrid), false);
		
	}

}
