/* SCCS  @(#)Med.mod	1.1---09/24/99--18:57:22 */
/* old kversion @(#)Med.mod	1.8 --- 08/05/99 -- 13:56:27 : jversion  @(#)Med.mod	1.2---04/23/99--18:39:34 */

nslImport nslAllImports;

//
// Medial Circuit
//
/**
 * Med class
 * Represents the Medial Circuit
 * @see     Med
 * @version 0.1 96/11/19
 * @author 
 * -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 * -var public LIPmem_in - input coming from LIP module (of type NslDouble2)<p>
 * -var public SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * -var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 * -var public SNRmedburst_out - output going to Thal module (of type NslDouble2)<p>
 */
nslModule Med (int CorticalArraySize,int StriatalArraySize) extends Func (CorticalArraySize) {
   //input ports
    public NslDinDouble2 FEFsac_in (CorticalArraySize,CorticalArraySize)  ;
    public NslDinDouble2 SNCdop_in (CorticalArraySize,CorticalArraySize)  ;
    public NslDinDouble2 PFCgo_in  (CorticalArraySize,CorticalArraySize) ;
    public NslDinDouble2 LIPmem_in (CorticalArraySize,CorticalArraySize)  ;

   // output ports
   public NslDoutDouble2 CDdirmedburst_out (StriatalArraySize,StriatalArraySize)  ; 
   public NslDoutDouble2 SNRmedburst_out(CorticalArraySize,CorticalArraySize)   ;

   public NslDoutDouble2 CDmedinh_out (StriatalArraySize,StriatalArraySize) ;
   public NslDoutDouble2 CDmedtan_out(StriatalArraySize,StriatalArraySize)  ;
   public NslDoutDouble2 CDindmedburst_out(CorticalArraySize,CorticalArraySize);
   public NslDoutDouble2 GPEmedburst_out (CorticalArraySize,CorticalArraySize);
   public NslDoutDouble2 STNmedburst_out(CorticalArraySize,CorticalArraySize) ;

  //variables
 
  static  private boolean CONSOLE_IO = false;
  
  //Number of times to "train" CD-SNr nslConnectections
  private static int NumberIterations = 10;
  private Element LearnedElements;
  private Element UnlearnedElements;
  private Element Teacher;

  // See MappingParameters
  private int FEFPatchCount;
  private int LIPPatchCount;
  private int PFCPatchCount;

  private int FEFPatchSize;
  private int LIPPatchSize;
  private int PFCPatchSize;

  private double FEFfill;
  private double LIPfill;
  private double PFCfill;

  private double ConnectionChance;
  //LearnRate use to be in CrowleyTop
  private  NslDouble0 LearnRate(.005); // 99/8/8 aa: calculated from value found in Element.mod
  // commented out 99/8/2 aa
  //private java.util.Random rand = new java.util.Random(599);

  static  private int MaxConnections = 50;
  static  private int MaxCenters = 16;

//  private int CDtemp[StriatalArraySize][StriatalArraySize];

    // mirror temporary variables

    private NslDouble2 FEFsac_mirror (CorticalArraySize,CorticalArraySize)   ;
    private NslDouble2 PFCgo_mirror  (CorticalArraySize,CorticalArraySize)  ;
    private NslDouble2 LIPmem_mirror (CorticalArraySize,CorticalArraySize) ;


    private CDmedinh cdmedinh (CorticalArraySize,StriatalArraySize)  ;
    private CDmedtan cdmedtan  (CorticalArraySize,StriatalArraySize)  ;
    private CDmedburst cdmedburst  (CorticalArraySize,StriatalArraySize) ;
    private GPEmedburst gpemedburst(CorticalArraySize)  ;
    private STNmedburst stnmedburst   (CorticalArraySize) ;
    private SNRmedburst snrmedburst  (CorticalArraySize,StriatalArraySize) ;

    private MedBulk medbulk1(CorticalArraySize,MaxConnections) ;

    private NslInt0  FOVEAX();
    private NslInt0  FOVEAY();


public void initModule() {
     //LearnRate=(NslDouble0)nslGetValue("crowleyTop.LearnRate");
     //LearnRate.nslSetAccess('W');
     FOVEAX=(NslInt0)nslGetValue("crowleyTop.FOVEAX");
     FOVEAY=(NslInt0)nslGetValue("crowleyTop.FOVEAY");

     /* this was before we were using ports
     this is the same as givin the variable read access
     which is now the default.
     enableAccess(FEFxmap_bulk);
     enableAccess(FEFymap_bulk);
     enableAccess(LIPxmap_bulk);
     enableAccess(LIPymap_bulk);
     enableAccess(PFCxmap_bulk);
     enableAccess(PFCymap_bulk);

     enableAccess(SNRxmap_bulk);
     enableAccess(SNRymap_bulk);
     enableAccess(SNRweights_bulk);
     enableAccess(SNRMapCount_bulk);
    */

    // 99/8/2 aa: could use var.memAlloc here

    verbatim_NSLJ;

    Teacher=new Element();
    LearnedElements= new Element() ;
    UnlearnedElements= new Element() ;

    verbatim_off;
}

public void makeConn () {
    // module inputs to leaf inputs
    nslRelabel(FEFsac_in,cdmedinh.FEFsac_in);
    nslRelabel(FEFsac_in,cdmedburst.FEFsac_in);
    nslRelabel(LIPmem_in,cdmedinh.LIPmem_in);
    nslRelabel(LIPmem_in,cdmedburst.LIPmem_in);
    nslRelabel(SNCdop_in,cdmedtan.SNCdop_in);
    nslRelabel(SNCdop_in,cdmedburst.SNCdop_in);
    nslRelabel(PFCgo_in,cdmedburst.PFCgo_in);


    // leaf internslConnectections
    nslConnect(cdmedinh.CDmedinh_out,cdmedtan.CDmedinh_in);
    nslConnect(cdmedtan.CDmedtan_out,cdmedburst.CDmedtan_in);
    nslConnect(cdmedburst.CDindmedburst_out,gpemedburst.CDindmedburst_in);
    nslConnect(cdmedburst.CDdirmedburst_out,snrmedburst.CDdirmedburst_in);
    nslConnect(gpemedburst.GPEmedburst_out,stnmedburst.GPEmedburst_in);
    nslConnect(stnmedburst.STNmedburst_out,snrmedburst.STNmedburst_in);

     // bulk output to CDmedburst 
     nslConnect(medbulk1.SNRMapCount_bulk, cdmedburst.SNRMapCount_bulk);
     nslConnect(medbulk1.FEFxmap_bulk, cdmedburst.FEFxmap_bulk);
     nslConnect(medbulk1.FEFymap_bulk, cdmedburst.FEFymap_bulk);
     nslConnect(medbulk1.LIPxmap_bulk,cdmedburst.LIPxmap_bulk);
     nslConnect(medbulk1.LIPymap_bulk,cdmedburst.LIPymap_bulk );
     nslConnect(medbulk1.PFCxmap_bulk,cdmedburst.PFCxmap_bulk);
     nslConnect(medbulk1.PFCymap_bulk,cdmedburst.PFCymap_bulk);
     nslConnect(medbulk1.SNRweights_bulk,cdmedburst.SNRweights_bulk);

     // bulk output to Cdmedinh 
     nslConnect(medbulk1.FEFxmap_bulk, cdmedinh.FEFxmap_bulk);
     nslConnect(medbulk1.FEFymap_bulk, cdmedinh.FEFymap_bulk);
     nslConnect(medbulk1.LIPxmap_bulk,cdmedinh.LIPxmap_bulk);
     nslConnect(medbulk1.LIPymap_bulk,cdmedinh.LIPymap_bulk );

     // bulk output to SNRmedburst
     nslConnect(medbulk1.SNRMapCount_bulk, snrmedburst.SNRMapCount_bulk);
     nslConnect(medbulk1.SNRweights_bulk,snrmedburst.SNRweights_bulk);
     nslConnect(medbulk1.SNRxmap_bulk,snrmedburst.SNRxmap_bulk);
     nslConnect(medbulk1.SNRymap_bulk,snrmedburst.SNRymap_bulk);

     // leaf outputs to module outputs
     nslRelabel(snrmedburst.SNRmedburst_out,SNRmedburst_out);
     nslRelabel(cdmedinh.CDmedinh_out,CDmedinh_out);
     nslRelabel(cdmedtan.CDmedtan_out,CDmedtan_out);
     nslRelabel(cdmedburst.CDindmedburst_out,CDindmedburst_out);
     nslRelabel(gpemedburst.GPEmedburst_out,GPEmedburst_out);
     nslRelabel(stnmedburst.STNmedburst_out,STNmedburst_out);
    // for init functions only
    nslRelabel(cdmedburst.CDdirmedburst_out,CDdirmedburst_out);

}

public void initRun () {
    MakeMapping(medbulk1);
    TestConnections();
    TestFoveaMapping();
}

  public void simRun () {


/*
System.out.println(
"\nSNRmedburst "+SNRmedburst_out+
"\nCDdirmedburst "+CDdirmedburst_out[0][0]+
"\nCDmedinh "+CDmedinh_out[0][0]+
"\nCDmedtan "+CDmedtan_out[0][0]+
"\nCDindmedburst "+CDindmedburst_out+
"\nGPEmedburst "+GPEmedburst_out+
"\nSTNmedburst "+STNmedburst_out);
*/

}

public void MakeMapping(MedBulk medbulk1) {
    int i, j, temp;
    
    int[][] FEFCenters = new int[MaxCenters][2];
    int[][] LIPCenters = new int[MaxCenters][2];
    int[][] PFCCenters = new int[MaxCenters][2];
    int FEFoffset, LIPoffset, PFCoffset;
 
    int iMinCell, jMinCell, MapSize;
 
    int overcrowded = 0;
    int FEFcount    = 0;
    int LIPcount    = 0;
    int PFCcount    = 0;



    MappingParameters();

    FEFoffset = FEFPatchSize /2;
    LIPoffset = LIPPatchSize /2;
    PFCoffset = PFCPatchSize /2;

    MapSize = StriatalArraySize /3;
    for(i=0; i<CorticalArraySize; i++) {
      iMinCell = (i/3) * MapSize;
      for(j=0; j<CorticalArraySize; j++) {
	jMinCell = (j/3) * MapSize;
	CalcCentroids(FEFPatchCount, FEFCenters, 
		      iMinCell, jMinCell, MapSize);
	CalcCentroids(LIPPatchCount, LIPCenters, 
		      iMinCell, jMinCell, MapSize);
	CalcCentroids(PFCPatchCount, PFCCenters, 
		      iMinCell, jMinCell, MapSize);
    /* Establish the nslConnectectivity between each cortical
       region and the caudate using the region centers
       and the calculated offsets.
       */

	PFCcount += 
	  MakeConnections(PFCCenters, PFCoffset, 
			  medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(),
			  PFCfill, i, j);
	FEFcount += 
	  MakeConnections(FEFCenters, FEFoffset, 
			  medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(),
			  FEFfill, i, j);
	LIPcount += 
	  MakeConnections(LIPCenters, LIPoffset, 
			  medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), 
			  LIPfill, i, j);
      }
    }

    /* Establish the direct path mapping from CD to SNr
       */
    SNRMapping(medbulk1,medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(), FEFPatchCount, MapSize);
    SNRMapping(medbulk1,medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPPatchCount, MapSize);
    SNRMapping(medbulk1,medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCPatchCount, MapSize);

    // Output each cortical nslConnectection array for comparison purposes
    OutputMapping(medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get());
    OutputMapping(medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get());
    OutputMapping(medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get());
}

