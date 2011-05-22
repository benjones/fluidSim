package edu.benjones.fluid;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class simGridDisplay extends JPanel {

	private static SimGrid simGrid;
	public static void main(String[] args){
		
		simGrid = new SimGrid(5,5, .1);
		simGrid.ones();
		final double dt = .05;
		
		final JFrame frame = new JFrame("Fluid Sim");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new simGridDisplay());
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		frame.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent event){
				simGrid.advance(dt);
				frame.repaint();
			}

			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke((float)(1.0/this.getHeight())));
		//setup affineTransform, but assume that the window width/height are equal
		AffineTransform trans = new AffineTransform((double)this.getWidth(), 0,
													0, (double)-this.getHeight(),
													0, (double)this.getHeight());
		g2.setTransform(trans);
		simGrid.draw(g2);
	}
	
}
