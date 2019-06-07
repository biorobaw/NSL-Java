/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GetNamePopup  - A class representing the dialog popped up when entering free
 * text in the icon editor window
 *
 * @author Gupta, Alexander
 * @version %I%, %G%
 * @param NameTF           the text entered by the user
 * @param status           the status of the entry box containing string "ok" or "cancel"
 * @since JDK8
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GetNamePopup extends JDialog
        implements ActionListener {


    TextField NameTF;
    String status = "ok";
    boolean okPressed = false;

    private String dialogTypeFlag;

    /**
     * Constructor of this class, with the parent set to fm.
     */

    public GetNamePopup(Frame fm, int nameLength) {


        super(fm, "Text Entry Dialog ", true);


        //   System.out.print("constructor called..") ;


        GridLayout gl = new GridLayout(2, 2);
        Container cp = getContentPane();
        cp.setLayout(gl);

        Label entryLabel = new Label("Name:");
        cp.add(entryLabel);


        NameTF = new TextField(nameLength);

        cp.add(NameTF);

        Button OkButton = new Button("Ok");
        OkButton.addActionListener(this);
        cp.add(OkButton);

        Button CancelButton = new Button("Cancel");
        CancelButton.addActionListener(this);
        cp.add(CancelButton);

        addWindowListener(new DWAdapter());
    }


    public GetNamePopup(Frame fm, String label, int nameLength) {


        super(fm, "Text Entry Dialog ", true);


        //   System.out.print("constructor called..") ;


        GridLayout gl = new GridLayout(2, 2);
        Container cp = getContentPane();
        cp.setLayout(gl);

        Label entryLabel = new Label(label);
        cp.add(entryLabel);


        NameTF = new TextField(nameLength);

        cp.add(NameTF);

        Button OkButton = new Button("Ok");
        OkButton.addActionListener(this);
        cp.add(OkButton);

        Button CancelButton = new Button("Cancel");
        CancelButton.addActionListener(this);
        cp.add(CancelButton);

        addWindowListener(new DWAdapter());
    }


    /**
     * Handle action events.
     */

    public void actionPerformed(ActionEvent event) {

        Button bn;

        if (event.getSource() instanceof Button) {
            bn = (Button) event.getSource();

            // Handle the OK event

            if (bn.getLabel().equals("Ok")) {

                //	    IconPanel.text_string =  NameTF.getText();
                status = "ok";
                dispose();

            }

            if (bn.getLabel().equals("Cancel")) {
                status = "cancel";
                dispose();

            }

        }
    }


    class DWAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
            dispose();
        }
    }

}







