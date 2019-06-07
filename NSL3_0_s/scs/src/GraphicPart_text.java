/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * GraphicPart_text - A class representing text included in the icon or
 * schematic
 *
 * @author nitgupta
 * @version %I%, %G%
 * @param x0      x-coordinate of the starting corner of the smallest
 * surrounding rectangle border of this text string
 * @param y0      y-coordinate of the starting corner of the smallest
 * surrounding rectangle border of this text string
 * @param x1      x-coordinate of the ending corner of the smallest
 * surrounding rectangle border of this oval
 * @param y1      y-coordinate of the ending corner of the smallest
 * surrounding rectangle border of this oval
 * @param size    size of the text
 * @param textString   text of the string
 * @param c       color of this graphic object
 * @param font    font used for the text
 * @since JDK8
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.FontMetrics;

@SuppressWarnings("Duplicates")
public class GraphicPart_text extends GraphicPart {
    int x0, y0, x1, y1;
    String textString;
    int size;
    Color c = OptionsFrame.freeText_col; //this could be module or instance though
    Font font = OptionsFrame.freeTextFont;

//------------------------------------------------------------------------

    /**
     * Constructor of this class with no parameters.
     */

    GraphicPart_text() {
    }
//---------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     * font to p_font , size to p_size
     * Note: the text location starts in the bottom left corner - not the upper left
     * so this function makes it so the two points coming in are assumed
     * to be xmin and ymin.
     **/

    GraphicPart_text(int px0, int py0, String p_text, Font p_font, Color p_color, int p_size) {
        initText(px0, py0, p_text, p_font, p_color, p_size);
    }

//------------------------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc,
     * font to p_font , size to p_size .
     * Note: this constructor will center the text in the rectangle you give it
     * if location is "CENTER",etc for
     * RIGHT,LEFT,ABOVE,BELOW .
     */

    GraphicPart_text(String location, int pxmin, int pymin, int pxmax, int pymax, String p_text, Font p_font, Color p_color, int p_size) {
        initText(location, pxmin, pymin, pxmax, pymax, p_text, p_font, p_color, p_size);
    }
//------------------------------------------------------------------------

    /**
     * Initialization of this class, setting the value of x0 and x1 to be
     * a function of the input based on location,
     * the value of y0 and y1 to be a function of the input based on location
     * , and the color c to be cc,
     * font to p_font , size to p_size .
     * Note: this constructor will center the text in the rectangle you give it
     * if location is "CENTER",etc for
     * RIGHT,LEFT,ABOVE,BELOW .
     * note: this method is also called repeatedly for the module label.
     */

    public void initText(String location, int pxmin, int pymin, int pxmax, int pymax, String p_text, Font p_font, Color p_color, int p_size) {
        FontMetrics fm = getFontMetrics(p_font);
        int locationX = pxmin + Math.round((pxmax - pxmin) / 2); //center of box
        int locationY = pymin + Math.round((pymax - pymin) / 2); //center of box


        //      System.out.println("Debug:GraphicPart_text:pxmin " + pxmin + " pymin "  + pymin );
        //      System.out.println("Debug:GraphicPart_text:pxmax "  +pxmax + " pymax "  + pymax );

        if ((p_text == null) || (p_text.equals(""))) {
            System.err.println("Error:GraphicPart_text:initText:string is null!");
            //todo: throw an exception
            return;
        }
        int w = fm.stringWidth(p_text);
        int h = fm.getHeight();
        c = p_color;
        size = p_size;
        font = p_font;
        textString = p_text;

        int wD2 = Math.round(fm.stringWidth(p_text) / 2);
        int hD2 = Math.round(fm.getHeight() / 2);

        // where is the center of the text going
        switch (location) {
            case "CENTER":
                //do nothing
                break;
            case "ABOVE":
                locationY = pymin - hD2 - SCSUtility.gridD2;
                break;
            case "BELOW":
                locationY = pymax + hD2 + SCSUtility.gridD2;
                break;
            case "LEFT":
                locationX = pxmin - wD2 - SCSUtility.gridD2;
                break;
            case "RIGHT":
                locationX = pxmax + wD2 + SCSUtility.gridD2;
                break;
            default:
                System.err.println("Error:GraphicPart_text:initText: incorrect justification given: " + location + ".");
                System.err.println("Error:GraphicPart_text:initText: using CENTER.");
                break;
        }

        //-----
        //locationX and locationY are the center of the text string
        // and  x0 and y0 are the upper left corner of the text string.
        x0 = xmin = locationX - wD2;  //upper left
        x1 = xmax = locationX + wD2;
        ymin = y0 = locationY - hD2;  //upper left
        ymax = y1 = locationY + hD2;

        //      System.out.println("Debug:GraphicPart_text:x0 " + x0 + " y0 "  + y0 );
        //      System.out.println("Debug:GraphicPart_text:x1 "  +x1 + " y1 "  + y1 );

    }

//---------------------------------------------------

