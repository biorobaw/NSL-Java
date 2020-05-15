package model;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class GridCell {
	private static final double HD_0 = 0; //angulo en radianes para la direccion preferida 0
	private static final double HD_120 = 120*Math.PI/180.0; //idem direccion preferida 120
	private static final double HD_240 = 240*Math.PI/180.0; //idem direccion preferida 240
	private static final double HD_60 = 60*Math.PI/180.0; //idem direccion preferida
	private static final double HD_180 = 180*Math.PI/180.0; //idem direccion preferida
	private static final double HD_300 = 300*Math.PI/180.0; //idem direccion preferida 
	public static final double[] DEFAULT_PREFERED_DIRECTIONS = {HD_0, HD_120, HD_240};
	private static final int CANT_HD_CELL = 3;
	
	private static final double ACTIVATION_BIAS = 1.8;
	public static double  DEFAULT_F_SOMA = 6.42;
	public static final double DEFAULT_A = 0;
	public static final double DEFAULT_B = 0; //Math.PI;

	HDGridCell[] hdGridCells = new HDGridCell[CANT_HD_CELL];

	public GridCell() {
		this(DEFAULT_F_SOMA, DEFAULT_PREFERED_DIRECTIONS, DEFAULT_A, DEFAULT_B);
	}
	
	public GridCell(double fSoma, double []preferedDirections, double A,double B) {
		hdGridCells[0] = new HDGridCell(fSoma, preferedDirections[0], B);
		hdGridCells[1] = new HDGridCell(fSoma, preferedDirections[1], Math.sqrt(3)*A/2-B/2);
		hdGridCells[2] = new HDGridCell(fSoma, preferedDirections[2], -Math.sqrt(3)*A/2-B/2);
	}

	// ejecuta un desplazamiento del animal y retorna el potencial de accion
	double doStep(double headDirection, double speed) {
		double result=1;
		for (int i=0;i<CANT_HD_CELL;i++)
			result=result*hdGridCells[i].doStep(headDirection, speed);
		return result;
	}
	
	public static void main(String [ ] args) {
		PrintWriter pw = null;
		Vector <Point2D.Double> points = null; 
		try {			
			points = LoadDataPointsFile.getDataPoints("coordenadasPredefinidasRataHasselmo.csv");
			//points = LoadDataPointsFile.getDataPoints("AWpoint.point");
			//points = HasselmoRandomExploration.getDataPoints(70000);
			//pw = new PrintWriter(new FileWriter("activation-"+System.currentTimeMillis(), APPEND));
			pw = new PrintWriter(new FileWriter("activationLog.data", false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Point2D.Double actualPosition;
		Point2D.Double lastPosition = points.get(0);
		int actPoints=0;
		double headDirection, speed, activationGrid0,activationGrid1, deltaX, deltaY;
		GridCell[] gridCells = new GridCell[2];
		gridCells[0] = new GridCell(DEFAULT_F_SOMA, DEFAULT_PREFERED_DIRECTIONS, 0, Math.PI);
		gridCells[1] = new GridCell(DEFAULT_F_SOMA, DEFAULT_PREFERED_DIRECTIONS, DEFAULT_A, DEFAULT_B);
		
		for (int iterMoves=0; iterMoves<points.size();iterMoves++) {
			actualPosition = points.elementAt(iterMoves);
			deltaX = actualPosition.x-lastPosition.x;
			deltaY = actualPosition.y-lastPosition.y;
			
			headDirection = Math.atan2(deltaY/HDGridCell.DELTA_T, deltaX/HDGridCell.DELTA_T);
			speed = Math.sqrt(Math.pow(deltaX/HDGridCell.DELTA_T,2.0)+Math.pow(deltaY/HDGridCell.DELTA_T,2.0));
			activationGrid0=gridCells[0].doStep(headDirection, speed);
			activationGrid1=gridCells[1].doStep(headDirection, speed);
			activationGrid0=activationGrid0>ACTIVATION_BIAS?1:0;
			activationGrid1=activationGrid1>ACTIVATION_BIAS?1:0;
			pw.print(actualPosition.getX()+ "\t" + actualPosition.getY()+ "\t" + activationGrid0+ "\t" + activationGrid1+ "\n");  // palabra clave y cantidad de nodos			
			lastPosition = actualPosition;
		}
		pw.close();
	}
}