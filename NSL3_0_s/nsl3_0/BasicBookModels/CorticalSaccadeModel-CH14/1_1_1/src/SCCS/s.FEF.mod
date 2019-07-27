h01248
s 00001/00001/00161
d D 1.2 99/09/22 23:20:56 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00162/00000/00000
d D 1.1 99/09/22 22:42:47 aalx 1 0
c date and time created 99/09/22 22:42:47 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*FEF
* Frontal Eye Field Submodule for Saccades
* @see FEF.mod
* @version 98/6/18
* @author Dominey: coder Alexander
*
*/
nslImport nslAllImports;

nslModule FEF (int stdsz)  {

// port inputs
public NslDinFloat2 ppqv (stdsz,stdsz);
public NslDinFloat2 thmem(stdsz,stdsz);
public NslDinFloat2 fon(stdsz,stdsz);
public NslDinFloat2 stimulation(stdsz,stdsz);

// port outputs
public NslDoutFloat2 fefvis(stdsz,stdsz);  
public NslDoutFloat2 fefmem(stdsz,stdsz);
public NslDoutFloat2 fefsac(stdsz,stdsz);
// parameters 
private NslFloat0 fefmemPot_tm();
private NslFloat0 fefvisPot_tm();
private NslFloat0 fefsacPot_tm();
private NslFloat0 fefmemPot_k1();
private NslFloat0 fefmemPot_k2();
private NslFloat0 fefmemPot_k4();
private NslFloat0 fefsacPot_k1();
private NslFloat0 fefsacPot_k2();
private NslFloat0 fefsacPot_k3();
private NslFloat0 fefvisPot_k1();
private NslFloat0 fefvisPot_k2();
private NslFloat0 fefvis_x1();
private NslFloat0 fefvis_x2();
private NslFloat0 fefvis_y1();
private NslFloat0 fefvis_y2();
private NslFloat0 fefmem_x1();
private NslFloat0 fefmem_x2();
private NslFloat0 fefmem_y1();
private NslFloat0 fefmem_y2();
private NslFloat0 fefsac_x1();
private NslFloat0 fefsac_x2();
private NslFloat0 fefsac_y1();
private NslFloat0 fefsac_y2();
private NslFloat0 fefsac_k1();

//vars
private NslFloat2 fefvisPot(stdsz,stdsz);
private NslFloat2 fefmemPot(stdsz,stdsz);
private NslFloat2 fefsacPot(stdsz,stdsz);
private NslFloat2 fefsactmp(stdsz,stdsz);

private NslInt0 protocolNum();
private int center;


public void initModule() {	
	fefsac.nslSetAccess('W');
	fefmemPot_k2.nslSetAccess('W');
}

public void initRun() {
       center = (int) stdsz/2;

D 2
       protocolNum = (NslInt0)nslGetValue("DomineyModel.protocolNum");
E 2
I 2
       protocolNum = (NslInt0)nslGetValue("domineyModel.protocolNum");
E 2

       fefvis=0;
       fefmem=0;
       fefsac=0;
       fefvisPot=0;
       fefmemPot=0;
       fefsacPot=0;
       fefsactmp=0;

	fefmemPot_tm= .008 ;
	fefvisPot_tm= .006 ; //xxx???AA
	fefsacPot_tm=  .008;
	fefmemPot_k1= 0.2 ;
	fefmemPot_k2= 0 ;
	//98/12/10 aa: not in 2.1.7  :doing a memory protocolNum
	if ((protocolNum==2)||(protocolNum==3)||(protocolNum==8)||(protocolNum==15)) {	
		fefmemPot_k2= 1 ;  //was lost in NSL2.1.7 nsl file
	}
	fefmemPot_k4= 8 ;

	fefsacPot_k1=  1;
	fefsacPot_k2=  2;
	fefsacPot_k3=  3;
	fefvisPot_k1=  0;
	fefvisPot_k2=  1;
	fefvis_x1=  0;
	fefvis_x2= 90 ;
	fefvis_y1=  0;
	fefvis_y2=90  ;
	fefmem_x1= 0 ;
	fefmem_x2=  90;
	fefmem_y1= 0 ;
	fefmem_y2=90  ;
	fefsac_x1=  80; //AA: in 2.1.7 but whynot 0?
	fefsac_x2= 90 ;
	fefsac_y1=  0;
	fefsac_y2= 90 ;
	
 	//XX 98/11/18 aa: fefsac_k1 depeneds on protocolNum 
	// fefsac_k1*stimulation:
	// simple,double = 0
	// collision  = 4.8 why?
	// compensatoryI = 2.5 why?  Published doc says: 1.58
	// compensatoryII = nothing stated. Published doc says: 1.58
	// If we look at the SC equations: supcol_k3 then this
	// fefsac_k1 for compII should be stronger than for compI case.
	// For this reason we will set it to 3.5
	fefsac_k1=0;  // if stimulation is not used on FEF in most protocolNums
	// if stimulation used
	if ((protocolNum==11)||( protocolNum==13)) {
		fefsac_k1=2.5;  // 
		// Thesis says fefsac_k1= 1.58 at 175 hz for 40 msec
		// for compensatory protocolNums but in the potential equation
	}
	if (protocolNum==12) {
		fefsac_k1=3.5;
	}
}
public void simRun() {

	fefvisPot=nslDiff(fefvisPot,fefvisPot_tm, (- fefvisPot + ppqv));
	fefmemPot=nslDiff(fefmemPot,fefmemPot_tm,( - fefmemPot + fefmemPot_k4*thmem + fefmemPot_k2*fefvis -fefmemPot_k1*fon));
	fefsacPot=nslDiff(fefsacPot,fefsacPot_tm,( - fefsacPot +fefsacPot_k1*fefvis +fefsacPot_k2*fefmem - fefsacPot_k3*fon));
	// aa: note: the published doc addes the fefsac_k1*stimulation 
	// in this equations (fefsacPot) instead of the fefsac equation below.

	fefsacPot[center][center] = 0;
	fefvis = nslSigmoid(fefvisPot,fefvis_x1,fefvis_x2,fefvis_y1,fefvis_y2);

	fefmem = nslSigmoid(fefmemPot,fefmem_x1,fefmem_x2,fefmem_y1,fefmem_y2);

	fefsactmp = nslSigmoid(fefsacPot,fefsac_x1,fefsac_x2,fefsac_y1,fefsac_y2); 
	fefsac = fefsactmp + (fefsac_k1*stimulation);

	//98/12/8 aa: from the 92 paper, set fefsac to 0 in equation 13 for
	// lesioning of fef
	if ((protocolNum==7)||(protocolNum==14)) {//lesion fef
		fefsac=0;  
	}

	if (system.debug>=18) {
	nslPrintln("simRun: FEF: fefsac:");
	nslPrintln(fefsac);
	nslPrintln("simRun: FEF: fefmem:");
	nslPrintln(fefmem);
	nslPrintln("simRun: FEF: fefvis:");
	nslPrintln(fefvis);
	}
}
}




E 1
