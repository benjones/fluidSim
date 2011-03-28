package edu.benjones.fluid;

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
}
