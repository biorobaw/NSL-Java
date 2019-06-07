/* SCCS %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.  
// Copyright: This software may be freely copied provided the toplevel 
// Copyright: COPYRIGHT file is included with each such copy.  
// Copyright: Email nsl@java.usc.edu.

/**
 * Schematic - A class representing the schematic structure of a module. It's
 * composed of all those drawableObjs of a schematic representation, including
 * modules, schematic inports, schematic outports, and connections.
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @param drawableObjs    an array of drawableObjs of this schematic,
 * each
 * component being either a IconInst,
 * SchematicInport, SchematicOutport,
 * or a Connection
 * @param selComponent    pointing to the selected component or
 * interconnect of this schematic
 * @since JDK8
 * <p>
 * Note: in version 1.4 we have tried to make the schematic a stand alone entity
 * which contains a list of drawable objects some of which are IconInsts which
 * point to Icons within the  schematics Icon list.
 */

import java.awt.*;
import java.util.Vector;
import java.io.*;

class Schematic {
    // protected Vector icons; add if trying to be efficient on space
    protected Vector drawableObjs;
    boolean schModified = false;
    Component selComponent = null; //can be any drawable object
    //selComponent used in SchematicPanel.java

    /**
     * Constructor of this class with no parameters.
     */
    public Schematic() {
        drawableObjs = new Vector();
    }

    /**
     * Constructor of this class, which is constructed out of an existing 
     * schematic.
     */
    public Schematic(Schematic schematicToCopy) {
        Component tempComponent;
        drawableObjs = new Vector();

        if (schematicToCopy == null) {
            System.err.println("Error:Schematic:constructor: schematic to copy is null.");
            return;
        }

        if (schematicToCopy.drawableObjs != null) {
            for (int ix = 0; ix < schematicToCopy.drawableObjs.size(); ix++) {
                tempComponent = (Component) schematicToCopy.drawableObjs.elementAt(ix);
                drawableObjs.addElement(tempComponent);
            }
        }
    }
//----------------------------------------------------------------------------
    /**
     * find an icon in the list of Icons associated with this schematic 
     *
     * @param     libNickName    Alias name of library this icon came from
     *
     * @param     moduleName    name of module this iconInst is related to
     *
     * @param     versionName    version of module this iconInst is
     *
     * This method is obe.  We are not using it any more !!!!!!!!!!!
     */
/*
    public Icon findIcon(String libNickName,String moduleName, String versionName) {
	Icon tempIcon=null;

	if (icons!=null) {	
        for (int ix = 0; ix < icons.size(); ix++) {
           	tempIcon = (Icon)icons.elementAt(ix);

		if ((tempIcon.libNickName.equals(libNickName)==true) &&
		(tempIcon.moduleName.equals(moduleName)==true) &&
		(tempIcon.versionName.equals(versionName)==true)) {
			return(tempIcon);
		}
	}
        }
	return(null);
    }
*/
//----------------------------------------------------------------
    /**
     * find another an iconInst in the list of drawableObjects associated with this schematic 
     * which is not this iconInst
     *
     * @param     libNickName    Alias name of library this icon came from
     *
     * @param     moduleName    name of module this iconInst is related to
     *
     * @param     versionName    version of module this iconInst is
     *
     */
/* This method is obe
public IconInst findAnotherIconInst(IconInst iconInst) {
	Component tempDrawable=null;
	IconInst tempIconInst=null;
	Icon tempIcon=null;

	if (drawableObjs!=null) {	
        for (int ix = 0; ix < drawableObjs.size(); ix++) {
		tempDrawable=(Component)drawableObjs.elementAt(ix);
		if (tempDrawable instanceof IconInst) {
			tempIconInst=(IconInst)tempDrawable;
			// maybe could pass in index instead
			if ((tempIconInst!=iconInst) &&
			    (tempIconInst.iconTemplate=iconInst.iconTemplate)) {
				return(tempIconInst);
			}
		}
	}
	}
	return(null);
}
*/

    /**
     * Delete a iconInst from this schematic called from deleteDrawableObj
     *
     * @param    iconInst    the icon to be deleted from this schematic
     */
    public void deleteIconInst(IconInst iconInst) {
        // test if there are more icons of this type
        // if yes, then do not delete the icon from icons
        // if no, then delete the icon from icons

    }
//---------------------------------------------------------------

    /**
     * Add a drawableObj to this schematic.
     *
     * @param    newComponent    the component to be added to this schematic
     */

    public void addDrawableObj(Component newComponent) {
        schModified = true;
        drawableObjs.addElement(newComponent);
    }
    //---------------------------------------------------------------

    /**
     * findDrawableIndex
     * @param    name    the iconInst selected
     */

    public int findDrawableIndex(String name) {
        int found = -1;

        for (int i = 0; i < drawableObjs.size(); i++) {
            if (drawableObjs.elementAt(i) instanceof IconInst) {
                IconInst foo = (IconInst) drawableObjs.elementAt(i);
                if (foo.instanceName.equals(name)) {
                    found = i;
                    return (found);
                }
            }
            if (drawableObjs.elementAt(i) instanceof SchematicInport) {
                SchematicInport foo = (SchematicInport) drawableObjs.elementAt(i);
                if (foo.Name.equals(name)) {
                    found = i;
                    return (found);
                }
            }
            if (drawableObjs.elementAt(i) instanceof SchematicOutport) {
                SchematicOutport foo = (SchematicOutport) drawableObjs.elementAt(i);
                if (foo.Name.equals(name)) {
                    found = i;
                    return (found);
                }
            }
            //    if (drawableObjs.elementAt(i) instanceof Connection) {
            //currently connections do not have names
            //}
        }
        return (found);
    }

