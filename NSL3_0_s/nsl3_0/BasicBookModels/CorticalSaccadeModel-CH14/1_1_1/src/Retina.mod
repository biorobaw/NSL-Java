/* SCCS @(#)Retina.mod	1.2 -- 09/22/99 -- 23:20:58 */

/*Retina
* Retinal calculations
* @see  Retina.nslm
* @version 98/6/18
* @author Dominey and Alexander
* This module takes the gated input from the visualInput.
* The input is gated by the signals coming from the brain stem.
*/

nslImport nslAllImports;

nslModule Retina (int stdsz,int bigsz) {

// ports
public NslDinFloat2 saccademask (stdsz,stdsz);
public NslDinFloat2 visualinput(bigsz,bigsz);
public NslDinFloat0 horizontalTheta();
public NslDinFloat0 verticalTheta();
public NslDoutFloat2 retina(stdsz,stdsz);


// parameters 
private NslFloat0 retinaPot_tm();

/// vars
public NslFloat2 retinaPot (stdsz,stdsz);
public NslFloat2 retinaPot1 (stdsz,stdsz);


public void initRun() {
       retina=0;
       retinaPot=0;
       retinaPot1=0;
	retinaPot_tm=.006;
}
public void simRun()
{
	retinaPot1 = DomineyLib.eyeMove(visualinput,horizontalTheta,verticalTheta,stdsz,bigsz);
	retinaPot=nslDiff(retinaPot,retinaPot_tm, - retinaPot + retinaPot1);
	retina = saccademask^retinaPot;

	if (system.debug>=15) {
	  nslPrintln("debug: Retina:retinaPot1 ");
	  nslPrintln(retinaPot1);
	  nslPrintln("debug: Retina:retinaPot ");
	  nslPrintln(retinaPot);
	  nslPrintln("debug: Retina:retina ");
	  nslPrintln(retina);

	}
}
}



