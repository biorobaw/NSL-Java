/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SCsac.mod	1.8 --- 08/05/99 -- 13:56:36 : jversion  @(#)SCsac.mod	1.2---04/23/99--18:39:47 */
// ----------------------------SCsac layer ----------------------------------

 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

//LNK_SC2
/**
* Here is the class representing the superior colliculus burst neuron
* saccade generating (SCsac) layer.
*/

 public class SCsac extends NslModule /*(int array_size)*/ {
	// input ports
    NslDinDouble2 SNRlatburst_in ; /*(array_size,array_size)     */
    NslDinDouble2 FEFsac_in ; /*(array_size,array_size)              */
    NslDinDouble2 SCqv_in ; /*(array_size,array_size)                */
    NslDinDouble2 SCbu_in ; /*(array_size,array_size)               */
	// output ports
    NslDoutDouble2 SCsac_out ; /*(array_size,array_size)        */

  private  double SCsacsigma1;
  private  double SCsacsigma2;
  private  double SCsacsigma3;
  private  double SCsacsigma4;
  //of the matrix SCsac
  private  double SCsacvelK;
  private  double SCsacmax;
  private  double SCsacprevmax;
  private  double scsactm;
  private  double BSGscsacK;
  private  double SCfefsacK;
  private  double SCsnrlbK;
  private  double SCscbuK;

  private  double SCscqvK;
  private  NslDouble2 scsac ; /*(array_size,array_size)   */
   
    private NslInt0 FOVEAX ; /*()*/
    private NslInt0 FOVEAY ; /*()*/

public  void initModule()
{
    FOVEAX.set((NslInt0)nslGetValue("crowleyTop.FOVEAX")) /*rule 114 */;
    FOVEAY.set((NslInt0)nslGetValue("crowleyTop.FOVEAY")) /*rule 114 */;  
}

public  void initRun(){
    scsac.set(0);
    SCsac_out.set(0);
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

public  void simRun(){
  // System.err.println("@@@@ SCsac simRun entered @@@@");
    scsac.set(system.nsldiff.eval(scsac,scsactm,
 __nsltmp108=nslj.src.math.NslSub.eval(__nsltmp108,
 __nsltmp107=nslj.src.math.NslSub.eval(__nsltmp107,
 __nsltmp105=nslj.src.math.NslAdd.eval(__nsltmp105,
 __nsltmp103=nslj.src.math.NslAdd.eval(__nsltmp103,
 __nsltmp101=nslj.src.math.NslSub.eval(__nsltmp101,0,scsac.get()),
 __nsltmp102=nslj.src.math.NslElemMult.eval(__nsltmp102,SCscqvK,SCqv_in.get())),
 __nsltmp104=nslj.src.math.NslElemMult.eval(__nsltmp104,SCfefsacK,FEFsac_in.get())),
 __nsltmp106=nslj.src.math.NslElemMult.eval(__nsltmp106,SCsnrlbK,SNRlatburst_in.get())),(SCscbuK)*((SCbu_in).get(4,4))))) /* rule 108 */;

    SCsac_out.set(Nsl2Sigmoid.eval(scsac,SCsacsigma1,SCsacsigma2,SCsacsigma3,SCsacsigma4)) /* rule 108 */;
}


	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	__nsltmp102 = new double[1][1];
	__nsltmp103 = new double[1][1];
	__nsltmp104 = new double[1][1];
	__nsltmp105 = new double[1][1];
	__nsltmp106 = new double[1][1];
	__nsltmp107 = new double[1][1];
	__nsltmp108 = new double[1][1];
	/* end of automatic instantiation statements */
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* nslInitTempRun inserted by NPP */
public void nslInitTempRun() {
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	for (int i = 0; i < __nsltmp101.length; i++) {
		for (int j = 0; j < __nsltmp101[0].length; j++) {
			__nsltmp101[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp102.length; i++) {
		for (int j = 0; j < __nsltmp102[0].length; j++) {
			__nsltmp102[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp103.length; i++) {
		for (int j = 0; j < __nsltmp103[0].length; j++) {
			__nsltmp103[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp104.length; i++) {
		for (int j = 0; j < __nsltmp104[0].length; j++) {
			__nsltmp104[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp105.length; i++) {
		for (int j = 0; j < __nsltmp105[0].length; j++) {
			__nsltmp105[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp106.length; i++) {
		for (int j = 0; j < __nsltmp106[0].length; j++) {
			__nsltmp106[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp107.length; i++) {
		for (int j = 0; j < __nsltmp107[0].length; j++) {
			__nsltmp107[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp108.length; i++) {
		for (int j = 0; j < __nsltmp108[0].length; j++) {
			__nsltmp108[i][j] = 0;
		}
	}
	/* end of automatic intialisation statements */
}

	/* nslInitTempTrain inserted by NPP */
public void nslInitTempTrain() {
	/* Initialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* Declaration statements generated by NslPreProcessor */
	/* makeinst() declared variables */
	/* temporary variables */
	private  double[][] __nsltmp101;
	private  double[][] __nsltmp102;
	private  double[][] __nsltmp103;
	private  double[][] __nsltmp104;
	private  double[][] __nsltmp105;
	private  double[][] __nsltmp106;
	private  double[][] __nsltmp107;
	private  double[][] __nsltmp108;
	/* constructor and related methods */
	/* nsl declarations */
	int array_size;

	 /*GENERIC CONSTRUCTOR:   */
	 public SCsac(String nslName, NslModule nslParent,int array_size) {
		super(nslName, nslParent);
		this.array_size = array_size;
		initSys();
		makeInst(nslName, nslParent,array_size);
	}
	public void makeInst(String nslName, NslModule nslParent,int array_size){ 
	 SNRlatburst_in=new NslDinDouble2 ("SNRlatburst_in",this,array_size,array_size); //NSLDECLS 
	 FEFsac_in=new NslDinDouble2 ("FEFsac_in",this,array_size,array_size); //NSLDECLS 
	 SCqv_in=new NslDinDouble2 ("SCqv_in",this,array_size,array_size); //NSLDECLS 
	 SCbu_in=new NslDinDouble2 ("SCbu_in",this,array_size,array_size); //NSLDECLS 
	 SCsac_out=new NslDoutDouble2 ("SCsac_out",this,array_size,array_size); //NSLDECLS 
	 scsac=new NslDouble2 ("scsac",this,array_size,array_size); //NSLDECLS 
	 FOVEAX=new NslInt0 ("FOVEAX",this); //NSLDECLS 
	 FOVEAY=new NslInt0 ("FOVEAY",this); //NSLDECLS 
	}
	/* end of automatic declaration statements */
}//end class