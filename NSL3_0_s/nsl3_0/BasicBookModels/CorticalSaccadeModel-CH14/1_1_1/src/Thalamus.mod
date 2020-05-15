/* SCCS  @(#)Thalamus.mod	1.2 --- 09/22/99 -- 23:21:01 */

/*Thalamus
* Thalamus
* @see Thalamus.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/

nslImport nslAllImports;

nslModule Thalamus(int stdsz) {

// port inputs
public NslDinFloat2 fefmem(stdsz,stdsz);
public NslDinFloat2 scDelay(stdsz,stdsz);
public NslDinFloat2 snrmem(stdsz,stdsz);
public NslDinFloat2 erasure2(stdsz,stdsz);
// port outputs
public NslDoutFloat2 thmem(stdsz,stdsz);

// parameters 
private NslFloat0 thmemPot_tm();
private NslFloat0 thmemPot_k1();
private NslFloat0 thmemPot_k2();
private NslFloat0 thmemPot_k3();
private NslFloat0 thmem_x1();
private NslFloat0 thmem_x2();
private NslFloat0 thmem_y1();
private NslFloat0 thmem_y2();


//vars
private NslFloat2 thmemPot(stdsz,stdsz);
private NslFloat2 erasureConvSCDelay(stdsz,stdsz);
private int center;

public void initRun() {
       thmem=0;
       thmemPot=0;
       erasureConvSCDelay =0;

	center = (int)stdsz/2;

	thmemPot_tm= .006 ;
	thmemPot_k1= 1 ;	
	thmemPot_k2= 4 ;
	thmemPot_k3= 1 ;// aa: diff from 98
	thmem_x1=0 ;
	thmem_x2=  45;
	thmem_y1= 0 ;
	thmem_y2=10  ;
}
public void simRun() {

	erasureConvSCDelay=erasure2@scDelay;

	thmemPot=nslDiff(thmemPot, thmemPot_tm, -thmemPot +thmemPot_k3*fefmem - thmemPot_k1*snrmem - thmemPot_k2*erasureConvSCDelay);
	thmemPot[center][center] = 0; // the " - FOVEA" term - so we don't 	 "rememPotber" the fixation point
	thmem = nslSigmoid(thmemPot,thmem_x1,thmem_x2,thmem_y1,thmem_y2);

	if (system.debug>25) {
		nslPrintln("Thalamus: simRun: thmem");
		nslPrintln(thmem);
	}

}
}