// See FUNC(MappingParameters)
public void MappingParameters() {
    if (!CONSOLE_IO) {
      MappingParameters(9, 3, 0.5, 0.5, 0.05);
      return;
    }
      
   LIPPatchCount = 
      FEFPatchCount =
      PFCPatchCount = 
      nslj.src.system.Console.readInt("Enter the number of patches (max 16) > ");

    LIPPatchSize =
      FEFPatchSize =
      PFCPatchSize = 
      nslj.src.system.Console.readInt("Enter patch size > ");

    LIPfill =
      FEFfill = 
      PFCfill =
      nslj.src.system.Console.readDouble("Enter fill ratio > ");

    ConnectionChance = 
      nslj.src.system.Console.readDouble("Enter nslConnectection percentage (0-1) > ");
    LearnRate.set(
      nslj.src.system.Console.readDouble("Enter learning rate (~0.05) >"));
    //99/8/8 aa: added line above : a value of 0.05 does not agree
    // with what is in Element.mod. Element.mod has .005.
  }  

public void MappingParameters(int count, int size, double fill,double nslConnectection, double learn) {
    LIPPatchCount = 
      FEFPatchCount =
      PFCPatchCount = count;
    LIPPatchSize =
      FEFPatchSize =
      PFCPatchSize = size;
    LIPfill =
      FEFfill = 
      PFCfill = fill;
    ConnectionChance = nslConnectection;
    LearnRate.set(learn);
}
public void CalcCentroids(int count, int[][] arr, int imin, int jmin, int size) {
    int i, j, area, side;


    side = (int)java.lang.Math.sqrt(count);
    area = size / side;

    for (i=0; i<side; i++ ) {
      for (j=0; j<side; j++) {

//	arr[i*side+j][0] = (int)(rand.nextDouble()*area + (area*i)+ imin);
//	arr[i*side+j][1] = (int)(rand.nextDouble()*area + (area*j)+jmin);


	arr[i*side+j][0] = (int)(nslRandom()*area + (area*i)+ imin);
	arr[i*side+j][1] = (int)(nslRandom()*area + (area*j)+jmin);

      }
    }
}


