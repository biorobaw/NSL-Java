/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * IconToolBox - A class representing the icon tool bar in the left of icon
 * editor containing buttons for immediate icon editing operations.
 *
 * @author Weifang Xie, Gupta, Alexander
 * @version %I%, %G%
 * @since JDK8
 */

//todo: eliminate this class and move to a tool bar.

public class IconToolBox extends JPanel implements ActionListener {
    /*
        Color cstat = new Color(254,254,254);
    */
    IconEditorFrame parentFrame;
    JPopupMenu shapeMenu;
    String status = null;
    WarningDialog warningPopup4;
    WarningDialogOkCancel okCancelPopup4;

    DeclarationDialog inputPortDeclarationDialog = null;
    DeclarationDialog outputPortDeclarationDialog = null;

    Declaration var = null;

    /**
     * Constructor of this class with no parameters.
     */
    public IconToolBox(IconEditorFrame parentFramefm) {
        JButton jbn;
        JMenuItem jmi;

        parentFrame = parentFramefm;
        setLayout(new FlowLayout());
        Dimension dim = new Dimension(100, 30);

        // Rectangle
        jbn = new JButton("Rectangle");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Line
        jbn = new JButton("Line");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Oval
        jbn = new JButton("Oval");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Polygon
        jbn = new JButton("Polygon");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Text
        jbn = new JButton("Text");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
        // Inport
        jbn = new JButton("InPort");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
        // Outport
        jbn = new JButton("OutPort");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
        //	add(bn=new JButton("Image"));
        //	bn.addActionListener(this);

        // Move
        add(jbn = new JButton("Move"));
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);

        jbn = new JButton("Delete");
        add(jbn);
        jbn.addActionListener(parentFrame);
        jbn.setPreferredSize(dim);

        jbn = new JButton("Clear");
        jbn.addActionListener(parentFrame);
        jbn.setPreferredSize(dim);
        add(jbn);

        //add(jbn=new JButton("Undo"));
        //jbn.addActionListener(this);
        //jbn.setPreferredSize(dim);

        //add(jbn=new JButton("Redo"));
        //jbn.addActionListener(this);
        //jbn.setPreferredSize(dim);

        warningPopup4 = new WarningDialog(parentFrame);
        okCancelPopup4 = new WarningDialogOkCancel(parentFrame);
    }
    //-------------------------------------------------------

    /**
     * Handle action events.
     */
    public void actionPerformed(ActionEvent event) {
        JButton jbn;
        String somestr;
        //	    System.out.println("Debug:IconToolBox:actionPerformed: "+event.getSource().toString());

        if (parentFrame.currModule == null) {
            warningPopup4.display("IconToolBox: Must use File Menu to create Icon first.");
            return;
        }

        if (event.getSource() instanceof JButton) {
            jbn = (JButton) event.getSource();
            String somestr2 = event.getActionCommand();
            somestr = jbn.getText();
            //	    System.out.println("Debug:IconToolBox: JButton: somestr:" + somestr);
            //	    System.out.println("Debug:IconToolBox: JButton: somestr2:" + somestr2);
            //-----------------------------------
            if (somestr.equals("Move")) {
                status = "move";
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //-----------------------------------
            if (somestr.equals("Clear")) {
                String qstring = "IconToolBox:Are you sure you want to clear the screen?";
                boolean okPressed = okCancelPopup4.display(qstring);
                if (okPressed) {
                    status = "clear";
                } else {
                    status = "nothing";
                }
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------------------------
            if (somestr.equals("InPort")) {
                doPort("InputPort");
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //-----------------------------------
            if (somestr.equals("OutPort")) {
                doPort("OutputPort");
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //-----------------------------------
            if (somestr.equals("Text")) {
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                // show the option panel  *nitgupta*
                //  System.out.print("text selected ");

                GetNamePopup ie = new GetNamePopup(parentFrame, SCSUtility.maxCharsFreeText);
                ie.setSize(300, 100);
                ie.setVisible(true);
                if (ie.status.equals("ok")) {
                    status = "insert_text";
                    IconPanel.text_string = ie.NameTF.getText();
                } else {
                    IconPanel.text_string = "";
                    status = "nothing";
                }
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Line")) {
                status = "insert_line";
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Oval")) {
                status = "insert_oval";
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Rectangle")) {
                status = "insert_rect";

                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Polygon")) {
                status = "insert_poly";
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            warningPopup4.display("IconToolBox:JButton but no one claimed it.");
            return;


        } //end if JButton
        warningPopup4.display("IconToolBox:Event but no button or menu item claimed it.");
    }

    //----------------------------------------------------------
    public void doPort(String dialogType) {
        DeclarationDialog dialog = null;

        parentFrame.myIconPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        //    Toolkit tkit = Toolkit.getDefaultToolkit();
        // 	     Image image = tkit.getImage("Images/inport.gif") ;
        // 	     Cursor mycursor = tkit.createCustomCursor(  image ,new Point(0,0),"inport") ;
        // 	     parentFrame.myIconPanel.setCursor(mycursor );
        if (dialogType.equals("InputPort")) {
            if (inputPortDeclarationDialog == null) {
                inputPortDeclarationDialog = new DeclarationDialog(parentFrame, "InputPort");

            }
            dialog = inputPortDeclarationDialog;
        }
        if (dialogType.equals("OutputPort")) {
            if (outputPortDeclarationDialog == null) {
                outputPortDeclarationDialog = new DeclarationDialog(parentFrame, "OutputPort");

            }
            dialog = outputPortDeclarationDialog;
        }

        var = parentFrame.currModule.fillVariableName(parentFrame, dialogType, "Port Instance Name(first letter lower case): ");
        if (var == null) return;

        assert dialog != null;
        boolean okPressed = dialog.display(var);
        //  System.out.print("Debug:IconToolBox:doPort middle");
        if (okPressed) {
            //everything is in var
            if (!var.inIcon) {
                if (dialogType.equals("InputPort")) {
                    status = "insert_inport";
                }
                if (dialogType.equals("OutputPort")) {
                    status = "insert_outport";
                }
                parentFrame.myIconPanel.newstatus();
                parentFrame.myIconPanel.setPortInfo(var);//add port
                //free to add
            }
        }
    }

    /**
     * Return the preferred size of this icon tool box.
     */
    public Dimension getPreferredSize() {
        return new Dimension(150, getSize().height);
    }
}










