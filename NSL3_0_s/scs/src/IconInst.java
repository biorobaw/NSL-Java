/* SCCS  %W% --- %G% -- %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * IconInst - A class representing the graphical appearance of a module in
 * schematic. It inherits the Icon class because of the special case where
 * we need instances of pins for the links to the connections in the model.
 * <p>
 * If creating scheamtics that contain a lot of the same icon, and one
 * wanted to be very efficient, one could have the IconInst class
 * not inherit the Icon class but instead have a list of icons as
 * drawing templates for the iconinstances and the pins on the icons would
 * contain links to an object that contained both an index to the instance of the
 * icon and a link to the connection. You must draw using offset though.
 *
 * @author Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import java.awt.*;
import java.io.*;

@SuppressWarnings("Duplicates")
class IconInst extends Icon {

    static int firstSpotForIcon = SCSUtility.gridT2;

    String instanceName;
    boolean getCurrentVersion; //float this version =0; specific version=1
    String parameters = "";

    GraphicPart_rect selIconBody;
    GraphicPart selPort;
    GraphicPart_text instanceLabel;


//------------------------------------------------------------------------

    /**
     * Constructor of this class with no parameters.
     */
    public IconInst() {
        super();
        instanceName = "";
        selIconBody = null;
        instanceLabel = null;
        selPort = null;
        xmin = ymin = 0;  //where the icon plus ports is located on a icon canvas
        xmax = ymax = 0;  //
        getCurrentVersion = true; //float this version =0; specific version=1
    }

    /**
     * Constructor of this class with instanceName and an Icon.
     * @param        instanceName    string contain the name of this
     *					instance of an icon
     * @param        getCurrentVersionBoolean    always use the same specific version of
     *                              this icon or let it float to the most
     *                              recent.
     * @param    iconTemplate    Icon
     * @param    parameters      String
     * @since JDK1.2
     */
    public IconInst(String instanceName, boolean getCurrentVersionBoolean, Icon iconTemplate, String parameters) {
        super(iconTemplate);
        this.instanceName = instanceName;

        selIconBody = null;
        selPort = null;
        getCurrentVersion = getCurrentVersionBoolean;
        //where the icon plus ports is located on a schematic canvas
        xmin = iconTemplate.xmin; //iconTemplate should be 0
        ymin = iconTemplate.ymin; //iconTemplate should be 0
        xmax = iconTemplate.xmax; //iconTemplate should be delta
        ymax = iconTemplate.ymax; //iconTemplate should be delta
        shapexmin = iconTemplate.shapexmin; //iconTemplate should be 0
        shapeymin = iconTemplate.shapeymin; //iconTemplate should be 0
        shapexmax = iconTemplate.shapexmax; //iconTemplate should be delta
        shapeymax = iconTemplate.shapeymax; //iconTemplate should be delta

        //note: moduleLabel and instanceLabel are not part of the drawable objects since
        //their location is dependent on the final shape of the icon itself.  If they
        // were included, the definition of where to place the text would be recursive.


        instanceLabel = new GraphicPart_text(OptionsFrame.instanceTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, instanceName, OptionsFrame.instanceTextFont, OptionsFrame.instanceText_col, OptionsFrame.instanceTextSize);

        this.parameters = parameters;
    }

