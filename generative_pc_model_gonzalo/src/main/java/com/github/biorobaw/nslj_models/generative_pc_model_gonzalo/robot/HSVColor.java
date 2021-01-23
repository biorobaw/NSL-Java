/**
 * 
 */
package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;

/**
 * @author gtejera
 *
 */
public class HSVColor {
	private double h,s,v;

	/**
	 * @param h double
	 * @param s double
	 * @param v double
	 */
	public HSVColor(double h, double s, double v) {
		this.h = h;
		this.s = s;
		this.v = v;		
	}

	/**
	 * @return the h
	 */
	public double getH() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * @return the s
	 */
	public double getS() {
		return s;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(double s) {
		this.s = s;
	}

	/**
	 * @return the v
	 */
	public double getV() {
		return v;
	}

	/**
	 * @param v the v to set
	 */
	public void setV(double v) {
		this.v = v;
	}

	

}
