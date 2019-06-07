 
/*********************************************/
/*	saccade2.c			     */
/*********************************************/

#  include "nsl_include.h" 
#  include "pfd_params2.h"

extern nsl_matrix NSLwinnertakeall(nsl_matrix& );
extern nsl_matrix NSLeyemove(nsl_matrix&, nsl_data&, nsl_data&);
extern nsl_matrix NSLshift(nsl_data&,nsl_data&, nsl_data&);
extern nsl_matrix 
	NSLXshift(nsl_data&,nsl_data&,nsl_data&,nsl_data&,nsl_data&);
extern nsl_matrix 
	NSLX2shift(nsl_data&,nsl_data&,nsl_data&,nsl_data&,nsl_data&);


NETWORK(SACCADE);

INPUT_MAT(INP,27,27);		// input layers
INPUT_MAT(STIMULATION,9,9);

ROW_VEC(P,329);		// data structure for time constants, thresholds etc.
			// see pfd_params2.h and set_parms2.h


ROW_VEC(LTOPO,9);	// these define the increasing projection strengths	
ROW_VEC(RTOPO,9);	// for more peripheral items in FEF and SC onto
COL_VEC(UTOPO,9);	// LLBNs in the brainstem - see Appendix on
COL_VEC(DTOPO,9);	// brainstem saccade generator in cerebral cortex paper
MATRIX(LWEIGHTS,9,9);
MATRIX(RWEIGHTS,9,9);
MATRIX(UWEIGHTS,9,9);
MATRIX(DWEIGHTS,9,9);
MATRIX(LSTM,9,9);
MATRIX(RSTM,9,9);
MATRIX(USTM,9,9);
MATRIX(DSTM,9,9);

MATRIX(llbn,9,9);	// long and medium lead burst neurons of the
MATRIX(llbnwta,9,9);	// brainstem saccade generator
MATRIX(LLBN,9,9);
MATRIX(Lmlbn,9,9);
MATRIX(LMLBN,9,9);
MATRIX(Rmlbn,9,9);
MATRIX(RMLBN,9,9);
MATRIX(Umlbn,9,9);
MATRIX(UMLBN,9,9);
MATRIX(Dmlbn,9,9);
MATRIX(DMLBN,9,9);

MATRIX(ltrig,9,9);	// components of the trigger mechanism that receives
MATRIX(rtrig,9,9);	// cortical and tectal input and inhibits the 
MATRIX(utrig,9,9);	// pause cells
MATRIX(dtrig,9,9);
DATA(LTRIG);
DATA(LTRIGN);
DATA(RTRIG);
DATA(RTRIGN);
DATA(UTRIG);
DATA(UTRIGN);
DATA(DTRIG);
DATA(DTRIGN);


MATRIX(LMASK,9,9);	// components of the DELTA machanism that takes
MATRIX(RMASK,9,9);	// the spatial code and transforms it into
MATRIX(UMASK,9,9);	// a scalar representation of saccade amplitude
MATRIX(DMASK,9,9);
MATRIX(deltal,9,9);
MATRIX(deltar,9,9);
MATRIX(deltau,9,9);
MATRIX(deltad,9,9);
DATA(DELTALI);
DATA(DELTARI);
DATA(DELTAUI);
DATA(DELTADI);
DATA(DELTAL);
DATA(DELTAR);
DATA(DELTAU);
DATA(DELTAD);

DATA(lebn);		// excitatory burst neurons
DATA(LEBN);
DATA(rebn);
DATA(REBN);
DATA(uebn);
DATA(UEBN);
DATA(debn);
DATA(DEBN);

DATA(lri);		// resettable integrators that are
DATA(rri);		// compared with DELTA
DATA(uri);
DATA(dri);
DATA(LRI);
DATA(RRI);
DATA(URI);
DATA(DRI);

DATA(lpause);		// pause cells are inhibited by TRIG
DATA(LPAUSE);		// and are reactivated when RI = DELTA
DATA(rpause);
DATA(RPAUSE);	
DATA(upause);
DATA(UPAUSE);
DATA(dpause);
DATA(DPAUSE);

DATA(RTN);		// Tonic neurons in brainstem - eye position
DATA(RTNDELAY);
DATA(RTNCHANGE);

DATA(LTN);
DATA(LTNDELAY);
DATA(LTNCHANGE);

DATA(UTN);
DATA(UTNDELAY);
DATA(UTNCHANGE);

DATA(DTN);
DATA(DTNDELAY);
DATA(DTNCHANGE);

DATA(RTNDELAY2);	// for dynamic temporal remapping
DATA(RTNDELAY3);
DATA(RTNCHANGE2);

