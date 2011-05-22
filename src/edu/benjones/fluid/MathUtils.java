package edu.benjones.fluid;

import java.awt.Color;

public class MathUtils {

	public static boolean doubleEquals(double n1, double n2, double tol) {
		return Math.abs(n1 - n2) < tol;
	}

	public static boolean doubleEquals(double n1, double n2) {
		return doubleEquals(n1, n2, .000001);// good enough?
	}

	/**
	 * Bilinearly interpolates function F, which is defined at corners of a square
	 * 
	 * @param fq11 f evaluated at bot left corner of square
	 * @param fq12 f evaluated at bot right corner of square
	 * @param fq21 f evaluated at top left corner of square
	 * @param fq22 f evaluated at top right corner of square
	 * @param dx square side length
	 * @param xDiff x = x0 + xDiff (xDiff should be < dx, > 0)
	 * @param yDiff same for y
	 * @return interpolated value
	 */
	public static double bilinearInterpolate(double fq11, double fq12,
			double fq21, double fq22, double dx, double xDiff, double yDiff) {
		double denom = 1.0 / (dx * dx);
		// bilinear interp:
		return (fq11 * (dx - xDiff) * (dx - yDiff) + fq21 * (xDiff)
				* (dx - yDiff) + fq12 * (dx - xDiff) * (yDiff) + fq22 * (xDiff)
				* (yDiff))
				* denom;
	}

	/**
	 * Linearly interpolates between f1 and f2
	 * @param f1 f evaluated at point 1
	 * @param f2 f evaluated at point 2
	 * @param dx distance between point 1/2
	 * @param xDiff distance from evaluated point to point 1
	 * @return intepolated value
	 */
	public static double linearInterpolate(double f1, double f2, double dx, double xDiff){
		return f1 + (f2 - f1)*(xDiff/dx);
	}
	
	public static Color blendColors(Color c1, Color c2, float alpha) {
		return new Color(
				(int) (c1.getRed() * (1 - alpha) + c2.getRed() * alpha),
				(int) (c1.getGreen() * (1 - alpha) + c2.getGreen() * alpha),
				(int) (c1.getBlue() * (1 - alpha) + c2.getBlue() * alpha));
	}

	/**
	 * scales val to range [0,1], 0 means <= low, 1 means >= high linearly
	 * scaled in between
	 * 
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static float scaleRange(float low, float high, float val) {
		assert (low != high);
		return Math.max(0f, Math.min(1.0f, (val - low) / ((high - low))));
	}
}
