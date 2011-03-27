package edu.benjones.fluid;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.*;

import javax.swing.*;


public class SimGrid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1292714532513562448L;
	
	//These are row major, starting from bottom left
	//across the bottom row,
	//then across the second row from the bottom, left to right, etc
	private double[] pressure;
	private double[] u;
	private double[] v;
	private int width, height;
	private final double arrowScale = .01;

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

	public void ones() {
		for (int i = 0; i < width * height; ++i) {
			pressure[i] = 0;
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

	public void draw(Graphics2D g2){
		drawGrid(g2);	
		drawVelocities(g2);
	}

	private void drawGrid(Graphics2D g2) {
		//draw the grid as black lines first
		g2.setColor(Color.BLACK);
		//vertical lines first
		double xStep = 1.0/(width +2);
		double yStep = 1.0/(height + 2);
		Point2D.Double p1, p2;
		p1 = new Point2D.Double(xStep, yStep);
		p2 = new Point2D.Double(xStep, yStep*(height +1));
		Line2D.Double line = new Line2D.Double(p1, p2);
		g2.draw(line);
		for(int i = 0; i < width; ++i){
			line.x1 += xStep;
			line.x2 += xStep;
			g2.draw(line);
		}
		line.y1 = yStep;
		line.y2 = yStep;
		line.x1 = xStep;
		line.x2 = xStep*(width+1);
		g2.draw(line);
		for(int i = 0; i < height; ++i)
		{
			line.y1 += yStep;
			line.y2 += yStep;
			g2.draw(line);
		}
	}
	
	private void drawArrow(Graphics2D g2, Point2D.Double position, Point2D.Double direction){
		AffineTransform trans = new AffineTransform();
		double mag = Math.sqrt(direction.x*direction.x + direction.y*direction.y);
		
		
		trans.translate(position.x, position.y);
		trans.rotate(direction.x, direction.y);
		trans.translate(mag*arrowScale, 0.0);
		trans.scale(arrowScale, arrowScale);
		Path2D.Double tri = new Path2D.Double();
		tri.moveTo(0, -.5);
		tri.lineTo(1, 0);
		tri.lineTo(0, .5);
		tri.closePath();
		tri.transform(trans);
		g2.draw(new Line2D.Double(position, 
				new Point2D.Double(position.x + direction.x*arrowScale, 
								   position.y + direction.y*arrowScale)));
		g2.fill(tri);
		
	}
	
	private void drawVelocities(Graphics2D g2){
		Point2D.Double pos = new Point2D.Double();
		Point2D.Double dir = new Point2D.Double();
		double xStep = 1.0/(width +2);
		double yStep = 1.0/(height + 2);
		dir.x = 0;
		for(int i = 0; i < width; ++i){
			for(int j = 0; j <= height; ++j){
				pos.x = (i + 1.5)*xStep;
				pos.y = (j+1)*yStep;
				dir.y = v[j*width + i];
				drawArrow(g2, pos, dir);
			}
		}
		dir.y = 0;
		for(int i = 0; i <= width; ++i){
			for(int j = 0; j < height; ++j){
				pos.x = (i + 1)*xStep;
				pos.y = (1.5+j)*yStep;
				dir.x = u[j*(width+1) + i];
				drawArrow(g2, pos, dir);
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
}
