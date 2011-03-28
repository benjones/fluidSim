package edu.benjones.fluid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathUtilsTest {
	
	@Test
	public void testDoubleEquals(){
		assertEquals(MathUtils.doubleEquals(.5, 1.0/2.0), true);
		assertEquals(MathUtils.doubleEquals(.5, 1.0/3.0), false);
		assertEquals(MathUtils.doubleEquals(.5, 3.0/6.0), true);
		assertEquals(MathUtils.doubleEquals(3.0/9.0, 4.0/12.0), true);
	}
	
	// a ones grid should interp stuff to the same value
	@Test
	public void testBilinearInterp() {
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(1, 1, 1, 1, 1, .5, .5), 1), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(0, 0, 0, 0, 1, .5, .5), 0), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(1, 1, 0, 0, 1, .5, .5), .5), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(100, 1, 1, 1, 1, 0, 0), 100), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(1, 1, 1, 100, 1, 1, 1), 100), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(1, 1, 1, 1, 1, .5, .5), 1), true);
		assertEquals(MathUtils.doubleEquals(
				MathUtils.bilinearInterpolate(0, 1, 1, 1, 1, .5, .5), .75), true);
	}
}