DATA(LTNDELAY2);
DATA(LTNDELAY3);
DATA(LTNCHANGE2);

DATA(UTNDELAY2);
DATA(UTNDELAY3);
DATA(UTNCHANGE2);

DATA(DTNDELAY2);
DATA(DTNDELAY3);
DATA(DTNCHANGE2);


DATA(LMN);		// motor neurons
DATA(RMN);
DATA(UMN);
DATA(DMN);
DATA(EyeHleft);		// eye position
DATA(EyeH);
DATA(EyeV);
DATA(EyeVdown);
DATA(HTHETA);
DATA(VTHETA);
DATA(HV);
DATA(VV);

MATRIX(M,9,9);		// masks for target erasure
MATRIX(M2,9,9);
MATRIX(M3,9,9);


DATA(BURSTERS);		// components that block some visual activity
DATA(saccadebool);	// during saccades
MATRIX(SACCADEMASK,9,9);

MATRIX(retina1,9,9);	// elements in the visual pathway from retina to FEF	
MATRIX(retina,9,9);
MATRIX(RETINA,9,9);
MATRIX(MT,9,9);
MATRIX(V4,9,9);
MATRIX(V2,9,9);
MATRIX(V1,9,9);
MATRIX(LGN,9,9);
MATRIX(PP,9,9);
MATRIX(ppqv,9,9);
MATRIX(PPqva,9,9);
MATRIX(PPqv,9,9);

MATRIX(fef_vis,9,9);	// FEF
MATRIX(FEFvis,9,9);
MATRIX(fef_mem,9,9);
MATRIX(FEFmem,9,9);
MATRIX(fef_sac,9,9);
MATRIX(FEFsac,9,9);

DATA(fefswitch);	// components of dynamic temporal remapping
DATA(FEFSWITCH);
DATA(fefgate);
DATA(FEFGATE);


MATRIX(FovElem,9,9);	// Fovea-On cells
MATRIX(fon,9,9);
MATRIX(FOn,9,9);

MATRIX(cd_mem,9,9);	// Basal Ganglia
MATRIX(CDmem,9,9);
MATRIX(cd_sac,9,9);
MATRIX(CDsac,9,9);

MATRIX(snr_mem,9,9);
MATRIX(SNRmem,9,9);
MATRIX(snr_sac,9,9);
MATRIX(SNRsac,9,9);

MATRIX(qvmask1,9,9);		// components of QVMASK
MATRIX(qvmask2,9,9); 		// some of these are for experimentation
DATA(QVFACTOR); 		// with different kinds of masks - 
DATA(QVctr);			// see the code
DATA(Oblique);
MATRIX(QVMASK,9,9);
DATA(InhSurr);
MATRIX(qv,9,9);
MATRIX(QV,9,9);
MATRIX(sc_qv,9,9);
MATRIX(SCqv,9,9);


MATRIX(sc_sac,9,9);		// Superior Colliculus
MATRIX(SCsac,9,9);
MATRIX(sc_sup,9,9);
MATRIX(SCsup,9,9);
MATRIX(sc,9,9);
MATRIX(sc_wta,9,9);
MATRIX(SC,9,9);
MATRIX(SC_Delay,9,9);


MATRIX(thal_mem,9,9);		// Thalamus
MATRIX(THmem,9,9);


INIT_MODULE(saccade_init)
{
	QVctr = 1;
	QVFACTOR = 1600;
	Oblique = 1;
	LLBN = 0;
	PPqv = 0;
	SC_Delay = 0;

	LTN = 154.0;		// firing rate for central position
	RTN = 154;
	UTN = 154;
	DTN = 154;

	RTNDELAY2 = 154;
	RTNDELAY3 = 154;
	RTNCHANGE2 = 0;

	LTNDELAY2 = 154;
	LTNDELAY3 = 154;
	LTNCHANGE2 = 0;

	UTNDELAY2 = 154;
	UTNDELAY3 = 154;
	UTNCHANGE2 = 0;

	DTNDELAY2 = 154;
	DTNDELAY3 = 154;
	DTNCHANGE2 = 0;

	RTNDELAY = 154;
	LTNDELAY = 154;
	UTNDELAY = 154;
	DTNDELAY = 154;

	fef_vis = 0;  fef_mem = 0; fef_sac = 0;
	FEFvis = 0;   FEFmem = 0;  FEFsac = 0;
	ppqv = 0; PPqva = 0; PPqv = 0;  
	llbn = 0; LLBN = 0;


	SNRmem = 100;
	SNRsac = 100;
}


///////////////////
//saccade generator ala Scudder;  These arrays represent the increased
//density of SC projection to LLBNs as a function of increase eccentricity
// in SC. Function: spatial to temporal transformation.
//////////////////////////  

