h50835
s 00023/00000/00000
d D 1.1 99/09/24 18:57:21 aalx 1 0
c date and time created 99/09/24 18:57:21 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
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


E 1
