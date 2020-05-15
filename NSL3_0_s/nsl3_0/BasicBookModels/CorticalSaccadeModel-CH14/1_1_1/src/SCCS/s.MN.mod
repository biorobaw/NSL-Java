h49046
s 00000/00000/00037
d D 1.2 99/09/22 23:20:57 aalx 2 1
c updated for NSL3_0_m and made manual the same as simple
e
s 00037/00000/00000
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

/*MN
* Motor Neurons
* @see MN.nslm
* @version 98/6/18
* @author Dominey ; coder Alexander
*
*/
nslImport nslAllImports;

nslModule MN ()  {

// ports
public NslDinFloat0 ebn ();
public NslDinFloat0 tn ();
public NslDoutFloat0 mn  ();
// parameters 

//vars


public void initRun() {
	mn=0;
}

public void simRun() {
	mn = ebn + tn;

	if (system.debug>=11) {
		nslPrintln("MN:simRun:mn"+mn);
	}
}
}



E 1
