package edu.benjones.fluid;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import edu.benjones.fluid.SimGrid.gridTypes;

public class SimGridTest {

	@Test
	public void testLoadSave() {
		SimGrid testGrid = new SimGrid(4, 5, .1);
		testGrid.zero();
		testGrid.writeToFile("zeroSave.dat");
		SimGrid readGrid = SimGrid.loadFromFile("zeroSave.dat");
		assertEquals("Loaded", testGrid.checkEquals(readGrid), true);

	}

	@Test
	public void testEquals() {
		SimGrid testGrid = new SimGrid(4, 5, .1);
		testGrid.zero();
		SimGrid otherGrid = new SimGrid(4, 6, .1);
		otherGrid.zero();
		assertEquals("Equals", testGrid.checkEquals(otherGrid), false);

	}

	@Test
	public void testIntepolateCenter() {
		SimGrid testGrid = new SimGrid(4, 8, .1);
		testGrid.ones();
		double dx = testGrid.getDx();
		for (int i = 0; i < testGrid.getHeight(); ++i) {
			for (int j = 0; j < testGrid.getWidth(); ++j) {
				//System.out.println("row: " + i + " col: " + j);
				assertEquals(MathUtils.doubleEquals(testGrid.getCellCenter(i,
						j, gridTypes.TEMP), testGrid.intepolateGridCenter(
						new Point2D.Double((j + .5) * dx, (i + .5) * dx),
						gridTypes.TEMP)), true);
			}
		}
	}

}
