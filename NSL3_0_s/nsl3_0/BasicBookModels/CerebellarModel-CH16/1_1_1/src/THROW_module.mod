/* SCCS %W%---%G%--%U% */

/* Copyright 1999 University of Southern California Brain Lab */
/* Author Jacob Spoelstra */
/* email nsl@java.usc.edu */

nslImport nslAllImports;

nslModule THROW_module() {
    // inports
    NslDinDouble1 nuc_in();
    // outports
    NslDoutDouble0 throw_out();
    // variables
 
 public void simTrain() {
     simRun();
 }
 
 public void simRun(){
     throw_out = (.5 - (1.+nuc_in[0])/(2.+nuc_in[1] + nuc_in[0]))*100.;
 }
}