// long lead burst neurons for left, right up and down

RUN_MODULE(saccade_input) 
{
	INP.run();		// visual input
	STIMULATION.run();	// microelectrode stimulation
}

RUN_MODULE(saccade_llbn)
{
	DIFF.eq(llbn,P.elem(llbn_tm)) = -llbn 
		+ P.elem(llbn_k1)*SC 		// input from SC
		+ P.elem(llbn_k3)*FEFsac;	// and FEF

	llbnwta = NSLwinnertakeall(llbn);
		// the winner take all is what allows a stimulated
		// saccade to interrupt an ongoing saccade - 
		// implies that weighted averageing occurs upstream

		// note that in the double saccades, the llbn (membrane
		// potential) layer sometimes shows activity at multiple sites
}

RUN_MODULE(saccade_LLBN)
{
	LLBN = NSLsaturation(llbnwta,P.elem(LLBN_kx1),P.elem(LLBN_kx2),
			     P.elem(LLBN_ky1),P.elem(LLBN_ky2));
}


RUN_MODULE(saccade_mlbn)
{
// LSTM, RSTM etc have weights that increase with distance from fovea
// performing the SpatioTeMporal transformation
	DIFF.eq(Lmlbn,P.elem(Lmlbn_tm)) = -Lmlbn + (LSTM^LLBN);
	DIFF.eq(Rmlbn,P.elem(Rmlbn_tm)) = -Rmlbn + (RSTM^LLBN);
	DIFF.eq(Umlbn,P.elem(Umlbn_tm)) = -Umlbn + (USTM^LLBN);
	DIFF.eq(Dmlbn,P.elem(Dmlbn_tm)) = -Dmlbn + (DSTM^LLBN);
}
RUN_MODULE(saccade_MLBN)
{
// medium lead burst neurons - see Hepp and Henn (in refs) for details
	LMLBN = NSLsaturation(Lmlbn,P.elem(LMLBN_kx1),P.elem(LMLBN_kx2),
		P.elem(LMLBN_ky1),P.elem(LMLBN_ky2));
	RMLBN = NSLsaturation(Rmlbn,P.elem(RMLBN_kx1),P.elem(RMLBN_kx2),
		P.elem(RMLBN_ky1),P.elem(RMLBN_ky2));
	UMLBN = NSLsaturation(Umlbn,P.elem(UMLBN_kx1),P.elem(UMLBN_kx2),
		P.elem(UMLBN_ky1),P.elem(UMLBN_ky2));
	DMLBN = NSLsaturation(Dmlbn,P.elem(DMLBN_kx1),P.elem(DMLBN_kx2),
		P.elem(DMLBN_ky1),P.elem(DMLBN_ky2));
}




RUN_MODULE(saccade_rebn)
{
// EBNs for left, right up and down - since we did a winner take all
// in the LLBNs, the maximum element is the one we want.  Inhibited
// by pause cells
	DIFF.eq(rebn,P.elem(rebn_tm)) = - rebn + RMLBN.max() -
 		P.elem(rebn_k1)*RPAUSE;
	DIFF.eq(lebn,P.elem(lebn_tm)) = - lebn + LMLBN.max() -
 		P.elem(lebn_k1)*LPAUSE;
	DIFF.eq(uebn,P.elem(uebn_tm)) = - uebn + UMLBN.max() -
 		P.elem(uebn_k1)*UPAUSE;
	DIFF.eq(debn,P.elem(debn_tm)) = - debn + DMLBN.max() -
 		P.elem(debn_k1)*DPAUSE;
}

RUN_MODULE(saccade_REBN)
{
	REBN =
 	  NSLramp(rebn,P.elem(REBN_k1),P.elem(REBN_k2),P.elem(REBN_k3));
	LEBN = 
	  NSLramp(lebn,P.elem(LEBN_k1),P.elem(LEBN_k2),P.elem(LEBN_k3));
	UEBN = 
	  NSLramp(uebn,P.elem(UEBN_k1),P.elem(UEBN_k2),P.elem(UEBN_k3));
	DEBN = 
	  NSLramp(debn,P.elem(DEBN_k1),P.elem(DEBN_k2),P.elem(DEBN_k3));
}



RUN_MODULE(saccade_pause)
// the trigger cells get input from FEF and SC.  Once the saccade begins,
// these cells are inhibited so that residual activity in FEF and SC won't
// prevent short saccades from ending

