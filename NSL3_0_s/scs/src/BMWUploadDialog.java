/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BMWUploadDialog extends JDialog
        implements ActionListener {
    // four panels on
    //   1 radiobutton
    //   1 checkbox
    //   2 radiobutton
    // ok cancel panel

    private Font localFont;
    private String[] fontlist;
    private SchEditorFrame parent;

    //public variables
    public boolean zipLibrary = false;
    public boolean zipModel = false;
    public boolean zipModule = false;

    JCheckBox checkSrc;
    JCheckBox checkExe;
    JCheckBox checkIO;
    JCheckBox checkDoc;

    public boolean hierarchicalZip = false;
    public boolean justMeZip = false;

    ButtonGroup group1;
    ButtonGroup group2;

    JButton okButton;
    JButton cancelButton;
    //JButton insertButton ;

    //returned
    public String toBeZippedDir = null;
    public String zipFileName = null;

    // constructor

    public BMWUploadDialog(Frame fm, String title) {

        super(fm, title, true);

        //setTitle(title);
        setSize(500, 330);
        parent = (SchEditorFrame) fm;
        getContentPane().setLayout(new BorderLayout());

        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BorderLayout());


        JPanel buttonTopPanel = new JPanel();
        group1 = new ButtonGroup();
        addRadioButton(buttonTopPanel, group1, "Zip Module", true);
        addRadioButton(buttonTopPanel, group1, "Zip Model", true);
        addRadioButton(buttonTopPanel, group1, "Zip Library", true);


        Border etched1 = BorderFactory.createEtchedBorder();
        Border titled1 = BorderFactory.createTitledBorder(etched1, "Select One");
        buttonTopPanel.setBorder(titled1);

        corePanel.add(buttonTopPanel, "North");
        //--------

        JPanel buttonBottomPanel = new JPanel();
        group2 = new ButtonGroup();
        addRadioButton(buttonBottomPanel, group2, "Just Me Zip", true);
        addRadioButton(buttonBottomPanel, group2, "Hierarchical Zip", true);

        Border etched3 = BorderFactory.createEtchedBorder();
        Border titled3 = BorderFactory.createTitledBorder(etched3, "Select One");
        buttonBottomPanel.setBorder(titled3);

        corePanel.add(buttonBottomPanel, "South");


        //--------

        JPanel checkWhatPanel = new JPanel();
        JCheckBox checkSrc = addCheckBox(checkWhatPanel, "src");
        JCheckBox checkExe = addCheckBox(checkWhatPanel, "exe");
        JCheckBox checkIO = addCheckBox(checkWhatPanel, "io");
        JCheckBox checkDoc = addCheckBox(checkWhatPanel, "doc");

        Border etched2 = BorderFactory.createEtchedBorder();
        Border titled2 = BorderFactory.createTitledBorder(etched2, "Select One or More");
        checkWhatPanel.setBorder(titled2);

        corePanel.add(checkWhatPanel, "Center");

        //--------
        JPanel okCancelPanel = new JPanel();
        okButton = new JButton("Ok");
        okCancelPanel.add(okButton);
        okButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        okCancelPanel.add(cancelButton);
        cancelButton.addActionListener(this);

        //--------
        getContentPane().add(corePanel, "North");
        getContentPane().add(okCancelPanel, "South");
    }// end constructor

    //------------------------------

    public void addRadioButton(JPanel buttonPanel, ButtonGroup g, String buttonName, boolean v) {
        JRadioButton button = new JRadioButton(buttonName, v);
        button.addActionListener(this);
        g.add(button);
        buttonPanel.add(button);
        button.setActionCommand(buttonName);
    }

    //------------------------------

    public JCheckBox addCheckBox(JPanel p, String name) {
        JCheckBox c = new JCheckBox(name);
        c.addActionListener(this);
        p.add(c);
        return c;
    }

    //------------------------------

    public void actionPerformed(ActionEvent event) {

        Button bn;

        Object source = event.getSource();

        if (source instanceof JRadioButton) {
            String command = group1.getSelection().getActionCommand();
            if (command == null) {
                command = group2.getSelection().getActionCommand();
            }
            switch (command) {
                case "Zip Library":
                    zipLibrary = true;
                    break;
                case "Zip Model":
                    zipModel = true;
                    break;
                case "Zip Module":
                    zipModule = true;
                    break;
                case "Hierarchical Zip":
                    hierarchicalZip = true;
                    break;
                case "Just Me Zip":
                    justMeZip = true;
                    break;
                default:
                    System.err.println("Error:BMWUploadDialog:actionPerformed: unknown radio button command.");
                    break;
            }
            return;
        }
        if (source instanceof JButton) {
            String cmdName = event.getActionCommand();
            if (cmdName.equals("Cancel")) {
                dispose();
            }
            if (cmdName.equals("Ok")) {
                //System.out.print("Ok pressed");
                String[] filter = new String[4];
                int countF = 0;
                if (checkSrc.isSelected()) {
                    filter[countF] = "src";
                    countF++;
                }
                if (checkExe.isSelected()) {
                    filter[countF] = "exe";
                    countF++;
                }
                if (checkIO.isSelected()) {
                    filter[countF] = "io";
                    countF++;
                }
                if (checkDoc.isSelected()) {
                    filter[countF] = "doc";
                    countF++;
                }
                if (selectDir().equals("Ok")) {
                    dispose();

                    if ((toBeZippedDir != null) && (!toBeZippedDir.equals(""))) {
                        if (hierarchicalZip) {
                            zipFileName = "hierarchical.zip";
                            SCSUtility.hierarchicalZip(zipLibrary, toBeZippedDir, toBeZippedDir, zipFileName, countF, filter);
                        } else if (justMeZip) {
                            zipFileName = "justMe.zip";
                            SCSUtility.zipMe(zipLibrary, toBeZippedDir, toBeZippedDir, zipFileName, countF, filter);
                        } else {
                            System.err.println("Error:BMWUploadDialog:actionPerformed: unknown zip option.");
                        }
                    }
                } //end if file selected ok
            }// end if ok button pushed
        }// end if JButton
    } //end actionPerformed

    //---------------------------------------------------------
    public String selectDir() {

        TableFileSelector tfs;
        String status;

        try {
            tfs = new TableFileSelector(parent, "Module", true);
        } catch (IOException e) {
            String errStr = "Error:EditorFrame:selectDir: problem with lib";
            System.err.println(errStr);
            parent.warningPopup.setMsg(errStr);
            parent.warningPopup.setVisible(true);
            status = "Error";
            return (status);
        }

        tfs.setLocation(new Point(200, 200));
        tfs.pack();
        tfs.setSize(700, 275);
        tfs.setVisible(true);

        //after
        if (tfs.pushed.equals("Cancel")) {
            status = "Cancel";
            return (status);
        }
        if (tfs.pushed.equals("Ok")) {
            status = "Ok";
            if (zipLibrary) {
                toBeZippedDir = tfs.returnLibraryPath();
            }
            if ((zipModel) || (zipModule)) {
                toBeZippedDir = tfs.returnVersionPath();
            }
            return (status);
        }
        return (null);
    } //end selectDir
    //---------------
} //end class

	
    
    
