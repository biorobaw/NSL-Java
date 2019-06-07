h49139
s 00023/00000/00000
d D 1.1 99/09/24 18:57:19 aalx 1 0
c date and time created 99/09/24 18:57:19 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)Generator.mod	1.8 --- 08/05/99 -- 13:56:21 : jversion  @(#)Generator.mod	1.2---04/23/99--18:39:30 */

nslImport nslAllImports;

nslModule Generator(int array_size){
	// output ports
	public NslDoutDouble2 generator(array_size,array_size);  

	//variables
	private double x;

public void initRun() { 
/*
   // commented out since we provide input thru the visual input
   // this was just for testing
	generator=0;

    generator=nslRandom(588599,0,50);
*/
}

} // end class
E 1
