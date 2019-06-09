/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * Icon - A class representing the graphical appearance of a module in
 * schematic. It's not only composed of graphical body with different shapes,
 * but also little arrows representing icon inports and icon outports.
 *
 * @author Xie, Gupta, Alexander
 * @version 1.4, 04/13/00
 * @param libNickName    Alias name of library this icon came from
 * @param moduleName    name of module this iconInst is related to
 * @param versionName    version of module this iconInst is
 * @param xmin    the x-coordinate of the left-up corner of the smallest
 * enclosing rectangle border of this icon with ports
 * @param ymin    the y-coordinate of the left-up corner of the smallest
 * enclosing rectangle border of this icon with ports
 * @param xmax    the x-coordinate of the right-down corner of the
 * smallest enclosing rectangle border of this icon with ports
 * @param ymax    the y-coordinate of the right-down corner of the
 * smallest enclosing rectangle border of this icon with ports
 * @param shapexmin    the x-coordinate of the left-up corner of the smallest
 * enclosing rectangle border of this icon with no ports
 * @param shapeymin    the y-coordinate of the left-up corner of the smallest
 * enclosing rectangle border of this icon with no ports
 * @param shapexmax    the x-coordinate of the right-down corner of the
 * smallest enclosing rectangle border of this icon with no ports
 * @param shapeymax    the y-coordinate of the right-down corner of the
 * smallest enclosing rectangle border of this icon with no ports
 * @param drawableParts    an vector of components that make up of this
 * icon, including graphical shapes, icon inports
 * and icon outports
 * @param select    a flag indicating whether this icon is selected and
 * highlighted
 * @parama moduleLabel
 * @since JDK8
 */

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;


@SuppressWarnings("Duplicates")
class Icon extends Component {
    //todo: really should be calculated pinLength grid is 8.
    public static final int pinLength = 24;//99/4/15 aa: must be increment of grid

    String libNickName = "LIB1"; //re-stating these here since it is needed
    // by the schematic editor - instances of icons
    String moduleName = null;
    String versionName = null;

    int xmin, ymin, xmax, ymax;  // with ports
    int shapexmin, shapeymin, shapexmax, shapeymax;  //without ports

    Vector drawableParts;
    GraphicPart_text moduleLabel = null;

    int select = 0; //is part of the icon selected?

    static private boolean debug = false;

    /**
     * Constructor of this class with no parameters.
     */
    public Icon() {
        libNickName = "LIB1"; //re-stating these here since it is needed
        // by the schematic editor - instances of icons
        moduleName = null;
        versionName = null;

        drawableParts = new Vector();
        xmin = ymin = 10000;  //where the icon plus ports is located on a icon canvas
        xmax = ymax = -10000;  //
        shapexmin = shapeymin = 10000; //where the icon is located on a icon canvas
        shapexmax = shapeymax = -10000;
        // only if you know the module name do you create a moduleLabel
        //moduleLabel=new GraphicPart_text(OptionsFrame.moduleTextLocation, shapexmin, shapeymin ,shapexmax,shapeymax,"DUMMY99", OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);

    }
    //------------------------------------------------

    /**
     * Constructor of this class with current partial Module
     */
    public Icon(String libNickName, String moduleName, String versionName) {
        this.libNickName = libNickName;
        this.moduleName = moduleName;
        this.versionName = versionName;

        drawableParts = new Vector();
        xmin = ymin = 10000;  //where the icon plus ports is located on a icon canvas
        xmax = ymax = -10000;  //
        shapexmin = shapeymin = 10000; //where the icon is located on a icon canvas
        shapexmax = shapeymax = -10000;
        // cannot create the label until we have the locations
        //moduleLabel=new GraphicPart_text(OptionsFrame.moduleTextLocation, shapexmin, shapeymin ,shapexmax,shapeymax,moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);

    }
    //------------------------------------------------------------

