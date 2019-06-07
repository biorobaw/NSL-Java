/* SCCS  %W% ---  %G% -- %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

/**
 * manages the list of nick names and library path names in the
 * file SCS_LIBRARY_PATHS in the user's "user.home" directory.
 */
@SuppressWarnings({"Duplicates", "SpellCheckingInspection"})
public class LibraryFileManager extends JFrame implements ListSelectionListener, ActionListener {


    private boolean DEBUG = true;

    Vector<String> nickAndLibPathEntries;
    JList<String> libraryList;
    JList<String> moduleList;
    JList<String> versionList;
    JList<String> srcDocIOList;
    JList<String> finalfileList;

    WarningDialogOkCancel warningDialogOkCancel;
    WarningDialog warningDialog;

    String[] librarydata;

    String[] moduledata = {SCSUtility.blankString10};

    String[] versiondata = {SCSUtility.blankString10};

    String[] srcDocIOdata = {SCSUtility.blankString10};

    String[] filedata = {SCSUtility.blankString10};

    String[] librarypaths;
    String[] librarynicks;

    int actionPerformedStatus = 1;  //error=1, no-error=0;

    /*--------------------*/

    /**
     * constructor
     */
    public LibraryFileManager() {

        super("Library Manager ");
        nickAndLibPathEntries = null;
        librarypaths = null;

        warningDialog = new WarningDialog(this, "warning");
        warningDialogOkCancel = new WarningDialogOkCancel(this, "warning");

        // todo: change to warning popup
        try {
            nickAndLibPathEntries = SCSUtility.readLibraryNickAndPathsFile();
            if (nickAndLibPathEntries == null) {
                warningDialog.display("LibraryFileManager:Please fix the library file: " + SCSUtility.scs_library_paths_file);
                dispose();
                return;
            }
        } catch (IOException e) {
            warningDialog.display("LibraryFileManager:Please fix the library file: " + SCSUtility.scs_library_paths_file);
            dispose();
            return;
        }

        librarypaths = SCSUtility.vectorToPathArray(nickAndLibPathEntries);
        if (librarypaths == null) {
            System.err.println("Error4:LibraryFileManager:Constructor: please fix the library file : " + SCSUtility.scs_library_paths_file);
            dispose();
            return;
        }

        setMenuBar(CreateMenuBar());

        // JPanel  listLabelPanel = new JPanel();
        JPanel listPanel = new JPanel();
        JPanel listButtonPanel = new JPanel();

        //-----------------------

        // Create a JList that displays the strings in data[]

        librarydata = librarypaths;

        JPanel listPanelAA = new JPanel();
        listPanelAA.setLayout(new BorderLayout());
        JLabel libLabel = new JLabel("Library");
        listPanelAA.add("North", libLabel);

        libraryList = new JList<>(librarydata);
        libraryList.addListSelectionListener(this);
        //libraryList.setPreferredSize(new Dimension(200,400));
        libraryList.setPreferredSize(new Dimension(400, 900));
        libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        libraryList.setValueIsAdjusting(true);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane1 = new JScrollPane(libraryList);
        //    getContentPane().setLayout(new FlowLayout());
        //    getContentPane().add(scrollPane1);
        //listPanel.add(scrollPane1);
        listPanelAA.add("South", scrollPane1);


        JPanel listPanelBB = new JPanel();
        listPanelBB.setLayout(new BorderLayout());
        JLabel moduleLabel = new JLabel("Module");
        listPanelBB.add("North", moduleLabel);

        moduleList = new JList<>(moduledata);
        moduleList.addListSelectionListener(this);
        //moduleList.setPreferredSize(new Dimension(150,400));
        moduleList.setPreferredSize(new Dimension(200, 900));
        moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane2 = new JScrollPane(moduleList);
        listPanelBB.add("South", scrollPane2);

        JPanel listPanelCC = new JPanel();
        listPanelCC.setLayout(new BorderLayout());
        JLabel versionLabel = new JLabel("Version");
        listPanelCC.add("North", versionLabel);

        versionList = new JList<>(versiondata);
        versionList.addListSelectionListener(this);
        //versionList.setPreferredSize(new Dimension(150,400));
        versionList.setPreferredSize(new Dimension(50, 900));
        versionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane3 = new JScrollPane(versionList);
        listPanelCC.add("South", scrollPane3);

        JPanel listPanelCCC = new JPanel();
        listPanelCCC.setLayout(new BorderLayout());
        JLabel srcDocIOLabel = new JLabel("Src Doc IO");
        listPanelCCC.add("North", srcDocIOLabel);

        srcDocIOList = new JList<>(srcDocIOdata);
        srcDocIOList.addListSelectionListener(this);
        srcDocIOList.setPreferredSize(new Dimension(50, 900));
        srcDocIOList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane33 = new JScrollPane(srcDocIOList);
        listPanelCCC.add("South", scrollPane33);

        JPanel listPanelDD = new JPanel();
        listPanelDD.setLayout(new BorderLayout());
        JLabel filesLabel = new JLabel("Files");
        listPanelDD.add("North", filesLabel);

        finalfileList = new JList<>(filedata);
        finalfileList.addListSelectionListener(this);
        //finalfileList.setPreferredSize(new Dimension(200,400));
        finalfileList.setPreferredSize(new Dimension(200, 900));
        finalfileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane4 = new JScrollPane(finalfileList);
        listPanelDD.add("South", scrollPane4);

        listPanel.add(listPanelAA);
        listPanel.add(listPanelBB);
        listPanel.add(listPanelCC);
        listPanel.add(listPanelCCC);
        listPanel.add(listPanelDD);

        getContentPane().add("Center", listPanel);

        Button closeButton = new Button("Close");
        listButtonPanel.add(closeButton);
        closeButton.addActionListener(this);

        getContentPane().add("South", listButtonPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }


    /*--------------------*/
    static String returnLibName(String libpath) {


        //  return ( libpath.substring(libpath.lastIndexOf(File.separator)  + 1 ) );
        return libpath;


    }

    /*--------------------*/
    public int addNewLibrary(String libNickName, String libraryName) {
        int addNewLibraryStatus = 1; //error if this stays 1, 0 =ok

        //System.out.println("Debug:addNewLibrary: "+libNickName+" "+libraryName);
        // TODO: check if nickname already in use.
        File tempFile = new File(libraryName);
        String myLibStr = libNickName + "=" + libraryName;

        if (tempFile.isDirectory()) {
            // create the module
            System.err.println("Error:LibraryFileManager:lib exists " + libraryName);
            System.err.println("----Use Add instead.");
            warningDialog.display("LibraryFileManager:library exists. Use Add instead");
            return (addNewLibraryStatus);
        }
        if (!tempFile.mkdirs()) {
            String errstr = "LibraryFileManager:cound not creat the lib. Please check path " + libraryName;
            warningDialog.display(errstr);
            return (addNewLibraryStatus);
        }
        try { //open SCS LIBRARY PATHS FILE, and write nick and libPath name to it.
            SCSUtility.stackOnFile(SCSUtility.scs_library_paths_path, myLibStr);
            reloadLibPaths();
        } catch (FileNotFoundException e) {
            System.err.println("Error:Library File Manager : File not found: " + libraryName);
            return (addNewLibraryStatus);
        } catch (IOException e) {
            System.err.println("Error:Library File Manager : Error adding new library path to file " + libraryName);
            return (addNewLibraryStatus);
        }
        addNewLibraryStatus = 0;
        return (addNewLibraryStatus);
    }

    /*--------------------*/
    /* *
     * Perform menu functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {

        MenuItem mi;
        String actionLabel = "none";

        GetNamePopup pathPopup;
        GetNamePopup nickPopup;

        actionPerformedStatus = 1;  //error=1, no-error=0;


        if (event.getSource() instanceof MenuItem) {
            mi = (MenuItem) event.getSource();
            actionLabel = mi.getLabel();
        }


        if (event.getSource() instanceof Button) {
            Button dmi = (Button) event.getSource();
            actionLabel = dmi.getLabel();
        }
        if (actionLabel.equals("Add Existing Library Path")) {
            LibraryPathEditor libpathpage = new LibraryPathEditor(this);
            libpathpage.mydisplay();
            reloadLibPaths();
            return;
        }

        if (actionLabel.equals("Close")) {
            //System.out.println("Debug:LLM:Closing lib manager:");
            dispose();
        }

        if (actionLabel.equals("Delete Library")) {
            // get the selected Library  path.. Give warning...
            String errstr = "LibraryFileManger:Going to delete library : " + returnLibraryPath();
            boolean okPressed = warningDialogOkCancel.display(errstr);

            if (okPressed) {
                SCSUtility.deleteFile(returnLibraryPath(), this);
                reloadLibPaths();
            }
        }
        if (actionLabel.equals("Delete Module")) {
            // get the selected module path.. Give warning...

            String errstr = "LibraryFileManager:Going to delete Module and Versions: " + returnModulePath();
            boolean okPressed = warningDialogOkCancel.display(errstr);

            if (okPressed) {
                SCSUtility.deleteFile(returnModulePath(), this);
                reloadLibPaths();
            }
        }
        if (actionLabel.equals("Delete Version")) {
            // get the selected  version   path.. Give warning...
            String errstr = "LibraryFileManager:Going to delete  version  : " + returnVersionPath();
            boolean okPressed = warningDialogOkCancel.display(errstr);

            if (okPressed) {
                SCSUtility.deleteFile(returnVersionPath(), this);
                reloadLibPaths();
            }
        }

        //if (actionLabel.equals("Copy Library") ) {
        // get the selected Library  path.. Give warning...
      /* aa : todo: will implement later
	pathPopup = new GetNamePopup((Frame)this,"Enter From Library Path",SCSUtility.maxCharsInPath) ;
	pathPopup.setSize(300,100);
	pathPopup.setLocation(600,200);
	pathPopup.show();
	if ( pathPopup.status.equals("cancel") == true ) {
	    //if ( pathPopup.status.equals("ok") == true )
	    return();
	}
	String  fromLibraryName = pathPopup.Name.getText();

	pathPopup = new GetNamePopup((Frame)this,"Enter New Absolute Library Path",SCSUtility.maxCharsInPath ) ;
	pathPopup.setSize(300,100);
	pathPopup.setLocation(400,200);
	pathPopup.show();
	if ( pathPopup.status.equals("cancel") ) {
	    return;
	}

	// create the library
	String  toLibraryName = pathPopup.Name.getText();
	// get the nick name.. Give warning...
	nickPopup = new GetNamePopup((Frame)this,"Enter Nick Name", SCSUtility.maxCharsInNick ) ;
	nickPopup.setSize(300,100);
	nickPopup.setLocation(400,200);
	nickPopup.show();
	if ( nickPopup.status.equals("cancel") == true ) {
	    //if ( nickPopup.status.equals("ok") == true )
	    return;
	}
	// more todo here: add entry to scs_library_paths
        SCSUtility.copyDir(fromLibraryName,toLibraryName, this );
        reloadLibPaths();
      */
        //}

        //if (actionLabel.equals("Copy Module") ) {
        // get the selected module path.. Give warning...
      /* aa:todo
	 SCSUtility.copyDir(fromModuleName,toModuleName, this );
      */
        //}

        //if (actionLabel.equals("Copy Version") ) {
      /* aa:todo
	 SCSUtility.copyDir(fromVersionName,toVersionName, this );
      */

        //}

        if (actionLabel.equals("New Library")) {
            // get the library  path.. Give warning...
            pathPopup = new GetNamePopup(this, "Enter New Absolute Library Path", SCSUtility.maxCharsInPath);
            pathPopup.setSize(300, 100);
            pathPopup.setLocation(400, 200);
            pathPopup.setVisible(true);
            if (pathPopup.status.equals("cancel")) {
                //if ( pathPopup.status.equals("ok") == true )
                return;
            }
            // get the nick name.. Give warning...
            nickPopup = new GetNamePopup(this, "Enter Nick Name", SCSUtility.maxCharsInNick);
            nickPopup.setSize(300, 100);
            nickPopup.setLocation(400, 200);
            nickPopup.setVisible(true);
            if (nickPopup.status.equals("cancel")) {
                //if ( nickPopup.status.equals("ok") == true )
                return;
            }

            // create the library..
            String nickName = nickPopup.NameTF.getText();
            String libraryName = pathPopup.NameTF.getText();
            actionPerformedStatus = addNewLibrary(nickName, libraryName);
        }
//  if (actionLabel.equals("View Lib") ) {
//    TableFileSelector tfs=new TableFileSelector(this,false);
//  }   
    }
    // --------------------------------------------------

