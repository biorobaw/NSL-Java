/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * Connection - A class representing the connections in the schematic.
 * A connection may start from a schematic inport or icon outport of a module,
 * then end at a schematic outport or icon inport of another module.
 *
 * @author Xie, Gupta, Alexander
 * @version    %I%, %G%
 * @param    myColor        color of this connection
 * @param    numVerticies    how many vertices constitute this connection
 * @param    x        an array of x-coordinates of vertices along this
 * connection
 * @param    y        an array of y-coordinates of vertices along this
 * connection
 * @param    select        a flag indicating whether this connection is
 * selected and highlighted
 * @param    src_iconOrSchPort    the source of this connection, could be either a
 * module or schematic inport
 * @param    dest_iconOrSchPort the destination of this connection, could be
 * either a module or schematic outport
 * @param    src_port    exactly which icon outport of the source module
 * is the source of this connection when this
 * connection originates from a module; if source
 * is a schematic inport, then this variable is
 * null
 * @param    dest_port    exactly which icon inport of the destination
 * module is the destination of this connection
 * when this connection ends at a module; if
 * destination is a schematic outport, then this
 * variable is null
 * @since JDK8
 */


import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.lang.Math;


@SuppressWarnings({"Duplicates", "SpellCheckingInspection", "DuplicateExpressions"})
public class Connection extends Component {
    Color myColor;
    int numVerticies = 0;
    int[] x = new int[64];
    int[] y = new int[64];
    int select = 0;
    Component src_iconOrSchPort;
    Component dest_iconOrSchPort;
    IconOutport src_port;
    IconInport dest_port;

    /**
     * Constructor of this class with no parameters.
     */
    Connection() {
    }

    /**
     * Constructor of this class.
     *
     * @param    sm    source component of this connection when it's a module
     * @param    sp    icon outport of the source module where this connection
     *			originates
     * @param    cc    color of this connection
     */
    Connection(IconInst sm, IconOutport sp, Color cc) {
        //	x[0] = x[1] = sp.x1;
        //	y[0] = y[1] = sp.y1;
        x[0] = x[1] = sp.x1;
        y[0] = y[1] = sp.y1;
        numVerticies = 2;
        select = 3;
        myColor = cc;
        src_iconOrSchPort = sm;
        src_port = sp;
        sp.connect(this);
        dest_iconOrSchPort = null;
        dest_port = null;
    }

    /**
     * Constructor of this class.
     *
     * @param    sm    source component of this connection when it's a
     *			schematic inport
     * @param    cc    color of this connection
     */
    Connection(SchematicInport sm, Color cc) {
        x[0] = x[1] = sm.px1;
        y[0] = y[1] = sm.py1;
        numVerticies = 2;
        select = 3;
        myColor = cc;
        src_iconOrSchPort = sm;
        src_port = null;
        sm.connect(this);
        dest_iconOrSchPort = null;
        dest_port = null;
    }

    /**
     * Set color of this connection.
     */
    public void setcolor(Color cc) {
        myColor = cc;
    }

    /**
     * Paint this connection.
     */
    public void paint(Graphics g) {
        int xx, yy;
        double dist;
        int x0, y0, x1, y1;

        //	g.setColor(myColor);

        g.setColor(OptionsFrame.connection_col);
        for (int i = 0; i < numVerticies - 1; i++)
            GraphicsUtil.drawLine(g, x[i], y[i], x[i + 1], y[i + 1], SchematicPanel.linewidth);
        // g.drawLine(x[i],y[i],x[i+1],y[i]);

        if (dest_iconOrSchPort == null) {
            x0 = x[numVerticies - 2];
            y0 = y[numVerticies - 2];
            x1 = x[numVerticies - 1];
            y1 = y[numVerticies - 1];
            dist = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));

