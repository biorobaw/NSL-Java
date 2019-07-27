/* SCCS  @(#)IJpair.mod	1.1---09/24/99--18:57:19 */
/* old kversion @(#)IJpair.mod	1.8 --- 08/05/99 -- 13:56:21 : jversion  @(#)IJpair.mod	1.2---04/23/99--18:39:30 */
// Java version of "other" classes used in module PFC

// Import statements
nslImport nslAllImports;

nslClass IJpair() {
  // private variables
  private int i;
  private int j;

  // Note: this class has a constructor
//  public IJpair()
// {
//    i = 0;
///    j = 0;
//  }


  // Note: this class has a constructor
  public void callFromConstructorBottom(){
    i = 0;
    j = 0;
  }

  public void init() {
    i = 0;
    j = 0;
  }


  public int MaxIJ( NslDouble2 inmat ) {
    // Returns the i,j location of the maximum value element in the 
    // matrix passed as input

    // 99/8/2 aa: this would be better as nslMaxElem
    // pass out an array of values

    int    ic, jc;
    int    imax, jmax;
    int    foundmax;
    double max;

    foundmax = 0;
    max = -0.5;

    imax = inmat.getSize1();
    jmax = inmat.getSize2();

    for ( ic=0; ic<imax; ic++ ){
      for ( jc=0; jc<jmax; jc++ ){
	if ( inmat[ic][jc] > max ) {
	  max = inmat[ic][jc];
	  foundmax = 1;
	  i = ic; 
	  j = jc;
	}
      }
    }
    if ( foundmax != 0)
      return 0;     //No positive maximum value

    return 1;
  }
  
  public int getI(){
    return i;
  }
  public int getJ(){
    return j;
  }
} //end class



