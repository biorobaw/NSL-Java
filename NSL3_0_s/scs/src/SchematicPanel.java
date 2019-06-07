/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * SchematicPanel - A class representing the panel in the schematic editor which
 * is used to draw schematics.
 *
 * @author Xie, Gupta, Alexander
 * @version %I%,%G%
 * @param c               the color of the current paint brush
 * @param old_x           the variable to keep the old value of the
 * x-coordinate of the mouse
 * @param old_y           the variable to keep the old value of the
 * x-coordinate of the mouse
 * @param parentFrame          pointing to the parentFrame--SchEditorFrame
 * @param grid            the measure of the current grid
 * @since JDK8
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class SchematicPanel extends JPanel
        implements MouseMotionListener, MouseListener, KeyListener {

    static int linewidth = 2;
    ;
    Color c = Color.yellow;
    int old_x, old_y;
    SchEditorFrame parentFrame;
    int grid = 8;

    public static Color currBackgroundCol = OptionsFrame.noActionTaken_col; //sometimes drawBack_col, sometimes noActionTakenBack_col.

    public static String freeTextString = "DUMMY";
    public static String instanceTextString = "DUMMY";

    /*  ama 6/3/02 took out and put in OptionsFrame
    public static  Color    drawBack_col = Color.black ;
    public static  Color    noActionTakenBack_col = Color.lightGray ;
    public static  Color    freeText_col   = Color.green  ; 
    public static   Color  instanceText_col   = Color.green  ; 
    public static   Color  line_col   = Color.green  ; 
    public static  String   textFontName = "Monospaced" ;//aa:00/4/13
    public static  int      text_size = 10 ;//aa:99/4/13
    public static  Font     freeTextFont=new Font(textFontName,Font.BOLD,text_size);
    public static  String   instanceLabelLocation="BELOW"; //options: CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon
    public static Font    instanceLabelFont=new Font(textFontName,Font.BOLD,text_size);
    */

    WarningDialog warningPopup;
    WarningDialogOkCancel warningPopupOkCancel;
    //-------------------------------------------------------

    /**
     * Constructor of this class with parentFrame set to fm.
     */
    SchematicPanel(SchEditorFrame fm) {
        parentFrame = fm;
        currBackgroundCol = OptionsFrame.drawBack_col;
        setBackground(currBackgroundCol);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);

        warningPopup = new WarningDialog(fm);
        warningPopupOkCancel = new WarningDialogOkCancel(fm);
        //freeTextFont=new Font(freeTextFontName,Font.BOLD,20);
        //instanceLabelFont=new Font(intanceTextFontName,Font.BOLD,text_size);
    }

    //-------------------------------------------------------

    /**
     * Return the preferred size of this schematic panel.
     */
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    /**
     * Repaint this schematic panel.
     */
    public void paintChildren(Graphics g) { //use to be paint

        if (parentFrame == null) {
            System.err.println("Error:SchematicPanel:paint:null Schematic frame");
            return;
        }
        if (parentFrame.currModule == null) {
            // if this is a new module it may not have been created yet
            return;
        }
        if (parentFrame.currModule.mySchematic == null) {
            // if this is a new module it will not have a schematic
            //System.err.println("Error:SchematicPanel:paint:current module does not have a schematic.");
            return;
        }
        parentFrame.currModule.mySchematic.paint(g);


        Component selComponent = parentFrame.currModule.mySchematic.selComponent;

        if (selComponent instanceof IconInst) {
            IconInst iconInst = (IconInst) selComponent;

            if (iconInst.selPort instanceof IconInport) {
                parentFrame.myStatusPanel.setStatusMessage("The type of this inport: " +
                        ((IconInport) iconInst.selPort).Type);
                return;
            }
            if (iconInst.selPort instanceof IconOutport) {
                parentFrame.myStatusPanel.setStatusMessage("The type of this outport: " +
                        ((IconOutport) iconInst.selPort).Type);
                return;
            }
        }
        parentFrame.myStatusPanel.setStatusMessage("");
    }
    //-------------------------------------------------------------------

    /**
     * Event handler for mouseMoved event.
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Event handler for mousePressed event.
     */
    public void mousePressed(MouseEvent e) {
        Module currModule = parentFrame.currModule;
        if (currModule == null) return;

        Vector schDrawables = currModule.mySchematic.drawableObjs;
        Component tempComponent;
        int x = e.getX();
        int y = e.getY();
        int ix;
        int found = -1;

        if (parentFrame.status.equals("insert_text")) {

            currModule.mySchematic.addDrawableObj(new GraphicPart_text(x, y, freeTextString, OptionsFrame.freeTextFont, OptionsFrame.freeText_col, OptionsFrame.freeTextSize));
            parentFrame.status = "nothing";
        }

        //todo: unhighlight previously selected object - after schDrawablesLoop

        //go thru all drawables and find new object selected
        schDrawablesLoop:
        for (ix = schDrawables.size() - 1; ix >= 0; ix--) {
            tempComponent = (Component) schDrawables.elementAt(ix);

            // check if iconInst selected
            if (tempComponent instanceof IconInst) {
                IconInst iconInst = (IconInst) tempComponent;

                //go thru drawables to see if pin on icon selected
                if (iconInst.selectport(x, y)) {

                    if (iconInst.selPort instanceof IconOutport) {
                        if (parentFrame.status.equals("Connection")) {
                            //add new connection
                            Connection conn;
                            conn = new Connection(iconInst, (IconOutport) iconInst.selPort, OptionsFrame.connection_col);
                            schDrawables.addElement(conn);
                            parentFrame.status = "";
                            currModule.mySchematic.selComponent = conn;
                            found = schDrawables.size() - 1;
                            break;
                        } else { //this is a IconOutport but not in connection mode
                            currModule.mySchematic.selComponent = tempComponent;
                            found = ix;
                            break;
                        }
                    }
                    // if inport selected give warning
                    if (iconInst.selPort instanceof IconInport) {
                        if (parentFrame.status.equals("Connection")) {
                            String errstr = "SchematicPanel:Cannot select inport for start of a connection. \b"; //rings bell
                            warningPopup.display(errstr);

                            parentFrame.myStatusPanel.setWarningMessage("Warning:" + errstr);
                            currModule.mySchematic.selComponent = null;
                            found = -1;
                            break;
                        } else {
                            currModule.mySchematic.selComponent = tempComponent;
                            found = ix;
                            break;
                        }
                    }
                    //should not get here
                    System.out.println("Debug:SchematicPanel:should not get here.");
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                } //end of - a pin has been selected

                //is icon selected
                if (iconInst.selectobj(x, y)) {
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }

                //6/19/02 aa; took out following because it does a repaint
                //iconInst.unselect();
            } //end if instanceof IconInst


            // check if SchematicInport selected
            if (tempComponent instanceof SchematicInport) {
                SchematicInport sip = (SchematicInport) tempComponent;
                if (sip.selectport(x, y)) {
                    if (parentFrame.status.equals("Connection")) {
                        //add new connection
                        Connection conn;
                        conn = new Connection(sip, OptionsFrame.connection_col);
                        schDrawables.addElement(conn);
                        parentFrame.status = "";
                        currModule.mySchematic.selComponent = conn;
                        found = schDrawables.size() - 1;
                        break;
                    }
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }

                if (sip.selectobj(x, y)) {
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                //6/19/02 aa; took out following because it does a repaint
                //sip.unselect();
            }

            // check if sch outport selected
            if (tempComponent instanceof SchematicOutport) {
                SchematicOutport sop = (SchematicOutport) tempComponent;
                //is the pin on the SchematicOutport selected?
                if (sop.selectport(x, y)) {
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                if (sop.selectobj(x, y)) {
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                //6/19/02 aa; took out following because it does a repaint
                //sop.unselect();
            }

            // check if connection selected
            if (tempComponent instanceof Connection && (!parentFrame.status.equals("Connection"))) {
                //If not first point in connection mode or
                //if not in connection mode at all
                Connection conn = (Connection) tempComponent;
                //if connection not done
                if (conn.selectpoint(x, y)) {
                    if ((conn.select == (conn.numVerticies + 1)) && (!conn.done()))
                        conn.addpoint();
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                //if you just want to select a connection
                if (conn.selectobj(x, y)) {
                    conn.insertpoint(x, y, grid);
                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                //6/19/02 aa; took out following because it does a repaint
                //conn.unselect();
            }

            // check if  free text selected

            if (tempComponent instanceof GraphicPart_text) {
                GraphicPart_text got = (GraphicPart_text) tempComponent;

                if (got.selectobj(x, y)) {

                    currModule.mySchematic.selComponent = tempComponent;
                    found = ix;
                    break;
                }
                //6/19/02 aa; took out following because it does a repaint
                //got.unselect();
            }
        } //end of schDrawablesLoop which is a down to

        //not found can be -1 meaning not found
        //now go and unhighlight anything previously selected
        //todo: should just know what the previously selected comp was
        //and only unhighlight it.
        if (found != -1) {
            for (ix = schDrawables.size() - 1; ix >= 0; ix--) {
                if (found != ix) {
                    tempComponent = (Component) schDrawables.elementAt(ix);

                    if (tempComponent instanceof IconInst)
                        ((IconInst) tempComponent).unselect();

                    if (tempComponent instanceof SchematicInport)
                        ((SchematicInport) tempComponent).unselect();

                    if (tempComponent instanceof SchematicOutport)
                        ((SchematicOutport) tempComponent).unselect();

                    if (tempComponent instanceof Connection)
                        ((Connection) tempComponent).unselect();

                    if (tempComponent instanceof GraphicPart_text)
                        ((GraphicPart_text) tempComponent).unselect();

                }
            } //end for (ix=schDrawables.size()-1
            currModule.mySchematic.pushtop(found);
        }  //if found
        else {
            currModule.mySchematic.selComponent = null;
        }
        old_x = (x + grid / 2) / grid * grid;
        old_y = (y + grid / 2) / grid * grid;
        repaint();
    }

    /**
     * Event handler for mouseDragged event.
     */
    public void mouseDragged(MouseEvent e) {
        if (parentFrame.currModule == null) return;

        Component selComponent = parentFrame.currModule.mySchematic.selComponent;
        if (selComponent == null) return;

        int x = (e.getX() + grid / 2) / grid * grid;
        int y = (e.getY() + grid / 2) / grid * grid;

        if (selComponent instanceof IconInst) {
            IconInst iconInst = (IconInst) selComponent;
            iconInst.selPort = null;
            iconInst.moveobj(x - old_x, y - old_y);
        }
        if (selComponent instanceof SchematicInport) {
            SchematicInport sip = (SchematicInport) selComponent;
            sip.moveobj(x - old_x, y - old_y);
        }
        if (selComponent instanceof SchematicOutport) {
            SchematicOutport sop = (SchematicOutport) selComponent;
            sop.moveobj(x - old_x, y - old_y);
        }
        if (selComponent instanceof Connection) {
            Connection conn = (Connection) selComponent;
            if (conn.src_iconOrSchPort instanceof IconInst)
                ((IconInst) conn.src_iconOrSchPort).selPort = null;
            conn.movepoint(x - old_x, y - old_y);
        }

        if (selComponent instanceof GraphicPart_text) {
            GraphicPart_text got = (GraphicPart_text) selComponent;

            got.moveobj(x - old_x, y - old_y);
        }

        old_x = x;
        old_y = y;
        repaint();
    }

    /**
     * Event handler for mouseReleased event.
     */
    public void mouseReleased(MouseEvent e) {
        if (parentFrame.currModule == null) return;

        Component selComponent = parentFrame.currModule.mySchematic.selComponent;
        if (selComponent == null) return;

        Module currModule = parentFrame.currModule;

        if (selComponent instanceof IconInst) {
            IconInst iconInst = (IconInst) selComponent;
            iconInst.selPort = null;

            for (int i = 0; i < iconInst.drawableParts.size(); i++) {
                GraphicPart gobj = (GraphicPart) iconInst.drawableParts.elementAt(i);
                Connection conn;

                if (gobj instanceof IconInport) {
                    conn = ((IconInport) gobj).link;  //todo:00/5/11 aa: this is the problem
                    if (conn != null)
                        conn.mergedest();
                }
                if (gobj instanceof IconOutport) {
                    IconOutport op = (IconOutport) gobj;

                    for (int j = 0; j < op.links.size(); j++) {
                        conn = op.links.elementAt(j);
                        conn.mergesrc();
                    }
                }
            }

            repaint();
            return;
        }

        if (selComponent instanceof SchematicInport) {
            SchematicInport sip = (SchematicInport) selComponent;

            for (int i = 0; i < sip.links.size(); i++) {
                Connection conn = sip.links.elementAt(i);
                conn.mergesrc();
            }

            repaint();
            return;
        }

        if (selComponent instanceof SchematicOutport) {
            SchematicOutport sop = (SchematicOutport) selComponent;
            if (sop.link != null)
                sop.link.mergedest();

            repaint();
            return;
        }

        Vector schDrawables = currModule.mySchematic.drawableObjs;
        Component tempComponent;

        int x = e.getX();
        int y = e.getY();

        if (selComponent instanceof Connection) {
            Connection conn = (Connection) selComponent;

            if (conn.src_iconOrSchPort instanceof IconInst)
                ((IconInst) conn.src_iconOrSchPort).selPort = null;

            for (int ix = schDrawables.size() - 1; ix >= 0; ix--) {
                tempComponent = (Component) schDrawables.elementAt(ix);

                if (tempComponent instanceof IconInst) {
                    IconInst iconInst = (IconInst) tempComponent;
                    if (iconInst.selectport(x, y)) {
                        if (iconInst.selPort instanceof IconInport) {
                            conn.connectdest(iconInst, (IconInport) iconInst.selPort);
                            iconInst.selPort = null;
                            break;
                        }
                    }
                }
                if (tempComponent instanceof SchematicOutport) {
                    SchematicOutport sop = (SchematicOutport) tempComponent;
                    if (sop.selectport(x, y)) {
                        conn.connectdest(sop);
                        break;
                    }
                }
            }
            conn.merge();
            if (conn.numVerticies == 1) {
                if (conn.src_iconOrSchPort instanceof IconInst)
                    conn.src_port.disconnect(conn);
                if (conn.src_iconOrSchPort instanceof SchematicInport)
                    ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);
                if (conn.dest_iconOrSchPort instanceof IconInst)
                    conn.dest_port.disconnect();
                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                    ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                schDrawables.removeElement(conn);
            }
        }

        repaint();
    }

    /**
     * Event handler for mouseEntered event.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Event handler for mouseExited event.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Event handler for mouseClicked event.
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Event handler for keyPressed event.
     */
    public void keyPressed(KeyEvent e) {

        //System.out.println("Debug:SchematicPanel:keyPressed: "+e.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == KeyEvent.VK_DELETE) {

            deleteComp();
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

            // reset the status string

            parentFrame.status = "nothing";

            parentFrame.myStatusPanel.setStatusMessage("Move object / modify connection mode ");
        }
    }

    //-------------------------------------------------------------------------

    /**
     * deleteComp
     * deletes the selected component or interconnect from
     * the list of variables and drawables and returns it 
     *
     */

    public Component deleteComp() {

        Module currModule = parentFrame.currModule;

        if (currModule == null) {
            System.err.println("Error:SchematicPanel: deleteComp:1 No Module Open");
            return (null);
        }
        if (currModule.mySchematic == null) {
            System.err.println("Error:SchematicPanel: deleteComp:2 No Schematic Open");
            return (null);
        }
        if (currModule.mySchematic.selComponent == null) {
            System.err.println("Error:SchematicPanel: deleteComp:3 No component selected");
            return (null);
        }
        if (currModule.variables == null) {
            System.err.println("Error:SchematicPanel: deleteComp:4 No variables to delete");
            return (null);
        }
        boolean okPressed = warningPopupOkCancel.display("SchematicPanel: Are you sure you want to delete the component or interconnect?");
        if (!okPressed) {
            return (null);
        }
        //now we unview and delete the object from variables lists
        Component comp = currModule.mySchematic.selComponent;

        //todo: delete pin from icon image as well
        //right now delete it from the schematic
        String name;
        boolean didit = false;
        if (comp instanceof SchematicInport) {
            SchematicInport sip = (SchematicInport) comp;
            name = sip.Name;
            if ((currModule.variables != null)) {
                didit = currModule.deleteVariable(name);
            }
        }
        if (comp instanceof SchematicOutport) {
            SchematicOutport sop = (SchematicOutport) comp;
            name = sop.Name;
            if ((currModule.variables != null)) {
                didit = currModule.deleteVariable(name);
            }
        }

        //now delete the iconinst, connection, text, or whatever
        currModule.mySchematic.deleteDrawableObj(comp);

        repaint();
        return (comp);
    }
    //-------------------------------------------------------

    /**
     * Event handler for keyReleased event.
     */
    public void keyReleased(KeyEvent e) {
    }
    //-------------------------------------------------------

    /**
     * Event handler for keyTyped event.
     */
    public void keyTyped(KeyEvent e) {
    }

    //-------------------------------------------------------
    //todo: merge the two setMiscColors methods

    /**
     * method to set the colors of various objects - from SchematicPanel
     **/

    public static void setMiscColors(Color p_drawBack_col,
                                     Color p_noActionTaken_col,
                                     boolean p_noActionTaken) {
        if (p_noActionTaken) {
            currBackgroundCol = p_noActionTaken_col;
        } else {
            currBackgroundCol = p_drawBack_col;
        }
    }
}//end class SchematicPanel