    //---------------------------------------------------------------

    /**
     * Add a new IconInst to this schematic, and add a new icon
     * to the list of Icons if necessary.
     *
     * @param    newIconInst    the iconInst selected
     */

    public boolean addIconInst(IconInst newIconInst) {

        if (newIconInst == null) {
            System.err.println("Error:Schematic:addIconInst: newIconInst is null ");
            return false;
        }
        if (newIconInst.instanceName.equals("")) {
            System.err.println("Error:Schematic:addIconInst: instance name is blank");
            return false;
        }
        int dup = findDrawableIndex(newIconInst.instanceName);
        if (dup != -1) {
            return false;
        } else {
            addDrawableObj(newIconInst);
            return true;
        }

    }


    //----------------------------------------------------------------

    /**
     * Delete a drawableObj from this schematic.
     *
     * @param    tempComponent    the component to be deleted from this schematic
     */
    public void deleteDrawableObj(Component tempComponent) {
        schModified = true;
        Connection conn;

        // connection
        if (tempComponent instanceof Connection) {
            conn = (Connection) tempComponent;

            if (conn.src_iconOrSchPort instanceof IconInst)
                conn.src_port.disconnect(conn);
            if (conn.src_iconOrSchPort instanceof SchematicInport)
                ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);

            if (conn.dest_iconOrSchPort != null) {
                if (conn.dest_iconOrSchPort instanceof IconInst)
                    conn.dest_port.disconnect();
                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                    ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
            }

            drawableObjs.removeElement(tempComponent);
        }
        // SchematicInport
        if (tempComponent instanceof SchematicInport) {
            SchematicInport sip = (SchematicInport) tempComponent;

            for (int i = 0; i < sip.links.size(); i++) {
                conn = sip.links.elementAt(i);

                if (conn.dest_iconOrSchPort != null) {
                    if (conn.dest_iconOrSchPort instanceof IconInst)
                        conn.dest_port.disconnect();
                    if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                        ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                }
                sip.disconnect(conn);
                drawableObjs.removeElement(conn);
            }

            drawableObjs.removeElement(tempComponent);
        }
        // SchematicOutport
        if (tempComponent instanceof SchematicOutport) {
            SchematicOutport sop = (SchematicOutport) tempComponent;

            if (sop.link != null) {
                conn = sop.link;

                if (conn.src_iconOrSchPort instanceof IconInst)
                    conn.src_port.disconnect(conn);
                if (conn.src_iconOrSchPort instanceof SchematicInport)
                    ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);

                sop.disconnect();
                drawableObjs.removeElement(conn);
            }

            drawableObjs.removeElement(tempComponent);
        }
        // Text
        if (tempComponent instanceof GraphicPart_text) {
            //System.out.println("Debug:Schematic:Deleting text object hopefully..");
            drawableObjs.removeElement(tempComponent);
        }

        // IconInst
        if (tempComponent instanceof IconInst) {
            Vector iconsDrawables = ((IconInst) tempComponent).drawableParts;
            if (iconsDrawables != null) {
                for (int i = 0; i < iconsDrawables.size(); i++) {
                    GraphicPart gobj = (GraphicPart) iconsDrawables.elementAt(i);
                    if (gobj instanceof IconInport) {
                        IconInport ip = (IconInport) gobj;
                        conn = ip.link;
                        if (conn != null) {
                            if (conn.src_iconOrSchPort instanceof IconInst)
                                conn.src_port.disconnect(conn);
                            if (conn.src_iconOrSchPort instanceof SchematicInport)
                                ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);

                            if (conn.dest_port != null)
                                conn.dest_port.disconnect();
                            drawableObjs.removeElement(conn);
                        }
                    }
                    if (gobj instanceof IconOutport) {
                        IconOutport op = (IconOutport) gobj;
                        for (int j = 0; j < op.links.size(); j++) {
                            conn = op.links.elementAt(j);
                            if (conn.dest_iconOrSchPort != null) {
                                if (conn.dest_iconOrSchPort instanceof IconInst)
                                    conn.dest_port.disconnect();
                                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                                    ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                            }
                            if (conn.src_port != null)
                                conn.src_port.disconnect(conn);
                            drawableObjs.removeElement(conn);
                        }
                    }
                } //end for (int i=0; i<iconsDrawables.size(); i++)
                drawableObjs.removeElement(tempComponent);

            } //end if iconsDrawables!=null
        } //end if instanceof IconInst
    } //end method
//----------------------------------------------------------------------------

    /**
     * Delete all drawableObjs from this schematic.
     */
    public void deleteAllDrawableObjs() {
        drawableObjs.removeAllElements();
    }
