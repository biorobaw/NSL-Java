/**
 * 
 */
package support;

/**
 * @author gtejera
 *
 */
public class Percentage {
	private long porcentaje=0;
	private String name;
	private int step;
	private boolean notTimedTrial = false;
	/**
	 * @param name
	 */
	public Percentage(String name, int step) {
		super();
		this.name = name;
		if (step<1) step =1;
		this.step = step;
	}

	/**
	 * @param epocasPorEnsayototal==0
	 * @param currenTrial
	 */
	public void printPorcentage(long actual, long total) {
		if (total==0 &&  !notTimedTrial ) {
			System.out.println(name+"::no es posible calcular el porcentaje de procesamiento por la naturaleza del ensayo.");
			notTimedTrial=true;
		} else if ((total!=0) &&(100*actual/total>porcentaje)) {
			porcentaje++;
			if ((porcentaje%step)==0)
				System.out.println(name+"::porcentaje de procesamiento: " + porcentaje + "%");
			notTimedTrial=false;
		}
	}

	/**
	 * @param avancesPorEnsayo
	 * @param actions
	 * @param string
	 */
	public void printPorcentage(long actual, long total,
			String string) {
		if (total==0 &&  !notTimedTrial ) {
			System.out.println(name+"::no es posible calcular el porcentaje de procesamiento por la naturaleza del ensayo.");
			notTimedTrial=true;
		} else if ((total!=0) &&(100*actual/total>porcentaje)) {
			porcentaje++;
			if ((porcentaje%step)==0)
				System.out.println(name+"::porcentaje de procesamiento: " + porcentaje + "%. "+string);
			notTimedTrial=false;
		}
	}
}
