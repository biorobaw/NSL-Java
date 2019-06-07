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

 public class GC_layer extends NslModule/*()*/ {
    // constants
    static final  double f_max  = 100.;

    // inports
    NslDinDouble2 pp_in ; /*()*/   // PP input
    NslDinDouble2 fcx_in ; /*()*/ // FCX input

    // outports
    NslDoutDouble2 gc_out ; /*(30,30)*/ 

    // variables
    NslDouble0 w ; /*()*/
    NslDouble2 gc_mp ; /*(30,30)*/
    NslDouble0 gc_offset ; /*()*/
    NslDouble0 gc_slope ; /*()*/
    NslDouble0 gc_dist ; /*()*/
    NslInt0    gc_nd ; /*()*/
    int[] src = new  int [3600];
    int[] Xo = new  int [3600];
    int[] Yo = new  int [3600];
    int[] Xd = new  int [3600];
    int[] Yd = new  int [3600];
     int NC;

 public  void initModule(){
     gc_offset.set((NslDouble0)nslGetValue("dartModel.gc_offset")) /*rule 114 */;
     gc_slope.set((NslDouble0)nslGetValue("dartModel.gc_slope")) /*rule 114 */;
     gc_dist.set((NslDouble0)nslGetValue("dartModel.gc_dist")) /*rule 114 */;
     gc_nd.set((NslInt0)nslGetValue("dartModel.gc_nd")) /*rule 114 */;

     w.set((1.)/((double)gc_nd.get()));

      int gx,gy,i,x,y;
     
     gc_out.set(50.);
     gc_mp.set(0.);
     // Create mapping function
     NC = 0;
     for(gx=0;gx<30;gx++){
	 for(gy=0;gy<30;gy++){
	     for(i=0;i<gc_nd.get();i++){
		 Xd[NC] = gx;
		 Yd[NC] = gy;
		 if((NslRandom.eval())<(gc_dist.get())){ // PP input
		     src[NC] = 0;
		     (Xo)[NC]=(int)NslRandom.eval(3,8)/* rule 112 */;
		     (Yo)[NC]=(int)NslRandom.eval(0,10)/* rule 112 */;
		 } else { // FCX input
		     src[NC] = 1;
		     (Xo)[NC]=(int)NslRandom.eval(0,10)/* rule 112 */;
		     (Yo)[NC]=(int)NslRandom.eval(1,3)/* rule 112 */;
		 }
		 NC++;
	     }
	 }
     }
 }
  
 public  void simTrain() {
     simRun();
 }
 
 public  void simRun(){
      int i,j;
      int mx,my,ix,iy;

     // Map inputs onto 30x30 array using mapping function
          
     gc_mp.set(0.);
     for(i=0;i<NC;i++){
      mx = Xd[i];
      my = Yd[i];
      ix = Xo[i];
      iy = Yo[i];
      if(src[i]==0)
	  (gc_mp).set(mx,my,((gc_mp).get(mx,my))+((pp_in).get(ix,iy)));
      else
	  (gc_mp).set(mx,my,((gc_mp).get(mx,my))+((fcx_in).get(ix,iy)));
     }
     
     gc_mp.set(
 __nsltmp101=nslj.src.math.NslElemMult.eval(__nsltmp101,w.get(),gc_mp.get()));

     gc_out.set(
 __nsltmp102=nslj.src.math.NslElemMult.eval(__nsltmp102,f_max,NslSigmoid.eval(gc_mp,gc_slope,gc_offset)));        
 }
	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	__nsltmp101 = new double[1][1];
	__nsltmp102 = new double[1][1];
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
	for (int i = 0; i < __nsltmp102.length; i++) {
		for (int j = 0; j < __nsltmp102[0].length; j++) {
			__nsltmp102[i][j] = 0;
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
	private  double[][] __nsltmp102;
	/* constructor and related methods */
	/* nsl declarations */

	 /*GENERIC CONSTRUCTOR:   */
	 public GC_layer(String nslName, NslModule nslParent) {
		super(nslName, nslParent);
		initSys();
		makeInst(nslName, nslParent);
	}
	public void makeInst(String nslName, NslModule nslParent){ 
	 pp_in=new NslDinDouble2 ("pp_in",this); //NSLDECLS 
	 fcx_in=new NslDinDouble2 ("fcx_in",this); //NSLDECLS 
	 gc_out=new NslDoutDouble2 ("gc_out",this,30,30); //NSLDECLS 
	 w=new NslDouble0 ("w",this); //NSLDECLS 
	 gc_mp=new NslDouble2 ("gc_mp",this,30,30); //NSLDECLS 
	 gc_offset=new NslDouble0 ("gc_offset",this); //NSLDECLS 
	 gc_slope=new NslDouble0 ("gc_slope",this); //NSLDECLS 
	 gc_dist=new NslDouble0 ("gc_dist",this); //NSLDECLS 
	 gc_nd=new NslInt0 ("gc_nd",this); //NSLDECLS 
	}
	/* end of automatic declaration statements */
}