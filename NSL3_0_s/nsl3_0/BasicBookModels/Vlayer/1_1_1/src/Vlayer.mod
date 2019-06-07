

nslModule Vlayer(int size){

//NSL Version: 3_0_n
//Sif Version: 6
//libNickName: booklib
//moduleName:  Vlayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 u_in(size); // 
public NslDoutDouble0 vf(); // 
private double vp[size]; // 
private double h2; // 

//methods 
public void initModule() {
	vf=0;
}

public void initRun() {
	vf=0;
	vp=0;
	h2=0;
}

public void simRun(){
	up = nslDiff(vp, -vp + nslSum(u_in) -h2);
	uf = nslRamp(vp);
}

}//end Vlayer

