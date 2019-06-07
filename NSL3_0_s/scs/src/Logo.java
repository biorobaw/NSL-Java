/* SCCS  %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * Logo - flashed the SCS Logo
 * an unfinished function is asked for.
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


class Logo extends Dialog
        implements ActionListener {

    static final int locationx = 300;
    static final int locationy = 300;
    static final int sizex = 700;
    static final int sizey = 250;

    UserPref myUserPref = new UserPref();
    Image image;
    String status = "none";

    public Logo(Frame fm) {
        super(fm, "SCS", true);

        BorderLayout bl = new BorderLayout();

        setLayout(bl);

        image = Toolkit.getDefaultToolkit().getImage(myUserPref.logoImage);

        Button okButton = new Button("OK");
        add("South", okButton);
        okButton.addActionListener(this);
    }

    //-------------------------------------------
    public void myDisplay() {
        int imageW = image.getWidth(this);
        int imageH = image.getHeight(this);

        //  System.out.println("Image ht is: " + imageH + "image WIdth is: " + imageW );
        //   setSize(   imageH , imageW );
        setLocation(locationx, locationy);
        setSize(sizex, sizey);
        setTitle("Welcome to the Schematic Capture System");
        setVisible(true);
        requestFocus();
    }

    //-------------------------------------------
    public void paint(Graphics g) {
        g.drawImage(image, 0, 10, 750, 245, this);
    }
    //---------------------------------------------

    /**
     * Perform functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        Button btn;

        if (event.getSource() instanceof Button) {
            String cmdName = event.getActionCommand();

            if (cmdName.equals("OK")) {
                status = "ok";
                dispose();
            }
        }
    }
    //--------------------------------------------

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
