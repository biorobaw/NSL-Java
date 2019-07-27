/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)ThPFCmem.mod	1.8 --- 08/05/99 -- 13:56:46 : jversion  @(#)ThPFCmem.mod	1.2---04/23/99--18:33:33 */

nslImport nslAllImports;
/////////////////////////////////////////////
//Module: ThPFCmem - Part of the Thalamus
/////////////////////////////////////////////

//
//ThPFCmem
//
nslModule ThPFCmem(int array_size){
//input ports
    public NslDinDouble2 SNRmedburst_in(array_size,array_size)    ;
    public NslDinDouble2 ThMEDlcn_in(array_size,array_size)     ;
    public NslDinDouble2 PFCmem_in(array_size,array_size)     ;
//output ports
    public NslDoutDouble2 ThPFCmem_out(array_size,array_size)  ;

//private variables

  private double Thpfcmemtm;
  private double ThpfcmemK1;
  private double ThpfcmemK2;
  private double ThpfcmemK3;
  private double ThPFCmemsigma1;
  private double ThPFCmemsigma2;
  private double ThPFCmemsigma3;
  private double ThPFCmemsigma4;
  private  NslDouble2 Thpfcmem(array_size,array_size)   ;

  private NslDouble2 THNewActivation(array_size,array_size)   ;

public void initModule()
  {
    THNewActivation = (NslDouble2)nslGetValue ("crowleyTop.thal1.THNewActivation");
  }

public void initRun(){
    Thpfcmem = 0;
    ThPFCmem_out = 0;
    Thpfcmemtm=0.006;
    ThpfcmemK1=1.5;
    ThpfcmemK2=0.5;
    ThpfcmemK3=0.5;
    ThPFCmemsigma1=30;
    ThPFCmemsigma2=65;
    ThPFCmemsigma3=0;
    ThPFCmemsigma4=60;
}

public void simRun(){
  /* <Q> PFCmem_in SNRmedburst_in ThMEDlcn_in THNewActivation */

  // System.err.println("@@@@ ThPFCmem_in simRun entered @@@@");
    Thpfcmem=nslDiff(Thpfcmem,Thpfcmemtm,-Thpfcmem
                     +(ThpfcmemK1*PFCmem_in)
                     -(ThpfcmemK2*SNRmedburst_in)
                     -(ThpfcmemK3*ThMEDlcn_in)
                     +THNewActivation.get());
			   
    ThPFCmem_out=Nsl2Sigmoid.eval(Thpfcmem,ThPFCmemsigma1,
                            ThPFCmemsigma2,
                            ThPFCmemsigma3,
                            ThPFCmemsigma4);
  }

} //end class


