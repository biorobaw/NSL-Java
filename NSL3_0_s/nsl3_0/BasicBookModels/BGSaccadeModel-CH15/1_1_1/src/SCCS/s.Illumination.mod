h11725
s 00260/00000/00000
d D 1.1 99/09/24 18:57:20 aalx 1 0
c date and time created 99/09/24 18:57:20 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)Illumination.mod	1.8 --- 08/05/99 -- 13:56:23 : jversion  @(#)Illumination.mod	1.8---08/05/99--13:56:23 */
verbatim_NSLJ;

import java.util.StringTokenizer;
import java.io.DataInputStream;

/**
* The Illumination class contains the input visual events that drive the model
* to perform saccades or maintain fixation. Each event has a location in
* the visual field, a level of activation, a start time, a stop time,
* and a pointer to the next event.
* An event is one node
* @author AA and MC
* @version 1.0 Dec 6, 1996
*/
public class Illumination {

  static final int MaxCorticalFiring = 90;
  static final int CorticalArraySize = 9;

  int x,y;
  double activation;
  double start;
  double stop;
  
  public int getX() {return x;}
  public int getY() {return y;}
  public double getValue () {return activation;}
  public double getStart () {return start;}
  public double getStop () {return stop;}

  public Illumination () {
    System.out.println("Error in Illumination initialization.");
  }
  public Illumination (int tx, int ty, double tactivation, 
	double tstart, double tstop) {
    x=tx;
    y=ty;
    activation=tactivation;
    start = tstart;
    stop = tstop;
    // next=NULL;
  }
  public void changeValues(int tx, int ty, double tactivation, 
	double tstart, double tstop) {
      //  Change values for current Fixation Illumination

      x          = tx;
      y          = ty;
      activation = tactivation;
      start      = tstart;
      stop       = tstop;
      // do not touch next pointer
      return;
  }

static public Illumination queryForValStartStop(int elemtx,int elemty, 
	String qvalstr, String qstartstr) {
  int tx;
  int ty;
  double tstart;
  double tval;
  double tstop;
  String  tvalstr;
  String  tstartstr;
  Illumination cur;
  DataInputStream ins = new DataInputStream(System.in);

//  Prompt for the event data
    tx = elemtx;
    ty = elemty;

    try {
      //  Prompt for the rest of the event data
      // "Enter activation for first target (0 to 90) at ("
      
      
      System.out.println(qvalstr + tx + "," + ty + "): ");
    //    tvalstr=System.in.readLine();  // no spaces    
      try { 
	tvalstr = ins.readLine();  // no spaces    
	tval = Double.valueOf(tvalstr).doubleValue();
      } catch (NumberFormatException e) {
	System.err.println("Input tstart/tstop not a double value. Return null");
	return null;
      }
      
      tval = dminmax( tval, 0, MaxCorticalFiring );
      
      //"Enter start/stop times (in msec, separate with space): ");
      System.out.println(qstartstr);
      tstartstr=ins.readLine();  // no spaces
      if (tstartstr== null) {
	System.err.println("Error: no start and stop time specified . Return null");
	return(null);
      }
      
      StringTokenizer t = new StringTokenizer(tstartstr," ");
      try {
	tstart = Double.valueOf(t.nextToken()).doubleValue()
	  / 1000.0;  //Convert to seconds
	tstop = Double.valueOf(t.nextToken()).doubleValue()
	  / 1000.0;  //Convert to seconds
      } catch (NumberFormatException e) {
	System.err.println("Input tstart/tstop not a double value. Return null");
	return null;
      }
      
      
    } catch (java.io.IOException e) {
      System.err.println("IO error for input stream. Return null");
      return null;
    }
    
    tstart = dmax(tstart, 0 );
    tstop  = dmax(tstop, tstart);

    cur = new Illumination( tx, ty, tval, tstart, tstop );
    return cur;

}

//  This function creates a fixation event and places it at the front of the
//  calling object.
static public Illumination queryForFixationIllumination() {
  int tx;
  int ty;
  double tstart;
  double tval;
  double tstop;
  Illumination temp;
  String tstopstr;
  DataInputStream ins = new DataInputStream(System.in);

  tx = 4; ty = 4; tstart = 0.0;


  try {

    System.out.println();
    System.out.println("Creating a fixation event");
    System.out.println("Enter fixation stop time (in msec, 0 for no fixation): ");
    tstopstr=ins.readLine();  // no spaces
    if (tstopstr== null) {
      System.out.println("Error: no Stop time specified ");
      return null;
    }

    //    tstop=Format.atof(tstopstr);
    try {
      tstop = Double.valueOf(tstopstr).doubleValue() 
	/ 1000.0;	//Convert to seconds
    } catch (NumberFormatException e) {
      System.err.println("Input tstop not a double value. Return null");
      return null;
    }
    tstop  = dmax(tstop, tstart);

    // [warning], may have some rounding error
    if ( tstop == 0 ) {
//      No fixation.  activation is 0, also.
        tval = 0;
    }
    else {
      System.out.println("Enter activation level for fixation (0 to 90): ");
      String tvalstr = ins.readLine();  // no spaces

      try {
	tval = Double.valueOf(tvalstr).doubleValue();	
      } catch (NumberFormatException e) {
	System.err.println("Input val not a double value. Return null");
	return null;
      }

      tval = dminmax( tval, 0, MaxCorticalFiring );
    }

    } catch (java.io.IOException e) {
      System.err.println("IO error for input stream. Return null");
      return null;
    }
    


    temp = new Illumination( tx, ty, tval, tstart, tstop );
    return temp;
}
//  Prompt for the event data --------------------------------------------
static public Illumination queryForIllumination() {
  int tx;
  int ty;
  double tstart;
  double tval;
  double tstop;
  Illumination temp;
  String txystr;
  DataInputStream ins = new DataInputStream(System.in);
  try {

//  Prompt for the event data
    System.out.println();
    System.out.println("Creating a visual event"); 

    // "Enter activation for first target (0 to 90) at ("
    System.out.println("Enter x,y location of target (separate with space): ");

    txystr=ins.readLine();  // no spaces
    if (txystr== null) {
      System.out.println("Error: no X and Y specified . Return null");
      return null;
    }
    StringTokenizer t = new StringTokenizer(txystr," ");
    try {
      tx = Integer.valueOf(t.nextToken()).intValue();
      ty = Integer.valueOf(t.nextToken()).intValue();
    } catch (NumberFormatException e) {
      System.err.println("Input tx, ty not a int value. Return null");
      return null;
    }
    tx = minmax( tx, 0, CorticalArraySize-1 );
    ty = minmax( ty, 0, CorticalArraySize-1 );
    } catch (java.io.IOException e) {
      System.err.println("IO error for input stream. Return null");
      return null;
    }

    temp=queryForValStartStop(tx,ty,"Enter activation level for fixation (0 to 90): ","Enter start/stop times (in msec, separate with space): ");
    return temp;
}

static  double dminmax( double double1, double sdouble, double ldouble )
  {
    //  Ensures double1 is between sdouble and ldouble
    
    if ( double1 < sdouble ) return sdouble;
    if ( double1 > ldouble ) return ldouble;    
    return double1;
  }
  static int minmax( int int1, int sint, int lint )
  {
    //  Ensures int1 is between sint and lint
    
    if ( int1 < sint ) return sint;
    if ( int1 > lint ) return lint;    
    return int1;
  }


  static double dmax( double double1, double double2 )
  {
    //  Determines which double is the maximum value and returns it
    if ( double1 > double2 )
      return double1;
    return double2;
}
 

} // end of Illumination class
verbatim_off;
E 1