public int MakeConnections(int[][] centers, int offset, int[][][] xmap, int[][][] ymap, double fill, int iindex, int jindex) {
    int count, xstart, ystart, xend, yend;
    int i, j;
    int[] _savei;
    int[] _savej;
    int ptr;
    double temp;
    count = 0;
    ptr = 0;

    int[][] CDtemp = new int[StriatalArraySize][StriatalArraySize];

    // very dirty trick, don't try it at home
    _savei=new int[1];
    _savej=new int[1];

    while (centers[count][0]!=0 || centers[count][1]!=0) {
      // Loop while nonzero values exist in centers
      xstart = (centers[count][0]-offset>0)? centers[count][0]-offset: 0;
      ystart = (centers[count][1]-offset>0)? centers[count][1]-offset: 0;
      xend = (centers[count][0]+offset+1<StriatalArraySize-1)?
	centers[count][0]+offset+1:StriatalArraySize-1;
      yend = (centers[count][1]+offset+1<StriatalArraySize-1)?
	centers[count][1]+offset+1:StriatalArraySize-1;

      for(i=xstart; i<xend; i++)
	for(j=ystart; j<yend; j++) {
	  // Determine nslConnectections
	  //??\\
//	  temp = (rand.nextDouble());
	  temp = nslRandom();
	  if ((temp < fill) && (ptr<MaxConnections)) {
	    if(CDtemp[i][j] == 0) {
	      CDtemp[i][j]++;
	      /*
	      loc = ((jindex*MaxConnections)+ptr) +
		     ( CorticalArraySize*MaxConnections*iindex);
		     */
	      xmap[iindex][jindex][ptr] = i;
	      ymap[iindex][jindex][ptr] = j;
	      ptr++;
	    }
	    else {
	      _savei[0]=0; _savej[0] = 0;
	      if(CheckNeighbors(CDtemp, i, j,StriatalArraySize,
				_savei, _savej)) {
		CDtemp[_savei[0]][_savej[0]]++;
		/*
		loc = ((jindex*MaxConnections)+ptr) +
		     ( CorticalArraySize*MaxConnections*iindex);
		     */
		xmap[iindex][jindex][ptr] = _savei[0];
		ymap[iindex][jindex][ptr] = _savej[0];
		ptr++;
	      }
	    }
	    
	    
	  }
	  
	}
      
      count++;
    }
    return ptr;
  }
  