    /**
     * Constructor of this class with with parameters.
     */
    public Icon(String libNickName, String moduleName, String versionName, int xmin, int ymin, int xmax, int ymax, int shapexmin, int shapeymin, int shapexmax, int shapeymax) {

        this.libNickName = libNickName;
        // by the schematic editor - instances of icons
        this.moduleName = moduleName;
        this.versionName = versionName;

        drawableParts = new Vector();
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        this.shapexmin = shapexmin;
        this.shapeymin = shapeymin;
        this.shapexmax = shapexmax;
        this.shapeymax = shapeymax;
        //note: moduleLabel is not part of the drawable objects since
        //their location is dependent on the final shape of the icon itself.  If they
        // were included, the definition of where to place the text would be recursive.

        moduleLabel = new GraphicPart_text(OptionsFrame.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);
    }
    //------------------------------------------------------------

    /**
     * Constructor of this class with an existing icon
     */
    public Icon(Icon myicon) {
        setIconInfo(myicon);
    }

    //------------------------------------------------------------

    /**
     * Set the Icon's information
     */
    public void setIconInfo(Icon myicon) {
        int ix;
        GraphicPart part;

        // by the schematic editor - instances of icons
        this.libNickName = myicon.libNickName;
        this.moduleName = myicon.moduleName;
        this.versionName = myicon.versionName;

        this.xmin = myicon.xmin;
        this.ymin = myicon.ymin;
        this.xmax = myicon.xmax;
        this.ymax = myicon.ymax;
        this.shapexmin = myicon.shapexmin;
        this.shapeymin = myicon.shapeymin;
        this.shapexmax = myicon.shapexmax;
        this.shapeymax = myicon.shapeymax;

        moduleLabel = myicon.moduleLabel;

        drawableParts = myicon.drawableParts;
	/*
	  if (myicon.drawableParts!=null) {
	  for (ix=0; ix<myicon.drawableParts.size(); ix++) {
	  part=(GraphicPart)drawableParts.elementAt(ix);
	  addDrawablePart(part);
	  }
	  }
	*/
    }
    //------------------------------------------------------------

    /**
     * Set the Icon's information with an offset
     * This is for copy the new icon information to the iconInstances
     * that are in existence.
     */
    public void setIconInfoWOffset(Icon myicon, int xOffset, int yOffset) {
        int ix;
        GraphicPart part;
        if (myicon == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon template is null");
            return;
        }

        if (myicon.libNickName == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon libNickName is null");
            return;
        }

        if (myicon.moduleName == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon moduleName is null");
            return;
        }

        if (myicon.versionName == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon versionName is null");
            return;
        }

        if (myicon.moduleLabel == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon moduleLabel is null");
            return;
        }

        if (myicon.drawableParts == null) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon drawableParts is null");
            return;
        }

        if (myicon.drawableParts.size() == 0) {
            System.err.println("Error:Icon:setIconInfoWOffset:icon drawableParts is zero");
            return;
        }

        // by the schematic editor - instances of icons
        this.libNickName = myicon.libNickName;
        this.moduleName = myicon.moduleName;
        this.versionName = myicon.versionName;

        this.xmin = myicon.xmin + xOffset;
        this.ymin = myicon.ymin + yOffset;
        this.xmax = myicon.xmax + xOffset;
        this.ymax = myicon.ymax + yOffset;
        this.shapexmin = myicon.shapexmin + xOffset;
        this.shapeymin = myicon.shapeymin + yOffset;
        this.shapexmax = myicon.shapexmax + xOffset;
        this.shapeymax = myicon.shapeymax + yOffset;

        //We are re-reading the module again to get the latest info.
        //We must erase the old drawableParts list.

        deleteAllDrawableParts(); //for this icon not the incoming


