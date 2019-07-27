/* SCCS  @(#)FON.mod	1.2 --- 09/22/99 -- 23:20:56 */

/*FON
* Turns on the correct Fovial Element in the Lateral Inter Parietal
* of the Posterior Parietal Cortex
* @see FON.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/
nslImport nslAllImports;

nslModule FON (int stdsz)  {

// port inputs
public NslDinFloat2 posteriorParietal (stdsz,stdsz);
// port outputs
public NslDoutFloat2 fon(stdsz,stdsz);  
public NslDoutFloat2 fovElem(stdsz,stdsz); 
public NslDoutFloat0 fonCenter();  

// parameters 
private NslFloat0 fonPot_k1();
private NslFloat0 fon_x1();
private NslFloat0 fon_y1();
private NslFloat0 fon_y2();

//vars
private NslFloat2 fonPot(stdsz,stdsz);
private NslFloat0 fovElemCenter();
//private NslFloat0 fonCenter();
private int center;


public void initModule() {	
}

public void initRun() {
       center =(int) stdsz/2;
       fon=0;
       fovElem=0;
       fonPot=0;
       fovElemCenter=0;
       fonCenter=0;

	fonPot_k1=1;
	fon_x1=5;
	fon_y1=0;
	fon_y2=90;
}
public void simRun() {
	fonPot =fonPot_k1 *posteriorParietal[center][center]; 

	fon = nslStep(fonPot,fon_x1,fon_y1,fon_y2);

	fovElem[center][center] = posteriorParietal[center][center]; 

	fonCenter=fon[center][center];
	fovElemCenter=fovElem[center][center];

	if (system.debug>=20) {
		nslPrintln("FON:simRun: fonCenter");
		nslPrintln(fonCenter);
		nslPrintln("FON:simRun: fovElemCenter");
		nslPrintln(fovElemCenter);
	}
}

} //end class




