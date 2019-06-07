/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)CDmedtan.mod	1.8 --- 08/05/99 -- 13:56:10 : jversion  @(#)CDmedtan.mod	1.2---04/23/99--18:39:22 */

// Import statements
nslImport nslAllImports;
//
// CDmedtan
//
/**
 * CDmedtan class
 * Represents the Caudate Tonically Active Cells Layer
 * @see     CDmedtan
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var private SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * -var private CDmedinh_in - input coming from CDmedinh module (of type NslDouble2)<p>
 * -var private CDmedtan_out - output going to CDmedburst module (of type NslDouble2)<p>
 */
nslModule CDmedtan(int CorticalArraySize, int StriatalArraySize){


//input ports
 NslDinDouble2 SNCdop_in     (StriatalArraySize,StriatalArraySize);
 NslDinDouble2 CDmedinh_in    (StriatalArraySize,StriatalArraySize);

//output ports
 NslDoutDouble2 CDmedtan_out  (StriatalArraySize,StriatalArraySize);

  // private variables
  private double cdmedtantm;
  private double cdmedtanTONIC;
  private double CDSNCdopK;
  private double CDltsigma1;
  private double CDltsigma2;
  private double CDltsigma3;
  private double CDltsigma4;
 private NslDouble2 cdmedtan  (StriatalArraySize,StriatalArraySize);


public void initRun () {
 CDmedtan_out=0;

    cdmedtantm = 0.01;
    cdmedtanTONIC = 10;
    CDSNCdopK = 0.5;
    CDltsigma1 = 0;
    CDltsigma2 = 10;
    CDltsigma3 = 0;
    CDltsigma4 = 10;
}

public void simRun () {
  // System.err.println("@@@@ CDmedtanh simRun entered @@@@");
    cdmedtan = nslDiff(cdmedtan,cdmedtantm, 
                              - cdmedtan + cdmedtanTONIC - CDmedinh_in
                              - (CDSNCdopK * nslMaxValue(SNCdop_in)));
    CDmedtan_out = Nsl2Sigmoid.eval( cdmedtan,CDltsigma1, CDltsigma2,
                                     CDltsigma3, CDltsigma4);
 }



} //end class


