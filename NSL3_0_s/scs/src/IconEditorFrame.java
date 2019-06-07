/* SCCS %W% ---%G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * IconEditorFrame - A class representing the main GUI for Icon Editor. It's
 * composed of four main parts:
 * - a menu bar at the top
 * - an icon tool box in the left containing buttons for immediate icon
 * editing operation
 * - an icon panel occupying the most area, where to draw icons
 * - a status panel at the bottom, used to print some status information if
 * needed
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


@SuppressWarnings("Duplicates")
class IconEditorFrame extends EditorFrame implements ActionListener {
    IconPanel myIconPanel;
    IconToolBox myToolBox;

    //    String PortName=null;
    //    String PortType=null;
    //    String PortSignalType = null ;
    //    String PortBuffering = null ;
    //    String PortDirection = null ;
    //    Vector PortParameters=null;
    //    int PortFlag;

    Icon currIcon = null;
    SchEditorFrame parentFrame;

    //WarningDialog warningPopup; //in EditorFRame
    //WarningDialogOkCancel okCancelPopup; //in EditorFRame

    OptionsFrame optChoiceFrame;

    //-------------------------------

    /**
     * Constructor of this class, with the parentFrame set to fm, and current module
     * set to module.
     *
     * @param fm     SchEditorFrame - pointing to the parentFrame--SchEditorFrame
     * @param module Module
     */
    public IconEditorFrame(SchEditorFrame fm, Module module) {
        super("Icon");
        parentFrame = fm;
        setLocation(new Point(175, 175)); //can also be done in EditorFrame
        setSize(550, 550);

        setTitle("Icon Editor");
        if (module != null) {
            boolean found = executiveFrame.windowsAndModulesOpen.contains("Icon", module);
            if (!found) {
                currModule = module;
            } else {//found
                warningPopup.display("IconEditorFrame: that module already open in an Icon Editor");
                currModule = null;
            }
        } else {
            currModule = null;
        }

        if (!(OptionsFrame.newPreferences)) {
            OptionsFrame.drawBack_col = SCSUtility.returnCol(UserPref.drawBackgroundColor);
            OptionsFrame.noActionTaken_col = SCSUtility.returnCol(UserPref.noActionTakenColor);
        }
        IconPanel.currBackgroundCol = OptionsFrame.noActionTaken_col;

        getContentPane().setLayout(new BorderLayout());

        setJMenuBar(CreateMenuBar());

        // add toolbars
        JPanel toolbars = new JPanel();
        toolbars.setLayout(new BorderLayout());

        // add the open,close toolbar
        IconEditorMainToolBar maintoolbar = new IconEditorMainToolBar(this);
        toolbars.add("North", maintoolbar.returnToolbar());     //***
        EditToolBar etb = new EditToolBar(this);  //99/4/15 aa
        toolbars.add("South", etb.returnToolbar());

        getContentPane().add("North", toolbars);

        // add status panel -- myStatusPanel defined in inherited EditorFrame
        myStatusPanel = new StatusPanel();
        getContentPane().add("South", myStatusPanel);

        myToolBox = new IconToolBox(this);
        myToolBox.parentFrame = this;
        getContentPane().add("West", myToolBox);


        myIconPanel = new IconPanel(this);  //00/5/07

        JScrollPane scroller = new JScrollPane(myIconPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Adjustable vadjust=scroller.getVAdjustable();
        //Adjustable hadjust=scroller.getHAdjustable();
        //hadjust.setUnitIncrement(10);
        //vadjust.setUnitIncrement(10);

        getContentPane().add("Center", scroller);

        myIconPanel.setBackground(IconPanel.currBackgroundCol);

        addWindowListener(new DWAdapter());//DWAdapter in EditorFrame

        optChoiceFrame = new OptionsFrame(this, "Hello", true);

        //System.out.println("Debug:IconEditorFrame:constructor-middle");

        //------------------------------------
        // This is really the end of the constructor - what we do next
        // depends on how the constructor was called.
        //------------------------------------
        // Now do the drawing of the module?
        if (currModule == null) {
            //System.out.println("Debug:IconEditorFrame:constructor-end1");
            return;
        }
        if (currModule.myIcon == null) {
            //System.out.println("Debug:IconEditorFrame: constructor and non null currModule");
            //boolean worked1 = executiveFrame.windowsAndModulesOpen.addInPieces("Icon", currModule);
            //if (!worked1) {
            //System.err.println("IconEditorFrame:constructor- could not add module to open windows list "+currModule.moduleName);
            //}
            finishNew();
            setTitle("Icon Editor: " + currModule.moduleName);
            //System.out.println("Debug:IconEditorFrame:constructor-end2");
            return;
        }
        // A Icon view exists by that name
        //System.out.println("Debug:IconEditorFrame: constructor and non null currIcon");
        boolean worked2 = executiveFrame.windowsAndModulesOpen.addInPieces("Icon", currModule);
        if (!worked2) {
            System.err.println("IconEditorFrame:constructor- could not add module to open windows" + currModule.moduleName);
        }
        finishOpen();
        setTitle("Icon Editor: " + currModule.moduleName);


        //System.out.println("Debug:IconEditorFrame:constructor-end3");

    } //end constructor

    //------------------------------------------------------

    /**
     * Create the menu bar.
     */
//------------------------------------------------------
    public JMenuBar CreateMenuBar() {
        JMenuItem mi;
        JMenu FileMenu;
        JMenu EditMenu;
        JMenu DefaultsMenu;
        JMenu ToolsMenu;
        JMenu HelpMenu;
        JMenuBar myMenuBar = new JMenuBar();


        FileMenu = new JMenu("File");
        FileMenu.add(mi = new JMenuItem("New"));
        mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Open"));
        mi.addActionListener(this);

        JMenuItem saveMenuItem;
        FileMenu.add(saveMenuItem = new JMenuItem("Save"));
        saveMenuItem.addActionListener(this);
        //    if (currModule==null) {
        //    	saveMenuItem.setEnabled(false);
        //    }
        FileMenu.add(mi = new JMenuItem("Save as"));
        mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Close"));
        mi.addActionListener(this);
        FileMenu.addSeparator();
        FileMenu.add(mi = new JMenuItem("Print"));
        mi.addActionListener(this);
        FileMenu.addSeparator();
        FileMenu.add(mi = new JMenuItem("CloseIconEditor"));
        mi.addActionListener(this);

        myMenuBar.add(FileMenu);

        // edit menu
        EditMenu = new JMenu("Edit");
        EditMenu.add(mi = new JMenuItem("Undo"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Redo"));
        mi.addActionListener(this);

        EditMenu.addSeparator();
        EditMenu.add(mi = new JMenuItem("Cut"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Copy"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Paste"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Clear"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Select all"));
        mi.addActionListener(this);

        EditMenu.addSeparator();
        EditMenu.add(mi = new JMenuItem("Group"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Ungroup"));
        mi.addActionListener(this);
        myMenuBar.add(EditMenu);


        //===== options menu
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.addActionListener(this);
        optionsMenu.add(mi = new JMenuItem("Graphics"));
        mi.addActionListener(this);
        myMenuBar.add(optionsMenu);

        // == help menu
        HelpMenu = new JMenu("Help");
        HelpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);
        myMenuBar.add(HelpMenu);
        //myMenuBar.setHelpMenu(HelpMenu);

        return (myMenuBar);
    }

    //--------------------------------------------------
    public void finishNew() {
        //System.out.println("Debug:IconEditorFrame:got here");

        if (!currModule.hasIcon) {
            currModule.myIcon = new Icon(currModule.libNickName, currModule.moduleName, currModule.versionName);
            currModule.hasIcon = true;
        }
        currIcon = currModule.myIcon;
        IconPanel.currBackgroundCol = OptionsFrame.drawBack_col;
        myIconPanel.setBackground(IconPanel.currBackgroundCol);
        myIconPanel.repaint();
        //System.out.println("Debug:IconEditorFrame:got here too");

        this.noActionTaken = false;
        this.savedInEd = false;

    }

    //--------------------------------------------------
    public void finishOpen() {
        boolean empty = false;
        if (!currModule.hasIcon) {//if created with schEd or nslmEd
            currModule.myIcon = new Icon(currModule.libNickName, currModule.moduleName, currModule.versionName);
            currModule.hasIcon = true;
            empty = true;
        }
        currIcon = currModule.myIcon;
        // since we zero the icon's coordinates, this just moves it
        // away from the edge
        if (!empty) {//if created with schEd or nslmEd
            currIcon.moveobj(SCSUtility.gridT2, SCSUtility.gridT2);
        }
        IconPanel.currBackgroundCol = OptionsFrame.drawBack_col;
        myIconPanel.setBackground(IconPanel.currBackgroundCol);
        myIconPanel.repaint();

        this.noActionTaken = false;
        this.savedInEd = false;

    }
    //--------------------------------------------------

    /**
     * Perform menu functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        JMenuItem mi;
        String srcPath = null;

        //System.out.println("Debug:IconEditorFrame:actionPerformed:start");
        myStatusPanel.setStatusMessage("");

        String actionLabel = "";

        if (event.getSource() instanceof JButton) {
            JButton dmi = (JButton) event.getSource();
            actionLabel = dmi.getActionCommand();
        } else if (event.getSource() instanceof JMenuItem) {
            mi = (JMenuItem) event.getSource();
            actionLabel = mi.getText();
	    /*	5/13/02 aa these are now caught above in JButton now
		} else 	if (event.getSource() instanceof IconToolBoxButton) {
	    IconToolBoxButton imi=(IconToolBoxButton)event.getSource();
	    actionLabel = imi.getText();
	    */
        }  //must allow for keystrokes as well
        //for some rease actionPerformed is getting called when Icon is newed and when first opened

        //System.out.println("Debug:IconEditorFrame:actionPerformed:Label is.." + actionLabel );
        // --------------------------------------------------
        //  File Menu
        // --------------------------------------------------
        //---------------------------------------------------
        if (actionLabel.equals("New")) {
            Module tempModule = newModule("Module");
            if (tempModule == null) {
                //System.out.println("Debug:IconEditorFrame:new module is null");
                return;
            }
            currModule = tempModule;
            //System.out.println("Debug:IconEditorFrame:got here");
            finishNew();
        }
        //---------------------------------------------------
        if (actionLabel.equals("Open")) {
            Module tempModule = openModule();
            if (tempModule == null) {
                return;
            }
            finishOpen();
        }
        //---------------------------------------------------
        if (actionLabel.equals("Save")) {
            //System.out.println("Debug:IconEditorFrame:Curr Dir is" + currModule.moduleName );
            if (!(saveModule())) {//error messages given from saveAsModule
                return;
            }
            return;
        } //end Save
        // --------------------------------------------------
        if (actionLabel.equals("Save as")) {
            if (!(saveAsModule())) {//error messages given from saveAsModule
                return;
            }
            return;
        } //end Save as
        //--------------------------------------------
        // todo: move delete module to the library manager
        //      if (actionLabel.equals("DeleteIconFile")) {
        //	FileDialog fd=new FileDialog(this, "Module Delete Dialog", FileDialog.SAVE);
        //	fd.setDirectory(".");
        //	fd.setLocation(new Point(400,200));
        //	fd.show();
        //
        //	srcPath=fd.getDirectory();
        //	String nameWSif=fd.getFile();
        //	if (nameWSif==null) return;
        //	if (!nameWSif.endsWith(".sif")) return;
        //
        //	File file=new File(srcPath,nameWSif);
        //	file.delete();
        //        savedInEd=true;
        //	return;
        //      }
        //--------------------------------------------------
        if (actionLabel.equals("Print")) {
            PrintJob pjob = getToolkit().getPrintJob(this, "Printing Icon", null);
            if (pjob != null) {
                Graphics pg = pjob.getGraphics();
                if (pg != null) {
                    myIconPanel.paintChildren(pg);
                    pg.dispose();
                }
                pjob.end();
            }
        }
        //--------------------------------------------
        if (actionLabel.equals("Close")) {
            if (!(closeModule())) {
                return;
            }
            // System.out.println("Debug:IconEditorFrame:close");
            currIcon = null;
            IconPanel.currBackgroundCol = OptionsFrame.noActionTaken_col;
            myIconPanel.setBackground(IconPanel.currBackgroundCol);
            myIconPanel.repaint();
            return;
        }
        //---------------------------------------------------
        if (actionLabel.equals("CloseIconEditor")) {
            if (!(closeModule())) {
                return;
            }
            currIcon = null;
            dispose();
            parentFrame.mySchematicPanel.requestFocus();
            return;
        }
        //---------------------------------------------------
        // Options Menu
        //---------------------------------------------------

        if (actionLabel.equals("Graphics")) {

            optChoiceFrame.setSize(350, 600); //(verical=num options +1)*24
            optChoiceFrame.setLocation(200, 200);
            optChoiceFrame.setVisible(true);
            myIconPanel.setBackground(IconPanel.currBackgroundCol);
            myIconPanel.repaint();
            return;
        }
        //---------------------------------------------------
        // Edit Menu
        //---------------------------------------------------
        //--------------------------------------------
        if (actionLabel.equals("Cut")) {
            savedInEd = false;
            removeDrawablePart();
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Delete")) {
            //System.out.println("delete seletected..");
            savedInEd = false;
            removeDrawablePart();
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Clear")) {
            if (currModule == null || currIcon == null) return;
            boolean okPressed = okCancelPopup.display("IconEditorFrame: Remove all visible items on screen?");
            if (okPressed) {
                currIcon.drawableParts.removeAllElements();
                savedInEd = false;
                myIconPanel.repaint();
            }
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Undo")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Redo")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Copy")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Paste")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Select All")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Group")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
            return;
        }
        //--------------------------------------------
        if (actionLabel.equals("Ungroup")) {
            warningPopup.display("IconEditorFrame:Sorry not implemented yet!");
        }
        //--------------------------------------------
        //warningPopup.display("IconEditorFrame:Sorry command not found!");

    }
    //----------------------------------------------

    /**
     * remove part of the icon
     */
    //----------------------------------------------
    public void removeDrawablePart() {
        //  Module currModule= currModule;
        //       Icon currIcon= currIcon;
        Declaration var;
        if (currModule == null || currIcon == null || currIcon.drawableParts == null) return;

        int count = currIcon.drawableParts.size();

        for (int i = 0; i < currIcon.drawableParts.size(); i++) {
            //System.out.println(currIcon.drawableParts.size()  );
            GraphicPart gobj = (GraphicPart) currIcon.drawableParts.elementAt(i);
            if (gobj.select == 0) continue;
            String portName = "";
            //todo: merge IconInport and IconOutport together
            if (gobj instanceof IconInport) {
                IconInport ip = (IconInport) gobj;
                if (currModule.variables != null) {
                    currModule.deleteVariable(ip.Name);
                }
            }
            if (gobj instanceof IconOutport) {
                IconOutport op = (IconOutport) gobj;
                if (currModule.variables != null) {
                    currModule.deleteVariable(op.Name);
                }
            }// end if gobj instanceof IconOutput
            // System.out.println("deleting for i = " + i );
            currIcon.deleteDrawablePart(i);
        } //for all drawableParts
        myIconPanel.repaint();
    }//end removeDrawablePart


    //--------------------------------------------------

    /**
     * Handle component events. This is added to correct the problem of losing
     * focus for keyboard input events if the window is setSized.
     */

    class CLAdapter extends ComponentAdapter {
        /**
         * Handle componentResized event.
         */
        public void componentResized(ComponentEvent event) {
            myIconPanel.requestFocus();
        }
    } //end class CLAdapter

} //end class IconEditorFrame












