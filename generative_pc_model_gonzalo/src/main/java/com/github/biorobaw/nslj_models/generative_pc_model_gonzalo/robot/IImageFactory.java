package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
import java.awt.image.BufferedImage;


/**
 * 
 */

/**
 * @author gtejera
 *
 */
public interface IImageFactory {
	public BufferedImage getImage();
	public void resetImage();
	public BufferedImage getSyncImage();	
	public boolean isConnected();
	public void connect();
	public void disconnect();
}
