/**
 * 
 */
package robot;

import java.awt.Color;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * @author gtejera
 *
 */
public class DohyoTracker {
    ContournsDetector detector = new ContournsDetector();
    CvRect rectangle;
	private static final double MIN_RECT_AREA = 10;

	MostrarImagenWeb imagenWeb = new MostrarImagenWeb("http://192.168.1.181/cgi-bin/video.jpg");
	HSVRange color2tack = new HSVRange(new Range(65, 95), new Range(55, 255), new Range(100, 256));
    Double dohyoCenterPX = new Double(197,121);
    int radioDohyoPX = 115; // 115 sin que vea las pareces, radio real en PX
    int radioParedPX = 10;
    int radioDohyoMM = 145*10/2; // diametro 145 cm

    int colorDescarte = ImageProcessing.color2RGB(Color.RED.brighter());
    
	public DohyoTracker() {
    }
    
    private void applyMask(BufferedImage image) {
		for (int iterH=0; iterH<image.getHeight();iterH++)
			for (int iterW=0; iterW<image.getWidth();iterW++)
				if (dohyoCenterPX.distance(new Double(iterW,iterH))>(radioDohyoPX+radioParedPX))
					image.setRGB(iterW,iterH, colorDescarte);
    }
    
    private final double ROTATE_EJE = Math.toRadians(-135);
    
    public Double getPosition() {
        IplImage frame;
        Double result = new Double(0,0);Double roted = new Double(0,0);
        BufferedImage image; 
		try {
			image = imagenWeb.getStableImage();
			applyMask(image);
			frame = ImageCanvas.bufferedImage2IplImage(image);
	        rectangle = detector.findBiggestShape(frame, color2tack, ContournsDetector.NO_BORDER_SEPARATION, 50);
	        if (rectangle!=null) {
	        	if (area(rectangle)<MIN_RECT_AREA)
	        		detector.drawRectangle(frame, rectangle, Color.RED);
	         	else {
	        		detector.drawRectangle(frame, rectangle, Color.BLUE);
	        		roted.x =(double)rectangle.x()+rectangle.width()/2.0;
	        		roted.y =(double)rectangle.y()+rectangle.height()/2.0;
//	        		result.x= 100*(result.x - dohyoCenterPX.x + (double)radioDohyoPX)/(2.0*(double)radioDohyoPX);
//	        		result.y= 100*(result.y -dohyoCenterPX.y + (double)radioDohyoPX)/(2.0*(double)radioDohyoPX);
	        		roted.x= (roted.x - dohyoCenterPX.x + (double)radioDohyoPX)/(2.0*(double)radioDohyoPX)-0.5; // [-0.5, 0.5]
	        		roted.y= (roted.y -dohyoCenterPX.y + (double)radioDohyoPX)/(2.0*(double)radioDohyoPX)-0.5;
	         	}
	        }
	        imagenWeb.setImage(frame.getBufferedImage());
//	        result.y = roted.x * Math.cos(ROTATE_EJE)-roted.y * Math.sin(ROTATE_EJE);
//	        result.x = roted.x * Math.sin(ROTATE_EJE)+roted.y * Math.cos(ROTATE_EJE);
	        result.x = roted.x;
	        result.y = -roted.y;
			System.err.println("DT::center: " + result.x + ", " + result.y);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
    }
    
	/**
	 * @param rectangle
	 * @return
	 */
	private double area(CvRect rectangle) {
		return rectangle.width()*rectangle.height();
	}
	
	public static void main(String[] args) {
		DohyoTracker dohyoTracker = new DohyoTracker();
		Double point;
		
		while (true) {
			try {
				point = dohyoTracker.getPosition();
				try {Thread.sleep(500);} catch (Exception e) {e.printStackTrace();}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
}
