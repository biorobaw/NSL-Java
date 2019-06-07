/**
 * 
 */
package robot;

import java.awt.Color;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

/**
 * @author gtejera
 *
 */
public class ImageProcessing {
	private static final double TOLERANCIA = 60;// 0.075;
	private static final double NOISE = 25;
	private static final int MIN_WIDTH_LANDMARK = 17;

	public static BufferedImage makePanorama(BufferedImage [] images) {
		int baseCol = 0;
		BufferedImage panorama = new BufferedImage(images.length * images[0].getWidth(),
				images[0].getHeight(), images[0].getType());
		for (int iterImages = 0; iterImages < images.length; iterImages++) {
			for (int iterWidth = 0; iterWidth < images[iterImages].getWidth(); iterWidth++)
				for (int iterHeight = 0; iterHeight < images[iterImages]
						.getHeight(); iterHeight++) {
					panorama.setRGB(baseCol + iterWidth, iterHeight,
							images[iterImages].getRGB(iterWidth, iterHeight));
				}
			baseCol = baseCol + images[iterImages].getWidth();
		}
		return panorama;
	}

	/* clasic histogram */
	public static int [] makeHistogram2(BufferedImage image, Color color) {
		int pixelsCounter, suma = 0;
		int [] result = new int [image.getWidth()];
		for (int iterWidth = 0; iterWidth < image.getWidth(); iterWidth++) {
			// primero cuento para esta columna cuantos pixeles hay de ese color
			pixelsCounter=0;
			for (int iterHeight = 0; iterHeight < image	.getHeight(); iterHeight++) {
				if (distancia(rgb2Color(image.getRGB(iterWidth,iterHeight)), color)<TOLERANCIA)
					pixelsCounter++;
			}
			result[iterWidth]=pixelsCounter > NOISE?pixelsCounter:0;
			suma = suma + result[iterWidth];

		}
		//System.out.println("IP::%color: " + (100*suma)/(image.getWidth()*image.getHeight()));

		return result;
	}

	/* Burst histogram */ 
	public static int [] makeHistogram(BufferedImage image, Color color) {
		int pixelsCounter, suma = 0, maxBurst=0;
		int [] result = new int [image.getWidth()];
		for (int iterWidth = 0; iterWidth < image.getWidth(); iterWidth++) {
			// primero cuento para esta columna cuantos pixeles hay de ese color
			pixelsCounter=0; maxBurst=0;
			for (int iterHeight = 0; iterHeight < image	.getHeight(); iterHeight++) {
				if (distancia(rgb2Color(image.getRGB(iterWidth,iterHeight)), color)<TOLERANCIA)
					pixelsCounter++;
				else {
					if (pixelsCounter>maxBurst)
						maxBurst = pixelsCounter;
					pixelsCounter=0;
				}
					
				
			}
			if (pixelsCounter>maxBurst)
				maxBurst = pixelsCounter;
			
			result[iterWidth]=maxBurst > NOISE?maxBurst:0;
			suma = suma + result[iterWidth];

		}
		//System.out.println("IP::%color: " + (100*suma)/(image.getWidth()*image.getHeight()));

		return result;
	}

	public static int colourCount(BufferedImage image, Color color) {
		int result=0;
		for (int iterWidth = 0; iterWidth < image.getWidth(); iterWidth++) {
			// primero cuento para esta columna cuantos pixeles hay de ese color
			for (int iterHeight = 0; iterHeight < image	.getHeight(); iterHeight++) {
				if (distancia(rgb2Color(image.getRGB(iterWidth,iterHeight)), color)<TOLERANCIA)
					result++;
			}
		}
		return result;
	}
	

	/**
	 * @param image bufferedImage
	 * @param color Color
	 * @param range Double
	 * @return result
	 */
	public static double percentInRange(BufferedImage image, Color color,
			Double range) {
		double result=0;
		int ini = (int) range.x;
		int fin = (int) range.y;

		if (ini<0) ini=0;
		if (fin>=image.getWidth()) fin = image.getWidth()-1;

		for (int iterWidth = ini; iterWidth <= fin; iterWidth++) {
			// primero cuento para esta columna cuantos pixeles hay de ese color
			for (int iterHeight = 0; iterHeight < image	.getHeight(); iterHeight++) {
				if (distancia(rgb2Color(image.getRGB(iterWidth,iterHeight)), color)<TOLERANCIA)
					result++;
			}
		}
		
		result= result/(fin-ini)/(double)image.getHeight()*100.0;
		
		System.err.println("IM::percentInRange: " +result);
		return result;
	}
	
	public static BufferedImage makeHistogramImage(BufferedImage image, Color color, Double landmark) {
		BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());
		int [] intHist = makeHistogram(image,color);
		// cambio la altura para la posición donde está la marca
		intHist[(int) landmark.x] = (int) landmark.y;
		int colorH;
		
