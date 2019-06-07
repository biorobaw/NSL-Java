/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SCbu.mod	1.8 --- 08/05/99 -- 13:56:35 : jversion  @(#)SCbu.mod	1.2---04/23/99--18:39:46 */
// --------------------------------- SCbu layer ------------------------------

nslImport nslAllImports;

//LNK_SC4
/**
* Here is the class representing the superior colliculus build up neurons.
* This layer is called as SCbu.
* This way the 3 modules SCsac, SCqv, SCbu are homogenously composed of
*.nslDifferential equations defining their function see SCtemp
*/

nslModule SCbu (int array_size) {
	// input ports
 public NslDinDouble2 PFCfovea_in (array_size,array_size);
 public NslDinDouble2 SCsac_in(array_size,array_size);
 public NslDinDouble1 BSGEyeMovement_in(array_size);
 public NslDinDouble0 BSGsaccade_in();
	// output ports
 public NslDoutDouble2 SCbu_out(array_size,array_size);
 //fovea = out(new NslDouble0());

private  double SCbusigma1;
private  double SCbusigma2;
private  double SCbusigma3;
private  double SCbusigma4;
private double scbutm;
private static final int NINE = 9;
private static final int CENTERX = 4;
private static final int CENTERY = 4;
private Target SCBUTarget;  
private double SCBUsaccade;
private double SCBUMaxFire;

private NslDouble2 scbu (array_size,array_size)  ;
private NslDouble2 SCBUtemp (array_size,array_size)   ;

    private NslInt0 FOVEAX();
    private NslInt0 FOVEAY();

public void initModule()
{
    FOVEAX = (NslInt0)nslGetValue("crowleyTop.FOVEAX");
    FOVEAY = (NslInt0)nslGetValue("crowleyTop.FOVEAY");  
}

public void initRun(){

  SCbu_out=0;
  SCbusigma1 = 0;
  SCbusigma2 =90;
  SCbusigma3 = 0;
  //LNK_SC4_1
  SCbusigma4 =90;
  scbu = 0;   
  scbutm =   0.006;
  //fovea = SCbu_out[CENTERX][CENTERY];
  SCBUsaccade = 0;
  SCBUMaxFire = 90;
}

public void simRun(){
// 96/12/20 aa - put SCBUtemp calculations here since in the original
// C++ code, it appears that SCbu_out may be sequentially dependent on
// SCBUtemp

  // System.err.println("@@@@ SCbu simRun entered @@@@");

        if ( BSGsaccade_in != 0.0 )  //Performing, or starting, a saccade
        {
           if ( SCBUsaccade == 0 )
            {
               SCBUsaccade = 1;     // Indicates occurrence of a saccade.
//              Get target locations for target remapping of SC buildup
//              neurons
                SCBUTarget = SC.MakeTargets( SCsac_in );
            }
//          Assume some kind of signal from BSG that tells the buildup
//          cells how far the eyes have moved, i.e., efference copy.
            if ( SCBUTarget != null )
            {
                SCBUTarget.Move( BSGEyeMovement_in );
//              Update the activation of the buildup neurons
		//                SCBUtemp = MoveEye( SCBUTarget, SCbu_out ) * SCBUMaxFire;
		// 96/12/20 aa put back as C++
                SCBUtemp = SC.MoveEye( SCBUTarget, SCbu_out ); 
		  SCBUtemp = SCBUtemp * SCBUMaxFire;
            }
        }
        else {
//          No saccade or saccade just finished.
            SCBUsaccade     = 0;
//          When there is no corollary feedback from the BSG, the buildup
//          neurons should gradually decay to a zero state as long as
//          there is no fixation activity from PFC.
	    //            SCBUtemp = 0;
            SCBUtemp = 0;
          }

  scbu=nslDiff(scbu,scbutm, -scbu + SCBUtemp + PFCfovea_in);
  SCbu_out=Nsl2Sigmoid.eval(scbu,SCbusigma1, SCbusigma2, SCbusigma3, SCbusigma4 );
  //fovea = SCbu_out[CENTERX][CENTERY];

//System.out.println("scbu: " + scbu);
//System.out.println("PFCfovea_in: " + PFCfovea_in);

//System.out.println("SCBUtemp: " + SCBUtemp);

}

  //  public double fovea() { return SCbu_out.get(CENTERX, CENTERY);}
					      

} //end class





