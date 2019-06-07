h58763
s 00000/00000/00057
d D 1.2 99/09/22 23:20:57 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00057/00000/00000
d D 1.1 99/09/22 22:42:48 aalx 1 0
c date and time created 99/09/22 22:42:48 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*MLBN
* Medium lead burst neurons -  module
* @see  MLBN.nslm
* @version 98/6/18
* @author Dominey and Alexander
*/
nslImport nslAllImports;

nslModule MLBN (int stdsz)  {

// ports
public NslDinFloat2 stm(stdsz,stdsz);  //input - spatio temporal transformations
public NslDinFloat2 llbn(stdsz,stdsz);  //input
public NslDoutFloat2 mlbn (stdsz,stdsz); // output
// parameters 
private NslFloat0 mlbnPot_tm();
private NslFloat0 mlbn_kx1();
private NslFloat0 mlbn_kx2();
private NslFloat0 mlbn_ky1();
private NslFloat0 mlbn_ky2();
// vars
private NslFloat2 mlbnPot (stdsz,stdsz);	// medium lead burst neurons of the brainstem saccade generator


public void initRun() {
       mlbn=0;
       mlbnPot=0;

	mlbnPot_tm=0.008;
	mlbn_kx1=0;
	mlbn_kx2=1500;
	mlbn_ky1=0;
	mlbn_ky2=950;

}
public void simRun() {
	// leftSTM, rightSTM etc have weights that increase with distance from fovea
	// performing the SpatioTeMporal transformation.
	// ^ = pointwise multiplication
	// medium lead burst neurons - see Hepp and Henn (in refs) for details.

	mlbnPot=nslDiff(mlbnPot,mlbnPot_tm, -mlbnPot + (stm^llbn));
	mlbn = nslSaturation(mlbnPot,mlbn_kx1,mlbn_kx2,mlbn_ky1,mlbn_ky2);
	if (system.debug>=7) {
		nslPrintln("debug: MLBN");
		nslPrintln(mlbn);
	}
}
}






E 1
