h49442
s 00071/00000/00000
d D 1.1 99/09/24 18:57:18 aalx 1 0
c date and time created 99/09/24 18:57:18 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)GPElatburst.mod	1.8 --- 08/05/99 -- 13:56:20 : jversion  @(#)GPElatburst.mod	1.2---04/23/99--18:39:29 */

// Import statements
nslImport nslAllImports;

//
// GPElatburst
//
/**
 * GPElatburst class
 * Represents the Globus Pallidus External Burst Cells Layer
 * @see    GPElatburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private CDindlatburst_in - input coming from CDlatburst module (of type NslDouble2)<p>
 * -var private GPElatburst_out - output going to STNlatburst module (of type NslDouble2)<p>
 */

nslModule GPElatburst(int CorticalArraySize){
  //input ports
   public NslDinDouble2 CDindlatburst_in (CorticalArraySize,CorticalArraySize)   ;

  //output ports
   public NslDoutDouble2 GPElatburst_out (CorticalArraySize,CorticalArraySize);          

  // private variables

  private double gpelatbursttm;
  private double GPElatburstTONIC;
  private double GPElatburstK;
  private double GPElbsigma1;
  private double GPElbsigma2;
  private double GPElbsigma3;
  private double GPElbsigma4;

  private NslDouble2 gpelatburst (CorticalArraySize,CorticalArraySize)  ;
  

  public void initRun () {
    GPElatburst_out =0;

	//System.err.println("GPElatburst:=1=");
    gpelatbursttm = 0.01;
	//System.err.println("GPElatburst:=2=");
    GPElatburstTONIC = 30;
	//System.err.println("GPElatburst:=3=");
    GPElatburstK = 2;
	//System.err.println("GPElatburst:=4=");
    GPElbsigma1 = 0;
	//System.err.println("GPElatburst:=5=");
    GPElbsigma2 = 60;
	//System.err.println("GPElatburst:=6=");
    GPElbsigma3 = 0;
	//System.err.println("GPElatburst:=7=");
    GPElbsigma4 = 60;
	//System.err.println("GPElatburst:=8=");
  }
  public void simRun () {

      // System.err.println("@@@@ GPElatburst simRun entered @@@@");
    gpelatburst = nslDiff (gpelatburst,gpelatbursttm, 
                                    - gpelatburst + GPElatburstTONIC 
                                                  - (GPElatburstK * CDindlatburst_in));
    GPElatburst_out = Nsl2Sigmoid.eval (gpelatburst,GPElbsigma1, GPElbsigma2,
                                           GPElbsigma3, GPElbsigma4);
  }

} //end class


E 1
