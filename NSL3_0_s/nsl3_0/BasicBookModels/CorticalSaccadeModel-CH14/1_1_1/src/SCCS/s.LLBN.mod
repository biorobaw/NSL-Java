h42575
s 00002/00002/00098
d D 1.2 99/09/22 23:20:57 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00100/00000/00000
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

/*LLBN
* Long lead burst neurons
* @see  LLBN.nslm
* @version 98/6/17
* @author Dominey and Alexander
*/
nslImport nslAllImports;

nslModule LLBN (int stdsz)  {

// ports
 public NslDinFloat2 supcol(stdsz,stdsz);
public NslDinFloat2 fefsac(stdsz,stdsz);
public NslDoutFloat2 llbn (stdsz,stdsz);	// output
// parameters 
private NslFloat0 	llbnPot_tm();
private NslFloat0 	llbnPot_k1();
private NslFloat0 	llbnPot_k3();
private NslFloat0 	llbn_kx1();
private NslFloat0 	llbn_kx2();
private NslFloat0 	llbn_ky1();
private NslFloat0 	llbn_ky2();
//vars
private NslFloat2 llbnwta (stdsz,stdsz);
private NslFloat2 llbnPot (stdsz,stdsz);	// long lead burst neurons of the brainstem saccade generator

private NslFloat0 nWTAThreshold();
private NslFloat0 protocolNum();

public void initModule() {
	llbnPot_k1.nslSetAccess('W');  // adaptaion factor for lesion FEF
	llbnPot_k3.nslSetAccess('W');  // adaptaion factor for lesion SCS
}

public void initRun() {
D 2
	nWTAThreshold=(NslFloat0)nslGetValue("DomineyModel.nWTAThreshold");
	protocolNum=(NslInt0)nslGetValue("DomineyModel.protocolNum");
E 2
I 2
	nWTAThreshold=(NslFloat0)nslGetValue("domineyModel.nWTAThreshold");
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
E 2

	llbn=0;
	llbnwta=0;
	llbnPot=0;
        
	llbnPot_tm = 0.08;
	// aa: From the 92 paper is says that the connection strength
	// from SC to LLBN
	// is increase from 2.67 to 5.0 for 14
	// However this is not in the 2.1.7 stimulus file.
	llbnPot_k1 = 2.67;
	if (protocolNum==14) {
		llbnPot_k1 = 5.0; // aa: lesioning of FEF causes
				// SC projections to LLBN to increase
	}
	// aa: From the 92 paper is says that the connection strength
	// from FEF to LLBN
	// is increase from 5.4 to 9.4 for 13
	// However this is not in the 2.1.7 stimulus file.
	llbnPot_k3 = 5.4;
	if (protocolNum==13){
		 llbnPot_k3 = 9.4; // aa: lesioning of SC causes 
			// FEF projections to LLBN to increase
	}

	llbn_kx1 = 0;
	llbn_kx2 = 950;
	llbn_ky1 = 0;
	llbn_ky2 = 950; 
}

public void simRun()
{
	llbnPot=nslDiff(llbnPot,llbnPot_tm,-llbnPot + llbnPot_k1*supcol+ llbnPot_k3*fefsac);	// 		// visualinput from SC and FEF
	llbnwta = DomineyLib.winnerTakeAll(llbnPot,nWTAThreshold.get(),stdsz);

		// the winner take all is what allows a stimulated
		// saccade to interrupt an ongoing saccade - 
		// implies that weighted averageing occurs upstream

		// note that in the double saccades, the llbnPot (membrane
		// Potential) layer sometimes shows activity at multiple sites

	llbn = nslSaturation(llbnwta,llbn_kx1,llbn_kx2,llbn_ky1,llbn_ky2);

	if (system.debug>=5) {
		nslPrintln("debug: LLBN: ");
		nslPrintln(llbn);
	}
}
};










E 1
