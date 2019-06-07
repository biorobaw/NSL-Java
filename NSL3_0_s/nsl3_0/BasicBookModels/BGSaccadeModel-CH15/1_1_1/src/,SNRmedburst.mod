/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SNRmedburst.mod	1.8 --- 08/05/99 -- 13:56:38 : jversion  @(#)SNRmedburst.mod	1.2---04/23/99--18:39:49 */

nslImport nslAllImports;

// Global variables used in this module
//
// NslInt2 SNRxmap []; 
// NslInt2 SNRymap []; 
// NslDouble2 SNRweights [];
// NslInt2 SNRMapCount;
//

//
// SNRmedburst
//
/**
 * SNRmedburst class
 * Represents the Substantia Nigra pars Reticulata Burst Cells Layer
 * @see     SNRmedburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private CDdirmedburst_in - input coming from 
 CDmedburst module (of type NslDouble2)<p>
 * -var private STNmedburst_in - input coming from 
 STNmedburst module (of type NslDouble2)<p>
 * -var private SNRmedburst_out - output going to 
 Thal module (of type NslDouble2)<p>
 */
nslModule SNRmedburst (int CorticalArraySize,int StriatalArraySize) {

  //input ports
  public NslDinInt2 SNRMapCount_bulk(CorticalArraySize,CorticalArraySize);
  public NslDinDouble3 SNRweights_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
  public NslDinInt3 SNRxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
  public NslDinInt3 SNRymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
   NslDinDouble2 STNmedburst_in(CorticalArraySize,CorticalArraySize);
   NslDinDouble2 CDdirmedburst_in (StriatalArraySize,StriatalArraySize);

  // output ports
   NslDoutDouble2 SNRmedburst_out (CorticalArraySize,CorticalArraySize);

  //privates
 
  private double snrmedbursttm;
  private double SNRmedburstTONIC;
  private double SNRcdlbK;
  private double SNRstnlbK;
  private double SNRlbsigma1;
  private double SNRlbsigma2;
  private double SNRlbsigma3;
  private double SNRlbsigma4;

  private int[][][] SNRxmap;
  private int[][][] SNRymap;
  private double[][][] SNRweights ;
  private int[][] SNRMapCount;


  //NslInt2 SNRMapCount;
   NslDouble2 snrmedburst (CorticalArraySize,CorticalArraySize);
   NslDouble2 SNRcdinput(9,9) ;


public void initModule()  {
/* used when we did not have ports
    SNRxmap_bulk = nslRefParent ("SNRxmap_bulk")   ;
    SNRymap_bulk = nslRefParent ("SNRymap_bulk")  ;
    SNRweights_bulk = nslRefParent ("SNRweights_bulk")   ;
    SNRMapCount_bulk=nslRefParent("SNRMapCount_bulk") ;
*/
    SNRxmap=(int[][][])SNRxmap_bulk.get();
    SNRymap=(int[][][])SNRymap_bulk.get(); 
    SNRweights=(double[][][])SNRweights_bulk.get();
    SNRMapCount = (int[][]) SNRMapCount_bulk.get();

}

  // This function is also called in the lib.h module by:
  // TestConnections, TestFoveaMapping

public void initRun () {
    snrmedbursttm = 0.01;
    SNRmedburstTONIC = 30;
    SNRcdlbK = 1;
    SNRstnlbK = 0.5;
    SNRlbsigma1 = 15;
    SNRlbsigma2 = 60;
    SNRlbsigma3 = 0;
    SNRlbsigma4 = 60;
    snrmedburst = 30;
    SNRmedburst_out = Nsl2Sigmoid.eval(snrmedburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);

  }
  public void simRun () {
    int tempint;
    /* <Q> SNRcdinput? */
  // System.err.println("@@@@ SNRmedburst simRun entered @@@@");

    tempint = SumCDtoSNR (CDdirmedburst_in, SNRcdinput);
//    System.out.println("CD.max "+CDdirmedburst_in.max() + "\nSNR "+SNRcdinput);
    snrmedburst = nslDiff (snrmedburst,snrmedbursttm, 
                                    - snrmedburst + SNRmedburstTONIC 
                                    - (SNRcdlbK * SNRcdinput)
                                    + (SNRstnlbK * STNmedburst_in));
    SNRmedburst_out = Nsl2Sigmoid.eval(snrmedburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);
//System.out.println("CDdirmedburst "+CDdirmedburst_in);
//System.out.println("SNRcdinput "+SNRcdinput);
 }


public int SumCDtoSNR (NslDouble2 CD, NslDouble2 SNR) 
  {
  //  This function sums the activity in the medial CD circuit onto 
  //  the medial SNR circuit through SNRweights, SNRxmap and SNRymap.

    int i, j, k, xmaploc, ymaploc;

verbatim_NSLJ;
    if (CD==null) System.err.println("CD null!!!!");
    if (SNR==null) System.err.println("SNR null!!!!");
verbatim_off;
     // System.err.println("SNRmedburst.SumCDtoSNR: entered....");
    SNR = 0;  // Ensure new mapping only
     // System.err.println("SNRmedburst.SumCDtoSNR: A");
     //System.err.println("SNRMapCount:"+SNRMapCount);
    for (i = 0; i < CorticalArraySize; i ++)
      for (j = 0; j < CorticalArraySize; j ++) {
        for (k = 0; k < SNRMapCount [i][j]; k ++) {
    //System.err.println("SNRmedburst.SumCDtoSNR: loop: ("+i+","+j+","+k+")");
          xmaploc = SNRxmap [i][j][k];
          ymaploc = SNRymap [i][j][k];
          SNR [i][j] = SNR[i][j] + CD [xmaploc][ymaploc] * SNRweights [i][j][k];
        }
      }
    return 0;
}

} //end class




