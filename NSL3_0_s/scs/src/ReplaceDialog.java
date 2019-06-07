/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * ReplaceDialog  - A class representing the dialog popped up when entering free
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
class ReplaceDialog extends JDialog
        implements ActionListener {


    JTextField existingStringJTF;
    JTextField replacementStringJTF;
    ButtonGroup forwardButtons;
    ButtonGroup globalReplaceButtons;
    String lastExistingString; //coming in string
    String lastReplacementString; //coming in string
    String existingString; //newly entered string
    String replacementString; //newly entered string
    String status = "ok";
    boolean okPressed = false;
    boolean forward = false;
    boolean globalReplace = false;

    final static int sizex = 300;
    final static int sizey = 200;
    final static int locationx = 400;
    final static int locationy = 400;

    //--------------------------------------------------

    /**
     * Constructor of this class, with the parent set to fm.
     */

    public ReplaceDialog(Frame fm) {

        super(fm, "Replace Dialog ", true); //blocking true

        initReplaceDialog(fm, "", globalReplace);

    }

    public ReplaceDialog(Frame fm, String lastString) {

        super(fm, "Replace Again Dialog ", true); //blocking true
        initReplaceDialog(fm, lastString, globalReplace);
    }

    public ReplaceDialog(Frame fm, String lastString, boolean inGlobalReplace) {

        super(fm, "Global Replace Dialog ", true); //blocking true

        initReplaceDialog(fm, lastString, inGlobalReplace);
    }

    public void initReplaceDialog(Frame fm, String lastString, boolean inGlobalReplace) {

        globalReplace = inGlobalReplace;

        GridBagConstraints gbcon;
        GridBagLayout gblayout;
        gblayout = new GridBagLayout();
        Container cpane = getContentPane();
        cpane.setLayout(gblayout);
        gbcon = new GridBagConstraints();

        JLabel existingStringJLabel1 = new JLabel("Existing String: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(existingStringJLabel1, gbcon);
        cpane.add(existingStringJLabel1, gbcon);
        existingStringJTF = new JTextField(SCSUtility.blankString20);
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(existingStringJTF, gbcon);
        cpane.add(existingStringJTF, gbcon);

        JLabel replacementStringJLabel1 = new JLabel("Replacement String: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(replacementStringJLabel1, gbcon);
        cpane.add(replacementStringJLabel1, gbcon);
        replacementStringJTF = new JTextField(SCSUtility.blankString20);
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(replacementStringJTF, gbcon);
        cpane.add(replacementStringJTF, gbcon);


        forwardButtons = SCSUtility.addOppositesButtonPanel(cpane, gblayout, gbcon, "Direction:", true, "Forward", "Backward", this);

        //could call SCSUtility.addOnOffButtonPanel
        globalReplaceButtons = SCSUtility.addOppositesButtonPanel(cpane, gblayout, gbcon, "Global Replace:", false, "True", "False", this);

        //	System.out.println("Debug:ReplaceDialog:middle12 of constructor");
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
    } //end initReplaceDialog

    //----------------------------------------------------------------
    /*
     * display
     * @return - okPressed
     */
    public boolean display(StringStringBooleanBoolean content) {

        lastExistingString = content.string1; //could be ""
        lastReplacementString = content.string2; //could be ""
        forward = content.boolean1;
        globalReplace = content.boolean2;

        existingStringJTF.setText(lastExistingString);
        replacementStringJTF.setText(lastReplacementString);
        okPressed = false;
        setLocation(new Point(locationx, locationy));
        setSize(sizex, sizey);
        setVisible(true); //blocking popup
        //	System.out.println("Debug:ReplaceDialog:after setVisible");
        if (okPressed) {
            content.string1 = existingStringJTF.getText();
            content.string2 = replacementStringJTF.getText();
            content.boolean1 = forward;
            content.boolean2 = globalReplace;
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
            //System.out.println("ReplaceDialog:actionPerformed JRadioButton");
            rbn = (JRadioButton) event.getSource(); //actually a radio button
            bntext = rbn.getText();
            // System.out.println("ReplaceDialog:actionPerformed "+bntext);
            if ((bntext.equals("Forward")) ||
                    (bntext.equals("Backward"))) {
                //		System.out.println("ReplaceDialog:actionPerformed "+bntext);
                forward = SCSUtility.getOppositesValue(forwardButtons, "Forward"); //returns boolean
            }
            if ((bntext.equals("True")) ||
                    (bntext.equals("False"))) {
                //		System.out.println("ReplaceDialog:actionPerformed "+bntext);
                globalReplace = SCSUtility.getOppositesValue(globalReplaceButtons, "True"); //returns boolean
            }
        }

    } //end actionPerformed

    //--------------------------------------------
    class DWAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
            dispose();
        }
    } //end DWAdapter class

} //end Replace Dialog class
