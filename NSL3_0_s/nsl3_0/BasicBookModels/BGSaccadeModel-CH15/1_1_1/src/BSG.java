/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)BSG.mod	1.8 --- 08/05/99 -- 13:56:06 : jversion  @(#)BSG.mod	1.2---04/23/99--18:39:18 */
/**
* Here is the class representing the Brainstem Saccade Generator (BSG) module.
* This is a completely non-neural module. It monitors (non-neurally) certain 
* conditions and triggers the saccade by setting BSGsaccade to 1 when a certain
* (see the ref.) contidion is met. The BSG module aslo extracts the velocity 
* and  direction information for a saccade.  The actuall ending of a saccade
* is also carried with BSG (again non-neurally)
* == Recordings of SC neurons (Sparks, 1986) have shown that saccades are 
* == initiated about the the time the peak in firing of the SRBNs accurs.
* M. Crowley: "Since our BSG is non-neural, we calculate directly when the
* activity from SC burst cell is declining. When this situation occurs, we
* extract the saccade vector and peak firing rate from SCsac and initiate a 
* saccade"
*/

 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 
 
// -------------------------- BSG module --------------------------
 
 public class BSG extends NslModule/*(int array_size)*/   
{
//input ports
  public NslDinDouble2 SCsac_in ; /*  () */
  public NslDinDouble2 SCbu_in ; /*  ()  */
//output ports
  public NslDoutDouble0 BSGsaccade_out ; /* ()  */
  public NslDoutDouble1 BSGEyeMovement_out ; /* (2) */ /* is in R^2 */
  public NslDoutDouble2 BSGEye ; /* (array_size,array_size) */
  public NslDoutDouble2 BSGsac ; /* (array_size,array_size)  */

//variables
 private  NslDouble1 BSGSaccadeVector ; /* (2)  */

 private static   int NINE = 9;
 private static   int CENTERX = 4;
 private static   int CENTERY = 4;
 
  //private Target EyeTargets;
 

//  private NslDouble0 BSGSaccadeVelocity;

  private  double BSGemtm;
  private  double BSGSaccadeVelocity;

  private  double BSGsactm;
  private  double BSGscsacK;
  private  double BSGscbuK;
 
  private  double SCsacmax;
  private  double SCsacprevmax;
 
  private  double BSGsacvel; 
  private  double BSGsacvelsigma1;
  private  double BSGsacvelsigma2;
  private  double BSGsacvelsigma3;
  private  double BSGsacvelsigma4;
 
  private  double Inhibition;
  private  double Activation;
  private  double fovea; 

  /* static  int  MaxCorticalFiring = 90; */
  static  private  int  MaxCorticalFiring = 90;


public  void initRun(){
  // Runmodule BrainstemSaccadeGenerator parameters
   BSGemtm             =    0.01;
   BSGSaccadeVelocity  =    0;
   BSGEyeMovement_out.set(0);
   BSGSaccadeVector.set(0);
   BSGEye.set(0);

   BSGsac.set(0);

   BSGsactm            =    0.01;
   BSGscbuK            =    1;
   BSGscsacK           =    0.06;
   SCsacmax            =    0;
   SCsacprevmax        =    0;
   BSGsaccade_out.set(0);

   Inhibition = 0;
   Activation = 0;
   fovea=(SCbu_in).get(4,4);
	
   BSGsacvelsigma1     =    0;
   BSGsacvelsigma2     = 1000;
   BSGsacvelsigma3     =    10.0 * system.nslGetRunDelta();//SACCADE.get_delta();
   BSGsacvelsigma4     =    20.0 * system.nslGetRunDelta();//SACCADE.get_delta();
}
public  void simRun(){
   double r;

  /* @@@ */ 
  //System.err.println("@@@@ BSG simRun entered @@@@");
  fovea=(SCbu_in).get(4,4);
  
  r= fovea*BSGscsacK;
  // System.err.println("@@@@ BSG simRun AA @@@@");
  BSGsac.set(system.nsldiff.eval(BSGsac,BSGsactm,
 __nsltmp104=nslj.src.math.NslSub.eval(__nsltmp104,
 __nsltmp103=nslj.src.math.NslAdd.eval(__nsltmp103,
 __nsltmp101=nslj.src.math.NslSub.eval(__nsltmp101,0,BSGsac.get()),
 __nsltmp102=nslj.src.math.NslElemMult.eval(__nsltmp102,SCsac_in.get(),BSGscsacK)),(fovea)*(BSGscbuK)))) /* rule 108 */;
  // System.err.println("@@@@ BSG simRun BB @@@@");
  BSGsac.set(nslj.src.math.NslSaturation.eval( BSGsac,0,MaxCorticalFiring,0,MaxCorticalFiring)); 
  //System.err.println("@@@@ BSG simRun CC @@@@");
  SCsacmax=NslMaxValue.eval(SCsac_in)/* rule 102*/;
  //System.err.println("@@@@ BSG simRun DD @@@@");

  // System.out.println("BSG: SCsacmax "+SCsacmax +"\tSCsacprevmax "+ 
	//	     SCsacprevmax+"\tBSGsaccade_out "+BSGsaccade_out+"\n"+SCsac_in);

  
  // System.err.println("@@@@ BSG: BSGsaccade_out:"+BSGsaccade_out);
  if ((SCsacmax < SCsacprevmax) &&
      (BSGsaccade_out.get() == 0.0) &&
      (SCsacmax > 30.0)) 
    
    // SCsacsigma4/4.0))
    //    (SCsacmax >BSGsacvelsigma1))  //Min for causing saccade
  {
      BSGsaccade_out.set(1);
  // System.err.println("@@@@ BSG simRun EE @@@@");
      BSGSaccadeVector.set(SC.GetSaccadeVector(BSGsac)) /* rule 108 */;
  // System.err.println("@@@@ BSG simRun FF @@@@");
      BSGsacvel =0+ Nsl2Sigmoid.eval(SCsacmax,BSGsacvelsigma1,BSGsacvelsigma2,
					BSGsacvelsigma3, BSGsacvelsigma4 );
  // System.err.println("@@@@ BSG simRun GG @@@@");
      SCsacmax     = 0;
      SCsacprevmax = 0;  /* seems redundant... */
  }
  SCsacprevmax = SCsacmax;
  
  /**
   * == M. Crowley explains the factor 0.33 as: 
   * == "SCbu_in tends to max at 30 or above based on current model performance.  
   * ==  That is why we uses .033 for XXX???"
   */
  Activation = fovea * .033;
  if ( Activation > 1 ) Activation = 1;
  Inhibition = ( 1 - Activation ) * ( 1 - Activation );
  
  BSGEyeMovement_out.set(system.nsldiff.eval(BSGEyeMovement_out,BSGemtm,
 __nsltmp108=nslj.src.math.NslAdd.eval(__nsltmp108,
 __nsltmp105=nslj.src.math.NslSub.eval(__nsltmp105,0,BSGEyeMovement_out.get()),
 __nsltmp107=nslj.src.math.NslElemMult.eval(__nsltmp107,
 __nsltmp106=nslj.src.math.NslElemMult.eval(__nsltmp106,BSGSaccadeVector.get(),BSGsacvel),Inhibition)))) /* rule 108 */;
  
  if ( Inhibition <= 0.01 )
    {
      //      Saccade is over
      BSGsaccade_out.set(0);
      BSGSaccadeVector.set(0);
      Inhibition          = 0;
      BSGEyeMovement_out.set(0);
      BSGsacvel           = 0;
    }
  /*  
  //  Apply any movement of the eyes to the targets
      EyeTargets = MakeTargets( RETINA );
  
  if ( EyeTargets != null )
    {
      //      Get target locations for target remapping of RETINA for
      //      display purposes only
      
      EyeTargets.Move(BSGEyeMovement_out);
      
      //      Update the image on the retina
      //        BSGEye = RETINA.get_sector(0,8,0,8);
      //        BSGEye = MoveEye( EyeTargets, BSGEye );
      //        RETINA = BSGEye.get_sector(0,8,0,8);
    }
    */

// System.err.println("BSG: simRun finished!!!!!");
} /* End of simRun() */
 


	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	__nsltmp102 = new double[1][1];
	__nsltmp103 = new double[1][1];
	__nsltmp104 = new double[1][1];
	__nsltmp105 = new double[1];
	__nsltmp106 = new double[1];
	__nsltmp107 = new double[1];
	__nsltmp108 = new double[1];
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
		__nsltmp105[i] = 0;
}
	for (int i = 0; i < __nsltmp106.length; i++) {
		__nsltmp106[i] = 0;
}
	for (int i = 0; i < __nsltmp107.length; i++) {
		__nsltmp107[i] = 0;
}
	for (int i = 0; i < __nsltmp108.length; i++) {
		__nsltmp108[i] = 0;
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
	private  double[] __nsltmp105;
	private  double[] __nsltmp106;
	private  double[] __nsltmp107;
	private  double[] __nsltmp108;
	/* constructor and related methods */
	/* nsl declarations */
	int array_size;

	 /*GENERIC CONSTRUCTOR:   */
	 public BSG(String nslName, NslModule nslParent,int array_size) {
		super(nslName, nslParent);
		this.array_size = array_size;
		initSys();
		makeInst(nslName, nslParent,array_size);
	}
	public void makeInst(String nslName, NslModule nslParent,int array_size){ 
	 SCsac_in=new NslDinDouble2 ("SCsac_in",this); //NSLDECLS 
	 SCbu_in=new NslDinDouble2 ("SCbu_in",this); //NSLDECLS 
	 BSGsaccade_out=new NslDoutDouble0 ("BSGsaccade_out",this); //NSLDECLS 
	 BSGEyeMovement_out=new NslDoutDouble1 ("BSGEyeMovement_out",this,2); //NSLDECLS 
	 BSGEye=new NslDoutDouble2 ("BSGEye",this,array_size,array_size); //NSLDECLS 
	 BSGsac=new NslDoutDouble2 ("BSGsac",this,array_size,array_size); //NSLDECLS 
	 BSGSaccadeVector=new NslDouble1 ("BSGSaccadeVector",this,2); //NSLDECLS 
	}
	/* end of automatic declaration statements */
}  /* END OF CLASS BSG_module */
