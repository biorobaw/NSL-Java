/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)FEF.mod	1.8 --- 08/05/99 -- 13:56:18 : jversion  @(#)FEF.mod	1.2---04/23/99--18:39:27 */


// ----------------------------FEF module ------------------------------------
 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

/* 
* Here is the class representing the Frontal Eye Fields (FEF) module.
* FEF is modeled to have two type of cells: FEFvis, visual response cells and
* FEFmem, memory response cells. FEFvis only responds to visual stimuli that 
* are targets of impending saccades and do not fire before saccades made 
* without visual input, nor they project to the SC.
*/

 public class FEF extends NslModule/*(int array_size)*/ 
{
	// input ports  
     public NslDinDouble2 ThFEFmem_in ; /*(array_size,array_size)  */
     public NslDinDouble2 LIPmem_in ; /*(array_size,array_size)  */
     public NslDinDouble2 PFCgo_in ; /*(array_size,array_size)  */
     public NslDinDouble2 PFCnovelty ; /*(array_size,array_size)  */
     public NslDinDouble2 PFCmem_in ; /*(array_size,array_size)  */

    // outputs
    public NslDoutDouble2 FEFmem_out ; /*(array_size,array_size)  */
    public NslDoutDouble2 FEFsac_out ; /*(array_size,array_size)  */ 

    // privates
    private NslDouble2 fefmem ; /*(array_size,array_size)   */
    private NslDouble2 fefsac ; /*(array_size,array_size)   */

    // envs or hierarchy variables
    private NslInt0 FOVEAX ; /*()*/
    private NslInt0 FOVEAY ; /*()*/
  /* ERH: 
   NINE = CorticalArraySize = 9 ; -may be it is better to replace NINE's
   with the overall common arraysize: CorticalArraySize
   But if this is to be passed as a parameter in the future, this is ok.
   */
  private static  int NINE = 9;
  /* ERH: 
     CorticalSlowdown is currently constant but there is some comment in
     LateralCaudate (RUN)module may be it can be useful in future:
     "Factor to slow down certain cortical areas due to cortical plasticity
     responding to slowdown already occurring in BG during PD and HD.
     Go with half to indicate slowdown lag between cortex and BG.
     
     CorticalSlowdown = NslSigma(SNCdopmax,SNCdopsigma3,SNCdopsigma4,
     cdburstsigma3,cdburstsigma4) / 2.0;
     "
     And CorticalSlowdown is also used by LateralCaudate (RUN)module.
  */
  private static  double CorticalSlowdown = 1.0;  
  private static  double basefefsactm = 0.008;     


  private  double FEFmemsigma1;
  private  double FEFmemsigma2;
  private  double FEFmemsigma3;
  private  double FEFmemsigma4;
  private  double FEFsacsigma1;
  private  double FEFsacsigma2;
  private  double FEFsacsigma3;
  private  double FEFsacsigma4;
  private  double FEFSaccadeVector ;
  private  double fefmemtm ;
  private  double fefsactm ;
  private  double fefmemK1 ;
  private  double fefmemK2 ;
  private  double fefmemK3;

  private  double fefsacK1 ;
  private  double fefsacK2 ;
  
 public  void initModule() 
 {
  FOVEAX.set((NslInt0)nslGetValue("crowleyTop.FOVEAX")) /*rule 114 */;
  FOVEAY.set((NslInt0)nslGetValue("crowleyTop.FOVEAY")) /*rule 114 */;
 }

  public  void initRun(){
    
    FEFmem_out.set(0);
    FEFsac_out.set(0);
    fefmem.set(0);
    fefsac.set(0);

    FEFmemsigma1 =   0;
    FEFmemsigma2 =  90;
    FEFmemsigma3 =   0;
    FEFmemsigma4 =  90;
    FEFsacsigma1 =  40;
    FEFsacsigma2 =  90;
    FEFsacsigma3 =   0;
    FEFsacsigma4 =  90;
    

    FEFSaccadeVector = 0;
    fefmemtm = 0.008;
    fefsactm = 0.006;
    fefmemK1 = 0.5;
    fefmemK2 = 1;
    fefmemK3 = 0.5;

    fefsacK1 = 0.3;
    fefsacK2 = 1;
  }
public  void simRun(){

  // System.err.println("@@@@ FEF simRun entered @@@@");


    //LNK_FEF2
    /**
     * A memory loop is established between FEFmem and mediodorsal thalamus
     *(ThFEFmem_in) to maintain the saccadic target memory.
     */

    // 1-2-97 isaac:  fefmemK3 * PFCmem is missing from the original code.
    // redefine the inport interface.

    fefmem.set(system.nsldiff.eval(fefmem,fefmemtm,
 __nsltmp107=nslj.src.math.NslAdd.eval(__nsltmp107,
 __nsltmp105=nslj.src.math.NslAdd.eval(__nsltmp105,
 __nsltmp103=nslj.src.math.NslAdd.eval(__nsltmp103,
 __nsltmp101=nslj.src.math.NslSub.eval(__nsltmp101,0,fefmem.get()),
 __nsltmp102=nslj.src.math.NslElemMult.eval(__nsltmp102,fefmemK1,ThFEFmem_in.get())),
 __nsltmp104=nslj.src.math.NslElemMult.eval(__nsltmp104,fefmemK2,LIPmem_in.get())),
 __nsltmp106=nslj.src.math.NslElemMult.eval(__nsltmp106,fefmemK3,PFCmem_in.get())))) /* rule 108 */;
    
    //    fefsactm = basefefsactm * CorticalSlowdown;
    
    fefsac.set(system.nsldiff.eval(fefsac,fefsactm,
 __nsltmp113=nslj.src.math.NslAdd.eval(__nsltmp113,
 __nsltmp112=nslj.src.math.NslAdd.eval(__nsltmp112,
 __nsltmp110=nslj.src.math.NslAdd.eval(__nsltmp110,
 __nsltmp108=nslj.src.math.NslSub.eval(__nsltmp108,0,fefsac.get()),
 __nsltmp109=nslj.src.math.NslElemMult.eval(__nsltmp109,fefsacK1,FEFmem_out.get())),
 __nsltmp111=nslj.src.math.NslElemMult.eval(__nsltmp111,fefsacK2,PFCgo_in.get())),PFCnovelty.get()))) /* rule 108 */;
    (fefsac).set(FOVEAX.getint(),FOVEAY.getint(),0);
    FEFmem_out.set(Nsl2Sigmoid.eval(fefmem,FEFmemsigma1,FEFmemsigma2,FEFmemsigma3,FEFmemsigma4)) /* rule 108 */;
    FEFsac_out.set(Nsl2Sigmoid.eval(fefsac,FEFsacsigma1,FEFsacsigma2,FEFsacsigma3,FEFsacsigma4)) /* rule 108 */;
	//96/12/20 aa
    //System.out.println("FEFsac_out: " + FEFsac_out);
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
	__nsltmp109 = new double[1][1];
	__nsltmp110 = new double[1][1];
	__nsltmp111 = new double[1][1];
	__nsltmp112 = new double[1][1];
	__nsltmp113 = new double[1][1];
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
	for (int i = 0; i < __nsltmp109.length; i++) {
		for (int j = 0; j < __nsltmp109[0].length; j++) {
			__nsltmp109[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp110.length; i++) {
		for (int j = 0; j < __nsltmp110[0].length; j++) {
			__nsltmp110[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp111.length; i++) {
		for (int j = 0; j < __nsltmp111[0].length; j++) {
			__nsltmp111[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp112.length; i++) {
		for (int j = 0; j < __nsltmp112[0].length; j++) {
			__nsltmp112[i][j] = 0;
		}
	}
	for (int i = 0; i < __nsltmp113.length; i++) {
		for (int j = 0; j < __nsltmp113[0].length; j++) {
			__nsltmp113[i][j] = 0;
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
	private  double[][] __nsltmp109;
	private  double[][] __nsltmp110;
	private  double[][] __nsltmp111;
	private  double[][] __nsltmp112;
	private  double[][] __nsltmp113;
	/* constructor and related methods */
	/* nsl declarations */
	int array_size;

	 /*GENERIC CONSTRUCTOR:   */
	 public FEF(String nslName, NslModule nslParent,int array_size) {
		super(nslName, nslParent);
		this.array_size = array_size;
		initSys();
		makeInst(nslName, nslParent,array_size);
	}
	public void makeInst(String nslName, NslModule nslParent,int array_size){ 
	 ThFEFmem_in=new NslDinDouble2 ("ThFEFmem_in",this,array_size,array_size); //NSLDECLS 
	 LIPmem_in=new NslDinDouble2 ("LIPmem_in",this,array_size,array_size); //NSLDECLS 
	 PFCgo_in=new NslDinDouble2 ("PFCgo_in",this,array_size,array_size); //NSLDECLS 
	 PFCnovelty=new NslDinDouble2 ("PFCnovelty",this,array_size,array_size); //NSLDECLS 
	 PFCmem_in=new NslDinDouble2 ("PFCmem_in",this,array_size,array_size); //NSLDECLS 
	 FEFmem_out=new NslDoutDouble2 ("FEFmem_out",this,array_size,array_size); //NSLDECLS 
	 FEFsac_out=new NslDoutDouble2 ("FEFsac_out",this,array_size,array_size); //NSLDECLS 
	 fefmem=new NslDouble2 ("fefmem",this,array_size,array_size); //NSLDECLS 
	 fefsac=new NslDouble2 ("fefsac",this,array_size,array_size); //NSLDECLS 
	 FOVEAX=new NslInt0 ("FOVEAX",this); //NSLDECLS 
	 FOVEAY=new NslInt0 ("FOVEAY",this); //NSLDECLS 
	}
	/* end of automatic declaration statements */
} //end class