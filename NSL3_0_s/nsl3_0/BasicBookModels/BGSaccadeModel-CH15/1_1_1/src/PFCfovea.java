/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)PFCfovea.mod	1.8 --- 08/05/99 -- 13:56:29 : jversion  @(#)PFCfovea.mod	1.2---04/23/99--18:39:38 */

// Import statements
 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

//
// PFCfovea
//
 public class PFCfovea extends NslModule/*(int array_size)*/ {
  //input ports
    public NslDinDouble2 LIPvis_in ; /*(array_size,array_size)   */
  //output ports
    public NslDoutDouble2 PFCfovea_out ; /* (array_size,array_size)   */


  // private variables
  private  double         pfcfoveatm;
  private  double         Fixation;
  private  double         DisFixation;
  private  double         PFCfoveasigma1;
  private  double         PFCfoveasigma2;
  private  double         PFCfoveasigma3;
  private  double         PFCfoveasigma4;

    private NslDouble2 pfcfovea ; /* (array_size,array_size)  */
    private NslInt0 FOVEAX ; /*(4)*/
    private NslInt0 FOVEAY ; /*(4)*/


public  void initModule(){

    FOVEAX.set((NslInt0)nslGetValue("crowleyTop.FOVEAX")) /*rule 114 */;
    FOVEAY.set((NslInt0)nslGetValue("crowleyTop.FOVEAY")) /*rule 114 */;  
}



public  void initRun(){
    pfcfovea.set(0.0);
    PFCfovea_out.set(0.0);
    pfcfoveatm = 0.008;
    PFCfoveasigma1 = 0.0;
    PFCfoveasigma2 = 60.0;
    PFCfoveasigma3 = 0.0;
    PFCfoveasigma4 = 90.0;

    Fixation = 1.0;
    DisFixation = 0.0;
}

public  void simRun(){
    // System.err.println("@@@@ PFCfovea simRun entered @@@@");
// 99/8/3 aa:in acutallity x should map with j, and y should map with i.
    pfcfovea.set(system.nsldiff.eval(pfcfovea,pfcfoveatm,
 __nsltmp102=nslj.src.math.NslAdd.eval(__nsltmp102,
 __nsltmp101=nslj.src.math.NslSub.eval(__nsltmp101,0,pfcfovea.get()),((LIPvis_in).get(FOVEAX.get(),FOVEAY.get()))*((Fixation)-(DisFixation))))) /* rule 108 */;
    PFCfovea_out.set(Nsl2Sigmoid.eval(pfcfovea,PFCfoveasigma1,PFCfoveasigma2,PFCfoveasigma3,PFCfoveasigma4)) /* rule 108 */;
}


	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	__nsltmp102 = new double[1][1];
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
	/* constructor and related methods */
	/* nsl declarations */
	int array_size;

	 /*GENERIC CONSTRUCTOR:   */
	 public PFCfovea(String nslName, NslModule nslParent,int array_size) {
		super(nslName, nslParent);
		this.array_size = array_size;
		initSys();
		makeInst(nslName, nslParent,array_size);
	}
	public void makeInst(String nslName, NslModule nslParent,int array_size){ 
	 LIPvis_in=new NslDinDouble2 ("LIPvis_in",this,array_size,array_size); //NSLDECLS 
	 PFCfovea_out=new NslDoutDouble2 ("PFCfovea_out",this,array_size,array_size); //NSLDECLS 
	 pfcfovea=new NslDouble2 ("pfcfovea",this,array_size,array_size); //NSLDECLS 
	 FOVEAX=new NslInt0 ("FOVEAX",this,4); //NSLDECLS 
	 FOVEAY=new NslInt0 ("FOVEAY",this,4); //NSLDECLS 
	}
	/* end of automatic declaration statements */
} //end class
