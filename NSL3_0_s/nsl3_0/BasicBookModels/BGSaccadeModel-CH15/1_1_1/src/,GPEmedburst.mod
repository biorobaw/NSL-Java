/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)GPEmedburst.mod	1.8 --- 08/05/99 -- 13:56:20 : jversion  @(#)GPEmedburst.mod	1.2---04/23/99--18:39:29 */

// Import statements
nslImport nslAllImports;

//
// GPEmedburst
//
/**
 * GPEmedburst class
 * Represents the Globus Pallidus External Burst Cells Layer
 * @see     GPEmedburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private CDindmedburst_in - input coming from CDmedburst module (of type NslDouble2)<p>
 * -var private GPEmedburst_out - output going to STNmedburst module (of type NslDouble2)<p>
 */
nslModule GPEmedburst(int CorticalArraySize){
	//input ports
    NslDinDouble2 CDindmedburst_in (CorticalArraySize,CorticalArraySize);                     
	//output ports
    NslDoutDouble2 GPEmedburst_out (CorticalArraySize,CorticalArraySize);                     
  // private variables


  private double gpemedbursttm;
  private double GPEmedburstTONIC;
  private double GPEmedburstK;
  private double GPElbsigma1;
  private double GPElbsigma2;
  private double GPElbsigma3;
  private double GPElbsigma4;

 private   NslDouble2 gpemedburst  (CorticalArraySize,CorticalArraySize);

  public void initRun () {
    GPEmedburst_out=0;
    gpemedbursttm = 0.01;
    GPEmedburstTONIC = 30;
    GPEmedburstK = 2;
    GPElbsigma1 = 0;
    GPElbsigma2 = 60;
    GPElbsigma3 = 0;
    GPElbsigma4 = 60;
  }
  public void simRun () {

  // System.err.println("@@@@ GPEmedburst simRun entered @@@@");
    gpemedburst = nslDiff (gpemedburst,gpemedbursttm, 
                                    - gpemedburst + GPEmedburstTONIC 
                                    - (GPEmedburstK * CDindmedburst_in));
    GPEmedburst_out = Nsl2Sigmoid.eval (gpemedburst,GPElbsigma1, GPElbsigma2,
                                           GPElbsigma3, GPElbsigma4);
  }



} //end class


