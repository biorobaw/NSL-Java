package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.robot;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public abstract class ImageCanvas implements IImageFactory {
	private boolean connected = true;
    private CanvasFrame canvasFrame;
    protected BufferedImage frame;
    protected final Object lock = new Object();
    
    protected ContournsDetector detector = new ContournsDetector();

    public ImageCanvas(String canvasName) {
    	canvasFrame = new CanvasFrame(canvasName);
    	canvasFrame.getCanvas().addMouseListener(new MyMouseListener(this));
    	
    }

	void showImage() {
		if (frame!=null) {
			IplImage cvImage = bufferedImage2IplImage(frame);
			Hashtable<Color, CvRect> rectangles = detector.findRectangles(cvImage);
		    detector.drawRectangles(cvImage, rectangles);
		    canvasFrame.setCanvasSize(cvImage.width(), cvImage.height());
		    canvasFrame.showImage(frame);
		}
	}

	/* (non-Javadoc)
	 * @see IImageFactory#getSyncImage()
	 */
	@Override
	public BufferedImage getSyncImage() {
		resetImage();
		try {
			synchronized (lock) {
			lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return frame;
	}
	
    public boolean isConnected() {
		return connected;
	}
	
	static public IplImage bufferedImage2IplImage(BufferedImage frame) {
		IplImage result = IplImage.create(frame.getWidth(), frame.getHeight(), IPL_DEPTH_8U, 3);

		result.copyFrom(frame);
//		for (int i=0; i<frame.getWidth(); i++)
//			for (int j=0; j<frame.getHeight(); j++)
//				result.
			
		return result;
	}


}

class MyMouseListener implements MouseListener {
	ImageCanvas canvas;
	Vector <CvScalar> hsvPoints = new Vector<CvScalar>();
	
	public MyMouseListener(ImageCanvas canvas){
		super();
		this.canvas = canvas;
	}
	
	public void mousePressed(MouseEvent e) {
		//IplImage timg = IplImage.createFrom(canvas.frame);
		IplImage timg = ImageCanvas.bufferedImage2IplImage(canvas.frame);
		
		if (timg == null)
			System.out.println("ERROR createFrom");
		CvScalar hsv;
		cvCvtColor(timg, timg, CV_BGR2HSV);

		hsv = cvGet2D(timg, e.getY(), e.getX());
		hsvPoints.add(hsv);
	}

	/**
	 * @param e MouseEvent
	 */

	public void mouseReleased(MouseEvent e) {
		double hueSum=0, satSum=0, valSum=0, hueDE=0, satDE=0, valDE=0;
		int iterPoint;
		
		for (iterPoint=0;iterPoint<hsvPoints.size();iterPoint++) {
			hueSum = hueSum + hsvPoints.elementAt(iterPoint).getVal(0);
			satSum = satSum + hsvPoints.elementAt(iterPoint).getVal(1);
			valSum = valSum + hsvPoints.elementAt(iterPoint).getVal(2);
		}
		
		double hueProm = hueSum/hsvPoints.size(), satProm = satSum/hsvPoints.size(), valProm = valSum/hsvPoints.size();
		
		for (iterPoint=0;iterPoint<hsvPoints.size();iterPoint++) {
			hueDE=hueDE+Math.pow(hsvPoints.elementAt(iterPoint).getVal(0)-hueProm, 2);
			satDE=satDE+Math.pow(hsvPoints.elementAt(iterPoint).getVal(1)-satProm, 2);
			valDE=valDE+Math.pow(hsvPoints.elementAt(iterPoint).getVal(2)-valProm, 2);
		}
		
		if (hsvPoints.size()>1) {
			hueDE = Math.sqrt(hueDE/(hsvPoints.size()-1));
			satDE = Math.sqrt(satDE/(hsvPoints.size()-1));
			valDE = Math.sqrt(valDE/(hsvPoints.size()-1));
		}
		//System.out.println("Promedio. Hue: " + hueProm +"+-"+ hueDE +". Sat: "+ satProm+"+-"+ satDE  +". Val: "+ valProm+"+-"+ valDE );
		System.out.println("Rangos. Hue: (" + Math.floor(hueProm-hueDE) +", "+ Math.ceil(hueProm+hueDE) +"). Sat: (" + Math.floor(satProm-satDE) +", "+ Math.ceil(satProm+hueDE) +"). Val: (" + Math.floor(valProm-valDE) +", "+ Math.ceil(valProm+valDE) +")");
//		System.out.println("Pos: "+ e.getX()+","+ e.getY()+"Hue: " + hsv.getVal(0) + "Sat: "+ hsv.getVal(1)+" Val: "+ hsv.getVal(2));
		canvas.detector.clearColors();
		canvas.detector.addColor(Color.RED, new HSVRange(new Range(Math.floor(hueProm-hueDE), Math.ceil(hueProm+hueDE)), new Range(Math.floor(satProm-satDE), Math.ceil(satProm+hueDE)), new Range(Math.floor(valProm-valDE), Math.ceil(valProm+valDE))));
		//Rangos Amarillo. Hue: (26.0, 33.0). Sat: (205.0, 221.0). Val: (248.0, 256.0)
		canvas.detector.addColor(Color.YELLOW,	new HSVRange(new Range(3, 31), new Range(150,227), new Range(245, 255)));
		//detector.addColor(Color.YELLOW,	new HSVRange(new Range(3, 21), saturation, value));
		//Rangos.Green Hue: (13.0, 20.0). Sat: (108.0, 124.0). Val: (135.0, 158.0)
		canvas.detector.addColor(Color.GREEN,	new HSVRange(new Range(9.0, 20.0), new Range(108.0, 124.0), new Range(115.0, 158.0)));
		//detector.addColor(Color.GREEN,	new HSVRange(new Range(16.0, 30.0),  saturation, value));
		//Rangos.Cyan Hue: (-13.0, 145.0). Sat: (14.0, 113.0). Val: (197.0, 216.0)
		canvas.detector.addColor(Color.CYAN,	new HSVRange(new Range(0, 15), new Range(41, 103), new Range(150, 205)));
		//detector.addColor(Color.CYAN,	new HSVRange(new Range(70, 100), saturation, value));
		//Rangos.Blue Hue: (137.0, 154.0). Sat: (48.0, 67.0). Val: (139.0, 174.0)
		canvas.detector.addColor(Color.BLUE,	new HSVRange(new Range(100, 200), new Range(22, 121), new Range(94, 158)));
		//detector.addColor(Color.BLUE,	new HSVRange(new Range(100, 160), saturation, value));
		//Rangos.MAGENTA Hue: (173.0, 176.0). Sat: (180.0, 195.0). Val: (237.0, 260.0)
		canvas.detector.addColor(Color.MAGENTA, new HSVRange(new Range(160.0, 180), new Range(160, 250), new Range(237.0, 255)));
		//detector.addColor(Color.MAGENTA, new HSVRange(new Range(160.0, 180), saturation, value));
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		hsvPoints.clear();
		System.out.println("Borro puntos");
	}

	public void mouseClicked(MouseEvent e) {
		System.err.println("mouseClicked");

	}

}
