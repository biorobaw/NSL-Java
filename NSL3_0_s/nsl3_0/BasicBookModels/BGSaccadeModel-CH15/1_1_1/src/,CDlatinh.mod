/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)CDlatinh.mod	1.8 --- 08/05/99 -- 13:56:07 : jversion  @(#)CDlatinh.mod	1.2---04/23/99--18:39:20 */
// Import statements
nslImport nslAllImports;

// Leaf nodes

//
// CDlatinh
//
/**
 * CDlatinh class
 * Represents the Caudate Non-dopaminergic Interneuron Cells Layer
 * @see     CDlatinh
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * @var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 * @var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * @var public CDlatinh_out - output going to CDlattan module (of type NslDouble2)<p>
 */
/*---*/

nslModule CDlatinh(int CorticalArraySize)  {
  public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize);
  public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize);
  public NslDoutDouble2 CDlatinh_out(CorticalArraySize,CorticalArraySize);

  // private variables

  private NslDouble2 cdlatinh(CorticalArraySize,CorticalArraySize);

  private double cdlatinhtm;
  private double CDlisigma1;
  private double CDlisigma2;
  private double CDlisigma3;
  private double CDlisigma4;
 

public void initRun () {
    CDlatinh_out=0;
    cdlatinhtm = 0.01;
    CDlisigma1 = 45;
    CDlisigma2 = 90;
    CDlisigma3 = 0;
    CDlisigma4 = 20; // 60;
}
public void simRun () {
  // System.err.println("@@@@ CDlatinh simRun entered @@@@");
    cdlatinh = nslDiff(cdlatinh,cdlatinhtm, 
                              - cdlatinh + FEFsac_in + PFCgo_in);
    CDlatinh_out = Nsl2Sigmoid.eval (cdlatinh,CDlisigma1, CDlisigma2,
                                     CDlisigma3, CDlisigma4);
}



} //end class


