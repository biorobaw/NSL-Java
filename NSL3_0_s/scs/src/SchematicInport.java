/* SCCS %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * SchematicInport - A class representing the schematic inports of a module's
 * schematic. This is an extended class from GraphicPart, inherited all those
 * graphical attributes, plus some additional attributes special for a schematic
 * inport.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version %I%, %G%
 * @param Name        the name of this schematic inport
 * @param Type        the type of this schematic inport, either Java
 * native data type, Nsl data type, or NslEnv type
 * @param Parameters      the parameters of the corresponding variable
 * representation of this schematic inport
 * @param links        the hookup to a connection from this schematic
 * inport
 * @param x        an array of x-coordinates of the  vertices
 * of the box and triangle shape of this schematic inport
 * @param y        an array of y-coordinates of the vertices
 * of the box and triangle shape of this schematic inport
 * @param px0        the x-coordinate of the starting point of the
 * little connecting arros
 * schematic inport
 * @param px1        the real world x-coordinate of the ending point of the
 * little connecting arrow
 * schematic inport
 * @param py0        the y-coordinate of the starting point of the
 * little connecting arrow
 * @param py1        the y-coordinate of the ending point of the
 * little connecting arrow
 * schematic inport
 * @param port_col        color of this schematic inport -set in Options Frame
 * @param outline_col        outline color of this schematic inport - set in OptionsFrame
 * @since JDK8
 */

import java.awt.*;
import java.util.Vector;
import java.lang.Math;
import java.io.*;


@SuppressWarnings("Duplicates")
public class SchematicInport extends GraphicPart {
    String Name;
    String Type;
    String Parameters;
    char portDirection; // which way does the port point
    Vector<Connection> links;

    int[] x = new int[5];
    int[] y = new int[5];
    int px0, px1, py0, py1;  //coordinates of the little connecting arrow
    int tx0, ty0; // lower left of text location
    Color port_col;
    Color outline_col;


