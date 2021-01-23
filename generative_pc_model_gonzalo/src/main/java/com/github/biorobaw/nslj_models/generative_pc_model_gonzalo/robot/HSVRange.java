package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
/**
 * 
 */

/**
 * @author gtejera
 *
 */
public class HSVRange {
	private Range hue, saturation, value;
	
	public HSVRange(Range hue, Range saturation, Range value) {
		this.hue = hue;
		this.saturation = saturation;		
		this.value = value;		
	}

	/**
	 * @return the hue
	 */
	public Range getHue() {
		return hue;
	}

	/**
	 * @param hue the hue to set
	 */
	public void setHue(Range hue) {
		this.hue = hue;
	}

	/**
	 * @return the saturation
	 */
	public Range getSaturation() {
		return saturation;
	}

	/**
	 * @param saturation the saturation to set
	 */
	public void setSaturation(Range saturation) {
		this.saturation = saturation;
	}

	/**
	 * @return the value
	 */
	public Range getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Range value) {
		this.value = value;
	}
	
}