public boolean CheckNeighbors( int[][] arr, int x, int y, int max, int[] outx, int[] outy) {
    int index, xindex, yindex;
    int[] xarr= {-1, 0, 1, 1, 1, 0,-1,-1};
    int[] yarr= {-1,-1,-1, 0, 1, 1, 1, 0};
    //boolean found;
    int count;
    //found = false;
    count = 0;
//    index = (int)(rand.nextDouble()*8);
    index = (int)(nslRandom()*8);

    while ((count<8)) {
      xindex = minmax( x+xarr[index], 0, max-1);
      yindex = minmax(y+yarr[index],0,max-1);
      if (arr[xindex][yindex]==0) {
	outx[0] = xindex;
	outy[0] = yindex;
	return true;
      }
      count++;
      index = (index+1)%8;
    } 
    //    overcrowded++;
    return false;

}

public int SNRMapping(MedBulk medbulk1, int[][][] xmap, int[][][] ymap, int numzones, int size) 
{
    /*
      This function maps the CD nslConnectections to the SNr
      size contains the length of each side of a CD zone.
      */
    int i, j, k, l, kindex;
    int ptr;

    int numside = (int)java.lang.Math.sqrt(numzones);
    int zonesize =(int)( size / numside ) ;
    int imin, jmin, xDiff, yDiff;
    int izone, jzone;
    int SNRsize = 3;
    int SNRimin, SNRjmin, SNRimax, SNRjmax;
    
    int[][] temparr = new int[3][3];
    int numloc = 0;

    // added for repeated initialization \\
    for(i=0; i<CorticalArraySize; i++ )  {
        for(j=0; j<CorticalArraySize; j++ )  { 
	  medbulk1.SNRMapCount_bulk.set(i,j, 0);
	}	
    }	

    for(i=0; i<CorticalArraySize; i++ )  {
        for(j=0; j<CorticalArraySize; j++ )  { 
//          First task is to read each xmap, ymap value and determine the
//          CD zone to which they belong.  Then determine which subzone
//          within that zone.  Last job is to then populate the SNr neurons
//          in the corresponding SNr zone with this CD nslConnectection.
	  ptr = 0;
	  while ((ptr<MaxConnections)&&(( xmap[i][j][ptr]!=0) || (ymap[i][j][ptr]!=0))) {
	    numloc++;
//              Have a single xmap,ymap value.  Determine subzone in CD.
	    imin    = (i/3) * size;
	    jmin = (j/3) * size;
	    xDiff = xmap[i][j][ptr] - imin;
	    yDiff = ymap[i][j][ptr] - jmin;
	    izone   = minmax( xDiff / zonesize, 0, numside-1 );
	    jzone   = minmax( yDiff / zonesize, 0, numside-1 );
    
//              Determine zone in SNr and create mapping back to CD

	    SNRimin = izone * SNRsize; SNRjmin = jzone * SNRsize;
	    SNRimax = SNRimin + SNRsize;    SNRjmax = SNRjmin + SNRsize;

	    temparr[izone][jzone]++;
                for ( k=SNRimin; k<SNRimax; k++ )
                    for ( l=SNRjmin; l<SNRjmax; l++ )

//                      Check probability of CD neuron nslConnectecting to SNR
//                      neuron.  The chance of a successful nslConnectection is
//                      established by the user in the NSL DATA object
//                      ConnectionChance.  By using nslConnectection probabilities,
//                      we ensure a Different set of CD neurons project to
//                      each of the SNR neurons in a specific zone.  Given
//                      sufficient Difference between the sets of connectections
//                      it should be possible to do remapping properly to
//                      a single neuron in a SNR zone.
		      if ((medbulk1.SNRMapCount_bulk.get(k,l) < MaxConnections ) && (nslRandom() < ConnectionChance)) {
//                          This CD neuron gets nslConnectected to this SNR neuron.
//                          Set next available mapping array element for all neurons
//                          in this region of SNr to values in xmap and ymap.
                            kindex = medbulk1.SNRMapCount_bulk.get(k,l)+1;
                            medbulk1.SNRxmap_bulk.set(k,l,kindex,xmap[i][j][ptr]);
                            medbulk1.SNRymap_bulk.set(k,l,kindex,ymap[i][j][ptr]);
                            medbulk1.SNRweights_bulk.set(k,l,kindex,0);
		      }
		ptr ++;
		  }
	}
    }
    return 0;
  }


