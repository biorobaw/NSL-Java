/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class MainToolBar
        //extends Panel
{


    private JButton newButton;
    private JButton openButton;

    private JButton closeButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton modifyButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton firstButton;
    private JButton lastButton;
    private JButton searchButton;
    private JButton playButton;
    private JButton stopButton;

    private JButton iconButton;
    private JButton textButton;
    private JButton inportButton;
    private JButton outportButton;

    private JButton connButton;
    private JButton upButton;
    private JButton downButton;


    private JButton helpButton;
    private JButton saveButton;
    private JButton printButton;
    private JButton exitButton;
    private JButton colorsButton;

    private JButton earthButton;
    JToolBar toolBar;

    UserPref myUserPref = new UserPref();


    public JToolBar returnToolbar() {

        return toolBar;
    }

    /**
     * Constructor of this class with no parameters.
     */


    public MainToolBar(ActionListener thislist) {
        toolBar = new JToolBar();

        newButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "new.gif"));

        newButton.addActionListener(thislist);
        newButton.setActionCommand("New Module");
        newButton.setToolTipText("New Module");
        toolBar.add(newButton);

        openButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "open.gif"));
        toolBar.add(openButton);
        openButton.setActionCommand("Open Module");
        openButton.addActionListener(thislist);
        openButton.setToolTipText("Open Module");


        closeButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "close.gif"));
        toolBar.add(closeButton);
        closeButton.setActionCommand("Close Module");

        closeButton.addActionListener(thislist);
        closeButton.setToolTipText("Close Module");

        saveButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "save.gif"));
        toolBar.add(saveButton);
        saveButton.setActionCommand("Save Module");
        saveButton.addActionListener(thislist);
        saveButton.setToolTipText("Save Module");

        printButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "print.gif"));
        toolBar.add(printButton);
        printButton.addActionListener(thislist);
        printButton.setActionCommand("Print");
        printButton.setToolTipText("Print");


        // modifyButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "modify.gif"));
        // toolBar.add( modifyButton);
        // modifyButton.setActionCommand("Graphics");
        // modifyButton.addActionListener(thislist);
        //modifyButton.setToolTipText("Graphics");

        exitButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "exit.gif"));
        toolBar.add(exitButton);
        exitButton.addActionListener(thislist);
        exitButton.setActionCommand("Exit");
        exitButton.setToolTipText("Exit");

        upButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "up.gif"));
        toolBar.add(upButton);
        upButton.addActionListener(thislist);
        upButton.setActionCommand("Ascend");
        upButton.setToolTipText("Ascend Hierarchy");

        downButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "down.gif"));
        toolBar.add(downButton);
        downButton.addActionListener(thislist);
        downButton.setActionCommand("Descend");
        downButton.setToolTipText("Descend Hierarchy");

        iconButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "icon.gif"));
        toolBar.add(iconButton);
        iconButton.addActionListener(thislist);
        iconButton.setActionCommand("Icon");
        iconButton.setToolTipText("Insert Icon");

        connButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "conn.gif"));
        toolBar.add(connButton);
        connButton.addActionListener(thislist);
        connButton.setActionCommand("Connection");
        connButton.setToolTipText("Insert Connection");

        inportButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "inport.gif"));
        toolBar.add(inportButton);
        inportButton.addActionListener(thislist);
        inportButton.setActionCommand("InPort");
        inportButton.setToolTipText("Insert Schematic InPort");

        outportButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "outport.gif"));
        toolBar.add(outportButton);
        outportButton.addActionListener(thislist);
        outportButton.setActionCommand("OutPort");
        outportButton.setToolTipText("Insert Schematic OutPort");

        textButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "text.gif"));
        toolBar.add(textButton);
        textButton.addActionListener(thislist);
        textButton.setActionCommand("Free text");
        textButton.setToolTipText("Insert Text");

        helpButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "help.gif"));
        toolBar.add(helpButton);
        helpButton.addActionListener(thislist);
        helpButton.setToolTipText("Help");

    }

    // public static void main(String[] args) {
//      MainToolBar frame = new MainToolBar(this);
//       //   frame.pack();
//         frame.show();
//     }
}
