/* SCCS  @(#)SC.mod	1.1---09/24/99--18:57:26 */
/* old kversion @(#)SC.mod	1.8 --- 08/05/99 -- 13:56:34 : jversion  @(#)SC.mod	1.2---04/23/99--18:39:41 */

nslImport nslAllImports;

//-----------------------------SC module---------------------------------------
// update: 11/28/1996 by Erhan

/**
* Here is the class representing the Superior Colliculus (SC) module. The
* module contains three layers SCsac, SCqv, SCbu. This is converted
* as a parent module and 4 children module (the extra module is for
* non-neural stuff).
*
* The Execute Saccade schema is implemented in the SC module and
* the "brainstem saccade generator".
* Once PFC has issued a GO signal, the combination of increased activity from
* PFC and decreased inhibitory activity from BG allow activation to grow in
* the SC. This activation is projected to the brainstem where motor neurons
* are excited and cause the eye muscles to move the eyes to the new target
* location.
*/

//-------------- SC (parent) module ------------------------------------------

/**
* The SC module as a whole gets inputs FEF, LIP and PFC, SNR.
*/

nslModule SC (int array_size) {

  // 9/12/20 aa SCBUtemp SCBUtemp1;
  // inputs
    public NslDinDouble2 SNRlatburst_in(array_size,array_size);
    public NslDinDouble2 FEFsac_in(array_size,array_size)      ;
    public NslDinDouble2 LIPmem_in(array_size,array_size)      ;
    public NslDinDouble2 PFCfovea_in(array_size,array_size)    ;
    public NslDinDouble0 BSGsaccade_in()  ;
    public NslDinDouble1 BSGEyeMovement_in(array_size);
      // outputs
    public NslDoutDouble2 SCsac_out(array_size,array_size) ;
    public NslDoutDouble2 SCbu_out(array_size,array_size) ;

//privates    
    private SCsac SCsac1 (array_size)  ;
    private SCqv SCqv1 (array_size)   ;
    private SCbu SCbu1 (array_size)   ;
    
    private NslInt0 FOVEAX();
    private NslInt0 FOVEAY();

public void initModule() {
    FOVEAX = (NslInt0)nslGetValue("crowleyTop.FOVEAX");
    FOVEAY = (NslInt0)nslGetValue("crowleyTop.FOVEAY");  
}

public void makeConn(){
    //parent inputs to leaf inputs
    nslRelabel(SNRlatburst_in,SCsac1.SNRlatburst_in);
    nslRelabel(FEFsac_in,SCsac1.FEFsac_in);
    nslRelabel(LIPmem_in,SCqv1.LIPmem_in);

    nslRelabel(PFCfovea_in,SCbu1.PFCfovea_in);
    nslRelabel(BSGsaccade_in,SCbu1.BSGsaccade_in);
    nslRelabel(BSGEyeMovement_in,SCbu1.BSGEyeMovement_in);
    
    //leaf internslConnectections
    nslConnect(SCqv1.SCqv_out,SCsac1.SCqv_in);
    nslConnect(SCbu1.SCbu_out,SCsac1.SCbu_in);
    nslConnect(SCsac1.SCsac_out,SCbu1.SCsac_in);
    
    //leaf outputs to parent outputs
    nslConnect(SCsac1.SCsac_out,SCsac_out);
    nslConnect(SCbu1.SCbu_out,SCbu_out);
    
}

// static utility functions
public static NslDouble2 MoveEye (Target Tlist, NslDouble2 inmat) {
    /*
      This function performs a "floating-point" remapping of the input matrix
      inmat using the x,y offsets specified in the input Target list Tlist.
      Movement of less than one array element is handled by spreading the
      activity over up to 4 array elements.  Targets have a size of one
      array element.
      */
    
    int     maxi, maxj;
    int     intTx, intTy;
    
    Target cur, prev, next;
    
    double Tx, Ty;
    
    prev = Tlist;
    cur  = Tlist;
    if ( cur == null ) return inmat;       //No targets in list
    next = Tlist.Next();
    
    maxi      = (int)inmat.getSize1();
    maxj      = (int)inmat.getSize2();
    
//99/8/3 aa:    NslDouble2 tempmat2d = new NslDouble2("tempmat2d",this,maxi, maxj);
    NslDouble2 tempmat2d (maxi, maxj);
    double[][] tempmat = tempmat2d.get();
    
    //  Main loop to remap the current values in inmat based on the new target
    //  location.
    
    while ( cur != null)
      {
        Tx        = cur.X();
        Ty        = cur.Y();
        intTx     = (int)Tx;
        intTy     = (int)Ty;
	
        if ( ( ( intTx < 0 ) || ( intTx >= maxi ) ) ||
             ( ( intTy < 0 ) || ( intTy >= maxj ) ) )
          {
            if(Tlist!=null)Tlist.RemoveTarget( cur );
	    // if (Tlist == NULL ) cout <<endl<<"Tlist is NULL";
          }
        else
          {
	    
            tempmat[intTx][intTy] = 
	      	( ( 1 + intTx - Tx ) * ( 1 + intTy - Ty ) ) 
		+ tempmat[intTx][intTy];

		if ( intTx+1 < maxi ) 
		  tempmat[intTx+1][intTy] = 
		    ( ( Tx - intTx ) * ( 1 + intTy - Ty ) )
                                         + tempmat[intTx+1][intTy];
  
            if ( intTy+1 < maxj ) 
                tempmat[intTx][intTy+1] = 
                    ( ( 1 + intTx - Tx ) * ( Ty - intTy ) )
                                         + tempmat[intTx][intTy+1];
 
            if ( ( intTx+1 < maxi ) && ( intTy+1 < maxj ) )
                tempmat[intTx+1][intTy+1] = 
                    ( ( Tx - intTx ) * ( Ty - intTy ) )
                                         + tempmat[intTx+1][intTy+1];
 
//      Need to make sure that any overlap between Targets does not produce
//      an activation greater than 1, since an array element is "full"
//      when completely covered by any number of targets.
 

            if ( tempmat[intTx][intTy] > 1)
	      tempmat[intTx][intTy]     = 1;
            if ( tempmat[intTx][intTy] > 1)
	      tempmat[intTx+1][intTy] = 1;
            if ( tempmat[intTx][intTy] > 1)
	      tempmat[intTx][intTy+1] = 1;
            if ( tempmat[intTx][intTy] > 1)
	      tempmat[intTx+1][intTy+1] = 1;

/*
            if ( tempmat[intTx][intTy] > 1)
	      tempmat[intTx][intTy]     = 1;
            if ( tempmat[intTx+1][intTy] > 1)
	      tempmat[intTx+1][intTy] = 1;
            if ( tempmat[intTx][intTy+1] > 1)
	      tempmat[intTx][intTy+1] = 1;
            if ( tempmat[intTx+1][intTy+1] > 1)
	      tempmat[intTx+1][intTy+1] = 1;
*/
          }
        cur = cur.Next();
    }
    return tempmat2d;
}
 
public static Target MakeTargets(NslDouble2 inmat2d) {
    double[][] inmat = inmat2d.get();
    int size1 = inmat.length;
    int size2 = inmat[0].length;
    //nslprintln("1)WHY?!!!");
    Target FirstTarget = null;
    //nslprintln("2)WHY?!!!");
    for(int i=0; i<size1; i++)
      for(int j=0; j<size2; j++) 
	if(inmat[i][j]>0) {
	  if(FirstTarget == null)
	    Target (i,j) FirstTarget  ;
	  else
	    Target (i,j,FirstTarget) FirstTarget  ;

	}
    return FirstTarget;
  }


public static NslDouble1 GetSaccadeVector(NslDouble2 inmat2d) {
/*
This function determines the amplitude and direction (vector)
of a saccade by determining the centroid of the activity in the input matrix.
This is accomplished by:
    (1)Calculating the SUM of all activity in inmat.
    (2)Dividing all matrix elements by the sum in step 1 and multiplying by
       the array element indices.  This creates the saccade vector as the
       normalized sum of all activity in the input matrix.
It returns the x,y components of the calculated saccade vector.
*/
 
    double[][] inmat = inmat2d.get();
    int size1 = inmat.length;
    int size2 = inmat[0].length;
    int half1 = size1/2;
    int half2 = size2/2;
// 99/8/4 aa: was : double SCsum=nslSum(inmat2d);

    double SCsum;
    SCsum = nslSum(inmat2d);
//99/8/3 aa:    NslDouble1 temp1d = new NslDouble1(2);
    NslDouble1 temp1d(2);
    double[] temp = temp1d.get();
    
    if(SCsum==0) {
      temp[0]=temp[1]=0;
    }
    else {
      for(int i=0; i<size1; i++) 
	for(int j=0; j<size2; j++) 
	  if(inmat[i][j]>0){
	    temp[0]=inmat[i][j]*(i-half1)/SCsum;
	    temp[1]=inmat[i][j]*(j-half2)/SCsum;
	  }
    }
    return temp1d;
  }

} /* END OF CLASS SC_module -PARENT CLASS FOR SC */





