/* SCCS  @(#)SCqv.mod	1.1---09/24/99--18:57:27 */
/* old kversion @(#)SCqv.mod	1.8 --- 08/05/99 -- 13:56:35 : jversion  @(#)SCqv.mod	1.2---04/23/99--18:39:47 */

// --------------------------------- SCqv layer -----------------------------

nslImport nslAllImports;

//LNK_SC3
/**
* Here is the class representing the target locations for SRBNs (saccade
* related burst neurons). This layer is called in the model quasi visual
* layer (SCqv) layer. The SCqv cells receive their input from LIP.
*/

nslModule SCqv (int array_size) {

//input ports
  NslDinDouble2 LIPmem_in(array_size,array_size)  ;
//output ports
  NslDoutDouble2 SCqv_out (array_size,array_size)        ;

//privates

private  NslDouble2 scqv (array_size,array_size)  ;
private double SCqvsigma1;
private double SCqvsigma2;
private double SCqvsigma3;
private double SCqvsigma4;
private double scqvtm;

public void initRun(){
  scqv = 0;
  SCqv_out = 0;

  SCqvsigma1 =   0;
  SCqvsigma2 =  90;
  SCqvsigma3 =   0;
  SCqvsigma4 =  90;
  scqvtm = 0.01;
}
public void simRun(){
  // System.err.println("@@@@ SCqv simRun entered @@@@");
  scqv=nslDiff(scqv,scqvtm, -scqv + LIPmem_in);
  SCqv_out=Nsl2Sigmoid.eval(scqv,SCqvsigma1,SCqvsigma2,SCqvsigma3,SCqvsigma4);
}

} //end class






