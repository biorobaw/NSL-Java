/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart_oval - A class representing graphic objects which have an oval
 * shape.
 *
 * @author Weifang Xie, Nitin Gupta
 * @version %I%, %G%
 * @param c       color of this graphic object
 * @param x0      x-coordinate of the starting corner of the smallest
 * surrounding rectangle border of this oval
 * @param y0      y-coordinate of the starting corner of the smallest
 * surrounding rectangle border of this oval
 * @param x1      x-coordinate of the ending corner of the smallest
 * surrounding rectangle border of this oval
 * @param y1      y-coordinate of the ending corner of the smallest
 * surrounding rectangle border of this oval
 * @since JDK8
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;


@SuppressWarnings("Duplicates")
public class GraphicPart_oval extends GraphicPart {
    Color c;
    int x0, y0, x1, y1;

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart_oval() {
    }

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     */
    GraphicPart_oval(int xx0, int yy0, Color cc) {
        x0 = x1 = xmin = xmax = xx0;
        y0 = y1 = ymin = ymax = yy0;
        c = cc;
        select = 5;
    }

    /**
     * Set the color of this line object to be cc.
     */
    public void setcolor(Color cc) {
        c = cc;
    }

    /**
     * Paint this oval object with offset for schematic.
     */
    public void paint(Graphics g, int xOffset, int yOffset) {
        int xS0 = x0 + xOffset;
        int xS1 = x1 + xOffset;
        int yS0 = y0 + yOffset;
        int yS1 = y1 + yOffset;

        //int xS0 = x0;
        //int xS1 = x1;
        //int yS0 = y0;
        //int yS1 = y1;

        int x = (xS1 > xS0) ? xS0 : xS1;
        int y = (yS1 > yS0) ? yS0 : yS1;
        int w = (xS1 > xS0) ? xS1 - xS0 : xS0 - xS1;
        int h = (yS1 > yS0) ? yS1 - yS0 : yS0 - yS1;

        g.setColor(c);
        g.fillOval(x, y, w, h);
        g.setColor(Color.black);
        g.drawOval(x, y, w, h);
        if (select != 0) {
            g.setColor(Color.red);
            g.fillRect(x - 2, y - 2, 4, 4);
            g.fillRect(x + w - 2, y - 2, 4, 4);
            g.fillRect(x - 2, y + h - 2, 4, 4);
            g.fillRect(x + w - 2, y + h - 2, 4, 4);
        }
    }


    /**
     * Paint this oval object on Icon canvas
     */
    public void paint(Graphics g) {
        paint(g, 0, 0);
    }

    /**
     * Move this oval object as a whole by x offset in x-direction and y offset
     * in y-direction.
     */
    public void moveobj(int x, int y) {
        //System.out.println("Debug:GraphicPart_oval:Moveobj called in oval..");
        x1 += x;
        x0 += x;
        y1 += y;
        y0 += y;
        if (x0 < x1) {
            xmin = x0;
            xmax = x1;
        } else {
            xmin = x1;
            xmax = x0;
        }
        if (y0 < y1) {
            ymin = y0;
            ymax = y1;
        } else {
            ymin = y1;
            ymax = y0;
        }
    }

    /**
     * Move the selected point of this oval object by x offset in x-direction
     * and y offset in y-direction.
     */
    public void movepoint(int x, int y) {
        if (select == 5 || select == 3)
            x1 += x;
        if (select == 4 || select == 2)
            x0 += x;
        if (select == 5 || select == 4)
            y1 += y;
        if (select == 3 || select == 2)
            y0 += y;
        if (x0 < x1) {
            xmin = x0;
            xmax = x1;
        } else {
            xmin = x1;
            xmax = x0;
        }
        if (y0 < y1) {
            ymin = y0;
            ymax = y1;
        } else {
            ymin = y1;
            ymax = y0;
        }
    }

