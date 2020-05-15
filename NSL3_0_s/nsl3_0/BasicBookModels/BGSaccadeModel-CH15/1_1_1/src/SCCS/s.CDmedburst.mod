h21405
s 00200/00000/00000
d D 1.1 99/09/24 18:57:12 aalx 1 0
c date and time created 99/09/24 18:57:12 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)CDmedburst.mod	1.8 --- 08/05/99 -- 13:56:08 : jversion  @(#)CDmedburst.mod	1.2---04/23/99--18:39:21 */

// Import statements
nslImport nslAllImports;

// Global variables used in this module via enableAccess in Med.mod
//
// NslInt2 FEFxmap [];
// NslInt2 FEFymap [];
// NslInt2 LIPxmap [];
// NslInt2 LIPymap [];
// NslInt2 PFCxmap [];
// NslInt2 PFCymap [];
//


//
// CDmedburst
//
/*
        Dopamine is modelled as an inhibitory effect on the indirect
        pathway projecting through GPe and STN and as an excitatory
        effect on the direct pathway projecting to GPi/SNr.
*/

/*
        Also, assume that the effect of dopamine on the projection neurons in 
        the caudate is to manipulate the time constant of the neurons.  More
        dopamine shortens the time constant of the neurons and less dopamine
        increases the time constant.  This makes the CD more or less responsive
        the inputs from the cortex.
*/

/**
 * CDmedburst class
 * Represents the Caudate Burst Cells Layer
 * @see     CDmedburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * -var public LIPmem_in - input coming from LIP module (of type NslDouble2)<p>
 * -var public SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * -var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 * -var public CDmedtan_in - input coming from CDmedtan module (of type NslDouble2)<p>
 * -var public CDindmedburst_out - output going to GPEmedburst module (of type NslDouble2)<p>
 * -var public CDdirmedburst_out - output going to SNRmedburst module (of type NslDouble2)<p>
 */

nslModule CDmedburst(int CorticalArraySize,int StriatalArraySize) extends Func(CorticalArraySize) {
	public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize); 
 	public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize); 
	public NslDinDouble2 LIPmem_in(CorticalArraySize,CorticalArraySize);
	
 	public NslDinDouble2 SNCdop_in(StriatalArraySize,StriatalArraySize); 

	public NslDinDouble2 CDmedtan_in(StriatalArraySize,StriatalArraySize); 


     public NslDinInt2 SNRMapCount_bulk(CorticalArraySize,CorticalArraySize); 

     public NslDinInt3 FEFxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinInt3 FEFymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinInt3 LIPxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinInt3 LIPymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinInt3 PFCxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinInt3 PFCymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 
     public NslDinDouble3 SNRweights_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); 

   // output ports
 public NslDoutDouble2 CDindmedburst_out (CorticalArraySize,CorticalArraySize);
			
 public NslDoutDouble2 CDdirmedburst_out (StriatalArraySize,StriatalArraySize);

//variables

     private NslDouble0 LearnRate(); 

 private NslDouble2 cdindmedburst (CorticalArraySize,CorticalArraySize); 
 private NslDouble2 cddirmedburst (StriatalArraySize,StriatalArraySize);
 private NslDouble2 SNCdopmed (StriatalArraySize,StriatalArraySize);
 private NslDouble2 CDfefinput(StriatalArraySize,StriatalArraySize);
 private NslDouble2 CDlipinput(StriatalArraySize,StriatalArraySize);
 private NslDouble2 CDpfcinput(StriatalArraySize,StriatalArraySize);

  // private variables
  private double SNCdopmax;
  private double CorticalSlowDown;
  private double basecdmedbursttm;
  private double cdmedbursttm;
  private double CDfefinputK;
  private double CDlipinputK;
  private double CDpfcinputK;
  private double CDmedtanK;
  private double CDmedsncK;
  private double CDmedfefsacK;
  private double CDmedpfcgoK;
  private double CDmbsigma1;
  private double CDmbsigma2;
  private double CDmbsigma3;
  private double CDmbsigma4;

  int FEFxmap[][][];
  int FEFymap[][][];
  int LIPxmap[][][];
  int LIPymap[][][];
  int PFCxmap[][][];
  int PFCymap[][][];  
  double SNRweights[][][];
  int SNRMapCount[][];