{
//  right
	DIFF.eq(rtrig,P.elem(rtrig_tm)) = -rtrig + (RSTM^SC)  +
 		(RSTM^FEFsac) - P.elem(rtrig_k1)*RMASK;

	DIFF.eq(rpause,P.elem(rpause_tm)) = -rpause + 
		P.elem(rpause_k1)*RTRIGN + DELTAR - P.elem(pause_k1)*RRI;

	rri = RRI + P.elem(rri_k1)*REBN - RPAUSE;

	deltar = (RMASK^RWEIGHTS);  // RWEIGHTS has values that increase
		// as you move to the periphery.  RMASK has a constant
		// value once the inputs are above threshold.  The result
		// is that the spatial FEF and SC signal gets converted into
		// delatar which is used to produce DELTAR which is compared
		// to the resetable integrator to terminate the saccade

//  left
	DIFF.eq(ltrig,P.elem(ltrig_tm)) = -ltrig + (LSTM^SC) + 
 		(LSTM^FEFsac) - P.elem(ltrig_k1)*LMASK;

	DIFF.eq(lpause,P.elem(lpause_tm)) = -lpause + 
		P.elem(lpause_k1) *LTRIGN + DELTAL - P.elem(pause_k1)*LRI;

	lri = LRI + P.elem(lri_k1)*LEBN - LPAUSE;
	deltal = (LMASK^LWEIGHTS);

//  up
	DIFF.eq(utrig,P.elem(utrig_tm)) = -utrig + (USTM^SC) + 
 		(USTM^FEFsac) - P.elem(utrig_k1)*UMASK;

	DIFF.eq(upause,P.elem(upause_tm)) = -upause + 
		P.elem(upause_k1) *UTRIGN + DELTAU - P.elem(pause_k1)*URI;

	uri = URI + P.elem(uri_k1)*UEBN - UPAUSE;
	deltau = (UMASK^UWEIGHTS);

//  down
	DIFF.eq(dtrig,P.elem(dtrig_tm)) = -dtrig + (DSTM^SC) +
 		(DSTM^FEFsac) - P.elem(dtrig_k1)*DMASK;

	DIFF.eq(dpause,P.elem(dpause_tm)) = -dpause + 
		P.elem(dpause_k1) *DTRIGN + DELTAD - P.elem(pause_k1)*DRI;

	dri = DRI + P.elem(dri_k1) *DEBN - DPAUSE;
	deltad = (DMASK^DWEIGHTS);
}

RUN_MODULE(saccade_PAUSE)
{
//  right	
	RTRIG.elem() = rtrig.max();
	RTRIGN = NSLramp(RTRIG,P.elem(RTRIGN_k1),
		 P.elem(RTRIGN_k2),P.elem(RTRIGN_k3));
		// this thresholding allows us to manipulate the accuracy
		// of the saccade by specifying how close DELTA and RI
		// must be to terminate the saccade (triger the pause cells)

	RRI = NSLramp(rri,P.elem(RRI_k1),
		P.elem(RRI_k2),P.elem(RRI_k3));

	RMASK = NSLstep(RMLBN,P.elem(RMASK_k1),
		P.elem(RMASK_k2),P.elem(RMASK_k3));
		// fires at a fixed rate when LLBNs above threshold.
		// used to get the DELTA

	DELTARI = deltar.max() - P.elem(DELTARI_k1)*RPAUSE;

	DELTAR = NSLramp(DELTARI,P.elem(DELTAR_k1),
		P.elem(DELTAR_k2),P.elem(DELTAR_k3));
	
	RPAUSE = NSLstep(rpause,P.elem(RPAUSE_k1),
		 P.elem(RPAUSE_k2),P.elem(RPAUSE_k3));


//  left	
	LTRIG.elem() = ltrig.max();

	LTRIGN = NSLramp(LTRIG,P.elem(LTRIGN_k1),
		P.elem(LTRIGN_k2),P.elem(LTRIGN_k3));

	LRI = NSLramp(lri,P.elem(LRI_k1),
		P.elem(LRI_k2),P.elem(LRI_k3));

	LMASK = NSLstep(LMLBN,P.elem(LMASK_k1),
		P.elem(LMASK_k2),P.elem(LMASK_k3));

	DELTALI = deltal.max() - P.elem(DELTALI_k1)*LPAUSE;

	DELTAL = NSLramp(DELTALI,P.elem(DELTAL_k1),
		P.elem(DELTAL_k2),P.elem(DELTAL_k3));

	LPAUSE = NSLstep(lpause,P.elem(LPAUSE_k1),
		P.elem(LPAUSE_k2),P.elem(LPAUSE_k3));


//  up	
	UTRIG.elem() = utrig.max();
	UTRIGN = NSLramp(UTRIG,P.elem(UTRIGN_k1),
		P.elem(UTRIGN_k2),P.elem(UTRIGN_k3));

	URI = NSLramp(uri,P.elem(URI_k1),
		P.elem(URI_k2),P.elem(URI_k3));

	UMASK = NSLstep(UMLBN,P.elem(UMASK_k1),
		P.elem(UMASK_k2),P.elem(UMASK_k3));
	
	DELTAUI = deltau.max() - P.elem(DELTAUI_k1)*UPAUSE;

	DELTAU = NSLramp(DELTAUI,P.elem(DELTAU_k1),
		P.elem(DELTAU_k2),P.elem(DELTAU_k3));

	UPAUSE = NSLstep(upause,P.elem(UPAUSE_k1),
		P.elem(UPAUSE_k2),P.elem(UPAUSE_k3));



//  down	
	DTRIG.elem() = dtrig.max();
	DTRIGN = NSLramp(DTRIG,P.elem(DTRIGN_k1),
		P.elem(DTRIGN_k2),P.elem(DTRIGN_k3));

	DRI = NSLramp(dri,P.elem(DRI_k1),
		P.elem(DRI_k2),P.elem(DRI_k3));

	DMASK = NSLstep(DMLBN,P.elem(DMASK_k1),
		P.elem(DMASK_k2),P.elem(DMASK_k3));

	DELTADI = deltad.max() - P.elem(DELTADI_k1)*DPAUSE;

	DELTAD = NSLramp(DELTADI,P.elem(DELTAD_k1),
		P.elem(DELTAD_k2),P.elem(DELTAD_k3));

	DPAUSE = NSLstep(dpause,P.elem(DPAUSE_k1),
		P.elem(DPAUSE_k2),P.elem(DPAUSE_k3));
}




