/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SCsac.mod	1.8 --- 08/05/99 -- 13:56:36 : jversion  @(#)SCsac.mod	1.2---04/23/99--18:39:47 */
// ----------------------------SCsac layer ----------------------------------

nslImport nslAllImports;

//LNK_SC2
/**
* Here is the class representing the superior colliculus burst neuron
* saccade generating (SCsac) layer.
*/

nslModule SCsac (int array_size) {
	// input ports
    NslDinDouble2 SNRlatburst_in(array_size,array_size)     ;
    NslDinDouble2 FEFsac_in(array_size,array_size)              ;
    NslDinDouble2 SCqv_in(array_size,array_size)                ;
    NslDinDouble2 SCbu_in(array_size,array_size)               ;
	// output ports
    NslDoutDouble2 SCsac_out(array_size,array_size)        ;

  private double SCsacsigma1;
  private double SCsacsigma2;
  private double SCsacsigma3;
  private double SCsacsigma4;
  //of the matrix SCsac
  private double SCsacvelK;
  private double SCsacmax;
  private double SCsacprevmax;
  private double scsactm;
  private double BSGscsacK;
  private double SCfefsacK;
  private double SCsnrlbK;
  private double SCscbuK;

  private double SCscqvK;
  private  NslDouble2 scsac(array_size,array_size)   ;
   
    private NslInt0 FOVEAX();
    private NslInt0 FOVEAY();

public void initModule()
{
    FOVEAX = (NslInt0)nslGetValue("crowleyTop.FOVEAX");
    FOVEAY = (NslInt0)nslGetValue("crowleyTop.FOVEAY");  
}

public void initRun(){
    scsac =0;
    SCsac_out = 0;
//threshold to the saccade
    SCsacsigma1 =   30;
    SCsacsigma2 =  160;
    SCsacsigma3 =    0;
    //LNK_SC2_1
    SCsacsigma4 = 1000;
    //of the matrix SCsac.

    SCsacvelK   = 0.9 / ( SCsac_out.getSize1() * SCsacsigma4 );

    SCsacmax            =    0;
    SCsacprevmax        =    0;
    scsactm =      0.01;
    BSGscsacK           =    0.06;
    SCfefsacK =    1;
    SCsnrlbK =     2;
    SCscbuK =     18.0;

 SCscqvK= 1.0;


}

public void simRun(){
  // System.err.println("@@@@ SCsac simRun entered @@@@");
    scsac=nslDiff(scsac,scsactm, 
                 -scsac + ( SCscqvK * SCqv_in )
                 + ( SCfefsacK * FEFsac_in )
                 - ( SCsnrlbK * SNRlatburst_in )
                 - ( SCscbuK * SCbu_in[4][4]));

    SCsac_out=Nsl2Sigmoid.eval(scsac,SCsacsigma1,SCsacsigma2,SCsacsigma3,SCsacsigma4);
}


}//end class






