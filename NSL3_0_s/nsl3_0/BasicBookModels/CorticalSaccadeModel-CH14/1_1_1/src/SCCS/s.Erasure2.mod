h64732
s 00000/00000/00060
d D 1.2 99/09/22 23:20:56 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00060/00000/00000
d D 1.1 99/09/22 22:42:47 aalx 1 0
c date and time created 99/09/22 22:42:47 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W% --- %G% -- %U% */

/*Erasure2
* Erasure2 Mask  - was called M2 in NSL2.1.7 version
* @see Erasure2.nslm
* @version 98/6/18
* @author Dominey and Alexander
*
*/

nslImport nslAllImports;

nslModule Erasure2 (int stdsz)  {
// port inputs
// port outputs
public NslDoutFloat2 erasure2(stdsz,stdsz);

// parameters 
//vars
int 	halfStdsz=0;
int	oneThirdStdsz=0;
int	twoThirdsStdszMin1=0;


public void initModule() {	
	halfStdsz=(int)stdsz/2;  // if stdsz=9, then this is 4
	oneThirdStdsz=(int)stdsz/3;  //if stdsz=9, then this is 3
	twoThirdsStdszMin1=oneThirdStdsz+ oneThirdStdsz-1; //if, then 5
}

public void initRun() {
	// the following draws a square outlining the fovea
	// with 1 everywhere except the outline which is 0.5
	erasure2=1;       // set everything to 1 then set some to 0.5                    
	erasure2[oneThirdStdsz][oneThirdStdsz] = .5;
	erasure2[oneThirdStdsz][halfStdsz] = 0.5; 
	erasure2[oneThirdStdsz][twoThirdsStdszMin1] = 0.5;

	erasure2[halfStdsz][oneThirdStdsz] = 0.5;
	erasure2[halfStdsz][halfStdsz] = 0.5; 
	erasure2[halfStdsz][twoThirdsStdszMin1] = 0.5;

	erasure2[twoThirdsStdszMin1][oneThirdStdsz] = 0.5;
	erasure2[twoThirdsStdszMin1][halfStdsz] = 0.5; 
	erasure2[twoThirdsStdszMin1][twoThirdsStdszMin1] = 0.5;

	if (system.debug>1) {
		nslPrintln("Erasure2:initRun: erasure2");
		nslPrintln(erasure2);
	}
}

}







E 1
