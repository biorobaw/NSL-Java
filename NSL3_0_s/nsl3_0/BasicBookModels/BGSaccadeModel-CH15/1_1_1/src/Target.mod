/* SCCS  @(#)Target.mod	1.1---09/24/99--18:57:31 */
/* old kversion @(#)Target.mod	1.8 --- 08/05/99 -- 13:56:43 */

verbatim_NSLJ;

import nslj.src.lang.*;


public class Target
{
// This class provides for a linked list of target objects that all have the
// size of a single array element.  The contents of this class are the
// x,y coordinates of the corner closest to array element 0,0, and a
// pointer to the next Target in the list.  The x-coordinate is the
// first sort.
 
    private double  xcor, ycor;
    private Target  next;

Target(String name) { this();} 
 
  Target( ) { 
    xcor = 0; ycor = 0; next = null; 
  }
  Target( int x, int y ) {
    xcor = (double)x;
    ycor = (double)y;
    next = null; 
  }

  Target( int x, int y, Target list ){
    xcor = (double)x;
    ycor = (double)y;
    next = list;
  }
  void Move( NslDouble1 invec ){
// This method applies the input movement vector to all of the Targets in
// the linked list.  The x,y-coordinates of each Target have the input
// movement vector subtracted from their corner coordinates as the motion
// of the Targets across the visual space is in the opposite direction to
// the movement of the eyes.
    Target cur;

 //  Do the first target as it always exists
 
    xcor = xcor - invec.get(0);
    ycor = ycor - invec.get(1);
 
    cur = next;  //get pointer to next Target
 
//  The do while will "move" the second and higher Targets if they exist
    while ( cur != null )      {
        cur.xcor = cur.xcor - invec.get(0);
        cur.ycor = cur.ycor - invec.get(1);
	cur = cur.next;
      }
  }

  void RemoveTarget( Target cur ){
// This method removes the cur Target from the list of Targets
 
    Target tempcur, prev;
    
    tempcur = next;
    prev    = null;
    
    if ( cur.next == next )  {
      // Remove first target from list by copying values from second target to
      // first target and deleting the memory for the second target.
      
      //      Check for NULL pointers
      /* just use gc       
	 if ( tempcur == null ) {delete cur;return;}
	 */
      if(next == null) {
	xcor = -1;
	ycor = -1;
	next = null;
      }
      //      Otherwise set the pointers to the new first element in the list 
      xcor = tempcur.xcor;
      ycor = tempcur.ycor;
      next = tempcur.next;
      //        delete tempcur;
      return;
    }
    
    //      Target to be removed is not the first Target.  Need to find the
    //      Target previous to the one to be removed
    
    while ( tempcur != cur ){
      prev    = tempcur;
      tempcur = tempcur.next;
    } 
    
    if ( prev == null )  {  // cur is second in list
        next = cur.next;
        //delete cur;
      }
    else  {
        prev.next = cur.next;
        //delete cur;
      }
  }

  //        Target *GetTarget( void );
 
  double X() {return xcor;}
  double Y() {return ycor;}
  Target Next() {return next;}
 
} 
verbatim_off;
