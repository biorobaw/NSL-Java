h08098
s 00055/00000/00000
d D 1.1 99/09/24 18:57:30 aalx 1 0
c date and time created 99/09/24 18:57:30 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)STNmedburst.mod	1.8 --- 08/05/99 -- 13:56:40 : jversion  @(#)STNmedburst.mod	1.2---04/23/99--18:39:50 */

nslImport nslAllImports;

//
// STNmedburst
//
/**
 * STNmedburst class
 * Represents the Subthalamic Nucleus Burst Cells Layer
 * @see     STNmedburst
 * @version 0.1 96/11/19
 * @author Michael Crowley
 * -var private GPEmedburst_in - input coming from GPEmedburst module (of type NslDouble2)<p>
 * -var private STNmedburst_out - output going to SNRmedburst module (of type NslDouble2)<p>
 */

nslModule STNmedburst (int CorticalArraySize) {

  public NslDinDouble2 GPEmedburst_in(CorticalArraySize,CorticalArraySize) ;  

  public NslDoutDouble2 STNmedburst_out(CorticalArraySize,CorticalArraySize) ;  

  // private variables


  private double stnmedbursttm;
  private double STNmedburstTONIC;
  private double STNlbsigma1;
  private double STNlbsigma2;
  private double STNlbsigma3;
  private double STNlbsigma4;
  NslDouble2 stnmedburst (CorticalArraySize,CorticalArraySize)   ;
  
   public void initRun () {
    STNmedburst_out=0;
    stnmedbursttm = 0.01;
    STNmedburstTONIC = 60;
    STNlbsigma1 = 10; //20;
    STNlbsigma2 = 60;
    STNlbsigma3 = 0;
    STNlbsigma4 = 60;
  }
  public void simRun () {
	  // System.err.println("@@@@ STNmedburst simRun entered @@@@");
    stnmedburst = nslDiff (stnmedburst,stnmedbursttm, 
                                    - stnmedburst + STNmedburstTONIC - GPEmedburst_in);
    STNmedburst_out = Nsl2Sigmoid.eval(stnmedburst,STNlbsigma1, STNlbsigma2,
                                           STNlbsigma3, STNlbsigma4);
  }

} //end class


E 1
