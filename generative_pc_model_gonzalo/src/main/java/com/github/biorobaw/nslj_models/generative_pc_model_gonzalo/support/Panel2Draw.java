/**
 * 
 */
package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.support;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * @author gtejera
 * 
 */
public class Panel2Draw extends JComponent {

	private static class Oval {
	    final int x1; 
	    final int y1;
	    final int x2;
	    final int y2;   
	    final Color color;

	    public Oval(int x1, int y1, int x2, int y2, Color color) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
	        this.color = color;
	    }               
	}

	private final LinkedList<Oval> ovals = new LinkedList<Oval>();
	private Color color;

	
	public void addPoint(double x1, double x2, int diam) {
		addPoint((int)(x1*getSize().height), (int)(getSize().width* x2), diam, diam);
	}

	public void addPoint(int x1, int x2, int x3, int x4) {
		ovals.add(new Oval(x1,x2,x3,x4, color));   
//		System.out.println("P2D::x1: "+ x1 + ". x2: "+ x2+ ". x3: "+ x3+ ". x4: "+ x4+ ".");
	    repaint();
	}

	public void clearLines() {
		ovals.clear();
	    repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for (Oval oval : ovals) {
	        g.setColor(oval.color);
	        g.drawOval(oval.x1, oval.y1, oval.x2, oval.y2);
	    }
//	    clearLines();
	}

	public Panel2Draw(String title, Color color) {
	    JFrame testFrame = new JFrame();
	    testFrame.setTitle(title);
	    this.color = color;
	    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setPreferredSize(new Dimension(200, 200));
	    testFrame.getContentPane().add(this, BorderLayout.CENTER);
	    JPanel buttonsPanel = new JPanel();
	    testFrame.pack();
	    testFrame.setVisible(true);
	}

}
