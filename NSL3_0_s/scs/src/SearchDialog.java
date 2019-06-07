/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * SearchDialog  - A class representing the dialog popped up when entering free
 * text in the icon editor window
 *
 * @author Gupta, Alexander
 * @version %I%, %G%
 * @param stringJTF           the text entered by the user
 * @param status           the status of the entry box containing string "ok" or "cancel"
 * @since JDK8
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


//-------------------------------------------------------------
@SuppressWarnings("Duplicates")
class SearchDialog extends JDialog
        implements ActionListener {


    JTextField newStringJTF;
    ButtonGroup forwardButtons;
    String oldString; //coming in string
    String newString; //newely entered string
    String status = "ok";
    boolean okPressed = false;
    boolean forward = false;

    final static int sizex = 300;
    final static int sizey = 200;
    final static int locationx = 400;
    final static int locationy = 400;

    //--------------------------------------------------

    /**
     * Constructor of this class, with the parent set to fm.
     */

    public SearchDialog(Frame fm) {

        super(fm, "Search Dialog ", true); //blocking true

        initSearchDialog(fm, "");

    }

    public SearchDialog(Frame fm, String lastString) {

        super(fm, "Search Again Dialog ", true); //blocking true
        initSearchDialog(fm, lastString);
    }

    public void initSearchDialog(Frame fm, String lastString) {

        GridBagConstraints gbcon;
        GridBagLayout gblayout;
        gblayout = new GridBagLayout();
        Container cpane = getContentPane();
        cpane.setLayout(gblayout);
        gbcon = new GridBagConstraints();

        JLabel newStringJLabel1 = new JLabel("String: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(newStringJLabel1, gbcon);
        cpane.add(newStringJLabel1, gbcon);
        newStringJTF = new JTextField(SCSUtility.blankString20);
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(newStringJTF, gbcon);
        cpane.add(newStringJTF, gbcon);

        forwardButtons = SCSUtility.addOppositesButtonPanel(cpane, gblayout, gbcon, "Direction:", true, "Forward", "Backward", this);

        //	System.out.println("Debug:SearchDialog:middle12 of constructor");
        JPanel okCancelPanel = new JPanel();
        Border etched = BorderFactory.createEtchedBorder();
        okCancelPanel.setBorder(etched);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(this);
        okCancelPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        okCancelPanel.add(cancelButton);

        SCSUtility.setSecondColumn(gbcon, 1, 2);
        gblayout.setConstraints(okCancelPanel, gbcon);
        cpane.add(okCancelPanel, gbcon);

        addWindowListener(new DWAdapter());

        setVisible(false);
    } //end initSearchDialog

    //----------------------------------------------------------------
    /*
     * display
     * @return - okPressed
     */
    public boolean display(StringBoolean content) {

        oldString = content.mystring; //could be ""
        forward = content.myboolean;

        newStringJTF.setText(oldString);
        okPressed = false;
        setLocation(new Point(locationx, locationy));
        setSize(sizex, sizey);
        setVisible(true); //blocking popup
        //	System.out.println("Debug:SearchDialog:after setVisible");
        if (okPressed) {
            content.mystring = newStringJTF.getText();
            content.myboolean = forward;
        }
        return (okPressed);
    }

    //----------------------------------------------------------------
    /*
     * actionPerformed - Handle action events.
     */

    public void actionPerformed(ActionEvent event) {
        JRadioButton rbn;
        JButton bn;
        String bntext;

        if (event.getSource() instanceof JButton) {
            bn = (JButton) event.getSource();
            bntext = bn.getText();

            if (bntext.equals("Ok")) {
                okPressed = true;
                status = "ok";
                setVisible(false);
            }
            if (bntext.equals("Cancel")) {
                okPressed = false;
                status = "cancel";
                setVisible(false);
            }
        }

        if (event.getSource() instanceof JRadioButton) {
            //System.out.println("SearchDialog:actionPerformed JRadioButton");
            rbn = (JRadioButton) event.getSource(); //actually a radio button
            bntext = rbn.getText();
            // System.out.println("SearchDialog:actionPerformed "+bntext);
            if ((bntext.equals("Forward")) ||
                    (bntext.equals("Backward"))) {
                //		System.out.println("SearchDialog:actionPerformed "+bntext);
                forward = SCSUtility.getOppositesValue(forwardButtons, "Forward"); //returns boolean
            }
        }
    } //end actionPerformed

    class DWAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
            dispose();
        }
    } //end DWAdapter class

} //end Search Dialog class
