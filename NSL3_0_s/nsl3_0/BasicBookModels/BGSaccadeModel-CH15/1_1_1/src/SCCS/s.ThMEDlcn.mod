h44872
s 00050/00000/00000
d D 1.1 99/09/24 18:57:32 aalx 1 0
c date and time created 99/09/24 18:57:32 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)ThMEDlcn.mod	1.8 --- 08/05/99 -- 13:56:45 : jversion  @(#)ThMEDlcn.mod	1.2---04/23/99--18:33:45 */

nslImport nslAllImports;

//
//Module ThMEDlcn - part of Thalamus
//

nslModule ThMEDlcn(int array_size){
//inputs
    public NslDinDouble2 RNMEDinh_in(array_size,array_size)  ;
//outputs
    public NslDoutDouble2 ThMEDlcn_out(array_size,array_size)  ;

	private double    Thmedlcntm;
	private double    ThmedlcnTONIC;
	private double    ThmedlcnK;
	private double    ThMEDlcnsigma1;
	private double    ThMEDlcnsigma2;
	private double    ThMEDlcnsigma3;
	private double    ThMEDlcnsigma4;
	private NslDouble2 Thmedlcn(array_size,array_size)   ;
  
public void initRun(){
    Thmedlcn = 0;
    ThMEDlcn_out = 0;

     Thmedlcntm=0.006;
     ThmedlcnTONIC=10; ThmedlcnK=1;
     ThMEDlcnsigma1=-10;
     ThMEDlcnsigma2=10;
     ThMEDlcnsigma3=0;
     ThMEDlcnsigma4=10;
}

public void simRun(){
	  /* <Q> RNMEDinh_in */
      // System.err.println("@@@@ ThMEDlcn simRun entered @@@@");
    Thmedlcn=nslDiff(Thmedlcn,Thmedlcntm,-Thmedlcn
                     +ThmedlcnTONIC-(ThmedlcnK*RNMEDinh_in));

    ThMEDlcn_out=Nsl2Sigmoid.eval(Thmedlcn,ThMEDlcnsigma1,
                            ThMEDlcnsigma2,
                            ThMEDlcnsigma3,
                            ThMEDlcnsigma4);
  }

} //end class

E 1