		int suma =0;
		for (int iterWidth = 0; iterWidth < image.getWidth(); iterWidth++) {
			colorH = ((int) landmark.x)==iterWidth?color2RGB(Color.RED):color2RGB(color);
			for (int iterHeight = 0; iterHeight < intHist[iterWidth]; iterHeight++) {
					result.setRGB(iterWidth, image.getHeight()-iterHeight-1, colorH);
			}
			suma = suma + intHist[iterWidth];

		}
		System.out.println("IP::MHI::%color: " + (100*suma)/(image.getWidth()*image.getHeight()));

		return result;
	}
	

	public static void makeHistogramMinWidth(int[] hist, int ini, int fin) {
		int firstActiveColumns = ini;

		for (int iterWidth = ini; iterWidth < fin; iterWidth++) {
			if (hist[iterWidth]==0) {
				if ((iterWidth-firstActiveColumns) < MIN_WIDTH_LANDMARK) // borro las columnas del histograma
					for (int iterWLand = firstActiveColumns; iterWLand <= iterWidth; iterWLand++)
						hist[iterWLand]=0;
				firstActiveColumns = iterWidth +1;
			}
		}
	}
	
	/**
	 * @param hist int[]
	 * @param ini int
	 * @param fin int
	 * @return int
	 */
	public static int maxHeightMinWidth(int[] hist, int ini, int fin) {
		makeHistogramMinWidth(hist, ini, fin);
		return maxHeight(hist, ini, fin);
	}
	
	static int maxHeight(int [] intHist , int ini, int fin) {
		int result=-1;
		int max = 0;
		
		if (ini<0) ini=0;
		if (fin>=intHist.length) fin = intHist.length-1;
		
		for (int iterWidth = ini; iterWidth <fin; iterWidth++)
			if (intHist[iterWidth]>max) {
				result = iterWidth;
				max = intHist[iterWidth];
			}
		return result;
	}
	

	public static Double averageMinWidth(int [] intHist , Double range) {
		makeHistogramMinWidth(intHist, (int)range.x, (int)range.y);
		return average(intHist, range);
	}

	static Double average(int [] intHist , Double range) {
		Double result=new Double();
		int suma = 0;
		double sd = 0; // desviacion estandar
		int ini = (int) range.x;
		int fin = (int) range.y;

		if (ini<0) ini=0;
		if (fin>=intHist.length) fin = intHist.length-1;
		
		for (int iterWidth = ini; iterWidth <fin; iterWidth++)
			suma = suma + intHist[iterWidth];
		result.x = suma/(double)(fin-ini); // seteo el promedio
		for (int iterWidth = ini; iterWidth <fin; iterWidth++)
			sd = sd + Math.pow(result.x-intHist[iterWidth],2);
		
		result.y = Math.sqrt(sd); // seteo la desviación estandar
		return result;
	}
	
	static int mediana(int [] intHist , int ini, int fin) {
		int result=-1;
		
		if (ini<0) ini=0;
		if (fin>=intHist.length) fin = intHist.length-1;
		
		int sumaPixles=0;
		// sumo todos los pixeles para el color
		for (int iterWidth = ini; iterWidth <fin; iterWidth++)
			sumaPixles=sumaPixles+intHist[iterWidth];
		
		if (sumaPixles>0) {
			// solo voy a considerar hata la mitad de los pixeles "mediana"
			sumaPixles = sumaPixles / 2;
			for (int iterWidth = ini; iterWidth < fin; iterWidth++) {
				sumaPixles = sumaPixles - intHist[iterWidth];
				if (sumaPixles <= 0) {
					result = iterWidth;
					break; // !esa!
				}
			}
		}
		return result;
	}
	
	public static Color rgb2Color(int rgb) {
		int red = (rgb & 0x00ff0000) >> 16;
		int green = (rgb & 0x0000ff00) >> 8;
		int blue = rgb & 0x000000ff;
		return new Color(red, green, blue);	
	}

	public static int color2RGB(Color color) {
		return (color.getRed() << 16)+(color.getGreen() << 8)+color.getBlue();
	}

	/* http://en.wikipedia.org/wiki/Color_difference */
