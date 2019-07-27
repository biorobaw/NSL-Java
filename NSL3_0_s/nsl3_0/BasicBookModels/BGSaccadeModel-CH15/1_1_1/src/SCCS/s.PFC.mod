h39852
s 00117/00000/00000
d D 1.1 99/09/24 18:57:23 aalx 1 0
c date and time created 99/09/24 18:57:23 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)PFC.mod	1.8 --- 08/05/99 -- 13:56:29 : jversion  @(#)PFC.mod	1.2---04/23/99--18:39:37 */
// Import statements
nslImport nslAllImports;


//
//  Module: PrefrontalCortex
//
//   From PAPER3.TXT, 3.2:
//    The Select Target schema is found within the prefrontal cortex (PFC)
//    in our model.
//   From PAPER3.TXT, 3.5:
//    We place the Issue GO Signal schema within prefrontal cortex.
//   From PAPER3.TXT, 3.4:
//    Thus, we place the Remap Targets schema within the BG.
//    The BG performs this remapping operation upon all potential saccade
//    targets simultaneously and relays the remapped target information to
//    prefrontal cortex and parietal cortex via thalamic relay neurons.

// INPUT:
//  LIPvis
//    MATRIX(LIPvis,CorticalArraySize,CorticalArraySize);
//  LIPmem
//    MATRIX(LIPmem,CorticalArraySize,CorticalArraySize);
//  ThPFCmem
//    MATRIX(ThPFCmem,CorticalArraySize,CorticalArraySize);
//
//  Description:
//   From PAPER3.TXT, 3.7:
//    LIP provides a visual buffer signal that is active for visual targets
//    as well as memory targets.
//   From PAPER3.TXT, 3.1:
//    Our model includes both visually-activated (LIPvis) and memory neurons
//    in LIP (LIPmem).  The visual neurons only respond to visual stimuli
//    (Gnadt & Andersen, 1988) and receive direct visual input in our model.
//    The memory response cells fire continuously during the delay portion
//    of a delay saccade task.
//   From PAPER3.TXT, 3.1:
//    These cells fire even if the stimulus never
//    enters their receptive field when a second saccade is arranged so that
//    it matched the cell's receptive field.  We propose a memory loop is
//    established between these cells in LIP and mediodorsal thalamus.


// OUTPUT:
//  PFCfovea
//    MATRIX(PFCfovea,CorticalArraySize,CorticalArraySize);
//  PFCgo
//    MATRIX(PFCgo,CorticalArraySize,CorticalArraySize);
//  PFCmem
//    MATRIX(PFCmem,CorticalArraySize,CorticalArraySize);
//
//  Description:
//   From PAPER3.TXT, 3.2:
//    Foveal inhibition is represented in prefrontal cortex by PFCfovea which
//    receives its input from the central element of the visual cells in LIP
//    (LIPvis).
//   From PAPER3.TXT, 3.5:
//    We place the Issue GO Signal schema within prefrontal cortex.
//    We will use a neuronal layer (PFCgo) to pass the trigger signal to FEF,
//    LIP and to the BG.  This signal will be used by the receiving layers
//    to increase their activation for the selected saccade target to cause
//    the activation through to the superior colliculus to effect the
//    saccade selected by prefrontal cortex.
//   From PAPER3.TXT, 3.2:
//    PFCmem will provide the next saccade target, if one exists.

nslModule PFC(int array_size) {
    public NslDinDouble2 LIPvis_in (array_size,array_size) ;
    public NslDinDouble2 LIPmem_in (array_size,array_size) ;
    public NslDinDouble2 ThPFCmem_in (array_size,array_size)   ;

    public NslDoutDouble2 PFCfovea_out(array_size,array_size)   ;
    public NslDoutDouble2 PFCgo_out (array_size,array_size)   ;
    public NslDoutDouble2 PFCmem_out  (array_size,array_size) ;

    private PFCfovea PFCfovea1 (array_size)  ;
    private PFCgo PFCgo1 (array_size)   ;
    private PFCmem PFCmem1 (array_size)  ;
    private PFCseq PFCseq1 (array_size)   ;
    private NslInt0 FOVEAX();
    private NslInt0 FOVEAY();

public void initModule()
{
    FOVEAX = (NslInt0)nslGetValue("crowleyTop.FOVEAX");
    FOVEAY = (NslInt0)nslGetValue("crowleyTop.FOVEAY");  
}
  
public void makeConn(){
    // module inputs to leaf inputs
    nslRelabel(LIPvis_in,PFCfovea1.LIPvis_in);
    nslRelabel(LIPmem_in,PFCgo1.LIPmem_in);
    nslRelabel(ThPFCmem_in,PFCmem1.ThPFCmem_in);
    nslRelabel(LIPmem_in,PFCmem1.LIPmem_in);
    nslRelabel(LIPvis_in,PFCseq1.LIPvis_in);

    // leaf internslConnectections
    nslConnect(PFCfovea1.PFCfovea_out,PFCgo1.PFCfovea_in);
    nslConnect(PFCseq1.PFCseq_out,PFCgo1.PFCseq_in);

    nslConnect(PFCseq1.pfcseq_out,PFCmem1.pfcseq_in);
    nslConnect(PFCmem1.PFCmem_out,PFCseq1.PFCmem_in);
    nslConnect(PFCfovea1.PFCfovea_out,PFCseq1.PFCfovea_in);

    // leaf outputs to module outputs
    nslRelabel(PFCfovea1.PFCfovea_out,PFCfovea_out);
    nslRelabel(PFCgo1.PFCgo_out,PFCgo_out);

    nslRelabel(PFCmem1.PFCmem_out,PFCmem_out);
}

} //end class



E 1
