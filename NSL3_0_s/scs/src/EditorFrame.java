/* SCCS %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project./
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * EditorFrame - A class representing the main GUI for all Editor.
 * The zzzModule and exitTool methods are called from the File Menu
 * actionPerformed methods of the inherited frame.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version %I%, %G%
 * @param editorType        name of the editor:Schematic,Icon,Nslm
 */
/*
 * *var       currModule         	the current module
 *
 * *var       savedInEd               flag indicating whether a save
 *                                      has taken place
 *
 * *var       warningPopup         	a warningPopup dialog which will be popped up
 * *var       okCancelPopup         	a okCancelPopup dialog which will be popped up
 *					when an unfinished function is asked for
 *  StatusPanel myStatusPanel = null;
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

@SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
class EditorFrame extends JFrame {   //JFrames betterthan Frame

    public String editorType = "Schematic"; //Schematic, Icon, Nslm
    public boolean noActionTaken = true; // used to set background colors also

    //-------------------------------
    public boolean savedInEd = false;
    public Module currModule = null;
    public String currDirStr = "";
    public static SchEditorFrame executiveFrame = null; //this is currently the SchematicEd

    public StatusPanel myStatusPanel = null;

    private boolean debugOn = false;  //todo: debugOn and writeSuf should be in preferences
    //private boolean writeSuf=true; //just a sch dump file
    private boolean writeSuf = false; //turn off dump
    public WarningDialog warningPopup;
    public WarningDialogOkCancel okCancelPopup;
    ModuleNewDialog moduleNewDialog = null;

    static final int sizex = 800;
    static final int sizey = 600;
    static final int locationx = 50;
    static final int locationy = 50;

    //------------------------------------------------------
    //constructor
    public EditorFrame() {
        initEditorFrame("Schematic", sizex, sizey, locationx, locationy);
    }

    //------------------------------------------------------
    //constructor
    public EditorFrame(String editorTypeIn) {
        initEditorFrame(editorTypeIn, sizex, sizey, locationx, locationy);
    }

    //------------------------------------------------------
    //constructor
    public EditorFrame(String editorTypeIn, int sizexx, int sizeyy, int locationxx, int locationyy) {
        initEditorFrame(editorTypeIn, sizexx, sizeyy, locationxx, locationyy);
    }

    //------------------------------------------------------
    //init
    public void initEditorFrame(String editorTypeIn, int sizexx, int sizeyy, int locationxx, int locationyy) {
        this.editorType = editorTypeIn;

        this.setSize(sizexx, sizeyy); // aa: 01/31/01  //todo: query screen size.
        this.setLocation(new Point(locationxx, locationyy));
        warningPopup = new WarningDialog(this);
        okCancelPopup = new WarningDialogOkCancel(this);

        //todo:maybe the DWAdapter should be added here so the classes
        //inheriting EditorFrame do not need to add it.

        if (editorType.equals("Schematic")) {
            executiveFrame = (SchEditorFrame) this;
        }
    }

    //------------------------------------------------------
    // The following methods are called from the File Menu actionPerformed
    // methods
    //------------------------------------------------------
    //------------------------------------------------------

    /**
     * foundInOtherEditorOk
     */
    //------------------------------------------------------
    public Module foundInOtherEditorOk(String libNickName, String moduleName, String versionName) {
        Module returnModule;
        String errstr = "EditorFrame:that module already opened in another editor. Retrieve Data Structure?";
        boolean okPressed2 = okCancelPopup.display(errstr);
        if (okPressed2) {
            StringModule returnStringModule = executiveFrame.windowsAndModulesOpen.getElement(libNickName, moduleName, versionName);
            //return the currently opened module
            returnModule = returnStringModule.module1;
        } else returnModule = null;
        return (returnModule);
    }
    //------------------------------------------------------

    /**
     * newModule
     */
    //------------------------------------------------------
    public Module newModule(String modelOrModuleStr) {

        Module returnModule;
        if (!(this.currModule == null)) {
            String errstr = "EditorFrame:Please close the existing Module first.";
            warningPopup.display(errstr);
            return (null);
        }
        if (moduleNewDialog == null) {
            moduleNewDialog = new ModuleNewDialog(this, modelOrModuleStr);
        }
        moduleNewDialog.display("Create New " + modelOrModuleStr); //could pass in last module
        if (moduleNewDialog.status.equals("cancel")) {
            return (null);
        }
        if (moduleNewDialog.errorStatus > 0) {
            return (null);
        }
        if (debugOn) {
            System.out.println("Debug:EditorFrame:newModule1: " + moduleNewDialog.moduleName);//debugOn
        }
        //System.out.println("Debug:EditorFrame:newModule:foundInOtherEditor "+moduleNewDialog.foundInOtherEditor);//debugOn
        if (moduleNewDialog.foundInOtherEditor) {
            returnModule = foundInOtherEditorOk(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName);
            currModule = endNewModule(returnModule, moduleNewDialog.srcPath);
            return (currModule);
        } else { //if not foundInOtherEditor
            if (!(moduleNewDialog.useExistingFile)) {  //if really NEW
                returnModule = new Module(SCSUtility.sifVersionNum, moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName, moduleNewDialog.type, moduleNewDialog.arguments, moduleNewDialog.buffering, moduleNewDialog.getCurrentVersion, (new Vector()));
            } else {  //if useExistingFile
                returnModule = new Module();
                try {
                    String sifFilePath = SCSUtility.catFullPathToSif(moduleNewDialog.srcPath, moduleNewDialog.moduleName);
                    String sbkFilePath = SCSUtility.catFullPathToSbk(moduleNewDialog.srcPath, moduleNewDialog.moduleName);
                    SCSUtility.copyModuleFile(sifFilePath, sbkFilePath);
                    returnModule.getModuleFromFileUsingNick(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName);
                } catch (IOException e) {
                    String errstr2 = "Error:EditorFrame:newModule1:IOException: " + moduleNewDialog.moduleName;
                    warningPopup.display(errstr2);
                    return (null);
                } catch (ClassNotFoundException e) {
                    String errstr = "Error:EditorFrame:newModule3:ClassNotFoundException " + moduleNewDialog.moduleName;
                    warningPopup.display(errstr);
                    return (null);
                }
            }
        }
        if (debugOn) {
            System.out.println("Debug:EditorFrame:newModule2: " + returnModule.moduleName);//debugOn
        }
        endNewModule(returnModule, moduleNewDialog.srcPath);
        return (currModule);
    }

    //---------------------------------------------
    private Module endNewModule(Module returnModule, String sourcePath) {
        this.currModule = returnModule;
        this.noActionTaken = false;
        this.savedInEd = false;
        this.currDirStr = sourcePath;
        this.setTitle(editorType + " Editor: " + this.currModule.moduleName);
        this.myStatusPanel.setStatusMessage("creating " + currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
        boolean worked3 = executiveFrame.windowsAndModulesOpen.addInPieces(editorType, currModule);
        if (!worked3) {
            System.err.println("EditorFrame:newModule2: could not add to windows vector :" + returnModule.moduleName);//debugOn
            return (null);
        }
        return (currModule);
    }
    //---------------------------------------------------------

    /**
     * openModule
     *
     * @return a Module
     * Note:
     * There are three editors: Schematic, Icon, and Nslm.  All three call
     * this method.
     * If a module is open in the Schematic Editor and then you call either
     * the Icon or the Nslm editor, then the editor comes up with the same
     * module in it.  However, if you then go back to the Sch and change it,
     * the changes will not be reflected in the copy of the module that is
     * being modified by the Icon editor or the Nslm editor.  Thus if you
     * then save from the Icon editor or the Nslm editor, you may not have
     * the changes made then in the Schematic editor.
     * Also, you could seperately bring up the same module in all three editors,
     * and change them idependently and have the same consistency problem.
     * <p>
     * Thus, we have a list of all of the modules being edited at the current time.
     * When one of the editors updates a module, it writes it to the global
     * version of the module.
     */
    //---------------------------------------------------------
    public Module openModule() {
        Module returnModule;
        String nameWsif = null;
        TableFileSelector tfs;
        String sourcePath;

        if ((this.currModule != null) && (!this.savedInEd) && (!noActionTaken)) {
            String errstr = "Current module is not saved. Please save or close first.";
            warningPopup.display(errstr);
            return (null);
        }

        try {
            tfs = new TableFileSelector(this, "Module", true);
        } catch (FileNotFoundException e) {
            String errstr = "EditorFrame:openModule: problem with lib - FileNotFoundException";
            warningPopup.display(errstr);
            return (null);
        } catch (IOException e) {
            String errstr = "EditorFrame:openModule: problem with lib - IOException";
            warningPopup.display(errstr);
            return (null);
        }
        tfs.setLocation(new Point(200, 200));
        tfs.pack();
        tfs.setSize(700, 275);
        tfs.setVisible(true);
        //after

        if (tfs.pushed.equals("Cancel")) {
            return (null);
        }
        if (tfs.pushed.equals("Ok")) {
            String libNickName = tfs.returnLibraryNickName();
            String moduleName = tfs.moduleName;
            String versionName = tfs.versionName;
            if ((libNickName == null) || (moduleName == null) || (versionName == null)) {
                String errstr = "EditorFrame:openModule: problem with lib or java.";
                warningPopup.display(errstr);
                return (null);
            }

            String nameWSif = tfs.returnFileNameOnly();
            sourcePath = tfs.getSourcePath();
            String sifFilePath = tfs.returnFilePath();
            String sbkFilePath = SCSUtility.getSbkString(sifFilePath);
            if (!nameWSif.endsWith(".sif")) {
                warningPopup.display("EditorFrame:Please select a file with extension .sif only");
                return (null);
            }
            StringModule sm = new StringModule(editorType, libNickName, moduleName, versionName);
            //will not add if already there
            boolean found = executiveFrame.windowsAndModulesOpen.contains(sm); //there could be many
            if (found) {
                String errstr = "EditorFrame:that module already open in this editor, " + editorType + "Editor";
                warningPopup.display(errstr);
                return (null);
            }
            boolean foundInOtherEditor = executiveFrame.windowsAndModulesOpen.containsModule(libNickName, moduleName, versionName);
            if (foundInOtherEditor) {
                returnModule = foundInOtherEditorOk(libNickName, moduleName, versionName);
                if (returnModule == null) { //cancel
                    return (null);
                } else {
                    endOpenModule(returnModule, sourcePath);
                    return (currModule);
                }
            }

            // now try to open the file
            try {
                SCSUtility.copyModuleFile(sifFilePath, sbkFilePath);
                if (debugOn) {
                    System.out.println("Debug:EditorFrame:File name is:" + sifFilePath);       //debugOn
                }
                File fii = new File(sifFilePath);
                FileInputStream fis = new FileInputStream(fii);
                ObjectInputStream ois = new ObjectInputStream(fis);
                returnModule = new Module();
                returnModule.read(ois);   // read the module info
                fis.close();
            }
            //  display  error messages
            catch (FileNotFoundException e) {
                String errstr = "Error:EditorFrame:openModule: FileNotFoundException " + sifFilePath;
                warningPopup.display(errstr);
                return (null);
            } catch (ClassNotFoundException e) {
                String errstr = "Error:EditorFrame:Open Module: ClassNotFoundException " + sifFilePath;
                warningPopup.display(errstr);
                return (null);
            } catch (IOException e) {
                String errstr = "Error:EditorFrame:Open Module: IOException " + sifFilePath;
                warningPopup.display(errstr);
                return (null);
            }
            currModule = endOpenModule(returnModule, sourcePath);
            return (currModule);
        } //if ok pushed

        //should not get here
        return (null);
    }//end openModule

    //---------------------------------------------
    private Module endOpenModule(Module returnModule, String sourcePath) {
        this.currModule = returnModule;
        this.noActionTaken = false;
        this.savedInEd = false;
        this.currDirStr = sourcePath;

        this.setTitle(this.editorType + " Editor: " + this.currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
        this.myStatusPanel.setStatusMessage("Opened " + this.currModule.moduleName);
        boolean worked3 = executiveFrame.windowsAndModulesOpen.addInPieces(editorType, currModule);
        if (!worked3) {
            System.err.println("Debug:EditorFrame:endOpenModule: could not add to windows vector :" + returnModule.moduleName);//debugOn
        }
        return (currModule);
    }
    //---------------------------------------------

    /**
     * saveModule
     */
    //---------------------------------------------
    public boolean saveModule() {
        String sifFilePath = null; //
        String sufFilePath; //

        if (noActionTaken) {
            String errstr = "EditorFrame:no action has been taken";
            warningPopup.display(errstr);
            return (false);
        }
        if (this.currModule == null) {
            String errstr = "EditorFrame:There is no module opened yet.";
            warningPopup.display(errstr);
            return (false);
        }

        //if (this.currDirStr==null) {
        //	    String errstr="Error:EditorFrame:current directory is null";
        //	    System.err.println(errstr);
        //	    warningPopup.setMsg(errstr);
        //	    warningPopup.show();
        //	    return(false);
        //	}
        //sifFilePath=this.currDirStr+ File.separator+this.currModule.moduleName+".sif";
        try {
            currDirStr = SCSUtility.getSrcPathUsingNick(currModule.libNickName, currModule.moduleName, currModule.versionName);
            sifFilePath = SCSUtility.catFullPathToSif(currDirStr, currModule.moduleName);
            sufFilePath = SCSUtility.catFullPathToSuf(currDirStr, currModule.moduleName);

            //System.out.println("Debug:EditorFrame:saveModule:curDirStr "+currDirStr);
            File currDir = new File(currDirStr);
            if (!currDir.isDirectory()) {
                currDir.mkdirs();
            }
            assert sifFilePath != null;
            File sifFile = new File(sifFilePath);
            FileOutputStream fos = new FileOutputStream(sifFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //5/02 now open the dump file
            File sufFile;
            FileOutputStream fos2;
            PrintWriter pw2;
            if (writeSuf) {  //yes, do a dump of the module?
                sufFile = new File(sufFilePath);
                fos2 = new FileOutputStream(sufFile);
                pw2 = new PrintWriter(fos2);
                currModule.write(oos, pw2);
                oos.flush();
                fos.close();
                pw2.close();
            } else {  //no, do a dump of the module?
                currModule.write(oos);
                oos.flush();
                fos.close();
            }
        } catch (IOException e) {
            String errstr = "EditorFrame:save:IOException: " + sifFilePath;
            warningPopup.display(errstr);
            return (false);
        }
        //	catch (FileNotFoundException e) {
        //	    String errstr;
        //	    errstr="Error:EditorFrame:save:FileNotFoundException: "+SCSUtility.scs_library_paths_path;
        //	    System.err.println(errstr);
        //	    warningPopup.setMsg(errstr);
        //	    warningPopup.show();
        //	    return(do_it);
        //	}
        this.savedInEd = true;
        this.myStatusPanel.clearWarningMessage();

        return true;
    } //end save

    //---------------------------------------------

    /**
     * addToTheFollowingModule
     */
    //---------------------------------------------
    private void addToTheFollowingModule(Module saveToModule) {
        if ((editorType.equals("Schematic")) && (!(saveToModule.hasSchematic))) {
            saveToModule.hasSchematic = true;
            saveToModule.mySchematic = currModule.mySchematic;
        }
        if ((editorType.equals("Icon")) && (!(saveToModule.hasIcon))) {
            saveToModule.hasIcon = true;
            saveToModule.myIcon = currModule.myIcon;
        }
        if ((editorType.equals("Nslm")) && (!(saveToModule.hasNslm))) {
            saveToModule.hasNslm = true;
            saveToModule.myNslm = currModule.myNslm;
        }
    }
    //---------------------------------------------

    /**
     * saveAsModule
     */
    //---------------------------------------------
    public boolean saveAsModule() {
        String sifFilePath; //
        String sufFilePath; //
        Module saveToModule;

        //todo:should be able to do a saveas when a module has been
        // automatically loaded
        if (noActionTaken) {
            String errstr = "Error:EditorFrame:saveAsModule:no action has been taken";
            warningPopup.display(errstr);
            return false;
        }

        if (this.currModule == null) {
            String errstr = "Error:EditorFrame:saveAsModule:There's no module opened yet.";
            warningPopup.display(errstr);
            return false;
        }
        if (moduleNewDialog == null) {
            moduleNewDialog = new ModuleNewDialog(this, "Module");
        }
        moduleNewDialog.display("Save As");
        if (moduleNewDialog.status.equals("cancel")) {
            return false;
        }
        if (moduleNewDialog.errorStatus > 0) {
            return false;
        }
        if (moduleNewDialog.moduleName == null) {
            String errstr = ":EditorFrame:new module name is null ";
            warningPopup.display(errstr);
            return false;
        }
        Module otherEditorModule;
        if (moduleNewDialog.foundInOtherEditor) {
            otherEditorModule = foundInOtherEditorOk(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName);
            if (otherEditorModule == null) {
                return false;
            } else {
                addToTheFollowingModule(otherEditorModule);
                boolean rpl = executiveFrame.windowsAndModulesOpen.replace(editorType, currModule, editorType, otherEditorModule);
                endSaveAs(otherEditorModule);
                return true;
            }
        }
        //not found in other editor
        sifFilePath = SCSUtility.catFullPathToSif(moduleNewDialog.srcPath, moduleNewDialog.moduleName);
        sufFilePath = SCSUtility.catFullPathToSif(moduleNewDialog.srcPath, moduleNewDialog.moduleName);
        //try and merge the modules
        try {
            File newSrcDir = new File(moduleNewDialog.srcPath);
            if (!newSrcDir.isDirectory()) {
                newSrcDir.mkdirs();
            }
            if (!moduleNewDialog.useExistingFile) { //New Module
                currModule.setHeaderOfModule(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName, moduleNewDialog.type, moduleNewDialog.arguments, moduleNewDialog.buffering, moduleNewDialog.getCurrentVersion, currModule.variables);
                saveToModule = currModule; //because of endSaveAs
            } else { //read the module we wanted to save this as
                saveToModule = new Module();
                saveToModule.getModuleFromFileUsingNick(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName);
                //todo: cannot decide on whether the new module should have
                //new selections or old info in the existing module
                saveToModule.setHeaderOfModule(moduleNewDialog.libNickName, moduleNewDialog.moduleName, moduleNewDialog.versionName, moduleNewDialog.type, moduleNewDialog.arguments, moduleNewDialog.buffering, moduleNewDialog.getCurrentVersion, currModule.variables);
                //I can only save this view and the varible list into the new module
                addToTheFollowingModule(saveToModule);
                currModule = saveToModule;
            } //end else
            File sifFile = new File(sifFilePath);
            FileOutputStream fos = new FileOutputStream(sifFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            this.currModule.write(oos);
            oos.flush();
            fos.close();
            if (writeSuf) {  //do a dump of the module?
                File sufFile = new File(sufFilePath);
                FileOutputStream fos2 = new FileOutputStream(sufFile);
                PrintWriter pw2 = new PrintWriter(fos2);
                currModule.writeAllChars(pw2);
                pw2.close();
            }
        } catch (IOException e) {
            String errstr;
            errstr = "Error:EditorFrame:saveAsModule:IOException: " + sifFilePath;
            warningPopup.display(errstr);

            return false;
        } catch (ClassNotFoundException e) {
            String errstr;
            errstr = "Error:EditorFrame:newModule3:ClassNotFoundException " + sifFilePath;

            warningPopup.display(errstr);
            return false;
        }
        endSaveAs(saveToModule);
        return true;
    } //end save-as

    //---------------------------------------------
    private void endSaveAs(Module returnModule) {
        this.currModule = returnModule;
        this.savedInEd = true;
        this.setTitle(this.editorType + " Editor: " + this.currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
    }

    //---------------------------------------------

    /**
     * closeModule
     */
    //---------------------------------------------
    public boolean closeModule() {
        String sifFilePath = null; //

        if (this.currModule == null) {
            //could be null if they tried to quit without saving first
            // and ok-close anyway was selected
            //warningPopup.display("EditorFrame:closeModule: null currModule");
            endCloseTool();
            return true; //todo: verify you do not need finally stuff
        }
        if (noActionTaken) {
            StringModule sm = new StringModule(editorType, currModule);
            boolean worked2 = executiveFrame.windowsAndModulesOpen.remove(sm);
            if (!worked2) {
                String errstr2 = "EditorFrame: could not remove module from vector";
                warningPopup.display(errstr2);
            }
            endCloseTool();
            return true;
        }
        if (!(this.savedInEd)) {
            String errstr = "EditorFrame:Module is not saved. Do you really want to quit?";
            boolean okPressed = okCancelPopup.display(errstr);
            if (!okPressed) {
                return false;
            }
        }

        StringModule sm = new StringModule(editorType, currModule);
        boolean worked2 = executiveFrame.windowsAndModulesOpen.remove(sm);
        if (!worked2) {
            String errstr2 = "EditorFrame: could not remove module from vector";
            warningPopup.display(errstr2);
            //close anyway - may lead to errors later though
        }
        endCloseTool();
        return true;
    } //end close

    //---------------------------------------------
    public void endCloseTool() {
        this.setTitle(editorType + "Editor");
        this.noActionTaken = true;
        this.savedInEd = true; //not really but it is like it is
        this.myStatusPanel.clearWarningMessage();
        this.currDirStr = null;
        this.currModule = null;
    }
    //---------------------------------------------

    /**
     * exitTool
     */
    //---------------------------------------------
    public boolean exitTool() {
        boolean worked;
        worked = closeModule();
        return (worked);
    }

    //---------------------------------------------

    /**
     * exitForGood
     */
    //---------------------------------------------
    public boolean exitForGood(Window w) {
        //System.out.println("Debug:EditorFrame:exitForGood");
        int sz = executiveFrame.windowsAndModulesOpen.size();
        if (sz <= 0) { //windows may be open, but nothing in them
            w.dispose();
            System.err.println("EditorFrame: going to exit");
            System.exit(0);
        } else {
            StringBuilder winstr = new StringBuilder();//list first 5 window open
            StringModule sm1 = (StringModule) executiveFrame.windowsAndModulesOpen.elementAt(0);
            Module m1 = sm1.module1;
            if (sm1.module1 != null) {
                winstr = new StringBuilder(m1.moduleName);
            }
            for (int i = 1; ((i < sz) && (i < 5)); i++) {
                sm1 = (StringModule) executiveFrame.windowsAndModulesOpen.elementAt(i);
                m1 = sm1.module1;
                if (sm1.module1 != null) {
                    winstr.append(",").append(m1.moduleName);
                }
            }

            String errstr2 = "EditorFrame:" + winstr + " is open. Exit anyway?";
            boolean okPressed2 = okCancelPopup.display(errstr2);
            if (okPressed2) {
                w.dispose();
                System.err.println("EditorFrame: going to exit");
                System.exit(0);
            }
        }
        return (false);
    }

    //------------------------------------------------
    class DWAdapter extends WindowAdapter {
        /**
         * Handle windowClosing event.
         */
        public void windowClosing(WindowEvent event) {
            String errstr = "EditorFrame:Do you really want to close the window?";
            boolean okPressed = okCancelPopup.display(errstr);
            //should not know about subclasses but o-well
            if (okPressed) {
                Window w = event.getWindow();
                if (currModule == null) {
                    if (editorType.equals("Schematic")) { //only one for now
                        exitForGood(w);
                    } else {
                        w.dispose();
                    }
                    return;
                }
                //else currModule open
                if ((editorType.equals("Icon")) || (editorType.equals("Nslm"))) {
                    StringModule sm = new StringModule(editorType, currModule);
                    boolean worked3 = executiveFrame.windowsAndModulesOpen.remove(sm); //there could be many
                    if (!worked3) {
                        String errstr4 = "EditorFrame: could not remove module from vector on windowClosing";
                        warningPopup.display(errstr4);
                    }
                    w.dispose();
                }
                if (editorType.equals("Schematic")) { //only one for now
                    //if you are exiting the Schematic editor
                    //everything else is going down too!
                    boolean worked = exitForGood(w);
                } //if schematic
            }// if okPressed
        } //windowClosing
    } //end inner class
    //----------------------------------------------------------------
} //end EditorFrame








