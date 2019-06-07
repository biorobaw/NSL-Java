h51456
s 00055/00000/00000
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
/* old kversion @(#)SNC.mod	1.8 --- 08/05/99 -- 13:56:37 : jversion  @(#)SNC.mod	1.2---04/23/99--18:39:48 */

nslImport nslAllImports;


//
// SNC module
//
/**
 * SNC class
 * Represents the Substantia Nigra pars Compacta Layer
 * @see    SNC
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private SNCdop_out - output going to CDlatburst and CDmedburst modules (of type NslDouble2)<p>
 */

nslModule SNC (int CorticalArraySize ){
	//input ports
   public NslDinDouble2 LimbicCortex_in(CorticalArraySize,CorticalArraySize);
	//output ports
   public NslDoutDouble2 SNCdop_out(CorticalArraySize,CorticalArraySize);  

//privates
 

   private NslDouble2 sncdop (CorticalArraySize,CorticalArraySize);

  // private variables
  private double sncdoptm;
  private double SNCdopsigma1;
  private double SNCdopsigma2;
  private double SNCdopsigma3;
  private double SNCdopsigma4;

  public void initRun () {
    sncdoptm = 0.01;
    SNCdopsigma1 = 0;
    SNCdopsigma2 = 60;
    SNCdopsigma3 = 0;
    SNCdopsigma4 = 20; //10;
  }

  public void simRun () {
  // System.err.println("@@@@ SNC simRun entered @@@@");
    sncdop = nslDiff (sncdop,sncdoptm, 
                          - sncdop + LimbicCortex_in);
    SNCdop_out = Nsl2Sigmoid.eval (sncdop,SNCdopsigma1, SNCdopsigma2,
                                 SNCdopsigma3, SNCdopsigma4);
  }

} //end class


E 1
