package edu.benjones.fluid;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.*;


public class SimGrid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1292714532513562448L;
	private double[] pressure;
	private double[] u;
	private double[] v;
	private int width, height;

	public SimGrid(int width, int height) {
		this.width = width;
		this.height = height;
		pressure = new double[width * height];
		u = new double[(width + 1) * height];
		v = new double[width * (height + 1)];

	}

	public void zero() {
		for (int i = 0; i < width * height; ++i) {
			pressure[i] = 0;
		}
		for (int i = 0; i < (width + 1) * height; ++i)
			u[i] = 0;
		for (int j = 0; j < width * (height + 1); ++j)
			v[j] = 0;
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

	public void draw(Graphics2D g2){
		g2.setColor(new Color(1,0,0));
		Point2D.Double p1 = new Point2D.Double(.1, .2);
		Point2D.Double p2 = new Point2D.Double(.5, .6);
		g2.draw(new Line2D.Double(p1, p2));		
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
}