RUN_MODULE(saccade_temporal_remapping)
{
	fefswitch.elem() = P.elem(fefswitch_k1)*FEFsac.max();
	DIFF.eq(fefgate,P.elem(fefgate_tm)) = - fefgate + FEFSWITCH;
}

RUN_MODULE(saccade_TEMPORAL_REMAPPING)
{
	FEFSWITCH = NSLstep(fefswitch,P.elem(FEFSWITCH_k1),
		P.elem(FEFSWITCH_k2),P.elem(FEFSWITCH_k3));

	FEFGATE = NSLstep(fefgate,P.elem(FEFGATE_k1),
		P.elem(FEFGATE_k2),P.elem(FEFGATE_k3));

	DIFF.eq(RTNDELAY2,P.elem(RTNDELAY2_tm)) = - RTNDELAY2 + RTN;
	DIFF.eq(RTNDELAY3,P.elem(RTNDELAY3_tm)) = - RTNDELAY3 + RTN;
	DIFF.eq(RTNCHANGE2,P.elem(RTNCHANGE2_tm)) = - RTNCHANGE2 +
		 RTNDELAY2 - RTNDELAY3;

	DIFF.eq(LTNDELAY2,P.elem(LTNDELAY2_tm)) = - LTNDELAY2 + LTN;
	DIFF.eq(LTNDELAY3,P.elem(LTNDELAY3_tm)) = - LTNDELAY3 + LTN;
	DIFF.eq(LTNCHANGE2,P.elem(LTNCHANGE2_tm)) = - LTNCHANGE2 +
		 LTNDELAY2 - LTNDELAY3;

	DIFF.eq(UTNDELAY2,P.elem(UTNDELAY2_tm)) = - UTNDELAY2 + UTN;
	DIFF.eq(UTNDELAY3,P.elem(UTNDELAY3_tm)) = - UTNDELAY3 + UTN;
	DIFF.eq(UTNCHANGE2,P.elem(UTNCHANGE2_tm)) = - UTNCHANGE2 +
		 UTNDELAY2 - UTNDELAY3;

	DIFF.eq(DTNDELAY2,P.elem(DTNDELAY2_tm)) = - DTNDELAY2 + DTN;
	DIFF.eq(DTNDELAY3,P.elem(DTNDELAY3_tm)) = - DTNDELAY3 + DTN;
	DIFF.eq(DTNCHANGE2,P.elem(DTNCHANGE2_tm)) = - DTNCHANGE2 + 
		DTNDELAY2 - DTNDELAY3;

}


//from (Robinson, 1970) we have the following for firing rate, D, as a
//function of location (theta) and velocity (d.theta/d.t):
// 		D = k*theta + r*d.theta/d.t + c
//		  = 2.75*theta + 0.9* d.theta/d.t + 154;
// this simplified linear sytem for the mapping of oculomotor neuron firing
// rates to location and velocity of eye will be used in our preliminary
// model. 



