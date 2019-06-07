/* This file is generated by  NSL3.0 preprocessor*/

/* SCCS  %W% -- %G% -- %U% */

/** STM
* Spatio Temporal Transformations
* @see STM.nslm
* @version 98/6/18
* @author Dominey; coder Alexander
*/
 import nslj.src.system.*; 
 import nslj.src.cmd.*; 
 import nslj.src.lang.*; 
 import nslj.src.math.*; 
 import nslj.src.display.*; 

 public class STM extends NslModule /*(int stdsz, int direction)*/{
//directions : 1=l,2=r,3=u.4=d
// port inputs
// port outputs
public NslDoutFloat2 stm ; /* (stdsz,stdsz)*/
public NslDoutFloat2 weights ; /* (stdsz,stdsz)*/
// parameters
private NslFloat0 stm_k1 ; /* ()*/

//vars
	// these define the increasing projection strengths
	// for more peripheral items in the FEF and supcol onto
	// llbns in the brainstem - see Appendix on
	// brainstem saccade generator in cerebral cortex paper
private NslFloat1 weighttopo ; /*(stdsz)*/
private NslFloat1 stmtopo ; /*(stdsz)*/


public  void initModule() {	
}

public  void initRun() {
	stm_k1.set(0.32);
	weighttopo.set(0);
	stmtopo.set(0);
	weights.set(0);
	stm.set(0);

 	if ((direction==1)||(direction==3)){
		(weighttopo).set(0,5.7);
		(weighttopo).set(1,4.275);
		(weighttopo).set(2,2.85);
		(weighttopo).set(3,1.425);
		(stmtopo).set(0,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(0)));
		(stmtopo).set(1,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(1)));
		(stmtopo).set(2,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(2)));
		(stmtopo).set(3,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(3)));
	}
	if ((direction==2)||(direction==4)){
		(weighttopo).set(5,1.425);
		(weighttopo).set(6,2.85);
		(weighttopo).set(7,4.275);
		(weighttopo).set(8,5.7);
		(stmtopo).set(5,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(5)));
		(stmtopo).set(6,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(6)));
		(stmtopo).set(7,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(7)));
		(stmtopo).set(8,
 nslj.src.math.NslElemMult.eval(stm_k1.get(),(weighttopo).get(8)));
	}

	if ((direction==1)||(direction==2)){
		weights.set(NslFillRows.eval(weights,weighttopo)) /* rule 108 */;//fill all rows the same
	        stm.set(NslFillRows.eval(stm,stmtopo)) /* rule 108 */;
	}
	if ((direction==3)||(direction==4)){
		weights.set(NslFillColumns.eval(weights,weighttopo)) /* rule 108 */;//fill all columns the same
		stm.set(NslFillColumns.eval(stm,stmtopo)) /* rule 108 */;
	}

	
 	if (system.debug>=26) {
	 System.out.println(("STM: initRun: direction  :")+(direction));
	 System.out.println("STM: initRun: stmtopo");
	 System.out.println(stmtopo);
	 System.out.println("STM: initRun: stm");
	 System.out.println(stm);
	 System.out.println("STM: initRun: weights:");
	 System.out.println(weights);
	}
}

	/* nslInitTempModule inserted by NPP */
public void nslInitTempModule() {
	/* Instantiation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic instantiation statements */
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* nslInitTempRun inserted by NPP */
public void nslInitTempRun() {
	/* Intialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* nslInitTempTrain inserted by NPP */
public void nslInitTempTrain() {
	/* Initialisation statements generated by NslPreProcessor */
	/* temporary variables */
	/* end of automatic intialisation statements */
}

	/* Declaration statements generated by NslPreProcessor */
	/* makeinst() declared variables */
	/* temporary variables */
	/* constructor and related methods */
	/* nsl declarations */
	int stdsz;
	int direction;

	 /*GENERIC CONSTRUCTOR:   */
	 public STM(String nslName, NslModule nslParent,int stdsz, int direction) {
		super(nslName, nslParent);
		this.stdsz = stdsz;
		this.direction = direction;
		initSys();
		makeInst(nslName, nslParent,stdsz, direction);
	}
	public void makeInst(String nslName, NslModule nslParent,int stdsz,int direction){ 
	 stm=new NslDoutFloat2 ("stm",this,stdsz,stdsz); //NSLDECLS 
	 weights=new NslDoutFloat2 ("weights",this,stdsz,stdsz); //NSLDECLS 
	 stm_k1=new NslFloat0 ("stm_k1",this); //NSLDECLS 
	 weighttopo=new NslFloat1 ("weighttopo",this,stdsz); //NSLDECLS 
	 stmtopo=new NslFloat1 ("stmtopo",this,stdsz); //NSLDECLS 
	}
	/* end of automatic declaration statements */
} //end class
