package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
/**
 * 
 */

/**
 * @author gtejera
 *
 */
public class Range {
	private double min;
	private double max;
	
	public Range(double min, double max) {
		this.min = min;
		this.max = max;		
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

}
