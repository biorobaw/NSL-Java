h45594
s 00000/00000/00085
d D 1.2 99/09/22 23:20:58 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00085/00000/00000
d D 1.1 99/09/22 22:42:48 aalx 1 0
c date and time created 99/09/22 22:42:48 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*Motor
* Motor Generating Schema
* @see  Motor.nslm
* @version 98/6/18
* @author Dominey; coder Alexander
*/
nslImport nslAllImports;

nslModule Motor (int stdsz, int direction)  {
//private int direction; //l=1, r=2, u=3, d=4

// input ports
public NslDinFloat2 llbn(stdsz,stdsz);
public NslDinFloat2 supcol(stdsz,stdsz);
public NslDinFloat2 fefsac(stdsz,stdsz);
public NslDinFloat0 tn ();

//output ports
public NslDoutFloat0 ebn ();
public NslDoutFloat0 fefgate();
public NslDoutFloat0 mn ();
public NslDoutFloat0 tnDelta();

//vars
private MLBN mlbn1 (stdsz);
private EBN ebn1 (stdsz);
private Pause pause1 (stdsz);
private TNDelta tnDelta1(stdsz);
private MN mn1();
private STM stm1(stdsz,direction);  //NSL2.1.7 had last in scheduler,
// but stm should be ok here since it comes after everything it feeds.

public void initModule() {
	if (system.debug>=6) {
		nslPrintln("debug:Motor:initModule:direction "+direction);
	}

}
public void initRun() {
	if (system.debug>=6) {
		nslPrintln("debug:Motor:initRun:direction "+direction);
	}
}
public void makeConn() {
	// outputs to inputs
	// inputs	
	nslRelabel(llbn,mlbn1.llbn);
	nslRelabel(supcol,pause1.supcol);
	nslRelabel(fefsac,pause1.fefsac);
	nslRelabel(fefsac,tnDelta1.fefsac);
	nslRelabel(tn,mn1.tn); 
	nslRelabel(tn,tnDelta1.tn);

	//middles
	nslConnect(mlbn1.mlbn,ebn1.mlbn);
	nslConnect(mlbn1.mlbn,pause1.mlbn);
	nslConnect(ebn1.ebn,pause1.ebn);
	nslConnect(pause1.pause,ebn1.pause);

	nslConnect(ebn1.ebn,mn1.ebn); 

	nslConnect(stm1.stm,mlbn1.stm);	
	nslConnect(stm1.stm,pause1.stm);	
	nslConnect(stm1.weights,pause1.weights);
 
	// outputs
	nslRelabel(ebn1.ebn,ebn);
	nslRelabel(tnDelta1.fefgate,fefgate);
	nslRelabel(mn1.mn,mn);
	nslRelabel(tnDelta1.tnDelta,tnDelta);
}

public void simRun(){
	if (system.debug>=6) {
		nslPrintln("debug:Motor:simRun:direction "+direction);
	}
}
}





E 1