    /**
     * Initializion of this class, setting the initial value of x0 to be the
     * inputX and the initial value of y0 to be the inputY minus the height
     * of the text. (Input is left justified and lower)
     * Note: the input defines a location at the lower left, and this method
     * calculate where x0, y0 should then be at the upper left.
     **/

    public void initText(int px0, int py0, String p_text, Font p_font, Color p_color, int p_size) {

        FontMetrics fm = getFontMetrics(p_font);

        c = p_color;
        size = p_size;
        font = p_font;
        textString = p_text;

        x0 = xmin = px0;
        y1 = py0;
        ymax = y1;

        x1 = xmax = x0 + fm.stringWidth(p_text);
        y0 = y1 - fm.getHeight(); //y0 is above y1
        ymin = y0;

        //System.out.println("Debug:GraphicPart_text:init:x0 " + x0 + " y0 "  + y0 );
        //System.out.println("Debug:GraphicPart_text:init:x1 "  +x1 + " y1 "  + y1 );
    }
    //-----------------------------------------------------


    /**
     * Set the color of this text object to be cc.
     */
    public void setcolor(Color cc) {
        c = cc;
    }


    /**
     * Set the font of this text object to be p_font.
     */

    public void setfont(Font p_font) {

        font = p_font;
    }

    public void settext(String p_text) {

        textString = p_text;
    }


    /**
     * Set the size of this text object to be p_size
     */

    public void reSize(int p_size) {

        size = p_size;
    }

    /**
     * Paint this  text object with offset.
     */
    public void paint(Graphics g, int xOffset, int yOffset) {
        int xS0 = x0 + xOffset;
        int xS1 = x1 + xOffset;
        int yS0 = y0 + yOffset;
        int yS1 = y1 + yOffset;


        g.setColor(c);
        g.setFont(font);

        if (yS1 == 0)
            g.drawString(textString, xS0, yS0);
        else
            g.drawString(textString, xS0, yS1);

        if (select == 1) {
            g.setColor(Color.red);
            g.drawRect(xS0, yS0, xS1 - xS0, yS1 - yS0);
            g.setColor(c);
        }
    }


    /**
     * Paint this  text object with no offset.
     */
    public void paint(Graphics g) {
        paint(g, 0, 0);
    }

    /**
     * Move this text object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y) {

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
     * Move the selected point of this line object by x offset in x-direction
     * and y offset in y-direction.
     */
    public void movepoint(int x, int y) {
        if (select == 1) {
            x0 += x;
            y0 += y;
        }
// 	if(select==3)
// 	{
// 	    x1 += x;
// 	    y1 += y;
// 	}
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
     * Select the point of this line object which is within close distance to
     * the point of (x,y).
     *
     * @return <code>true</code> if there exists one point on this line
     * object which is within close distance to the point of (x,y)
     * <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y) {
        if (x - x0 < 3 && x - x0 > -3 && y - y0 < 3 && y - y0 > -3) {
            select = 2;
            return (true);
        } else if (x - x1 < 3 && x - x1 > -3 && y - y1 < 3 && y - y1 > -3) {
            select = 3;
            return (true);
        }
        return (false);
    }

    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset) {
        //System.out.println("Debug:GraphicPart_text:selectobjWOffset:x" + x + "y: " + y);
        double k, dist, x2, y2;

        int tempx0 = x0 + xOffset;
        int tempx1 = x1 + xOffset;
        int tempy0 = y0 + xOffset;
        int tempy1 = y1 + xOffset;

        if (x - tempx0 > 0 && x - tempx1 < 0 && y - tempy0 > 0 && y - tempy1 < 0) {
            select = 1;
            //System.out.println("Debug:GraphicPart_text:selected..");
            return (true);
        }
        return (false);
    }
    //----------------------------------------------

    public boolean selectobj(int x, int y) {
        //System.out.println("Debug:GraphicPart_text:selectobj:x" + x + "y: " + y);

        double k, dist, x2, y2;

        if (x - x0 > 0 && x - x1 < 0 && y - y0 > 0 && y - y1 < 0) {
            select = 1;
            //System.out.println("Debug:GraphicPart_text:selected..");
            return (true);
        }
        return (false);
    }

