/* SCCS  %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * SchematicOutport - A class representing the schematic outports of a module's
 * schematic. This is an extended class from GraphicPart, inherited all those
 * graphical attributes, plus some additional attributes special for a schematic
 * outport.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version %I%, %G%
 * @param Name            the name of this schematic outport
 * @param Type            the type of this schematic outport, either Java
 * native data type, Nsl data type, or NslEnv type
 * @param Parameters      the parameters of the corresponding variable
 * representation of this schematic outport
 * @param link            the hookup to a connection to this schematic
 * outport
 * @param x               an array of x-coordinates of the  vertices
 * of the shape of this schematic outport
 * @param y               an array of y-coordinates of the  vertices
 * x
 * of the polygon shape of this schematic outport
 * @param px0             the x-coordinate of the starting point of the
 * little arrow of this schematic outport
 * @param px1             the x-coordinate of the ending point of the
 * little arrow of this schematic outport
 * @param py0             the y-coordinate of the starting point of the
 * little arrow of this schematic outport
 * @param py1             the y-coordinate of the ending point of the
 * little arrow of this schematic outport
 * @param port_col        the color of this schematic outport
 * @since JDK8
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

@SuppressWarnings("Duplicates")
public class SchematicOutport extends GraphicPart {
    String Name;
    String Type;
    String Parameters;
    char portDirection;
    Connection link;
    int[] x = new int[5];
    int[] y = new int[5];
    int px0, px1, py0, py1; //little connecting arrow
    int tx0, ty0; // bottom left corner of text of icon

    //Color port_col;
    //Color outline_col;

    /**
     * Constructor of this class with no parameters.
     */
    public SchematicOutport() {
        Name = "";
        Type = "";
        Parameters = "";
        portDirection = 'L';
        link = null;
        // todo: change to get current setting for portDirection

        initSchematicOutport();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, and
     * Parameters set to n, t, and p, respectively.
     */
    public SchematicOutport(String n, String t, String p) {
        Name = n;
        Type = t;
        Parameters = "";

        // todo: change to get current setting for portDirection
        portDirection = 'L';

        link = null;
        initSchematicOutport();
    }

    public SchematicOutport(String n, String t, String p, char p_portDirection) {
        Name = n;
        Type = t;
        Parameters = p;

        link = null;
        // todo: change to get current setting for portDirection
        portDirection = p_portDirection;
        initSchematicOutport();
    }

    public SchematicOutport(Declaration var) {
        Name = var.varName;
        Type = var.varType;
        Parameters = var.varParams;
        portDirection = var.portSchDirection;

        link = null;
        // todo: change to get current setting for portDirection

        initSchematicOutport();
    }

    private void initSchematicOutport() {
        //port_col = OptionsFrame.outPortFill_col;
        //outline_col =OptionsFrame.outPin_col;
        select = 0;
        tx0 = 0;  //this actually gets set below in paint.
        ty0 = 0;

        // 99/4/6
        switch (portDirection) {
            case 'T': // top down
                //   |
                //   V    // go clockwise from @
                // @---   //first line x[0] to x[1] 32 across
                // |  |   // 12 down
                // |  |
                // \  /   // slant 12 down
                //  \/
                px0 = 16; // little connecting arrow
                py0 = 0;
                px1 = 16;
                py1 = 16;
                x[0] = 0;  // add py1 to all y numbers
                y[0] = 16;
                x[1] = 32;
                y[1] = 16;
                x[2] = 32;
                y[2] = 28;
                x[3] = 16;
                y[3] = 40;
                x[4] = 0;
                y[4] = 28;

                xmin = 0;
                ymin = 0;
                xmax = 32;
                ymax = 40;
                break;
            case 'L': //left to right
                //    @----\
                //  ->|     >   //go clockwise from @
                //    |----/
                px0 = 0; // little connecting arrow
                py0 = 16;
                px1 = 16;
                py1 = 16;
                x[0] = 16;  // add px1 to all x numbers
                y[0] = 0;
                x[1] = 28;
                y[1] = 0;
                x[2] = 40;
                y[2] = 16;
                x[3] = 28;
                y[3] = 32;
                x[4] = 16;
                y[4] = 32;

                xmin = 0;
                ymin = 0;
                xmax = 40; //not including the arrow
                ymax = 32;
                break;
            case 'B': // bottom up
                //        ^
                //	     / \
                //	    |   |   //go clockwise from @
                //	    @----
                //        ^
                //        |
                px0 = 16; // little connecting arrow  (40-16=24)
                py0 = 40;
                px1 = 16;
                py1 = 24; //head

                x[0] = 0;  // clockwise from @
                y[0] = 24;
                x[1] = 0;
                y[1] = 12;
                x[2] = 16;
                y[2] = 0;
                x[3] = 32;
                y[3] = 12;
                x[4] = 32;
                y[4] = 24;
                xmin = 0;
                ymin = 0;
                xmax = 32;
                ymax = 24;
                break;

            case 'R': //right to left
                //   /----|
                //  <     |<-          //go clockwise
                //   \----@

                px0 = 40; // little connecting arrow
                py0 = 16;
                px1 = 24;
                py1 = 16;

                x[0] = 24;
                y[0] = 32;
                x[1] = 12;
                y[1] = 32;
                x[2] = 0;
                y[2] = 16;
                x[3] = 12;
                y[3] = 0;
                x[4] = 24;
                y[4] = 0;

                xmin = 0;
                ymin = 0;
                xmax = 24;
                ymax = 32;
                break;

            default:
                System.err.println("Error: SchematicOutport: no port direction.");
        }
    }
    /**
     * Set the color of this schematic outport to be cc.
     */
    // 6/13/02 aa: not called
    /* public void setcolor(Color cc)
    {
	port_col = cc;
    }
    */

    /**
     * Paint this schematic outport with Offsets
     */
    public void paint(Graphics g) {
        int[] xS = new int[5];
        int[] yS = new int[5];

        for (int i = 0; i < 5; i++) {
            xS[i] = x[i];
            yS[i] = y[i];
        }

        Polygon p;
        int xx, yy;
        double dist;

        p = new Polygon();
        for (int i = 0; i < 5; i++)
            p.addPoint(xS[i], yS[i]);
        g.setColor(OptionsFrame.outPortFill_col);
        g.fillPolygon(p);
        // fill triangle

        g.setColor(OptionsFrame.outPin_col);
        for (int i = 0; i < 5; i++)
            g.drawLine(xS[i], yS[i], xS[(i + 1) % 5], yS[(i + 1) % 5]);
        // draw border

        // todo: change so that you get the font and size
        Font font = OptionsFrame.instanceTextFont;
        g.setFont(font);
        FontMetrics fontmetrics = getFontMetrics(font);

        g.setColor(OptionsFrame.instanceText_col);
        //g.drawString(Name,(xmin+xmax)/2-fontmetrics.stringWidth(Name)/2,
        //(ymin+ymax)/2+8+fontmetrics.getHeight()/2);
        switch (portDirection) {
            case 'T': //top to bottom  - put text center bottom
                tx0 = (xmin + xmax) / 2 - fontmetrics.stringWidth(Name) / 2;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;
            case 'L': //left to right  - put text left justified
                tx0 = px1;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;
            case 'B': //bottom to top  - put text on top
                tx0 = (xmin + xmax) / 2 - fontmetrics.stringWidth(Name) / 2;
                ty0 = (ymin - SCSUtility.grid); //this is probably negative
                g.drawString(Name, tx0, ty0);
                break;
            case 'R': //right to left - put text left justified
                tx0 = px0;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;

            default:
                System.err.println("Error: SchematicOutport:1 not a port direction.");
        }

        // draw name

        int pxS0 = px0;
        int pxS1 = px1;
        int pyS0 = py0;
        int pyS1 = py1;

        // this is the little arrow
        //todo: use get color instead.
        g.setColor(OptionsFrame.outPin_col);
        g.drawLine(pxS0, pyS0, pxS1, pyS1);
        p = new Polygon();
        p.addPoint(pxS1, pyS1);
        dist = Math.sqrt(Math.pow(pxS1 - pxS0, 2) + Math.pow(pyS1 - pyS0, 2));

        if (dist > 10) {
            xx = (int) (pxS1 + (-10 * (pxS1 - pxS0) + 3 * (pyS1 - pyS0)) / dist);
            yy = (int) (pyS1 + (-10 * (pyS1 - pyS0) - 3 * (pxS1 - pxS0)) / dist);
            p.addPoint(xx, yy);

            xx = (int) (pxS1 + (-10 * (pxS1 - pxS0) - 3 * (pyS1 - pyS0)) / dist);
            yy = (int) (pyS1 + (-10 * (pyS1 - pyS0) + 3 * (pxS1 - pxS0)) / dist);
            p.addPoint(xx, yy);
        } else {
            xx = (int) (pxS1 + (-0.8 * (pxS1 - pxS0) + 0.24 * (pyS1 - pyS0)));
            yy = (int) (pyS1 + (-0.8 * (pyS1 - pyS0) - 0.24 * (pxS1 - pxS0)));
            p.addPoint(xx, yy);

            xx = (int) (pxS1 + (-0.8 * (pxS1 - pxS0) - 0.24 * (pyS1 - pyS0)));
            yy = (int) (pyS1 + (-0.8 * (pyS1 - pyS0) + 0.24 * (pxS1 - pxS0)));
            p.addPoint(xx, yy);
        }
        g.fillPolygon(p);
        // end draw arrow

        if (select != 0) {
            g.setColor(OptionsFrame.highlight_col);
            g.fillRect(xmin - 2, ymin - 2, 4, 4);
            g.fillRect(xmin - 2, ymax - 2, 4, 4);
            g.fillRect(xmax - 2, ymin - 2, 4, 4);
            g.fillRect(xmax - 2, ymax - 2, 4, 4);
        }
        // draw handles when selected
    }


    /**
     * Paint this schematic outport with out Offsets
     */
    //    public void paint(Graphics g) 
    //    {
    //	paint(g,0,0);
    //    }


    /**
     * Calculate the values of xmin, ymin, xmax, and ymax.
     */
    public void setminmax() {
        xmin = 1000;
        ymin = 1000;
        xmax = -1000;
        ymax = -1000;
        for (int i = 0; i < 5; i++) {
            if (x[i] < xmin) xmin = x[i];
            if (y[i] < ymin) ymin = y[i];
            if (x[i] > xmax) xmax = x[i];
            if (y[i] > ymax) ymax = y[i];
        }
        //text done in paint

        //little connecting arrow
        xmin = Math.min(xmin, px0);
        xmin = Math.min(xmin, px1);
        ymin = Math.min(ymin, py0);
        ymin = Math.min(ymin, py1);
        xmax = Math.max(xmax, px0);
        xmax = Math.max(xmax, px1);
        ymax = Math.max(ymax, py0);
        ymax = Math.max(ymax, py1);
    }

    /**
     * Move this schematic outport as a whole by xx offset in x-direction and yy
     * offset in y-direction.
     *
     * @param       xx       the moving offset in x-direction
     * @param       yy       the moving offset in y-direction
     */
    public void moveobj(int xx, int yy) {
        int i;

        for (i = 0; i < 5; i++) {
            x[i] += xx;
            y[i] += yy;
        }
        px0 += xx;
        px1 += xx;
        py0 += yy;
        py1 += yy;
        setminmax();

        if (link != null)
            link.movedest(xx, yy);
    }

    /**
     * Select the little arrow of this schematic outport if the point (xx,yy) is
     * within close distance to this little arrow.
     *
     * @return          <code>true</code> if the point (xx,yy) is within close
     *                  distance to the little arrow of this schematic outport
     *                  <code>false</code> otherwise
     */
    public boolean selectport(int xx, int yy) {
        return px0 == px1 && Math.abs(xx - px0) <= 3 &&
                (yy <= py0 && yy >= py1 || yy <= py1 && yy >= py0)
                || py0 == py1 && Math.abs(yy - py0) <= 3 &&
                (xx <= px0 && xx >= px1 || xx <= px1 && xx >= px0);

    }

    /**
     * Select this schematic outport as a whole if the point (xx,yy) is within
     * the scope of this schematic outport.
     *
     * @return          <code>true</code> if the point (xx,yy) is within the
     *                  scope of this schematic outport
     *                  <code>false</code> otherwise
     */
    public boolean selectobj(int xx, int yy) {
        if (xx >= xmin && xx <= xmax && yy >= ymin && yy <= ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }

    /**
     * Make this schematic outport unselected.
     */
    public void unselect() {
        select = 0;
    }

    /**
     * Connect this schematic outport to a connection "conn".
     */
    public void connect(Connection conn) {
        link = conn;
    }

    /**
     * Disconnect this schematic outport from connection "conn" among all of its
     * connected connections.
     */
    public void disconnect() {
        link = null;
    }
//----------------------------------------------------------------

    /**
     * Write this schematic outport to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {
        int i;

        try {
            os.writeUTF(Name);
            os.writeUTF(Type);
            os.writeUTF(Parameters);
            os.writeChar(portDirection);

            int ix;
            String str;

            //write 5 points that make up the arrow
            for (i = 0; i < 5; i++) {
                os.writeInt(x[i]);
                os.writeInt(y[i]);
            }
            //write bounds
            os.writeInt(px0);
            os.writeInt(py0);
            os.writeInt(px1);
            os.writeInt(py1);

            //6/13/02 aa: removed write color. it is set in environment
            //os.writeObject(OptionsFrame.outPortFill_col);
        } catch (IOException e) {
            System.err.println("Error:SchematicOutport write IOException");
            throw new IOException("Error:SchematicOutport write IOException");
        }
    }
    //----------------------------------------------------------------

    /**
     * WriteAllChars this schematic outport to the output stream os.
     */
    public void writeAllChars(PrintWriter pw) {
        int i;

        pw.print("schematicOutport: ");
        pw.print(Name);
        pw.print("\n");
        pw.print("Type: ");
        pw.print(Type);
        pw.print("\n");
        pw.print("Parameters: ");
        pw.print(Parameters);
        pw.print("\n");
        pw.print("portDirection: ");
        pw.print(portDirection);
        pw.print("\n");

        int ix;
        String str;
        pw.print("points of arrow: ");
        for (i = 0; i < 5; i++) {
            pw.print((Integer.valueOf(x[i])).toString());
            pw.print(" ");
            pw.print((Integer.valueOf(y[i])).toString());
            pw.print(" ");
        }
        pw.print("\n");


        pw.print("px0 py0 px1 py1: ");
        pw.print((Integer.valueOf(px0)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(py0)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(px1)).toString());
        pw.print(" ");
        pw.print((Integer.valueOf(py1)).toString());
        pw.print("\n");

        //os.writeObject(OptionsFrame.outPortFill_col);

    }

    //----------------------------------------------------------------

    /**
     * Read this schematic outport from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException {
        int n, i;

        try {
            Name = os.readUTF();
            Type = os.readUTF();
            Parameters = os.readUTF();
            portDirection = os.readChar();

            //read arrow points
            for (i = 0; i < 5; i++) {
                x[i] = os.readInt();
                y[i] = os.readInt();
            }
            //read bounds
            px0 = os.readInt();
            py0 = os.readInt();
            px1 = os.readInt();
            py1 = os.readInt();
            //6/13/02 aa: removed read color it is set in environment
            //port_col=(Color)os.readObject();

            link = null;
            select = 0;
            setminmax();
        }
        //catch (ClassNotFoundException e) {
        // throw new ClassNotFoundException("SchematicOutport read ClassNotFoundException");
        //}
        catch (IOException e) {
            throw new IOException("SchematicOutport read IOException");
        }
    }


} //end class SchematicOutport


