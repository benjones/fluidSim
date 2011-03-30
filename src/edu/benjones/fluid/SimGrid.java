package edu.benjones.fluid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SimGrid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1292714532513562448L;

	// These are row major, starting from bottom left
	// across the bottom row,
	// then across the second row from the bottom, left to right, etc
	private double[] pressure;
	private double[] u, uTemp;
	private double[] v, vTemp;
	private double[] T, TTemp;// temperature and advected temp
	// also stored at cell centers
	private int width, height;
	double dx;// grid spacing
	private final double arrowScale = .01;

	private Color lowBlend, highBlend;

	private double xStep;

	private double yStep;

	private enum gridTypes {
		TEMP, PRESSURE
	};

	public SimGrid(int width, int height, double dx) {
		this.width = width;
		this.height = height;
		this.dx = dx;
		pressure = new double[width * height];
		u = new double[(width + 1) * height];
		uTemp = new double[(width + 1) * height];
		v = new double[width * (height + 1)];
		vTemp = new double[width * (height + 1)];
		T = new double[width * height];
		TTemp = new double[width * height];

		lowBlend = new Color(0.0f, 0.0f, 1.0f);
		highBlend = new Color(1.0f, 0.0f, 0.0f);
		xStep = 1.0 / (width + 2);
		yStep = 1.0 / (height + 2);

	}

	public void zero() {
		for (int i = 0; i < width * height; ++i) {
			pressure[i] = T[i] = TTemp[i] = 0;
		}
		for (int i = 0; i < (width + 1) * height; ++i)
			u[i] = 0;
		for (int j = 0; j < width * (height + 1); ++j)
			v[j] = 0;
	}

	public void ones() {
		for (int i = 0; i < width * height; ++i) {
			pressure[i] = 1;
			T[i] = i * .1;
		}
		for (int i = 0; i < (width + 1) * height; ++i)
			u[i] = 1;
		for (int j = 0; j < width * (height + 1); ++j)
			v[j] = 1;

	}

	public static SimGrid loadFromFile(String filename) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		SimGrid ret;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			ret = (SimGrid) in.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = null;
		}

		return ret;
	}

	public void writeToFile(String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void draw(Graphics2D g2) {
		drawScalarField(g2, gridTypes.TEMP);
		drawGrid(g2);
		drawVelocities(g2);

	}

	private void drawGrid(Graphics2D g2) {
		// draw the grid as black lines first
		g2.setColor(Color.BLACK);
		// vertical lines first

		Point2D.Double p1, p2;
		p1 = new Point2D.Double(xStep, yStep);
		p2 = new Point2D.Double(xStep, yStep * (height + 1));
		Line2D.Double line = new Line2D.Double(p1, p2);
		g2.draw(line);
		for (int i = 0; i < width; ++i) {
			line.x1 += xStep;
			line.x2 += xStep;
			g2.draw(line);
		}
		line.y1 = yStep;
		line.y2 = yStep;
		line.x1 = xStep;
		line.x2 = xStep * (width + 1);
		g2.draw(line);
		for (int i = 0; i < height; ++i) {
			line.y1 += yStep;
			line.y2 += yStep;
			g2.draw(line);
		}
	}

	private void drawArrow(Graphics2D g2, Point2D.Double position,
			Point2D.Double direction) {
		AffineTransform trans = new AffineTransform();
		double mag = Math.sqrt(direction.x * direction.x + direction.y
				* direction.y);

		trans.translate(position.x, position.y);
		trans.rotate(direction.x, direction.y);
		trans.translate(mag * arrowScale, 0.0);
		trans.scale(arrowScale, arrowScale);
		Path2D.Double tri = new Path2D.Double();
		tri.moveTo(0, -.5);
		tri.lineTo(1, 0);
		tri.lineTo(0, .5);
		tri.closePath();
		tri.transform(trans);
		g2.draw(new Line2D.Double(position, new Point2D.Double(position.x
				+ direction.x * arrowScale, position.y + direction.y
				* arrowScale)));
		g2.fill(tri);

	}

	private void drawVelocities(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		Point2D.Double pos = new Point2D.Double();
		Point2D.Double dir = new Point2D.Double();
		dir.x = 0;
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j <= height; ++j) {
				pos.x = (i + 1.5) * xStep;
				pos.y = (j + 1) * yStep;
				dir.y = v[j * width + i];
				drawArrow(g2, pos, dir);
			}
		}
		dir.y = 0;
		for (int i = 0; i <= width; ++i) {
			for (int j = 0; j < height; ++j) {
				pos.x = (i + 1) * xStep;
				pos.y = (1.5 + j) * yStep;
				dir.x = u[j * (width + 1) + i];
				drawArrow(g2, pos, dir);
			}
		}
	}

	private void drawScalarField(Graphics2D g2, gridTypes gridType) {
		float minScalar, maxScalar;
		double[] arr;
		switch (gridType) {
		case TEMP:
			minScalar = 0;
			maxScalar = 5;
			arr = T;
			break;
		case PRESSURE:
			minScalar = 0;
			maxScalar = 1;
			arr = pressure;
			break;
		default:
			minScalar = 0;
			maxScalar = 1;
			arr = null;
		}
		Rectangle2D.Double square = new Rectangle2D.Double(0, 0, xStep, yStep);
		for (int i = 0; i < height; ++i) {
			square.y = yStep * (i + 1);
			for (int j = 0; j < width; ++j) {
				square.x = xStep * (j + 1);
				g2.setColor(MathUtils.blendColors(lowBlend, highBlend,
						MathUtils.scaleRange(minScalar, maxScalar,
								(float) arr[j + i * width])));
				g2.fill(square);
			}
		}

	}

	public boolean checkEquals(SimGrid other) {
		if (width == other.width && height == other.height
				&& u.length == other.u.length && v.length == other.v.length
				&& pressure.length == other.pressure.length) {
			for (int i = 0; i < u.length; ++i)
				if (u[i] != other.u[i])
					return false;
			for (int i = 0; i < v.length; ++i)
				if (v[i] != other.v[i])
					return false;
			for (int i = 0; i < pressure.length; ++i)
				if (pressure[i] != other.pressure[i])
					return false;
			return true;
		} else
			return false;
	}

	public void advect(double dt) {
		// semi lagrangian advection
		// q_grid^n+1 = interpolate(q^n, x_p) wher x_p is where the 'particle'
		// advected to the
		// grid point started at the beginning of the timestep
		Point2D.Double xP = new Point2D.Double();
		Point2D.Double xMid = new Point2D.Double();
		for (int i = 0; i < width * height; ++i) {

		}

	}

	private double intepolateGridCenter(Point2D.Double pos, gridTypes gridType) {

		int xGrid = Math.max((int) Math.floor(pos.x / dx), 0);
		int yGrid = Math.max((int) Math.floor(pos.y / dx), 0);
		int xPGrid = Math.min(xGrid + 1, width - 1);
		int yPGrid = Math.min(yGrid + 1, height - 1);

		double xDiff = pos.x - xGrid * dx;
		double yDiff = pos.y - yGrid * dx;

		double[] arr;
		switch (gridType) {
		case TEMP:
			arr = T;
			break;
		case PRESSURE:
			arr = pressure;
			break;
		default:
			arr = null;
		}
		double fq11, fq21, fq12, fq22;
		fq11 = arr[xGrid + yGrid * width];
		fq12 = arr[xGrid + yPGrid * width];
		fq21 = arr[xPGrid + yGrid * width];
		fq22 = arr[xPGrid + yPGrid * width];

		return MathUtils.bilinearInterpolate(fq11, fq12, fq21, fq22, dx, xDiff,
				yDiff);

	}

	public Color getLowBlend() {
		return lowBlend;
	}

	public void setLowBlend(Color lowBlend) {
		this.lowBlend = lowBlend;
	}

	public Color getHighBlend() {
		return highBlend;
	}

	public void setHighBlend(Color highBlend) {
		this.highBlend = highBlend;
	}

}
