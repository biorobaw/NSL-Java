h47490
s 00000/00000/00152
d D 1.2 99/09/22 23:20:53 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00152/00000/00000
d D 1.1 99/09/22 22:42:45 aalx 1 0
c date and time created 99/09/22 22:42:45 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */
/**
* BrainStem
* BrainStem network
* @see  BrainStem.mod
* @version 98/6/18
* @author Dominey ; coder Alexander
* Saccade generator ala Scudder;  These arrays represent the increased
* density of SC projection to LLBNs as a function of increase eccentricity
* in SC. 
*/

nslImport nslAllImports;

nslModule BrainStem (int stdsz, int numOfDirections, NslFloat0 nWTAThreshold)  {
// inports
public NslDinFloat2 supcol(stdsz,stdsz);
public NslDinFloat2 fefsac(stdsz,stdsz);


// outports
public NslDoutFloat0 horizontalVelocity();
public NslDoutFloat0 verticalVelocity();
public NslDoutFloat0 horizontalTheta();
public NslDoutFloat0 verticalTheta();
public NslDoutFloat2 saccademask(stdsz,stdsz);
 
//vars
private LLBN llbn1(stdsz);
private Motor motorl(stdsz,1);//left
private Motor motorr(stdsz,2);//right
private Motor motoru(stdsz,3);//up
private Motor motord(stdsz,4);//down
private EyePositionAndVelocity eyePosAndVel1();
private TNChange tnChangel();//left
private TNChange tnChanger();//right
private TNChange tnChangeu();//up
private TNChange tnChanged();//down

private Bursters bursters1(stdsz);

public void makeConn() {
	// inputs	
	nslRelabel(supcol, llbn1.supcol);
	nslRelabel(fefsac, llbn1.fefsac);

	nslRelabel(supcol,motorl.supcol);	
	nslRelabel(supcol,motorr.supcol);	
	nslRelabel(supcol,motoru.supcol);	
	nslRelabel(supcol,motord.supcol);	

	nslRelabel(fefsac,motorl.fefsac);
	nslRelabel(fefsac,motorr.fefsac);
	nslRelabel(fefsac,motoru.fefsac);
	nslRelabel(fefsac,motord.fefsac);

	//middles
	nslConnect(llbn1.llbn,motorl.llbn);
	nslConnect(llbn1.llbn,motorr.llbn);
	nslConnect(llbn1.llbn,motoru.llbn);
	nslConnect(llbn1.llbn,motord.llbn);


	//nslConnect(motorl.ebn,motorr.opposite_ebn);
	//nslConnect(motorr.ebn,motorl.opposite_ebn);
	//nslConnect(motoru.ebn,motord.opposite_ebn);
	//nslConnect(motord.ebn,motoru.opposite_ebn);

	nslConnect(motorl.ebn,bursters1.lebn);
	nslConnect(motorr.ebn,bursters1.rebn);
	nslConnect(motoru.ebn,bursters1.uebn);
	nslConnect(motord.ebn,bursters1.debn);


	nslConnect(motorl.ebn,tnChangel.ebn);
	nslConnect(motorr.ebn,tnChanger.ebn);
	nslConnect(motoru.ebn,tnChangeu.ebn);
	nslConnect(motord.ebn,tnChanged.ebn);

        // note: the TN Change in Theta calculations require both ebns
	nslConnect(motorl.ebn,tnChanger.opposite_ebn);
	nslConnect(motorr.ebn,tnChangel.opposite_ebn);
	nslConnect(motoru.ebn,tnChanged.opposite_ebn);
	nslConnect(motord.ebn,tnChangeu.opposite_ebn);

	nslConnect(motorl.ebn,eyePosAndVel1.lebn);
	nslConnect(motorr.ebn,eyePosAndVel1.rebn);
	nslConnect(motoru.ebn,eyePosAndVel1.uebn);
	nslConnect(motord.ebn,eyePosAndVel1.debn);

	nslConnect(motorl.fefgate,tnChangel.fefgate);
	nslConnect(motorr.fefgate,tnChanger.fefgate);
	nslConnect(motoru.fefgate,tnChangeu.fefgate);
	nslConnect(motord.fefgate,tnChanged.fefgate);

	nslConnect(motorl.tnDelta,tnChangel.tnDelta);
	nslConnect(motorr.tnDelta,tnChanger.tnDelta);
	nslConnect(motoru.tnDelta,tnChangeu.tnDelta);
	nslConnect(motord.tnDelta,tnChanged.tnDelta);

	nslConnect(tnChangel.tn,motorl.tn);
	nslConnect(tnChanger.tn,motorr.tn);
	nslConnect(tnChangeu.tn,motoru.tn);
	nslConnect(tnChanged.tn,motord.tn);

	nslConnect(tnChangel.tn,eyePosAndVel1.ltn);
	nslConnect(tnChanger.tn,eyePosAndVel1.rtn);
	nslConnect(tnChangeu.tn,eyePosAndVel1.utn);
	nslConnect(tnChanged.tn,eyePosAndVel1.dtn);

	nslConnect(tnChangel.tnChange,eyePosAndVel1.ltnChange);
	nslConnect(tnChanger.tnChange,eyePosAndVel1.rtnChange);
	nslConnect(tnChangeu.tnChange,eyePosAndVel1.utnChange);
	nslConnect(tnChanged.tnChange,eyePosAndVel1.dtnChange);

	// outputs

	nslRelabel(eyePosAndVel1.horizontalVelocity,horizontalVelocity);
	nslRelabel(eyePosAndVel1.verticalVelocity,verticalVelocity);
	nslRelabel(eyePosAndVel1.horizontalTheta,horizontalTheta);
	nslRelabel(eyePosAndVel1.verticalTheta, verticalTheta);

	nslRelabel(bursters1.saccademask,saccademask);
}


public void simRun() {
	if (system.debug>=2) {
		nslPrintln("BrainStem:simRun");
	}
}


} // end of class


















E 1
