/* SCCS  @(#)Generator.mod	1.1---09/24/99--18:57:19 */
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
