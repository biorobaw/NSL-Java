h44695
s 00070/00000/00000
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
// HillOut
// This is the module that displays all of the output graphs in a NslFrame

nslImport nslAllImports;

nslModule HillOut(int weightSize,int areaSize) extends NslOutModule () {
//nsl input ports

public 	NslDinDouble0 x();
public 	NslDinDouble0 y();
public 	NslDinDouble0 distance();
public 	NslDinDouble2 Weight(weightSize,weightSize);
public 	NslDinDouble2 areaTrain(areaSize,areaSize);
public 	NslDinDouble2 areaRun(areaSize,areaSize);

//variables
/*
public 	NslDouble0 x();
public 	NslDouble0 y();
public 	NslDouble0 distance();
public 	NslDouble2 Weight(weightSize,weightSize);
public 	NslDouble2 areaTrain(areaSize,areaSize);
public 	NslDouble2 RunRun(areaSize,areaSize);
*/

private	boolean goodstatus=false; //false=fail; true=success


public void initModule() {
      if (!(system.getNoDisplay())) {		
	nslAddTemporalCanvas(x,-5,5);
	nslAddTemporalCanvas(y,-5,5);
	nslAddTemporalCanvas(distance,-8,8);
	nslAddAreaCanvas(Weight,0,1);
	nslAddAreaCanvas(areaTrain,0,1);
	nslAddAreaCanvas(areaRun,0,1);
      }  //if not noDisplay
}

public void initRun(){
	if (system.debug>1) {
		nslPrintln("HillOut:initRun");
	}
}
public void simRun(){
}

} //end class





















E 1
