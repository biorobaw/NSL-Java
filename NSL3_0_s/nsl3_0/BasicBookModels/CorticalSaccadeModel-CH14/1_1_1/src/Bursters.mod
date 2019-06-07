/* SCCS  @(#)Bursters.mod	1.2 --- 09/22/99 -- 23:20:54 */

/*Bursters
* Burst Neurons
* @see Bursters.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/
nslImport nslAllImports;

nslModule Bursters (int stdsz) 	 {
// ports
public NslDinFloat0 lebn ();
public NslDinFloat0 rebn ();
public NslDinFloat0 uebn ();
public NslDinFloat0 debn ();

public NslDoutFloat2 saccademask(stdsz,stdsz);
// parameters 
private NslFloat0 saccadebool_k1();
private NslFloat0 saccadebool_k2();
private NslFloat0 saccadebool_k3();

//vars
private NslFloat0 bursters  ();


public void initRun() {
	saccadebool_k1=240;
	saccadebool_k2=1;
	saccadebool_k3=0;
}
public void simRun() {
	bursters = uebn + debn + lebn + rebn;
	// aa: this is reverse of what is intuitive
	// if x<240, then y=1.0
	// if x>=240, then y=0.0
	saccademask = nslStep(bursters,saccadebool_k1,saccadebool_k2,saccadebool_k3);
	if (system.debug>=14) {
		nslPrintln("Bursters:simRun: saccademask");
		nslPrintln(saccademask);
	}
}

} // end of class