            if (dist > 10) {
                xx = (int) (x1 + (-10 * (x1 - x0) + 3 * (y1 - y0)) / dist);
                yy = (int) (y1 + (-10 * (y1 - y0) - 3 * (x1 - x0)) / dist);
                //	      g.drawLine(x[numVerticies-1],y[numVerticies-1],xx,yy,3);  // nitgu
                GraphicsUtil.drawLine(g, x[numVerticies - 1], y[numVerticies - 1], xx, yy, SchematicPanel.linewidth);


                xx = (int) (x1 + (-10 * (x1 - x0) - 3 * (y1 - y0)) / dist);
                yy = (int) (y1 + (-10 * (y1 - y0) + 3 * (x1 - x0)) / dist);
                //      g.drawLine(x[numVerticies-1],y[numVerticies-1],xx,yy,3);
                GraphicsUtil.drawLine(g, x[numVerticies - 1], y[numVerticies - 1], xx, yy, SchematicPanel.linewidth);

            } else {
                xx = (int) (x1 + (-0.8 * (x1 - x0) + 0.24 * (y1 - y0)));
                yy = (int) (y1 + (-0.8 * (y1 - y0) - 0.24 * (x1 - x0)));
                //	      g.drawLine(x[numVerticies-1],y[numVerticies-1],xx,yy,3);  // nitgupta
                GraphicsUtil.drawLine(g, x[numVerticies - 1], y[numVerticies - 1], xx, yy, SchematicPanel.linewidth);

                xx = (int) (x1 + (-0.8 * (x1 - x0) - 0.24 * (y1 - y0)));
                yy = (int) (y1 + (-0.8 * (y1 - y0) + 0.24 * (x1 - x0)));
                GraphicsUtil.drawLine(g, x[numVerticies - 1], y[numVerticies - 1], xx, yy, 3);
                //	       g.drawLine(x[numVerticies-1],y[numVerticies-1],xx,yySchematicPanel.linewidth);  // nitgupta
                GraphicsUtil.drawLine(g, x[numVerticies - 1], y[numVerticies - 1], xx, yy, SchematicPanel.linewidth);
            }
        }

        if (select != 0) {
            g.setColor(Color.red);
            for (int i = 0; i < numVerticies; i++)
                g.fillRect(x[i] - 2, y[i] - 2, 4, 4);
        }
    }

    /**
     * Add a new point at the end of this connection, with the initial position
     * same as the last vertex of this connection.
     */
    public void addpoint() {
        x[numVerticies] = x[numVerticies - 1];
        y[numVerticies] = y[numVerticies - 1];
        select = numVerticies + 2;
        numVerticies++;
    }

    /**
     * Insert a new point into a segment of this connection which is within
     * close distance to the point of (xx,yy).
     *
     * @param    xx    the x-position of the insert point
     * @param    yy    the y-position of the insert point
     * @param    grid    the measure of the grid
     */
    public void insertpoint(int xx, int yy, int grid) {
        int i, j;
        double k, dist, x0, y0;

        for (i = 0; i < numVerticies - 1; i++) {
            if (x[i] == x[i + 1]) {
                if (Math.abs(xx - x[i]) <= 3 && yy <= y[i + 1] && yy >= y[i] ||
                        Math.abs(xx - x[i]) <= 3 && yy <= y[i] && yy >= y[i + 1]) {
                    for (j = numVerticies; j > i + 1; j--) {
                        x[j] = x[j - 1];
                        y[j] = y[j - 1];
                    }
                    x[i + 1] = (xx + grid / 2) / grid * grid;
                    y[i + 1] = (yy + grid / 2) / grid * grid;
                    select = i + 3;
                    numVerticies++;
                    return;
                }
            }

            k = (y[i + 1] - y[i]) * 1.0 / (x[i + 1] - x[i]);

            x0 = (k * k * x[i] - k * y[i] + k * yy + xx) / (k * k + 1);
            y0 = k * (x0 - x[i]) + y[i];

            if (x0 >= x[i] && x0 <= x[i + 1] || x0 >= x[i + 1] && x0 <= x[i]) {
                dist = Math.sqrt(Math.pow(x0 - xx, 2) + Math.pow(y0 - yy, 2));
                if (dist <= 3) {
                    for (j = numVerticies; j > i + 1; j--) {
                        x[j] = x[j - 1];
                        y[j] = y[j - 1];
                    }
                    x[i + 1] = (xx + grid / 2) / grid * grid;
                    y[i + 1] = (yy + grid / 2) / grid * grid;
                    select = i + 3;
                    numVerticies++;
                    return;
                }
            }
        }
    }

    /**
     * Move the source point of this connection to the position of (xx,yy).
     */
    public void moveSrcAbsolute(int xx, int yy) {
        x[0] = xx;
        y[0] = yy;
    }


    /**
     * Move the source point of this connection to by xx, yy delta).
     */
    public void movesrc(int xx, int yy) {
        x[0] += xx;
        y[0] += yy;
    }

    /**
     * Move the destination point of this connection by adding (xx,yy).
     */
    public void moveDestAbsolute(int xx, int yy) {
        x[numVerticies - 1] = xx;
        y[numVerticies - 1] = yy;
    }

    /**
     * Move the destination point of this connection by adding (xx,yy).
     */
    public void movedest(int xx, int yy) {
        x[numVerticies - 1] += xx;
        y[numVerticies - 1] += yy;
    }

    /**
     * Move the selected point of this connection by adding (xx,yy).
     */
    public void movepoint(int xx, int yy) {
        if (select > 2) {
            x[select - 2] += xx;
            y[select - 2] += yy;
        }
    }

    /**
     * Merge the selected point with its neighbour on the connection if they
     * happen to be overlapped, or just eliminate the selected point if it's on
     * the straight line between its two neighbours.
     */
    public void merge() {
        int i;

        if (select < 3) return;

        if (x[select - 2] == x[select - 3] && y[select - 2] == y[select - 3]
                || x[select - 2] == x[select - 1] && y[select - 2] == y[select - 1]
                || x[select - 2] == x[select - 3] && x[select - 2] == x[select - 1] &&
                (y[select - 2] <= y[select - 3] && y[select - 2] >= y[select - 1] ||
                        y[select - 2] <= y[select - 1] && y[select - 2] >= y[select - 3])
                || (y[select - 2] - y[select - 3]) * 1.0 / (x[select - 2] - x[select - 3]) ==
                (y[select - 1] - y[select - 2]) * 1.0 / (x[select - 1] - x[select - 2])) {
            for (i = select - 2; i < numVerticies - 1; i++) {
                x[i] = x[i + 1];
                y[i] = y[i + 1];
            }
            numVerticies--;
            return;
        }

        if (select >= 4)
            if (x[select - 3] == x[select - 4] && x[select - 3] == x[select - 2] &&
                    (y[select - 3] <= y[select - 4] && y[select - 3] >= y[select - 2] ||
                            y[select - 3] <= y[select - 2] && y[select - 3] >= y[select - 4])
                    || (y[select - 3] - y[select - 4]) * 1.0 / (x[select - 3] - x[select - 4]) ==
                    (y[select - 2] - y[select - 3]) * 1.0 / (x[select - 2] - x[select - 3])) {
                for (i = select - 3; i < numVerticies - 1; i++) {
                    x[i] = x[i + 1];
                    y[i] = y[i + 1];
                }
                numVerticies--;
                return;
            }

        if (select <= numVerticies - 1)
            if (x[select - 1] == x[select - 2] && x[select - 1] == x[select] &&
                    (y[select - 1] <= y[select - 2] && y[select - 1] >= y[select] ||
                            y[select - 1] <= y[select] && y[select - 1] >= y[select - 2])
                    || (y[select - 1] - y[select - 2]) * 1.0 / (x[select - 1] - x[select - 2]) ==
                    (y[select] - y[select - 1]) * 1.0 / (x[select] - x[select - 1])) {
                for (i = select - 1; i < numVerticies - 1; i++) {
                    x[i] = x[i + 1];
                    y[i] = y[i + 1];
                }
                numVerticies--;
            }
    }

    /**
     * Merge the source point with its neighbour on the connection if they
     * happen to be overlapped.
     */
    public void mergesrc() {
        // merge the point (x[0], y[0])

        int i;

        if (numVerticies <= 2) return;

        if (x[0] == x[1] && y[0] == y[1]
                || x[1] == x[0] && x[1] == x[2] &&
                (y[1] <= y[0] && y[1] >= y[2] ||
                        y[1] <= y[2] && y[1] >= y[0])
                || (y[1] - y[0]) * 1.0 / (x[1] - x[0]) ==
                (y[2] - y[1]) * 1.0 / (x[2] - x[1])) {
            for (i = 1; i < numVerticies - 1; i++) {
                x[i] = x[i + 1];
                y[i] = y[i + 1];
            }
            numVerticies--;
        }
    }

    /**
     * Merge the destination point with its neighbour on the connection if they
     * happen to be overlapped.
     */
    public void mergedest() {
        // merge the point (x[numVerticies-1], y[numVerticies-1])

        if (numVerticies <= 2) return;

        if (x[numVerticies - 1] == x[numVerticies - 2] && y[numVerticies - 1] == y[numVerticies - 2]
                || x[numVerticies - 2] == x[numVerticies - 3] && x[numVerticies - 2] == x[numVerticies - 1] &&
                (y[numVerticies - 2] <= y[numVerticies - 3] && y[numVerticies - 2] >= y[numVerticies - 1] ||
                        y[numVerticies - 2] <= y[numVerticies - 1] && y[numVerticies - 2] >= y[numVerticies - 3])
                || (y[numVerticies - 2] - y[numVerticies - 3]) * 1.0 / (x[numVerticies - 2] - x[numVerticies - 3]) ==
                (y[numVerticies - 1] - y[numVerticies - 2]) * 1.0 / (x[numVerticies - 1] - x[numVerticies - 2])) {
            x[numVerticies - 2] = x[numVerticies - 1];
            y[numVerticies - 2] = y[numVerticies - 1];
            numVerticies--;
        }
    }

    /**
     * Select the point of the connection which is within close distance to the
     * point of (xx,yy).
     *
     * @return    <code>true</code> if there exists one point on the connection
     * 		which is within close distance to the point of (xx,yy)
     *		<code>false</code> otherwise
     */
    public boolean selectpoint(int xx, int yy) {
        for (int i = 1; i < numVerticies; i++) {
            if (xx - x[i] < 3 && xx - x[i] > -3 && yy - y[i] < 3 && yy - y[i] > -3) {
                if (i == numVerticies - 1 && dest_iconOrSchPort != null) return (false);
                select = i + 2;
                return (true);
            }
        }
        return (false);
    }

    /**
     * Select this connection as a whole if the point (xx, yy) is within close
     * distance to this connection.
     *
     * @return    <code>true</code> if the point (xx,yy) is within close distance
     * 		to this connection
     * 		<code>false</code> otherwise
     */
    public boolean selectobj(int xx, int yy) {
        int i;
        double k, dist, x0, y0;

        for (i = 0; i < numVerticies - 1; i++) {
            if (x[i] == x[i + 1]) {
                if (Math.abs(xx - x[i]) <= 3 && yy <= y[i + 1] && yy >= y[i] ||
                        Math.abs(xx - x[i]) <= 3 && yy <= y[i] && yy >= y[i + 1]) {
                    select = 1;
                    return (true);
                }
            }

            k = (y[i + 1] - y[i]) * 1.0 / (x[i + 1] - x[i]);

            x0 = (k * k * x[i] - k * y[i] + k * yy + xx) / (k * k + 1);
            y0 = k * (x0 - x[i]) + y[i];

            if (x0 >= x[i] && x0 <= x[i + 1] || x0 >= x[i + 1] && x0 <= x[i]) {
                dist = Math.sqrt(Math.pow(x0 - xx, 2) + Math.pow(y0 - yy, 2));
                if (dist <= 3) {
                    select = 1;
                    return (true);
                }
            }
        }
        return (false);
    }

    /**
     * Make this connection unselected.
     */
    public void unselect() {
        select = 0;
    }

    /**
     * Connect this connection to the destination module dm on its icon inport
     * dp.
     */
    public void connectdest(IconInst dm, IconInport dp) {
        x[numVerticies - 1] = dp.x0;
        y[numVerticies - 1] = dp.y0;
        dest_iconOrSchPort = dm;
        dest_port = dp;
        dp.connect(this);
    }
