/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconInport - A class representing the icon inports of a module's icon.
 * This is an extended class from GraphicPart_line, inherited all those graphical
 * attributes, plus some additional attributes special for an icon inport.
 *
 * @author Weifang Xie
 * @version 1.1, 03/15/99
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;


@SuppressWarnings("Duplicates")
class IconInport extends GraphicPart_line {
    String Name = "";
    String Type = "";
    String parameters = "";
    char direction = 'L';
    char signalType = 'E';  // excitatory 'E' vs. inhibitory 'I'
    // output = 'O'
    Connection link = null;


    static public Color port_col = Color.white;  //changes in OptionsFrame
    static public String input_port_shape = "default";
    // 99/4/27 move pinLength to Icon.java

    /**
     * Constructor of this class with no parameters.
     */
    public IconInport() {
        //link=new Connection();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon inports, and color being set to n, t, p, xx0, yy0, and cc.
     *
     * @param n            the name of this icon inport
     * @param t            the type of this icon inport, either Java native data
     *                     type, Nsl data type, or NslEnv type
     * @param params       the parameters of the corresponding variable
     *                     representation of this icon inport
     * @param p_dir        direction of the icon port
     * @param p_signalType excitatory or inhibitory. E or I
     * @author Weifang Xie
     * @version 1.1, 03/15/99
     * @since JDK8
     */

    // Note: xx1 and yy1 now represent the point with the arrow */
    public IconInport(String n, String t, String params, char p_dir, char p_signalType, int xx1, int yy1, Color cc) {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.

        super(xx1, yy1, cc);
        Name = n;
        Type = t;
        parameters = params;
        direction = p_dir;
        signalType = p_signalType;
        calcX0Y0(p_dir, xx1, yy1);
    }

    /**
     * Constructor of this class, with initial values of var of type declaration,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon inports, and color being set with cc.
     *
     * @author Alexander
     * @since JDK8
     */

    // Note: xx1 and yy1 now represent the point with the arrow */
    public IconInport(Declaration var, int xx1, int yy1, Color cc) {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.

        super(xx1, yy1, cc);
        Name = var.varName;
        Type = var.varType;
        parameters = var.varParams;
        direction = var.portIconDirection;
        signalType = var.portSignalType;

        calcX0Y0(direction, xx1, yy1);
    }

    //----------------------------------------------------------
    protected void calcX0Y0(char direction, int xx1, int yy1) {
        x1 = xx1;
        y1 = yy1;

        //System.out.println(direction);
        //System.out.println("x0 " + xx1  + "yo " + yy1 + "x1 " + x1 + "y1 " + y1 );

        if (direction == 'L') { //left to right
            x0 = xx1 - Icon.pinLength;
            y0 = yy1;
            if (x0 < 0) {
                // todo: throw exception
                System.err.println("Error: IconInport: x0 less than 0 ");
            }
            //System.out.println("x0 " + xx1  + "yo " + yy1 + "x1 " + x1 + "y1 " + y1 );
        }

        if (direction == 'T') { //top to bottom
            x0 = xx1;
            y0 = yy1 - Icon.pinLength;
            if (y0 < 0) {
                // todo: throw exception
                System.err.println("Error: IconInport: y0 less than 0 ");
            }

            //System.out.println("x0 " + xx1  + "yo " + yy1 + "x1 " + x1 + "y1 " + y1 );
        }

        if (direction == 'B') { //bottom to top
            x0 = xx1;
            y0 = yy1 + Icon.pinLength;
            //System.out.println("x0 " + xx1  + "yo " + yy1 + "x1 " + x1 + "y1 " + y1 );
        }

        if (direction == 'R') { //right to left
            x0 = xx1 + Icon.pinLength;
            y0 = yy1;
            //System.out.println("x0 " + xx1  + "yo " + yy1 + "x1 " + x1 + "y1" + y1 );
        }
        // 99/4/27 key to moving Icon problem
        setminmax();  // call GraphicPart_line's setminmax
    }
    //----------------------------------------------------------


    public void movepoint(int x, int y) {
        moveobj(x, y);
        //  x1 = 10 ;
        //       y1 = 10 ;


    }
    //----------------------------------------------------------
    // paint the inhibitory type input port

    public void paint_inhibit(Graphics g) {


        g.setColor(port_col);
        g.drawLine(x0, y0, x1, y1);
        Polygon p = new Polygon();
        int x, y;
        double dist;

        int width = 5;

        // don't draw little rectangles
        //g.setColor(Color.red);
        //g.drawRect(x0-width , y0 - width , width*2 , width*2);
        //g.drawRect(x1-width , y1- width , width*2 , width*2);
        g.setColor(port_col);

        if (y0 == y1) {
            // 	g.drawOval(( x0+x1)/2 , y0 , 8,8);
            g.drawOval((x1 - 4), y0, 8, 8);
        } else {
            // 	g.drawOval( x0, (y0+y1)/2,8,8);
            g.drawOval(x0, (y0 + y1) / 2, 8, 8);
        }


        if (select != 0) {
            if (select != -1) {
                g.setColor(port_col);
                g.fillRect(x0 - 2, y0 - 2, 4, 4);
                g.fillRect(x1 - 2, y1 - 2, 4, 4);
            }
            //Font font=new Font("Serif",Font.ITALIC,20);
            Font font = new Font("Monospaced", Font.PLAIN, 12);
            g.setFont(font);
            FontMetrics fontmetrics = getFontMetrics(font);

            g.setColor(Color.yellow);
            g.fillRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                    fontmetrics.getHeight() + 10);
            g.setColor(Color.black);
            g.drawRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10, fontmetrics.getHeight() + 10);

            g.setColor(Color.red);
            // uncommented following line. aa:/99/4/13
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }
    //----------------------------------------------------------