    /**
     * Create the menu bar.
     */
    public MenuBar CreateMenuBar() {

        MenuItem mi;


        Menu CatalogMenu;
        Menu DefaultsMenu;
        Menu EditMenu;
        Menu FileMenu;
        Menu OptionsMenu;
        Menu ToolsMenu;
        Menu HelpMenu;
        Menu LibraryMenu;
        // 	Menu CatalogMenu ;
        Menu DbtoolsMenu;
        Menu WhichMenu;
        Menu ImapDisplayMenu;
//    Menu ViewMenu ;
        MenuBar MyMenuBar = new MenuBar();


        // File main menu

        FileMenu = new Menu("File");

        // FileMenu.add(mi=new MenuItem("Close"));
        //     mi.addActionListener(this);
        //     FileMenu.addSeparator();

        //    FileMenu.add(mi=new MenuItem("Print"));
        //     mi.addActionListener(this);

        FileMenu.addSeparator();
        FileMenu.add(mi = new MenuItem("About"));
        mi.addActionListener(this);

        //  FileMenu.add(mi=new MenuItem("Exit"));
//     mi.addActionListener(this);
        MyMenuBar.add(FileMenu);

        // Edit Main Menu

        EditMenu = new Menu("Edit");
        EditMenu.add(mi = new MenuItem("Undo"));
        mi.addActionListener(this);
        EditMenu.add(mi = new MenuItem("Redo"));
        mi.addActionListener(this);

        EditMenu.addSeparator();

        MyMenuBar.add(EditMenu);

        // Library main menu

        LibraryMenu = new Menu("Library");

        LibraryMenu.add(mi = new MenuItem("New Library"));
        mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Add Existing Library Path"));
        mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Delete Library"));
        mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Copy Library"));
        mi.addActionListener(this);

        LibraryMenu.addSeparator();

        // LibraryMenu.add(mi=new MenuItem("New Module"));
        //     mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Delete Module"));
        mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Copy Module"));
        mi.addActionListener(this);
        LibraryMenu.addSeparator();

        //  LibraryMenu.add(mi=new MenuItem("New Version"));
        //     mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Delete Version"));
        mi.addActionListener(this);
        LibraryMenu.add(mi = new MenuItem("Copy Version"));
        mi.addActionListener(this);
        LibraryMenu.addSeparator();

        MyMenuBar.add(LibraryMenu);

//    ViewMenu  = new Menu("View Lib");
//    MyMenuBar.add(ViewMenu);


        OptionsMenu = new Menu("Options");

        //  DefaultsMenu = new Menu("Defaults");
        //     DefaultsMenu.add(mi=new MenuItem("Library Paths"));
        mi.addActionListener(this);


        MyMenuBar.add(OptionsMenu);


        //help menu
        HelpMenu = new Menu("Help");
        HelpMenu.add(mi = new MenuItem("Help"));
        mi.addActionListener(this);

        MyMenuBar.add(HelpMenu);
        MyMenuBar.setHelpMenu(HelpMenu);

        return (MyMenuBar);
    }

