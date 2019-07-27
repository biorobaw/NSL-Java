h17258
s 00000/00000/00041
d D 1.2 99/09/22 23:20:53 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00041/00000/00000
d D 1.1 99/09/22 22:42:45 aalx 1 0
c date and time created 99/09/22 22:42:45 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*BasalGanglia
* BasalGanglia
* @see BasalGanglia.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/
nslImport nslAllImports;

nslModule BasalGanglia (int stdsz)  {
// port inputs
public NslDinFloat2 fefmem(stdsz,stdsz);
public NslDinFloat2 fefsac(stdsz,stdsz);
// port outputs
public NslDoutFloat2 snrmem(stdsz,stdsz);
public NslDoutFloat2 snrsac(stdsz,stdsz);
// parameters 
//vars
private Caudate cd(stdsz);
private SNR snr(stdsz);

public void makeConn() {
	nslRelabel(fefsac,cd.fefsac);
	nslRelabel(fefmem,cd.fefmem);
	nslConnect(cd.cdsac,snr.cdsac);
	nslConnect(cd.cdmem,snr.cdmem);
	nslRelabel(snr.snrsac,snrsac);
	nslRelabel(snr.snrmem,snrmem);
}

public void simRun() {
	if (system.debug>=21) {
		nslPrintln("BG: simRun");
	}
}

}


E 1
