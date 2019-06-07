/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * MessageDialogOkCancel - A class representing the dialog which will be popped up when
 * an unfinished function is asked for.
 *
 * @author Weifang Xie, Amanda Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("Duplicates")
class MessageDialogOkCancel extends JDialog
        implements ActionListener {

    String msg = "MessageDialogOkCancel:Not implemented yet.";
    String juice = "Not implemented yet.";
    String juiceMessage = "Message: Not implemented yet.";
    Label msgLabel = new Label("");
    FontMetrics fontmetrics = null;

    int sizex = 700;
    final static int sizey = 200;
    final static int locationx = 400;
    final static int locationy = 400;

    public boolean okPressed = false; //return this value from display

    Frame fm;

    /**
     * Constructor of this class, with the parent set to fm.
     */

    public MessageDialogOkCancel(Frame fm) {
        super(fm, "Message Dialog - Ok Cancel", true);
        msg = "Message:Not implemented yet.";
        initDialogOkCancel(fm, msg);
    }

    //-----------------------------
    public MessageDialogOkCancel(Frame fm, String message) {

        super(fm, "Message Dialog - Ok Cancel", true);
        initDialogOkCancel(fm, message);
    }

    //-----------------------------
    public void initDialogOkCancel(Frame fm, String message) {

        msg = message;
        Container wd = getContentPane();
        wd.setLayout(new BorderLayout());

        // add message label
        Panel panel = new Panel();
        msgLabel.setText(message);

        panel.add("Center", msgLabel);
        wd.add("Center", panel);

        Panel myButtonPanel = new Panel();
        JButton btn;
        btn = new JButton("Ok");
        btn.addActionListener(this);
        myButtonPanel.add("West", btn);

        JButton cancelJButton = new JButton("Cancel");
        cancelJButton.addActionListener(this);
        myButtonPanel.add("East", cancelJButton);

        wd.add("South", myButtonPanel);

        addWindowListener(new DWAdapter());

        Font warningFont = new Font("Monospaced", Font.BOLD, 10);
        fontmetrics = getFontMetrics(warningFont);
        if (fontmetrics == null) {
            System.err.println("MessageDialog:fontmetrics null");
        }
    }

    //-------------------------------------------------------
    public void setMsg(String message) {
        //should really be called setDialogMessage which is short
        msg = message; //looks like filename:juice
        int len = message.length();
        int i = message.indexOf(':');
        if ((i > 1) && ((i + 2) < len)) {
            juice = message.substring((i + 1));
        } else {
            juice = message;
        }
        juiceMessage = "Message: " + juice;
        msgLabel.setText(juiceMessage);
    }

    //-------------------------
    public boolean display(String message) {
        //messages come as "file:message"
        // the method prints the message 2 places: err file, dialog box

        msg = message;
        //String errstr="Message:"+message;
        //System.err.println(errstr);
        setMsg(message);
        setLocation(new Point(locationx, locationy));
        sizex = fontmetrics.stringWidth(juiceMessage) + 25;
        if (sizex < 155) sizex = 155;
        setSize(sizex, sizey);
        setVisible(true);
        //System.err.println("okPressed: "+okPressed);
        return (okPressed);
    }

    //-------------------------
    public boolean display() {
        return (display(msg)); //okPressed
    }
    //-------------------------

    /**
     * Perform functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        JButton btn;

        if (event.getSource() instanceof JButton) {
            btn = (JButton) event.getSource();

            if (btn.getText().equals("Ok")) {
                okPressed = true;
                //dispose();
                setVisible(false);
            }

            if (btn.getText().equals("Cancel")) {
                okPressed = false;
                setVisible(false);
                //dispose();
            }
        }
    } //end actionPerformed

    //-------------------------

    /**
     * Handle window events.
     */
    class DWAdapter extends WindowAdapter {
        /**
         * Handle windowClosing event.
         */
        public void windowClosing(WindowEvent event) {
            dispose();
        }
    }
} //end MessageDialogOkCancel