// MOTOR NEURONS
RUN_MODULE(saccade_MN)
{
	LMN = LEBN + LTN;
	RMN = REBN + RTN;
	UMN = UEBN + UTN;
	DMN = DEBN + DTN;
}


	//NOTE: this represents gaze angle, and is not used in the
	//model, but only as an indicator of eye position for experimenter

RUN_MODULE(saccade_EYE_POSITION_AND_VELOCITY) 


{
	EyeH = P.elem(EyeH_k2)*RTN - 56;
	EyeV = P.elem(EyeV_k2)*UTN - 56; 

	EyeVdown = P.elem(EyeVdown_k2)*DTN - 56;
	EyeHleft = P.elem(EyeHleft_k2)*LTN - 56;

	VV = P.elem(VV_k1)*UEBN - P.elem(VV_k1)*DEBN + 
		P.elem(VV_k2)*UTNCHANGE - P.elem(VV_k3)*DTNCHANGE;

	HV = P.elem(HV_k1)*REBN - P.elem(HV_k1)*LEBN + 
		P.elem(HV_k2)*RTNCHANGE - P.elem(HV_k3)*LTNCHANGE;
	HTHETA = P.elem(HTHETA_k1)*EyeH;
	VTHETA = P.elem(VTHETA_k1)*EyeV;
}

// CHANGE IN POSITION FOR QV SHIFT

RUN_MODULE(saccade_tonic_changes)
{
	DIFF.eq(RTNDELAY,P.elem(RTNDELAY_tm)) = - RTNDELAY + RTN;
	DIFF.eq(LTNDELAY,P.elem(LTNDELAY_tm)) = - LTNDELAY + LTN;
	DIFF.eq(UTNDELAY,P.elem(UTNDELAY_tm)) = - UTNDELAY + UTN;
	DIFF.eq(DTNDELAY,P.elem(DTNDELAY_tm)) = - DTNDELAY + DTN;
}

RUN_MODULE(saccade_TN)
{
//on each timestep,the tonic component uppdated by 2.75*(change in theta).
// Change in theta is angular velocity * time =
//			   EBN*(1/R)  * (simuation time step)
	LTN = LTN + P.elem(LTN_k1)*LEBN - P.elem(LTN_k1)*REBN - 
		FEFGATE*LTNCHANGE2;
	RTN = RTN + P.elem(RTN_k1)*REBN - P.elem(RTN_k1)*LEBN - 
		FEFGATE*RTNCHANGE2;
	UTN = UTN + P.elem(UTN_k1)*UEBN - P.elem(UTN_k1)*DEBN -
		FEFGATE*UTNCHANGE2;
	DTN = DTN + P.elem(DTN_k1)*DEBN - P.elem(LTN_k1)*UEBN -
		FEFGATE*DTNCHANGE2;
}



RUN_MODULE(saccade_TONIC_CHANGES)
{
	RTNCHANGE = RTN - RTNDELAY;
	LTNCHANGE = LTN - LTNDELAY;
	UTNCHANGE = UTN - UTNDELAY;
	DTNCHANGE = DTN - DTNDELAY;
}

RUN_MODULE(saccade_BURSTERS)
{
BURSTERS = UEBN + DEBN + LEBN + REBN;
saccadebool = NSLstep(BURSTERS,P.elem(saccadebool_k1),
	      P.elem(saccadebool_k2),P.elem(saccadebool_k3));
SACCADEMASK = saccadebool;
}

RUN_MODULE(saccade_retina)
{
	retina1 = NSLeyemove(INP,HTHETA,VTHETA);
	DIFF.eq(retina,P.elem(retina_tm)) = - retina + retina1;
}

RUN_MODULE(saccade_RETINA)
{
	RETINA = (SACCADEMASK^retina);
}


RUN_MODULE(saccade_VISCORTEX)
{
	DIFF.eq(PP,P.elem(PP_tm)) = - PP + MT;
	DIFF.eq(MT,P.elem(MT_tm)) = - MT + V4;
	DIFF.eq(V4,P.elem(V4_tm)) = - V4 + V2;
	DIFF.eq(V2,P.elem(V2_tm)) = - V2 + V1;
	DIFF.eq(V1,P.elem(V1_tm)) = - V1 + LGN;
	DIFF.eq(LGN,P.elem(LGN_tm)) = - LGN + RETINA;
}

RUN_MODULE(saccade_M)
{
	M.elem(4,4) = 1;

	M3 = P.elem(M3_k1) * M2;	
}


