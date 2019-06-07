/* SCCS %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * ModuleNewDialog - A class representing the dialog popped up when creating
 * a new model or module in the schematic editor.
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @since JDK8
 */
// todo:  fix bugs
// 1. need DWAdapter for window close - should equal cancel not ok
// 2. if you forget to type in the name it catches you,
// but when you go back and fill in the name, it does not accept it.
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Vector;

//import com.sun.java.swing.*;
//import com.sun.java.swing.border.*;

//todo: ModuleNewDialog and IconInstDialog and DeclarationDialog are very similar
// maybe merge and make subclasses

class ModuleNewDialog extends JDialog
        implements ActionListener {

    //Frame parent; 
    Choice nickAndLibChoice;
    public TextField moduleNameTF;
    public TextField moduleVersionTF;
    Choice moduleTypeChoice;
    Choice moduleBufferingChoice;
    Choice getCurrentVersionChoice;
    public TextField argumentsTF;

    //JComboBox nickAndLibList ; 
    WarningDialog warningPopup;
    WarningDialogOkCancel okCancelPopup;
    String status = "none";
    boolean thisIsAModule = false;
    public EditorFrame parentFrame;

    /*
     * @param parent           pointing to the parent--SchEditorFrame,
     *                         IconEditorFrame, or the NlsmEditorFrame
     * @param modelOrModule    string indicating Model or Module
     * *public libNickName      return the Nick name for the library
     * *public libraryPath      return the path name of the library
     * *public versionPath      return the version path
     * *public srcPath          return the path to the src directory
     * *public moduleName       return the name of the module that'll be created
     * *public versionName      return the string representing the version: 1_2_3
     * *public type             return the type of the module that'll be created
     * *public buffering        return turn port buffering to true or false
     * *public floatOrSpecificStr        return whether to use a specif version for
     *                                  submodules
     * *public floatOrSpecificInt        return whether to use a specif version for
     *                                  submodules
     * *public boolean useExistingFile=false;
     * *public boolean useOpenModule=false;
     */

    // the following are retreivable from other files
    //todo: change these to be a module, and then access as module.libNickName etc.
    // it will make things easier when items are added to Module
    public String getCurrentVersionStr = "";
    public String libraryPath = "";
    public String versionPath = "";
    public String srcPath = "";
    public String bufferingStr = "";

    public int errorStatus = -1;
    public boolean useExistingFile = false;
    public boolean foundInOtherEditor = false;

    //todo: replace following variables with a Module container

    public String libNickName = "";
    public String moduleName;
    public String versionName;
    public String type;
    public String arguments = "";
    public boolean buffering = false;
    public boolean getCurrentVersion = true;


    //------------------------- 


    /**
     * Constructor of this class, with the parent set to parentFrame.
     * @param parentFrame      pointing to the parent--SchEditorFrame,
     *                         IconEditorFrame, or the NlsmEditorFrame
     * @param modelOrModule    string indicating Model or Module
     */
    public ModuleNewDialog(EditorFrame parentFrame, String modelOrModule) {


        super(parentFrame, ("Create New " + modelOrModule + " Dialog"), true);
        Vector libList;

        //passing in parentFrame instead of this because it will
        // center itself in the parentFrame - ModuleNewDialog may
        // not be wide enough to show the message
        warningPopup = new WarningDialog(parentFrame, "Under Construction ");
        okCancelPopup = new WarningDialogOkCancel(parentFrame, "Under Construction ");

        try {
            libList = SCSUtility.readLibraryNickAndPathsFile();
        } catch (FileNotFoundException e) {
            System.err.println("Error:ModuleNewDialog:constructor: please fix the library file : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
            errorStatus = 1;
            return;
        } catch (IOException e) {
            System.err.println("Error:ModuleNewDialog:constructor: io exception with : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
            errorStatus = 2;
            return;
        }

        thisIsAModule = modelOrModule.equals("Module");

        this.parentFrame = parentFrame;
        getContentPane().setLayout(new GridLayout(9, 1));

        //1
        Panel nickAndLibPanel = new Panel();
        nickAndLibPanel.add("West", new Label("Library:"));
        nickAndLibChoice = new Choice();
        String libNickAndPath;
        int count;
        for (count = 0; count < libList.size(); count++) {
            libNickAndPath = (String) libList.elementAt(count);
            nickAndLibChoice.addItem(libNickAndPath);
        }
        nickAndLibPanel.add("East", nickAndLibChoice);
        getContentPane().add("South", nickAndLibPanel);

	/*
	  // since using GridBag must take two squares for this
	  JPanel nickAndLibPanel=new JPanel();
	  nickAndLibPanel.add( new Label("Library Name:"));
	  getContentPane().add("South", nickAndLibPanel);

	  nickAndLibList = new JComboBox(   );
	  nickAndLibList.setEditable(false);
	  String libNickAndPath=null;
	  int count = 0 ;
	  for ( count = 0 ; count < libList.size()  ; count++) {
	  libNickAndPath=(String)libList.elementAt(count);
	  nickAndLibList.addItem(libNickAndPath);
	  }
	  nickAndLibList.setSelectedIndex(0);
	  nickAndLibList.setAlignmentX(Component.LEFT_ALIGNMENT);
	  nickAndLibList.addActionListener(this);
	  //nickAndLibPanel.add(nickAndLibList);
	  getContentPane().add("South", nickAndLibList);
	*/
        //2
        //aJPanel NamePanel=new JPanel();
        Panel moduleNamePanel = new Panel();

        // added feb 18,1999  nitgupta
        // just added the labels and choice selector
        // have to handle that in action part
        moduleNamePanel.add("West", new Label("Module Name(first capital):"));
        moduleNameTF = new TextField("", SCSUtility.maxCharsInModuleName);
        moduleNamePanel.add("East", moduleNameTF);
        getContentPane().add("South", moduleNamePanel);

        //aNamePanel.add(new Label("Name:"));
        //aName=new TextField("", SCSUtility.maxCharsInModuleName);
        //aNamePanel.add( Name);

        //3
        Panel moduleVersionPanel = new Panel();
        moduleVersionPanel.add("West", new Label("Version:"));
        moduleVersionTF = new TextField("1_1_1", 11);
        moduleVersionPanel.add("East", moduleVersionTF);
        getContentPane().add("South", moduleVersionPanel);

        //4
        Panel moduleTypePanel = new Panel();
        moduleTypePanel.add("West", new Label("Type:"));
        moduleTypeChoice = new Choice();
        if (thisIsAModule) {
            moduleTypeChoice.addItem("NslModule");
            moduleTypeChoice.addItem("NslModel");
            moduleTypeChoice.addItem("NslClass");
            moduleTypeChoice.addItem("NslInModule");
            moduleTypeChoice.addItem("NslOutModule");
        } else {
            moduleTypeChoice = new Choice();
            moduleTypeChoice.addItem("NslModel");
        }

        moduleTypePanel.add("East", moduleTypeChoice);
        getContentPane().add("South", moduleTypePanel);

        //5
        Panel moduleBufferingPanel = new Panel();
        moduleBufferingPanel.add("West", new Label("Buffering:"));
        moduleBufferingChoice = new Choice();
        moduleBufferingChoice.addItem("false");
        moduleBufferingChoice.addItem("true");

        moduleBufferingPanel.add("East", moduleBufferingChoice);
        getContentPane().add("South", moduleBufferingPanel);

        //6
        Panel moduleGetCurrentVersionPanel = new Panel();
        moduleGetCurrentVersionPanel.add("West", new Label("Get Newest Version of SubModules:"));
        getCurrentVersionChoice = new Choice();
        getCurrentVersionChoice.addItem("true");
        getCurrentVersionChoice.addItem("false");

        moduleGetCurrentVersionPanel.add("East", getCurrentVersionChoice);
        getContentPane().add("South", moduleGetCurrentVersionPanel);


        //7
        Panel argumentsPanel = new Panel();
        argumentsPanel.add("West", new Label("Arguments:"));
        argumentsTF = new TextField("", SCSUtility.argumentsCharsDisplayed);
        argumentsPanel.add("East", argumentsTF);
        getContentPane().add("South", argumentsPanel);


        //8
        //Panel spacePanel=new Panel();
        //getContentPane().add("South",spacePanel);

        //9
        JPanel ButtonPanel = new JPanel();
        Button btn;
        btn = new Button("Ok");
        btn.addActionListener(this);
        ButtonPanel.add("West", btn);

        btn = new Button("Cancel");
        btn.addActionListener(this);
        ButtonPanel.add("Center", btn);

        btn = new Button("Help");
        btn.addActionListener(this);
        ButtonPanel.add("East", btn);

        getContentPane().add("South", ButtonPanel);

        addWindowListener(new DWAdapter());
    }

// Todo: The following initializer should be called from EditorFrame.SaveAs
    //public void ModuleNewDialogInit(EditorFrame parentFrame, Module currModule) {
    //
    //}


    //-------------------------------------------------
    // display function places the window and sets the size of the window
    //         :bhansali 4/5/99
    public void display(String title) {
        setTitle(title);
        setLocation(new Point(200, 200));
        setSize(600, 450);
        setVisible(true);
    }

    //-------------------------------------
/*
public static   void tagFile(  String parentDir , String filename)
    {

      try {
	FileOutputStream ostream=new FileOutputStream( new File (parentDir ,filename ).getAbsolutePath()  );	
	
	PrintWriter pout=new PrintWriter(ostream);
	
	pout.println("This is a dummy tag file .Dont remove it !!");
	pout.flush();
	pout.close();
	
      }

             catch (FileNotFoundException e) {
               System.err.println("Error:ModuleNewDialog:tag File: FileNotFoundException");

		errorStatus=3;
               return;
             }
             catch (IOException e) {
               System.err.println("Error:ModuleNewDialog:tag file: IOException");
		errorStatus=4;
               return;
             }

    }
*/
//-------------------------------------------------
    public int isValidString(String inputString) {
        if ((inputString == null) ||
                (inputString.equals("")) || (inputString.contains(" ")))

            return -1;  //error
        else
            return 0;  // no error

    }

    //-------------------------------------------------
    public int isValidType(String inputString) {
        int status = 0;

        if (inputString.equals("")) {

            status = -1;  //error
        }

        return (status);  // error
    }

    //-------------------------------------------------
    public int isValidBoolean(String inputString) {
        int status = 0;

        if (inputString.equals("")) {
            status = -1;  //error
        }

        return (status);  // error
    }

    //-------------------------------------------------
    public int isValidGetCurrentVersion(String inputString) {
        int status = 0;

        if (inputString.equals("")) {
            status = -1;  //error
        }

        return (status);  // error
    }

//-------------------------------------------------

    /**
     * Handle action events.
     */
//-------------------------------------------------
    public void actionPerformed(ActionEvent event) {

        Button btn;

        String libNickAndPath;

        if (event.getSource() instanceof Button) {
            btn = (Button) event.getSource();
            if (btn.getLabel().equals("Cancel")) {
                status = "cancel";
                setVisible(false);
                return;
            }


            if (btn.getLabel().equals("Ok")) {
                status = "ok";
                //todo: move the stuff after ok to the calling method

                //	  libraryPath = Library.getText() ;
                libNickAndPath = nickAndLibChoice.getSelectedItem();
                //System.out.println("Debug:ModuleNewDialog:libNickAndPath: "+libNickAndPath);
                libraryPath = SCSUtility.parseOutLibPathName(libNickAndPath);
                SCSUtility.setCurrLibPath(libraryPath);
                libNickName = SCSUtility.parseOutLibNickName(libNickAndPath);
                moduleName = moduleNameTF.getText();
                versionName = moduleVersionTF.getText();
                type = moduleTypeChoice.getSelectedItem();
                bufferingStr = moduleBufferingChoice.getSelectedItem();
                buffering = bufferingStr.equals("true");

                getCurrentVersionStr = getCurrentVersionChoice.getSelectedItem();
                getCurrentVersion = getCurrentVersionStr.equals("true");

                arguments = argumentsTF.getText(); //can be null

                if (isValidString(libraryPath) != 0) {
                    errorStatus = 5;
                    String errstr = "Error: ModuleNewDialog: invalid library name!";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }
                if (isValidString(moduleName) != 0) {
                    errorStatus = 6;
                    String errstr = "Error: ModuleNewDialog: invalid model name.";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }

                if (isValidString(versionName) != 0) {
                    errorStatus = 7;
                    String errstr = "Error: ModuleNewDialog: invalid version syntax.";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }

                if (isValidString(type) != 0) {
                    errorStatus = 8;
                    String errstr = "Error: ModuleNewDialog: invalid model type";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }

                if (isValidBoolean(bufferingStr) != 0) {
                    errorStatus = 9;
                    String errstr = "Error: ModuleNewDialog: invalid buffering value.";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }

                if (isValidGetCurrentVersion(getCurrentVersionStr) != 0) {
                    errorStatus = 10;
                    String errstr = "Error:ModuleNewDialog: invalid choice for float or specific version of submodules or icons.";
                    warningPopup.display(errstr);
                    setVisible(false);
                    return;
                }
                // check whether the module directory and the versiondir exists
                // assuming that library dir exists alredy

                File libFile = new File(libraryPath);

                if (!libFile.isDirectory()) {

                    errorStatus = 11;
                    warningPopup.display("ModuleNewDialog:Library does not exist.. Please check ..");

                    setVisible(false);
                    return;
                }

                // cannot use tempModule from below because the other module may not
                // have been written to disk yet.
                // otherwise it maybe ok to create that view for that module
                StringModule sm = new StringModule(parentFrame.editorType, libNickName, moduleName, versionName);
                //will not add if already there
                boolean found = EditorFrame.executiveFrame.windowsAndModulesOpen.contains(sm); //there could be many
                if (found) {
                    errorStatus = 15;
                    String errstr = "ModuleNewDialog:that module already open in this editor, " + parentFrame.editorType + "Editor";
                    warningPopup.display(errstr);
                    setVisible(false);   //todo: change to setVisible(false); etc.
                    return;
                }
                foundInOtherEditor = EditorFrame.executiveFrame.windowsAndModulesOpen.containsModule(libNickName, moduleName, versionName);
                //System.out.println("Debug:ModuleNewDialog:foundInOtherEditor"+foundInOtherEditor);
                if (foundInOtherEditor) {
                    //not necessarily an error
                    setVisible(false);   //todo: change to setVisible(false); etc.
                    return;
                }

                //now to check the module within the file
                srcPath = SCSUtility.catFullPathToSrc(libraryPath, moduleName, versionName);
                //Check if src dir already exists
                File srcFile = new File(srcPath);
                srcPath = srcFile.getAbsolutePath();
                if (!srcFile.isDirectory()) {
                    // AA: does not think dirs should be created until
                    // module is saved.
                    setVisible(false);   //todo: change to setVisible(false); etc.
                    return;
                }
                //else if the directory exists
                Module tempModule;
                try {
                    String sifPath = SCSUtility.catFullPathToSif(srcPath, moduleName);
                    FileInputStream fis = new FileInputStream(sifPath);
                    ObjectInputStream mois = new ObjectInputStream(fis); //lots of exceptions
                    //check what editor this is and do we have that view already
                    tempModule = new Module();

                    tempModule.readThruFlags(mois); //must catch exceptions
                    fis.close();
                } catch (IOException e) {
                    String errstr = "Error:ModuleNewDialog:actionPerformed: IOException";
                    warningPopup.display(errstr);
                    errorStatus = 12;
                    setVisible(false);   //todo: change to setVisible(false); etc.
                    return;
                }
                if (((parentFrame.editorType.equals("Schematic")) && (tempModule.hasSchematic)) ||
                        ((parentFrame.editorType.equals("Icon")) && (tempModule.hasIcon)) ||
                        ((parentFrame.editorType.equals("Nslm")) && (tempModule.hasNslm))) {
                    String errstr = "Error:ModuleNewDialog:cannot create a new " + parentFrame.editorType + "  by that name. Use -open- instead.";
                    warningPopup.display(errstr);
                    errorStatus = 14;
                    setVisible(false);   //todo: change to setVisible(false); etc.
                    return;
                } else {
                    useExistingFile = true;
                }
                // AA: does not think dirs should be created until
                // module is saved.
                setVisible(false);


            } //if button ==Ok
        } //if button
    } // end actionPerformed	
    //-------------------------------------------------

    /**
     * Handle window events.
     */
    class DWAdapter extends WindowAdapter {
        /**
         * Handle windowClosing event.
         */
        public void windowClosing(WindowEvent event) {
            setVisible(false);
        }
    }

}//end class ModuleNewDialog







    
