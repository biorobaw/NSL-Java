/* SCCS  @(#)ThLIPmem.mod	1.1---09/24/99--18:57:32 */
/* old kversion @(#)ThLIPmem.mod	1.8 --- 08/05/99 -- 13:56:45 : jversion  @(#)ThLIPmem.mod	1.2---04/23/99--18:34:25 */

nslImport nslAllImports;

//
//Module ThLIPmem - parth of the Thalamus
//

nslModule ThLIPmem(int array_size) {
	//inputs
    public NslDinDouble2 LIPmem_in(array_size,array_size)    ;
    public NslDinDouble2 SNRmedburst_in(array_size,array_size)    ;
    public NslDinDouble2 ThMEDlcn_in(array_size,array_size)     ;
	//outputs
    public NslDoutDouble2 ThLIPmem_out(array_size,array_size)  ;
    public NslDoutDouble2 THNA_out(array_size,array_size);

  //privates
  private double   Thlipmemtm;
  private double   ThlipmemK1;
  private double   ThlipmemK2;
  private double   ThlipmemK3;
  private double   ThLIPmemsigma1;
  private double   ThLIPmemsigma2;
  private double   ThLIPmemsigma3;
  private double   ThLIPmemsigma4;

 private   NslDouble2 Thlipmem(array_size,array_size)   ;
    // envs
  private  NslDouble2 THNewActivation(array_size,array_size)   ;


public void initModule(){
   THNewActivation=(NslDouble2)nslGetValue("crowleyTop.thal1.THNewActivation");


   //System.out.println("\n\n``````````````````````` THNewActivation:"+THNewActivation+"\n\n");
}

public void initRun(){
     ThLIPmem_out = 0;
     Thlipmem =0;

     Thlipmemtm=0.006;
     ThlipmemK1=1.5;
     ThlipmemK2=0.5;
     ThlipmemK3=0.5;
     ThLIPmemsigma1=30;
     ThLIPmemsigma2=65;
     ThLIPmemsigma3=0;
     ThLIPmemsigma4=60;
     THNA_out=THNewActivation;
}

public void simRun(){
  /* <Q> LIPmem_in SNRmedburst_in ThMEDlcn_in THNewActivation */

  // System.err.println("@@@@ ThLIPmem simRun entered @@@@");

     THNA_out=THNewActivation;
//System.err.println("THNEWACT:"+THNewActivation);
    Thlipmem=nslDiff(Thlipmem,Thlipmemtm,-Thlipmem
                     +(ThlipmemK1*LIPmem_in)
                     -(ThlipmemK2*SNRmedburst_in)
                     -(ThlipmemK3*ThMEDlcn_in)
                     +THNewActivation.get());

   ThLIPmem_out=Nsl2Sigmoid.eval(Thlipmem,ThLIPmemsigma1,
                            ThLIPmemsigma2,
                            ThLIPmemsigma3,
                            ThLIPmemsigma4);
  }

} //end class


             

