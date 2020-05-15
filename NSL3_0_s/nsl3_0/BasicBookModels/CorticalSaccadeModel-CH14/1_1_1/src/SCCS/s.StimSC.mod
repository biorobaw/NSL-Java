h60282
s 00001/00001/00114
d D 1.2 99/09/22 23:21:00 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00115/00000/00000
d D 1.1 99/09/22 22:42:51 aalx 1 0
c date and time created 99/09/22 22:42:51 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */

/* StimSC
* This module is for the stimulatedPremptive and stimulatedColliding saccades.
* This use to be the Compensatory and the Colliding experiment in Dominey's
* thesis.
* The monkey is suppose to stare at a fixation point and not move his
* eyes until it is gone?
* Target A comes on and he starts to move his eyes to it.
* A stimulated Target B comes on and either prempts or collides with
* the ongoing saccade

* @see StimSC.nslm
* @version 98/6/23
* @author Dominey; code: Alexander
*
*/

nslImport nslAllImports;

nslModule StimSC (int stdsz)  {

// The Saccade generator is ala Scudder;  
// These arrays represent the increased density of supcol projection 
// to llbn as a function of increase eccentricity in supcol. 

// Note, the fixation point for a 27x27 grid is 13,13
// However, the StimSC module uses a 9x9 array

// Note, see the documentation in VisualInput.mod

public NslDoutFloat2 stimSC(stdsz,stdsz);
private double currentTime =0;
private double currentTimePlusDelta =0;
private float value =175; 
private int localstdsz = 0;
private int center ;
private int centerP3 ;
private int centerP2 ;
private int centerM2 ;
private int centerM3 ;

private NslInt0 protocolNum();

public void initModule() {
	stimSC.nslSetAccess('W');
}

public void initRun() {
	protocolNum=-1;
D 2
	protocolNum=(NslInt0)nslGetValue("DomineyModel.protocolNum");
E 2
I 2
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
E 2

	// Have to do the following because we start at 0 .
	// Note: for 27xby27 the center is 13,13
	// however, the visual field has been pared down by the time
	// the signal gets to the supcol, thus we are dealing with
	// a 9x9 array here with a center of 4,4

	stimSC=0;	

	center = (int)stdsz/2; //(9/2 =4.5 round down 4)
	centerP3= center + 3;
	centerP2= center + 2;
	centerM3= center - 3;
	centerM2= center - 2;
}

public void simRun() {
	currentTime=system.getCurTime();	
	currentTime=currentTime + 0.005;

	// ("stimulated SC I")
	if (protocolNum==9) {
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11)){
			stimSC[centerM3][center]=value;
		}
		// unlight stimSC
		if (currentTime>0.11) {
			stimSC[centerM3][center]=0;
		}
	}
	//("stimulated SC II")
	if (protocolNum==10) {
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11)){
			stimSC[center][centerM2]=value;
		}
		// unlight stimSC
		if (currentTime>0.11) {
			stimSC[center][centerM2]=0;
		}
	}
	//("Stimulate SC and Lesion FEF I")
	else if (protocolNum==14) {
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11)){
			stimSC[centerM3][center]=value;
		}
		// unlight stimSC
		if (currentTime>0.11) {
			stimSC[centerM3][center]=0;
		}
	}
	if (system.debug>1) {
		//nslPrintln("debug: stimSC ");
		//nslPrintln(stimSC);
		nslPrintln("debug: stimSC:"+ stimSC);
	}
} //end simRun

}



E 1
