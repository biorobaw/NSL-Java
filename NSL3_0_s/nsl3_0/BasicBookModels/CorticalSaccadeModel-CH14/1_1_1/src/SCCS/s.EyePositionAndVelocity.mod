h19809
s 00000/00000/00122
d D 1.2 99/09/22 23:20:56 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00122/00000/00000
d D 1.1 99/09/22 22:42:47 aalx 1 0
c date and time created 99/09/22 22:42:47 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*EyePositionAndVelocity
* Eye Position and Velocity 
* @see  EyePositionAndVelocity.nslm
* @version 98/6/18
* @author Dominey and Alexander
* This module represents the saccade motor neurons.
* From (Robinson, 1970) we have the following for firing rate, D, as a
* function of location (Theta) and velocity (d.Theta/d.t):
*  		D = k*Theta + r*d.Theta/d.t + c
* 		  = 2.75*Theta + 0.9* d.Theta/d.t + 154;
*  this simplified linear sytem for the mapping of oculomotor neuron firing
*  rates to location and velocity of eye will be used in our preliminary
*  model. 
*/
nslImport nslAllImports;

nslModule EyePositionAndVelocity ()  {
// input ports
public NslDinFloat0 ltn ();
public NslDinFloat0 rtn ();
public NslDinFloat0 utn ();
public NslDinFloat0 dtn ();
public NslDinFloat0 lebn ();
public NslDinFloat0 rebn ();
public NslDinFloat0 uebn ();
public NslDinFloat0 debn ();
public NslDinFloat0 ltnChange ();
public NslDinFloat0 rtnChange ();
public NslDinFloat0 utnChange ();
public NslDinFloat0 dtnChange ();
// output ports
public NslDoutFloat0 horizontalVelocity();
public NslDoutFloat0 verticalVelocity();
public NslDoutFloat0 horizontalTheta();
public NslDoutFloat0 verticalTheta();


// parameters 
private NslFloat0 eyeH_k1 ();
private NslFloat0 eyeV_k1 ();
private NslFloat0 eyeH_k2 ();
private NslFloat0 eyeV_k2 ();
private NslFloat0 vv_k1 ();
private NslFloat0 vv_k2 ();
private NslFloat0 vv_k3 ();
private NslFloat0 hv_k1 ();
private NslFloat0 hv_k2 ();
private NslFloat0 hv_k3 ();
private NslFloat0 horizontalTheta_k1 ();
private NslFloat0 verticalTheta_k1();
private NslFloat0 eyeVdown_k1();
private NslFloat0 eyeHleft_k1();
private NslFloat0 eyeVdown_k2();
private NslFloat0 eyeHleft_k2();

//vars
private NslFloat0 eyeV();
private NslFloat0 eyeH();
private NslFloat0 eyeVdown();
private NslFloat0 eyeHleft();


public void initRun() {
       horizontalVelocity=0;;
       verticalVelocity=0;
       horizontalTheta=0;
       verticalTheta=0;
       eyeV=0;
       eyeH=0;
       eyeVdown=0;
       eyeHleft=0;

	eyeV_k1 =154;
	eyeV_k2 = 0.3636364;
	eyeH_k1 = 154;
	eyeH_k2 = 0.3636364;
	vv_k1 = 0;
	vv_k2 = 17;
	vv_k3 = 17;
	hv_k1 = 0;
	hv_k2 = 17;
	hv_k3 = 17;
	horizontalTheta_k1 = 0.1;
	verticalTheta_k1 = 0.1;
	eyeVdown_k1 = 154;
	eyeVdown_k2 = 0.3636364;
	eyeHleft_k1 = 154;
	eyeHleft_k2 = 0.3636364;
}
	
public void simRun() {
	eyeH = eyeH_k2*rtn - 56;
	eyeV = eyeV_k2*utn - 56; 
	verticalVelocity = vv_k1*uebn - vv_k1*debn + vv_k2*utnChange - vv_k3*dtnChange;
	horizontalVelocity = hv_k1*rebn - hv_k1*lebn + hv_k2*rtnChange - hv_k3*ltnChange;
	horizontalTheta = horizontalTheta_k1*eyeH;
	verticalTheta = verticalTheta_k1*eyeV;

	//NOTE: eyeHdown and eyeVleft represents gaze angle, and is not used in the
	//model, but only as an indicator of eye position for experimenter
	eyeVdown = eyeVdown_k2*dtn - 56;
	eyeHleft = eyeHleft_k2*ltn - 56;

	if (system.debug>=12) {
		nslPrintln("EyePos: simRun: horizontalVelocity:"+horizontalVelocity);
		nslPrintln("EyePos: simRun: verticalVelocity:"+verticalVelocity);
		nslPrintln("EyePos: simRun: horizontalTheta:"+horizontalTheta);
		nslPrintln("EyePos: simRun: verticalTheta:"+verticalTheta);
		
	}


}

}





E 1
