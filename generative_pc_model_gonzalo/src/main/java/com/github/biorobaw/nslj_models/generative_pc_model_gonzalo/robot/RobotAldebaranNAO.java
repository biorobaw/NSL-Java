package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
import java.awt.Color;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;





public class RobotAldebaranNAO implements IRobot {
	
	private native void avanzar();
	private native void girar(int angulo);
	private native int [][] getImage();

	public boolean[] affordances() {
		// TODO Auto-generated method stub
		return null;
	}

	public void doAction(int grados) {
		// TODO Auto-generated method stub

	}

	public Double getGlobalCoodinate() {
		// TODO Auto-generated method stub
		return null;
	}

	public BufferedImage getPanoramica() {
		// TODO Auto-generated method stub
		return null;
	}

	public void startRobot() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
	    new RobotAldebaranNAO().avanzar();
	}

	// cargo la biblioteca del sistema libNAO.so
	static {
	    System.loadLibrary("NAO");
	}

	@Override
	public boolean findFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getGlobalDirection() {
		// TODO Auto-generated method stub
		return 0;
	}
	/* (non-Javadoc)
	 * @see khepera.IRobot#findLandmarks()
	 */
	@Override
	public Double[] findLandmarks() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see robot.IRobot#getColorsLandmarks()
	 */
	@Override
	public Color[] getColorsLandmarks() {
		// TODO Auto-generated method stub
		return null;
	}


}