public int OutputMapping(int[][][] xmap, int[][][] ymap) {

    //  This function accepts an x,y mapping from a cortical area to striatum and
    //  outputs the nslConnectections onto striatum as an array of 0s and 1s.
    int[][] arr = new int[StriatalArraySize][StriatalArraySize];
    int i, j, k;

    for ( i=0; i<CorticalArraySize; i++ )
      for ( j=0; j<CorticalArraySize; j++ )
	for ( k=0; k<MaxConnections; k++ ){
	    
	    if ( ( xmap[i][j][k] > 0 ) || ( ymap[i][j][k] > 0 ) )
	      {
		arr[xmap[i][j][k]][ymap[i][j][k]] = 1;
	      }
	  }
    
    return 0;
}
public int minmax( int int1, int sint, int lint )
{
    //  Ensures int1 is between sint and lint
    
    if ( int1 < sint ) return sint;
 
    if ( int1 > lint ) return lint;
    
    return int1;
}

public int MapToFovea( int iloc, int jloc ) {
	
//  This function establishes the weights between CD and SNr for "learned"
//  nslConnectections
    FEFsac_mirror = FEFsac_in;
    LIPmem_mirror = LIPmem_in;
    PFCgo_mirror = PFCgo_in;

  FEFsac_mirror[iloc][jloc] = 60;
  LIPmem_mirror[iloc][jloc] = 60;
  PFCgo_mirror[iloc][jloc]  = 60;

//  Set CD excitation through xmap, ymap arrays for each cortical
//  area


//System.err.println("===== Med[1] Calling SetCD");
  SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(), FEFsac_mirror );