    /**
     * Paint this icon inport.
     */
    public void paint(Graphics g) {

        int x, y;
        double dist;
        Polygon p = new Polygon();

        int width = 5;

        //  if ( IconInport.input_port_shape.equals("CROSS") == true )
        //        {       paint_cross(g); return ; }

        if (signalType == 'I') {
            // paint a circle at input
            paint_inhibit(g);
            return;
        }


        // 99/4/16 don't draw little rect
        // g.setColor(Color.red);
        //g.drawRect(x0-width , y0 - width , width*2 , width*2);
        //g.drawRect(x1-width , y1- width , width*2 , width*2);
        g.setColor(port_col);

        g.drawLine(x0, y0, x1, y1);

        if (x0 == x1)

            g.drawLine(x0 + 1, y0, x1 + 1, y1);


        if (y0 == y1)

            g.drawLine(x0, y0 - 1, x1, y1 - 1);


        // I think this must be for the arrow

        p.addPoint(x1, y1);

        dist = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));

        if (dist > 10) {
            x = (int) (x1 + (-10 * (x1 - x0) + 3 * (y1 - y0)) / dist);
            y = (int) (y1 + (-10 * (y1 - y0) - 3 * (x1 - x0)) / dist);
            p.addPoint(x, y);

            x = (int) (x1 + (-10 * (x1 - x0) - 3 * (y1 - y0)) / dist);
            y = (int) (y1 + (-10 * (y1 - y0) + 3 * (x1 - x0)) / dist);
            p.addPoint(x, y);
        } else {
            x = (int) (x1 + (-0.8 * (x1 - x0) + 0.24 * (y1 - y0)));
            y = (int) (y1 + (-0.8 * (y1 - y0) - 0.24 * (x1 - x0)));
            p.addPoint(x, y);

            x = (int) (x1 + (-0.8 * (x1 - x0) - 0.24 * (y1 - y0)));
            y = (int) (y1 + (-0.8 * (y1 - y0) + 0.24 * (x1 - x0)));
            p.addPoint(x, y);
        }

        g.fillPolygon(p);


        if (select != 0) {
            if (select != -1) {
                g.setColor(port_col);
                g.fillRect(x0 - 2, y0 - 2, 4, 4);
                g.fillRect(x1 - 2, y1 - 2, 4, 4);
            }

            //Font font=new Font("Serif",Font.ITALIC,20);
            Font font = new Font("Monospaced", Font.PLAIN, 12);

            g.setFont(font);
            FontMetrics fontmetrics = getFontMetrics(font);

            g.setColor(Color.yellow);
            g.fillRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                    fontmetrics.getHeight() + 10);
            g.setColor(Color.black);
            g.drawRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                    fontmetrics.getHeight() + 10);
            // uncommented aa:99/4/13
            g.setColor(Color.red);
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }

    //----------------------------------------------------------
    public void paint_vertical_line(Graphics g) {

        int x, y;
        double dist;
        Polygon p = new Polygon();

        int width = 5;

        //  if ( IconInport.input_port_shape.equals("CROSS") == true )
        //        {       paint_cross(g); return ; }

        if (signalType == 'I') {
            // paint a circle at input
            paint_inhibit(g);
            return;
        }


        // g.setColor(Color.white);
        // dont draw little rectangles
        //g.setColor(Color.red);
        //g.drawRect(x0-width , y0 - width , width*2 , width*2);
        //g.drawRect(x1-width , y1- width , width*2 , width*2);
        g.setColor(port_col);
        g.drawLine(x0, y0, x1, y1);

        // for arrow?
        if (y0 == y1) {

            g.drawLine((x0 + x1) / 2, y0 + 5, (x0 + x1) / 2, y0 - 5);
        } else {
            g.drawLine(x0 - 5, (y0 + y1) / 2, x0 + 5, (y0 + y1) / 2);
        }

        if (select != 0) {
            if (select != -1) {
                g.setColor(port_col);
                g.fillRect(x0 - 2, y0 - 2, 4, 4);
                g.fillRect(x1 - 2, y1 - 2, 4, 4);
            }

            Font font = new Font("Monospaced", Font.PLAIN, 12);
            //Font font=new Font("Serif",Font.ITALIC,20);

            g.setFont(font);
            FontMetrics fontmetrics = getFontMetrics(font);

            g.setColor(Color.yellow);
            g.fillRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                    fontmetrics.getHeight() + 10);
            g.setColor(Color.black);
            g.drawRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                    fontmetrics.getHeight() + 10);
            g.setColor(Color.red);
            //uncommented following line aa:99/4/13
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }


    //----------------------------------------------------------

    /**
     * Connect this icon inport to a connection "conn".
     */
    public void connect(Connection conn) {
        link = conn;
    }
    //----------------------------------------------------------

    /**
     * Disconnect this icon inport from its connected connection.
     */
    public void disconnect() {
        link = null;
    }
    //-----------------------------------------------------

    /**
     * Write this icon inport to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException {
        try {
            super.write(os, x, y);
            os.writeUTF(Name);
            os.writeUTF(Type);
            os.writeUTF(parameters);
            os.writeChar(direction);
            os.writeChar(signalType);
            //todo: why don't we write out the link?
        } catch (IOException e) {
            System.err.println("IconInport write IOException");
            throw new IOException("IconInport write IOException");
        }
    }

    //-----------------------------------------------------

    /**
     * Write this icon inport to the output stream os.
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        pw.print("iconInport: ");
        super.writeAllChars(pw, x, y);
        pw.print("Name: ");
        pw.print(Name);
        pw.print("\n");
        pw.print("Type: ");
        pw.print(Type);
        pw.print("\n");
        pw.print("Params: ");
        pw.print(parameters);
        pw.print("\n");
        pw.print("direction: ");
        pw.print((new Character(direction)).toString());
        pw.print("\n");
        pw.print("signalType: ");
        pw.print((new Character(signalType)).toString());
        pw.print("\n");

        if (link != null) {
            pw.print("link: ");
            link.writeAllChars(pw);
            pw.print("\n");
        }
    }

    //----------------------------------------------------------------

    /**
     * Read this icon inport from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    // ,UTFDataFormatException
    {
        try {
            super.read(os);
            Name = os.readUTF();
            Type = os.readUTF();
            parameters = os.readUTF();
            direction = os.readChar();
            signalType = os.readChar();
            //todo: why don't we read in the link?
        } catch (IOException e) {
            System.err.println("IconInport read Name&Type IOException");
            throw new IOException("IconInport read Name&Type IOException");
        } catch (ClassNotFoundException e) {
            System.err.println("IconInport read Name&TypeClassNotFoundException");
            throw new ClassNotFoundException("IconInport read&Type ClassNotFoundException");
        }

    } //end read

}//end class IconInport






