h55037
s 00068/00000/00000
d D 1.1 99/09/24 18:57:28 aalx 1 0
c date and time created 99/09/24 18:57:28 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SNRlatburst.mod	1.8 --- 08/05/99 -- 13:56:37 : jversion  @(#)SNRlatburst.mod	1.2---04/23/99--18:39:48 */

nslImport nslAllImports;

//
// SNRlatburst
//
/**
 * SNRlatburst class
 * Represents the Substantia Nigra pars Reticulata Burst Cells Layer
 * @see    SNRlatburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private CDdirlatburst_in - input coming from CDlatburst module (of type NslDouble2)<p>
 * -var private STNlatburst_in - input coming from STNlatburst module (of type NslDouble2)<p>
 * -var private SNRlatburst_out - output going to SC module (of type NslDouble2)<p>
 */
nslModule SNRlatburst (int CorticalArraySize) {
//input ports
    public NslDinDouble2 CDdirlatburst_in(CorticalArraySize,CorticalArraySize)   ;
    public NslDinDouble2 STNlatburst_in(CorticalArraySize,CorticalArraySize)   ;
//outpu ports
    public NslDoutDouble2 SNRlatburst_out(CorticalArraySize,CorticalArraySize)   ;

  // private variables


  private double snrlatbursttm;
  private double SNRlatburstTONIC;
  private double SNRcdlbK;
  private double SNRstnlbK;
  private double SNRlbsigma1;
  private double SNRlbsigma2;
  private double SNRlbsigma3;
  private double SNRlbsigma4;
  private   NslDouble2 snrlatburst (CorticalArraySize,CorticalArraySize)  ;
  
public void initRun () {

    snrlatbursttm = 0.01;
    SNRlatburstTONIC = 30;
    SNRcdlbK = 1;
    SNRstnlbK = 0.5;
    SNRlbsigma1 = 15;
    SNRlbsigma2 = 60;
    SNRlbsigma3 = 0;
    SNRlbsigma4 = 60;
    snrlatburst = 30;

 	// 99/8/2 aa check on the following
    SNRlatburst_out = Nsl2Sigmoid.eval (snrlatburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);
  }
public void simRun () {
  // System.err.println("@@@@ SNRlatburst simRun entered @@@@");
    snrlatburst = nslDiff (snrlatburst,snrlatbursttm, 
                                    - snrlatburst + SNRlatburstTONIC 
                                                  - (SNRcdlbK * CDdirlatburst_in)
                                                  + (SNRstnlbK * STNlatburst_in));
    SNRlatburst_out = Nsl2Sigmoid.eval (snrlatburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);

  }
} //end class



E 1