//---------------------------------------------------------------------------------

    /**
     * Connect this connection to the destination dm, which is a schematic
     * outport.
     */
    public void connectdest(SchematicOutport dm) {
        x[numVerticies - 1] = dm.px0;
        y[numVerticies - 1] = dm.py0;
        dest_iconOrSchPort = dm;
        dest_port = null;
        dm.connect(this);
    }
//---------------------------------------------------------------------------------

    /**
     * Check whether this connection is already connected to a destination or
     * still dangling.
     *
     * @return    <code>true</code> if this connection is already connected to a
     *		destination
     *		<code>false</code> if this connection is still dangling
     */
    public boolean done() {
        return dest_iconOrSchPort != null;
    }
//---------------------------------------------------------------------------------

    /**
     * Write this connection to the output stream os.
     *
     * @exception IOException    if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {
        int i;

        try {
            os.writeObject(myColor);
            os.writeInt(numVerticies);

            for (i = 0; i < numVerticies; i++) {
                os.writeInt(x[i]);
                os.writeInt(y[i]);
            }

        } catch (IOException e) {
            System.err.println("Error:Connection write IOException");
            throw new IOException("Connection write IOException");
        }
    }
//----------------------------------------------------------------------

    /**
     * WriteAllChars this connection to the output stream os.
     */
    public void writeAllChars(PrintWriter pw) {
        int i;

        //os.writeObject(myColor);
        pw.print("numVerticies: ");
        pw.print((new Integer(numVerticies)).toString());
        pw.print("\n");
        for (i = 0; i < numVerticies; i++) {
            pw.print(" x: ");
            pw.print((new Integer(x[i])).toString());
            pw.print(" y: ");
            pw.print((new Integer(y[i])).toString());
            pw.print("\n");
        }
    }
//----------------------------------------------------------------------
//	/**
//	 * writeNslm this connection to the output stream os.
//	 *
//	 * @exception	IOException	if an IO error occurred
//	 */
//	public void writeNslm(PrintWriter pw) throws IOException
//	{
//		int i;
//		//in future this could print the name of the connection
//	}

//-----------------------------------------------------------------------------

    /**
     * Read this connection from the input stream os.
     *
     * @exception IOException        if an IO error occurred
     * @exception ClassNotFoundException    if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        int i;

        try {

            myColor = (Color) os.readObject();
            numVerticies = os.readInt();
            //System.out.println("Debug:Connection:read: numVerticies: "+ numVerticies);
            for (i = 0; i < numVerticies; i++) {

                x[i] = os.readInt();
                //System.out.println("Debug:Connection:read: xi: "+ x[i]);
                y[i] = os.readInt();
                //System.out.println("Debug:Connection:read: yi: "+ y[i]);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Connection read ClassNotFoundException");
            throw new ClassNotFoundException("Connection read ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Connection read IOException");
            throw new IOException("Connection read IOException");
        }
    }

}







