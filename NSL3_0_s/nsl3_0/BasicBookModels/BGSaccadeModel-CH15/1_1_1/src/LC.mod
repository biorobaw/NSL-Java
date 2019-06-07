/* SCCS  @(#)LC.mod	1.1---09/24/99--18:57:21 */
/* old kversion @(#)LC.mod	1.8 --- 08/05/99 -- 13:56:24 : jversion  @(#)LC.mod	1.2---04/23/99--18:39:32 */
nslImport nslAllImports;

// LNK_LC1
/* 
* Here is the class representing the LimbicCortex (LC) input module.
* In old NSL it is an input array, but since NSLJ does not have
* input arrays yet we make them static arrays.
*/

nslModule LC(int array_size){

  //output ports
  public NslDoutDouble2 LimbicCortex_out(array_size,array_size)  ;

  public void initRun(){
    LimbicCortex_out=30;
  }

} //end class