//	static double distanciaCEI76(Color a, Color b) {
//		ColorSpaceConverter csc = new ColorSpaceConverter();
//		int [] rgbA = {a.getRed(),a.getGreen(), a.getBlue()};
//		int [] rgbB = {b.getRed(),b.getGreen(), b.getBlue()};		
//		double [] labA=csc.RGBtoLAB(rgbA);
//		double [] labB=csc.RGBtoLAB(rgbB);
//			 
//		return Math.sqrt(Math.pow(labA[0]-labB[0],2)+Math.pow(labA[1]-labB[1],2)+Math.pow(labA[2]-labB[2],2));	
//	}

	/* http://www.compuphase.com/cmetric.htm */
	static double distancia2(Color a, Color b)
	{
	  long rmean = (a.getRed() + b.getRed()) / 2;
	  long red = a.getRed() - b.getRed();
	  long green = a.getGreen() - b.getGreen();
	  long blue = a.getBlue()-b.getBlue();
	  
	  return Math.sqrt((((512+rmean)*red*red)>>8) + 4*green*green + (((767-rmean)*blue*blue)>>8));
	}
	
	static double distanciaHSV(Color a, Color b) {
		HSVColor hsvA = rgb2hsv(a),hsvB = rgb2hsv(b);
		double result;
		double TH_V_MIN = 0.3, TH_V_MAX = 0.6;
		double TH_S_MIN = 0.2, TH_S_MAX = 0.8;
		if ((hsvA.getV()<TH_V_MIN) || (hsvA.getV()>TH_V_MAX) || (hsvA.getV()<TH_V_MIN) || (hsvA.getV()>TH_V_MAX))
			result = 1;
		else if ((hsvA.getS()<TH_S_MIN) || (hsvA.getS()>TH_S_MAX) || (hsvA.getS()<TH_S_MIN) || (hsvA.getS()>TH_S_MAX))
			result = 1;
		else
			result = Math.abs(hsvA.getH()-hsvB.getH());
		return result;
	}

	static double distancia(Color a, Color b) {
		return Math.sqrt(Math.pow(a.getRed()-b.getRed(),2)+Math.pow(a.getGreen()-b.getGreen(),2)+Math.pow(a.getBlue()-b.getBlue(),2));
	}
	
	//The hue (H) varies on the 360-degree hue circle (also known as the color wheel), red being 0, yellow 60, green 120, cyan 180, blue 240, and magenta 300. The S and V range from 0 to 1. Here is the source code of the RGB-to-HSV converter function.
	
	static HSVColor rgb2hsv(Color color) {
		double h=0, s=0, v=0;
		double r = color.getRed()/256.0, g = color.getGreen()/256.0, b = color.getBlue()/256.0;
//

//		 computedH = 60*(h - d/(maxRGB - minRGB));
//		 computedS = (maxRGB - minRGB)/maxRGB;
//		 computedV = maxRGB;
//		 return [computedH,computedS,computedV];
//		}
		double minRGB = Math.min(b, Math.min(g, r));
		double maxRGB = Math.max(b, Math.max(g, r));
		HSVColor result;
		double d;
//		 // Black-gray-white
		if (minRGB==maxRGB) result = new HSVColor(0,0,(int)minRGB);
		else {
			d = (r==minRGB) ? (g-b) : ((b==minRGB) ? (r-g) : (b-r));
			h = (r==minRGB) ? 3 : ((b==minRGB) ? 1 : 5);
			result = new HSVColor((60*(h - d/(maxRGB - minRGB)))/360.0, ((maxRGB - minRGB)/maxRGB), maxRGB);
		}
		
		return result;
	}
	
//	function rgb2hsv (r,g,b) {
//		 var computedH = 0;
//		 var computedS = 0;
//		 var computedV = 0;
//
//		 //remove spaces from input RGB values, convert to int
//		 var r = parseInt( (''+r).replace(/\s/g,''),10 ); 
//		 var g = parseInt( (''+g).replace(/\s/g,''),10 ); 
//		 var b = parseInt( (''+b).replace(/\s/g,''),10 ); 
//
//		 if ( r==null || g==null || b==null ||
//		     isNaN(r) || isNaN(g)|| isNaN(b) ) {
//		   alert ('Please enter numeric RGB values!');
//		   return;
//		 }
//		 if (r<0 || g<0 || b<0 || r>255 || g>255 || b>255) {
//		   alert ('RGB values must be in the range 0 to 255.');
//		   return;
//		 }
//		 r=r/255; g=g/255; b=b/255;
//		 var minRGB = Math.min(r,Math.min(g,b));
//		 var maxRGB = Math.max(r,Math.max(g,b));
//
//		 // Black-gray-white
//		 if (minRGB==maxRGB) {
//		  computedV = minRGB;
//		  return [0,0,computedV];
//		 }
//
//		 // Colors other than black-gray-white:
//		 var d = (r==minRGB) ? g-b : ((b==minRGB) ? r-g : b-r);
//		 var h = (r==minRGB) ? 3 : ((b==minRGB) ? 1 : 5);
//		 computedH = 60*(h - d/(maxRGB - minRGB));
//		 computedS = (maxRGB - minRGB)/maxRGB;
//		 computedV = maxRGB;
//		 return [computedH,computedS,computedV];
//		}
}
