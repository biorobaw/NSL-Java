/* SCCS  @(#)Pause.mod	1.2 --- 09/22/99 -- 23:20:58 */
/*Pause
* pause -  module
* @see  Pause.nslm
* @version 98/6/18
* @author Dominey and Alexander
*/
nslImport nslAllImports;

nslModule Pause (int stdsz)  {

// input ports
public NslDinFloat2 supcol(stdsz,stdsz);  //input
public NslDinFloat2 fefsac(stdsz,stdsz);  //input
public NslDinFloat2 stm(stdsz,stdsz);  //input
public NslDinFloat2 weights(stdsz,stdsz);  //input
public NslDinFloat2 mlbn(stdsz,stdsz);  //input
public NslDinFloat0 ebn();  //input

// output ports
public NslDoutFloat0 pause (); // output
// vars
private NslFloat0 pausePot ();	
  // pausePot cells are inhibited by Trig and are reactivated when RI = DELTA
private NslFloat2 arrTrig (stdsz,stdsz);	
private NslFloat0 dataTrig ();
private NslFloat0 dataTrigN ();
private NslFloat0 dataIdelta ();	
private NslFloat0 datadelta();
private NslFloat2 arrdelta (stdsz,stdsz);
private NslFloat2 arrMask(stdsz,stdsz);


// parameters 
private NslFloat0 	alldirectionpause_k1();
private NslFloat0 	arrTrig_tm();
private NslFloat0 	arrTrig_k1();
private NslFloat0 	pausePot_tm();
private NslFloat0 	pausePot_k1();
private NslFloat0 	dataTrigN_k1();
private NslFloat0 	dataTrigN_k2();
private NslFloat0 	dataRTrigN_k3();
private NslFloat0 	arrMask_k1();
private NslFloat0 	arrMask_k2();
private NslFloat0 	arrMask_k3();
private NslFloat0 	dataIdelta_k1();
private NslFloat0 	datadelta_k1();
private NslFloat0 	datadelta_k2();
private NslFloat0 	datadelta_k3();
private NslFloat0 	pause_k1();
private NslFloat0 	pause_k2();
private NslFloat0 	pause_k3();

// resettable integrators that are compared with DELTA
private NslFloat0 resetInteg ();  
private NslFloat0 resetIntegPot ();
// parameters 
private NslFloat0 	resetIntegPot_k1();
private NslFloat0 	resetInteg_k1();
private NslFloat0 	resetInteg_k2();
private NslFloat0 	resetInteg_k3();
private float maxdelta;

public void initModule() {	
}

public void initRun() {
	resetInteg=0;

	 pause =0;
       pausePot =0;
       arrTrig =0;
       dataTrig=0;
       dataTrigN =0;
       dataIdelta=0;
       datadelta=0;
       arrdelta =0;
       arrMask=0;
       maxdelta=0;

	alldirectionpause_k1=1.1;
	pausePot_tm=0.006;

 	arrTrig_tm=0.006;
	arrTrig_k1=50;
	// aa: 2.1.7 model had the left pausePot with a k1 of 1 while the rest were 0.1.
	pausePot_k1=0.1;
	dataTrigN_k1=0;
	dataTrigN_k2=0;
	dataRTrigN_k3=0;
	arrMask_k1=240;
	arrMask_k2=0;
	arrMask_k3=40;
	dataIdelta_k1=3;
	datadelta_k1=0;
	datadelta_k2=0;
	datadelta_k3=0;
	pause_k1=8;
	pause_k2=300;
	pause_k3=0;
	resetIntegPot_k1=0.031;
	resetInteg_k1=0;
	resetInteg_k2=0;
	resetInteg_k3=0;


}

public void simRun() {
	// The trigger cells get input from FEF and supcol.  Once the saccade begins,
	// these cells are inhibited so that residual activity in FEF and supcol won't
	// prevent short saccades from ending
	arrTrig=nslDiff(arrTrig,arrTrig_tm,-arrTrig+(stm^supcol)+(stm^fefsac) - arrTrig_k1*arrMask);

	pausePot=nslDiff(pausePot,pausePot_tm,-pausePot + pausePot_k1*dataTrigN + datadelta - alldirectionpause_k1*resetInteg);

	resetIntegPot = resetInteg + resetIntegPot_k1*ebn -pause;
		// this thresholding allows us to manipulate the accuracy
		// of the saccade by specifying how close DELTA and RI
		// must be to terminate the saccade (triger the pausePot cells)


	arrdelta = arrMask^weights;  // weights has values that increase
		// as you move to the periphery.  arrMask has a constant
		// value once the inputs are above threshold.  The result
		// is that the spatial FEF and supcol signal gets converted into
		// delatA which is used to produce datadelta which is compared
		// to the resetable integrator to terminate the saccade
//-------------------
	dataTrig = nslMaxValue(arrTrig);
	dataTrigN = nslRamp(dataTrig,dataTrigN_k1,dataTrigN_k2,dataRTrigN_k3);
		// this thresholding allows us to manipulate the accuracy
		// of the saccade by specifying how close DELTA and RI
		// must be to terminate the saccade (triger the pausePot cells)
	resetInteg = nslRamp(resetIntegPot, resetInteg_k1, resetInteg_k2, resetInteg_k3);

	arrMask = nslStep(mlbn,arrMask_k1,arrMask_k2,arrMask_k3);
		// fires at a fixed rate when llbns above threshold.
		// used to get the Delta

	maxdelta = nslMaxValue(arrdelta);
	dataIdelta = maxdelta - dataIdelta_k1*pause;
	datadelta = nslRamp(dataIdelta,datadelta_k1,datadelta_k2,datadelta_k3);
	pause = nslStep(pausePot,pause_k1,pause_k2,pause_k3);

	if (system.debug>=9) {
		nslPrintln("debug: Pause: dataTrigN");
		nslPrintln(dataTrigN);
		nslPrintln("debug: Pause: pause");
		nslPrintln(pause);
	}
}
}