//System.err.println("===== Med[2] Calling SetCD");
  SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPmem_mirror );
//System.err.println("===== Med[3] Calling SetCD");
  SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCgo_mirror );


//  Learning:  match SNr location teaching signal
//  with active neurons in CD and modify appropriate weight

  LearnWeights(medbulk1.SNRMapCount_bulk, medbulk1.SNRweights_bulk, medbulk1.SNRxmap_bulk, medbulk1.SNRymap_bulk, FOVEAX.get(), FOVEAY.get() );

//  Reset FEF, PFC, LIP, and CD to 0
    /* [Warning] Change input variable */
  /*
    FEFsac_in = 0;  
    LIPmem_in = 0; 
    PFCgo_in = 0; 
    */
    FEFsac_mirror = 0;
    LIPmem_mirror = 0;
    PFCgo_mirror = 0;

    CDdirmedburst_out = 0;

    return 0;
}

public int MapToOffset( int iloc1, int jloc1, int iloc2, int jloc2 ) {

//  This function establishes the weights between CD and SNr for "learned"
//  nslConnectections

int temp, ioff, joff;

    ioff = iloc2 - iloc1;
    joff = jloc2 - jloc1;

//  Don't do mapping if the offset distance between the two targets is
//  more than half the width of the array

// some problem in placing a negative sign on a nsl numeric object
// need refinement of nslj.src.math.Sub class
    if ( ( ioff > FOVEAX ) || ( ioff < -FOVEAX.get() ) ) return 0;
    if ( ( joff > FOVEAY ) || ( ioff < -FOVEAY.get() ) ) return 0;

    FEFsac_mirror = FEFsac_in;
    LIPmem_mirror = LIPmem_in;
    PFCgo_mirror = PFCgo_in;

    LIPmem_mirror[iloc2][jloc2] = 60;
    PFCgo_mirror[iloc1][jloc1]  = 60;

//  Set CD excitation through xmap, ymap arrays for each cortical
//  area.  FEF not used for targets not selected for current saccade.
//  Only use LIP and PFC go signal.

//System.err.println("===== Med[4] Calling SetCD");
    temp = SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPmem_mirror );
//System.err.println("===== Med[5] Calling SetCD");
    temp = SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCgo_mirror  );


//  Learning:  match SNr location teaching signal
//  with active neurons in CD and increment appropriate weights

    temp = LearnWeights(medbulk1.SNRMapCount_bulk,medbulk1.SNRweights_bulk,medbulk1.SNRxmap_bulk, medbulk1.SNRymap_bulk, ioff+FOVEAX.get(), joff+FOVEAY.get() );

//  Reset PFC, LIP, and CD to 0
    LIPmem_mirror = 0;
    PFCgo_mirror = 0;
    /*
    LIPmem_in = 0; 
    PFCgo_in = 0; 
    */
    CDdirmedburst_out = 0;

    return 0;
}

