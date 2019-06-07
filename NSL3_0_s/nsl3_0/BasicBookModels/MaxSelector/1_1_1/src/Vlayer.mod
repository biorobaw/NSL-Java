

nslModule Vlayer() extends NslModule() {

//NSL Version: 3_0_n
//Sif Version: 6
//libNickName: booklib
//moduleName:  Vlayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 u_in(); // 
public NslDoutDouble0 vf(); // 

private NslDouble0 vp(); // 
private NslDouble0 h2(); // 

//methods 
public void initModule() {
	vf=0;
}

public void initRun() {
	vf=0;
	vp=0;
	h2=0.5;
}

public void simRun(){
	vp = nslDiff(vp, -vp + nslSum(u_in) - h2);
	vf = nslRamp(vp);
}

}//end Vlayer

