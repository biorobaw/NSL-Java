package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class smallRobotWorldCanvas extends Canvas {
	
	
	private final int IMAGE_SIZE=80;
	BufferedImage image = new BufferedImage(5*IMAGE_SIZE, IMAGE_SIZE,BufferedImage.TYPE_3BYTE_BGR);
   
    
    public smallRobotWorldCanvas() {
    }

    public void update(Graphics g) {
		image = RobotFactory.getRobot().getPanoramica();
		g.drawImage(image, 0, 0, null);
		
    }
}
