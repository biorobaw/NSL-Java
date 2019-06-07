/* SCCS %W%  %G% %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project./
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Stack;
import java.io.*;


@SuppressWarnings({"Duplicates", "SpellCheckingInspection"})
class SchEditorFrame extends EditorFrame implements ActionListener {

    /**
     * SchEditorFrame - A class representing the main GUI for Schematic Editor. It's
     * composed of four main parts:
     * - a menu bar at the top
     * - a schematic tool bar under the menu bar containing image buttons for
     * immediate schematic editing operation
     * - a schematic panel occupying the most area, where to draw schematics
     * - a status panel at the bottom, used to print some status information if
     * needed
     * <p>
     * Weifang Xie, Nitin Gupta, Amanda Alexander
     * %W% , %G% , %U%
     *
     * @since JDK8
     */

    MainToolBar maintoolbar;
    EditToolBar edittoolbar;


    JPanel myToolBox = null;
    SchematicPanel mySchematicPanel = null;
    String status = "";
    Stack<Module> hierarchyStack = new Stack<>();
    private Component clipboardComponent;

    OptionsFrame optChoiceFrame;
    PortOptionsFrame portOptionsFrame;

    DeclarationDialog inputPortDeclarationDialog = null;
    DeclarationDialog outputPortDeclarationDialog = null;
    DeclarationDialog subModuleDeclarationDialog = null;

    StringModuleV windowsAndModulesOpen = null;  //Vector of Elements of String and Module
    //where the first is the name of the editor and the second is the
    // open module in that editor - or the currModule
    // Used for corridination amongst editors

    TextViewer myTextViewer = null;

    /*
     * *var       myStatusPanel   	the panel for printing status 
     *					information
     * *var       myToolBox       	the tool bar at the next to top 
     *					containing image buttons for immediate
     *                              	schematic editing operation
     * *var       mySchematicPanel	the panel where to draw schematics
     * *var       currModule      	the current module that's being 
     *					manipulated
     *
     * *var	status			a flag indicating whether it's at the
     *					phase of inserting a connection
     * *var	hierarchyStack			the stack of hierarchies of schematics
     *					such that the navigation among them is
     *					made possible 

     *					when an unfinished function is asked for 


     /** 
      * The main function of the whole system, taking responsibility to be the  
      * main window. 
      */

    public static void main(String[] args) {

        SchEditorFrame sef = new SchEditorFrame();

        //todo: put name of path in information line
        System.out.println("SchEditorFrame:Preferences are in: " + SCSUtility.scs_preferences_path);
        System.out.println("SchEditorFrame:Library List is in: " + SCSUtility.scs_library_paths_path);

        // The logo is needed for copyright protection
        Logo mylogo = new Logo(sef);
        mylogo.myDisplay();
        //todo: check the users screen dimensions
        sef.setSize(600, 600);
        sef.setVisible(true);
        sef.mySchematicPanel.requestFocus();
    }

    /**
     * Constructor of this class.
     */
    public SchEditorFrame() {
        super("Schematic");

        // initialize the library paths string
        try {
            SCSUtility.setNickAndPathFilePath();
        } catch (NullPointerException e) {
            warningPopup.display("SchEditorFrame:Null library path name.");
            return;
        }
        setTitle("Schematic Editor");
        setJMenuBar(CreateMenuBar());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setSize(new Dimension(400, 100));

        JPanel mainAndEditPanel = new JPanel(true);
        mainAndEditPanel.setLayout(new GridLayout(2, 1, 2, 2));
        maintoolbar = new MainToolBar(this);
        //getContentPane().add( maintoolbar.returnToolbar() ,BorderLayout.NORTH );
        mainAndEditPanel.add(maintoolbar.returnToolbar());
        edittoolbar = new EditToolBar(this);
        //getContentPane().add( edittoolbar.returnToolbar(), BorderLayout.NORTH);
        mainAndEditPanel.add(edittoolbar.returnToolbar());
        getContentPane().add(mainAndEditPanel, BorderLayout.NORTH);


        // read  sch panel back color from the config file
        // todo: read the rest of the color and font choices from config

        if (!(OptionsFrame.newPreferences)) {
            OptionsFrame.drawBack_col = SCSUtility.returnCol(UserPref.drawBackgroundColor);
            OptionsFrame.noActionTaken_col = SCSUtility.returnCol(UserPref.noActionTakenColor);
        }

        mySchematicPanel = new SchematicPanel(this);
        SchematicPanel.currBackgroundCol = OptionsFrame.noActionTaken_col;
        mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);


        JScrollPane scroller = new JScrollPane(mySchematicPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scroller, BorderLayout.CENTER);

        /*
	ScrollPane scroller = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
	scroller.add(mySchematicPanel);

	Adjustable vadjust=scroller.getVAdjustable(); 
	Adjustable hadjust=scroller.getHAdjustable(); 
	hadjust.setUnitIncrement(20); 
	vadjust.setUnitIncrement(20); 
	getContentPane().add(scroller, BorderLayout.CENTER); 
	*/

        // add status panel -- myStatusPanel defined in inherited EditorFrame
        myStatusPanel = new StatusPanel();
        getContentPane().add(myStatusPanel, BorderLayout.SOUTH);

        optChoiceFrame = new OptionsFrame(this, "Options", true);
        portOptionsFrame = new PortOptionsFrame(this, "Hello", true);

        addComponentListener(new CLAdapter());

        addWindowListener(new DWAdapter());  //for window closing
        windowsAndModulesOpen = new StringModuleV();

        myStatusPanel.setStatusMessage("Preferences are in: " + SCSUtility.scs_preferences_path);
    }


    // -------------------------------------------------- 

    /**
     * Create the menu bar.
     */
    public JMenuBar CreateMenuBar() {
        JMenuItem mi;

        JMenu ModelFileMenu;
        JMenu FileMenu;
        //JMenu ModuleFileMenu ;

        JMenu EditMenu;
        JMenu HierarchyMenu;
        JMenu InsertMenu;
        JMenu OptionsMenu;
        JMenu ToolsMenu;
        JMenu HelpMenu;
        JMenu DbtoolsMenu;
        JMenu ImageMapMenu;
        JMenu ImapDisplayMenu;
        JMenu ViewMenu;
        JMenuBar myMenuBar = new JMenuBar();
        JMenu BmwMenu;
        JMenu ForeignMenu;


        FileMenu = new JMenu("File");

        JMenu NewMenu = new JMenu("New");

        NewMenu.add(mi = new JMenuItem("Module"));
        mi.addActionListener(this);
        NewMenu.add(mi = new JMenuItem("Model"));
        mi.addActionListener(this);
        FileMenu.add(NewMenu);

        // File main menu

        //FileMenu = new JMenu("Module");
        //  ModuleFileMenu.add(mi=new JMenuItem("New Module"));
        // mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Open"));
        mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Save"));
        mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Save as"));
        mi.addActionListener(this);
        FileMenu.add(mi = new JMenuItem("Close"));
        mi.addActionListener(this);

        //myMenuBar.add(ModuleFileMenu);

        FileMenu.addSeparator();

        FileMenu.add(mi = new JMenuItem("Print"));
        mi.addActionListener(this);

        FileMenu.addSeparator();

        FileMenu.add(mi = new JMenuItem("Exit"));
        mi.addActionListener(this);
        myMenuBar.add(FileMenu);


        // File main menu

        // commented out Feb 9,1999 - nitgupta
        // dont need it any more

        //     ModelFileMenu = new Menu("Model");
        //     ModelFileMenu.add(mi=new MenuItem("New Model"));
        //     mi.addActionListener(this);
        //     ModelFileMenu.add(mi=new MenuItem("Open Model"));
        //     mi.addActionListener(this);
        //     ModelFileMenu.add(mi=new MenuItem("Save Model"));
        //     mi.addActionListener(this);
        //     ModelFileMenu.add(mi=new MenuItem("Save Model as"));
        //     mi.addActionListener(this);
        //     ModelFileMenu.add(mi=new MenuItem("Close Model"));
        //     mi.addActionListener(this);


        //     myMenuBar.add(ModelFileMenu);

        // Edit Main Menu

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

        EditMenu.addSeparator();

        EditMenu.add(mi = new JMenuItem("Zoom in"));
        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Zoom out"));

        mi.addActionListener(this);
        EditMenu.add(mi = new JMenuItem("Refresh"));
        mi.addActionListener(this);
        myMenuBar.add(EditMenu);


        // hier menu

        HierarchyMenu = new JMenu("Hierarchy");

        HierarchyMenu.add(mi = new JMenuItem("Descend"));
        mi.addActionListener(this);
        HierarchyMenu.add(mi = new JMenuItem("Ascend"));
        mi.addActionListener(this);

        myMenuBar.add(HierarchyMenu);
        // Insert Main  Menu

        InsertMenu = new JMenu("Insert");
        InsertMenu.add(mi = new JMenuItem("Icon"));
        mi.addActionListener(this);
        InsertMenu.add(mi = new JMenuItem("Connection"));
        mi.addActionListener(this);
        InsertMenu.add(mi = new JMenuItem("InPort"));
        mi.addActionListener(this);
        InsertMenu.add(mi = new JMenuItem("OutPort"));
        mi.addActionListener(this);
        InsertMenu.add(mi = new JMenuItem("Free text"));
        mi.addActionListener(this);
        myMenuBar.add(InsertMenu);

        //options menu
        OptionsMenu = new JMenu("Options");
        OptionsMenu.add(mi = new JMenuItem("Graphics"));
        mi.addActionListener(this);
        OptionsMenu.add(mi = new JMenuItem("Port Shapes"));
        mi.addActionListener(this);
        myMenuBar.add(OptionsMenu);


        // Tools Main Menu

        ToolsMenu = new JMenu("Tools");
        ToolsMenu.add(mi = new JMenuItem("Library Management"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("Library Path Editor"));
        mi.addActionListener(this);
        ToolsMenu.addSeparator();

        ToolsMenu.add(mi = new JMenuItem("Icon Editor"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("NSLM Editor"));
        mi.addActionListener(this);
        ToolsMenu.addSeparator();
        ToolsMenu.add(mi = new JMenuItem("Check NSLM"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("Generate NSLM"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("View NSLM"));
        mi.addActionListener(this);
        ToolsMenu.addSeparator();
        ToolsMenu.add(mi = new JMenuItem("Build Java Version"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("Simulate Using Java"));
        mi.addActionListener(this);
        ToolsMenu.addSeparator();
        ToolsMenu.add(mi = new JMenuItem("Build C++ Version"));
        mi.addActionListener(this);
        ToolsMenu.add(mi = new JMenuItem("Simulate Using C++"));
        mi.addActionListener(this);


        //    ToolsMenu.add(mi=new JMenuItem("Catalog Management"));
        //    mi.addActionListener(this);
        //   ToolsMenu.add(mi=new JMenuItem("Check model"));
        //    mi.addActionListener(this);

        //ToolsMenu.addSeparator();
        //ToolsMenu.add(mi=new MenuItem("   "));  //???? why
        //mi.addActionListener(this);

        myMenuBar.add(ToolsMenu);


        DbtoolsMenu = new JMenu("DBTools");

        BmwMenu = new JMenu("BMW");

        BmwMenu.add(mi = new JMenuItem("BMW Browser&Download"));
        mi.addActionListener(this);

        BmwMenu.add(mi = new JMenuItem("BMW Upload"));
        mi.addActionListener(this);

        DbtoolsMenu.add(BmwMenu);


        ForeignMenu = new JMenu("Foreign DB");

        ForeignMenu.add(mi = new JMenuItem("Foreign DB Manager"));
        mi.addActionListener(this);

        ForeignMenu.add(mi = new JMenuItem("Schematic Linker"));
        mi.addActionListener(this);

        ForeignMenu.add(mi = new JMenuItem("Schematic Viewer"));
        mi.addActionListener(this);


        ForeignMenu.add(mi = new JMenuItem("NSLM Linker"));
        mi.addActionListener(this);


        ForeignMenu.add(mi = new JMenuItem("NSLM Viewer"));
        mi.addActionListener(this);


        DbtoolsMenu.add(ForeignMenu);


        //----------


        // imagemap submenu
        //ImageMapMenu = new Menu("ImageMap Editor");
        //ImageMapMenu.add(mi = new JMenuItem("SDB Interface"));
        //mi.addActionListener(this);
        //ImageMapMenu.add(mi = new JMenuItem("TSDB Interface"));
        //mi.addActionListener(this);
        //ImageMapMenu.add(mi = new JMenuItem("NeuArt"));
        //mi.addActionListener(this);
        //DbtoolsMenu.add(ImageMapMenu);
        //ImapDisplayMenu = new  JMenu("ImageMap Display");
        //ImapDisplayMenu.add(mi = new JMenuItem("SDB Interface"));
        //mi.addActionListener(this);
        //ImapDisplayMenu.add(mi = new JMenuItem("TSDB Interface"));
        //mi.addActionListener(this);
        //ImapDisplayMenu.add(mi = new JMenuItem("NeuArt"));
        //mi.addActionListener(this);

        //DbtoolsMenu.add(ImapDisplayMenu);

        //-----------

        myMenuBar.add(DbtoolsMenu);

        HelpMenu = new JMenu("Help");
        HelpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);
        HelpMenu.add(mi = new JMenuItem("About"));
        mi.addActionListener(this);

        myMenuBar.add(HelpMenu);
        //myMenuBar.setHelpMenu(HelpMenu);
        return (myMenuBar);
    }

    /**
     * Perform menu functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        JMenuItem mi;
        IconInst selIconInst;
        Module selModule;
        String name;
        String dir;

        String actionLabel = "";

        //warningPopup=new WarningDialog((Frame)this, "Under Construction ");

        Object src = event.getSource();

        myStatusPanel.setStatusMessage("");

        if (event.getSource() instanceof JButton) {
            JButton dmi = (JButton) event.getSource();
            actionLabel = dmi.getActionCommand();
        }

        if (event.getSource() instanceof JMenuItem) {
            mi = (JMenuItem) event.getSource();
            //actionLabel = mi.getLabel() ;
            actionLabel = mi.getText();
        }
        // --------------------------------------------------
        //  File Menu
        // --------------------------------------------------
        // NEW or New
        // --------------------------------------------------
        //	  if (actionLabel.equals("New Module")) {
        //	  if (actionLabel.equals("New Model")) {
        if ((actionLabel.equals("Module")) ||
                (actionLabel.equals("Model"))) {
            Module tempModule = newModule(actionLabel);//modifies currModule
            if (tempModule == null) {
                return;
            }
            if (!currModule.hasSchematic) {//if created with iconEd or nslmEd
                currModule.mySchematic = new Schematic();
                currModule.hasSchematic = true;
            }
            while (!hierarchyStack.empty()) {
                hierarchyStack.pop();
            }
            SchematicPanel.currBackgroundCol = OptionsFrame.drawBack_col;
            mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            mySchematicPanel.repaint();
            return;
        }
        // --------------------------------------------------
        if (actionLabel.equals("Open")) {
            Module tempModule = openModule();//modifies currModule
            if (tempModule == null) {
                return;
            }
            while (!hierarchyStack.empty()) //start new hierarchy stack
                hierarchyStack.pop();
            if (!currModule.hasSchematic) {//if created with iconEd or nslmEd
                currModule.mySchematic = new Schematic();
                currModule.hasSchematic = true;
            }
            SchematicPanel.currBackgroundCol = OptionsFrame.drawBack_col;
            mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            mySchematicPanel.repaint();
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("Save")) {
            if (!(saveModule())) { //error messages given from saveModule
                return;
            }
            //System.out.println("Debug:SchEditorFrame:saved " +currModule.moduleName);
            return;
        } //end Save
        // --------------------------------------------------
        if (actionLabel.equals("Save as")) {
            if (!(saveAsModule())) {//error messages given from saveAsModule
                return;
            }
            SchematicPanel.currBackgroundCol = OptionsFrame.drawBack_col;
            mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            return;
        }//end Save as
        // --------------------------------------------------
        if (actionLabel.equals("Close")) {
            if (!(closeModule())) {
                return;
            }
            currModule = null;
            //currDirStr=null;

            //todo: fix greying out of menu items
            //parentFrame.saveMenuItem.setEnabled(false);//was true

            //if there is a sch on the stack, go there

            if (!hierarchyStack.empty()) {
                currModule = hierarchyStack.pop();
                SchematicPanel.currBackgroundCol = OptionsFrame.drawBack_col;
                mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                return;
            } else {
                noActionTaken = true;
                SchematicPanel.currBackgroundCol = OptionsFrame.noActionTaken_col;
                mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                mySchematicPanel.repaint();

                return;
            }
        } //end close
        //------------------------------------------------------
        if (actionLabel.equals("Print")) {

            PrintJob pjob = getToolkit().getPrintJob(this, "Printing Test", null);
            if (pjob != null) {
                Graphics pg = pjob.getGraphics();
                if (pg != null) {
                    mySchematicPanel.paintChildren(pg);
                    pg.dispose();
                }
                pjob.end();
            }
        }
        // --------------------------------------------------
        if (actionLabel.equals("Exit")) {
            //System.out.println("SchEditorFrame:actionlabel==Exit");
            if (!(exitTool())) {
                //System.out.println("SchEditorFrame:actionlabel- cancel pressed");
                return;
            }
            exitForGood(this);
            //dispose(); //00/5/2 aa
            //System.exit(0);
        }
        // --------------------------------------------------
        // Edit Menu
        // --------------------------------------------------
        // --------------------------------------------------
        if (actionLabel.equals("Undo")) {
            savedInEd = false;
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Redo")) {
            savedInEd = false;
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }

        if (actionLabel.equals("Cut")) {
            savedInEd = false;
            if (currModule == null) {
                myStatusPanel.setWarningMessage("There's no open module.");
                return;
            } else {
                // SAVE THE DELETED COMPONENT (icon,text,or port) however
                // interconnect not saved
                clipboardComponent = mySchematicPanel.deleteComp();
                mySchematicPanel.repaint();
                return;
            }

        }

        // --------------------------------------------------
        //     if (actionLabel.equals("Copy"))
        // 	{
        savedInEd = false;
        // 	  if (currModule==null) return ;

        // 	  if (currModule.mySchematic.selComponent==null) return  ;

        // 	  Component comp=currModule.mySchematic.selComponent;


        // // 	  if ((comp instanceof SchematicInport   ) || (comp instanceof   SchematicOutport)) {


        //    }//
        // --------------------------------------------------
        //      if (actionLabel.equals("Paste"))
        // 	{
        // 	  // check for the type of clipboard object
        // 	  // paste only if an icon or text

        // 	  if ( ( clipboardComponent  instanceof  Module) || (clipboardComponent  instanceof GraphicPart_text) )
        // 	    {
        // 	      Module tempMod = new Module("","","");
        // 	      tempMod = (Module) clipboardComponent.clone() ;

        // 	    currModule.mySchematic.addComponent(tempMod);
        // 	  repaint();
        // 	  return ;
        // 	    }
        // 	  else
        // 	    {
        // 	      System.err.println("Error:SchEditorFrame:Cannot  paste  this component ");
        // 	  return ;
        // 	    }


        // 	}
        // --------------------------------------------------
        if (actionLabel.equals("Clear")) {
            if (currModule == null) {
                warningPopup.display("SchEditorFrame: Nothing to clear.");
                return;
            }
            currModule.mySchematic.deleteAllDrawableObjs();
            mySchematicPanel.repaint();

            return;
        }
        // --------------------------------------------------
        if (actionLabel.equals("Select all")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Group")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Ungroup")) {

            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Zoom in")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Zoom out")) {

            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }

        // --------------------------------------------------
        if (actionLabel.equals("Refresh")) {

            mySchematicPanel.repaint();

            return;
        }

        // --------------------------------------------------
        // Hierarchy Menu
        // --------------------------------------------------

        // --------------------------------------------------
        if (actionLabel.equals("Descend")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("There's no module opened yet.");
                return;
            }
            if (currModule.mySchematic.selComponent == null ||
                    !(currModule.mySchematic.selComponent instanceof IconInst)) {
                myStatusPanel.setWarningMessage("No module selected yet.");
                return;
            }
            hierarchyStack.push(currModule);
            if (!(currModule.mySchematic.selComponent instanceof IconInst)) {
                //if port
                myStatusPanel.setWarningMessage("Can only descend into modules.");
                return;
            }
            selIconInst = (IconInst) currModule.mySchematic.selComponent;
            Module tempModule = new Module();
            try {
                tempModule.getModuleFromFileUsingNick(selIconInst.libNickName, selIconInst.moduleName, selIconInst.versionName);
            } catch (ClassNotFoundException e) {
                String errStr = "Error:SchEditorFrame:ClassNotFoundException: " + selIconInst.moduleName;
                warningPopup.display(errStr);
                return;
            } catch (FileNotFoundException e) {
                String errStr = "Error:SchEditorFrame:FileNotFoundException: " + selIconInst.moduleName;
                warningPopup.display(errStr);
                return;

            } catch (IOException e) {
                String errStr = "Error:SchEditorFrame:IOException: " + selIconInst.moduleName;
                warningPopup.display(errStr);
                return;

            }

            currModule = tempModule;
            //currDirStr=SCSUtility.getSrcPathUsingNick(currModule.libNickName,currModule.moduleName, currModule.versionName);
            setTitle("Schematic Editor: Module " + tempModule.moduleName);
            mySchematicPanel.repaint();
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("Ascend")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("There's no module opened yet.");
                return;
            }
            if (hierarchyStack.empty()) {
                myStatusPanel.setWarningMessage("You have reached the top most level.");
                return;
            }
            currModule = hierarchyStack.pop();
            //currDirStr=SCSUtility.getSrcPathUsingNick(currModule.libNickName,currModule.moduleName, currModule.versionName);
            setTitle("Schematic Editor: Module " + currModule.moduleName);
            mySchematicPanel.repaint();
            return;
        }


        // --------------------------------------------------
        // Insert Menu
        // --------------------------------------------------

        if (actionLabel.equals("Icon")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Icon mode ");
            insertIcon();

            //	    SelectIconDialog sd=new SelectIconDialog(this);
            //	    sd.setLocation(new Point(400,200));
            //	    sd.setSize(600,360);
            //	    sd.setVisible(true;
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("InPort")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Port mode ");
            insertPort("InputPort");
            return;
        }
        // --------------------------------------------------
        if (actionLabel.equals("OutPort")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Port mode ");
            insertPort("OutputPort");
            return;
        }


        // --------------------------------------------------

        if (actionLabel.equals("Connection")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Connection  mode ");
            status = "Connection";
            return;
        }


        // --------------------------------------------------

        if (actionLabel.equals("Free text")) {

            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Free Text mode ");

            GetNamePopup ie = new GetNamePopup(this, SCSUtility.maxCharsFreeText);
            ie.setSize(300, 100);
            ie.setLocation(600, 200);
            ie.setVisible(true);

            if (ie.status.equals("ok")) {
                status = "insert_text";
                SchematicPanel.freeTextString = ie.NameTF.getText();
            } else {
                //	    IconPanel.text_string  = "" ;
                status = "nothing";
            }
            return;


        }
        // --------------------------------------------------
        // Options Menu
        // --------------------------------------------------

        if (actionLabel.equals("Graphics")) {
            //	    System.out.print("Debug:SchEditorFrame:graphics selected");

            optChoiceFrame.setSize(350, 600);//veritical (num options +1)*24
            optChoiceFrame.setLocation(200, 200);
            optChoiceFrame.setVisible(true);
            mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            mySchematicPanel.repaint();
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("Port Shapes")) {
            //	    System.out.print("Debug:SchEditorFrame: Port Shapes  selected");
            portOptionsFrame.setSize(300, 300);
            portOptionsFrame.setLocation(400, 200);
            portOptionsFrame.setVisible(true);
            return;
        }


        // --------------------------------------------------
        //  Tools Menu
        // --------------------------------------------------

        if (actionLabel.equals("Library Path Editor")) {
            LibraryPathEditor libpathpage = new LibraryPathEditor(this);
            libpathpage.mydisplay();
            return;

        }
        // --------------------------------------------------
        if (actionLabel.equals("Library Management")) {

            LibraryFileManager frame = new LibraryFileManager();
            frame.pack();
            frame.setSize(1200, 400);
            frame.setVisible(true);

            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("Icon Editor")) {
            IconEditorFrame iconEF;
            selIconInst = null;
            selModule = null;

            if (currModule != null)
                if (currModule.mySchematic != null)
                    if (currModule.mySchematic.selComponent instanceof IconInst)
                        selIconInst = (IconInst) currModule.mySchematic.selComponent;

            if (selIconInst != null) {
                try {
                    selModule = selIconInst.getModuleFromFile();
                } catch (IOException e) {
                    // todo: this should bring up the warning box
                    warningPopup.display("Error:SchEditorFrame: cannot find module for " + selIconInst.moduleName);
                    return;
                }
            } else {
                selModule = currModule;
            }
            // note: selModule could be null which would bring up new
            iconEF = new IconEditorFrame(this, selModule);
            iconEF.setSize(800, 700);
            iconEF.setVisible(true);
            iconEF.myIconPanel.requestFocus();
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("NSLM Editor")) {
            NslmEditorFrame nslmEF;
            selIconInst = null;
            selModule = null;

            if (currModule != null)
                if (currModule.mySchematic != null)
                    if (currModule.mySchematic.selComponent instanceof IconInst)
                        selIconInst = (IconInst) currModule.mySchematic.selComponent;

            if (selIconInst != null)
                try {
                    selModule = selIconInst.getModuleFromFile();
                } catch (IOException e) {
                    // todo: this should bring up the warning box
                    String errStr = "Error:SchEditorFrame: cannot find module for " + selIconInst.moduleName;
                    warningPopup.display(errStr);
                    return;

                }
                //todo: maybe this is a FileNotFoundException ???

            else {
                selModule = currModule;
            }

            // note: selModule could be null; which brings up new nslmED
            nslmEF = new NslmEditorFrame(this, selModule);

            //nslmEF.setSize(600,800);   //todo:need to query the configuration
            nslmEF.setVisible(true);
            return;
        }

        // --------------------------------------------------
        if (actionLabel.equals("Check NSLM")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        if (actionLabel.equals("Generate NSLM")) {
            if (currModule == null) {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            if (currDirStr == null) currDirStr = SCSUtility.getCurrLibPath();
            if (currDirStr.equals("")) currDirStr = SCSUtility.getCurrLibPath();
            if (currDirStr.equals("")) currDirStr = SCSUtility.user_home;
            ExportNslm en = new ExportNslm(this);
            en.writeNslm(currDirStr, currModule);
            myStatusPanel.setStatusMessage("Module code generated...");
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("View NSLM")) {//note:we also have NSLM Viewer
            String currFileStr = "";
            if ((currModule != null) && (currModule.moduleName != null) && (!(currModule.moduleName.equals("")))) {
                currFileStr = currModule.moduleName + ".mod";
            }
            if (myTextViewer == null) {
                myTextViewer = new TextViewer(this);
            }
            //todo:this should be firstlib not user_home
            if (currDirStr == null) currDirStr = SCSUtility.getCurrLibPath();
            if (currDirStr.equals("")) currDirStr = SCSUtility.getCurrLibPath();
            if (currDirStr.equals("")) currDirStr = SCSUtility.user_home;
            myTextViewer.display(currDirStr, currFileStr);

            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("Build Java Version")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------

        if (actionLabel.equals("Simulate Using Java")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
            return;
        }
	/*
	  //todo: this is not actually right since you are looking for
	  // the "mod" file and not the sif file.
	  TableFileSelector tfs=null;
	  try {
	  boolean wantFile=true;
	  tfs = new TableFileSelector( this , "Model",wantFile);
	  }
	  catch (FileNotFoundException e) {
	  warningPopup.display("Error:SchEditorFrame:SimulateUsingJava: FileNotFoundException on library");
	  //todo: popup warning
	  return;
	  }	
	  catch (IOException e) {
	  warningPopup.display("Error:SchEditorFrame:SimulateUsingJava: IOException on library");
	  //todo: popup warning
	  return;
	  }
	  tfs.setLocation(new Point(300,200));
	  tfs.pack();
	  tfs.setSize(700,300);
	  tfs.setVisible(true);

	  if (tfs.pushed.equals("Cancel")) return;
	  if (tfs.pushed.equals("Ok")) {
	    
	  String modeldir;
	  String modelsif;
	  String modelWFullPath;

	  modeldir =   tfs.getSourcePath()  ;
	  modelsif = tfs.returnFileNameOnly();
	  // todo: change to look for "mod" not "sif"
	  modelWFullPath = modeldir + File.separator + modelsif  ;
	  //System.out.println("Debug:SchEditorFrame:ActionPerformed:modelWFullPath "+ modelWFullPath);

	  // todo: need to put more here
	  // add code to check if file up to date and if a exe or jar exists
	  return;
	  }
	  }
	*/
        // --------------------------------------------------

        if (actionLabel.equals("Build C++ Version")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------

        if (actionLabel.equals("Simulate Using C++")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------

        if (actionLabel.equals("BMW Browser&Download")) {
            try {
                BrowserLauncher.openURL("http://java.usc.edu/bmw_dev/apb/webdriver?MIval=/index.html");
            } catch (IOException ioe) {
                warningPopup.display("SchEditorFrame:Browser failed to launch.");
            }
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("BMW Upload")) {
            BMWUploadDialog bmwUploadDialog;
            try {
                bmwUploadDialog = new BMWUploadDialog(this, "BMW Upload");
                bmwUploadDialog.setVisible(true);
                //todo: how to pass the bmwUploadDialog.toBeZippedDir+File.separator+bmwUploadDialog.zipFileName to the html page
                BrowserLauncher.openURL("http://java.usc.edu/bmw_dev/apb/webdriver?MIval=/index-login.html");
            } catch (IOException ioe) {
                warningPopup.display("SchEditorFrame:Browser failed to launch.");
            }
            return;
        }

        // --------------------------------------------------

        if (actionLabel.equals("Foreign DB Manager")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("Schematic Linker")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("Schematic Viewer")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("NSLM Linker")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
            return;
        }
        // --------------------------------------------------

        if (actionLabel.equals("NSLM Viewer")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }
        // --------------------------------------------------
        // Help Menu
        // --------------------------------------------------

        if (actionLabel.equals("Help")) {
            warningPopup.display("SchEditorFrame: command not implemented yet:  " + actionLabel);
        }

        if (actionLabel.equals("About")) {
            Logo mylogo = new Logo(this);
            mylogo.myDisplay();
        }

    } //end action Performed

    //------------------------------------
    // insertPort
    // very similary to the one in IconToolBox
    //------------------------------------ 
    public void insertPort(String dialogType) {
        Declaration var;
        DeclarationDialog dialog = null;

        if (dialogType.equals("InputPort")) {
            if (this.inputPortDeclarationDialog == null) {
                this.inputPortDeclarationDialog = new DeclarationDialog(this, "InputPort");
            }
            dialog = this.inputPortDeclarationDialog;
        }
        if (dialogType.equals("OutputPort")) {
            if (this.outputPortDeclarationDialog == null) {
                this.outputPortDeclarationDialog = new DeclarationDialog(this, "OutputPort");

            }
            dialog = this.outputPortDeclarationDialog;
        }

        var = currModule.fillVariableName(this, dialogType, "Port Instance Name(first letter lower case): ");
        if (var == null) return;

        assert dialog != null;
        boolean okPressed = dialog.display(var);
        //  System.out.print("Debug:SchEditorFrame:insertPort middle");
        if (!okPressed) return;

        boolean okToAddPort = false;

        int porti = currModule.mySchematic.findDrawableIndex(var.varName);
        int vari = currModule.findVarIndex(var.varName);

        if ((vari == -1) && (porti == -1)) { // all new
            boolean worked2 = currModule.addVariable(var);
            okToAddPort = true;
        }
        if ((vari != -1) && (porti == -1)) { // already in variable list
            if (!var.inSch) {
                okToAddPort = true;
            }
        }
        if ((vari == -1) && (porti != -1)) { // already in draw list
            //should have been in var list already
            boolean worked2 = currModule.addVariable(var);
            okToAddPort = false;
        }
        if ((vari != -1) && (porti != -1)) { // already in both lists
            warningPopup.display("SchEditorFrame:Same name. Cannot add.");
            okToAddPort = false;
        }
        if (okToAddPort) {
            if (dialogType.equals("InputPort")) {
                currModule.mySchematic.addDrawableObj(new SchematicInport(var));
            }
            if (dialogType.equals("OutputPort")) {
                currModule.mySchematic.addDrawableObj(new SchematicOutport(var));
            }
            var.inSch = true;
            this.mySchematicPanel.repaint();
        }
    } //end insertPort

    //------------------------------------ 
    // insertIcon
    // very similary to the one in NslmEditorFrame
    //------------------------------------ 
    public void insertIcon() {

        Module iconModule;
        IconInst tempIconInst;
        Declaration var;
        DeclarationDialog dialog = null;

        String dialogType = "SubModule";
        if (this.subModuleDeclarationDialog == null) {
            this.subModuleDeclarationDialog = new DeclarationDialog(this, dialogType);
        }
        dialog = this.subModuleDeclarationDialog;

        var = currModule.fillVariableName(this, dialogType, "SubModule Instance Name (first letter lower case): ");
        if (var == null) return;

        boolean okPressed = subModuleDeclarationDialog.display(var);
        //  System.out.print("Debug:SchEditorFrame:insertIcon middle");
        if (!okPressed) {
            return;
        }

        //everything is in var except libraryPath
        String libraryPath = subModuleDeclarationDialog.libPath;

        // Now Create the Icon Inst
        iconModule = new Module();
        try { //todo: this should use the getCurrentVersion flag
            iconModule.getModuleFromFile(libraryPath, var.varType, var.modVersion);
        } catch (ClassNotFoundException e) {
            String errStr = "SchEditorFrame:Class Not Found: " + libraryPath;
            warningPopup.display(errStr);
            return;
        } catch (IOException e) {
            String errStr = "SchEditorFrame:IOException on " + libraryPath;
            warningPopup.display(errStr);
            return;
        }
        //System.out.println("Debug:SchEditorFrame:Module Template is :  " +   iconModule.moduleName ) ;
        if (iconModule.myIcon == null) {
            String errStr = "SchEditorFrame:module's Icon is null in module: " + iconModule.moduleName;
            warningPopup.display(errStr);
            return;
        }
        if (iconModule.myIcon.moduleName == null) {
            warningPopup.display("SchEditorFrame:module's Icon name is null");
            return;
        }
        tempIconInst = new IconInst(var.varName, var.modGetCurrentVersion, iconModule.myIcon, var.varParams);
        //System.out.println("Debug:SchEditorFrame:Module instance name is :  " +   tempIconInst.instanceName ) ;

        //System.out.println("Debug:SchEditorFrame:Icon Name is :  " +   iconModule.myIcon.moduleName ) ;
        if (iconModule.moduleName == null) return;
        if (this.currModule == null) return;

        int vari = currModule.findVarIndex(var.varName);
        int iconi = currModule.mySchematic.findDrawableIndex(var.varName);
        if ((vari == -1) && (iconi == -1)) { // all new
            boolean worked1 = this.currModule.mySchematic.addIconInst(tempIconInst);
            var.inSch = true;
            boolean worked2 = currModule.addVariable(var);
            this.mySchematicPanel.repaint();
            return;
        }
        if ((vari != -1) && (iconi == -1)) { // already in variable list
            if (var.varDialogType.equals("SubModule")) {
                boolean worked1 = this.currModule.mySchematic.addIconInst(tempIconInst);
                var.inSch = true;
                this.mySchematicPanel.repaint();
                return;
            }
        }
        if (vari == -1) { // already in icon list
            //should have already been in variable list
            boolean worked2 = currModule.addVariable(var);
            return;
        }
        if (iconi != -1) { // already in both lists
            warningPopup.display("SchEditorFrame:same name. Cannot add.");
        }
    }//end insertIcon


    //------------------------------------

    /**
     * Generate Nslm code  for the current module.
     */
    // -------------------------------------------------- 
    public void GenNslmCode(Module module) {
    }


    // -------------------------------------------------- 

    /**
     * Handle component events. This is added to correct the problem of losing
     * focus for keyboard input events if the window is setSized.
     */
    class CLAdapter extends ComponentAdapter {
        /**
         * Handle componentResized event.
         */
        public void componentResized(ComponentEvent event) {
            mySchematicPanel.requestFocus();
        }
    }
    // -------------------------------------------------- 


} //end Class SchEditorFrame


























