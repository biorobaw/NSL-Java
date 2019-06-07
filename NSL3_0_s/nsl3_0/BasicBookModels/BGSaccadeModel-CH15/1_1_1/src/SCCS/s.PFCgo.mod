h05133
s 00061/00000/00000
d D 1.1 99/09/24 18:57:24 aalx 1 0
c date and time created 99/09/24 18:57:24 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)PFCgo.mod	1.8 --- 08/05/99 -- 13:56:31 : jversion  @(#)PFCgo.mod	1.2---04/23/99--18:39:39 */
/* $lOG$ */

nslImport nslAllImports;

//
// PFCgo
//
nslModule PFCgo(int array_size) {
   // input ports
    public NslDinDouble2 LIPmem_in(array_size,array_size);
    public NslDinDouble2 PFCseq_in(array_size,array_size);
    public NslDinDouble2 PFCfovea_in(array_size,array_size);
   // output ports
    public NslDoutDouble2 PFCgo_out (array_size,array_size)  ;
    public NslDouble2 pfcgo(array_size,array_size)   ;

  // private variables

  private double           pfcgotm;
  private double           basepfcgotm;
  private double           CorticalSlowdown;
  private double           PFCgosigma1;
  private double           PFCgosigma2;
  private double           PFCgosigma3;
  private double           PFCgosigma4;
   static private double PFCseqK=  0.9; 
   static private double PFClipmem  =  0.35;
  
public void initRun(){
//    PFClipmem      =  0.35; 
//    PFCseqK        =  0.9;

    pfcgo = 0.0;
    PFCgo_out = 0.0;
    basepfcgotm = 0.008;
    CorticalSlowdown = 1.0;
    pfcgotm = basepfcgotm * CorticalSlowdown;
    PFCgosigma1 = 20.0;
    PFCgosigma2 = 60.0;
    PFCgosigma3 = 0.0;
    PFCgosigma4 = 60.0;
}

public void simRun(){

  // System.err.println("@@@@ PFCgo simRun entered @@@@");
    /* <Q> PFClipmem PFCseqK */
    pfcgo = nslDiff(pfcgo,pfcgotm,
		       -pfcgo
		       + (PFClipmem * LIPmem_in)
		       + (PFCseqK * PFCseq_in)
		       - PFCfovea_in);
    PFCgo_out = Nsl2Sigmoid.eval(pfcgo,PFCgosigma1, PFCgosigma2,
				  PFCgosigma3, PFCgosigma4);
	// 96/12/20 aa
	//System.out.println("PFCgo_out: " + PFCgo_out);
  }

} // end class
E 1
