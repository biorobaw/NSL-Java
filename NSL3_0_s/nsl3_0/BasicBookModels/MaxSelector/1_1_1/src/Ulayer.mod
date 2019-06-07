

nslModule Ulayer(int size){

//NSL Version: 3_0_n
//Sif Version: 6
//libNickName: booklib
//moduleName:  Ulayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 s_in(); // This is the source input for the module
public NslDinDouble0 v_in(); // Feedback input

public NslDoutDouble1 uf(size); // Firing rate output from module Ulayer.

private NslDouble0 h1(); // 
private NslDouble1 up(size); // 

//methods 
public void initModule() {
	uf=0;
}

public void initRun() {
	uf=0;
	up=0;
	h1=0.1;
}

public void simRun(){
	up = nslDiff(up, -up + uf - v_in - h1 + s_in);
	uf = nslStep(up,0.1,0,1.0);
}

}//end Ulayer