    // TODO: add viewing of nicknames here and in TableFileSelector
    //--------------------------------------------------------------------
    public void reloadLibPaths() {
        try {
            nickAndLibPathEntries = SCSUtility.readLibraryNickAndPathsFile();
            if (nickAndLibPathEntries == null) {
                System.err.println("Error:LibraryFileManager:reloadLibPaths: please fix the library file : " + SCSUtility.scs_library_paths_file);
                return;
            }
            librarydata = SCSUtility.vectorToPathArray(nickAndLibPathEntries);
            if (librarydata == null) {
                System.err.println("Error:LibraryFileManager:reloadLibPaths: please fix the library file : " + SCSUtility.scs_library_paths_file);
                return;
            }
            libraryList.setListData(librarydata);
            libraryList.repaint();
            moduleList.setListData(filedata);
            moduleList.repaint();
            moduleList.validate();
            versionList.setListData(versiondata);
            versionList.repaint();
            versionList.validate();
            srcDocIOList.setListData(srcDocIOdata);
            srcDocIOList.repaint();
            srcDocIOList.validate();
            finalfileList.setListData(filedata);
            finalfileList.repaint();
            finalfileList.validate();
        } catch (FileNotFoundException e) {
            System.err.println("Error:LibraryFileManager:reloadLibPaths: please fix the library file : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
        } catch (IOException e) {
            System.err.println("Error:LibraryFileManager:reloadLibPaths: io exception with : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
        }
    }

    // todo: trying to remove this method
    private String[] returnDirList(String parentdirpath) {

        //todo: convert to function and make simpler
        // I think this use to be more complicated because
        // of the old tag files.
        String[] resultList = new String[SCSUtility.maxFilesInADir];
        resultList[0] = SCSUtility.blankString10;
        File albumdir = new File(parentdirpath);

        if (!albumdir.isDirectory())
            return resultList;
        int i;
        String[] fileList = new String[SCSUtility.maxFilesInADir];

        for (i = 0; i < SCSUtility.maxFilesInADir; i++)  // todo: why fill this with "none"?
            fileList[i] = "none";

        fileList = albumdir.list(); // here we get the directory listing

        // now we
        int count = 0;

        //    System.out.println("Debug:LLM:Dir list is: " + fileList );

        assert fileList != null;
        for (i = 0; i < fileList.length; i++) {

            File tempfile = new File(parentdirpath, fileList[i]);

            // 	System.out.println(Debug:LLM:tempfile.getAbsolutePath());
            if (tempfile.isDirectory() && (!albumdir.getName().equals(SCSUtility.blankString10))) {
                {
                    resultList[count] = fileList[i];
                    count++;
                }
            }
        }
        return (resultList);
    }


//  private boolean checktag ( File  dir , String tagname )
//    {
//      if ( new File( dir, tagname).isFile() == true )
//	return true ;
//
//      return false ;
//
//    }

    private String returnLibraryPath() {
        return libraryList.getSelectedValue();

    }

    private String returnModulePath() {
        String tempA = returnLibraryPath();
        if ((tempA != null) && (libraryList.getSelectedValue() != null))
            return (tempA + File.separator + moduleList.getSelectedValue());
        else
            return (null);
    }


    private String returnVersionPath() {
        String tempB = returnModulePath();
        if ((tempB != null) && (versionList.getSelectedValue() != null))
            return (tempB + File.separator + versionList.getSelectedValue());
        else
            return null;
    }

    private String returnSrcDocIOPath() {
        String tempC = returnVersionPath();
        if ((tempC != null) && (srcDocIOList.getSelectedValue() != null))
            return (tempC + File.separator + srcDocIOList.getSelectedValue());
        else
            return null;
    }

    private String returnFilePath() {
        String tempD = returnSrcDocIOPath();
        if ((tempD != null) && (finalfileList.getSelectedValue() != null))
            return (tempD + finalfileList.getSelectedValue());
        else
            return null;
    }

    public void valueChanged(ListSelectionEvent event) {
        if (event.getSource() instanceof JList) {
            JList templist = (JList) event.getSource();

            // library selected -- handle module pane
            if (templist.equals(libraryList)) {
                //System.out.println("Debug:LLM:library  selected");

                if (libraryList.getSelectedValue() == null) return;
                //System.out.println("Debug:LibraryFileManager:Path is:" +  returnLibraryPath() );
                // System.out.println("Debug:LLM :" libraryList.getSelectedValue() );
                String[] fileList;

                File albumdir = new File(libraryList.getSelectedValue());
                //fileList = returnDirList( (String)  libraryList.getSelectedValue());
                fileList = albumdir.list();
                moduleList.setListData(fileList);
                moduleList.repaint();
                moduleList.validate();
                versionList.setListData(versiondata);
                versionList.repaint();
                versionList.validate();
                srcDocIOList.setListData(srcDocIOdata);
                srcDocIOList.repaint();
                srcDocIOList.validate();
                finalfileList.setListData(filedata);
                finalfileList.repaint();
                finalfileList.validate();
            }

            // module selected --  handle version pane

            if (templist.equals(moduleList)) {
                //System.out.println("Debug:LibraryFileManager:Module selected");

                if (moduleList.getSelectedValue() == null) return;
                String modPath = returnModulePath();
                //System.out.println("Debug:LibraryFileManager:ModPath is " +  modPath );
                //System.out.println("Debug:LibraryFileManager:  moduleList.getSelectedValue() );
                String[] fileList;
                assert modPath != null;
                fileList = (new File(modPath)).list();
                versionList.setListData(fileList);
                versionList.validate();
                versionList.repaint();
                srcDocIOList.setListData(srcDocIOdata);
                srcDocIOList.repaint();
                srcDocIOList.validate();
                finalfileList.setListData(filedata);
                finalfileList.validate();
                finalfileList.repaint();
            }
            // version selected -- handle file pane

            if (templist.equals(versionList)) {
                //System.out.println("Debug:LibraryFileManager:version   selected");
                if (versionList.getSelectedValue() == null) return;
                //System.out.println("Debug:LibraryFileManager:VersionPath is " +  returnVersionPath() );
                String[] fileList;

                fileList = (new File(Objects.requireNonNull(returnVersionPath()))).list();

                srcDocIOList.setListData(fileList);
                srcDocIOList.repaint();
                srcDocIOList.validate();
                finalfileList.setListData(filedata);
                finalfileList.validate();
                finalfileList.repaint();
            }

            // srcDocIO selected -- handle file pane

            if (templist.equals(srcDocIOList)) {
                //System.out.println("Debug:LibraryFileManager:srcDocIO   selected");
                if (srcDocIOList.getSelectedValue() == null) return;
                //System.out.println("Debug:LibraryFileManager:srcDocIOPath is " +  returnSrcDocIOPath() );
                String[] fileList;

                fileList = (new File(Objects.requireNonNull(returnSrcDocIOPath()))).list(); //throws IO

                finalfileList.setListData(fileList);
                //    finalfileList.validate();
                finalfileList.repaint();
                validate();
            }

            if (templist.equals(finalfileList)) {
                //System.out.println("Debug:LibraryFileManager: file  selected ");

                finalfileList.getSelectedValue();
                //System.out.println("Debug:LibraryFileManager: File Path is" +  returnFilePath() );
            }

            //System.out.println("Debug:LibraryFileManager: templist " +templist.toString() );

        }
    }


//  public static void main(String[] args) {
//    LibraryFileManager frame = new LibraryFileManager();
//    frame.pack();
//    frame.setSize(1000,400);
//    frame.setVisible(true);
//  }


} //end class LibraryFileManager









