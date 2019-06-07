/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)STNlatburst.mod	1.8 --- 08/05/99 -- 13:56:39 : jversion  @(#)STNlatburst.mod	1.2---04/23/99--18:39:50 */

 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

//
// STNlatburst
//
/**
 * STNlatburst class
 * Represents the Subthalamic Nucleus Burst Cells Layer
 * @see    STNlatburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var public GPElatburst_in - input coming from GPElatburst module (of type NslDouble2)<p>
 * -var public STNlatburst_out - output going to SNRlatburst module (of type NslDouble2)<p>
 */

 public class STNlatburst extends NslModule /*(int CorticalArraySize)*/  {
    public NslDinDouble2 GPElatburst_in ; /*(CorticalArraySize,CorticalArraySize)   */
    //output ports
    public NslDoutDouble2 STNlatburst_out ; /*(CorticalArraySize,CorticalArraySize)  */


  // private variables


    private NslDouble2 stnlatburst ; /*(CorticalArraySize,CorticalArraySize)   */
  private  double stnlatbursttm;
  private  double STNlatburstTONIC;
  private  double STNlbsigma1;
  private  double STNlbsigma2;
  private  double STNlbsigma3;
  private  double STNlbsigma4;
  

  public  void initRun () {
    stnlatbursttm = 0.01;
    STNlatburstTONIC = 60;
    STNlbsigma1 = 10; //20; see lc30.nsl
    STNlbsigma2 = 60;
    STNlbsigma3 = 0;
    STNlbsigma4 = 60;
    stnlatburst.set(0);
    STNlatburst_out.set(0);
  }
  public  void simRun () {
  // System.err.println("@@@@ STNlatburst simRun entered @@@@");
    stnlatburst.set(system.nsldiff.eval(stnlatburst,stnlatbursttm,
 __nsltmp103=nslj.src.math.NslSub.eval(__nsltmp103,
 __nsltmp102=nslj.src.math.NslAdd.eval(__nsltmp102,
 __nsltmp101=nslj.src.math.NslSub.eval(__nsltmp101,0,stnlatburst.get()),STNlatburstTONIC),GPElatburst_in.get()))) /* rule 108 */;
    STNlatburst_out.set(Nsl2Sigmoid.eval(stnlatburst,STNlbsigma1,STNlbsigma2,STNlbsigma3,STNlbsigma4)) /* rule 108 */;
  }

	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	__nsltmp102 = new double[1][1];
	__nsltmp103 = new double[1][1];
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
	/* constructor and related methods */
	/* nsl declarations */
	int CorticalArraySize;

	 /*GENERIC CONSTRUCTOR:   */
	 public STNlatburst(String nslName, NslModule nslParent,int CorticalArraySize) {
		super(nslName, nslParent);
		this.CorticalArraySize = CorticalArraySize;
		initSys();
		makeInst(nslName, nslParent,CorticalArraySize);
	}
	public void makeInst(String nslName, NslModule nslParent,int CorticalArraySize){ 
	 GPElatburst_in=new NslDinDouble2 ("GPElatburst_in",this,CorticalArraySize,CorticalArraySize); //NSLDECLS 
	 STNlatburst_out=new NslDoutDouble2 ("STNlatburst_out",this,CorticalArraySize,CorticalArraySize); //NSLDECLS 
	 stnlatburst=new NslDouble2 ("stnlatburst",this,CorticalArraySize,CorticalArraySize); //NSLDECLS 
	}
	/* end of automatic declaration statements */
} // end class