public int LearnWeights(NslInt2 SNRMapCount, NslDouble3 SNRweights, NslInt3 SNRxmap, NslInt3 SNRymap, int imap, int jmap ) {

//  This function sets the nslConnectecting weights between active CD neurons and
//  active SNR neurons.

int k, xmaploc, ymaploc;

    for ( k=0; k<SNRMapCount.get(imap,jmap); k++ ) {
//      Check all CD neurons projecting to this SNR neuron to see
//      if they are active.  If so, increase the weight between them.

        xmaploc = SNRxmap.get(imap,jmap,k);
        ymaploc = SNRymap.get(imap,jmap,k);
        if ( CDdirmedburst_out.get(xmaploc,ymaploc) != 0 ) {
	//        if ( CDdirmedburst_out[xmaploc][ymaploc] != 0 ) {
//          Found a linkage.  Increase weight for activity, but decrease
//          if remap is wrong.  Errors caused by shared nslConnectections in CD.

            SNRweights.set(imap,jmap,k,(SNRweights.get(imap,jmap,k)+LearnRate.get()));
	    SNRweights.set(imap,jmap,k,(SNRweights.get(imap,jmap,k)-Teacher.Check( imap, jmap )));
        }
    }

    return 0;
}


public void LearnConnections( Element elem ) {

    int     temp;
    int     ii;
    Element curelem;

    curelem = elem;

    while ( curelem != null ) {
 
        for ( ii=0; ii<NumberIterations; ii++ ) {      //# of iterations
//          Set cortical excitation

            SNRmedburst_out    = 0;
            CDdirmedburst_out  = 0;
            FEFsac_mirror         = 0;
            LIPmem_mirror         = 0;
            PFCgo_mirror          = 0;

//          Determine correct remappings for non-neural Teacher
	    /* [warning] what is Teacher? */
            curelem.Remap( (int)CorticalArraySize, Teacher );

//          Increment weights between active CD neurons and SNR fovea

            temp = MapToFovea( curelem.X(), curelem.Y() );

//          Time to map the nonsaccade target as well
	    /*
            LIPmem_in = 0; 
	    FEFsac_in = 0; 
	    PFCgo_in = 0; 
*/
            FEFsac_mirror         = 0;
            LIPmem_mirror         = 0;
            PFCgo_mirror          = 0;


	    CDdirmedburst_out = 0;

//          increment weights between active CD neurons and remapped location

            temp = MapToOffset( curelem.X(),  curelem.Y(), 
                                curelem.XO(), curelem.YO() );
        }

        curelem = curelem.Next();
    }

    return;
}

public void TestConnections( ) {

    Element curelem;
    int ia, ja, temp;

    curelem = LearnedElements;

    while ( curelem != null && curelem.X()!=-1 && curelem.Y()!=-1){
        LIPmem_mirror = 0; 
	FEFsac_mirror = 0; 
	PFCgo_mirror  = 0; 
	CDdirmedburst_out = 0; 
	SNRmedburst_out = 0;

        FEFsac_mirror[curelem.X()][curelem.Y()] = 60;
        LIPmem_mirror[curelem.X()][curelem.Y()] = 60;
        PFCgo_mirror[curelem.X()][curelem.Y()] = 60;
        LIPmem_mirror[curelem.XO()][curelem.YO()] = 60;

//      Propagate cortical activity to CD

System.err.println("===== Med[6] Calling SetCD");
        temp = SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(), FEFsac_mirror );
System.err.println("===== Med[7] Calling SetCD");
        temp = SetCD(  CDdirmedburst_out, medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPmem_mirror );
System.err.println("===== Med[8] Calling SetCD");
        temp = SetCD(  CDdirmedburst_out, medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCgo_mirror );

//      Sum CD activity onto SNr through SNRxmap and SNRymap

        temp = SumCDtoSNR( CDdirmedburst_out, SNRmedburst_out );

        curelem = curelem.Next();
    }

    return;
}

public void DisplayCorticalNeuron( int[][] xarr, int[][] yarr, int max, int[][][] xmap, int[][][] ymap ) {

//  Check the input cortical region to find if it projects to the
//  neuron indicated by the xloc,yloc location

int i, j, k,  xmaploc, ymaploc;

    for ( i=0; i<CorticalArraySize; i++ )
        for (j=0; j<CorticalArraySize; j++ )
            for ( k=0; k<MaxConnections; k++ ) {
                
		xmaploc = xmap[i][j][k]; 
		ymaploc = ymap[i][j][k];
	    }
    return;
}

