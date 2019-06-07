/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

@SuppressWarnings("SpellCheckingInspection")
public class EditToolBar
        //extends Panel
{


    private JButton cutButton;


    private JButton pasteButton;

    private JButton copyButton;
    //private JButton pasteButton ;

    private JButton helpButton;

    private JButton exitButton;

    JToolBar toolBar;

    UserPref myUserPref = new UserPref();


    public JToolBar returnToolbar() {

        return toolBar;
    }

    /**
     * Constructor of this class with no parameters.
     */


    public EditToolBar(ActionListener thislist) {
        toolBar = new JToolBar();

        cutButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "cut.gif"));

        cutButton.addActionListener(thislist);
        cutButton.setActionCommand("Cut");
        cutButton.setToolTipText("Cut");
        toolBar.add(cutButton);

        copyButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "copy.gif"));
        toolBar.add(copyButton);
        copyButton.setActionCommand("Copy");
        copyButton.addActionListener(thislist);
        copyButton.setToolTipText("Copy");


        pasteButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "paste.gif"));
        toolBar.add(pasteButton);
        pasteButton.setActionCommand("paste");
        pasteButton.addActionListener(thislist);
        pasteButton.setToolTipText("paste");

        //----------


        helpButton = new JButton(new ImageIcon(myUserPref.IconDir + File.separator + "help.gif"));
        //  toolBar.add( helpButton);
        helpButton.addActionListener(thislist);
        helpButton.setToolTipText("Help documentation");

    }


}




