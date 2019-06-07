/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS %W%---%G%--%U% */

/* Copyright 1999 University of Southern California Brain Lab */
/* Author Jacob Spoelstra */
/* email nsl@java.usc.edu */

 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

 public class PC_layer extends NslModule/*()*/ {
    // constants
    static final  double f_max  = 100.;
    static final  double offset = 750.; // output +- 5 for 0 input
    static final  double slope  = .005;
    // inports
    NslDinDouble2 gc_in ; /*()*/   // GC input
    NslDinDouble1 io_in ; /*()*/   // IO input
    // outports
    NslDoutDouble2 pc_out ; /*(2,5)*/ 
    // variables
    NslDouble1 w ; /*(3000)*/
    NslDouble2 pc_mp ; /*(2,5)*/
    NslDouble0 alpha ; /*()*/

 public  void initModule(){

     
     // Initialize weights
     w.set(NslAdd.eval((20.)/(((30.)*(30.))/(3.)),NslElemDiv.eval(NslRandom.eval(w,-(.5),.5),2))) /* rule 108 */;
     pc_out.set(5.);
     pc_mp.set(0.);
 }
 
 public  void simTrain() {
     simRun();
 }
 
 public  void endTrain() {
     endRun();
 }

 public  void simRun(){
     processGCInputs(false);   
     // Update PC cells
     pc_out.set(
 __nsltmp101=nslj.src.math.NslElemMult.eval(__nsltmp101,f_max,NslSigmoid.eval(pc_mp,slope,offset)));
 }

 public  void endRun() {
     // Learning
     processGCInputs(true);
     w.set(NslBound.eval(w,0,1,0,1)) /* rule 108 */;
 }

 private  void processGCInputs(boolean endEpoch) {
      int px,py,gx,gy,y,wc;
      int beam_start;
      int i,j;
     // GC inputs
     pc_mp.set(0.);
     wc = 0;
     for(px=0;px<2;px++){
	 for(py=0;py<5;py++){
	     beam_start = py*30/5;
	     for(gx=0;gx<30;gx++){
		 for(y=0;y<10;y++){
		     gy = (beam_start + y)%30;
	             if (!endEpoch) {
		         (pc_mp).set(px,py,((pc_mp).get(px,py))+(((w).get(wc))*((gc_in).get(gx,gy))));
		     } else {
			(w).set(wc,((w).get(wc))+((
 nslj.src.math.NslElemMult.eval(alpha.get(),((gc_in).get(gx,gy))*(.01)))*(((io_in).get(px))-(2.))));
		     }
		     wc++;
		 }
	     }
	 }
     }
 }
 
	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	/* end of automatic instantiation statements */
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* nslInitTempRun inserted by NPP */
public void nslInitTempRun() {
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	for (int i = 0; i < __nsltmp101.length; i++) {
		for (int j = 0; j < __nsltmp101[0].length; j++) {
			__nsltmp101[i][j] = 0;
		}
	}
	/* end of automatic intialisation statements */
}

	/* nslInitTempTrain inserted by NPP */
public void nslInitTempTrain() {
	/* Initialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* Declaration statements generated by NslPreProcessor */
	/* makeinst() declared variables */
	/* temporary variables */
	private  double[][] __nsltmp101;
	/* constructor and related methods */
	/* nsl declarations */

	 /*GENERIC CONSTRUCTOR:   */
	 public PC_layer(String nslName, NslModule nslParent) {
		super(nslName, nslParent);
		initSys();
		makeInst(nslName, nslParent);
	}
	public void makeInst(String nslName, NslModule nslParent){ 
	 gc_in=new NslDinDouble2 ("gc_in",this); //NSLDECLS 
	 io_in=new NslDinDouble1 ("io_in",this); //NSLDECLS 
	 pc_out=new NslDoutDouble2 ("pc_out",this,2,5); //NSLDECLS 
	 w=new NslDouble1 ("w",this,3000); //NSLDECLS 
	 pc_mp=new NslDouble2 ("pc_mp",this,2,5); //NSLDECLS 
	 alpha=new NslDouble0 ("alpha",this); //NSLDECLS 
	}
	/* end of automatic declaration statements */
}