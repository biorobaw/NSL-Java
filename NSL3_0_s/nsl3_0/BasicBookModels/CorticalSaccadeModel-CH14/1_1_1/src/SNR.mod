/* SCCS  @(#)SNR.mod	1.2 -- 09/22/99 -- 23:20:59 */

/*SNR
* Substantia Nigra
* @see SNR.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/
nslImport nslAllImports;

nslModule SNR (int stdsz)  {

// port inputs
public NslDinFloat2 cdmem(stdsz,stdsz);
public NslDinFloat2 cdsac(stdsz,stdsz);
// port outputs 
public NslDoutFloat2 snrmem(stdsz,stdsz);
public NslDoutFloat2 snrsac(stdsz,stdsz);
// parameters 
private NslFloat0 snrmemPot_tm();
private NslFloat0 snrsacPot_tm();
private NslFloat0 snrmemPot_k1();
private NslFloat0 snrsacPot_k1();
private NslFloat0 snrmem_x1();
private NslFloat0 snrmem_x2();
private NslFloat0 snrmem_y1();
private NslFloat0 snrmem_y2();
private NslFloat0 snrsac_x1();
private NslFloat0 snrsac_x2();
private NslFloat0 snrsac_y1();
private NslFloat0 snrsac_y2();

//vars
private NslFloat2 snrmemPot(stdsz,stdsz);
private NslFloat2 snrsacPot(stdsz,stdsz);



public void initRun() {
	snrmem=100;
	snrsac=100;
        snrmemPot=0;
        snrsacPot=0;

	snrmemPot_tm= .04 ;
	snrsacPot_tm=  .02;
	snrmemPot_k1= 1.5 ;
	snrsacPot_k1=  1;	
	snrmem_x1=0;
	snrmem_x2=  50;
	snrmem_y1= 100 ;
	snrmem_y2=0  ;
	snrsac_x1=  0;
	snrsac_x2= 50 ;
	snrsac_y1=  100;
	snrsac_y2= 0 ;	
}
public void simRun() {
	snrsacPot=nslDiff(snrsacPot,snrsacPot_tm,- snrsacPot + snrsacPot_k1*cdsac);
	snrmemPot=nslDiff(snrmemPot,snrmemPot_tm, - snrmemPot + snrmemPot_k1*cdmem);
	snrsac = nslSigmoid(snrsacPot,snrsac_x1,snrsac_x2,snrsac_y1,snrsac_y2);
	snrmem = nslSigmoid(snrmemPot,snrmem_x1,snrmem_x2,snrmem_y1,snrmem_y2);

	if (system.debug>=23) {
		nslPrintln("SNR: snrsac");
		nslPrintln(snrsac);
		nslPrintln("SNR: snrmem");
		nslPrintln(snrmem);
	}
}

}  // end class