//---------------------------------------------------------

    /**
     * Write this line object to the output stream os.
     *
     * @param os the output stream to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     * @throws IOException if an IO error occurred
     */
    // Removed writing and reading of color and font
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException {


        //System.out.println("Debug:GraphicPart_text:Write text called..: x0 " + x0 + "y0:" + y0 );
        try {

            super.write(os, x, y);
            //	  os.writeObject(c);
//	  os.writeObject(font);
            //todo: this should take the input from the options
            //	  os.writeObject(new Font("Monospaced", Font.PLAIN , 12));
            os.writeObject(textString);
            os.writeInt(x0 - x);
            os.writeInt(y0 - y);
            os.writeInt(x1 - x);
            os.writeInt(y1 - y);

        } catch (IOException e) {
            System.err.println("Error:GraphicPart_text write IOException");
            throw new IOException("GraphicPart_text  write IOException");
        }
    }

//---------------------------------------------------------

    /**
     * WriteAllChars this line object to the output stream os.
     *
     * @param pw the output stream to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     * @throws IOException if an IO error occurred
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        //System.out.println("Debug:GraphicPart_text:Write text called..: x0 " + x0 + "y0:" + y0 );

        pw.print("Text: ");
        super.writeAllChars(pw, x, y);
//	  os.writeObject(c);
//	  os.writeObject(new Font("Monospaced", Font.PLAIN , 12));
        pw.print(textString);
        pw.print("x0 y0 x1 y1: ");
        pw.print((new Integer(x0 - x)).toString());
        pw.print(" ");
        pw.print((new Integer(y0 - y)).toString());
        pw.print(" ");
        pw.print((new Integer(x1 - x)).toString());
        pw.print(" ");
        pw.print((new Integer(y1 - y)).toString());
        pw.print("\n ");

    }

//---------------------------------------------------------

    /**
     * Read this line object from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {

        //System.out.println("Debug:GraphicPart_text:read text called..");
        try {

            super.read(os);
            //System.out.println("Debug:GraphicPart_text:read super called");
            //	    c=(Color)os.readObject();
            //System.out.println("Debug:GraphicPart_text:read object called ..");
            //	    font = (Font)   os.readObject();
            //	System.out.println("Debug:GraphicPart_text:read Name  : " + font.getName() );
            //	System.out.println("Debug:GraphicPart_text:read Font Name  : " + font.getFontName() );
            //	System.out.println("Debug:GraphicPart_text:read Font style  : " + font.getStyle() );
            //	System.out.println("Debug:GraphicPart_text:read Font Size  : " + font.getSize() );


            textString = (String) os.readObject();
            x0 = os.readInt();
            y0 = os.readInt();
            x1 = os.readInt();
            y1 = os.readInt();

            //      System.out.println("Debug:GraphicPart_text:read read text called..: x0 " + x0 + "y0:" + y0 );
        } catch (ClassNotFoundException e) {
            System.err.println("Error:GraphicPart_text read ClassNotFoundException");
            throw new ClassNotFoundException("GraphicPart_line read ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:GraphicPart_text  read IOException");
            throw new IOException("GraphicPart_text  read IOException");
        }
    }
} //end class GraphicPart_text

