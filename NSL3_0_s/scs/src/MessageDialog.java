/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * MessageDialog - A class representing the dialog which will be popped up when
 * an unfinished function is asked for.
 *
 * @author Weifang Xie, Amanda Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("Duplicates")
class MessageDialog extends JDialog
        implements ActionListener {

    Frame fm;
    int sizex = 700;
    final static int sizey = 200;
    final static int locationx = 400;
    final static int locationy = 400;


    String msg = "MessageDialog:Sorry! Still under construction.";
    String juice = "Still under construction.";
    String juiceMessage = "Message: Still under construction.";
    Label msgLabel = null;
    FontMetrics fontmetrics = null;


    /**
     * Constructor of this class, with the parent set to fm.
     */
    public MessageDialog(Frame fm) {
        super(fm, "Message Dialog", true);

        msg = "Sorry! Still under construction.";
        initDialog(fm, msg);
    }

    public MessageDialog(Frame fm, String message) {
        super(fm, "Message Dialog", true);
        initDialog(fm, message);
    }

    public void initDialog(Frame fm, String message) {
        this.fm = fm;
        Container wd = getContentPane();
        wd.setLayout(new BorderLayout());

        // add message label
        Panel panel = new Panel();
        msgLabel = new Label("");
        msg = message;
        msgLabel.setText(message);
        //        panel.add("Center",  Label(message));
        panel.add("Center", msgLabel);
        wd.add("Center", panel);

        Panel ButtonPanel = new Panel();
        Button btn;
        btn = new Button("Ok");
        btn.addActionListener(this);
        ButtonPanel.add("Center", btn);
        wd.add("South", ButtonPanel);

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

    //------------------------------------------------------------
    public void display(String message) {
        //messages come as "file:message"
        // the method prints the message 3 places: err file, dialog box, and warning/status window

        msg = message;
        //String errstr="Message:"+message;
        //System.err.println(errstr);
        setMsg(message);
        setLocation(new Point(locationx, locationy));
        sizex = fontmetrics.stringWidth(juiceMessage) + 25;
        if (sizex < 155) sizex = 155;
        setSize(sizex, sizey);
        setVisible(true);
    }

    //---------------------------------------------------------
    public void display() {
        display(msg);
    }

    /**
     * Perform functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        Button btn;

        if (event.getSource() instanceof Button) {
            btn = (Button) event.getSource();
            if (btn.getLabel().equals("Ok")) {
                dispose();
            }
        }
    }

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
}






