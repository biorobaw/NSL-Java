h48225
s 00050/00000/00000
d D 1.1 99/09/24 18:57:26 aalx 1 0
c date and time created 99/09/24 18:57:26 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)RNMEDinh.mod	1.8 --- 08/05/99 -- 13:56:33 : jversion  @(#)RNMEDinh.mod	1.2---04/23/99--18:39:41 */

nslImport nslAllImports;

/**
* Module RNMEDinh - part of thalamus
*/
nslModule RNMEDinh(int array_size) {
	//inports
    public NslDinDouble2 SNRmedburst_in(array_size,array_size)  ;
	//outports
    public NslDoutDouble2 RNMEDinh_out(array_size,array_size)  ;

	// privates
    private NslDouble2 RNmedinh (array_size,array_size)   ;
	private double     RNmedinhtm;
	private double     RNmedinhTONIC;
	private double     RNmedinhK;
	private double     RNmedinhsigma1;
	private double     RNmedinhsigma2;
	private double     RNmedinhsigma3;
	private double     RNmedinhsigma4;


public void initRun(){
    RNmedinh = 0;
    RNMEDinh_out = 0;
     RNmedinhtm=0.006;
     RNmedinhTONIC=10;
     RNmedinhK=0.16;
     RNmedinhsigma1=-10;
     RNmedinhsigma2=10;
     RNmedinhsigma3=0;
     RNmedinhsigma4=10;
}

public void simRun() {
	  /* <Q> SNRmedburst_in */
  // System.err.println("@@@@ RNMEDinh simRun entered @@@@");
    RNmedinh=nslDiff(RNmedinh,RNmedinhtm,-RNmedinh
                     +RNmedinhTONIC -(RNmedinhK*SNRmedburst_in));

    RNMEDinh_out=Nsl2Sigmoid.eval(RNmedinh,RNmedinhsigma1,
                            RNmedinhsigma2,
                            RNmedinhsigma3,
                            RNmedinhsigma4);
  }

} //end class
E 1
