package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
import java.io.IOException;

public class GiroAvanzo extends KheperaClient {
	private static final int TIME_OUT = 300;

	private static final int WAIT_TIME = 2000;

	private static final int MAX_VEL = 48000;

	private static final int MAX_IR_READ = 4000;

	private static final int CANT_IRS = 11;

	private static final double PULSOS_POR_VUELTA = 2764;

	// muy grande se queda corto y muy chico se pasa
	private static final double DIAMETRO_RUEDA = 128;
	private static final double SEPARACION_RUEDAS = 87; //87.5;
	private static final double PERIMETRO_VUELTA = SEPARACION_RUEDAS * Math.PI;
	private static final double PERIMETRO_RUEDA = DIAMETRO_RUEDA * Math.PI;
	private static final double PULSOS_GRADO = (PULSOS_POR_VUELTA * PERIMETRO_VUELTA)
			/ (360.0 * DIAMETRO_RUEDA);
	//private static final double MM_PER_PULSE = 0.047; // By K-Team
	//private static final double WHEEL_DISTANCE  = 88.41; //mm // By K-Team

	private static final int INC_POS = 2000;

	private static final double UMBRAL_ERROR_ENCODER = 10;

	private static final double UMBRAL_ERROR_SPEED = 20;

	private static double posIzqActual = INITIAL_POSITION, posDerActual = INITIAL_POSITION;

	private boolean reached;

	public GiroAvanzo() {
		super();
	}

	public void avanzar(double milimetros) throws IOException {
		double incremento = PULSOS_POR_VUELTA * milimetros / PERIMETRO_RUEDA;
		//double incremento = MM_PER_PULSE * milimetros; // By K-Team
		posIzqActual = posIzqActual + incremento;
		posDerActual = posDerActual + incremento;
		setPosition((int)posIzqActual, (int)posDerActual);
		
		if (synchroMoves)
			wait2ReachSpeed();
	}

	public void girarAngulo(int grados) throws IOException {
		double pasosGirar = grados * PULSOS_GRADO;
		//double pasosGirar = grados * 360.0*Math.PI*WHEEL_DISTANCE/MM_PER_PULSE; // By K-Team
		posIzqActual = posIzqActual - pasosGirar;
		posDerActual = posDerActual + pasosGirar;
		//System.out.println("setPosition::("+posIzqActual+" ," + posDerActual+").");
		setPosition((int)posIzqActual, (int)posDerActual);
		if (synchroMoves)
			wait2ReachSpeed();
	}
	
	/**
	 * 
	 */
	private void wait2ReachPos() {
		boolean reach = false;
		double toLeft, toRight;
		int[] actualPos;
		
		while (!reach) {
			try {
				actualPos = getPosition();
				// los Math.abs sobre las posiciones es porque le dlife está devolviendo posiciones sin signo :(
				toLeft = Math.abs(Math.abs(posIzqActual)-Math.abs(actualPos[0]));
				toRight = Math.abs(Math.abs(posDerActual)-Math.abs(actualPos[1]));
				reach=(toLeft<=UMBRAL_ERROR_ENCODER)&&(toRight<=UMBRAL_ERROR_ENCODER);
				//System.out.println("GiroAvanzo::trying to reach position::max error: "+Math.max(toLeft, toRight));
				System.out.println("GiroAvanzo::trying to reach position: "+posIzqActual+", "+posDerActual+" current: "+ actualPos[0]+ ", "+actualPos[1]);
				reached = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				reach=true;
				reached=false;
			}
		}
	}

	/**
	 * 
	 */
	private void wait2ReachSpeed() {
		boolean reach = false;
		double toLeft, toRight;
		long[] actualVel;
		
		while (!reach) {
			actualVel = getSpeed();
			if (actualVel!=null) {
				// los Math.abs sobre las posiciones es porque le dlife está devolviendo posiciones sin signo :(
				toLeft = Math.abs(actualVel[0]);
				toRight = Math.abs(actualVel[1]);
				reach=(toLeft<=UMBRAL_ERROR_SPEED)&&(toRight<=UMBRAL_ERROR_SPEED);
				System.out.println("GiroAvanzo::trying to reach speed zero, max speed: "+Math.max(toLeft, toRight));
				reached = true;
			} else {
				// TODO Auto-generated catch block
				reach=true;
				reached=false;
			}
		}
	}

