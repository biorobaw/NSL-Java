/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconPanel - A class representing the panel in the icon editor which is used
 * to draw icons.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version %I%, %G%
 * @*var c        the color of the current paint brush
 * @*var old_x        the variable to keep the old value of the
 * x-coordinate of the mouse
 * @*var old_y        the variable to keep the old value of the
 * y-coordinate of the mouse
 * @*var resizingOrMoving        a flag indicating whether the current
 * highlighted component is selected as a whole (so
 * that it's in the mode of moving) or only one
 * point is selected (so that it's in the mode of
 * resizing)
 * @*var parentFrame        pointing to the parentFrame--IconEditorFrame
 * @*var grid        the measure of the current grid
 * @*var mousedown    a flag indicating whether the mouse is in the
 * button pressed mode
 * @since JDK8
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//------------------------------------------------------------------
@SuppressWarnings("Duplicates")
public class IconPanel extends JPanel
        implements MouseMotionListener, MouseListener, KeyListener {

    Color c = Color.green;
    int old_x = 0;
    int old_y = 0;
    int resizingOrMoving; //resizing=0, moving=1
    IconEditorFrame parentFrame;
    int grid = 8;
    boolean mousedown = false;
    Icon currIcon = null;
    Declaration var = null;

    static public String text_string = "DUMMY";
    public static Color currBackgroundCol = OptionsFrame.noActionTaken_col; // will be either drawBack_col or OptionsFrame.noActionTaken_col
    WarningDialog warningPopup;
    //-------------------------------------------------------------------------

    /**
     * Constructor of this class with frame parameter
     */
    IconPanel(IconEditorFrame parentF) {
        parentFrame = parentF;
        setBackground(currBackgroundCol); //why not noActionTakenBack_col
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        if (parentFrame.currIcon != null) {
            currIcon = parentFrame.currIcon;
        }
        warningPopup = new WarningDialog(parentF);
    }
    //-------------------------------

    /**
     * Return the preferred size of this icon panel.
     */
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
    //-------------------------------

    /**
     * paint this icon panel.
     */
    public void paintChildren(Graphics g) { //not this has to be 
        //paintComponent for swing

        if (parentFrame.currIcon != null) {
            parentFrame.currIcon.paint(g);
        }
    }
    //-------------------------------

    /**
     * When status of the icon panel as to what component will be inserted next
     * is changed, this function is called to clear the select flags of those
     * components on this icon panel.
     */

    public void newstatus() {
        int ix;
        GraphicPart tempComp;

        if (parentFrame == null) {
            //System.out.println("Debug:IconPanel: IconEditorFrame is null");
            return;
        }
        if (parentFrame.currIcon == null) {
            //System.out.println("Debug:IconPanel: IconEditorFrame's current Icon is null");
            return;
        }

        if (parentFrame.currIcon.drawableParts != null) {
            for (ix = 0; ix < parentFrame.currIcon.drawableParts.size(); ix++) {
                tempComp = (GraphicPart) parentFrame.currIcon.drawableParts.elementAt(ix);
                tempComp.select = 0;
            }
        }
        setBackground(currBackgroundCol);
        repaint();
    }
//--------------------------------------------


    /**
     * Event handler for mouseMoved event.
     */
    public void mouseMoved(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        //   System.out.println("Debug:IconPanel:Mouse moved event...");

    }
//--------------------------------------------

    /**
     * Event handler for mousePressed event.
     */
//--------------------------------------------
    public void mousePressed(MouseEvent e) {
        GraphicPart tempComp;
        int ix;
        String status = parentFrame.myToolBox.status;

        currIcon = parentFrame.currIcon; // IconPanel may not know
        //there is a new icon until the mouse is pressed

        int x = (e.getX() + grid / 2) / grid * grid;
        int y = (e.getY() + grid / 2) / grid * grid;

        mousedown = true;
        requestFocus();
        //System.out.println("Debug:IconPanel:mousePressed: x:"+x+" y:"+y);
        if (status == null) {
            //System.out.println("Debug:IconPanel:mousePressed: null status");
            return;
        }
        if (currIcon == null) {
            //System.out.println("Debug:IconPanel:mousePressed: null curIcon");
            return;
        }
        if (currIcon.drawableParts == null) {
            //System.out.println("Debug:IconPanel:mousePressed: null drawableParts");
            return;
        }

        for (ix = 0; ix < currIcon.drawableParts.size(); ix++) {
            tempComp = (GraphicPart) currIcon.drawableParts.elementAt(ix);

            if (tempComp.select != 0 && !status.equals("move")) {
                tempComp.addpoint(x, y);
                old_x = x;
                old_y = y;
                currIcon.setminmax();
                repaint();
                return;
            }
            tempComp.select = 0;
        }
        resizingOrMoving = 0; //resizing=0, moving=1

        switch (status) {
            case "insert_line":
                currIcon.addDrawablePart(new GraphicPart_line(x, y, OptionsFrame.line_col));

                //-- new addition
                break;
            case "insert_text":
                GraphicPart_text tempGOT = new GraphicPart_text(x, y, text_string, OptionsFrame.freeTextFont, OptionsFrame.freeText_col, OptionsFrame.freeTextSize);
                currIcon.addDrawablePart(tempGOT);
                break;
            case "insert_oval":
                currIcon.addDrawablePart(new GraphicPart_oval(x, y, OptionsFrame.oval_col));
                //System.out.println("Debug:IconPanel:mousePressed: insert_oval");
                break;
            case "insert_rect":
                //System.out.println("Debug:IconPanel:mousePressed: insert_rect");
                currIcon.addDrawablePart(new GraphicPart_rect(x, y, OptionsFrame.rect_col));
                break;
            case "insert_poly":
                //system.out.println("Debug:IconPanel:mousePressed: insert_poly");
                currIcon.addDrawablePart(new GraphicPart_poly(x, y, OptionsFrame.poly_col));
                break;
            case "insert_inport":
                addInport(x, y);
                break;
            case "insert_outport":
                addOutport(x, y);
                break;
            case "move":
            case "null":
                for (ix = currIcon.drawableParts.size() - 1; ix >= 0; ix--) {
                    tempComp = (GraphicPart) currIcon.drawableParts.elementAt(ix);

                    if (tempComp.selectpoint(e.getX(), e.getY())) {
                        resizingOrMoving = 0; // resizing mode
                        //currIcon.moveToLast(ix); 6/22/02 causes bug in reading schematic connections
                        break;
                    }
                    if (tempComp.selectobj(e.getX(), e.getY())) {
                        resizingOrMoving = 1;   // moving mode
                        //currIcon.moveToLast(ix); 6/22/02 causes bug in reading schematic connections
                        break;
                    }
                }
                break;
        }
        old_x = x;
        old_y = y;
        currIcon.setminmax();
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }  //end mousedPressed


    //-------------------------------

    /**
     * get pointer to the var variable for adding a new port
     */
    public void setPortInfo(Declaration var) {
        this.var = var;
    }

    //--------------------------------------
    public void addPort(Declaration var, int x, int y, GraphicPart port) {
        //left to right
        if ((var.portIconDirection == 'L') && (x < Icon.pinLength)) {
            String errMsg = "Error:IconPanel:addPort:x less than Icon.pinLength";
            warningPopup.display(errMsg);
            //top to bottom
        } else if ((var.portIconDirection == 'T') && (y < Icon.pinLength)) {
            String errMsg = "Error:IconPanel:addPort:y less than Icon.pinLength";
            warningPopup.display(errMsg);
        } else {
            if (parentFrame.currModule != null) {
                currIcon.addDrawablePart(port);
                var.inIcon = true;
                //we modified the old variable already
                int index = parentFrame.currModule.findVarIndex(var.varName);
                if (index == -1) {
                    parentFrame.currModule.addVariable(var);
                }
            }
        }
    } // end addPort

    //--------------------------------------
    public void addInport(int x, int y) {
        GraphicPart port = new IconInport(var, x, y, c);
        addPort(var, x, y, port);
    } // end addInports

    //--------------------------------------
    public void addOutport(int x, int y) {
        GraphicPart port = new IconOutport(var, x, y, c);
        addPort(var, x, y, port);
    }
    //------------------------------------------------------------

    /**
     * Event handler for mouseDragged event.
     */
    public void mouseDragged(MouseEvent e) {
        GraphicPart tempComp;
        int ix;
        String status = parentFrame.myToolBox.status;
        Icon currIcon = parentFrame.currIcon;

        int x = (e.getX() + grid / 2) / grid * grid;
        int y = (e.getY() + grid / 2) / grid * grid;

        //System.out.println("Debug:IconPanel:mouseDragged: x:"+x+" y:"+y);

        if (status == null) {
            //System.out.println("Debug:IconPanel:mouseDragged:status==null");
            return;
        }

        if (currIcon == null) {
            //System.out.println("Debug:IconPanel:mouseDragged:currIcon==null");
            return;
        }

        for (ix = 0; ix < currIcon.drawableParts.size(); ix++) {
            tempComp = (GraphicPart) currIcon.drawableParts.elementAt(ix);

            if (tempComp.select > 1 && resizingOrMoving == 0) {
                //System.out.println("Debug:IconPanel:mouseDragged: x y:"+x+" "+y);
                //System.out.println("Debug:IconPanel:mouseDragged: old_x old_y:"+old_x+" "+old_y);
                tempComp.movepoint(x - old_x, y - old_y);  //sending in delta
                //	     tempComp.moveobj(x-old_x, y-old_y);
                break;
            }
            if (tempComp.select == 1 && resizingOrMoving == 1) {
                tempComp.moveobj(x - old_x, y - old_y);
                break;
            }
        }


        old_x = x;
        old_y = y;
        currIcon.setminmax();
        repaint();
    }
    //----------------------------------------------------------

    /**
     * Event handler for mouseReleased event.
     */
    public void mouseReleased(MouseEvent e) {
        String status = parentFrame.myToolBox.status;
        Icon currIcon = parentFrame.currIcon;
        GraphicPart tempComp;
        int ix;

        mousedown = false;

        int x = (e.getX() + grid / 2) / grid * grid;
        int y = (e.getY() + grid / 2) / grid * grid;

        if (status == null) {
            //System.out.println("Debug:IconPanel:mouseReleased:status==null");
            return;
        }
        if (currIcon == null) {
            //System.out.println("Debug:IconPanel:mouseReleased:currIcon==null");
            return;
        }
        if (status.equals("move")) {
            //System.out.println("Debug:IconPanel:mouseReleased:status==move");
            return;
        }

        //System.out.println("Debug:IconPanel:MouseReleased:x:"+x+" y:"+y);

        for (ix = 0; ix < currIcon.drawableParts.size(); ix++) {
            tempComp = (GraphicPart) currIcon.drawableParts.elementAt(ix);

            // unselect the other objects first
            tempComp.select = 0; // unselect this component.
            // if it is selected.. it will be done again below

            // select the whole object
            tempComp.selectobj(x, y);

            //if ( tempComp.select != 0) {
            //System.out.println("Debug:IconPanel:Comp selected now..");
            //}
            //if (tempComp.done())  {
            //tempComp.select=0;
            //}
        }

        if (status.equals("insert_inport") || status.equals("insert_outport"))
            parentFrame.myToolBox.status = null;

        repaint();

// 	int x=(e.getX()+grid/2)/grid*grid;
// 	int y=(e.getY()+grid/2)/grid*grid;
    }

    /**
     * Event handler for mouseEntered event.
     */
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Debug:IconPanel:mouseEntered");
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
        //System.out.println("Debug:IconPanel:mouseClicked");
    }

    /**
     * Event handler for keyPressed event.
     */
    public void keyPressed(KeyEvent e) {
        //System.out.println("Debug:IconPanel:keyPressed:"+e.getKeyText(e.getKeyCode()))

        if (mousedown) return;

        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            parentFrame.removeDrawablePart();
        }
    }

    /**
     * Event handler for keyReleased event.
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Event handler for keyTyped event.
     */
    public void keyTyped(KeyEvent e) {
    }


    //----------------------------------------------

    /**
     * method to set the colors of various objects - from IconPanel
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

    //----------------------------------------------
    /**
     * method to set the colors of various objects - from IconPanel
     *
     **/
