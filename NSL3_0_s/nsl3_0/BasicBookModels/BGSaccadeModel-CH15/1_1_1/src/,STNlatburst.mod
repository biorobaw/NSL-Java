/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)STNlatburst.mod	1.8 --- 08/05/99 -- 13:56:39 : jversion  @(#)STNlatburst.mod	1.2---04/23/99--18:39:50 */

nslImport nslAllImports;

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

nslModule STNlatburst (int CorticalArraySize)  {
    public NslDinDouble2 GPElatburst_in(CorticalArraySize,CorticalArraySize)   ;
    //output ports
    public NslDoutDouble2 STNlatburst_out(CorticalArraySize,CorticalArraySize)  ;


  // private variables


    private NslDouble2 stnlatburst(CorticalArraySize,CorticalArraySize)   ;
  private double stnlatbursttm;
  private double STNlatburstTONIC;
  private double STNlbsigma1;
  private double STNlbsigma2;
  private double STNlbsigma3;
  private double STNlbsigma4;
  

  public void initRun () {
    stnlatbursttm = 0.01;
    STNlatburstTONIC = 60;
    STNlbsigma1 = 10; //20; see lc30.nsl
    STNlbsigma2 = 60;
    STNlbsigma3 = 0;
    STNlbsigma4 = 60;
    stnlatburst=0;
    STNlatburst_out=0;
  }
  public void simRun () {
  // System.err.println("@@@@ STNlatburst simRun entered @@@@");
    stnlatburst = nslDiff (stnlatburst,stnlatbursttm, 
                                    - stnlatburst + STNlatburstTONIC 
                                                  - GPElatburst_in);
    STNlatburst_out = Nsl2Sigmoid.eval (stnlatburst,STNlbsigma1, STNlbsigma2,
                                           STNlbsigma3, STNlbsigma4);
  }

} // end class


