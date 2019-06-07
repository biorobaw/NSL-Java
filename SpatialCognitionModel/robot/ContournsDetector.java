package robot;
/* 
 * I developed some code for recognize motion detections with JavaCV.
 * Actually, it works with an array of Rect, performing, every cicle, an
 * intersection test with area of difference with the rect of interests
 * (this array is callad "sa", stands for SizedArea). I hope could this
 * helps someone.
 * 
 * Feel free to ask about any question regarding the code above, cheers!
 *
 * Angelo Marchesin <marchesin.angelo@gmail.com>
 */

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;


import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.*;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class ContournsDetector {
	private static final int DEFAULT_MAX_SEPARATION_INF_BORDER = 60;
	public static final int NO_BORDER_SEPARATION = Integer.MAX_VALUE;
	public static final int NO_PORCENTAGE = 0;

	private static final int DEFAULT_MIN_PORCENTAGE = 50;
	private Hashtable<Color,HSVRange> colors = new Hashtable<Color,HSVRange>();
    // inicializo Canvas Frame
    //private CanvasFrame canvasFrame = new CanvasFrame("Contourns Detector");
	
	void addColor(Color rgb, HSVRange hsvRange) {
		colors.put(rgb, hsvRange);
	}
	
	void clearColors() {
		colors.clear();
	}
	
	/**
	 * @param frame IplImage
	 * @param color2tack HSVRange
	 * @param maxBorderSeparation int
	 * @param minPorcentage int
	 * @return CvRect
	 */
	public CvRect findBiggestShape(IplImage frame, HSVRange color2tack, int maxBorderSeparation, int minPorcentage) {
		IplImage gray = thresholdImage(frame, color2tack);

		return findBiggestShapeGray(gray, maxBorderSeparation, minPorcentage);
	}
	
	CvRect findBiggestShape(IplImage color_img, HSVRange colorRange) {
		return findBiggestShape(color_img,colorRange,DEFAULT_MAX_SEPARATION_INF_BORDER, DEFAULT_MIN_PORCENTAGE);
	}

	//static MostrarImagenWeb imagenUmbral = new MostrarImagenWeb();
	/*
	 * Retorna una imagen binaria con los pixeles que cumplen la condicion de
	 * los parametros Hue entre 0 y 128, Saturation entre 0 y 255, y Value entre
	 * 0 y 255
	 */
	public IplImage thresholdImage(IplImage img_hsv, HSVRange colorRange) {
		// select even sizes only
		int width = img_hsv.width() & -2;
		int height = img_hsv.height() & -2;
		// timg = CvCloneImage( color_img ) # make a copy of input image
		IplImage timg = img_hsv.clone();

		// select the maximum ROI in the image
		cvSetImageROI(timg, new CvRect(0, 0, width, height));

		// down-scale and upscale the image to filter out the noise
		IplImage pyr = cvCreateImage(new CvSize(width / 2, height / 2), 8, 3);
		cvPyrDown(timg, pyr, 7);
		cvPyrUp(pyr, timg, 7);

		// Convert image to HSV
		cvCvtColor(timg, timg, CV_BGR2HSV);
		// Blur the image :: Gonzalo
		// dilate and erode the image :: Gonzalo
		cvDilate(timg, timg, null,1);
		cvErode(timg, timg, null,1);
		
		// filtrar por rango:
		IplImage thresholded_img = cvCreateImage(cvGetSize(timg), 8, 1);
		// HSV stands for hue, saturation, and value
		CvScalar minColor = new CvScalar(colorRange.getHue().getMin(), colorRange.getSaturation().getMin(), colorRange.getValue().getMin(),0);
		CvScalar maxColor = new CvScalar(colorRange.getHue().getMax(), colorRange.getSaturation().getMax(), colorRange.getValue().getMax(),0);
		cvInRangeS(timg, minColor, maxColor,	thresholded_img);
		if (colorRange.getHue().getMin() < 0) {
			// Si minHue es menor a cero, se calcula 2 veces, uno para valores
			// de hue entre [minHue,maxHue] y otro entre [180+minHue,180]
			IplImage thresholded_img2 = cvCreateImage(cvGetSize(timg), 8, 1);
			colorRange.getHue().setMin(180 + colorRange.getHue().getMin());
			colorRange.getHue().setMax(180);
			
			cvInRangeS(timg, minColor, maxColor, thresholded_img2);
			cvAdd(thresholded_img, thresholded_img2, thresholded_img, null); // para
																				// imprimir
		}
		//imagenUmbral.setImage(thresholded_img.getBufferedImage());
		return thresholded_img;
	}

	// use contour search to find shapes in binary image
	CvRect findBiggestShapeGray(IplImage grayImage, int maxBorderSeparation, int minPorcentage) {
		CvSeq contours = new CvSeq(null);
		CvSeq points;
		CvMemStorage storage = cvCreateMemStorage(0);
		cvFindContours(grayImage, storage, contours,
				Loader.sizeof(CvContour.class), CV_RETR_LIST,
				CV_CHAIN_APPROX_SIMPLE);
		storage = cvCreateMemStorage(0);
		// Temporales para guardar contorno más grande
		double maxArea = 0, area;
		CvRect maxRect = null, rectangle;

		while (contours != null && !contours.isNull()) {
			if (contours.elem_size() > 0) {
				
				points = cvApproxPoly(contours, Loader.sizeof(CvContour.class),
						storage, CV_POLY_APPROX_DP,
						cvContourPerimeter(contours) * 0.02, 0);
				area = Math.abs(cvContourArea(points, CV_WHOLE_SEQ, 0));
				rectangle = cvBoundingRect(points, 0);
				int mayorY = rectangle.y() + rectangle.height();
				// seteo el rectangulo encontrado como area de interes
				cvSetImageROI(grayImage, rectangle);
				double porcentageColor = 100*cvCountNonZero(grayImage)/(rectangle.height()* rectangle.width());
				if (area>50)
				System.err.println("CD::mayorY: "+ mayorY + ". dif: "+ (grayImage.height()-mayorY)+". area: " + area +". %util: " + porcentageColor);
				if ((area > maxArea)&&(Math.abs(grayImage.height()-mayorY)<maxBorderSeparation) &&(porcentageColor>minPorcentage)){
				//if (area > maxArea) { 
					maxRect = rectangle;
					maxArea = area;
				}
				//System.err.println("CD::contorno con "+contours.elem_size()+"elementos y " + area + " de area");

			}
			contours = contours.h_next();
		}
		
		return maxRect;
	}



	/**
	 * @param image IplImage
	 * @param rectangle CvRect
	 * @param color Color
	 */
	public void drawRectangle(IplImage image, CvRect rectangle, Color color) {
		// TODO Auto-generated method stub
        CvPoint p1 = new CvPoint(0,0),p2 = new CvPoint(0,0);
        p1.x(rectangle.x());
    	p2.x(rectangle.x()+rectangle.width());
    	p1.y(rectangle.y());
    	p2.y(rectangle.y()+rectangle.height());
    	cvRectangle(image, p1,p2, CV_RGB(color.getRed(), color.getGreen(), color.getBlue()), 2, 8, 0);
	}


	public void drawRectangles(IplImage image, Hashtable <Color, CvRect> rectangles) {
		Enumeration<Color> keys = rectangles.keys();
        Color key;
        CvRect rectangle;

        while (keys.hasMoreElements()) {
			key = keys.nextElement();
        	rectangle = rectangles.get(key);
        	drawRectangle(image, expand(rectangle), key);
        	drawRectangle(image, rectangle, key.darker());
		}
		
	}
	
	/**
	 * @param rectangle
	 * @return
	 */
	private CvRect expand(CvRect rectangle) {
		int BORDER_PX = 3;
		
		return new CvRect(rectangle.x()-BORDER_PX, rectangle.y()-BORDER_PX, rectangle.width()+2*BORDER_PX,rectangle.height()+2*BORDER_PX);
	}

	/**
	 * @param frame
	 * @return
	 */
	Hashtable <Color, CvRect> findRectangles(IplImage frame) {
		Hashtable <Color, CvRect> rectangles = new Hashtable<Color, CvRect>();
		CvRect rectangle;
		Enumeration<Color> keys = colors.keys();
		Color key;
		HSVRange hsvRange;
		
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			hsvRange = colors.get(key);
			rectangle = findBiggestShape(frame, hsvRange);
			if (rectangle!=null) rectangles.put(key, rectangle);
		}
		return rectangles;
	}


	/* combina varias imagenes de distinto ancho pero igual alto
	 * pero no anda bien el cvCopy con el ROI, copia la misma imagen y se escracha
	 */
	IplImage combineImages(IplImage [] images) {
		IplImage result = cvCreateImage( cvSize(images.length*images[0].width(),images[0].height()), images[0].depth(), images[0].nChannels());
		int actualWidthPos = 0;
		for (int iterImages=0;iterImages<images.length;iterImages++) {	
	        cvSetImageROI(result, cvRect(actualWidthPos, 0, actualWidthPos+images[iterImages].width(), images[0].height())); 
//	        System.out.println("src.depth::"+ images[iterImages].depth() + ". dst: "+result.depth());
//	        System.out.println("src.size::"+ images[iterImages].sizeof() + ". dst: "+result.sizeof());
	        System.out.println("ROI::"+ actualWidthPos + ", 0, "+(actualWidthPos+images[iterImages].width())+", "+images[0].height());
	        System.out.println("src.width::"+ images[iterImages].width() + ". src.height::"+ images[iterImages].height());
	        cvCopy(images[iterImages], result);
	        //cvResetImageROI(result); 
	        actualWidthPos = actualWidthPos + images[iterImages].width();
		}
		return result;
	}


	public static void main(String[] args) throws Exception {
		// inicializo camara
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        
        // ?
        IplImage frame = grabber.grab();

        ContournsDetector detector = new ContournsDetector();
        Hashtable <Color, CvRect> rectangles;
        
        // hue, value, saturation; hue determina el color
        Range saturation = new Range(70, 255);
        Range value = new Range(100, 255);
        Range hueGreen = new Range(60, 80);
        Range hueOrange = new Range(10, 20);
        Range hueMagenta = new Range(160, 180);
        Range hueBlue = new Range(90, 110);
        
        
        detector.addColor(Color.GREEN, new HSVRange(hueGreen, saturation, value));
        detector.addColor(Color.ORANGE, new HSVRange(hueOrange, saturation, value));
        detector.addColor(Color.MAGENTA, new HSVRange(hueMagenta, saturation, value));
        detector.addColor(Color.BLUE, new HSVRange(hueBlue, saturation, value));

        // prueba de combinar con la primer imagen tomada al principio
        IplImage [] images = new IplImage [2];
        IplImage temp = cvCreateImage( cvSize(frame.width(),frame.height()), frame.depth(), frame.nChannels());
        cvCopy(frame, temp);
        images[1] = temp;

        //images[2] = frame.clone();
        
        IplImage panorama;
        //while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
//        while (canvasFrame.isVisible() ) {
        while ((frame = grabber.grab()) != null) {
        	images[0] = frame; //images[1];
            panorama = detector.combineImages(images);
            rectangles = detector.findRectangles(panorama);
            detector.drawRectangles(frame, rectangles);
            //detector.showImage(frame);
        }
        grabber.stop();
        // canvasFrame.dispose();
    }



}