RUN_MODULE(saccade_PPqv)
{
	qvmask1 = NSLshift(HV,VV,QVFACTOR);

//	qvmask2 = NSLX2shift(HV,VV,QVFACTOR,QVctr,Oblique);

	QVMASK = P.elem(QVMASK_k1)*qvmask1 + P.elem(QVMASK_k2)*qvmask2 ;

	QVMASK.elem(2,2) = InhSurr.elem(); QVMASK.elem(2,3) = InhSurr.elem();
	QVMASK.elem(2,4) = InhSurr.elem(); QVMASK.elem(2,5) = InhSurr.elem();
	QVMASK.elem(2,6) = InhSurr.elem(); QVMASK.elem(3,6) = InhSurr.elem();
	QVMASK.elem(4,6) = InhSurr.elem(); QVMASK.elem(5,6) = InhSurr.elem();
	QVMASK.elem(6,2) = InhSurr.elem(); QVMASK.elem(6,3) = InhSurr.elem();
	QVMASK.elem(6,4) = InhSurr.elem(); QVMASK.elem(6,5) = InhSurr.elem();
	QVMASK.elem(6,6) = InhSurr.elem(); QVMASK.elem(3,2) = InhSurr.elem();
	QVMASK.elem(4,2) = InhSurr.elem(); QVMASK.elem(5,2) = InhSurr.elem();


	ppqv = PP  + QVMASK*PPqva - 
		P.elem(ppqv_k1)*FovElem -
		P.elem(ppqv_k2)*(M2*SC_Delay);

	ppqv = M2^ppqv; 

	PPqva = NSLsigma(ppqv,P.elem(PPqv_kx1),P.elem(PPqv_kx2),
			P.elem(PPqv_ky1),P.elem(PPqv_ky2));

	PPqv = PPqva;
	PPqv.elem(4,4) = 0; 	
}


RUN_MODULE(saccade_fef)
{

	DIFF.eq(fef_vis,P.elem(fef_vis_tm))= - fef_vis + PPqv;


	DIFF.eq(fef_mem,P.elem(fef_mem_tm)) = - fef_mem + 
		P.elem(fef_mem_k4)*THmem + 
		P.elem(fef_mem_k2)*FEFvis -
		P.elem(fef_mem_k1)*FOn;

	DIFF.eq(fef_sac,P.elem(fef_sac_tm)) = - fef_sac +
		P.elem(fef_sac_k1)*FEFvis +
		P.elem(fef_sac_k2)*FEFmem - 
		P.elem(fef_sac_k3)*FOn;

	fef_sac.elem(4,4) = 0;
}


RUN_MODULE(saccade_FEF)
{

	FEFvis = NSLsigma(fef_vis,P.elem(FEFvis_x1),P.elem(FEFvis_x2),
		P.elem(FEFvis_y1),P.elem(FEFvis_y2));

	FEFmem = NSLsigma(fef_mem,P.elem(FEFmem_x1),P.elem(FEFmem_x2),
		P.elem(FEFmem_y1),P.elem(FEFmem_y2));

	FEFsac = NSLsigma(fef_sac,P.elem(FEFsac_x1),P.elem(FEFsac_x2),
		P.elem(FEFsac_y1),P.elem(FEFsac_y2)) +
		P.elem(FEFsac_k1) * STIMULATION ;
}


RUN_MODULE(saccade_FOn)
{
	fon = 	P.elem(fon_k1) * PP.elem(4,4); 

	FOn = NSLstep(fon,P.elem(FOn_x1),P.elem(FOn_y1),
			P.elem(FOn_y2));

	FovElem.elem(4,4) = PP.elem(4,4); 
}

RUN_MODULE(saccade_caudate)
{

	DIFF.eq(cd_mem,P.elem(cd_mem_tm)) = - cd_mem + 
		P.elem(cd_mem_k1)*FEFmem;

	DIFF.eq(cd_sac,P.elem(cd_sac_tm)) = - cd_sac +
		P.elem(cd_sac_k1)*FEFsac;
}

RUN_MODULE(saccade_CAUDATE)
{

	CDmem = NSLsigma(cd_mem,P.elem(CDmem_x1),P.elem(CDmem_x2),
		P.elem(CDmem_y1),P.elem(CDmem_y2));

	CDsac = NSLsigma(cd_sac,P.elem(CDsac_x1),P.elem(CDsac_x2),
		P.elem(CDsac_y1),P.elem(CDsac_y2));

}

RUN_MODULE(saccade_snr)
{
	DIFF.eq(snr_sac,P.elem(snr_sac_tm)) = - snr_sac + 
			P.elem(snr_sac_k1)*CDsac; 

	DIFF.eq(snr_mem,P.elem(snr_mem_tm)) = - snr_mem + 
			P.elem(snr_mem_k1)*CDmem;
}

