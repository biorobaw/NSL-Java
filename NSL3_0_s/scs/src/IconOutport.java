/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * IconOutport - A class representing the icon outports or output pins
 * of a module's icon. This is an extended class from GraphicPart_line,
 * inherited all those graphical
 * attributes, plus some additional attributes special for an icon outport.
 * One new addition, is the index number of the instance of the icon
 * that this pin is attached to. (00/05/16 aa).
 *
 * @author Weifang Xie, Alexander
 * @version %I%, %G%
 * @param Name    the name of this icon outport
 * @param Type    the type of this icon outport, either Java native data
 * type, Nsl data type, or NslEnv type
 * @param Parameters      the parameters of the corresponding variable
 * representation of this icon outport
 * @param link    the hookup to a connection to this icon outport
 * @since JDK8
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

//todo: merger with IconInport

@SuppressWarnings("Duplicates")
class IconOutport extends GraphicPart_line {
    String Name = "";
    String Type = "";
    String parameters = "";
    char direction = 'L';
    char signalType = 'O'; //output
    Vector<Connection> links;

    static public String output_port_shape = "default";
    static public Color port_col = Color.white; //changes in OptionsFrame

    //final int pinLength = 70;//was
    // final int pinLength = 24;//99/4/15 aa: must be increment of grid
    //really should be calculated. grid is 8.
    // 99/4/27 move pinLength to Icon.java

