package model;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class LoadDataPointsFile {
	private static final String FILE_NAME = "coordenadasPredefinidasRataHasselmo.csv";

	public static Vector<Point2D.Double> getDataPoints() throws IOException {
		return getDataPoints(FILE_NAME);
	}
	
	public static Vector<Point2D.Double> getDataPoints(String filename) throws IOException {
		Vector<Point2D.Double> result = new Vector<Point2D.Double>();
		File file = new File(filename);
		//System.out.println(new File("").toURI());
		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;
		String[] strArray;
		Point2D.Double point;
		int iterPoints = 0, iterLines = 0;
		double x, y;

		System.out.print("Leyendo puntos del archivo " + FILE_NAME + "...");
		while ((line = bufRdr.readLine()) != null) {
			strArray = line.split(",");
			try {
				//x = (java.lang.Double.parseDouble(strArray[0]) + 100) / 200;
				//y = (java.lang.Double.parseDouble(strArray[1]) + 100) / 200;
				x = java.lang.Double.parseDouble(strArray[0]);
				y = java.lang.Double.parseDouble(strArray[1]);
				point = new Point2D.Double(x, y);
				result.add(point);
				iterPoints++;
			} catch (NumberFormatException exception) {
			}
			iterLines++;
		}
		System.out.println(" puntos leidos " + iterPoints + "/" + iterLines);
		return result;
	}
}
