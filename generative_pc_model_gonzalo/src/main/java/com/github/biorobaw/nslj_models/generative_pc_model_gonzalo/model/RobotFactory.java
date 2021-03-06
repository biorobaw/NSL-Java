package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot.IRobot;


public class RobotFactory {
	private static IRobot robot=null;
    private static final String robotClassName = Configuration.getString("Reflexion.Robot");

  public static IRobot getRobot() {
	if(robot==null) {

		// Reflexion para levantar la clase aprendizaje desde archivo de configuracion
		Class[] types = new Class[] {};
		Constructor cons = null;
		try {
			cons = Class.forName(robotClassName).getConstructor(types);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[] args = new Object[] {}; // constructor sin argumentos
		try {
			robot = (IRobot) cons.newInstance(args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

    return robot;
  }
  
  
}