//----------------------------------------------------------------------------

    /**
     * Push the ix'th component of this schematic to the last of the drawableObjs 
     * array. Since we are drawing the drawableObjs in the order that they're in 
     * the array, so being the last component means it will be the last to be 
     * painted, so appears in the top position.
     *
     * @param       ix      the index of the component in the array that's gonna
     *                      be pushed to the top
     */
    public void pushtop(int ix) {
        Component tempComponent;

        tempComponent = (Component) drawableObjs.elementAt(ix);
        drawableObjs.removeElementAt(ix);
        drawableObjs.addElement(tempComponent);
    }
//--------------------------------------------------------------------------------

    /**
     * Paint this schematic.
     */
    public void paint(Graphics g) { //use to be called paint
        Component tempComponent;

        if (drawableObjs != null) {
            for (int ix = 0; ix < drawableObjs.size(); ix++) {
                tempComponent = (Component) drawableObjs.elementAt(ix);
                tempComponent.paint(g);
            }
        }
    }
//-----------------------------------------------------------------------------

    /**
     * Write this schematic to the output stream oos.
     *
     * @exception FileNotFoundException    if a file-not-found error
     *						occurred
     * @exception IOException        if an IO error occurred
     */
    public void write(ObjectOutputStream oos)
            throws IOException {

        int compCount = 0;

        try {
            if (drawableObjs != null) {
                // count the number of drawableObjs that are not connections
                for (int i = 0; i < drawableObjs.size(); i++) {
                    Component comp = (Component) drawableObjs.elementAt(i);

                    if (!(comp instanceof Connection)) compCount++;
                }
            }

            oos.writeInt(compCount); //  write down the component count

            if (drawableObjs != null) {
                for (int i = 0; i < drawableObjs.size(); i++) {
                    Component comp = (Component) drawableObjs.elementAt(i);

                    if (comp instanceof SchematicInport) {
                        oos.writeInt(1);  //SchematicInport
                        ((SchematicInport) comp).write(oos);
                    }

                    if (comp instanceof SchematicOutport) {
                        oos.writeInt(2);  //SchematicOutport
                        ((SchematicOutport) comp).write(oos);
                    }

                    if (comp instanceof GraphicPart_text) {
                        oos.writeInt(4);  //Graphic text

                        ((GraphicPart_text) comp).write(oos, 0, 0);
                    }

                    // IconInst

                    if (comp instanceof IconInst) {
                        IconInst iconInst = (IconInst) comp;
                        oos.writeInt(3);  //IconInst  code
                        ((IconInst) comp).write(oos);
                    }

                }
            }

            //begin to write Connection
            if (drawableObjs == null) {
                oos.writeInt(0);
            } else {
                //begin to write number of Connection
                oos.writeInt(drawableObjs.size() - compCount);

                for (int i = 0; i < drawableObjs.size(); i++) {
                    Component comp = (Component) drawableObjs.elementAt(i);
                    if (comp instanceof Connection) {
                        Connection conn = (Connection) comp;
                        conn.write(oos);
                        writePortSrcConnection(oos, conn);
                        writePortDestConnection(oos, conn);
                    } //end if
                } //end for
            } //end else
        } //end try
        catch (FileNotFoundException e) {
            System.err.println("Error:Schematic:write: FileNotFoundException");
            throw new FileNotFoundException("Schematic IconInst write: FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Schematic:write: IOException");
            throw new IOException("Schematic write: IOException");
        }
    }

    //------------------------------------------------------------------

    /**
     * Write Port Source Connection List
     */
    //------------------------------------------------------------------
    public void writePortSrcConnection(ObjectOutputStream oos, Connection conn) throws IOException {

        try {
            if (conn.src_iconOrSchPort == null) {
                oos.writeInt(0);
                return;
            }
            oos.writeInt(1); // exists a src
            if (conn.src_iconOrSchPort instanceof SchematicInport) {
                //System.out.println("Debug:Schematic:writePortSrc: schin");
                oos.writeInt(1);
                oos.writeUTF(((SchematicInport) conn.src_iconOrSchPort).Name);
                return;
            }
            if (conn.src_iconOrSchPort instanceof IconInst) {
                //System.out.println("Debug:Schematic:writePortSrc: iconinst");
                oos.writeInt(2);
                oos.writeUTF(((IconInst) conn.src_iconOrSchPort).instanceName);

                //write src_port's index
                Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
                if (iconsDrawables != null) {
                    for (int j = 0; j < iconsDrawables.size(); j++) {
                        Component icon_comp = (Component) iconsDrawables.elementAt(j);

                        if (icon_comp instanceof IconOutport) {
                            if (((IconOutport) icon_comp).Name.
                                    equals(conn.src_port.Name)) {
                                oos.writeInt(j);
                                oos.writeUTF(conn.src_port.Name);
                                break;
                            }
                        }
                    }//end for
                }
            }
        } //end try
        catch (IOException e) {
            System.err.println("Error:Schematic:writePortSrcConnection: IOException");
            throw new IOException("Schematic writePortSrcConnection: IOException");
        }
    } //end writePortSrcConnection
    //------------------------------------------------------------------

    /**
     * Write Port Destination Connection List
     */
    //------------------------------------------------------------------
    public void writePortDestConnection(ObjectOutputStream oos, Connection conn) throws IOException {
        try {
            if (conn.dest_iconOrSchPort == null) {
                oos.writeInt(0); //no destination exists
                return;
            }
            oos.writeInt(1); //yes destination does exists
            if (conn.dest_iconOrSchPort instanceof SchematicOutport) {
                //System.out.println("Debug:Schematic:writePortDest: schout");
                oos.writeInt(1); //type
                oos.writeUTF(((SchematicOutport) conn.dest_iconOrSchPort).Name);
                return;
            }
            if (conn.dest_iconOrSchPort instanceof IconInst) {
                //System.out.println("Debug:Schematic:writePortDest: iconinst");
                oos.writeInt(2); //type
                oos.writeUTF(((IconInst) conn.dest_iconOrSchPort).instanceName);

                //write dest_port's index
                Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
                if (iconsDrawables != null) {
                    for (int j = 0; j < iconsDrawables.size(); j++) {
                        Component icon_comp = (Component) iconsDrawables.elementAt(j);

                        if (icon_comp instanceof IconInport) {
                            IconInport foo = (IconInport) icon_comp;
                            //System.out.println("Debug:Schematic:writePortDest:iconInport? "+foo.Name);
                            if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name)) {
                                oos.writeInt(j);
                                oos.writeUTF(conn.dest_port.Name);
                                break;
                            }
                        }
                    }
                }//end iconsDrawables!=null
            } //IconInst
        } //end try
        catch (IOException e) {
            System.err.println("Error:Schematic:writePortDestConnection: IOException");
            throw new IOException("Schematic writePortDestConnection: IOException");
        }
    } //end writePortDestConnection

    //-------------------------------------------------------------------------

    /**
     * WriteAllChars format for dup of schematic to the PrintWriter pw.
     */
    public void writeAllChars(PrintWriter pw) {
        //throws FileNotFoundException {
        int compCount = 0;
        pw.print("\n");
        if (drawableObjs != null) {
            // count the number of drawableObjs that are not connections
            for (int i = 0; i < drawableObjs.size(); i++) {
                Component comp = (Component) drawableObjs.elementAt(i);
                if (!(comp instanceof Connection)) compCount++;
            }
        }
        pw.print("Num Drawable Objs: "); //  write down the num of icons
        pw.print((new Integer(drawableObjs.size())).toString()); //  write down the num of drawableObjs
        pw.print("\n"); //
        pw.print("Num Drawable Components: "); //
        pw.print((new Integer(compCount)).toString()); //
        pw.print("\n"); //

        if (drawableObjs != null) {
            for (int i = 0; i < drawableObjs.size(); i++) {
                pw.print("DrawableObj Num: "); //  write down type of port
                pw.print((new Integer(i)).toString());
                pw.print("\n");
                Component comp = (Component) drawableObjs.elementAt(i);

                if (comp instanceof SchematicInport) {
                    pw.print("SchInPort: "); //  write down type of port
                    ((SchematicInport) comp).writeAllChars(pw);
                }

                if (comp instanceof SchematicOutport) {
                    pw.print("SchOutPort: "); //  write down type of port
                    ((SchematicOutport) comp).writeAllChars(pw);
                }

                if (comp instanceof GraphicPart_text) { // nitgupta
                    pw.print("GraphicText: "); //  write down type of drawable
                    ((GraphicPart_text) comp).writeAllChars(pw, 0, 0);
                }

                // IconInst
                if (comp instanceof IconInst) {
                    IconInst iconInst = (IconInst) comp;
                    pw.print("IconInst: "); //  write down type of drawable
                    ((IconInst) comp).writeAllChars(pw);
                }

            }
        }
        pw.print("\n");
        pw.print("Num Drawable Connections: "); //  write down type of drawable
        //begin to write Connection
        if (drawableObjs == null) {
            pw.print("0");
            pw.print("\n"); //  write down type of connections
        } else {
            pw.print((new Integer(drawableObjs.size() - compCount)).toString()); //  write down the num of drawableObjs
            pw.print("\n"); //  write down type of connections

            for (int i = 0; i < drawableObjs.size(); i++) {
                Component comp = (Component) drawableObjs.elementAt(i);
                if (comp instanceof Connection) {
                    pw.print("\n");
                    pw.print("Connection Num:");
                    pw.print((new Integer(i)).toString());
                    pw.print("\n");
                    Connection conn = (Connection) comp;
                    conn.writeAllChars(pw);
                    writeAllCharsPortSrcConnection(pw, conn);
                    writeAllCharsPortDestConnection(pw, conn);
                } //end if
            } //end for
        } //end else
    } //end writeAllChars
    //------------------------------------------------------------------

    /**
     * WriteAllChars Port Source Connection List
     */
    //------------------------------------------------------------------
    public void writeAllCharsPortSrcConnection(PrintWriter pw, Connection conn) {
        pw.print("PortSrcConnection num:");
        if (conn.src_iconOrSchPort == null) {
            pw.print("0");
            pw.print("\n");
            return;
        }
        pw.print("1");
        pw.print("\n");
        if (conn.src_iconOrSchPort instanceof SchematicInport) {
            //System.out.println("Debug:Schematic:writeAllCharsPortSrc: schin");
            pw.print("SchInport: ");
            pw.print(((SchematicInport) conn.src_iconOrSchPort).Name);
            pw.print("\n");
        }

        if (conn.src_iconOrSchPort instanceof IconInst) {
            //System.out.println("Debug:Schematic:writeAllCharsPortSrc: iconinst");
            pw.print("IconInst: ");
            pw.print(((IconInst) conn.src_iconOrSchPort).instanceName);
            pw.print("\n");
            //write src_port's index
            Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
            if (iconsDrawables != null) {
                for (int j = 0; j < iconsDrawables.size(); j++) {
                    Component icon_comp = (Component) iconsDrawables.elementAt(j);

                    if (icon_comp instanceof IconOutport) {
                        if (((IconOutport) icon_comp).Name.
                                equals(conn.src_port.Name)) {
                            pw.print("pin num: ");
                            pw.print((new Integer(j)).toString()); //  write down the num of drawableObjs
                            pw.print(" ");
                            pw.print("conn.src_port.Name:");
                            pw.print(conn.src_port.Name);
                            pw.print("\n");
                            break;
                        }
                    }
                }
            }
        }
    } //end writeAllCharsPortSrcConnection
    //------------------------------------------------------------------

    /**
     * WriteAllChars Port Destination Connection List
     */
    //------------------------------------------------------------------
    public void writeAllCharsPortDestConnection(PrintWriter pw, Connection conn) {
        pw.print("PortDestConnection num:");

        if (conn.dest_iconOrSchPort == null) {
            pw.print("0");
            pw.print("\n");
            return;
        }
        pw.print("1");
        pw.print("\n");
        if (conn.dest_iconOrSchPort instanceof SchematicOutport) {
            //System.out.println("Debug:Schematic:writeAllCharsPortDest: schout");
            pw.print("SchOutPort: ");
            pw.print(((SchematicOutport) conn.dest_iconOrSchPort).Name);
            pw.print("\n");
            return;
        }
        if (conn.dest_iconOrSchPort instanceof IconInst) {
            //System.out.println("Debug:Schematic:writeAllCharsPortDest: iconinst");
            pw.print("IconInst: ");
            pw.print(((IconInst) conn.dest_iconOrSchPort).instanceName);
            pw.print("\n");

            //write dest_port's index
            Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
            if (iconsDrawables != null) {
                for (int j = 0; j < iconsDrawables.size(); j++) {
                    Component icon_comp = (Component) iconsDrawables.elementAt(j);

                    if (icon_comp instanceof IconInport) {
                        //    IconInport foo=(IconInport)icon_comp;
                        //System.out.println("Debug:Schematic:writeAllCharsPortDest:iconInport? "+foo.Name);
                        if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name)) {
                            pw.print("pin num: ");
                            pw.print((new Integer(j)).toString());
                            pw.print(" ");
                            pw.print("conn.dest_port.Name:");
                            pw.print(conn.dest_port.Name);
                            pw.print("\n");
                            break;
                        }
                    }
                }
            } //end if (iconsDrawables)
            pw.print("\n");
        } //end if (conn.des_conOrSchPort)
    } //end writeAllCharsPortDestConnection

    //------------------------------------------------------------------

    /**
     * Read this schematic from the input stream oos.
     *
     * @exception FileNotFoundException    if a file-not-found error
     *						occurred
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *						occurred
     */
    //------------------------------------------------------------------
    public void read(ObjectInputStream ois) throws IOException, ClassNotFoundException {

        Module module = null;
        Icon icon = null;
        int n = 0;

        try {
            selComponent = null;

            if ((drawableObjs != null) && (drawableObjs.size() != 0)) {
                drawableObjs.removeAllElements();
            }

            // DRAWABLES
            int numDrawables = ois.readInt();
            //System.out.println("Debug:Schematic:read:numDrawables: "+numDrawables);

            for (int i = 0; i < numDrawables; i++) {
                int typeOfObj = ois.readInt();

                switch (typeOfObj) {

                    case 1: //SchematicInport
                        SchematicInport sip = new SchematicInport();
                        sip.read(ois);  //throws ClassNotFoundException
                        drawableObjs.addElement(sip);
                        break;

                    case 2: //SchematicOutport
                        SchematicOutport sop = new SchematicOutport();
                        sop.read(ois);
                        drawableObjs.addElement(sop);
                        break;

                    case 4: // text string

                        //	    System.out.println("Text type being read");
                        GraphicPart_text text = new GraphicPart_text();
                        text.read(ois);
                        drawableObjs.addElement(text);
                        break;

                    case 3: //IconInst -
                        IconInst iconInst = new IconInst();
                        iconInst.read(this, ois);  //should catch exceptions
                        drawableObjs.addElement(iconInst);
                        //System.out.println("Debug:Schematic:read:iconinst "+iconInst.instanceName);
                        break;

                    default: //error
                        System.err.println("Error:Schematic:read unknown type of object");
                        break;
                }//end switch
            } //for (i=0; i<numDrawables; i++);

            //begin to read Connections

            int numConnections = ois.readInt(); //throws IOException
            //System.out.println("Debug:Schematic:read:connections: "+numConnections);
            for (int ic = 0; ic < numConnections; ic++) {
                Connection conn = new Connection();
                //System.out.println("Debug:Schematic:Read: before conn read.");
                conn.read(ois);
                //System.out.println("Debug:Schematic:Read: after conn read.");
                readPortSrcConnection(ois, conn);
                //System.out.println("Debug:Schematic:Read: after readPortSrcConn read.");
                //todo: eventually want to have multiple destinations on a connection.
                //todo: Right now you have to start over from the src and lay the
                //todo: interconnect on top of another interconnect.
                readPortDestConnection(ois, conn);
                //System.out.println("Debug:Schematic:Read: after readPortDestConn read.");
                drawableObjs.addElement(conn);
                //System.out.println("Debug:Schematic:read:addConnection");
            } //end (ic=0 ; i<numConnections; ic++)
        } //end try
        catch (IOException e) {
            System.err.println("Error:Schematic:read: IOException");
            throw new IOException("Schematic read IOException");
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Schematic:Read: ClassNotFoundException");
            throw new ClassNotFoundException("Schematic read ClassNotFoundException");
        }
	/*
	catch (FileNotFoundException e) {
	     System.err.println("Error:Schematic:Read: FileNotFoundException");
	     throw new FileNotFoundException("Schematic read FileNotFoundException");
	     }

	*/
    } //end read

    //--------------------------------------------------------------

    /** readPortSrcConnection
     * There is only one input source (SchematicInport or IconOutport) per connection.
     */

    public void readPortSrcConnection(ObjectInputStream ois, Connection conn)
            throws IOException {
        int exist;
        String name;
        int typeOfConnector;

        //read source IconInst and port information
        exist = ois.readInt();
        if (exist == 0) {
            //System.out.println("Debug:Schematic:readPortSrcConnection: exist false");
            conn.src_iconOrSchPort = null;
            conn.src_port = null;
            return;
        }
        if (exist == 1) {
            //System.out.println("Debug:Schematic:readPortSrcConnection: exist true");
            typeOfConnector = ois.readInt(); //1=SchematicInport 2=IconInst
            switch (typeOfConnector) {
                case 1:  //SchematicInport
                    //System.out.println("Debug:Schematic:readPortSrcConnection:schin");
                    name = ois.readUTF();
                    for (int j = 0; j < drawableObjs.size(); j++) {
                        Component comp = (Component) drawableObjs.elementAt(j);
                        if (comp instanceof SchematicInport) {
                            SchematicInport sip = (SchematicInport) comp;
                            if (sip.Name.equals(name)) {
                                conn.src_iconOrSchPort = sip;
                                conn.src_port = null;
                                sip.links.addElement(conn);
                                break;
                            }
                        }
                    }
                    break;
                case 2:  //IconInst
                    //System.out.println("Debug:Schematic:readPortSrcConnection:iconist");
                    name = ois.readUTF();
                    int foundIconInst = -1;
                    for (int j = 0; j < drawableObjs.size(); j++) {
                        Component comp = (Component) drawableObjs.elementAt(j);
                        if (comp instanceof IconInst) {
                            IconInst iconInst = (IconInst) comp;
                            if (iconInst.instanceName.equals(name)) {
                                foundIconInst = j;
                                conn.src_iconOrSchPort = iconInst;
                                int connPinIndex = ois.readInt();
                                String connPinName = ois.readUTF();
                                Component realPin = (Component) iconInst.drawableParts.elementAt(connPinIndex);
                                //should be connecting to IconOutport
                                IconOutport realOutPin;
                                if (realPin instanceof IconOutport) {
                                    realOutPin = (IconOutport) realPin;
                                    //check if name the same as before
                                    if (connPinName.equals(realOutPin.Name)) {
                                        fixConnSrc(conn, realOutPin);
                                        conn.src_port = realOutPin;
                                        realOutPin.links.addElement(conn);
                                        break;
                                    } else if (frozenIconOutport(conn, realOutPin)) {
                                        //assumes pin has been renamed
                                        fixConnSrc(conn, realOutPin); //not needed
                                        conn.src_port = realOutPin;
                                        realOutPin.links.addElement(conn);
                                        break;
                                    } else { //IconInport but pin moved
                                        //find by Name as second option
                                        boolean found = findAndFixIconOutport(conn, connPinName, iconInst);
                                        break;
                                    }
                                } //end if IconOutport
                                else {
                                    //find by Name as second option
                                    boolean found = findAndFixIconOutport(conn, connPinName, iconInst);
                                    break;
                                }
                            } //instance name matches
                        }//iconinst
                    } //end goThruDrawableObjs
                    if (foundIconInst == -1) {
                        System.err.println("Error:Schematic:readPortSrcConnection: cannot find: " + name + " in instance list.");
                    }

                    break; //case 2
                default:
                    System.err.println("Error:Schematic:readPortSrcConnection: unknown type of connection on interconnect.");
                    break; //default
            }//end switch
        } //end (exist==1)
    }//end readPortSrcConnection
    //--------------------------------------------------------------

    /** readPortDestConnection
     */
    //--------------------------------------------------------------
    public void readPortDestConnection(ObjectInputStream ois, Connection conn) throws IOException {
        int exist;
        int typeOfConnector2;

        //System.out.println("Debug:Schematic:readPortDestConnection");
        //read destination IconInst and port information
        exist = ois.readInt();
        if (exist == 0) {
            //System.out.println("Debug:Schematic:readPortSrcConnection:exist=0");
            conn.dest_iconOrSchPort = null;
            conn.dest_port = null;
            return;
        }
        if (exist == 1) {
            //System.out.println("Debug:Schematic:readPortDestConnection:exist=1");

            String name;
            typeOfConnector2 = ois.readInt();
            switch (typeOfConnector2) {
                case 1:  //SchematicOutport
                    //System.out.println("Debug:Schematic:readPortDestConnection:schout");
                    name = ois.readUTF();
                    for (int j = 0; j < drawableObjs.size(); j++) {
                        Component comp = (Component) drawableObjs.elementAt(j);
                        if (comp instanceof SchematicOutport) {
                            SchematicOutport sop = (SchematicOutport) comp;
                            if (sop.Name.equals(name)) {
                                conn.dest_iconOrSchPort = sop;
                                conn.dest_port = null;
                                sop.link = conn;
                                break;
                            }
                        }
                    }
                    break;

                case 2:  //IconInst
                    //System.out.println("Debug:Schematic:readPortDestConnection:iconinst");
                    name = ois.readUTF();
                    int foundIconInst = -1;
                    for (int j = 0; j < drawableObjs.size(); j++) {
                        Component comp = (Component) drawableObjs.elementAt(j);
                        if (comp instanceof IconInst) {
                            IconInst iconInst = (IconInst) comp;
                            if (iconInst.instanceName.equals(name)) {
                                foundIconInst = j;
                                conn.dest_iconOrSchPort = iconInst;
                                int connPinIndex = ois.readInt();
                                String connPinName = ois.readUTF();
                                Component realPin = (Component) iconInst.drawableParts.elementAt(connPinIndex);
                                IconInport realInPin;
                                if (realPin instanceof IconInport) {
                                    //System.out.println("Debug:Schematic:readDest: "+connPinIndex+" "+connPinName);
                                    realInPin = (IconInport) realPin;
                                    //check if name the same as before
                                    if (connPinName.equals(realInPin.Name)) {
                                        fixConnDest(conn, realInPin);
                                        conn.dest_port = realInPin;
                                        conn.dest_port.link = conn;
                                        break;
                                    } else if (frozenIconInport(conn, realInPin)) { //if pin did not move, then assume pin has been renamed
                                        fixConnDest(conn, realInPin); //dont need here
                                        conn.dest_port = realInPin;
                                        conn.dest_port.link = conn;
                                        break;
                                    } else { //IconInport but not correct pin
                                        //find by Name as second option
                                        boolean found = findAndFixIconInport(conn, connPinName, iconInst);
                                        break;
                                    }
                                } else { //else not IconInport thus not right pin
                                    boolean found = findAndFixIconInport(conn, connPinName, iconInst);
                                    break;
                                }
                            }
                        } //if iconInst
                    }//end goThruDrawableObjs2
                    if (foundIconInst == -1) {
                        System.err.println("Error:Schematic:readPortDestConnection: cannot find: " + name + " in instance list.");
                    }
                    break; //end case 2
                default:
                    System.err.println("Error:Schematic:readPortDestConnection: unknown type of connection on interconnect.");
                    break;
            }//end switch
        } //end if (exist==1)
    }//end readPortDestConnection

    //---------------------------------------------
    public boolean frozenIconInport(Connection conn, IconInport realPin) {
        //did the pin move?  yes=not frozen no=frozen
        boolean frozen = true;
        int x = conn.x[conn.numVerticies - 1];
        int y = conn.y[conn.numVerticies - 1];
        if ((x != realPin.x0) || (y != realPin.y0)) {
            //System.out.print("Debug:Schematic:frozenIconInport ");
            //System.out.print(x+" "+y);
            //System.out.println(" to "+realPin.x0+" "+realPin.y0);
            frozen = false;
        }
        return (frozen);
    }

    //---------------------------------------------
    public boolean frozenIconOutport(Connection conn, IconOutport realPin) {
        //did the pin move?  yes=not frozen no=frozen
        boolean frozen = true;
        int x = conn.x[0];
        int y = conn.y[0];
        if ((x != realPin.x1) || (y != realPin.y1)) {
            //System.out.print("Debug:Schematic:frozenIconOutport ");
            //System.out.print(x+" "+y);
            //System.out.println(" to "+realPin.x1+" "+realPin.y1);
            frozen = false;
        }
        return (frozen);
    }

    //---------------------------------------------
    public boolean fixConnSrc(Connection conn, IconOutport realPin) {
        boolean frozen = frozenIconOutport(conn, realPin);
        if (!frozen) { //if pin moved, then move interconnect
            conn.moveSrcAbsolute(realPin.x1, realPin.y1);
        }
        return (!frozen);
    }

    //---------------------------------------------
    public boolean fixConnDest(Connection conn, IconInport realPin) {
        boolean frozen = frozenIconInport(conn, realPin);
        if (!frozen) { //if pin moved, then move interconnect
            conn.moveDestAbsolute(realPin.x0, realPin.y0);
        }
        return (!frozen);
    }

    //---------------------------------------------
    public boolean findAndFixIconInport(Connection conn, String connPinName, IconInst iconInst) {
        boolean found;
        int foundi; //go find pin by name
        foundi = iconInst.findInport(connPinName);
        if (foundi >= 0) {
            IconInport realInPin = (IconInport) iconInst.drawableParts.elementAt(foundi);
            fixConnDest(conn, realInPin);
            conn.dest_port = realInPin;
            conn.dest_port.link = conn;//only one
            found = true;
        } else {//error conn not pointing to an IconInport and cannot find pin elsewhere on iconinst
            //todo: call IconInst.selectPort to find a IconInport near by the end of the interconnect and connect to it.
            System.err.println("Error:Schematic:findAndFixIconInport: cannot connect to pin on: " + iconInst.instanceName);
            conn.dest_iconOrSchPort = null;
            conn.dest_port = null;

            found = false;
        }
        return (found);
    }

    //---------------------------------------------
    public boolean findAndFixIconOutport(Connection conn, String connPinName, IconInst iconInst) {
        boolean found;
        int foundi; //go find pin by name
        foundi = iconInst.findOutport(connPinName);
        if (foundi >= 0) {
            IconOutport realOutPin = (IconOutport) iconInst.drawableParts.elementAt(foundi);
            fixConnSrc(conn, realOutPin);
            conn.src_port = realOutPin;
            conn.src_port.links.addElement(conn); //more than one
            found = true;
        } else {//error conn not pointing to an IconOutport and cannot find pin elsewhere on iconinst
            //todo: call IconInst.selectPort to find a IconOutport near by the end of the interconnect and connect to it.
            System.err.println("Error:Schematic:findAndFixIconOutport: cannot connect to pin on: " + iconInst.instanceName);
            conn.src_iconOrSchPort = null;
            conn.src_port = null;
            found = false;
        }
        return (found);
    }

    //---------------------------------------------

    /**
     * writeNslm from these connections
     *
     * @param    pw PrintWriter
     */
    public void writeNslm(PrintWriter pw) {

        Connection conn;

        int non_conn = 0;
        if (drawableObjs == null) {
            return;
        }
        if (drawableObjs.size() == 0) {
            return;
        }
        pw.print("\n");

        // go thru drawables, when you find either a SchPort or a icon
        // then go thru its output ports and write the connections
        // note: It looks like NSL only allows one src and one dest per nslConnect
        // or nslRelabel statement currently.

        for (int i = 0; i < drawableObjs.size(); i++) {
            String tempstr;
            Component tempComponent = (Component) drawableObjs.elementAt(i);

            // SchematicInport
            if (tempComponent instanceof SchematicInport) {
                SchematicInport sip = (SchematicInport) tempComponent;
                for (int j = 0; j < sip.links.size(); j++) {  //for each link

                    conn = sip.links.elementAt(j);
                    if (conn.dest_iconOrSchPort != null) {
                        if (conn.dest_iconOrSchPort instanceof IconInst) {
                            tempstr = "    nslRelabel(" + sip.Name + ",";
                            tempstr = tempstr + ((IconInst) conn.dest_iconOrSchPort).instanceName + "." + conn.dest_port.Name + ");\n";
                            pw.print(tempstr);
                        }
                        //run thru
                        if (conn.dest_iconOrSchPort instanceof SchematicOutport) {
                            tempstr = "    nslRelabel(" + sip.Name + ",";
                            tempstr = tempstr + ((SchematicOutport) conn.dest_iconOrSchPort).Name + ");\n";
                            pw.print(tempstr);
                        }
                    } //end if (conn.dest_iconOrSchPort!=null)
                } //end for sip.links.size()
                // IconInst output pins only
            } else if (tempComponent instanceof IconInst) {
                String instanceName = ((IconInst) tempComponent).instanceName;
                Vector iconsDrawables = ((IconInst) tempComponent).drawableParts;
                if (iconsDrawables == null) {
                    break; //this cannot happen
                }
                for (int k = 0; k < iconsDrawables.size(); k++) {
                    GraphicPart gobj = (GraphicPart) iconsDrawables.elementAt(k);
                    if (gobj instanceof IconOutport) { //for each output port
                        IconOutport op = (IconOutport) gobj;

                        for (int m = 0; m < op.links.size(); m++) { //for each link
                            conn = op.links.elementAt(m);
                            if (conn.dest_iconOrSchPort != null) {
                                if (conn.dest_iconOrSchPort instanceof IconInst) {
                                    tempstr = "    nslConnect(" + instanceName + "." + op.Name + ",";
                                    tempstr = tempstr + ((IconInst) conn.dest_iconOrSchPort).instanceName + "." + conn.dest_port.Name + ");\n";
                                    pw.print(tempstr);
                                }
                                if (conn.dest_iconOrSchPort instanceof SchematicOutport) {
                                    tempstr = "    nslConnect(" + instanceName + "." + op.Name + ",";
                                    tempstr = tempstr + ((SchematicOutport) conn.dest_iconOrSchPort).Name + ");\n";
                                    tempstr = makeRelabel(tempstr);
                                    pw.print(tempstr);
                                }
                            }
                        }//end for op.links.size()
                    }//end if instanceof IconOutport
                } //end for iconsDrawables.size()
            }//end if instanceof IconInst
        } //end for (int i=0; i<drawableObjs.size(); i++)
    }//end writeNslm

    //-------------------------------------------------------
    String makeRelabel(String tempstr) {
        int nslConnectLength = 14; //"    nslConnect"
        String temp2;
        temp2 = tempstr.substring(nslConnectLength);
        tempstr = "    nslRelabel" + temp2;
        return (tempstr);
    }
    //------------------------------------------------------------------
} //end Class Schematic
