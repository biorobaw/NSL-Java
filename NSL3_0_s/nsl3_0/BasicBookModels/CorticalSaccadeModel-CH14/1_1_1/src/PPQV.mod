/* SCCS  @(#)PPQV.mod	1.2 --- 09/22/99 -- 23:20:58 */

/*PPQV
* Burst Neurons
* @see PPQV.nslm
* @version 98/6/18
* @author Dominey; coder Alexander
* Note: the index need to be size independent
*/

nslImport nslAllImports;

nslModule PPQV (int stdsz)  {

// input ports
public NslDinFloat0 horizontalVelocity();
public NslDinFloat0 verticalVelocity ();
public NslDinFloat2 scDelay(stdsz,stdsz);
public NslDinFloat2 erasure2(stdsz,stdsz);
public NslDinFloat2 posteriorParietal(stdsz,stdsz);
public NslDinFloat2 fovElem(stdsz,stdsz);
//output ports
public NslDoutFloat2 ppqv(stdsz,stdsz);
// parameters 
private NslFloat0 qvCtr();
private NslFloat0 qvFactor();
private NslFloat0 oblique ();
private NslFloat0 qvMask_k1 ();
private NslFloat0 qvMask_k2 ();
private NslFloat0 ppqvPot_k1 ();
private NslFloat0 ppqvPot_k2 ();
private NslFloat0 ppqv_kx1();
private NslFloat0 ppqv_kx2();
private NslFloat0 ppqv_ky1();
private NslFloat0 ppqv_ky2();
//vars
private NslFloat2 qvMask(stdsz,stdsz);
private NslFloat2 qvmask1(stdsz,stdsz);
// private NslFloat2 qvmask2(stdsz,stdsz); // 2.1.7 has commented out
private NslFloat0 inhSurr();
private NslFloat2 ppqvPot(stdsz,stdsz);
private NslFloat2 ppqva(stdsz,stdsz);
private NslFloat2 ppqvT(stdsz,stdsz);

private NslFloat0 protocolNum(); 
private int center;

public void initModule() {
	qvMask_k1.nslSetAccess('W');
}

public void initRun() {
	protocolNum=-1;
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
	center = stdsz/2;

	ppqv=0;
        ppqva=0;
        ppqvT=0;
        qvMask=0;
        qvmask1=0;
        inhSurr=0;
        ppqvPot=0;

	qvCtr = 1;       
 
	qvFactor = 1600;
	oblique = 1;

	qvMask_k1=1.23;
	//98/11/17 aa: qvMask_k1 =1.23 for non-memory protocolNums
       // and 0 for memory protocolNums
       if ((protocolNum==2)||(protocolNum==3)) {

               qvMask_k1=0;  //0 for memory protocolNums to disable
		// ppqv's intrinsic memory
       }
       // 99/7/27 these are memory saccades but require 
	// ppqv to save the second second until FON goes high
	// and the new location can be locked into fefmem and thmem.

       if ((protocolNum==8)||(protocolNum==15)) {
	  qvMask_k1=1.23;
	}

	qvMask_k2=0; 
	ppqvPot_k1= 1;
	ppqvPot_k2= 0;// for scDelay
	ppqv_kx1=0;


	ppqv_kx2=97.2; //aa: 96.5 or 97.4 in 2.1.7 but no description as 
	// too which protocolNum 

	ppqv_ky1=0;
	ppqv_ky2=90;
	inhSurr=0;  //aa: 98-7-20 no data to support this except comment below
}
public void simRun() {

	qvmask1 =DomineyLib.shift(horizontalVelocity,verticalVelocity,qvFactor,stdsz);

	// commented out in 2.1.7
	// qvmask2 =DomineyLib.shift(horizontalVelocity,verticalVelocity,
	// qvFactor, stdsz, qvCenter, Oblique);

	//qvMask = qvMask_k1*qvmask1 + qvMask_k2*qvmask2;
	// qvMask_k2 always 0 in 2.1.7
	qvMask = qvMask_k1*qvmask1;

	// This draws a square -5 elements on a side- around the fovea area
	qvMask[2][2] = inhSurr		;
	qvMask[2][3] = inhSurr		;
	qvMask[2][4] = inhSurr		; 
	qvMask[2][5] = inhSurr		;
	qvMask[2][6] = inhSurr		;
	qvMask[3][6] = inhSurr		;
	qvMask[4][6] = inhSurr		;
 	qvMask[5][6] = inhSurr		;
	qvMask[6][2] = inhSurr		;
	qvMask[6][3] = inhSurr		;
	qvMask[6][4] = inhSurr		;
	qvMask[6][5] = inhSurr		;
	qvMask[6][6] = inhSurr		;
	qvMask[3][2] = inhSurr		;
	qvMask[4][2] = inhSurr		; 
	qvMask[5][2] = inhSurr		;


	ppqvPot = posteriorParietal  + qvMask@ppqva - ppqvPot_k1*fovElem -ppqvPot_k2*(erasure2@scDelay);
	ppqvPot = erasure2^ppqvPot; 
	ppqva = nslSigmoid(ppqvPot,ppqv_kx1,ppqv_kx2,ppqv_ky1,ppqv_ky2);
	ppqv = ppqva;
	ppqv	[center][center] = 0; 	// center of fovea is zero

       //98/11/9 aa: added until 3d plot gets fixed
       // using this matrix instead of ppqv for 3d display
       ppqvT =nslTrans(ppqv);

	if (system.debug>=17) {
		nslPrintln("PPQV: ppqv: ");
		nslPrintln(ppqv);
	}

}

}//end class