    /**
     * Constructor of this class with no parameters.
     */
    public SchematicInport() {
        Name = "";
        Type = "";
        Parameters = "";
        portDirection = 'L'; //L=left T=top
        links = new Vector<Connection>();
        // todo: change so that it reads the currently set direction
        initSchematicInport();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, and 
     * Parameters set to n, t, and p, respectively.
     */
    public SchematicInport(String n, String t, String p, char p_portDirection) {
        Name = n;
        Type = t;
        Parameters = p;
        portDirection = p_portDirection;
        links = new Vector<>();
        // todo: change so that it reads the currently set direction

        initSchematicInport();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, and 
     * Parameters set to n, t, and p, respectively.
     */
    public SchematicInport(Declaration var) {
        Name = var.varName;
        Type = var.varType;
        Parameters = var.varParams;
        portDirection = var.portSchDirection;
        links = new Vector<Connection>();
        // todo: change so that it reads the currently set direction

        initSchematicInport();
    }

    private void initSchematicInport() {
        select = 0;
        // todo: should be get port color
        //port_col = OptionsFrame.inPortFill_col;
        //outline_col = OptionsFrame.inPin_col;
        switch (portDirection) {
            case 'T':    //top to bottom
                // @---   //first line x[0] to x[1] 32 across
                // |  |   // 12 down
                // |  |
                // \  /   // slant 12 down
                //  \/
                //   |
                //   V    // go clockwise from @

                x[0] = 0;
                y[0] = 0;
                x[1] = 32;
                y[1] = 0;
                x[2] = 32;
                y[2] = 12;
                x[3] = 16;
                y[3] = 24;
                x[4] = 0;
                y[4] = 12;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 24;
                px1 = 16;
                py1 = 40;
                xmin = 0;
                ymin = 0;
                xmax = 32;  //does not include little arrow
                ymax = 24;
                break;
            case 'L': // left to right
                //    @----\
                //    |     >->   //go clockwise from @
                //    |----/
                x[0] = 0;
                y[0] = 0;
                x[1] = 12;
                y[1] = 0;
                x[2] = 24;
                y[2] = 16;
                x[3] = 12;
                y[3] = 32;
                x[4] = 0;
                y[4] = 32;
                // corridinates of little connecting arrow
                px0 = 24;
                py0 = 16;
                px1 = 40;
                py1 = 16;
                xmin = 0;
                ymin = 0;
                xmax = 40;
                ymax = 32;
                break;

            case 'R': // right to left
                //     /----|
                // <- <     |          //go clockwise
                //     \----@
                x[0] = 40; //(16+24=40)
                y[0] = 32;
                x[1] = 28; //40-12
                y[1] = 32;
                x[2] = 16;
                y[2] = 16;
                x[3] = 28; //16+12=28
                y[3] = 0;
                x[4] = 40;
                y[4] = 0;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 16;
                px1 = 0;
                py1 = 16;
                xmin = 0;
                ymin = 0;
                xmax = 40;
                ymax = 32;
                break;
            case 'B':    //bottom to top
                //        ^
                //        |
                //        ^
                //	     / \
                //	    |   |   //go clockwise from @
                //	    @----
                x[0] = 0;
                y[0] = 40;
                x[1] = 0;
                y[1] = 28; //40-12
                x[2] = 16;
                y[2] = 16;  //28-12 =16
                x[3] = 32;
                y[3] = 28;
                x[4] = 32;
                y[4] = 40;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 16;
                px1 = 16;
                py1 = 0;
                xmin = 0;
                ymin = 0;
                xmax = 32;  //does not include little arrow
                ymax = 40;
                break;

            default:
                System.err.println("Error:SchematicInport:Not a port direction.");
        }
    }

    /**
     * Set the fill color of this schematic inport to be cc.
     */
    /*
      not used: set in OptionsFrame
    public void setcolor(Color cc)
    {
	port_col = cc;
    }
    */

    /**
     * Paint this schematic inport.
     */
    public void paint(Graphics g) {

//	    int xS[] = new int[5];
//	    int yS[] = new int[5];
//	for(int i=0;i<5;i++) {
//	    xS[i]=x[i]+xOffset;
//	    yS[i]=y[i]+yOffset;
//
//	}

//
//	    int xSmin=xmin+xOffset;
//	    int ySmin=ymin+yOffset;
//	    int xSmax=xmax+xOffset;
//	    int ySmax=ymax+yOffset;

        Polygon p;
        int xx, yy;
        double dist;

        p = new Polygon();
        for (int i = 0; i < 5; i++)
            p.addPoint(x[i], y[i]);
        g.setColor(OptionsFrame.inPortFill_col);
        g.fillPolygon(p);
        // fill Polygon

        g.setColor(OptionsFrame.inPin_col);
        for (int i = 0; i < 5; i++)
            g.drawLine(x[i], y[i], x[(i + 1) % 5], y[(i + 1) % 5]);
        // draw border

        // todo: change to get font and size
        Font font = OptionsFrame.instanceTextFont;
        g.setFont(font);
        FontMetrics fontmetrics = getFontMetrics(font);

        g.setColor(OptionsFrame.instanceText_col);
        //g.drawString(Name, (xmin+xmax)/2-fontmetrics.stringWidth(Name)/2,
        //             (ymin+ymax)/2-8+fontmetrics.getHeight()/2);
        // This should be ymin - 1 grid.
        // todo: switch this to a GraphicPart_text and allow locations of
        // CENTER, BELOW, ABOVE, LEFT, RIGHT
        switch (portDirection) {
            case 'T': //top to bottom : put text at top center
                tx0 = (xmin + xmax) / 2 - fontmetrics.stringWidth(Name) / 2;
                ty0 = (ymin - SCSUtility.grid);
                g.drawString(Name, tx0, ty0);
                break;
            case 'L': //left to right : put text at bottom left justified
            case 'R': //right to left : put text at bottom left justified
                tx0 = xmin;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;
            case 'B': //bottom to top: put text at bottom center
                tx0 = (xmin + xmax) / 2 - fontmetrics.stringWidth(Name) / 2;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;

            default:
                System.err.println("Error: SchematicOutport: not a port direction.");
        }


        // draw little connecting arrow
        // todo: again this should make a get color call from the defaults

//	int pxS0=px0+xOffset;
//	int pxS1=px1+xOffset;
//	int pyS0=py0+yOffset;
//	int pyS1=py1+yOffset;

        g.setColor(OptionsFrame.inPin_col);
        g.drawLine(px0, py0, px1, py1);
        p = new Polygon();
        p.addPoint(px1, py1);
        dist = Math.sqrt(Math.pow(px1 - px0, 2) + Math.pow(py1 - py0, 2));

        if (dist > 10) {
            xx = (int) (px1 + (-10 * (px1 - px0) + 3 * (py1 - py0)) / dist);
            yy = (int) (py1 + (-10 * (py1 - py0) - 3 * (px1 - px0)) / dist);
            p.addPoint(xx, yy);

            xx = (int) (px1 + (-10 * (px1 - px0) - 3 * (py1 - py0)) / dist);
            yy = (int) (py1 + (-10 * (py1 - py0) + 3 * (px1 - px0)) / dist);
            p.addPoint(xx, yy);
        } else {
            xx = (int) (px1 + (-0.8 * (px1 - px0) + 0.24 * (py1 - py0)));
            yy = (int) (py1 + (-0.8 * (py1 - py0) - 0.24 * (px1 - px0)));
            p.addPoint(xx, yy);

            xx = (int) (px1 + (-0.8 * (px1 - px0) - 0.24 * (py1 - py0)));
            yy = (int) (py1 + (-0.8 * (py1 - py0) + 0.24 * (px1 - px0)));
            p.addPoint(xx, yy);
        }
        g.fillPolygon(p);
        // draw arrow

        if (select != 0) {
            g.setColor(OptionsFrame.highlight_col);
            g.fillRect(xmin - 2, ymin - 2, 4, 4);
            g.fillRect(xmin - 2, ymax - 2, 4, 4);
            g.fillRect(xmax - 2, ymin - 2, 4, 4);
            g.fillRect(xmax - 2, ymax - 2, 4, 4);
        }
        // draw handles when selected
    }
//--------------------------------------------

    /**
     * Paint this schematic inport.
     */
/*
        public void paint(Graphics g) 
        {
    	paint(g,0,0);
        }
*/
    /**
     * Paint this schematic inport.
     */
/*  This is commented out !!!!!!!!!!!!!!!!!!!!!!!!! see above
    public void paint(Graphics g, int xOffset, int yOffset)
    {
	    int xS[] = new int[5];
	    int yS[] = new int[5];
	    int xSmin=xmin+xOffset;
	    int ySmin=ymin+yOffset;
	    int xSmax=xmax+xOffset;
	    int ySmax=ymax+yOffset;


	for(int i=0;i<5;i++) {
	    xS[i]=x[i]+xOffset;
	    yS[i]=y[i]+yOffset;
	}

	Polygon p;
	int xx, yy;
	double dist;

	p = new Polygon();
	for(int i=0;i<5;i++)
	    p.addPoint(xS[i],yS[i]);
	g.setColor(OptionsFrame.inPortFill_col);
	g.fillPolygon(p);
	// fill Polygon

	g.setColor(OptionsFrame.inPin_col);
	for(int i = 0;i<5;i++)
	   g.drawLine(xS[i],yS[i],xS[(i+1)%5],yS[(i+1)%5]);
	// draw border

	// todo: change to get font and size
        Font font=new Font("Monospaced",Font.PLAIN,12);// was Serif,ITALIC,20
        g.setFont(font);
        FontMetrics fontmetrics=getFontMetrics(font);

	g.setColor(OptionsFrame.highlight_col);
        //g.drawString(Name, (xSmin+xSmax)/2-fontmetrics.stringWidth(Name)/2,
        //             (ySmin+ySmax)/2-8+fontmetrics.getHeight()/2);
        // This should be ymin - 1 grid.
	switch (portDirection) {
	case 'T': //top to bottom
		tx0=(xSmin+xSmax)/2-fontmetrics.stringWidth(Name)/2;
		ty0=(ymin-SCSUtility.grid);
		g.drawString(Name,tx0,ty0);
		break;
	case 'L': //left to right
		tx0=xSmin;
		ty0=(ySmax+SCSUtility.grid+fontmetrics.getHeight() );
		g.drawString(Name,tx0,ty0);
		break;
	case 'B': //bottom to top
		tx0=(xSmin+xSmax)/2-fontmetrics.stringWidth(Name)/2;
		ty0=(ymax+SCSUtility.grid+fontmetrics.getHeight());
		g.drawString(Name,tx0,ty0);
		break;
	case 'R': //right to left
		tx0=xSmin;
		ty0=(ySmax+SCSUtility.grid+fontmetrics.getHeight() );
		g.drawString(Name,tx0,ty0);
		break;

	default: System.err.println("Error: SchematicOutport: not a port direction.");
	}


	// draw little connecting arrow
	// todo: again this should make a get color call from the defaults
	int pxS0=px0+xOffset;
	int pxS1=px1+xOffset;
	int pyS0=py0+yOffset;
	int pyS1=py1+yOffset;

	g.setColor(OptionsFrame.inPin_col);
	g.drawLine(pxS0, pyS0, pxS1, pyS1);
	p = new Polygon();
        p.addPoint(pxS1, pyS1);
        dist=Math.sqrt(Math.pow(pxS1-pxS0,2)+Math.pow(pyS1-pyS0,2));

        if (dist>10) {
           xx=(int)(pxS1+(-10*(pxS1-pxS0)+3*(pyS1-pyS0))/dist);
           yy=(int)(pyS1+(-10*(pyS1-pyS0)-3*(pxS1-pxS0))/dist);
           p.addPoint(xx,yy);

           xx=(int)(pxS1+(-10*(pxS1-pxS0)-3*(pyS1-pyS0))/dist);
           yy=(int)(pyS1+(-10*(pyS1-pyS0)+3*(pxS1-pxS0))/dist);
           p.addPoint(xx,yy);
        }
        else {
           xx=(int)(pxS1+(-0.8*(pxS1-pxS0)+0.24*(pyS1-pyS0)));
           yy=(int)(pyS1+(-0.8*(pyS1-pyS0)-0.24*(pxS1-pxS0)));
           p.addPoint(xx,yy);

           xx=(int)(pxS1+(-0.8*(pxS1-pxS0)-0.24*(pyS1-pyS0)));
           yy=(int)(pyS1+(-0.8*(pyS1-pyS0)+0.24*(pxS1-pxS0)));
           p.addPoint(xx,yy);
        }
	g.fillPolygon(p);
	// draw arrow

	if (select!=0)
	{
	   g.setColor(OptionsFrame.highlight_col);
           g.fillRect(xSmin-2,ySmin-2,4,4);
           g.fillRect(xSmin-2,ySmax-2,4,4);
           g.fillRect(xSmax-2,ySmin-2,4,4);
           g.fillRect(xSmax-2,ySmax-2,4,4);
	}
	// draw handles when selected
    }
*/

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
     * Move this schematic inport as a whole by xx offset in x-direction and yy 
     * offset in y-direction.
     *
     * @param       xx       the moving offset in x-direction
     * @param       yy       the moving offset in y-direction
     */
    public void moveobj(int xx, int yy) {
        int i;
        Connection conn;

        for (i = 0; i < 5; i++) {
            x[i] += xx;
            y[i] += yy;
        }
        px0 += xx;
        px1 += xx;
        py0 += yy;
        py1 += yy;
        setminmax();

        for (i = 0; i < links.size(); i++) {
            conn = links.elementAt(i);
            conn.movesrc(xx, yy);
        }
    }

    /**
     * Select the little arrow of this schematic inport if the point (xx,yy) is      * within close distance to this little arrow.
     *
     * @return          <code>true</code> if the point (xx,yy) is within close
     *                  distance to the little arrow of this schematic inport
     *                  <code>false</code> otherwise
     */
    public boolean selectport(int xx, int yy) {
        return px0 == px1 && Math.abs(xx - px0) <= 3 &&
                (yy <= py0 && yy >= py1 || yy <= py1 && yy >= py0)
                || py0 == py1 && Math.abs(yy - py0) <= 3 &&
                (xx <= px0 && xx >= px1 || xx <= px1 && xx >= px0);

    }

    /**
     * Select this schematic inport as a whole if the point (xx,yy) is within 
     * the scope of this schematic inport.
     *
     * @return        <code>true</code> if the point (xx,yy) is within the
     *			scope of this schematic inport
     *              	<code>false</code> otherwise
     */
    public boolean selectobj(int xx, int yy) {
        if (xx >= xmin && xx <= xmax && yy >= ymin && yy <= ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }

    /**
     * Make this schematic inport unselected.
     */
    public void unselect() {
        select = 0;
    }

    /**
     * Connect this schematic inport to a connection "conn".
     */
    public void connect(Connection conn) {
        links.addElement(conn);
    }

    /**
     * Disconnect this schematic inport from connection "conn" among all of its 
     * connected connections.
     */
    public void disconnect(Connection conn) {
        links.removeElement(conn);
    }
//------------------------------------------------------------

    /**
     * Write this schematic inport to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {
        int i;

        try {
            int ix;
            String str;
            os.writeUTF(Name);
            os.writeUTF(Type);
            os.writeUTF(Parameters);
            os.writeChar(portDirection);

            //write the 5 corners of the port
            for (i = 0; i < 5; i++) {
                os.writeInt(x[i]);
                os.writeInt(y[i]);
            }
            //write the bounds
            os.writeInt(px0);
            os.writeInt(py0);
            os.writeInt(px1);
            os.writeInt(py1);

            //6/13/02 aa: removed write the color fill: it is part of environ

        } catch (IOException e) {
            System.err.println("SchematicInport:write IOException");
            throw new IOException("SchematicInport write IOException");
        }
    }

//------------------------------------------------------------

    /**
     * WriteAllChars this schematic inport to the output stream os.
     */
    public void writeAllChars(PrintWriter pw) {
        int i;

        pw.print("schematicInport: ");
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

        pw.print("points of arrow: ");
        for (i = 0; i < 5; i++) {
            pw.print((new Integer(x[i])).toString());
            pw.print(" ");
            pw.print((new Integer(y[i])).toString());
            pw.print(" ");
        }
        pw.print("\n");
        pw.print("px0 py0 px1 py1: ");
        pw.print((new Integer(px0)).toString());
        pw.print(" ");
        pw.print((new Integer(py0)).toString());
        pw.print(" ");
        pw.print((new Integer(px1)).toString());
        pw.print(" ");
        pw.print((new Integer(py1)).toString());
        pw.print("\n");


        int ix;
        String str;
    }
    //------------------------------------------------------------------

    /**
     * Read this schematic inport from the input stream os.
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

            //read the 5 points that make up the port
            for (i = 0; i < 5; i++) {
                x[i] = os.readInt();
                y[i] = os.readInt();
            }
            //read bounds
            px0 = os.readInt();
            py0 = os.readInt();
            px1 = os.readInt();
            py1 = os.readInt();

            //6/13/02 aa:removed the color reading: it is part of the environment

            if (links.size() != 0)
                links.removeAllElements();

            select = 0;
            setminmax();
        }
        //catch (ClassNotFoundException e) {
        //  throw new ClassNotFoundException("SchematicInport read ClassNotFoundException");
        //}
        catch (IOException e) {
            throw new IOException("SchematicInport read IOException");
        }
    }

    //-----------------------
} //end class SchematicInport



