h41447
s 00086/00000/00000
d D 1.1 99/09/24 16:05:43 aalx 1 0
c date and time created 99/09/24 16:05:43 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS %W% --- %G% -- %U% */
/* Copyright 1999 University of Southern California Brain Lab */
/* Author: Mihai Bota */
/* email nsl@java.usc.edu */

nslImport nslAllImports;

nslModel HillclimbTop() {
        private int weightSize=4;
        private int arraySize=40;

	private HillclimbCalc calc(weightSize,arraySize);
	private HillOut out(weightSize,arraySize);

	private double ttime;
	private double rtime;

	public void initSys (){
		//system.setEndTime(800.0);
		system.setTrainEndTime(500.0);
		system.nslSetTrainDelta(1.0);
		system.setRunEndTime(300.0);
		system.nslSetRunDelta(1.0);
	}
	public void makeConn() {
		nslConnect(calc.x,out.x);
		nslConnect(calc.y,out.y);
		nslConnect(calc.distance,out.distance);
		nslConnect(calc.Weight,out.Weight);
		nslConnect(calc.areaTrain,out.areaTrain);
		nslConnect(calc.areaRun,out.areaRun);
	}
	public void initModule() {
		nslSetRunDelta(1.0);
		nslSetTrainDelta(1.0);
	}

	public void simTrain() {
		if (system.debug>1 ) {
			ttime=system.getCurrentTime();
			nslPrintln("Top:simTrain:"+ttime);
		}
	}
	public void simRun() {	
		if (system.debug>1 ) {
			rtime=system.getCurrentTime();
			nslPrintln("Top:simRun:"+rtime);
		}

	}
} //end class



































E 1
