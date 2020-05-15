/* SCCS  @(#)CDlatburst.mod	1.1---09/24/99--18:57:10 */
/* old kversion @(#)CDlatburst.mod	1.8 --- 08/05/99 -- 13:56:06 : jversion  @(#)CDlatburst.mod	1.2---04/23/99--18:39:19 */


// Import statements

nslImport nslAllImports;

// Leaf nodes

//
// CDlatburst
//
/*
        Dopamine is modelled as an inhibitory effect on the indirect
        pathway projecting through GPe and STN and as an excitatory
        effect on the direct pathway projecting to GPi/SNr.
*/

/*
        Assume that the effect of dopamine on the projection neurons 
        in the caudate is to manipulate the time constant of the neurons.  
        More dopamine shortens the time constant of the neurons and 
        less dopamine increases the time constant.
*/


/**
 * CDlatburst class
 * Represents the Caudate Burst Cells Layer
 * @see     CDlatburst
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * @var private SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * @var private FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * @var private PFCfovea_in - input coming from PFC module (of type NslDouble2)<p>
 * @var private PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 * @var private CDlattan_in - input coming from CDlattan module (of type NslDouble2)<p>
 * @var private CDindlatburst_out - output going to GPElatburst module (of type NslDouble2)<p>
 * @var private CDdirlatburst_out - output going to SNRlatburst module (of type NslDouble2)<p>
 */

nslModule CDlatburst(int CorticalArraySize)   {
// input ports
    public NslDinDouble2 FEFsac_in()  ;
    public NslDinDouble2 SNCdop_in()  ;
    public NslDinDouble2 PFCgo_in()   ;
    public NslDinDouble2 PFCfovea_in()  ;
    public NslDinDouble2 CDlattan_in()   ;
// output ports
    public NslDoutDouble2 CDindlatburst_out(CorticalArraySize,CorticalArraySize); 
    public NslDoutDouble2 CDdirlatburst_out(CorticalArraySize,CorticalArraySize) ;

  // private variables
    private NslDouble2 cdindlatburst (CorticalArraySize,CorticalArraySize);
    private NslDouble2 cddirlatburst (CorticalArraySize,CorticalArraySize);


  private double CorticalSlowDown;
  private double basecdlatbursttm;
  private double cdlatbursttm;
  private double CDlatfefsacK;
  private double CDlattanK;
  private double CDlatsncK;
  private double CDlatpfcK;
  private double CDlatpfcgoK;
  private double CDlbsigma1;
  private double CDlbsigma2;
  private double CDlbsigma3;
  private double CDlbsigma4;


public void initRun () {

    CDindlatburst_out=0;
    CDdirlatburst_out=0;

    CorticalSlowDown = 1.;
    basecdlatbursttm = 0.01;
    cdlatbursttm = basecdlatbursttm * CorticalSlowDown;
    CDlatfefsacK = 0.85;
    CDlattanK = 2;
    CDlatsncK = 1;
    CDlatpfcK = 0.25;
    CDlatpfcgoK = 0.25;
    CDlbsigma1 = 0;
    CDlbsigma2 = 60; // 90; //99/8/2 aa ???
    CDlbsigma3 = 0;
    CDlbsigma4 = 60;
  }
public void simRun () {
    cdindlatburst = nslDiff (cdindlatburst,cdlatbursttm, 
                  - cdindlatburst + (CDlatfefsacK * FEFsac_in)
                  - (CDlattanK * CDlattan_in)
                  - (CDlatsncK * SNCdop_in)
                  + (CDlatpfcK * PFCfovea_in));

    //PFCgo_in));
    CDindlatburst_out = Nsl2Sigmoid.eval(cdindlatburst,CDlbsigma1, CDlbsigma2,
                                               CDlbsigma3, CDlbsigma4);

    cddirlatburst = nslDiff (cddirlatburst,cdlatbursttm, 
                  - cddirlatburst + (CDlatfefsacK * FEFsac_in)
                  + (CDlatpfcgoK * PFCgo_in)
                  - (CDlattanK * CDlattan_in)
                  + (CDlatsncK * SNCdop_in));

    CDdirlatburst_out = Nsl2Sigmoid.eval(cddirlatburst, CDlbsigma1, CDlbsigma2,
                                               CDlbsigma3, CDlbsigma4);
  }


} //end class


