/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart_rect - A class representing graphic objects which have a rectangle
 * shape.
 *
 * @author Xie, Gupta
 * @version %I%, %G%
 * @param c       color of this graphic object
 * @param x0      x-coordinate of the starting corner of this rectangle
 * object
 * @param y0      y-coordinate of the starting corner of this rectangle
 * object
 * @param x1      x-coordinate of the ending corner of this rectangle
 * object
 * @param y1      y-coordinate of the ending corner of this rectangle
 * object
 * @since JDK1.1
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;


@SuppressWarnings("Duplicates")
public class GraphicPart_rect extends GraphicPart {
    Color c;
    int x0, y0, x1, y1;

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart_rect() {
    }
    //------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     */
    GraphicPart_rect(int xx0, int yy0, Color cc) {
        x0 = x1 = xmin = xmax = xx0;
        y0 = y1 = ymin = ymax = yy0;
        c = cc;
        select = 5;
    }
    //------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 to be
     * xx0, the initial value of y0 to be yy0, and the color c to be cc.
     */
    GraphicPart_rect(int xx0, int yy0, int xx1, int yy1, Color cc) {
        x0 = xx0;
        y0 = yy0;
        x1 = xx1;
        y1 = yy1;
        c = cc;
        select = 5;
    }
    //------------------------------------------------------

    /**
     * Set the color of this rectangle object to be cc.
     */
    public void setcolor(Color cc) {
        c = cc;
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object with offset
     */
    public void paint(Graphics g, int xOffset, int yOffset) {

        //System.out.println("Debug:GraphicPart_rect:paint(g,x,y): "+xOffset+" "+yOffset);
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
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        g.drawRect(x, y, w, h);
        if (select != 0) //draw handles
        {
            g.setColor(Color.red);
            g.fillRect(x - 2, y - 2, 4, 4);
            g.fillRect(x + w - 2, y - 2, 4, 4);
            g.fillRect(x - 2, y + h - 2, 4, 4);
            g.fillRect(x + w - 2, y + h - 2, 4, 4);
        }
    }

    //------------------------------------------------------

    /**
     * Paint this rectangle object.
     */
    public void paint(Graphics g) {
        //System.out.println("Debug:GraphicPart_rect:paint(g)");
        paint(g, 0, 0);
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object with offset
     */
    public void paintOpen(Graphics g, int xOffset, int yOffset) {
        //System.out.println("Debug:GraphicPart_rect:paintOpen(g,x,y): "+xOffset+" "+yOffset);
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
//	g.fillRect(x,y,w,h);
//	g.setColor(Color.black);
        g.drawRect(x, y, w, h);
        if (select != 0) //draw handles
        {
            g.setColor(Color.red);
            g.fillRect(x - 2, y - 2, 4, 4);
            g.fillRect(x + w - 2, y - 2, 4, 4);
            g.fillRect(x - 2, y + h - 2, 4, 4);
            g.fillRect(x + w - 2, y + h - 2, 4, 4);
        }
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object.
     */
    public void paintOpen(Graphics g) {
        //System.out.println("Debug:GraphicPart_rect:paint(g)");
        paintOpen(g, 0, 0);
    }
    //------------------------------------------------------


    /**
     * Move this rectangle object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y) {
        //	System.out.println("Debug:GraphicPart_rect:moveobj");
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
    //------------------------------------------------------

    /**
     * Move the selected point of this rectangle object by x offset in 
     * x-direction and y offset in y-direction.
     */
    public void movepoint(int x, int y) {
        //	System.out.println("Debug:GraphicPart_rect:movepoint:select: "+select);
        //	System.out.println("Debug:GraphicPart_rect:movepoint:x y: "+x+" "+y);

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
        //	System.out.println("Debug:GraphicPart_rect:movepoint:xmin ymin: "+xmin+" "+ymin);
        //	System.out.println("Debug:GraphicPart_rect:movepoint:xmax ymax: "+xmax+" "+ymax);
    }
    //------------------------------------------------------

    /**
     * Select the point of this oval object which is within close distance to
     * the point of (x,y).
     *
     * @return  <code>true</code> if there exists one point among the four
     *          vertices of this rectangle object which is within close distance
     *          to the point of (x,y)
     *          <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y) {
        //	System.out.println("Debug:GraphicPart_rect:selectpoint:select: "+select);
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
    //------------------------------------------------------

    /**
     * Select this rectangle object as a whole if the point (x,y) is within 
     * close distance to this object.
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this polygon object
     *          <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y) {
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }
    //------------------------------------------------------

    /**
     * Select this rectangle object as a whole if the point (x,y) is within 
     * close distance to this object.
     *
     * @return  <code>true</code> if the point (x,y) is within close distance
     *          to this polygon object
     *          <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset) {
        int tempxmin = xmin + xOffset;
        int tempymin = ymin + yOffset;
        int tempxmax = xmax + xOffset;
        int tempymax = ymax + yOffset;
        if (x >= tempxmin && x <= tempxmax && y >= tempymin && y <= tempymax) {
            select = 1;
            return (true);
        }
        return (false);
    }

    //------------------------------------------------------

    /**
     * Write this rectangle object to the output stream os.
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
            System.err.println("Error:GraphicPart_rect: write IOException.");
            throw new IOException("GraphicPart_rect write IOException");
        }
    }


    //------------------------------------------------------

    /**
     * WriteAllChars this rectangle object to the output stream os.
     *
     * @param   pw      the output stream to be written to
     * @param   x       the x-coordinate of the reference point
     * @param   y       the y-coordinate of the reference point
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        pw.print("rect: ");
        super.writeAllChars(pw, x, y);
        //os.writeObject(c);
        pw.print("x0 y0 x1 y1: ");
        pw.print((Integer.valueOf(x0 - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(y0 - y)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(x1 - x)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(y1 - y)).toString());
        pw.print("\n");

    }
//-------------------------------------------------------------------------------

    /**
     * Read this rectangle object from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *                                          occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        try {
            super.read(os);
            c = (Color) os.readObject();
            x0 = os.readInt();
            y0 = os.readInt();
            x1 = os.readInt();
            y1 = os.readInt();
        } catch (ClassNotFoundException e) {
            //System.out.println("GraphicPart_rect read ClassNotFoundException");
            throw new ClassNotFoundException("GraphicPart_rect read ClassNotFoundException");
        } catch (IOException e) {
            //System.out.println("GraphicPart_rect read IOException");
            throw new IOException("GraphicPart_rect read IOException");
        }
    }
}








