/* SCCS  @(#)CDmedinh.mod	1.1---09/24/99--18:57:12 */
/* old kversion @(#)CDmedinh.mod	1.8 --- 08/05/99 -- 13:56:09 : jversion  @(#)CDmedinh.mod	1.2---04/23/99--18:39:21 */

// Import statements
verbatim_NSLJ;
import nslj.src.lang.*;
import nslj.src.math.*;
verbatim_off;
//
// Global variables used in this module
//
// NslInt2 FEFxmap [];
// NslInt2 FEFymap [];
// NslInt2 LIPxmap [];
// NslInt2 LIPymap [];
//
//

//
// CDmedinh
//
/**
 * CDmedinh class
 * Represents the Caudate Non-dopaminergic Interneuron Cells Layer
 * @see     CDmedinh
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * -var public LIPmem_in - input coming from LIP module (of type NslDouble2)<p>
 * -var public CDmedinh_out - output going to CDmedtan module (of type NslDouble2)<p>
 */
nslModule CDmedinh(int CorticalArraySize,int StriatalArraySize) extends Func(CorticalArraySize)
{
  // input ports
  public NslDinInt3  FEFxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
  public NslDinInt3  FEFymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
  public NslDinInt3  LIPxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
  public NslDinInt3  LIPymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize);
 public    NslDinDouble2 FEFsac_in  (CorticalArraySize,CorticalArraySize) ;
 public    NslDinDouble2 LIPmem_in  (CorticalArraySize,CorticalArraySize);

  // output ports
 public    NslDoutDouble2 CDmedinh_out (StriatalArraySize,StriatalArraySize);

  // private variables
  private double cdmedinhtm;
  private double CDlisigma1;
  private double CDlisigma2;
  private double CDlisigma3;
  private double CDlisigma4;

  private int[][][]  FEFxmap;
  private int[][][]  FEFymap;
  private int[][][]  LIPxmap;
  private int[][][]  LIPymap;


 private   NslDouble2 CDfefinput  (StriatalArraySize,StriatalArraySize);
 private   NslDouble2 CDlipinput  (StriatalArraySize,StriatalArraySize);
 private   NslDouble2 cdmedinh   (StriatalArraySize,StriatalArraySize);

public void initModule()
{
    /*
    FEFxmap_bulk = nslRefParent() ;
    FEFymap_bulk = nslRefParent() ;
    LIPxmap_bulk = nslRefParent() ;
    LIPymap_bulk = nslRefParent() ;
    */
    FEFxmap =(int[][][])FEFxmap_bulk.get();
    FEFymap =(int[][][])FEFymap_bulk.get(); 
    LIPxmap =(int[][][])LIPxmap_bulk.get();
    LIPymap =(int[][][])LIPymap_bulk.get();
}


public void initRun () {
    CDmedinh_out =0;
    cdmedinhtm = 0.01;
    CDlisigma1 = 45;
    CDlisigma2 = 90;
    CDlisigma3 = 0;
    CDlisigma4 = 60;
}
public void simRun () {
    int tempint;

   // System.err.println("@@@@ CDmedinh simRun entered @@@@");

    tempint = SetCD (CDfefinput, FEFxmap , FEFymap , FEFsac_in);
    tempint = SetCD (CDlipinput, LIPxmap , LIPymap , LIPmem_in);

    cdmedinh = nslDiff (cdmedinh,cdmedinhtm, 
                              - cdmedinh + CDfefinput + CDlipinput);

    CDmedinh_out = Nsl2Sigmoid.eval(cdmedinh,CDlisigma1, CDlisigma2,
                                     CDlisigma3, CDlisigma4);
  }

} // end class






















