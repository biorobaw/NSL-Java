/* SCCS @(#)DomOutDisplay.mod	1.2 -- 09/22/99 -- 23:20:54 */
// DomOutDisplay
// This is the module that displays all of the output graphs in a NslFrame

nslImport nslAllImports;

nslModule DomOutDisplay(int stdsz, int bigsz) extends NslOutModule () {

//input ports
public 	NslDinFloat2 visualinputSub(stdsz,stdsz);
public 	NslDinFloat2 stimSC(stdsz,stdsz);
public 	NslDinFloat2 stimFEF(stdsz,stdsz);
public 	NslDinFloat0 posteriorParietalCenter();
public 	NslDinFloat2 ppqv(stdsz,stdsz);
public 	NslDinFloat2 scqv(stdsz,stdsz);
public 	NslDinFloat2 supcol(stdsz,stdsz);
public 	NslDinFloat2 scsac(stdsz,stdsz);
public 	NslDinFloat2 fefsac(stdsz,stdsz);
public 	NslDinFloat2 fefvis(stdsz,stdsz);
public 	NslDinFloat2 fefmem(stdsz,stdsz);
public 	NslDinFloat2 thmem(stdsz,stdsz);
public 	NslDinFloat0 horizontalTheta();
public 	NslDinFloat0 verticalTheta();

//public 	NslDinFloat0 motorLmn();
//public 	NslDinFloat0 motorRmn();
//public 	NslDinFloat0 motorUmn();
//public 	NslDinFloat0 motorDmn();


// variables
private	boolean goodstatus=false; //false=fail; true=success

private int center ;
private int centerP3 ;
private int centerP2 ;
private int centerM2 ;
private int centerM3 ;

private 	NslFloat0 fixation();
private 	NslFloat0 visinP3M3();
private 	NslFloat0 visinM3P3();
private 	NslFloat0 visinM3P0();
private 	NslFloat0 visinP0M2();
private 	NslFloat0 visinM2P0();
private 	NslFloat0 visinM2P2();
private 	NslFloat0 visinM2M3();
private 	NslFloat0 stimfefM3P0();
private 	NslFloat0 stimscM3P0();
private 	NslFloat0 stimfefP0M2();
private 	NslFloat0 stimscP0M2();

/*
// The following is used only if you are not using ports
// for communication with this module.
// Was used in debug phase.

private 	NslFloat0 motorLmn();
private 	NslFloat0 motorRmn();
private 	NslFloat0 motorUmn();
private 	NslFloat0 motorDmn();

private 	NslFloat0 motorLebn();
private 	NslFloat0 motorRebn();
private 	NslFloat0 motorUebn();
private 	NslFloat0 motorDebn();

private 	NslFloat0 tnChangeLtn();
private 	NslFloat0 tnChangeRtn();
private 	NslFloat0 tnChangeUtn();
private 	NslFloat0 tnChangeDtn();

private 	NslFloat2 llbn (stdsz,stdsz);
private 	NslFloat2 visualinputSub(stdsz,stdsz);
private 	NslFloat0 posteriorParietalCenter();
private 	NslFloat2 ppqv(stdsz,stdsz);
private 	NslFloat2 scqv(stdsz,stdsz);
private 	NslFloat2 supcol(stdsz,stdsz);
private 	NslFloat2 scsac(stdsz,stdsz);
private 	NslFloat2 scDelay(stdsz,stdsz);
private 	NslFloat2 fefsac(stdsz,stdsz);
private 	NslFloat2 fefvis(stdsz,stdsz);
private 	NslFloat2 fefmem(stdsz,stdsz);
private 	NslFloat2 thmem(stdsz,stdsz);
private 	NslFloat2 cdmem(stdsz,stdsz);
private 	NslFloat2 snrmem(stdsz,stdsz);
*/

public void initModule() {
	// note this is different than that declared in the visualinput.mod
	center = (int)stdsz/2;
	centerP3 = center + 3;
	centerP2 = center + 2;
	centerM3 = center - 3;
	centerM2 = center - 2;

/*
	// the following is only if you are using
	// nslSetValue(var, charcharString);
	motorLmn.nslSetAccess('W');
	motorRmn.nslSetAccess('W');
	motorUmn.nslSetAccess('W');
	motorDmn.nslSetAccess('W');

	motorLebn.nslSetAccess('W');
	motorRebn.nslSetAccess('W');
	motorUebn.nslSetAccess('W');
	motorDebn.nslSetAccess('W');

	tnChangeLtn.nslSetAccess('W');
	tnChangeRtn.nslSetAccess('W');
	tnChangeUtn.nslSetAccess('W');
	tnChangeDtn.nslSetAccess('W');

	visualinputSub.nslSetAccess('W');
	posteriorParietalCenter.nslSetAccess('W');
	scqv.nslSetAccess('W');
	supcol.nslSetAccess('W');
	scsac.nslSetAccess('W');
	scDelay.nslSetAccess('W');
	llbn.nslSetAccess('W');
	fefsac.nslSetAccess('W');
	ppqv.nslSetAccess('W');
	fefvis.nslSetAccess('W');
	fefmem.nslSetAccess('W');
	thmem.nslSetAccess('W');
	cdmem.nslSetAccess('W');
	snrmem.nslSetAccess('W');
*/
//  For Big Displays
/*      if (!(system.getNoDisplay())) {		
	nslAddAreaCanvas(visualinputSub,0,90);
	nslAddAreaCanvas(stimSC,0,200); //175
	nslAddAreaCanvas(stimFEF,0,200);//175
	nslAddTemporalCanvas(posteriorParietalCenter,0,100);
//	nslAddAreaCanvas(llbn,-200,2000);
	nslAddAreaCanvas(ppqv,0,100);
	nslAddAreaCanvas(scqv,0,100);
	nslAddAreaCanvas(supcol,0,100);
//	nslAddAreaCanvas(scsac,0,100);
//	nslAddAreaCanvas(scDelay,0,100);
	nslAddAreaCanvas(fefsac,0,100);
	nslAddAreaCanvas(fefvis,0,100);
//	nslAddAreaCanvas(fefmem,0,100);
	nslAddAreaCanvas(thmem,0,20);
//	nslAddAreaCanvas(cdmem,0,100);
//	nslAddAreaCanvas(snrmem,0,100);
	nslAddTemporalCanvas(horizontalTheta,-5,5);
	nslAddTemporalCanvas(verticalTheta,-5,5);

//	nslAddTemporalCanvas(motorLmn,-200,2000);
//	nslAddTemporalCanvas(motorRmn,-200,2000);
//	nslAddTemporalCanvas(motorUmn,-200,2000);
//	nslAddTemporalCanvas(motorDmn,-200,2000);

//	nslAddTemporalCanvas(motorLebn,-200,2000);
//	nslAddTemporalCanvas(motorRebn,-200,2000);
//	nslAddTemporalCanvas(motorUebn,-200,2000);
//	nslAddTemporalCanvas(motorDebn,-200,2000);

//	nslAddTemporalCanvas(tnChangeLtn,-200,2000);
//	nslAddTemporalCanvas(tnChangeRtn,-200,2000);
//	nslAddTemporalCanvas(tnChangeUtn,-200,2000);
//	nslAddTemporalCanvas(tnChangeDtn,-200,2000);

      }  //if not noDisplay]
*/

// For time plots
      if (!(system.getNoDisplay())) {		
	nslAddTemporalCanvas(fixation,0,100);
	nslAddTemporalCanvas(posteriorParietalCenter,0,100);
	nslAddTemporalCanvas(visinM3P0,0,100);
	nslAddTemporalCanvas(visinM3P3,0,100);
	nslAddTemporalCanvas(visinP3M3,0,100);
	nslAddTemporalCanvas(visinP0M2,0,100);
	nslAddTemporalCanvas(visinM2P2,0,100);
	nslAddTemporalCanvas(visinM2M3,0,100);
	nslAddTemporalCanvas(stimfefM3P0,0,200); //175
	nslAddTemporalCanvas(stimscM3P0,0,200);  //175
	nslAddTemporalCanvas(stimfefP0M2,0,200); //175
	nslAddTemporalCanvas(stimscP0M2,0,200);  //175
	nslAddTemporalCanvas(horizontalTheta,-5.5,5.5);
	nslAddTemporalCanvas(verticalTheta,-5.5,5.5);
      }  //if not noDisplay
}

public void initRun(){
	fixation=0;
	visinP3M3=0;
	visinM3P3=0;
	visinM3P0=0;
	visinP0M2=0;
	visinM2P2=0;
	visinM2M3=0;
	stimfefM3P0=0;
	stimscM3P0=0;
	stimfefP0M2=0;
	stimscP0M2=0;

	if (system.debug>27) {
		nslPrintln("DomOutDisplay:initRun");
	}
	getValues();

}
public void simRun(){
	getValues();	           

}
public void getValues(){	

/*

	// 99/7/28 aa: the following is commented out because
	// it was only used for debugging and it runs
	// slower than using ports.
         goodstatus=nslSetValue(visualinputSub,"domineyModel.visualinput1.visualinputSub");
	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get visualinput"); 
        }
	 goodstatus=nslSetValue(posteriorParietalCenter,"domineyModel.fon1.fonCenter");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get fon"); 
        }

	goodstatus=nslSetValue(scqv,"domineyModel.supcol1.scqv");
 	if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get scqv"); 
        }

	 goodstatus=nslSetValue(ppqv,"domineyModel.ppqv1.ppqv");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get ppqv"); 
        }

	 goodstatus=nslSetValue(llbn,"domineyModel.brainStem1.llbn1.llbn");
	 if (!goodstatus) {
	  	nslPrintln("DomOutDisplay: bad status in get llbn"); 
        }

	 goodstatus=nslSetValue(supcol,"domineyModel.supcol1.supcol");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get supcol"); 
        }
	 goodstatus=nslSetValue(scsac,"domineyModel.supcol1.scsac");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get scsac"); 
        }
	 goodstatus=nslSetValue(scDelay,"domineyModel.supcol1.scDelay");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get scDelay"); 
        }

	 goodstatus=nslSetValue(fefsac,"domineyModel.fef1.fefsac");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get fefsac"); 
        }
	 goodstatus=nslSetValue(fefvis,"domineyModel.fef1.fefvis");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get fefvis"); 
        }
	 goodstatus=nslSetValue(fefmem,"domineyModel.fef1.fefmem");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get fefmem"); 
        }
	 goodstatus=nslSetValue(thmem,"domineyModel.thal1.thmem");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get thmem"); 
        }
	 goodstatus=nslSetValue(cdmem,"domineyModel.bg1.cdmem");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get cdmem"); 
        }
	 goodstatus=nslSetValue(snrmem,"domineyModel.bg1.snrmem");
		 if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get snrmem"); 
        }


	  goodstatus=nslSetValue(motorLmn,"domineyModel.brainStem1.motorl.mn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motorl"); 
        }

	  goodstatus=nslSetValue(motorRmn,"domineyModel.brainStem1.motorr.mn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motorr"); 
        }
	goodstatus=nslSetValue(motorUmn,"domineyModel.brainStem1.motoru.mn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motoru"); 
        }
	goodstatus=nslSetValue(motorDmn,"domineyModel.brainStem1.motord.mn");
		  	  if (!goodstatus) {
			nslPrintln("DomOutDisplay: bad status in get motord"); 
	}


	goodstatus=nslSetValue(motorLebn,"domineyModel.brainStem1.motorl.ebn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motorl"); 
        }

	goodstatus=nslSetValue(motorRebn,"domineyModel.brainStem1.motorr.ebn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motorr"); 
        }
	goodstatus=nslSetValue(motorUebn,"domineyModel.brainStem1.motoru.ebn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get motoru"); 
        }
	goodstatus=nslSetValue(motorDebn,"domineyModel.brainStem1.motord.ebn");
		  	  if (!goodstatus) {
			nslPrintln("DomOutDisplay: bad status in get motord"); 
	}

	goodstatus=nslSetValue(tnChangeLtn,"domineyModel.brainStem1.tnChangel.tn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get tnChangel"); 
        }

	goodstatus=nslSetValue(tnChangeRtn,"domineyModel.brainStem1.tnChanger.tn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get tnChanger"); 
        }
	goodstatus=nslSetValue(tnChangeUtn,"domineyModel.brainStem1.tnChangeu.tn");
	  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get tnChangeu"); 
        }
	goodstatus=nslSetValue(tnChangeDtn,"domineyModel.brainStem1.tnChanged.tn");
		  	  if (!goodstatus) {
		nslPrintln("DomOutDisplay: bad status in get tnChanged"); 
	}
*/

	fixation=visualinputSub.get(center,center);
	visinP3M3=visualinputSub.get(centerP3,centerM3);
	visinM3P3=visualinputSub.get(centerM3,centerP3);
	visinM3P0=visualinputSub.get(centerM3,center);
	visinP0M2=visualinputSub.get(center,centerM2);
	visinM2P2=visualinputSub.get(centerM2,centerP2);
	visinM2M3=visualinputSub.get(centerM2,centerM3);

	stimfefM3P0=stimFEF.get(centerM3,center);
	stimscM3P0=stimSC.get(centerM3,center);
	stimfefP0M2=stimFEF.get(center,centerM2);
	stimscP0M2=stimSC.get(center,centerM2);



}  //end getValues

} //end class





















