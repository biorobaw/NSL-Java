/* SCCS  @(#)BasalGanglia.mod	1.2 --- 09/22/99 -- 23:20:53 */

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


