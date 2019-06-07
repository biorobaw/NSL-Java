/* SCCS @(#)TNDelta.mod	1.2 --- 09/22/99 -- 23:21:01 */

/*TNDelta
* This module calculates the delay and delta for 
* the temporal remapping.
* @see  TNDelta.nslm
* @version 98/6/18
* @author Dominey and Alexander
*/
nslImport nslAllImports;

nslModule TNDelta (int stdsz)  {

// input ports
public NslDinFloat2 fefsac (stdsz,stdsz);
public NslDinFloat0 tn();

// output ports
public NslDoutFloat0 fefgate();
public NslDoutFloat0 tnDelta();

// parameters 
private NslFloat0 fefswitchPot_k1 ();
private NslFloat0 fefgatePot_tm ();
private NslFloat0 fefswitch_k1 ();
private NslFloat0 fefswitch_k2 ();
private NslFloat0 fefswitch_k3 ();
private NslFloat0 fefgate_k1 ();
private NslFloat0 fefgate_k2 ();
private NslFloat0 fefgate_k3 ();
/// vars
private NslFloat0 fefswitchPot ();
private NslFloat0 fefgatePot ();
public NslFloat0 fefswitch ();

private NslInt0 protocolNum();


// aa: shortened this list from the orignal
private NslFloat0 tnDelay2_tm();
private NslFloat0 tnDelay3_tm();
private NslFloat0 tnDelta_tm();
// vars
private NslFloat0 tnDelay2();
private NslFloat0 tnDelay3();

public void initRun() {
	if (system.debug>=10) {
		nslPrintln("TNDelta:initRun");
	}

// 	only need if doing collision experiments
//	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
	
       fefgate =0;
       fefswitch=0;
       fefswitchPot=0;
       fefgatePot=0;

	fefswitchPot_k1 =0.21;
	fefgatePot_tm=0.04;
	fefswitch_k1=89;
	fefswitch_k2=0;
	fefswitch_k3=90;
	fefgate_k1=40;  // aa: 2.1.7 also mentions 45
	fefgate_k2 =0;

       //98/11/20 aa: fefgate_k3 = 1 for collisions experiments
	 // is what the 2.1.7 file had as the default
       fefgate_k3=0;  // for non-collision experiments
       // fefgate_k3=1;  // for collision experiments

       tnDelta=0;  //use to be TNCHANGE2 in NSL2.1.7
       tnDelay2=154;
       tnDelay3=154;

	tnDelay2_tm = 0.02;
	tnDelay3_tm = 0.04;  // aa: 2.1.7 had up direction as 0.02
	tnDelta_tm = 0.006;
}
public void simRun() {
	fefswitchPot = fefswitchPot_k1.get()*nslMaxValue(fefsac);
	fefgatePot=nslDiff(fefgatePot,fefgatePot_tm,- fefgatePot + fefswitch);
	fefswitch = nslStep(fefswitchPot,fefswitch_k1,fefswitch_k2,fefswitch_k3);
	fefgate = nslStep(fefgatePot,fefgate_k1,fefgate_k2,fefgate_k3);

	tnDelay2=nslDiff(tnDelay2,tnDelay2_tm, - tnDelay2 + tn);
	tnDelay3=nslDiff(tnDelay3,tnDelay3_tm, - tnDelay3 + tn);
	tnDelta=nslDiff(tnDelta,tnDelta_tm, - tnDelta + tnDelay2 - tnDelay3);

	if (system.debug>=10) {
		nslPrintln("TNDelta:fefgate:"+fefgate);
		nslPrintln("TNDelta:tnDelta:"+tnDelta);
	}

}
}







