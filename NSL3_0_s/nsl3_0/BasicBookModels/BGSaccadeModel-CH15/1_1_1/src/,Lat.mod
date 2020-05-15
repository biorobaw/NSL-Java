/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)Lat.mod	1.8 --- 08/05/99 -- 13:56:26 : jversion  @(#)Lat.mod	1.2---04/23/99--18:39:34 */

// Import statements
nslImport nslAllImports;

//
// Lateral Circuit
//
/**
 * Lat class
 * Represents the Lateral Circuit
 * @see    Lat
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 * -var public SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * -var public PFCfovea_in - input coming from PFC module (of type NslDouble2)<p>
 * -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * -var public SNRlatburst_out - output going to SC module (of type NslDouble2)<p>
 */

nslModule Lat(int CorticalArraySize) {
	//input ports
    public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize)  ;
    public NslDinDouble2 SNCdop_in(CorticalArraySize,CorticalArraySize)   ;
    public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize)   ;
    public NslDinDouble2 PFCfovea_in(CorticalArraySize,CorticalArraySize)   ;
	//output ports
    public NslDoutDouble2 SNRlatburst_out(CorticalArraySize,CorticalArraySize)   ;

    // private variables
    private CDlatinh cdlatinh (CorticalArraySize) ;
    private CDlattan cdlattan   (CorticalArraySize)  ;
    private CDlatburst cdlatburst  (CorticalArraySize)   ;
    private GPElatburst gpelatburst  (CorticalArraySize)  ;
    private STNlatburst stnlatburst (CorticalArraySize)   ;
    private SNRlatburst snrlatburst   (CorticalArraySize)  ;
  
  public void makeConn () {
    // module inputs to leaf inputs
    nslRelabel(FEFsac_in,cdlatinh.FEFsac_in);
    nslRelabel(PFCgo_in,cdlatinh.PFCgo_in);
    nslRelabel(SNCdop_in,cdlattan.SNCdop_in);
    nslRelabel(FEFsac_in,cdlatburst.FEFsac_in);
    nslRelabel(SNCdop_in,cdlatburst.SNCdop_in);
    nslRelabel(PFCgo_in,cdlatburst.PFCgo_in);
    nslRelabel(PFCfovea_in,cdlatburst.PFCfovea_in);

    // leaf internslConnectections
    nslConnect(cdlatinh.CDlatinh_out,cdlattan.CDlatinh_in);
    nslConnect(cdlattan.CDlattan_out,cdlatburst.CDlattan_in);
    nslConnect(cdlatburst.CDindlatburst_out,gpelatburst.CDindlatburst_in);
    nslConnect(cdlatburst.CDdirlatburst_out,snrlatburst.CDdirlatburst_in);
    nslConnect(gpelatburst.GPElatburst_out,stnlatburst.GPElatburst_in);
    nslConnect(stnlatburst.STNlatburst_out,snrlatburst.STNlatburst_in);


    // leaf outputs to module outputs
    nslRelabel(snrlatburst.SNRlatburst_out,SNRlatburst_out);

  }

  public void simRun () {
    /*
System.out.println("CDlatinh_out"+CDlatinh_out+"\nCDlattan_out"+CDlattan_out+
"\nCDindlatburst_out"+CDindlatburst_out+
"\nCDdirlatburst_out"+CDdirlatburst_out+
"\nGPElatburst_out"+GPElatburst_out+
"\nSTNlatburst_out"+STNlatburst_out+"\n\n");

*/
  }

} //end class


