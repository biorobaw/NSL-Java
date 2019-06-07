h17795
s 00103/00000/00000
d D 1.1 99/09/24 18:57:33 aalx 1 0
c date and time created 99/09/24 18:57:33 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)Thal.mod	1.8 --- 08/05/99 -- 13:56:47 : jversion  @(#)Thal.mod	1.2---04/23/99--18:33:17 */

nslImport nslAllImports;

//Submodule: Thalamus
//membranes
//
//ThPFCmem
//

nslModule Thal(int array_size) {
//inputs
    	public NslDinDouble2 SNRmedburst_in(array_size,array_size)   ;
    	public NslDinDouble2 PFCmem_in(array_size,array_size)   ;
    	public NslDinDouble2 FEFmem_in(array_size,array_size)   ;
    	public NslDinDouble2 LIPmem_in(array_size,array_size)   ;
//outputs
	public NslDoutDouble2 ThPFCmem_out(array_size,array_size)   ;
	public NslDoutDouble2 ThFEFmem_out(array_size,array_size)   ;
	public NslDoutDouble2 ThLIPmem_out(array_size,array_size)   ;
	public NslDoutDouble2 ThMEDlcn_out(array_size,array_size)   ;
	public NslDoutDouble2 RNMEDinh_out(array_size,array_size)   ;

//private variables
static  private int    THBurstRate=60;
static  private int    THBurstLevel=15;
static  private double DecayRate   = 0.9;

    // module instances
private	ThPFCmem thpfc(array_size)  ;
private	ThFEFmem thfef(array_size)   ;	
private	ThLIPmem thlip(array_size)   ;
private	ThMEDlcn thmed(array_size)   ;
private	RNMEDinh rnmed(array_size)   ;
private        NslDouble2 THNewActivation (array_size,array_size);

public void makeConn(){
    // inputs
    	nslRelabel(SNRmedburst_in,thpfc.SNRmedburst_in);
    	nslRelabel(SNRmedburst_in,thfef.SNRmedburst_in);
    	nslRelabel(SNRmedburst_in,thlip.SNRmedburst_in);
    	nslRelabel(SNRmedburst_in,rnmed.SNRmedburst_in);
    	nslRelabel(PFCmem_in,thpfc.PFCmem_in);
    	nslRelabel(FEFmem_in,thfef.FEFmem_in);
    	nslRelabel(LIPmem_in,thlip.LIPmem_in);

	// privates
	nslConnect(thmed.ThMEDlcn_out,thpfc.ThMEDlcn_in); 
	nslConnect(thmed.ThMEDlcn_out,thfef.ThMEDlcn_in); 
	nslConnect(thmed.ThMEDlcn_out,thlip.ThMEDlcn_in); 
	nslConnect(rnmed.RNMEDinh_out,thmed.RNMEDinh_in); 

        // outputs
	nslRelabel(thpfc.ThPFCmem_out,ThPFCmem_out);
	nslRelabel(thfef.ThFEFmem_out,ThFEFmem_out);
	nslRelabel(thlip.ThLIPmem_out,ThLIPmem_out);
	nslRelabel(thmed.ThMEDlcn_out,ThMEDlcn_out);
	nslRelabel(rnmed.RNMEDinh_out,RNMEDinh_out);
  }

  public void initRun(){
    THNewActivation = 0;
  }

  public void simRun(){
    //    THNewActivation = THCheckBurst(SNRmedburst);
    // System.err.println("@@@@ Thal simRun entered @@@@");
     THCheckBurst(THNewActivation, SNRmedburst_in);
  }

  /**
    Uses  bursting constant to casue activation in neuorns
    undergoing remapping. This is indicated by a decrease in 
    inhibition below a certain threshold.
    */

public  void  THCheckBurst(NslDouble2 THNewActivation, NslDouble2 SNRmedburst_in) {
    double[][] inmat = SNRmedburst_in.get();
    double[][] outmat = THNewActivation.get();
    int size1 = inmat.length;
    int size2 = inmat[0].length;

    if(size1!=outmat.length || size2!=outmat[0].length) {
      System.err.println("THCheckBurst: array size not match\n"+
			 "THNewActivation ("+size1+"x"+size2+
			 ") SNRmedburst_in ("+inmat.length+"x"+
			 inmat[0].length+")");
    }
    for (int i=0; i<size1; i++) {
      for(int j=0; j<size2; j++) {
	if (inmat[i][j]<THBurstLevel)
	  outmat[i][j]=THBurstRate;
	else 
          outmat[i][j]=outmat[i][j]*DecayRate;
      }
    }

  }
	
} //end class


E 1
