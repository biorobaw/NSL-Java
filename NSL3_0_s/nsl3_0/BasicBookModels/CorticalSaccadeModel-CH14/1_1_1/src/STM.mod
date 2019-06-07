/* SCCS  @(#)STM.mod	1.2 -- 09/22/99 -- 23:20:59 */

/** STM
* Spatio Temporal Transformations
* @see STM.nslm
* @version 98/6/18
* @author Dominey; coder Alexander
*/
nslImport nslAllImports;

nslModule STM (int stdsz, int direction){
//directions : 1=l,2=r,3=u.4=d
// port inputs
// port outputs
public NslDoutFloat2 stm (stdsz,stdsz);
public NslDoutFloat2 weights (stdsz,stdsz);
// parameters
private NslFloat0 stm_k1 ();

//vars
	// these define the increasing projection strengths
	// for more peripheral items in the FEF and supcol onto
	// llbns in the brainstem - see Appendix on
	// brainstem saccade generator in cerebral cortex paper
private NslFloat1 weighttopo(stdsz);
private NslFloat1 stmtopo(stdsz);


public void initModule() {	
}

public void initRun() {
	stm_k1=0.32;
	weighttopo=0;
	stmtopo=0;
	weights=0;
	stm=0;

 	if ((direction==1)||(direction==3)){
		weighttopo[0] = 5.7;
		weighttopo[1] = 4.275;
		weighttopo[2] = 2.85;
		weighttopo[3] = 1.425;
		stmtopo[0]=stm_k1*weighttopo[0];
		stmtopo[1]=stm_k1*weighttopo[1];
		stmtopo[2]=stm_k1*weighttopo[2];
		stmtopo[3]=stm_k1*weighttopo[3];
	}
	if ((direction==2)||(direction==4)){
		weighttopo[5] = 1.425;
		weighttopo[6] = 2.85;
		weighttopo[7] = 4.275;
		weighttopo[8] = 5.7;
		stmtopo[5]=stm_k1*weighttopo[5];
		stmtopo[6]=stm_k1*weighttopo[6];
		stmtopo[7]=stm_k1*weighttopo[7];
		stmtopo[8]=stm_k1*weighttopo[8];
	}

	if ((direction==1)||(direction==2)){
		weights = nslFillRows(weights,weighttopo);//fill all rows the same
	        stm = nslFillRows(stm,stmtopo);
	}
	if ((direction==3)||(direction==4)){
		weights = nslFillColumns(weights,weighttopo);//fill all columns the same
		stm = nslFillColumns(stm,stmtopo);
	}

	
 	if (system.debug>=26) {
	 nslPrintln("STM: initRun: direction  :" + direction);
	 nslPrintln("STM: initRun: stmtopo");
	 nslPrintln(stmtopo);
	 nslPrintln("STM: initRun: stm");
	 nslPrintln(stm);
	 nslPrintln("STM: initRun: weights:");
	 nslPrintln(weights);
	}
}

} //end class