    /**
     * Constructor of this class with no parameters.
     */
    public IconOutport() {
        links = new Vector<Connection>();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, Parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon outports, and color being set to n, t, p, xx0, yy0, and cc.
     * @param       n    the name of this icon outport
     * @param       t    the type of this icon outport, either Java native data
     *                      type, Nsl data type, or NslEnv type
     * @param       p      the parameters of the corresponding variable
     *                              representation of this icon outport
     * @param       signalType    the hookup to a connection to this icon outport
     *
     * @since JDK8
     */
    public IconOutport(String n, String t, String p, char p_dir, char signalType, int xx0, int yy0, Color cc) {
        super(xx0, yy0, cc);
        Name = n;
        Type = t;
        parameters = p;
        direction = p_dir;
        this.signalType = signalType;

        calcX1Y1(direction, xx0, yy0);
    }

    /**
     * Constructor of this class, with initial values of Name, Type, Parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon outports, and color being set to n, t, p, xx0, yy0, and cc.
     * @param       var     Declaration variable
     * @since JDK1.1
     */
    public IconOutport(Declaration var, int xx0, int yy0, Color cc) {
        super(xx0, yy0, cc);
        Name = var.varName;
        Type = var.varType;
        direction = var.portIconDirection;
        parameters = var.varParams;
        this.signalType = var.portSignalType;

        calcX1Y1(direction, xx0, yy0);
    }

    //--------------------------------------------------------
    protected void calcX1Y1(char direction, int xx0, int yy0) {

        x0 = xx0;
        y0 = yy0;

        //System.out.println("x0 " + xx0  + "yo" + yy0 + "x1" + x1 + "y1" + y1 );

        //System.out.println(direction);

        if (direction == 'L') {
            x1 = xx0 + Icon.pinLength;
            y1 = yy0;
            //System.out.println("x0 " + xx0  + "yo" + yy0 + "x1" + x1 + "y1" + y1 );
        }

        if (direction == 'T') {
            x1 = xx0;
            y1 = yy0 + Icon.pinLength;
            //System.out.println("x0 " + xx0  + "yo" + yy0 + "x1" + x1 + "y1" + y1 );
        }

        if (direction == 'B') {
            x1 = xx0;
            y1 = yy0 - Icon.pinLength;
            if (y1 < 0) {
                // todo: throw exception
                System.out.println("Error: IconOutport: y1 less than 0 ");
            }
            //System.out.println("x0 " + xx0  + "yo" + yy0 + "x1" + x1 + "y1" + y1 );
        }

        if (direction == 'R') {
            x1 = xx0 - Icon.pinLength;
            y1 = yy0;
            if (x1 < 0) {
                // todo: throw exception
                System.out.println("Error: IconOurport: x1 less than 0 ");
            }
            //System.out.println("x0 " + xx0  + "yo" + yy0 + "x1" + x1 + "y1" + y1 );
        }

        // 99/4/28 key to moving icon problem
        setminmax(); // call GraphicPart_lines setminmax
    }

    //------------------------------------------------------------
    public void movepoint(int x, int y) {
        moveobj(x, y);
        //  x1 = 10 ;
        //  y1 = 10 ;
    }
    //------------------------------------------------------

    /**
     * Paint this icon outport.
     */
    public void paint_arrow(Graphics g) {
        //    g.setColor(Color.white);
        g.setColor(port_col);
        g.drawLine(x0, y0, x1, y1);
        Polygon p = new Polygon();
        int x, y;
        double dist;

        int width = 5;


        // dont draw little rectangles
        //g.setColor(Color.red);
        //g.drawRect(x0-width , y0 - width , width*2 , width*2);
        //g.drawRect(x1-width , y1- width , width*2 , width*2);

        g.setColor(port_col);

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
            //if (select!= -1) {
            // don't draw little rectangles
            //g.setColor(Color.red);
            //g.fillRect(x0-2,y0-2,4,4);
            //g.fillRect(x1-2,y1-2,4,4);
            //}
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
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }

    /**
     * Paint this icon outport.
     */
    public void paint(Graphics g) {

        if (IconOutport.output_port_shape.equals("CROSS")) {
            paint_cross(g);
            return;
        }

        if (IconOutport.output_port_shape.equals("ARROW")) {
            paint_arrow(g);
            return;
        }


        //    g.setColor(Color.white);

        g.setColor(port_col);

        g.drawLine(x0, y0, x1, y1);

        if (x0 == x1)

            g.drawLine(x0 + 1, y0, x1 + 1, y1);


        if (y0 == y1)

            g.drawLine(x0, y0 - 1, x1, y1 - 1);


        Polygon p = new Polygon();
        int x, y;
        double dist;

        int width = 5;

        // don't draw little rectangles
        //g.setColor(Color.red);
        //g.drawRect(x0-width , y0 - width , width*2 , width*2);
        //g.drawRect(x1-width , y1- width , width*2 , width*2);

        g.setColor(port_col);

        if (select != 0) {
            //if (select!= -1) {
            // don't draw little rectangles
            //g.setColor(Color.red);
            //g.fillRect(x0-2,y0-2,4,4);
            //g.fillRect(x1-2,y1-2,4,4);
            //}
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
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }
    //----------------------------------------------


    /**
     * Paint this icon outport.
     */
    public void paint_cross(Graphics g) {
        //    g.setColor(Color.white);
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

            g.drawLine((x0 + x1) / 2 - 5, y0 - 5, (x0 + x1) / 2 + 5, y0 + 5);
            g.drawLine((x0 + x1) / 2 + 5, y0 - 5, (x0 + x1) / 2 - 5, y0 + 5);
        } else {

            g.drawLine(x0 - 5, (y0 + y1) / 2 - 5, x0 + 5, (y0 + y1) / 2 + 5);
            g.drawLine(x0 - 5, (y0 + y1) / 2 + 5, x0 + 5, (y0 + y1) / 2 - 5);

        }


        // p.addPoint(x1, y1);

        //     dist=Math.sqrt(Math.pow(x1-x0,2)+Math.pow(y1-y0,2));

        //     if (dist>10) {
        //        x=(int)(x1+(-10*(x1-x0)+3*(y1-y0))/dist);
        //        y=(int)(y1+(-10*(y1-y0)-3*(x1-x0))/dist);
        //        p.addPoint(x,y);

        //        x=(int)(x1+(-10*(x1-x0)-3*(y1-y0))/dist);
        //        y=(int)(y1+(-10*(y1-y0)+3*(x1-x0))/dist);
        //        p.addPoint(x,y);
        //     }
        //     else {
        //        x=(int)(x1+(-0.8*(x1-x0)+0.24*(y1-y0)));
        //        y=(int)(y1+(-0.8*(y1-y0)-0.24*(x1-x0)));
        //        p.addPoint(x,y);

        //        x=(int)(x1+(-0.8*(x1-x0)-0.24*(y1-y0)));
        //        y=(int)(y1+(-0.8*(y1-y0)+0.24*(x1-x0)));
        //        p.addPoint(x,y);
        //     }

        //     g.fillPolygon(p);

        if (select != 0) {
            //if (select!= -1) {
            // don't draw little rectangles
            //g.setColor(Color.red);
            //g.fillRect(x0-2,y0-2,4,4);
            //g.fillRect(x1-2,y1-2,4,4);
            //}
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
            g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
        }
    }


    //----------------------------------------------

    /**
     * Connect this icon outport to a connection "conn".
     */
    public void connect(Connection conn) {
        links.addElement(conn);
    }
    //----------------------------------------------

    /**
     * Disconnect this icon outport from its connected connection.
     */
    public void disconnect(Connection conn) {
        links.removeElement(conn);
    }
    //----------------------------------------------

    /**
     * Write this icon outport to the output stream os.
     *
     * @exception IOException     if an IO error occurred
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
        } catch (IOException e) {
            System.out.println("IconOutport write IOException");
            throw new IOException("IconOutport write IOException");
        }
    }
    //----------------------------------------------

    /**
     * Write this icon outport to the output stream os.
     */
    public void writeAllChars(PrintWriter pw, int x, int y) {
        pw.print("iconOutport: ");
        super.writeAllChars(pw, x, y);
        pw.print("Name: ");
        pw.print(Name);
        pw.print("\n");
        pw.print("Type: ");
        pw.print(Type);
        pw.print("\n");
        pw.print("parameters: ");
        pw.print(parameters);
        pw.print("\n");
        pw.print("direction: ");
        pw.print((Character.valueOf(direction)).toString());
        pw.print("\n");
        pw.print("signalType: ");
        pw.print((Character.valueOf(signalType)).toString());
        pw.print("\n");

        if (links != null) {
            for (int ix = 0; ix < links.size(); ix++) {
                Connection conn = links.elementAt(ix);
                pw.print("link num: ");
                pw.print((Integer.valueOf(ix)).toString());
                pw.print("\n");
                conn.writeAllChars(pw);
                pw.print("\n");
            }
        }
    }
    //-----------------------------------------------------

    /**
     * Read this icon outport from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        try {
            super.read(os);
            Name = os.readUTF();
            Type = os.readUTF();
            parameters = os.readUTF();
            direction = os.readChar();
            signalType = os.readChar();
        } catch (ClassNotFoundException e) {
            System.out.println("IconOutport read ClassNotFoundException");
            throw new ClassNotFoundException("IconOutport read ClassNotFoundException");
        } catch (IOException e) {
            System.out.println("IconOutport read IOException");
            throw new IOException("IconOutport read IOException");
        }
    }

}






