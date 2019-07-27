/* SCCS @(#)HillclimbTop.mod	1.1 --- 09/24/99 -- 16:05:43 */
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



