public void TestFoveaMapping( ) {

//  Tests the mapping of a single target onto the fovea to see if extra
//  mappings have also been established in the learning process

    Element curelem;
    int ia, ja, temp;
    int jj=0;

        curelem = LearnedElements;

    while ( curelem != null && curelem.X()!=-1 && curelem.Y()!=-1){
        LIPmem_mirror = 0; 
	FEFsac_mirror = 0; 
	PFCgo_mirror = 0; 
	CDdirmedburst_out = 0; 
	SNRmedburst_out = 0;

  FEFsac_mirror[curelem.X()][curelem.Y()] = 60;
  LIPmem_mirror[curelem.X()][curelem.Y()] = 60;
  PFCgo_mirror[curelem.X()][curelem.Y()] = 60;

//      Propagate cortical activity to CD

System.err.println("===== Med[9] Calling SetCD");
       SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(), FEFsac_mirror );
       // see lib.h for this if ( true ) break;
System.err.println("===== Med[10] Calling SetCD");
  SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPmem_mirror );
System.err.println("===== Med[11] Calling SetCD");
  SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCgo_mirror );


//      Sum CD activity onto SNr through SNRxmap and SNRymap

        temp = SumCDtoSNR( CDdirmedburst_out, SNRmedburst_out );

//      Output SNR values for testing purposes
	/*
        cout<<endl<<"Fovea mapping test.\nSNR values for test #"<<jj;

        for (ia=0; ia<CorticalArraySize; ia++)
            for (ja=0; ja<CorticalArraySize; ja++ )
                if ( SNRmedburst.elem(ia,ja) > 1.0 ) {
                    printf(" %d,%d:%4.0f", ia, ja, SNRmedburst.elem(ia,ja) );
                    cout<<endl<<"FEF";

                    DisplayCorticalNeuron( *SNRxmap[ia][ja], 
                                           *SNRymap[ia][ja],
                                           SNRMapCount[ia][ja],
                                           **FEFxmap, **FEFymap );
                    cout<<endl<<"LIP";
                    DisplayCorticalNeuron( ia, ja, **LIPxmap, **LIPymap );
                    cout<<endl<<"PFC";
                    DisplayCorticalNeuron( ia, ja, **PFCxmap, **PFCymap );

		}
		*/
        curelem = curelem.Next();
        
    }

    return;
}


public   int SumCDtoSNR (NslDouble2 CD, NslDouble2 SNR){
    return snrmedburst.SumCDtoSNR(CD, SNR);
  }
  /**** Menu items ****/
  public void startNewElementList() {
    //System.out.println("LearnedElements:"+LearnedElements);
     //System.out.println("UnlearnedElements:"+UnlearnedElements);
            LearnedElements.Remove();
            UnlearnedElements.Remove();
  }

public   void addNewRandomElementsToLearn() {
    int tmp = nslj.src.system.Console.readInt("Number of elements to add > ");
    addNewRandomElementsToLearn(tmp);
  }
public   void addNewRandomElementsToLearn(int num) {
   UnlearnedElements.AddRandomElements((int)(CorticalArraySize-1), num );
  }

public   void addNewSpecifiedElementsToLearn() {
    int tmp = nslj.src.system.Console.readInt("Number of elements to add > ");
    UnlearnedElements.AddSpecifiedElements((int)(CorticalArraySize-1), tmp );
  }
public void addNewSpecifiedElementsToLearn(int x, int y, int xo, int yo) {
    UnlearnedElements.AddSpecifiedElements((int)(CorticalArraySize-1), x, y, xo, yo );
  }

 public  void learnNewElements() {
    LearnConnections(UnlearnedElements );
    //System.out.println("nslConnect. done");
    LearnedElements.Merge( UnlearnedElements );
    //System.out.println("merge.done");
    UnlearnedElements.Remove();
  }

  public void testAllLearnedElements() {
    TestConnections( );
  }  

  public void displayLearnedAndUnlearnedElements() {
    nslprintln("Learned elements");
    nslprintln(LearnedElements);
    nslprintln("Unlearned elements");
    nslprintln(UnlearnedElements);

  }

  public void testFoveaMappingOnly() {
    TestFoveaMapping();
  }



} //end class




    