        if (myicon.drawableParts != null) {
            for (ix = 0; ix < myicon.drawableParts.size(); ix++) {
                part = (GraphicPart) myicon.drawableParts.elementAt(ix);
                part.moveobj(xOffset, yOffset);
                addDrawablePart(part);
            }
        }
        // the following is actually calculated in addDrawablePart
        //moduleLabel=myicon.moduleLabel;
        //moduleLabel.moveobj(xOffset,yOffset);

    }
    //------------------------------------------------------------

    /**
     * Add a component to this icon. The component might be a graphic shape, an icon
     * inport, or icon outport.
     *
     * @param gobj the graphic component that's gonna be added to this icon
     *             Note: adjust location of moduleLabel.
     */
    // 99/5/13 aa: this method is only called when the initial first
    // point of the rectangle is placed.  Thus xmax and ymax are not
    // growing. ??? todo: is this true
    public void addDrawablePart(GraphicPart gobj) {
        drawableParts.addElement(gobj);
        //System.out.println("Debug:Icon:addDrawablePart:");
        setminmax(gobj);
        if (moduleLabel == null) {
            moduleLabel = new GraphicPart_text(OptionsFrame.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);
        } else { //recalculate the location of the moduleLabel
            moduleLabel.initText(OptionsFrame.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);
        }
    }
    //------------------------------------------------------------

    /**
     * Delete the ix'th component of this icon.
     *
     * @param ix the index of the component in the vector that's gonna be
     *           deleted
     *           Note: adjust location of moduleLabel.
     */
    public void deleteDrawablePart(int ix) {
        drawableParts.removeElementAt(ix);
        //System.out.println("Icon:deleteDrawablePart:setminmax");
        setminmax();
        if ((drawableParts.size() == 0) && (moduleLabel != null)) {
            moduleLabel = null;
        } else { //re-calculate the location of the moduleLabel
            assert moduleLabel != null;
            moduleLabel.initText(OptionsFrame.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);
        }
    }

    //------------------------------------------------------------

    /**
     * Delete All drawableParts
     * <p>
     * <p>
     * deleted
     */
    public void deleteAllDrawableParts() {
        drawableParts = null;
        drawableParts = new Vector();
        if ((moduleLabel != null)) {
            moduleLabel = null;
        }
    }


    //------------------------------------------------------------

    /**
     * Move the ix'th component of this icon to the last of the components drawableParts vector.
     * Since we are drawing the components in the order that they're in the vector,
     * being the last component means it will be the last to be painted and
     * appear on top of other images.
     *
     * @param ix the index of the component in vector that's gonna be
     *           moved to the last position
     *           Note: this is a very dangerous method and should NOT BE USED.
     *           Rearranging the pins on a icon, causes the schematic editor to
     *           chock when it reads in the connections.
     */
    public void moveToLast(int ix) {
        GraphicPart TempComponent;

        TempComponent = (GraphicPart) drawableParts.elementAt(ix);
        drawableParts.removeElementAt(ix);
        drawableParts.addElement(TempComponent);
    }

    /**
     * Paint this icon.
     */
    public void paint(Graphics g) {
        int ix;
        GraphicPart TempComponent;

        if (drawableParts != null) {
            for (ix = 0; ix < drawableParts.size(); ix++) {
                TempComponent = (GraphicPart) drawableParts.elementAt(ix);
                TempComponent.paint(g);
            }
        }
        //System.out.println("Debug:Icon:paint:going to call setminmax()");
        //setminmax(); //00/05/07 aa: took out

        //if selected: draw selection box.
        if (select != 0) {
            g.setColor(Color.red);
            //System.out.println("Debug:Icon: red "+xmin+" "+ymin);
            g.drawRect(xmin, ymin, xmax - xmin, ymax - ymin); // nitgupta
	    /*
	      g.fillRect(xmin-2,ymin-2,4,4);
	      g.fillRect(xmin-2,ymax-2,4,4);
	      g.fillRect(xmax-2,ymin-2,4,4);
	      g.fillRect(xmax-2,ymax-2,4,4);
	    */
        }
        if (moduleLabel != null) moduleLabel.paint(g);
    }

    //------------------------------------------------------------

    /**
     * Move the ix'th part of this icon by x offset in x-direction and y offset * in y-direction.
     *
     * @param ix the index of the component that'll be moved
     * @param x  the moving offset in x-direction
     * @param y  the moving offset in y-direction
     */
    public void movePart(int ix, int x, int y) {
        GraphicPart part;

        part = (GraphicPart) drawableParts.elementAt(ix);
        part.moveobj(x, y);
        //System.out.println("Icon:movePart:setminmax");
        setminmax();
    }
    //------------------------------------------------------------

    /**
     * Move this icon as a whole by x offset in x-direction and y offset in
     * y-direction.
     *
     * @param x the moving offset in x-direction
     * @param y the moving offset in y-direction
     */
    public void moveobj(int x, int y) {
        int ix;
        GraphicPart part;

        if (drawableParts != null) {
            for (ix = 0; ix < drawableParts.size(); ix++) {
                part = (GraphicPart) drawableParts.elementAt(ix);
                part.moveobj(x, y);
            }
            moduleLabel.moveobj(x, y);
        }

        //System.out.println("Icon:moveobj:setminmax");
        //aa: note this setminmax has to be separate or else
        // if you try and set it in the loop above the min and max
        // settings for xmin, xmax, ymin, and ymax do not work
        setminmax();
        //label location should be based on xmin, xmax,ymin,ymax
        moduleLabel.moveobj(x, y);
    }
    //------------------------------------------------------------

    /**
     * I think this is old 00/05/10 aa.
     * Select this icon as a whole if the point (x,y) is within the scope of this
     * icon.
     *
     * @return <code>true</code> if the point (x,y) is within the scope of this
     * icon
     * <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y) {
        //System.out.println("Debug:Icon:Select called in icon with x " + x + "y: " + y);
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }
    //------------------------------------------------------------

    /**
     * I think this is old 00/05/10 aa.
     * Select this icon as a whole if this icon is completely within the rectangle
     * represented by the left-up corner (x0,y0) and right-down corner (x1,y1).
     *
     * @return <code>true</code> if this icon is completely within the
     * rectangle represented by the left-up corner (x0,y0) and
     * right-down corner (x1,y1)
     * <code>false</code> otherwise
     */
    public boolean selectrect(int x0, int y0, int x1, int y1) {
        if (x0 < xmin && x1 > xmax && y0 < ymin && y1 > ymax) {
            select = 1;
            return (true);
        }
        return (false);
    }
    //------------------------------------------------------------

    /**
     * Calculate the values of xmin, ymin, xmax, and ymax.
     */
    public void setminmax() {
        // this should work, even though the initial values stored
        // were only the first point, if drawableParts is storing
        // references to the graphic_obj, which gets continuously
        // updated.

        int ix;
        GraphicPart part;

        xmin = ymin = 10000;
        xmax = ymax = -10000;
        shapexmin = shapeymin = 10000;//99/4/14 added part
        shapexmax = shapeymax = -10000;

        //System.out.println("Debug:Icon: setminmax() begin");
        if (drawableParts != null) {
            for (ix = 0; ix < drawableParts.size(); ix++) {
                part = (GraphicPart) drawableParts.elementAt(ix);
                setminmax(part); // this will be slower
            }
        }
        //System.out.println("Debug:Icon: setminmax() end");
    }

    //-----------------------------------------------------------------
    // aa: adding this method to reset component min/max
    // using the graphics object.
    //-----------------------------------------------------------------
    public void setminmax(GraphicPart gobj) {
        //String strtemp1;
        // over all dimensions including ports
        xmin = Math.min(xmin, gobj.xmin);
        ymin = Math.min(ymin, gobj.ymin);
        xmax = Math.max(xmax, gobj.xmax);
        ymax = Math.max(ymax, gobj.ymax);
        //System.out.println("Debug:Icon:setminmax(gobj):gobj.xmin:"+gobj.xmin+" gobj.ymin:"+gobj.ymin+" gobj.xmax:"+gobj.xmax+" gobj.ymax:"+gobj.ymax);
        //System.out.println("Debug:Icon:setminmax(gobj):xmin "+xmin+" ymin "+ymin+" xmax "+xmax+" ymax "+ymax);
        //strtemp1=gobj.getClass().getName();
        //System.out.print("Debug:Icon: "+strtemp1+ " ");
        if ((!(gobj instanceof IconInport)) && (!(gobj instanceof IconOutport))) {
            //do not include ports in boundaries of shape
            shapexmin = Math.min(shapexmin, gobj.xmin);
            shapeymin = Math.min(shapeymin, gobj.ymin);
            shapexmax = Math.max(shapexmax, gobj.xmax);
            shapeymax = Math.max(shapeymax, gobj.ymax);
        }


        //System.out.println("Debug:Icon:setminmax(gobj):shapedim:"+shapexmin+" "+shapeymin+" "+shapexmax+" "+shapeymax);
    }
    //-------------------------------------------------------------------------

    /**
     * Write this icon to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */

    public void write(ObjectOutputStream os, boolean xminZeroed)
            throws IOException {
        setminmax();  //todo: is this necessary? could be
        int sxmin = xmin; //saved xmin
        int symin = ymin;

        try {
            os.writeUTF(libNickName); // this is only need for the icons
            // within the schematics list of icons
            os.writeUTF(moduleName);
            os.writeUTF(versionName);
            if (xminZeroed) {
                os.writeInt(0);  // zero
                os.writeInt(0);  // zero
                os.writeInt(xmax - xmin);  // this use to be xdelta=xmax-xmin
                os.writeInt(ymax - ymin);  // this use to be ydelta=ymax-ymin
                os.writeInt(shapexmin - xmin);
                os.writeInt(shapeymin - ymin);
                os.writeInt(shapexmax - xmin);
                os.writeInt(shapeymax - ymin);

                //System.out.println("Debug:Icon:write: xdelta "+xdelta+" ydelta "+ydelta);
            } else {
                os.writeInt(xmin);  // non-zero
                os.writeInt(ymin);  // non-zero
                os.writeInt(xmax);  // this use to be xdelta=xmax-xmin
                os.writeInt(ymax);  // this use to be ydelta=ymax-ymin
                os.writeInt(shapexmin);
                os.writeInt(shapeymin);
                os.writeInt(shapexmax);
                os.writeInt(shapeymax);
            }
            int ix;
            GraphicPart part;
            if (drawableParts == null) {
                os.writeInt(0);
            } else {
                os.writeInt(drawableParts.size());
                for (ix = 0; ix < drawableParts.size(); ix++) {
                    // GraphicParts are always repositioned to 0,0
                    part = (GraphicPart) drawableParts.elementAt(ix);
                    if (part instanceof IconInport) {
                        os.writeUTF("Inport");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof IconOutport) {
                        os.writeUTF("Outport");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof GraphicPart_line) {
                        os.writeUTF("Line");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof GraphicPart_rect) {
                        os.writeUTF("Rect");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof GraphicPart_oval) {
                        os.writeUTF("Oval");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof GraphicPart_poly) {
                        os.writeUTF("Poly");
                        part.write(os, sxmin, symin);
                    } else if (part instanceof GraphicPart_text) {
                        //System.out.println("Writing text fields..");
                        os.writeUTF("Text");
                        part.write(os, sxmin, symin);
                    } else {
                        System.err.println("Error:Icon:write: unknown part type.");
                        throw new IOException("Icon:write: unknown part type.");
                    }
                }
            }//end if
        } catch (IOException e) {
            System.err.println("Error:Icon: write IOException");
            throw new IOException("Icon write IOException");
        }
    }

    //-------------------------------------------------------------------------

    /**
     * WriteAllChars this icon to the output stream os.
     * When saveing the icon, we zero out the xmin and ymin.
     * When saveing icon instances we do not.
     */

    public void writeAllChars(PrintWriter pw, boolean xminZeroed) {
        //setminmax();  //did above in write
        int sxmin = xmin; //saved xmin
        int symin = ymin; //saved ymin


        pw.print("\n");
        pw.print("icon: "); // this is only need for the icons
        pw.print("\n");
        pw.print("libNickName: "); // this is only need for the icons
        pw.print(libNickName); // this is only need for the icons
        pw.print("\n");
        // within the schematics list of icons
        pw.print("moduleName: "); // this is only need for the icons
        pw.print(moduleName);
        pw.print("\n");
        pw.print("versionName: "); // this is only need for the icons
        pw.print(versionName);
        pw.print("\n");
        if (xminZeroed) {
            pw.print("xmin: ");
            pw.print("0");  // zero
            pw.print("\n");
            pw.print("ymin: ");
            pw.print("0");  // zero
            pw.print("\n");
            pw.print("xmax: ");
            pw.print((Integer.valueOf(xmax - xmin)).toString());  // this use to be xdelta=xmax-xmin
            pw.print("\n");
            pw.print("ymax: ");
            pw.print((Integer.valueOf(ymax - ymin)).toString());  // this use to be ydelta=ymax-ymin
            pw.print("\n");
            pw.print("shapexmindelta: ");
            pw.print((Integer.valueOf(shapexmin - xmin)).toString());
            pw.print("\n");
            pw.print("shapeymindelta: ");
            pw.print((Integer.valueOf(shapeymin - ymin)).toString());
            pw.print("\n");
            pw.print("shapexmaxdelta: ");
            pw.print((Integer.valueOf(shapexmax - xmin)).toString());
            pw.print("\n");
            pw.print("shapeymaxdelta: ");
            pw.print((Integer.valueOf(shapeymax - ymin)).toString());
            pw.print("\n");
        } else {
            pw.print("xmin: ");
            pw.print((Integer.valueOf(xmin)).toString());  //
            pw.print("\n");
            pw.print("ymin: ");
            pw.print((Integer.valueOf(ymin)).toString());  //
            pw.print("\n");
            pw.print("xmax: ");
            pw.print((Integer.valueOf(xmax)).toString());  // this use to be xdelta=xmax-xmin
            pw.print("\n");
            pw.print("ymax: ");
            pw.print((Integer.valueOf(ymax)).toString());  // this use to be ydelta=ymax-ymin
            pw.print("\n");
            pw.print("shapexmindelta: ");
            pw.print((Integer.valueOf(shapexmin)).toString());
            pw.print("\n");
            pw.print("shapeymindelta: ");
            pw.print((Integer.valueOf(shapeymin)).toString());
            pw.print("\n");
            pw.print("shapexmaxdelta: ");
            pw.print((Integer.valueOf(shapexmax)).toString());
            pw.print("\n");
            pw.print("shapeymaxdelta: ");
            pw.print((Integer.valueOf(shapeymax)).toString());
            pw.print("\n");
        }
        //System.out.println("Debug:Icon:write: xdelta "+xdelta+" ydelta "+ydelta);

        int ix;
        GraphicPart part;
        pw.print("Num Of Drawable Parts: ");
        if (drawableParts == null) {
            pw.print("0");
            pw.print("\n");
        } else {
            pw.print((Integer.valueOf(drawableParts.size())).toString());
            pw.print("\n");
            // drawable parts are always moved to 0,0
            for (ix = 0; ix < drawableParts.size(); ix++) {
                pw.print("DrawablePart: "); //  write down type of port
                part = (GraphicPart) drawableParts.elementAt(ix);
                if (part instanceof IconInport) {
                    pw.print("InportPin: ");
                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof IconOutport) {
                    pw.print("OutportPin: ");
                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof GraphicPart_line) {
                    pw.print("Line: ");
                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof GraphicPart_rect) {
                    pw.print("Rect: ");
                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof GraphicPart_oval) {
                    pw.print("Oval: ");

                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof GraphicPart_poly) {
                    pw.print("Poly: ");

                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else if (part instanceof GraphicPart_text) {
                    //System.out.println("Debug:Icon:Writing text fields..");
                    pw.print("Text: ");
                    part.writeAllChars(pw, sxmin, symin);
                    pw.print("\n");
                } else {
                    System.err.println("Icon:writeAllChars: unknown part type.");
                }

            }
        }//end if

    }
    //-------------------------------------------------------------------------

    /**
     * Read this icon from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        try {
            libNickName = os.readUTF(); // this is only need for the icons
            // within the schematics list of icons
            moduleName = os.readUTF();
            versionName = os.readUTF();
            if (debug) {
                System.out.println("Debug:Icon:libNickName: " + libNickName);
                System.out.println("Debug:Icon:moduleName: " + moduleName);
                System.out.println("Debug:Icon:versionName: " + versionName);
            }
            xmin = os.readInt();
            ymin = os.readInt();
            xmax = os.readInt();
            ymax = os.readInt();
            shapexmin = os.readInt();
            shapeymin = os.readInt();
            shapexmax = os.readInt();
            shapeymax = os.readInt();

            if (debug) {
                System.out.println("Debug:Icon:xmin ymin xmax ymax: " + xmin + " " + ymin + " " + xmax + " " + ymax);
                System.out.println("Debug:Icon:shapexmin shapeymin shapexmax shapeymax: " + shapexmin + " " + shapeymin + " " + shapexmax + " " + shapeymax);

            }

            moduleLabel = new GraphicPart_text(OptionsFrame.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax, moduleName, OptionsFrame.moduleTextFont, OptionsFrame.moduleText_col, OptionsFrame.moduleTextSize);


            //System.out.println("Debug:Icon:write: xmin "+xmin+" ymin "+ymin);
            //System.out.println("Debug:Icon:read: xmax "+xmax+" ymax "+ymax);
            select = 0;

            if ((drawableParts != null) && (drawableParts.size() != 0)) {
                drawableParts.removeAllElements();
            }
            int ix;
            int n = os.readInt();  // read number of drawableParts
            String str;
            IconInport inport;
            IconOutport outport;
            GraphicPart_line line;
            GraphicPart_rect rect;
            GraphicPart_oval oval;
            GraphicPart_poly poly;
            GraphicPart_text text;

            for (ix = 0; ix < n; ix++) {
                str = os.readUTF();
                switch (str) {
                    case "Inport":
                        inport = new IconInport();
                        inport.read(os);
                        drawableParts.addElement(inport);
                        break;
                    case "Outport":
                        outport = new IconOutport();
                        outport.read(os);
                        drawableParts.addElement(outport);
                        break;
                    case "Line":
                        line = new GraphicPart_line();
                        line.read(os);
                        drawableParts.addElement(line);
                        break;
                    case "Rect":
                        rect = new GraphicPart_rect();
                        rect.read(os);
                        drawableParts.addElement(rect);
                        break;
                    case "Oval":
                        oval = new GraphicPart_oval();
                        oval.read(os);
                        drawableParts.addElement(oval);
                        break;
                    case "Poly":
                        poly = new GraphicPart_poly();
                        poly.read(os);
                        drawableParts.addElement(poly);
                        break;
                    case "Text":
                        text = new GraphicPart_text();
                        text.read(os);
                        drawableParts.addElement(text);
                        break;
                    default:
                        System.err.println("Icon:read: unknown part type.");
                        throw new IOException("Icon:read: unknown part type.");
                }

            }
        } catch (ClassNotFoundException e) {
            System.err.println("Icon read ClassNotFoundException");
            throw new ClassNotFoundException("Icon read ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Icon read IOException");
            throw new IOException("Icon read IOException");
        }
    }

}//end class Icon










