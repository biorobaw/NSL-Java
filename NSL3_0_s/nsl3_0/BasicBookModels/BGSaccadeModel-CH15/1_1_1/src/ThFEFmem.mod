/* SCCS  @(#)ThFEFmem.mod	1.1---09/24/99--18:57:31 */
/* old kversion @(#)ThFEFmem.mod	1.8 --- 08/05/99 -- 13:56:44 : jversion  @(#)ThFEFmem.mod	1.2---04/23/99--18:39:52 */

nslImport nslAllImports;

//
//Module ThFEFmem - Part of the Thalamus
//
nslModule ThFEFmem(int array_size){
//input ports
    public NslDinDouble2 FEFmem_in(array_size,array_size)  ;
    public NslDinDouble2 SNRmedburst_in(array_size,array_size) ;
    public NslDinDouble2 ThMEDlcn_in(array_size,array_size);

//output ports
    public NslDoutDouble2 ThFEFmem_out(array_size,array_size)  ;

  //private variables

  private double Thfefmemtm;
  private double ThfefmemK1;
  private double ThfefmemK2;
  private double ThfefmemK3;
  private double ThFEFmemsigma1;
  private double ThFEFmemsigma2;
  private double ThFEFmemsigma3;
  private double ThFEFmemsigma4;
   private NslDouble2 THNewActivation(array_size,array_size);

    private NslDouble2 Thfefmem(array_size,array_size)  ;



public void initModule()
 {
    THNewActivation= (NslDouble2)nslGetValue("crowleyTop.thal1.THNewActivation") ;
  }

public void initRun(){

    ThFEFmem_out = 0;
    Thfefmem = 0;
    Thfefmemtm=0.006;
    ThfefmemK1=1.5;
    ThfefmemK2=0.5;
    ThfefmemK3=0.5;
    ThFEFmemsigma1=30;
    ThFEFmemsigma2=65;
    ThFEFmemsigma3=0;
    ThFEFmemsigma4=60;
  }

public void simRun(){
  /* <Q> FEFmem_in  SNRmedburst_in  ThMEDlcn_in  THNewActivation*/

  // System.err.println("@@@@ ThFEFmem simRun entered @@@@");
    Thfefmem=nslDiff(Thfefmem,Thfefmemtm,-Thfefmem
                     +(ThfefmemK1*FEFmem_in)
                     -(ThfefmemK2*SNRmedburst_in)
                     -(ThfefmemK3*ThMEDlcn_in)
                     +THNewActivation.get());


   ThFEFmem_out=Nsl2Sigmoid.eval(Thfefmem,ThFEFmemsigma1,
                            ThFEFmemsigma2,
                            ThFEFmemsigma3,
                            ThFEFmemsigma4);

	// 96/12/20
	//System.out.println("ThFEFmem_out: " + ThFEFmem_out);
  }

} //end class

