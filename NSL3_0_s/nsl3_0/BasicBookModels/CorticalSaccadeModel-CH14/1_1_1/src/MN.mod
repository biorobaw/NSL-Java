/* SCCS  @(#)MN.mod	1.2 --- 09/22/99 -- 23:20:57 */

/*MN
* Motor Neurons
* @see MN.nslm
* @version 98/6/18
* @author Dominey ; coder Alexander
*
*/
nslImport nslAllImports;

nslModule MN ()  {

// ports
public NslDinFloat0 ebn ();
public NslDinFloat0 tn ();
public NslDoutFloat0 mn  ();
// parameters 

//vars


public void initRun() {
	mn=0;
}

public void simRun() {
	mn = ebn + tn;

	if (system.debug>=11) {
		nslPrintln("MN:simRun:mn"+mn);
	}
}
}