//----------------------------------------------------------------------------

    /**
     * Paint this icon.
     */
    public void paint(Graphics g) {
        int ix;
        int xdmin = 0;
        int ydmin = 0;
        GraphicPart tempPart;

        //todo: need to get font size and color from options
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        //      g.setFont(font);
        //      g.setColor(Color.red);
        FontMetrics fontmetrics = getFontMetrics(font);


        if (drawableParts == null) {
            //System.err.println("Error:IconInst:paint:drawableParts null");
            return;
        }
        int max = drawableParts.size();
        for (ix = 0; ix < max; ix++) {
            tempPart = (GraphicPart) drawableParts.elementAt(ix);
            tempPart.paint(g); // aa 00/05/05
        }
        //if selIconBody: draw selection box.
        if (selIconBody != null) {
//	//System.out.println("Debug:IconInst:paint: red "+xmin+" "+ymin);
            // selIconBody should have the right coordinates
            selIconBody.paintOpen(g);// move must update the selection
        }
        // show the input port name
        if (selPort != null) {
            if (selPort instanceof IconInport) {
                IconInport ip = (IconInport) selPort; //this is a sub Part of an icon

                g.setColor(Color.yellow);
                g.fillRect(ip.xmax + 3, ip.ymax + 3, fontmetrics.stringWidth(ip.Name) + 10,
                        fontmetrics.getHeight() + 10);
                g.setColor(Color.black);
                g.drawRect(ip.xmax + 3, ip.ymax + 3, fontmetrics.stringWidth(ip.Name) + 10,
                        fontmetrics.getHeight() + 10);
                g.setColor(Color.red);
                g.drawString(ip.Name, ip.xmax + 8, ip.ymax + 8 + fontmetrics.getHeight());

            }
            // show the outputport name
            if (selPort instanceof IconOutport) {
                IconOutport op = (IconOutport) selPort;

                g.setColor(Color.yellow);
                g.fillRect(op.xmax + 3, op.ymax + 3, fontmetrics.stringWidth(op.Name) + 10,
                        fontmetrics.getHeight() + 10);
                g.setColor(Color.black);
                g.drawRect(op.xmax + 3, op.ymax + 3, fontmetrics.stringWidth(op.Name) + 10,
                        fontmetrics.getHeight() + 10);
                g.setColor(Color.red);
                g.drawString(op.Name, op.xmax + 8, op.ymax + 8 + fontmetrics.getHeight());
            }
        }
        if (instanceLabel != null) {
            instanceLabel.paint(g);
        }
        if (moduleLabel != null) {
            moduleLabel.paint(g);
        }
    }
//---------------------------------------------------------------------

    /**
     * Move this icon as a whole by x offset in x-direction and y offset in
     * y-direction.
     *
     * @param    x    the moving offset in x-direction
     * @param    y    the moving offset in y-direction
     */
    public void moveobj(int x, int y) {

        GraphicPart part;
        Connection conn;

        xmin = xmin + x;
        ymin = ymin + y;
        xmax = xmax + x;
        ymax = ymax + y;
        shapexmin = shapexmin + x;
        shapeymin = shapeymin + y;
        shapexmax = shapexmax + x;
        shapeymax = shapeymax + y;

        // note: we do not move the drawableParts since they are part of the Icon
        // and their location is calculated each time we paint.

        //move bounding box indicating the object is selected
        if (selIconBody != null) {
            selIconBody.moveobj(x, y);
        }

        // move text label of instance selected
        if (instanceLabel != null) {
            instanceLabel.moveobj(x, y);
        }
        //todo: figure out how paint gets called after move
        if (moduleLabel != null) {
            moduleLabel.moveobj(x, y);
        }

        // move connections with object  - 00/05/16 aa
        if (drawableParts != null) {
            for (int i = 0; i < drawableParts.size(); i++) {
                part = (GraphicPart) drawableParts.elementAt(i);
                part.moveobj(x, y);
                if (part instanceof IconInport) {
                    conn = ((IconInport) part).link;
                    if (conn != null)
                        conn.movedest(x, y);
                }
                if (part instanceof IconOutport) {
                    IconOutport op = (IconOutport) part;
                    for (int j = 0; j < op.links.size(); j++) {
                        conn = op.links.elementAt(j);
                        conn.movesrc(x, y);
                    }
                }
            }
        }

    }
//---------------------------------------------------------------------

    /**
     * Select this icon as a whole if the point (x,y) is within the scope of this
     * icon.
     *
     * @return    <code>true</code> if the point (x,y) is within the scope of this
     *		icon
     *		<code>false</code> otherwise
     */
    public boolean selectobj(int x, int y) {
        //System.out.println("Debug:IconInst:selectobj: x " + x + "y: " + y);
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
            selIconBody = new GraphicPart_rect(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            return (true);
        }
        return (false);
    }