    /**
     * Select the point of this oval object which is within close distance to
     * the point of (x,y).
     *
     * @return  <code>true</code> if there exists one point among the four
     *          points of the surrounding rectangle of this oval object which is
     *		within close distance to the point of (x,y)
     *          <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y) {
        if (x - x0 < 3 && x - x0 > -3 && y - y0 < 3 && y - y0 > -3) {
            select = 2;
            return (true);
        }
        if (x - x1 < 3 && x - x1 > -3 && y - y0 < 3 && y - y0 > -3) {
            select = 3;
            return (true);
        }
        if (x - x0 < 3 && x - x0 > -3 && y - y1 < 3 && y - y1 > -3) {
            select = 4;
            return (true);
        } else if (x - x1 < 3 && x - x1 > -3 && y - y1 < 3 && y - y1 > -3) {
            select = 5;
            return (true);
        }
        return (false);
    }

    /**
     * Select this oval object as a whole if the point (x,y) is within close
     * distance to this object.
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this oval object
     *          <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y) {
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }

    /**
     * Select this oval object as a whole if the point (x,y) is within close
     * distance to this object.
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this oval object
     *          <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset) {
        int tempxmin = xmin + xOffset;
        int tempxmax = xmax + xOffset;
        int tempymin = ymin + yOffset;
        int tempymax = ymax + yOffset;
        if (x >= tempxmin && x <= tempxmax && y >= tempymin && y <= tempymax) {
            select = 1;
            return (true);
        }
        return (false);
    }
//------------------------------------------------------

    /**
     * Write this oval object to the output stream os.f
     *
     * @param   os      the output stream to be written to
     * @param   x       the x-coordinate of the reference point
     * @param   y       the y-coordinate of the reference point
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException {
        try {
            super.write(os, x, y);
            os.writeObject(c);
            os.writeInt(x0 - x);
            os.writeInt(y0 - y);
            os.writeInt(x1 - x);
            os.writeInt(y1 - y);
        } catch (IOException e) {
            System.err.println("GraphicPart_oval write IOException");
            throw new IOException("GraphicPart_oval write IOException");
        }
    }
//-------------------------------------------------------------

    /**
     * WriteAllChars this oval object to the output stream os.f
     *
     * @param   pw      the output stream to be written to
     * @param   x       the x-coordinate of the reference point
     * @param   y       the y-coordinate of the reference point
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        pw.print("oval: ");
        super.writeAllChars(pw, x, y);
        //os.writeObject(c);
        pw.print("x0 y0 x1 y1 :");
        pw.print((Integer.valueOf(x0 - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(y0 - y)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(x1 - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(y1 - y)).toString());
        pw.print("\n");
    }

//------------------------------------------------------

    /**
     * Read this oval object from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *                                          occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        try {
            super.read(os);
        } catch (IOException e) {
            System.err.println("GraphicPart_oval super read IOException");
            throw new IOException("GraphicPart_oval super read IOExcception");
        }

        try {
            c = (Color) os.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Error:GraphicPart_oval readColor ClassNotFoundException");
            throw new ClassNotFoundException("Error:GraphicPart_oval readColor ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_oval: " + e.toString());
            System.err.println("Error:GraphicPart_oval readColor IOException");
            throw new IOException("GraphicPart_oval readColor IOExcception");
        }

        try {
            x0 = os.readInt();
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_oval x0 readInts IOException");
            throw new IOException("GraphicPart_oval x0 readInts IOExcception");
        }
        try {
            y0 = os.readInt();
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_oval y0 readInts IOException");
            throw new IOException("GraphicPart_oval y0 readInts IOExcception");
        }
        try {
            x1 = os.readInt();
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_oval x1 readInts IOException");
            throw new IOException("GraphicPart_oval x1 readInts IOExcception");
        }
        try {
            y1 = os.readInt();
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_oval y1 readInts IOException");
            throw new IOException("GraphicPart_oval y1 readInts IOExcception");
        }
/*
	try {
	    super.read(os);
            c=(Color)os.readObject();
            x0=os.readInt();
            y0=os.readInt();
            x1=os.readInt();
            y1=os.readInt();
	}
	catch (ClassNotFoundException e) {
	    System.err.println("Error:GraphicPart_oval read ClassNotFoundException");
	    throw new ClassNotFoundException("GraphicPart_oval read ClassNotFoundException");
	}
	catch (IOException e) {
	    System.err.println("Error:GraphicPart_oval read IOException");
	    throw new IOException("GraphicPart_oval read IOExcception");
	}
*/
    }
}