RUN_MODULE(saccade_SNR)
{
	SNRsac = NSLsigma(snr_sac,P.elem(SNRsac_x1),P.elem(SNRsac_x2),
			P.elem(SNRsac_y1),P.elem(SNRsac_y2));

	SNRmem = NSLsigma(snr_mem,P.elem(SNRmem_x1),P.elem(SNRmem_x2),
			P.elem(SNRmem_y1),P.elem(SNRmem_y2));
}

RUN_MODULE(saccade_sc)  
{
	DIFF.eq(sc_sup,P.elem(sc_sup_tm)) = - sc_sup -
		 P.elem(sc_sup_k1)*FOn + P.elem(sc_sup_k2)*RETINA;


	DIFF.eq(sc_qv,P.elem(sc_qv_tm)) = 
			-sc_qv + NSLwinnertakeall(PPqv);

	DIFF.eq(sc_sac,P.elem(sc_sac_tm)) = -sc_sac +  
		P.elem(sc_sac_k1)*FEFsac - 
		P.elem(sc_sac_k2)*SNRsac;

	DIFF.eq(sc,P.elem(sc_tm)) = -sc  + 
		P.elem(sc_k2)*SCsac + 
		P.elem(sc_k3)*SCqv -
		P.elem(sc_k4)*FOn  + 
		P.elem(sc_k5)*STIMULATION +
		P.elem(sc_k6)*SCsup - 
		P.elem(sc_k1)*SC_Delay; // this is zero.

	sc.elem(4,4) = 0; // no saccades to where we already are! //

	sc_wta = NSLwinnertakeall(sc);
}

RUN_MODULE(saccade_SC)
{
	SCsup = NSLsigma(sc_sup,P.elem(SCsup_x1),P.elem(SCsup_x2),
		P.elem(SCsup_y1),P.elem(SCsup_y2));

	SCqv = (SACCADEMASK^sc_qv);

	SCsac = NSLsigma(sc_sac,P.elem(SCsac_x1),P.elem(SCsac_x2),
		P.elem(SCsac_y1),P.elem(SCsac_y2));

	SC = NSLsigma(sc_wta,P.elem(SC_x1),P.elem(SC_x2),
		P.elem(SC_y1),P.elem(SC_y2)) + P.elem(SC_k3)*STIMULATION;

	DIFF.eq(SC_Delay,P.elem(SC_Delay_tm)) = -SC_Delay + SC;
}

RUN_MODULE(saccade_thal)
{
	DIFF.eq(thal_mem,P.elem(thal_mem_tm)) = - thal_mem +
		P.elem(thal_mem_k3)*FEFmem - 
		P.elem(thal_mem_k1)*SNRmem -
		P.elem(thal_mem_k2)*(M2*SC_Delay);
	thal_mem.elem(4,4) = 0; // the " - FOVEA" term - so we don't 	
				// "remember" the fixation point
}

RUN_MODULE(saccade_THAL)
{
	THmem = NSLsigma(thal_mem,P.elem(THmem_x1),P.elem(THmem_x2),
		P.elem(THmem_y1),P.elem(THmem_y2));
}


RUN_MODULE(saccade_SPATIO_TEMPORAL_TRANS)
{
LTOPO.elem(0) = 5.7;
LTOPO.elem(1) = 4.275;
LTOPO.elem(2) = 2.85;
LTOPO.elem(3) = 1.425;

RTOPO.elem(5) = 1.425;
RTOPO.elem(6) = 2.85;
RTOPO.elem(7) = 4.275;
RTOPO.elem(8) = 5.7;

UTOPO.elem(0) = 5.7;
UTOPO.elem(1) = 4.275;
UTOPO.elem(2) = 2.85;
UTOPO.elem(3) = 1.425;

DTOPO.elem(5) = 1.425;
DTOPO.elem(6) = 2.85;
DTOPO.elem(7) = 4.275;
DTOPO.elem(8) = 5.7;


}

RUN_MODULE(saccade_BRAINSTEM_LAYERS)
{
LWEIGHTS = NSLrow_vec_to_mat(LWEIGHTS,LTOPO);
RWEIGHTS = NSLrow_vec_to_mat(RWEIGHTS,RTOPO);
UWEIGHTS = NSLcol_vec_to_mat(UWEIGHTS,UTOPO);
DWEIGHTS = NSLcol_vec_to_mat(DWEIGHTS,DTOPO);
LSTM = P.elem(LSTM_K1)*NSLrow_vec_to_mat(LSTM,LTOPO);
RSTM = P.elem(RSTM_K1)*NSLrow_vec_to_mat(RSTM,RTOPO);
USTM = P.elem(USTM_K1)*NSLcol_vec_to_mat(USTM,UTOPO);
DSTM = P.elem(DSTM_K1)*NSLcol_vec_to_mat(DSTM,DTOPO);
}



