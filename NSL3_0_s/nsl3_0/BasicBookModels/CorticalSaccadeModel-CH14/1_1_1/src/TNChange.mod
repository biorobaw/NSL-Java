/* SCCS  @(#)TNChange.mod	1.2 --- 09/22/99 -- 23:21:00 */

/** TNChange
* Tonic Neurons: calculation for change in theta or eye position
* @see TNChange.nslm
* @version 98/6/18
* @author Dominey: coder Alexander
* Change in position for QV Shift
*/
nslImport nslAllImports;

nslModule TNChange ()  {

// port inputs
public NslDinFloat0 fefgate();
public NslDinFloat0 ebn ();
public NslDinFloat0 opposite_ebn ();
public NslDinFloat0 tnDelta ();
// port outputs
public NslDoutFloat0 tn ();
public NslDoutFloat0 tnChange ();
// parameters 
private NslFloat0 tn_k1();
private NslFloat0 tnDelay1_tm();
//vars
private NslFloat0 tnDelay1 ();


public void initRun() {
	if (system.debug>=13) {
		nslPrintln("debug:TNChange:initRun");
	}

       tn=0;
       tnChange=0;
	tnDelay1=154; // use to be TNDELAY
	tn=154;
	tnChange=0;

	tn_k1=.01527778;
	tnDelay1_tm=.006;
}
public void simRun() {
	tnDelay1=nslDiff(tnDelay1,tnDelay1_tm, - tnDelay1 + tn);
	//On each timestep,the tonic component uppdated by 2.75*(change in Theta).
	// Change in Theta is angular velocity * time =
	//			   opposite_ebn*(1/R)  * (simuation time step)
	tn = tn + tn_k1*ebn - tn_k1*opposite_ebn - fefgate*tnDelta;
	tnChange = tn - tnDelay1;

	if (system.debug>=13) {
		nslPrintln("TNChange:tn: "+tn);
		nslPrintln("TNChange:tnChange: "+tnChange);
	}

}
}










