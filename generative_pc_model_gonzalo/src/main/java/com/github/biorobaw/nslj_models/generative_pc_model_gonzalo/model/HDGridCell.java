package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
/* Clase que implementa los disparos de una celda grilla direccional basado en las
   propuestas de Burgess2007 y Hasselmo2005.
   Gonzalo Tejera
   Versión: 1 (Junio, 2012)
 */


public class HDGridCell {
	public static double DELTA_T = 0.01;
	private static double H = 300; 
	private static double BH = 2.0/(Math.sqrt(3)*H);
	private static double INITIAL_PHASE = 0;
	private double preferedDirection; // direccion preferida de esta neurona
	private double phaseDendrite = INITIAL_PHASE;
	private double phaseSoma = INITIAL_PHASE;
	private double fSoma;
	
	public HDGridCell(double fSoma, double preferedDirection, double initialPhase) {
		this.fSoma=fSoma;
		this.preferedDirection = preferedDirection;
		phaseDendrite = initialPhase;
	}

	// ejecuta un desplazamiento del animal y retorna el potencial de accion
	double doStep(double headDirection, double speed) {
		double fDendrite = fSoma + fSoma*BH*speed*Math.cos(headDirection-preferedDirection);
		phaseDendrite = phaseDendrite + fDendrite*2.0*Math.PI*DELTA_T;
		phaseSoma = phaseSoma + fSoma*2.0*Math.PI*DELTA_T;
		return Math.cos(phaseDendrite)+Math.cos(phaseSoma);
	}	

}
