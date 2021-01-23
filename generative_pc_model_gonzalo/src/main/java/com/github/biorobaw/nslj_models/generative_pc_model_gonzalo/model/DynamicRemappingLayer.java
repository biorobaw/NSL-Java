package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
/* Mdulo NSL que representa la capa de remapeo dinmico del
   ambiente y del ancla dentro del mismo.
   Alejandra Barrera
   Versin: 1
   Fecha: 18 de marzo de 2005
   Versin: 3
   Fecha: 19 de febrero de 2007
 */

import java.awt.geom.Point2D.Double;

import com.github.biorobaw.nslj.lang.NslDinInt0;
import com.github.biorobaw.nslj.lang.NslDoutDouble1;
import com.github.biorobaw.nslj.lang.NslDoutDouble2;
import com.github.biorobaw.nslj.lang.NslModule;

public class DynamicRemappingLayer extends NslModule {
	public static final int DYNAMIC_REMAPPING_HEIGHT = Configuration
			.getInt("DynamicRemappingLayer.LAYER_HEIGHT"); // 25 laberintos
															// anteriores
	public static final int DYNAMIC_REMAPPING_WIDTH = Configuration
			.getInt("DynamicRemappingLayer.LAYER_WIDTH");

	private final double DYNAMIC_MAP_HEIGHT = Configuration
			.getDouble("DynamicRemappingLayer.MAP_HEIGHT");
	private final double DYNAMIC_MAP_WIDTH = Configuration
			.getDouble("DynamicRemappingLayer.MAP_WIDTH");

	public static final boolean PI_ON = Configuration
			.getBoolean("DynamicRemappingLayer.PI_ON");

	public NslDinInt0 currentDir;
	public NslDinInt0 nextDir;
	public NslDoutDouble1 PIps1dim;
	public NslDoutDouble2 PIps2dim;

	private int sizePS;
	private int dnrDim1;
	private int dnrDim2;
    private double[] PIps1dimAux;
    private double[][] PIps2dimAux;
	private Convolution conv;
	private int x;
	private int y;

	public DynamicRemappingLayer(String nslName, NslModule nslParent) {
		super(nslName, nslParent);
		dnrDim1 = DYNAMIC_REMAPPING_HEIGHT * 2;
		dnrDim2 = DYNAMIC_REMAPPING_WIDTH * 2;
		sizePS = dnrDim1 * dnrDim2;
		currentDir = new NslDinInt0("currentDir", this);
		nextDir = new NslDinInt0("nextDir", this);
		PIps1dim = new NslDoutDouble1("PIps1dim", this, sizePS);
		PIps2dim = new NslDoutDouble2("PIps2dim", this, dnrDim1, dnrDim2);
		PIps1dimAux = new double[sizePS];
		PIps2dimAux = new double[dnrDim1][dnrDim2];
		if (!DynamicRemappingLayer.PI_ON)
			System.err.println("DRL::PI = false");
		buildPerceptualSchemaFromMazeCoor(Rat.simItem.getInitialPosition().x,
				Rat.simItem.getInitialPosition().z); // OJO con el Z :D
	}

	private void buildPerceptualSchemaFromMazeCoor(double x, double y) {

		this.x=(int)(x*10+DYNAMIC_REMAPPING_HEIGHT/2);
		this.y=(int)(y*10+DYNAMIC_REMAPPING_WIDTH/2);
			
		buildPerceptualSchema2Dim(this.x, this.y);
//		System.err.println("DRL::matriz::x=" + this.x + " y=" + this.y
//				+ "************************");
		System.err.println("DRL::coordenadas::x=" + x + " y=" + y);
//		System.err.println("DRL::coordenadas globales::x="
//				+ RobotFactory.getRobot().getGlobalCoodinate().x + " y="
//				+ RobotFactory.getRobot().getGlobalCoodinate().y);
	}

	
	public void buildPerceptualSchema2Dim(int posX, int posY) {
		// TODO: By Gonzalo la convolucion tienen memoria y antes no se estaba
		// resetanto cuando la rata de movia a prepo a una determinado posicion
		conv = new Convolution(dnrDim1 / 2, dnrDim1 / 2);
		System.err.println("DRL::build:: x: " + posX + " y:" + posY);
		double d1 = 2;
		int i1 = dnrDim1 - posY * 2;
		int j1 = dnrDim2 - posX * 2;
		System.err.println("DRL::row, col: " + i1 + ", " + j1);

		for (int k = 0; k < dnrDim1; k++) {
			for (int l = 0; l < dnrDim2; l++)
				PIps2dimAux[k][l] = Math
						.exp((double) (-1 * (k - i1) * (k - i1))
								/ (2D * (d1 * d1))
								+ (double) (-1 * (l - j1) * (l - j1))
								/ (2D * (d1 * d1)));
		}
	}

	public void buildPerceptualSchema1Dim() {
		double maxValue = java.lang.Double.MIN_VALUE, maxX = -1, maxY = -1;

		for (int l = 0; l < dnrDim1; l++) {
			for (int i1 = 0; i1 < dnrDim2; i1++) {
				PIps1dimAux[l * dnrDim2 + i1] = PIps2dimAux[l][i1];
				if (PIps1dimAux[l * dnrDim2 + i1] < 0.01D)
					PIps1dimAux[l * dnrDim2 + i1] = 0.0D;
				if (PIps1dimAux[l * dnrDim2 + i1] > 1.0D)
					PIps1dimAux[l * dnrDim2 + i1] = 1.0D;

				if (PIps1dimAux[l * dnrDim2 + i1] > maxValue) {
					maxValue = PIps1dimAux[l * dnrDim2 + i1];
					maxX = l;
					maxY = i1;
				}

			}
		}
		// System.out.println("DRL::build1Dim::Posición del ancla: "+ maxX+", "+
		// maxY+ "@@@@@@@@@@@@@@@@@@");
	}

	public void simRun() {
		if (DynamicRemappingLayer.PI_ON) {
				if (Rat.newTrial)
					buildPerceptualSchemaFromMazeCoor(
							Rat.simItem.getInitialPosition().x,
							Rat.simItem.getInitialPosition().z); // / OJO con el
																	// Z :D
				else if (currentDir.get() == nextDir.get()) {
					conv.buildConvolution(currentDir.get(), nextDir.get(),
							PIps2dimAux);
					Double robotCoors = RobotFactory.getRobot().getGlobalCoodinate();
					int posXDLR = dnrDim1 - (int)(robotCoors.x*10+DYNAMIC_REMAPPING_HEIGHT/2.0)*2;
					int posYDLR = dnrDim2 - (int)(robotCoors.y*10+DYNAMIC_REMAPPING_WIDTH/2.0)*2;
					if ((conv.maxRow!=posYDLR)||(conv.maxCol!=posXDLR)&&(currentDir.get() == nextDir.get()))
						System.err.println("DRL::error en posicion, pos "+conv.maxCol+", "+conv.maxRow + ". DLR::pos: "+posXDLR +", "+ posYDLR);
				}
		} else // ojo que esto no es apagarlo si no setearlo con información global // solo para debug
			buildPerceptualSchemaFromMazeCoor(RobotFactory.getRobot().getGlobalCoodinate().x,RobotFactory.getRobot().getGlobalCoodinate().y);

		buildPerceptualSchema1Dim();

		
		PIps1dim.set(PIps1dimAux);
		PIps2dim.set(PIps2dimAux);
	} // simRun
}