public void initSys() {
	System.err.println("CDmedburst:initSys");
}
public void initModule() {

LearnRate= (NslDouble0)nslGetValue("crowleyTop.med.LearnRate");
/* now using ports instead
FEFxmap_bulk = nslRefParent("FEFxmap_bulk");
FEFymap_bulk = nslRefParent("FEFymap_bulk");
LIPxmap_bulk = nslRefParent("LIPxmap_bulk");
LIPymap_bulk = nslRefParent("LIPymap_bulk");
PFCxmap_bulk = nslRefParent("PFCxmap_bulk");
PFCymap_bulk = nslRefParent("PFCymap_bulk");
SNRweights_bulk  = nslRefParent("SNRweights_bulk");
SNRMapCount_bulk = nslRefParent("SNRMapCount_bulk");
*/

    FEFxmap = (int[][][])FEFxmap_bulk.get();
    FEFymap = (int[][][])FEFymap_bulk.get();
    LIPxmap = (int[][][])LIPxmap_bulk.get();
    LIPymap = (int[][][])LIPymap_bulk.get();
    PFCxmap = (int[][][])PFCxmap_bulk.get();
    PFCymap = (int[][][])PFCymap_bulk.get();
    SNRweights = (double[][][])SNRweights_bulk.get();
    SNRMapCount = (int[][])SNRMapCount_bulk.get();
}

public void initRun () {

 CDindmedburst_out=0;
			
 CDdirmedburst_out=0;

    CorticalSlowDown = 1.;
    basecdmedbursttm = 0.01;
    cdmedbursttm = basecdmedbursttm * CorticalSlowDown;
    CDfefinputK = 1.0;
    CDlipinputK = 1.0;
    CDpfcinputK = 1.0;
    CDmedtanK = 2;
    CDmedsncK = 1;
    CDmedfefsacK = 0.35;
    CDmedpfcgoK = 0.5;
    CDmbsigma1 = 25;
    CDmbsigma2 = 90;
    CDmbsigma3 = 0;
    CDmbsigma4 = 60;

  }
public void simRun () {
    int tempint;
  // System.err.println("@@@@ CDmedburst simRun entered @@@@");

    SNCdopmax = nslMaxValue(SNCdop_in);
    SNCdopmed = SNCdopmax;
    /* <Q> where do FEFsac_in, LIPmem_in come from? */

    //System.err.println("===== CDmedburst[1] Calling SetCD");
    tempint = SetCD (CDfefinput, FEFxmap, FEFymap, FEFsac_in);
//    tempint = SetCD (CDlipinput, LIPxmap, LIPymap, LIPmem_in);
    //System.err.println("===== CDmedburst[2] Calling SetCD");
    tempint = SetCD (CDlipinput, LIPxmap, LIPymap, LIPmem_in);
//nslprintln("\tCDlip "+nslMax(CDlipinput));
    //System.err.println("===== CDmedburst[3] Calling SetCD");
     tempint = SetCD (CDpfcinput, PFCxmap, PFCymap, PFCgo_in);

    cdindmedburst = nslDiff (cdindmedburst,cdmedbursttm, 
                                        - cdindmedburst 
					+ (CDmedfefsacK * LIPmem_in)
                                        - (CDmedsncK * SNCdop_in)
                                        + (CDmedpfcgoK * nslMaxValue(PFCgo_in)));
    CDindmedburst_out = Nsl2Sigmoid.eval (cdindmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4);

    cddirmedburst = nslDiff (cddirmedburst,cdmedbursttm, 
                                        - cddirmedburst 
					+ (CDfefinputK * CDfefinput)
                                        + (CDlipinputK * CDlipinput)
                                        + (CDpfcinputK * CDpfcinput)
                                        - (CDmedtanK * CDmedtan_in)
                                        + (CDmedsncK * SNCdopmed));

    CDdirmedburst_out = Nsl2Sigmoid.eval(cddirmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4);
  }

} //end class


E 1
