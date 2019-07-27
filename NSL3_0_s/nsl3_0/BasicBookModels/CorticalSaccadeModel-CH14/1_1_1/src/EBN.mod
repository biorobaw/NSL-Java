/* SCCS  @(#)EBN.mod	1.2 --- 09/22/99 -- 23:20:55 */
/*EBN
* Excitatory burst neurons -  module
* @see  EBN.nslm
* @version 98/6/18
* @author Dominey and Alexander
* Ebns for left, right up and down - since we did a winner take all
* in the llbns, the maximum element is the one we want. 
* Inhibited by pause cells
*/
nslImport nslAllImports;

nslModule EBN (int stdsz)  {

// ports
public NslDinFloat0 pause();  //input
public NslDinFloat2 mlbn(stdsz,stdsz);  //input
public NslDoutFloat0 ebn (); // output
// parameters 
// aa: shortened this list from the orignal
private NslFloat0 ebnPot_tm();
private NslFloat0 ebnPot_k1();
private NslFloat0 ebn_k1();
private NslFloat0 ebn_k2();
private NslFloat0 ebn_k3();
// vars
private NslFloat0 ebnPot ();	// excitatory burst neurons of the brainstem saccade generator
private NslFloat0 mlbnMax();	// medium long lead burst neurons max

public void initRun() {
	ebn=0;
	ebnPot=0;
	mlbnMax=0;

	ebnPot_tm = 0.006;
	ebnPot_k1 = 10;
	ebn_k1 = 240;
	ebn_k2 = 0;
	ebn_k3 = 240;

}
public void simRun() {
	mlbnMax= nslMaxValue(mlbn);
	ebnPot= nslDiff(ebnPot,ebnPot_tm,-ebnPot + mlbnMax -ebnPot_k1*pause);
	ebn = nslRamp(ebnPot,ebn_k1,ebn_k2,ebn_k3);
	if (system.debug>=8) {
		nslPrintln("EBN:simRun: ebn:"+ebn);
		nslPrintln("EBN:simRun: mlbnMax:"+mlbnMax);
	}
}
}



