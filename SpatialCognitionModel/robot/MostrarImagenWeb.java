package robot;
import java.awt.geom.Point2D.Double;

import java.awt.*;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import static com.googlecode.javacv.cpp.opencv_core.*;


public class MostrarImagenWeb extends Frame implements MouseListener {
	private static final String IP_CAMERA = "khepera";
	private static final String PORT_CAMERA = "8080";
	private static final long IMAGE_CONSISTENCE_DELAY = 100;
	private static final double DISTANCE2BY_DIFFERENTS = 32;
	private static final long MAX_DIFFERENT_PIXELS = 250;
	
	private static String STR_IMAGE_URL = "http://"+IP_CAMERA+":"+PORT_CAMERA+"/?action=snapshot";

	private URL IMAGE_URL;
	private BufferedImage imagen;
	
	public MostrarImagenWeb() {
		this(STR_IMAGE_URL);
	}

	/**
	 * @param url String
	 */
	public MostrarImagenWeb(String url) {
		System.err.println("MIW::URL: "+url);
		setSize(320, 240);
		setVisible(true);
		try {
			IMAGE_URL = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addMouseListener(this);
	}

	public void paint(Graphics g) {
		g.drawImage(imagen, 0, 0, this);
	}

	public static void main(String[] args) {
		MostrarImagenWeb imagen = new MostrarImagenWeb();		


		System.out.println("URL: "+ STR_IMAGE_URL);
		
		while (true) {
			try {
				imagen.getImage();
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	BufferedImage getImage() throws Exception {
		imagen = getStableImage(); //ImageIO.read(IMAGE_URL);
		setSize(imagen.getWidth(), imagen.getHeight());
		repaint();
		return imagen;
		//System.out.println("Image Height: "+imagen.getHeight()+ ". Width: "+imagen.getWidth()+"RGB: "+imagen.getRGB(0, 0));
	}
	
	public BufferedImage getStableImage() throws Exception {
		BufferedImage ini, fin;
		fin= ImageIO.read(IMAGE_URL); 
		do {
			ini = fin;
			try {Thread.sleep(IMAGE_CONSISTENCE_DELAY);} catch (Exception e) {e.printStackTrace();}	
			fin= ImageIO.read(IMAGE_URL); 		
			System.err.print(".");
		} while (aBitDifferents(ini, fin));
		System.err.println("");
		return fin;
	}

	private boolean aBitDifferents(BufferedImage a, BufferedImage b) {
        IplImage aHSV = ImageCanvas.bufferedImage2IplImage(a);
        IplImage bHSV = ImageCanvas.bufferedImage2IplImage(b);
        double distance, minDistance=java.lang.Double.MAX_VALUE;
        long pixelsDifferents=0;
      
		for (int iterW=0; iterW<a.getWidth();iterW++)
			for (int iterH=0; iterH<a.getHeight();iterH++) {
				distance = Math.abs(cvGet2D(aHSV, iterH, iterW).val(0) - cvGet2D(bHSV, iterH, iterW).val(0));
				if (distance > DISTANCE2BY_DIFFERENTS) pixelsDifferents++;
				minDistance = Math.min(minDistance,distance);
			}
		System.err.print("MinDist:"+minDistance+". DifPx: "+pixelsDifferents);

		return pixelsDifferents>MAX_DIFFERENT_PIXELS;
	}
	
	void setImage(BufferedImage image) {
		this.imagen=image ;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	Double iniPoint = new Double();
	Double finPoint = new Double();

	public void mousePressed(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON1) {
		System.out.println("PIF x: "+ e.getX()+", " + e.getY()+".");
		iniPoint.setLocation(e.getX(), e.getY());
		} else 	if (e.getButton()==MouseEvent.BUTTON3) {
			clearPoints();
		} else 	if (e.getButton()==MouseEvent.BUTTON2) {
			processPoints();
			
		}
	}

	/**
	 * 
	 */
	private void clearPoints() {
		pointsHSV.clear();
		pointsRGB.clear();
		try {
			getImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.err.println("MIW::borrando puntos y tomo foto.");

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON1) {
		finPoint.setLocation(e.getX(), e.getY());
		addPoints();
		}
	}
	
	void printDataPoint(MouseEvent e) {
		BufferedImage staticImage;
		try {
			staticImage = this.getImage();
	        IplImage iplHSV = ImageCanvas.bufferedImage2IplImage(staticImage);
	        System.err.println("x: " +e.getX() + ". y: "+e.getY()+". hue: "+cvGet2D(iplHSV,e.getY() , e.getX()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("x: " +e.getX() + ". y: "+e.getY()+". hue: ERROR");

		}

	}
	Vector<Color> pointsRGB = new Vector<Color>();
	Vector<CvScalar> pointsHSV = new Vector<CvScalar>();
	
	void addPoints() {
		try {
			BufferedImage staticImage = this.getImage();
	        IplImage iplHSV = ImageCanvas.bufferedImage2IplImage(staticImage);
//			System.err.println("i: " +(int) Math.min(iniPoint.getX(), finPoint.getX())+ ":"+(int)Math.max(iniPoint.getX(), finPoint.getX()));
//			System.err.println("j: " +(int) Math.min(iniPoint.getY(), finPoint.getY())+ ":"+(int)Math.max(iniPoint.getY(), finPoint.getY()));

			for (int i=(int) Math.min(iniPoint.getX(), finPoint.getX()); i<(int)Math.max(iniPoint.getX(), finPoint.getX()); i++)
				for (int j= (int) Math.min(iniPoint.getY(), finPoint.getY()); j<(int)Math.max(iniPoint.getY(), finPoint.getY());j++) {
					pointsRGB.add(ImageProcessing.rgb2Color(staticImage.getRGB(i, j)));
					staticImage.setRGB(i, j,255);
					pointsHSV.add(cvGet2D(iplHSV, j, i));
				}
			setImage(staticImage);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	
	void processPoints() {
		Color color, colorProm;
		double count=0;
		double r=0.0,g=0.0,b=0.0,h=0.0,s=0.0,v=0.0;
		double rMin=255,gMin=255,bMin=255,hMin=255,sMin=255,vMin=255;
		double rMax=0.0,gMax=0.0,bMax=0.0,hMax=0.0,sMax=0.0,vMax=0.0;
		double rSD=0.0,gSD=0.0,bSD=0.0,hSD=0.0,sSD=0.0,vSD=0.0,distRGBSD=0.0,distHSVSD=0.0;
		try {
			CvScalar pixelHsv;
			System.err.println("size: " + pointsHSV.size()+".");
			for (int iterPoints=0; iterPoints<pointsHSV.size(); iterPoints++) {
					color = pointsRGB.elementAt(iterPoints);
					pixelHsv = pointsHSV.elementAt(iterPoints);

					r=r+color.getRed();
					g=g+color.getGreen();
					b=b+color.getBlue();
					h=h+pixelHsv.val(0);
					s=s+pixelHsv.val(1);
					v=v+pixelHsv.val(2);
					count++;
					rMin=Math.min(rMin, color.getRed());
					gMin=Math.min(gMin, color.getGreen());
					bMin=Math.min(bMin, color.getBlue());
					rMax=Math.max(rMax, color.getRed());
					gMax=Math.max(gMax, color.getGreen());
					bMax=Math.max(bMax, color.getBlue());

					hMin=Math.min(hMin, pixelHsv.val(0));
					sMin=Math.min(sMin, pixelHsv.val(1));
					vMin=Math.min(vMin, pixelHsv.val(2));
					hMax=Math.max(hMax, pixelHsv.val(0));
					sMax=Math.max(sMax, pixelHsv.val(1));
					vMax=Math.max(vMax, pixelHsv.val(2));
			}
			System.err.print("\n");

			r=r/count;
			g=g/count;
			b=b/count;
			h=h/count;
			s=s/count;
			v=v/count;
			colorProm = new Color((int)r,(int)g,(int)b);
			
			for (int iterPoints=0; iterPoints<pointsHSV.size(); iterPoints++) {
				color = pointsRGB.elementAt(iterPoints);
				pixelHsv = pointsHSV.elementAt(iterPoints);

					rSD=rSD+Math.pow(color.getRed()-r,2);
					gSD=gSD+Math.pow(color.getGreen()-g,2);
					bSD=bSD+Math.pow(color.getBlue()-b,2);
					hSD=hSD+Math.pow(pixelHsv.val(0)-h,2);
					sSD=sSD+Math.pow(pixelHsv.val(1)-s,2);
					vSD=vSD+Math.pow(pixelHsv.val(2)-v,2);
					distRGBSD=distRGBSD + ImageProcessing.distancia(color, colorProm);
			}
			rSD=Math.sqrt(rSD/count);
			gSD=Math.sqrt(gSD/count);
			bSD=Math.sqrt(bSD/count);
			hSD=Math.sqrt(hSD/count);
			sSD=Math.sqrt(sSD/count);
			vSD=Math.sqrt(vSD/count);
			distRGBSD = distRGBSD/count;
			
			System.err.print("\n");
			int CANT_SD_H =2;
			int CANT_SD_S =40;
			int CANT_SD_V =15;
			//System.out.println("MIW::rgb: ("+r+"+-"+rSD+", "+g+"+-"+gSD+", "+b+"+-"+bSD+"). distProm: "+distRGBSD);
			System.out.println("MIW::Promedio HSV: ("+h+"+-"+hSD+", "+s+"+-"+sSD+", "+v+"+-"+vSD+")");
			
			System.out.println("MIW::Rango H: ("+hMin+", "+hMax+"). S: ("+sMin+", "+sMax+"). V: ("+vMin+", "+vMax+"). ");
	        //Range saturation = new Range(sMin, sMax);  // valores chicos poco saturado (blancos, grises depende de el valor V)
	        //Range saturation = new Range(30, 230);
	        Range saturation = new Range(s-sSD*CANT_SD_S, s+sSD*CANT_SD_S);
	        //Range value = new Range(vMin, vMax);		// valores chicos poca luz (negro)
	        //Range value = new Range(0, 255);		
	        Range value = new Range(v-vSD*CANT_SD_V, v+vSD*CANT_SD_V);		
	        //Range hue = new Range(hMin, hMax);
	        Range hue = new Range(h-hSD*CANT_SD_H, h+hSD*CANT_SD_H);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}

