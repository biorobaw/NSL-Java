/* SCCS %W%---%G%--%U% */

/* Copyright 1999 University of Southern California Brain Lab */
/* Author Jacob Spoelstra */
/* email nsl@java.usc.edu */

nslImport nslAllImports;

nslModule IO_layer() {
    // constants
    nslConst double f_max  = 10.;
    nslConst double offset = 1.3865;  // gives output of 2 for 0 input
    nslConst double slope  = 1.;
    // inports
    NslDinDouble1 sens_in(2);  // SENS input
    NslDinDouble1 nuc_in(2);   // NUC input
    // outports
    NslDoutDouble1 io_out(2); 
    // variables
    NslDouble1 io_mp(2);
  
 public void initModule() {
     io_out = 2.;
 }
  
 public void simTrain() {
     simRun();
 }
  
 public void simRun(){
     double nuc_act;

     nuc_act = nslSumRows(nuc_in);
     io_mp   = sens_in - .01*nuc_act;
     io_out  = f_max * nslSigmoid(io_mp,slope,offset);
 }
}