//---------------------------------------------------------------------

    /**
     * Select this icon as a whole if this icon is completely within the rectangle
     * represented by the left-up corner (x0,y0) and right-down corner (x1,y1).
     *
     * @return    <code>true</code> if this icon is completely within the
     *		rectangle represented by the left-up corner (x0,y0) and
     *		right-down corner (x1,y1)
     *		<code>false</code> otherwise
     */
    public boolean selectrect(int x0, int y0, int x1, int y1) {
        //System.out.println("Debug:IconInst:selectrect: x0 " + x0 + "y0: " + y0);
        //System.out.println("Debug:IconInst:selectrect: x1 " + x1 + "y1: " + y1);
        if (x0 < xmin && x1 > xmax && y0 < ymin && y1 > ymax) {
            selIconBody = new GraphicPart_rect(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            return (true);
        }
        return (false);
    }

//---------------------------------------------------------------------

    /**
     * get the corresponding Module that belongs with this Icon Instance
     *
     * @exception IOException     if an IO error occurred
     */
    public Module getModuleFromFile()
            throws IOException {
        Module returnModule = new Module();
        try {
            returnModule.getModuleFromFileUsingNick(libNickName, moduleName, versionName);
        } catch (IOException e) {
            System.err.println("IconInst:getModuleFromFile IOException " + moduleName);
            throw new IOException("IconInst getModleFromFile IOException");
        } catch (ClassNotFoundException e) {
            System.err.println("IconInst:getModuleFromFile ClassNotFoundException " + moduleName);
            throw new IOException("IconInst getModleFromFile ClassNotFoundException");
        }
        return (returnModule);
    }
    /*3456789012345678901234567890123456789012345678901234567890*/
    /*--------------------------*/

    /**
     * Find an icon Inport of this IconInst's icon by name
     *
     * @return    <code>index</code> if the name matches
     */

    public int findInport(String name) {
        GraphicPart tempPart;
        int index = -1;
        System.out.println("Debug:IconInst in findInport");
        for (int i = 0; i < drawableParts.size(); i++) {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if (tempPart instanceof IconInport) {
                IconInport ii = (IconInport) tempPart;
                if (ii.Name.equals(name)) {
                    return (i);
                }
            }
        }
        return (index);
    }

    /*--------------------------*/

    /**
     * Find an icon Outport of this IconInst's icon by name
     *
     * @return    <code>index</code> if the name matches
     */

    public int findOutport(String name) {
        GraphicPart tempPart;
        int index = -1;
        //System.out.println("Debug:IconInst in findOutport");
        for (int i = 0; i < drawableParts.size(); i++) {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if (tempPart instanceof IconOutport) {
                IconOutport io = (IconOutport) tempPart;
                if (io.Name.equals(name)) {
                    return (i);
                }
            }
        }
        return (index);
    }

    /*--------------------------*/

    /**
     * Select an icon port of this IconInst's icon if the point
     * (x,y) is within  close distance to that port.
     *
     * @return    <code>true</code> if the point (x,y) is within
     *          close distance to
     *		an icon port of this IconInst's icon and meanwhile 
     *          set that port's selPort variable to the found port.
     *          Also, unselect the port itself because ???
     *		Return true if found a port and false otherwise.
     */

    public boolean selectport(int x, int y) {
        GraphicPart tempPart;
        //System.out.println("Debug:IconInst in selectport: x:"+x+" y:"+y);
        for (int i = 0; i < drawableParts.size(); i++) {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if ((tempPart instanceof IconInport ||
                    tempPart instanceof IconOutport) &&
                    tempPart.selectobj(x, y)) { //00/5/11 aa
                selPort = tempPart;
                tempPart.unselect(); // todo:???
                return (true);
            }
        }
        return (false);
    }

    /*--------------------------*/

    /**
     * Make this IconInst unselected.
     */
    public void unselect() {
        //in C++ we would dispose of the old rect object but in java we wait for
        // the garbage collector to run
        selIconBody = null;
        //todo:do we have to paint the rect black?
        repaint(); //00/5/10 aa
    }

//------------------------------------------------------

    /**
     * Write this icon to the output stream oos.
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream oos)
            throws IOException {
        try {
            oos.writeUTF(instanceName);
            oos.writeBoolean(getCurrentVersion);
            oos.writeUTF(parameters);
            //for instances we do not zero
            super.write(oos, false); //write the icon info
        } catch (IOException e) {
            System.err.println("IconInst: write IOException");
            throw new IOException("IconInst write IOException");
        }

    }
    //------------------------------------------------------

    /**
     * writeAllChars this icon to the print writer
     */
    public void writeAllChars(PrintWriter pw) {

        pw.print("iconInstance: ");
        pw.print("\n");
        pw.print("instanceName: ");
        pw.print(instanceName);
        pw.print("\n");
        pw.print("getCurrentVersion: ");
        pw.print((Boolean.valueOf(getCurrentVersion)).toString());
        pw.print("\n");
        pw.print("parameters: ");
        pw.print(parameters);
        pw.print("\n");

        //for instances we do not zero
        super.writeAllChars(pw, false); //write the icon info
    }

    //---------------------------------------------------------------------

    /**
     * Read this icon from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(Schematic schematic, ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        int savedxmin;
        int savedymin;

        try {
            instanceName = ois.readUTF();
            getCurrentVersion = ois.readBoolean();
            parameters = ois.readUTF();
            super.read(ois); //we need the xmin and xmax
            selIconBody = null;
            selPort = null;
            savedxmin = xmin;
            savedymin = ymin;
        } catch (IOException e) {
            System.err.println("Error:IconInst read part1 IOException.");
            throw new IOException("IconInst read part1 IOException");
        }
        // This next part, replaces the IconInst with the
        // latest information from the file system
        // In the first case where the IconInst is a float, this
        // is absolutely necessary.
        // In the second case where the IconInst is a specific,
        // we go and get the same module again because its looks
        // could have changes. If one wants to get really picky,
        // they can disallow this, and leave specific IconInst as
        // they were when they were saved with the Schematic.
        try {
            FileInputStream fis;

            if (getCurrentVersion) {
                // float
                // open new stream
                // find path that icon.libNickName points to
                // find moduleName					   		      // read modules most recent icon information and set icon to that
                fis = SCSUtility.getModuleStreamUsingNickAndNoVersion(libNickName, moduleName);
            } else {
                //Specific
                // open new stream
                // find path that icon.libNickName points to
                // find moduleName and specific version of that module
                // read modules info and set icon to that
                fis = SCSUtility.getModuleStreamUsingNick(libNickName, moduleName, versionName);
            }
            if (fis == null) { // todo: change to exception
                System.err.println("Error:fis file is null");

                throw new IOException("Error: fis file is null");
            }
            ObjectInputStream mois = new ObjectInputStream(fis);   // read the Module and Icon which is stored separately
            Module module = new Module();
            module.readThruIcon(mois);//we only read until icon info since that is all we really need, and then this module will be garbage collected
            //System.out.println("Debug:IconInst:read part2: got module");
            fis.close();

            //System.out.println("Debug:IconInst:read: savedxmin: "+savedxmin+"  savedymin: "+savedymin);

            //System.out.println("Debug:IconInst:read: xmax "+xmax+" ymax "+ymax);

            // we use the location from the instance
            setIconInfoWOffset(module.myIcon, savedxmin, savedymin);

            instanceLabel = new GraphicPart_text(OptionsFrame.instanceTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, instanceName, OptionsFrame.instanceTextFont, OptionsFrame.instanceText_col, OptionsFrame.instanceTextSize);

        } //end try
        catch (IOException e) {
            System.err.println("Error:IconInst read part2 IOException.");
            throw new IOException("IconInst read part2 IOException");
        }

    }//end method	


} //end class IconInst









