/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)Func.mod	1.8 --- 08/05/99 -- 13:56:19 : jversion  @(#)Func.mod	1.2---04/23/99--18:39:28 */

// 99/8/2 this could also be made into a library

nslImport nslAllImports;

//
// Global variables used in this module
//
// NslInt2 FEFxmap [];
// NslInt2 FEFymap [];
// NslInt2 LIPxmap [];
// NslInt2 LIPymap [];
// NslInt2 PFCxmap [];
// NslInt2 PFCymap [];
// NslInt2 SNRxmap []; 
// NslInt2 SNRymap []; 
// NslDouble2 SNRweights [];
// NslInt2 SNRMapCount;
//


//
// This class contains the SetCD function from lib.h file
//
/**
 * Func class 
 * Contains the SetCD function from lib.h file
 * This function is also called by other functions in the lib.h file:
 * MapToFovea, MapToOffset, TestConnections, TestFoveaMapping
 * @see     Func
 */
/*----------*/
nslModule Func(int CorticalArraySize) {

	static private int MaxConnections = 50;


  // internal variables


  // This function is also called by other functions in the lib.h file:
  // MapToFovea, MapToOffset, TestConnections, TestFoveaMapping

public int SetCD (NslDouble2 CD, int[][][] xmap, int[][][] ymap, NslDouble2 mat) {


  //  This function maps the cortical excitation onto the CD based on the
  //  nslConnectections established in the xmap and ymap arrays

    int i, j, k, loc, xmaploc, ymaploc;

verbatim_NSLJ; 
	if (CD==null) { System.err.println("********** CD NULL!!!!");
			System.exit(0);
	}

	if (xmap==null) {System.err.println("********** xmap NULL!!!!");
			  System.exit(0);
	}
	if (ymap==null) {System.err.println("********** ymap NULL!!!!");
			  System.exit(0);
	}
	if (mat==null) {System.err.println("********** mat NULL!!!!");
			  System.exit(0);
	}

verbatim_off; 

    for (i = 0; i < CorticalArraySize; i ++)
      for (j = 0; j < CorticalArraySize; j ++) {
        if (mat [i][j] != 0) { // Found an active cortical neuron
          for (k = 0; k < MaxConnections; k ++) {
//            loc = ( MaxConnections * j ) + k + 
//                  ( MaxConnections * CorticalArraySize * i );
            xmaploc = xmap[i][j][k]; ymaploc = ymap[i][j][k];
	    //            xmaploc = *(xmap+loc); ymaploc = *(ymap+loc);
            if ((xmaploc != 0) || (ymaploc != 0)) {
              CD [xmaploc][ymaploc] = mat [i][j];
	    }

          }
        }
      }
    return 0;
}


/* ERH 6/15/98 added the constructor here */
// 99/8/2 aa soon we willl not need this constructor
verbatim_NSLJ;
        public Func(String nslName, NslModule nslParent) {
                super();
        }
verbatim_off;

} //end class









