package edu.benjones.fluid;

import java.awt.Color;

public class MathUtils {

	public static boolean doubleEquals(double n1, double n2, double tol) {
		return Math.abs(n1 - n2) < tol;
	}

	public static boolean doubleEquals(double n1, double n2) {
		return doubleEquals(n1, n2, .000001);// good enough?
	}

	public static double bilinearInterpolate(double fq11, double fq12,
			double fq21, double fq22, double dx, double xDiff, double yDiff) {
		double denom = 1.0 / (dx * dx);
		// bilinear interp:
		return (fq11 * (dx - xDiff) * (dx - yDiff) + fq21 * (xDiff)
				* (dx - yDiff) + fq12 * (dx - xDiff) * (yDiff) + fq22 * (xDiff)
				* (yDiff))
				* denom;
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
