/* SCCS  @(#)LIP.mod	1.1---09/24/99--18:57:21 */
/* old kversion @(#)LIP.mod	1.8 --- 08/05/99 -- 13:56:25 : jversion  @(#)LIP.mod	1.2---04/23/99--18:39:33 */
//Module: LIP - Lateral IntraParital (Cortex)

nslImport nslAllImports;

//
//LIPvis
//
/**
*A class representing the lateral Intraparital Cortex layer of Crowley Model.
*@see Michael Crowley Model
*@version   Fall 96
*@ author   HBP
* -var public lipvistm used in LIPvis to calculate the membrane potential at
 lipvistm.<p>
* -var public lipvistm used in LIPmem to calculate the membrane potential at
 lipmemtm.<p>
*/  
nslModule LIP(int array_size) {
  
    // intputs
    NslDinDouble2 ThLIPmem_in(array_size,array_size)  ;
    NslDinDouble2 SLIPvis_in(array_size,array_size) ; //RETINA

    // outputs
   NslDoutDouble2 LIPvis_out (array_size,array_size) ;
   NslDoutDouble2 LIPmem_out (array_size,array_size) ;

//private variables

  // these were public but why
  private double lipvistm;
  private double lipmemtm;
  private double LIPmemK;
  private double LIPvissigma1;
  private double LIPvissigma2;
  private double LIPvissigma3;
  private double LIPvissigma4;
  private double LIPmemsigma1;
  private double LIPmemsigma2;
  private double LIPmemsigma3;
  private double LIPmemsigma4;

    // private
    NslDouble2 lipvis(array_size,array_size)   ;
    NslDouble2 lipmem (array_size,array_size) ;

    // environment or hierarchical variables
    NslInt0 FOVEAX();
    NslInt0 FOVEAY();

public void initModule()
 {
    FOVEAX = (NslInt0)nslGetValue("crowleyTop.FOVEAX")  ;
    FOVEAY = (NslInt0)nslGetValue("crowleyTop.FOVEAX")  ;
  }

public void initRun(){
    lipvis = 0;
    lipmem = 0;

    LIPvis_out = 0;
    LIPmem_out=0;
    LIPmemK=0.9;

    lipvistm=0.006;
    lipmemtm=0.008;
    LIPvissigma1=0;
    LIPvissigma2=90;
    LIPvissigma3=0;
    LIPvissigma4=90;

    LIPmemsigma1=0;
    LIPmemsigma2=90;
    LIPmemsigma3=0;
    LIPmemsigma4=90;
}

public void simRun(){
  // System.err.println("@@@@ LIP simRun entered @@@@");
  /* <Q> RETINA? LIPmemK ThLIPmem_in */

/* Note: the order of the following lines is very important */
/* The membrain potentials are calculated first, then the firing rates */

    lipvis=nslDiff(lipvis,lipvistm,-lipvis+ SLIPvis_in); //RETINA;
    lipmem=nslDiff(lipmem,lipmemtm,-lipmem+(LIPmemK*ThLIPmem_in)+LIPvis_out);

    lipmem[FOVEAX][FOVEAY]=LIPvis_out[FOVEAX][FOVEAY];                        

    LIPvis_out=Nsl2Sigmoid.eval(lipvis,LIPvissigma1, LIPvissigma2,
                              LIPvissigma3, LIPvissigma4);
    LIPmem_out=Nsl2Sigmoid.eval(lipmem,LIPmemsigma1, LIPmemsigma2,
                              LIPmemsigma3, LIPmemsigma4);
  }


} //end class



