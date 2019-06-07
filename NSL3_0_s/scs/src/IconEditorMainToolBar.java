/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class IconEditorMainToolBar
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


    public IconEditorMainToolBar(ActionListener thislist) {
        toolBar = new JToolBar();
        //myUserPref.IconDir should be resources

        newButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "new.gif"));

        newButton.addActionListener(thislist);
        newButton.setActionCommand("New");
        newButton.setToolTipText("New");
        toolBar.add(newButton);

        openButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "open.gif"));
        toolBar.add(openButton);
        openButton.setActionCommand("Open");
        openButton.addActionListener(thislist);
        openButton.setToolTipText("Open");


        closeButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "close.gif"));
        toolBar.add(closeButton);
        closeButton.setActionCommand("Close");

        closeButton.addActionListener(thislist);
        closeButton.setToolTipText("Close");

        saveButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "save.gif"));
        toolBar.add(saveButton);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(thislist);
        saveButton.setToolTipText("Save");

        printButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "print.gif"));
        toolBar.add(printButton);
        printButton.addActionListener(thislist);
        printButton.setActionCommand("Print");
        printButton.setToolTipText("Print");

        exitButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "exit.gif"));
        toolBar.add(exitButton);
        exitButton.addActionListener(thislist);
        exitButton.setActionCommand("Exit");
        exitButton.setToolTipText("Exit");


//        playButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "play.gif"));
//       toolBar.add( playButton);
//       playButton.addActionListener(thislist);

//  stopButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "stop.gif"));
//  toolBar.add( stopButton);
//  stopButton.addActionListener(thislist);

//   colorsButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "loop.gif"));
//       toolBar.add( colorsButton);
//       colorsButton.addActionListener(thislist);

//  firstButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "first.gif"));
//       toolBar.add( firstButton);
//       firstButton.addActionListener(thislist);


//       prevButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "prev.gif"));
//       toolBar.add( prevButton);
//       prevButton.addActionListener(thislist);


//       nextButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "next.gif"));
//       toolBar.add( nextButton);
//       nextButton.addActionListener(thislist);

//       lastButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "last.gif"));
//       toolBar.add( lastButton);
//       lastButton.addActionListener(thislist);


        //    searchButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "search.gif"))  ;
//       toolBar.add( searchButton);
//       searchButton.addActionListener(thislist);

        //   addButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "add.gif"));
//       toolBar.add( addButton);
//       addButton.addActionListener(thislist);

        //   deleteButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "delete.gif"));
//       toolBar.add( deleteButton);
//       deleteButton.addActionListener(thislist);


        //modifyButton = new JButton( new ImageIcon(myUserPref.IconDir + File.separator + "modify.gif"));
        //toolBar.add( modifyButton);
        //modifyButton.setActionCommand("Graphics");
        //modifyButton.addActionListener(thislist);
        //modifyButton.setToolTipText("Graphics");

        helpButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "help.gif"));
        toolBar.add(helpButton);
        helpButton.addActionListener(thislist);
        helpButton.setToolTipText("Help documentation");

    }


    // public static void main(String[] args) {
//      MainToolBar frame = new MainToolBar(this);
//       //   frame.pack();
//         frame.show();
//     }
}
