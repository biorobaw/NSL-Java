package robot;
import java.awt.*;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Hashtable;


public class MostrarImagenAndFilter extends Frame implements Runnable {
	
	private BufferedImage imagen;
	HSVSlider hsvSliders = new HSVSlider();
    ContournsDetector detector = new ContournsDetector();
    MostrarImagen thresImage = new MostrarImagen();
    MostrarImagen imageWithLandM = new MostrarImagen();

	public MostrarImagenAndFilter() {
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawImage(imagen, 0, 0, this);
	}

	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
		setSize(imagen.getWidth(),imagen.getHeight());
		repaint();
	}

	public static void main(String[] args) {
		String onBoardCamera = "http://khepera:8080/?action=snapshot";
		String topCamera = "http://192.168.1.181/cgi-bin/video.jpg";
		MostrarImagenAndFilter imagen = new MostrarImagenAndFilter();
		MostrarImagenWeb imagenWeb = new MostrarImagenWeb(topCamera);
		
		while (true) {
			try {
				imagen.setImagen(imagenWeb.getImage());
				imagen.filter();
				try {Thread.sleep(500);} catch (Exception e) {e.printStackTrace();}	

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	BufferedImage getImage() {
		return imagen;
	}

	/**
	 * 
	 */
	public void clearImagen() {
		if(imagen!=null)
			setImagen(new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {


		while (true) {
			filter();
			try {Thread.sleep(500);} catch (Exception e) {e.printStackTrace();}	
		}
	}

	public void filter() {
		CvRect rectangle;
		IplImage frame, marcaImage;
		HSVRange color;
		
		color = hsvSliders.getHSVRange();
        frame = ImageCanvas.bufferedImage2IplImage(imagen);
        rectangle = detector.findBiggestShape(frame,color, ContournsDetector.NO_BORDER_SEPARATION, ContournsDetector.NO_PORCENTAGE);
    	marcaImage = ImageCanvas.bufferedImage2IplImage(imagen).clone();
        
        if (rectangle!=null) {
        	detector.drawRectangle(marcaImage, rectangle, Color.RED);
        	//System.err.println("MIAF::altura: " + rectangle.height() + ". Area: " + rectangle.width()*rectangle.height()+".");
        }
        imageWithLandM.setImagen(marcaImage.getBufferedImage());
        thresImage.setImagen(detector.thresholdImage(frame, color).getBufferedImage());
	}

	
}