	boolean synchroMoves = false;
	

	/**
	 * @param synchroMoves the synchroMoves to set
	 */
	public void setSynchroMoves(boolean synchroMoves) {
		this.synchroMoves = synchroMoves;
	}

	public static void main(String[] args) {
		lrfReadings();
		//ussReadings();
	}
	
	static void ussReadings() {
		// TODO Auto-generated method stub
		long timeIni =System.currentTimeMillis();
		GiroAvanzo robot = new GiroAvanzo();
		robot.connect();
		int MAX_REPS=4000;
		int iterReps=0;
		robot.setSynchroMoves(false);
        long[] uss;
		try {
			while (iterReps<MAX_REPS) {
				uss = robot.getUSs();
				System.out.print("("+iterReps+") Time(s): "+(System.currentTimeMillis()-timeIni)/1000+". ");
				if (uss == null)
					System.out.println("null uss");
				else
					System.out.println(""+uss[0]+", "+uss[1]+", "+uss[2]+", "+uss[3]+", "+uss[4]+".");
				Thread.sleep(3000);
				iterReps++;
			}
			robot.quit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void lrfReadings() {
		// TODO Auto-generated method stub
		long timeIni =System.currentTimeMillis();
		GiroAvanzo robot = new GiroAvanzo();
		robot.connect();
		int MAX_REPS=4000;
		int iterReps=0;
		robot.setSynchroMoves(false);
        long[] lrf;
		try {
			while (iterReps<MAX_REPS) {
				lrf = robot.getLRF();
				System.out.print("("+iterReps+") Time(s): "+(System.currentTimeMillis()-timeIni)/1000+". ");
				if (lrf == null)
					System.out.println("null lrf");
				else
					System.out.println(""+lrf[0]+", "+lrf[1]+", "+lrf[2]+", "+lrf[3]+", "+lrf[4]+", "+lrf[5]+", "+lrf[6]+".");
				Thread.sleep(3000);
				iterReps++;
			}
			robot.quit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	static void pruebaGiros() {
		// TODO Auto-generated method stub
		long timeIni =System.currentTimeMillis();
		GiroAvanzo robot = new GiroAvanzo();
		robot.connect();
		int MAX_REPS=4000;
		int iterReps=0;
		long DELAY_CMD = 0;
		//robot.setSpeedProfile(10000,32);
		robot.setSynchroMoves(false);
		
		int GIRO = 400;
		
		try {

			System.out.println(" casi giro...");
			Thread.sleep(4000);

			System.out.println("giro...");

			robot.setPosition((int)INITIAL_POSITION+GIRO,(int) INITIAL_POSITION-GIRO);
			Thread.sleep(10000);

//			robot.resetPosition(1000, 1000);
//			Thread.sleep(3000);
//			robot.resetPosition(1000, 1000);
//
//			System.out.println("giro...");
//			robot.setPosition(600, 1400);
//			Thread.sleep(3000);
//			System.out.println("vuelvo...");
//
//			robot.setPosition(1400, 600);
//			Thread.sleep(1000);

			int signo=1;
			while (iterReps<MAX_REPS) {
			robot.girarAngulo(signo*90);
			Thread.sleep(DELAY_CMD);
			signo=-1*signo;
			robot.girarAngulo(signo*90);
			Thread.sleep(DELAY_CMD);
			//System.out.println("("+iterReps+") Time(s): "+(System.currentTimeMillis()-timeIni)/1000+". "+robot.getBattery());
			System.out.println("("+iterReps+") Time(s): "+(System.currentTimeMillis()-timeIni)/1000+".");
			iterReps++;	iterReps++;

			}
			robot.quit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @return the reached
	 */
	public boolean isReached() {
		return reached;
	}
}
