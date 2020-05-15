/* SCCS  @(#)BSG.mod	1.1---09/24/99--18:57:10 */
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

nslImport nslAllImports;
 
// -------------------------- BSG module --------------------------
 
nslModule BSG(int array_size)   
{
//input ports
  public NslDinDouble2 SCsac_in  () ;
  public NslDinDouble2 SCbu_in  ()  ;
//output ports
  public NslDoutDouble0 BSGsaccade_out ()  ;
  public NslDoutDouble1 BSGEyeMovement_out (2) ; /* is in R^2 */
  public NslDoutDouble2 BSGEye (array_size,array_size) ;
  public NslDoutDouble2 BSGsac (array_size,array_size)  ;

//variables
 private  NslDouble1 BSGSaccadeVector (2)  ;

 private static  int NINE = 9;
 private static  int CENTERX = 4;
 private static  int CENTERY = 4;
 
  //private Target EyeTargets;
 

//  private NslDouble0 BSGSaccadeVelocity;

  private double BSGemtm;
  private double BSGSaccadeVelocity;

  private double BSGsactm;
  private double BSGscsacK;
  private double BSGscbuK;
 
  private double SCsacmax;
  private double SCsacprevmax;
 
  private double BSGsacvel; 
  private double BSGsacvelsigma1;
  private double BSGsacvelsigma2;
  private double BSGsacvelsigma3;
  private double BSGsacvelsigma4;
 
  private double Inhibition;
  private double Activation;
  private double fovea; 

  /* static  int  MaxCorticalFiring = 90; */
  static  private int  MaxCorticalFiring = 90;


public void initRun(){
  // Runmodule BrainstemSaccadeGenerator parameters
   BSGemtm             =    0.01;
   BSGSaccadeVelocity  =    0;
   BSGEyeMovement_out  =    0;
   BSGSaccadeVector    =    0;
   BSGEye              =    0;

   BSGsac              =    0;

   BSGsactm            =    0.01;
   BSGscbuK            =    1;
   BSGscsacK           =    0.06;
   SCsacmax            =    0;
   SCsacprevmax        =    0;
   BSGsaccade_out      =    0;

   Inhibition = 0;
   Activation = 0;
   fovea = SCbu_in[4][4];
	
   BSGsacvelsigma1     =    0;
   BSGsacvelsigma2     = 1000;
   BSGsacvelsigma3     =    10.0 * system.nslGetRunDelta();//SACCADE.get_delta();
   BSGsacvelsigma4     =    20.0 * system.nslGetRunDelta();//SACCADE.get_delta();
}
public void simRun(){
  double r;

  /* @@@ */ 
  //System.err.println("@@@@ BSG simRun entered @@@@");
  fovea = SCbu_in[4][4];
  
  r= fovea*BSGscsacK;
  // System.err.println("@@@@ BSG simRun AA @@@@");
  BSGsac=nslDiff(BSGsac,BSGsactm,-   BSGsac+ SCsac_in*BSGscsacK - fovea*BSGscbuK);
  // System.err.println("@@@@ BSG simRun BB @@@@");
  BSGsac.set(nslj.src.math.NslSaturation.eval( BSGsac,0,MaxCorticalFiring,0,MaxCorticalFiring)); 
  //System.err.println("@@@@ BSG simRun CC @@@@");
  SCsacmax = nslMaxValue(SCsac_in);
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
      BSGsaccade_out = 1;
  // System.err.println("@@@@ BSG simRun EE @@@@");
      BSGSaccadeVector = SC.GetSaccadeVector(BSGsac);
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
  
  BSGEyeMovement_out=nslDiff(BSGEyeMovement_out,BSGemtm,-BSGEyeMovement_out
				     + (BSGSaccadeVector * BSGsacvel * Inhibition));
  
  if ( Inhibition <= 0.01 )
    {
      //      Saccade is over
      BSGsaccade_out      = 0;
      BSGSaccadeVector    = 0;
      Inhibition          = 0;
      BSGEyeMovement_out  = 0;
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
 


}  /* END OF CLASS BSG_module */



