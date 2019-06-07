/* SCCS  @(#)CrowleyTop.mod	1.1---09/24/99--18:57:15 */
/* old kversion @(#)CrowleyTop.mod	1.8 --- 08/05/99 -- 13:56:14 : jversion  @(#)CrowleyTop.mod	1.2---04/23/99--18:39:23 */

nslImport nslAllImports;

nslModel CrowleyTop() 
{
  static private int CorticalArraySize = 9;
  static private int StriatalArraySize = 90;


  private Element Teacher;
  private int local_simulation_time=0;
  private int half_CorticalArraySize=0;

  // added by Jacob Spoelstra
  public CrowleyFrame cf;
  public SubData subdata;
  public Target TEST;

  // random used in testing only
  // public NslDouble2 random1("random1",CorticalArraySize,CorticalArraySize);  
  private NslInt0 FOVEAX(half_CorticalArraySize);
  private NslInt0 FOVEAY(half_CorticalArraySize);


  private   Generator generator1(CorticalArraySize);
  // input modules that hold single output matrices
  private   VISINPUT visinput(CorticalArraySize);
  private   LC lc(CorticalArraySize);

    // LIP and Thalamus
  private   LIP lip(CorticalArraySize);
  private   Thal thal1(CorticalArraySize);
    // Medial circuit
  private Med med(CorticalArraySize,StriatalArraySize);
    // Lateral Circuit
  private  Lat lat(CorticalArraySize);
  private   SNC snc(CorticalArraySize);
    // Others
  private   PFC pfc(CorticalArraySize);
  private   SC   sc(CorticalArraySize);  
  private   FEF fef(CorticalArraySize);
  private   BSG bsg(CorticalArraySize);

  private   CrowleyOut crowout(CorticalArraySize,StriatalArraySize);

public void initSys(){
    System.out.println("crowleyTop: initSys entered");
    system.setEndTime(0.55);
    system.nslSetRunDelta(0.001);
    system.nslSetBuffering(true);  //all output ports will be double buffered
    half_CorticalArraySize = CorticalArraySize % 2;
}

public void initModule(){
 System.out.println("crowleyTop: initModule entered");
 //med.nslSetAccess('W');
 verbatim_NSLJ;
    TEST=new Target();  
    cf=new CrowleyFrame(visinput,med);
    cf.setVisible(true);  // pure java
  verbatim_off;

}
public void makeConn(){
    // LIP inputs
    nslConnect(visinput.visinput_out , lip.SLIPvis_in);
    nslConnect(thal1.ThLIPmem_out , lip.ThLIPmem_in);

    // Thalamus inputs
    nslConnect(pfc.PFCmem_out ,thal1.PFCmem_in);
    nslConnect(fef.FEFmem_out ,thal1.FEFmem_in);
    nslConnect(lip.LIPmem_out ,thal1.LIPmem_in);
    nslConnect(med.SNRmedburst_out , thal1.SNRmedburst_in);


    // Medial circuit input
    nslConnect (lip.LIPmem_out , med.LIPmem_in);
    nslConnect (snc.SNCdop_out , med.SNCdop_in);
    nslConnect (pfc.PFCgo_out , med.PFCgo_in);
    nslConnect (fef.FEFsac_out , med.FEFsac_in);


    // Lateral Circuit inputs
    /* small dots are not shown in the simulation */
    nslConnect (pfc.PFCgo_out , lat.PFCgo_in);
    nslConnect (snc.SNCdop_out , lat.SNCdop_in);
    nslConnect (pfc.PFCfovea_out , lat.PFCfovea_in);
    nslConnect (fef.FEFsac_out , lat.FEFsac_in);


    // PFC inputs
    /* slightly behavioral incorrect */
    
    nslConnect (lip.LIPvis_out , pfc.LIPvis_in);
    nslConnect (lip.LIPmem_out , pfc.LIPmem_in);
    nslConnect (thal1.ThPFCmem_out , pfc.ThPFCmem_in);
    

    // SC inputs 
    nslConnect(lat.SNRlatburst_out ,sc.SNRlatburst_in);
    nslConnect(fef.FEFsac_out ,sc.FEFsac_in);
    nslConnect(pfc.PFCfovea_out ,sc.PFCfovea_in);
    
    nslConnect(bsg.BSGsaccade_out ,sc.BSGsaccade_in);
    nslConnect(bsg.BSGEyeMovement_out ,sc.BSGEyeMovement_in);
    nslConnect(lip.LIPmem_out ,sc.LIPmem_in);
    
    // FEF inputs
    /* check value at (0,0) */
    nslConnect(lip.LIPmem_out ,fef.LIPmem_in);

    nslConnect(thal1.ThFEFmem_out ,fef.ThFEFmem_in);

    nslConnect(pfc.PFCgo_out ,fef.PFCgo_in);
    nslConnect(pfc.PFCmem_out ,fef.PFCmem_in);

    //nslConnect(pfc.PFCnovelty ,fef.PFCnovelty);
    nslConnect(generator1.generator ,fef.PFCnovelty);
  
    // BSG inputs
    nslConnect(sc.SCbu_out ,bsg.SCbu_in);
    nslConnect(sc.SCsac_out ,bsg.SCsac_in);
    
    // SNC inputs
    /* no missing dots on limbicCortext and SNC*/
    nslConnect(lc.LimbicCortex_out , snc.LimbicCortex_in);
}
public void initRun(){	
 System.out.println("crowleyTop: In initRun entered");
}

public void simRun() {
  local_simulation_time++;
  nslPrintln("Current Time: "+system.getCurTime()+" :cycle"+system.getCurrentCycle());
}

}  //end class