/* REMOVED:took out because we are doing it in OptionsFrame
    public static void setMiscColors (String p_backCol ,
				      String p_noActionTakenBackCol ,
				      String p_inportCol ,
				      String p_outportCol, 
				      String p_lineCol  , 
				      String p_rectCol , 
				      String p_polyCol  ,
				      String p_ovalCol  , 
				      String p_freeTextCol , 
				      String p_moduleTextCol , 
				      int p_textSize , 
				      String p_textFont,
				      boolean p_noActionTaken)
    {
	OptionsFrame.drawBack_col = SCSUtility.returnCol(p_backCol);
	OptionsFrame.noActionTaken_col = SCSUtility.returnCol(p_noActionTakenBackCol );

	IconOutport.port_col =SCSUtility.returnCol( p_inportCol) ;
	IconInport.port_col =SCSUtility.returnCol( p_outportCol) ;

	OptionsFrame.line_col = SCSUtility.returnCol(p_lineCol);
	OptionsFrame.poly_col = SCSUtility.returnCol(p_polyCol );
	OptionsFrame.oval_col = SCSUtility.returnCol(  p_ovalCol );
	OptionsFrame.rect_col =  SCSUtility.returnCol( p_rectCol );
	OptionsFrame.freeText_col = SCSUtility.returnCol( p_freeTextCol );
	OptionsFrame.moduleText_col = SCSUtility.returnCol( p_moduleTextCol );
	//OptionsFrame.instanceText_col = SCSUtility.returnCol( p_instanceTextCol );
	OptionsFrame.freeTextSize = p_textSize ;
	OptionsFrame.freeTextFontName = p_textFont ;

	if (p_noActionTaken) {
	    currBackgroundCol=noActionTakenBack_col;
	} else {
	    currBackgroundCol=OptionsFrame.drawBack_col;
	}
    }
*/    






    /*  
    public static   Color OptionsFrame.line_col  = Color.green ;
    public static   Color  OptionsFrame.poly_col   = Color.red  ;
    public static   Color OptionsFrame.oval_col   = Color.orange  ;
    public static   Color  OptionsFrame.rect_col   = Color.green  ; 
    public static Color  OptionsFrame.freeText_col   = Color.green  ; 


    public static   Color  OptionsFrame.moduleText_col   = Color.black  ; 
    //public static   Color  OptionsFrame.instanceText_col   = Color.green  ; 
    public static Color OptionsFrame.drawBack_col = Color.black ;
    public static Color OptionsFrame.noActionTaken_col = Color.lightGray;


    static   String OptionsFrame.freeTextFontName = "Monospaced" ; //aa:99/4/13
    static   int OptionsFrame.freeText_size = 10 ;//aa:99/4/13

    public static  Font     OptionsFrame.freeTextFont=new Font(OptionsFrame.freeTextFontName,Font.BOLD,OptionsFrame.freeText_size);

    public static  String   OptionsFrame.moduleLabelLocation="CENTER"; //options: ABOVE, BELOW, RIGHT, LEFT of instance icon
    //todo: add OptionsFrame.moduleLabelLocation to the options menu  

    public static  Font     OptionsFrame.moduleLabelFont=new Font(OptionsFrame.freeTextFontName,Font.BOLD,OptionsFrame.freeText_size);

    */


}//end class IconPanel
