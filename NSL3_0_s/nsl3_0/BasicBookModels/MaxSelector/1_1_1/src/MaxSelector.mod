
nslModule MaxSelector(int size) extends NslModule() {

//NSL Version: 3_0_n
//Sif Version: 6
//libNickName: booklib
//moduleName:  MaxSelector
//versionName: 1_1_1
//floatSubModules: true


//variables 
private Ulayer u1(size); // This is the main module within the model
private Vlayer v1(); // 

public NslDinDouble1 in(size); // 
public NslDoutDouble1 out(size); // 

//methods 
public void makeConn(){
    nslConnect(this.in, u1.s_in);
    nslConnect(v1.vf, u1.v_in);
    nslConnect(u1.uf, v1.u_in);
    nslConnect(u1.uf, this.out);
}
}//end MaxSelector

