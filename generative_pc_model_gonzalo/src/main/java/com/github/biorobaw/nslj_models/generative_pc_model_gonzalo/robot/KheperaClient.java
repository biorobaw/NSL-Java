package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class KheperaClient {
	protected static final double INITIAL_POSITION = 0;

	public static enum SENTIDO {
		IZQ, DER
	};

	public static enum RUEDA {
		IZQ, DER
	};

	// parametros de comuncación por defecto
	//public static String IP_SERVER = "192.168.1.50";
	//public static String IP_SERVER = "192.168.1.98";
	//public static String IP_SERVER = "192.168.1.97";
	public static String IP_SERVER = "127.0.0.1";
	//public static int PORT_SERVER = 8001;
	public static int PORT_SERVER = 10020;
	//private static int PORT_CLIENT = 7001;
	private static int PORT_CLIENT = 10020;
	private static final int LARGO_MAX_MSG = 1024;
	// private static final int TIME_OUT = 300;

	// atributos utilizados para intercambiar mensajes UDP con el servidor
	/*
	 * private static InetAddress serverIP; private static int serverPort;
	 */

	// posibles tipos de mensajes entre el servidor y el cliente
	private static final String CMD_GET_IRS = "N";
	private static final String CMD_GET_ALS = "O"; // ambient light
	private final String CMD_GET_LRF = "L"; // lectura del sensor LRF pero solo de -120 a 120, de a 45º
	private final String CMD_GET_USS ="G";
	private final String CMD_SET_VEL = "D";
	private final String CMD_GET_VEL = "E";
	private static final String CMD_SET_POS = "P";
//	private static final String CMD_SET_POS_BLOCK = "B"; // setea la posicion de
															// los encoders y
															// espera a que el
															// robot se detenga
	private static final String CMD_RESET_POS = "I";
	private static final String CMD_GET_POS = "R";
	private static final String CMD_GET_BAT = "V";
	private static final String CMD_RESET_MOT = "M";
	private static final String CMD_QUIT = "Q";
	private static final String CMD_SET_SPEED_PROFILE = "J";
	// respuestas del servidor
	private static final String STR_COMMAND_RECIVED = "COMMAND RECIVED";
	/*
	 * private final String STR_MOTORS_RE_INITIALIZED ="MOTORS RE-INITIALIZED";
	 * private final String STR_INVALID_COMMAND = "INVALID COMMAND";
	 */

	// codigo de retorno
	public static int COMMAND_RECIVED = 3;
	public static int MOTORS_RE_INITIALIZED = 2;
	public static int OTHER_OK = 1;
	public static int OTHER_ERROR = -1;
	public static int INVALID_COMMAND = -2;
	public static int IO_ERROR = -3;

	private static String SEPARATOR = ",";

	public static final int IR_SIZE = 11;
	public static final int US_SIZE = 5;
	public static final int LRF_SIZE = 7; //-120, -90, -45, 0, 45, 90, 120

	/*
	 * // constante para direccionamiento de los US sensors private final int
	 * us_left = 0; private final int us_front_left = 1; private final int
	 * us_front = 2; private final int us_front_right = 3; private final int
	 * us_right = 4;
	 */
	private static final int US_ALL = 5;
	// private static final long MAX_VEL_OPEN_LOOP =48000;
	public static final long MIN_VEL_REGULATED = 2000;
	public static final long MAX_VEL_REGULATED = 43000;
	/*
	 * private static final int WAIT_TIME = 2000; private static final int
	 * MAX_VEL = 48000; private static final int MAX_IR_READ = 4000;
	 */
	private static final int us_MINVALUE = 0;
	private static final int us_MAXVALUE = 99999999;
	/*
	 * private final float us_RANGOERR = 0.30f; // Porcentaje de error para
	 * topear el valor al calcular el Promedio
	 * 
	 * private static final double PULSOS_POR_VUELTA = 2764; private static
	 * final double DIAMETRO_RUEDA = 128.8; private static final double
	 * SEPARACION_RUEDAS = 89; //87.5; private static final double
	 * PERIMETRO_VUELTA = SEPARACION_RUEDAS * Math.PI; private static final
	 * double PERIMETRO_RUEDA = DIAMETRO_RUEDA * Math.PI;
	 * 
	 * private static final double PULSOS_GRADO = (PULSOS_POR_VUELTA *
	 * PERIMETRO_VUELTA)/(360.0*DIAMETRO_RUEDA);
	 */

	private static final double DIAMETRO_RUEDA = 42; // 41;
	private static final double PERIMETRO_RUEDA = DIAMETRO_RUEDA * Math.PI;
	private static final double SEPARACION_RUEDAS = 89.0; // 87.5;
	public static final double PERIMETRO_VUELTA = SEPARACION_RUEDAS * Math.PI;
	private static final double MOTOR_REDUCTION = 27.0;
	private static final double GEAR_REDUCTION = 1.6;
	private static final double MOTOR_WHEEL_REDUCTION = MOTOR_REDUCTION
			* GEAR_REDUCTION;
	private static final int PULSOS_POR_VUELTA_MOTOR = 16;
	private static final int ENCODER_RESOLUTION = 4;
	private static final double PULSOS_POR_VUELTA_RUEDA = PULSOS_POR_VUELTA_MOTOR
			* MOTOR_WHEEL_REDUCTION * ENCODER_RESOLUTION;
	public static final double PULSOS_POR_MM = PULSOS_POR_VUELTA_RUEDA
			/ PERIMETRO_RUEDA;

	// private int [] us_data = new int[us_all];
	private int[] us_dataAvg = new int[US_ALL];
	private boolean[] us_resetAvg = new boolean[US_ALL];
	private int[] us_dataMin = new int[US_ALL];
	private int[] us_dataMax = new int[US_ALL];

	// atributos utilizados para intercambiar mensajes TCP con el servidor
	private DataOutputStream outToServer = null;
	private BufferedReader inFromServer = null;
	// private InetAddress iaServer = null;
	private Socket clientSocket = null;
	// private DatagramSocket socket;
	// private byte[] buffer = new byte[LARGO_MAX_MSG];

//	private int posIzqActual;
//	private int posDerActual;
	private boolean connected = false;

	/**
	 * Constructor con los parámetros de comunicación por defecto
	 */
	public KheperaClient() {
		this(IP_SERVER, PORT_SERVER, PORT_CLIENT);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Constructor que recibe el IP del Khepera y el puerto donde esta
	 * ejecutandose el servidor: KheperaServer
	 */
	public KheperaClient(String IPServidor, int puertoServidor,
			int puertoCliente) {
		System.out.println("Levantando cliente en contra " + IPServidor + ":"
				+ puertoServidor + ".");
		IP_SERVER = IPServidor;
		PORT_SERVER = puertoServidor;
	}
	
	public void connect() {
		try {
			// this.iaServer = InetAddress.getByName(IPServidor);
			this.clientSocket = new Socket(IP_SERVER, PORT_SERVER);
			this.outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			this.inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			/*
			 * this.socket = new DatagramSocket(puertoCliente);
			 * this.socket.setSoTimeout(TIME_OUT);
			 */

			resetPosition((int)INITIAL_POSITION, (int)INITIAL_POSITION);
			connected  = true;
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
		
	}
	

	/**
	 * 
	 */
	public void disconnect() {
		// TODO Auto-generated method stub
		connected = false;
		try {
			outToServer.flush();
			outToServer.close();
			inFromServer.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long[] getIRs() throws IOException {
		long[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_IRS);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length == IR_SIZE))
				result = new long[IR_SIZE];
			for (int iterIRs = 0; iterIRs < IR_SIZE; iterIRs++)
				result[iterIRs] = Long.parseLong(msgArray[iterIRs]);
		} catch (IOException e) {
		}

		return result;
	}

	public long[] getALs() throws IOException {
		long[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_ALS);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length == IR_SIZE))
				result = new long[IR_SIZE];
			for (int iterIRs = 0; iterIRs < IR_SIZE; iterIRs++)
				result[iterIRs] = Long.parseLong(msgArray[iterIRs]);
		} catch (IOException e) {
		}

		return result;

	}

	public long[] getLRF() throws IOException {
		long[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_LRF);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length == LRF_SIZE))
				result = new long[LRF_SIZE];
			for (int iterLRF = 0; iterLRF < LRF_SIZE; iterLRF++)
				result[iterLRF] = Long.parseLong(msgArray[iterLRF]);
		} catch (IOException e) {
		}

		return result;

	}

	public long[] getUSs() throws IOException {
		long[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_USS);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length >= US_SIZE)) {
				result = new long[US_SIZE];
				for (int iterUSs = 0; iterUSs < US_SIZE; iterUSs++)
					result[iterUSs] = Long.parseLong(msgArray[iterUSs]);
			}
		} catch (IOException e) {
		}

		return result;
	}
	
	public int setPosition(int posIzq, int posDer) {
		int result;

		String msg;
		try {
			msg = enviarComando(CMD_SET_POS + SEPARATOR + posIzq
					+ SEPARATOR + posDer);
			if (msg.equals(STR_COMMAND_RECIVED))
				result = COMMAND_RECIVED;
			else
				result = OTHER_ERROR;
		} catch (IOException e) {
			result = IO_ERROR;
		}

		return result;
	}

	public int resetPosition(int posIzq, int posDer) {
		int result;

		String msg;
		try {
			msg = CMD_RESET_POS + SEPARATOR + posIzq + SEPARATOR + posDer;
			//System.err.println("GiroAvanzo::resetPosition msg: " + msg);
			msg = enviarComando(msg);
			if (msg.equals(STR_COMMAND_RECIVED))
				result = COMMAND_RECIVED;
			else
				result = OTHER_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			result = IO_ERROR;
		}

		return result;
	}

	public int resetMotor() {
		int result;

		String msg;
		try {
			msg = enviarComando(CMD_RESET_MOT);
			if (msg.equals(STR_COMMAND_RECIVED))
				result = COMMAND_RECIVED;
			else
				result = OTHER_ERROR;
		} catch (IOException e) {
			result = IO_ERROR;
		}

		return result;
	}

	public int[] getPosition() throws IOException {
		int[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_POS);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length == 2)) {
				result = new int[2];
				result[0] = Integer.parseInt(msgArray[0]);
				result[1] = Integer.parseInt(msgArray[1]);
			}
		} catch (IOException e) {
		}

		return result;
	}

	/*
	 * Ajusta el profile de velocidad max speed (16bits), acceleration (8bits)
	 * standard values: max speed to 20000, acc to 64
	 */
	public int setSpeedProfile(int maxSpeed, int maxAcceleration) {
		int result;

		String msg;
		try {
			msg = enviarComando(CMD_SET_SPEED_PROFILE + SEPARATOR + maxSpeed
					+ SEPARATOR + maxAcceleration);
			if (msg.equals(STR_COMMAND_RECIVED))
				result = COMMAND_RECIVED;
			else
				result = OTHER_ERROR;
		} catch (IOException e) {
			result = IO_ERROR;
		}

		return result;

	}

	public Battery getBattery() throws IOException {
		Battery result = null;

		String msg = enviarComando(CMD_GET_BAT);
		String[] msgArray = msg.split(",");
		if ((msgArray != null) && (msgArray.length == 4))
			result = new Battery(Double.parseDouble(msgArray[0]),
					Double.parseDouble(msgArray[1]),
					Double.parseDouble(msgArray[2]),
					Double.parseDouble(msgArray[3]));

		return result;
	}

	// cierra la conexion y detiene los motores
	public String quit() throws IOException {
		String result = enviarComando(CMD_QUIT); // cierra la conexion remota y
													// detiene los motores
		clientSocket.close();
		return result;
	}

	/**
	 * Envía un comando al Khepera y retorna su respuesta.
	 * 
	 * @throws IOException
	 */
	private synchronized String enviarComando(String cmd) throws IOException {
		char[] msgRespuesta = new char[LARGO_MAX_MSG];
		String data = null;
		String[] strRespuesta = null;

		if (outToServer==null) throw new IOException("Server conexion null");
		
		outToServer.write(cmd.getBytes("ascii"));
		outToServer.flush();
		inFromServer.read(msgRespuesta);

		data = new String(msgRespuesta);
		strRespuesta = data.split("\\r");

		return strRespuesta[0];
	}

	/*
	 * private String enviarBloqueante(String str) throws IOException { boolean
	 * estadoOK=false; DatagramPacket packet = new DatagramPacket(buffer,
	 * buffer.length); String received=null;
	 * 
	 * while (!estadoOK) { enviar(str); try { socket.receive(packet); received =
	 * new String(packet.getData(), "ascii"); estadoOK=true; } catch
	 * (SocketTimeoutException ex) { System.out.print("."); } } return received;
	 * }
	 * 
	 * private void enviar(String msg) throws UnsupportedEncodingException,
	 * IOException { socket.send(new DatagramPacket(msg.getBytes("ascii"),
	 * msg.getBytes("ascii").length, this.iaServer, PORT_SERVER)); }
	 */

	private void resetUSAvg() {
		for (int i = 0; i < US_ALL; resetUSAvg(i++))
			;
	}

	private void resetUSAvg(int IdSensor) {
		us_dataAvg[IdSensor] = 0;
		us_resetAvg[IdSensor] = true;
	}

	private void resetUSMin() {
		for (int i = 0; i < US_ALL; resetUSMin(i++))
			;
	}

	private void resetUSMin(int IdSensor) {
		us_dataMin[IdSensor] = us_MAXVALUE;
	}

	private void resetUSMax() {
		for (int i = 0; i < US_ALL; resetUSMax(i++))
			;
	}

	private void resetUSMax(int IdSensor) {
		us_dataMax[IdSensor] = us_MINVALUE;
	}

	public void resetUS() {
		resetUSAvg();
		resetUSMax();
		resetUSMin();
	}

	public int setSpeed(long velIzq, long velDer) {
		int result;

		String msg;
		try {
			msg = enviarComando(CMD_SET_VEL + SEPARATOR + velIzq + SEPARATOR
					+ velDer);
			if (msg.equals(STR_COMMAND_RECIVED))
				result = COMMAND_RECIVED;
			else
				result = OTHER_ERROR;
		} catch (IOException e) {
			result = IO_ERROR;
		}

		return result;
	}

	public long[] getSpeed() {
		long[] result = null;
		String msg;

		try {
			msg = enviarComando(CMD_GET_VEL);
			String[] msgArray = msg.split(",");
			if ((msgArray != null) && (msgArray.length == 2))
				result = new long[2];
			result[0] = Long.parseLong(msgArray[0]);
			result[1] = Long.parseLong(msgArray[1]);
		} catch (IOException e) {
		}

		return result;
	}
}
