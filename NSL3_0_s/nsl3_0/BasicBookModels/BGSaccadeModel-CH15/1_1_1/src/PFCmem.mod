/* SCCS  @(#)PFCmem.mod	1.1---09/24/99--18:57:25 */
/* old kversion @(#)PFCmem.mod	1.8 --- 08/05/99 -- 13:56:32 : jversion  @(#)PFCmem.mod	1.2---04/23/99--18:39:39 */


nslImport nslAllImports;

//
// PFCmem
//
nslModule PFCmem(int array_size){
    // input ports
    public NslDinDouble2 ThPFCmem_in()   ;
    public NslDinDouble2 LIPmem_in()   ;
    public NslDinDouble2 pfcseq_in()   ;

    // output ports
    public NslDoutDouble2 PFCmem_out(array_size,array_size)   ;


  // private variables

   private NslDouble2 pfcmem(array_size,array_size)   ;
   private double  pfcmemtm;
   private double  pfcmemK1;
   private double  pfcmemK2;
   private double  pfcseqK;
   private double  PFCmemsigma1;
   private double  PFCmemsigma2;
   private double  PFCmemsigma3;
   private double  PFCmemsigma4;



public void initRun(){
    PFCmem_out = 0.0;
    pfcmem = 0.0;

    pfcmemtm = 0.008;
    pfcmemK1 = 1.5;
    pfcmemK2 = 0.5;
    pfcseqK = 2.0;
    PFCmemsigma1 = 0.0;
    PFCmemsigma2 = 180.0;
    PFCmemsigma3 = 0.0;
    PFCmemsigma4 = 90.0;
}

public void simRun(){
	//ThPFCmem_in = pfcmemK1* ThPFCmem_in;

  // System.err.println("@@@@ PFCmem simRun entered @@@@");
    pfcmem = nslDiff(pfcmem,pfcmemtm,
			 -pfcmem
			 + (pfcmemK1 * ThPFCmem_in)
			 + (pfcmemK2 * LIPmem_in)
			 + (pfcseqK * pfcseq_in .* LIPmem_in));
   
    pfcmem[4][4] = 0.0;
    PFCmem_out = Nsl2Sigmoid.eval(pfcmem,PFCmemsigma1, PFCmemsigma2,
			      PFCmemsigma3, PFCmemsigma4);
  }

} //end class


