/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart - A base class representing graphic objects which is inherited by
 * classes of special shaped graphic objects.
 *
 * @author Weifang Xie
 * @version %I%, %G%
 * @param select    a flag indicating whether this graphic object is
 * selected and highlighted. different meaning with
 * different value:
 * 0  -- not selected
 * 1  -- selected as a whole
 * >1 -- vertices selected, the first vertex having value
 * 2, and so on ...
 * @param xmin    the x-coordinate of the left-up corner of the smallest
 * surrounding rectangle border of this graphic object
 * @param ymin    the y-coordinate of the left-up corner of the smallest
 * surrounding rectangle border of this graphic object
 * @param xmax    the x-coordinate of the right-down corner of the
 * smallest surrounding rectangle border of this graphic
 * object
 * @param ymax    the y-coordinate of the right-down corner of the
 * smallest surrounding rectangle border of this graphic
 * object
 * @since JDK8
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;


@SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
public class GraphicPart extends Component {
    int select;
    protected int xmin, ymin, xmax, ymax;

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart() {
    }

    /**
     * An overridable method which checks whether this graphic object is finished     * building or not. 
     */
    public boolean done() {
        return (true);
    }

    /**
     * Set the color of this graphic object to be c.
     */
    public void setcolor(Color c) {
    }

    /**
     * Paint this graphic object.
     */
    public void paintComponent(Graphics g)  //use to be called paint
    {
    }

    /**
     * Paint this graphic object but offset
     */
    public void paintComponent(Graphics g, int xmin, int ymin) {
    }

    /**
     * Move this graphic object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y) {
    }

    /**
     * Move the selected point of this graphic object by x offset in x-direction
     * and y offset in y-direction.
     */
    public void movepoint(int x, int y) {
    }

    /**
     * Select the point of this graphic object which is within close distance to
     * the point of (x,y).
     *
     * @return  <code>true</code> if there exists one point on this graphic 
     *          object which is within close distance to the point of (x,y) 
     *          <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y) {
        return (false);
    }

    /**
     * Select this graphic object as a whole if the point (x,y) is within close
     * distance to this object given the offsets. 
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this graphic object
     *          <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset) {
        // todo: put in error message here, I do not think this should ever
        // be called
        return (false);
    }

    /**
     * Select this graphic object as a whole if the point (x,y) is within close
     * distance to this object. 
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this graphic object
     *          <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y) {
        // todo: put in error message here, I do not think this should ever
        // BE CALLED
        return (false);
    }

    /**
     * An overidable method which adds a point (x,y) to the outline of this 
     * graphic object.
     */
    public void addpoint(int x, int y) {
    }

    /**
     * Make this graphic object unselected.
     */
    public void unselect() {
        select = 0;
    }
//------------------------------------------------------------------

    /**
     * Write this graphic object to the output stream os.
     *
     * @param    os    the output stream to be written to
     * @param    x    the x-coordinate of the reference point
     * @param    y    the y-coordinate of the reference point
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException {
        try {
            os.writeInt(xmin - x);
            os.writeInt(ymin - y);
            os.writeInt(xmax - x);
            os.writeInt(ymax - y);
        } catch (IOException e) {
            System.err.println("Error:GraphicPart write IOException");
            throw new IOException("Error:GraphicPart write IOException");
        }
    }

//------------------------------------------------------------------

    /**
     * WriteAllChars this graphic object to the output stream os.
     *
     * @param    pw    the output stream to be written to
     * @param    x    the x-coordinate of the reference point
     * @param    y    the y-coordinate of the reference point
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        pw.print("xmin ymin xmax ymax: ");
        pw.print((Integer.valueOf(xmin - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(ymin - y)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(xmax - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(ymax - y)).toString());
        pw.print("\n");
    }
//------------------------------------------------------------------

    /**
     * Read this graphic object from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *                                          occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        try {
            xmin = os.readInt();
            ymin = os.readInt();
            xmax = os.readInt();
            ymax = os.readInt();
        } catch (IOException e) {
            System.err.println("Error:GraphicPart read IOException");
            throw new IOException("GraphicPart read IOException");
        }

        select = 0;
    }
} //end class GraphicPart






