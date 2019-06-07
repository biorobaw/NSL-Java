/* SCCS %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * ExportNslm - A class representing the main GUI for Nslm Text Editor.
 * It's composed of several parts:
 * - a menu bar at the top
 * - a declaration section for editing variables of leaf module's Nslm
 * codes
 * <p>
 * - an Other AllMethods section for editing other methods of the module
 *
 * @author Alexander
 * @version %I%, %G%
 * @param parentFrame    the frame from which this was launched
 * @param currModule    current active module to be manipulated
 */

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.*;

//----------------------------------------------------------
// todo: all recursive generation of mod files
//----------------------------------------------------------
@SuppressWarnings("SpellCheckingInspection")
class ExportNslm {

    JFrame parentFrame;
    File lastExportFile = null;
    String lastExportFileStr = "";
    WarningDialog warningPopup;
    WarningDialogOkCancel okCancelPopup;
    MessageDialog messagePopup;

    /**
     * constructor
     */
    //--------------------------------------------------------------
    public ExportNslm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        warningPopup = new WarningDialog(parentFrame);
        okCancelPopup = new WarningDialogOkCancel(parentFrame);
        messagePopup = new MessageDialog(parentFrame);
    }

    //--------------------------------------------------------------
    public Module openModule(String currDirStr, Module currModule) {   //from EditorFrame class

        Module newModule;
        String newModuleName = "";
        String nameWSif;
        String sifFilePath = "";

        TableFileSelector tfs;

        //System.out.println("Debug:ExportNslm:exportNslm");

        try {
            if ((currDirStr != null) && (!(currDirStr.equals("")))) {
                if (currModule != null) {
                    tfs = new TableFileSelector(parentFrame, "Module", true, currDirStr, currModule);
                } else {
                    tfs = new TableFileSelector(parentFrame, "Module", true, currDirStr);
                }
            } else {
                tfs = new TableFileSelector(parentFrame, "Module", true);
            }
        }//end try

        catch (FileNotFoundException e) {
            String errstr = "EditorFrame:exportNslm: problem with lib - FileNotFoundException";
            warningPopup.display(errstr);
            return (null);
        } catch (IOException e) {
            String errstr = "EditorFrame:exportNslm: problem with lib - IOException";
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
            newModuleName = tfs.moduleName;
            nameWSif = tfs.returnFileNameOnly();
            sifFilePath = tfs.returnFilePath();
            if (!(nameWSif.endsWith(".sif"))) {
                warningPopup.display("EditorFrame:Please select a file with extension .sif only");
                return (null);
            }
        }

        // now try to open the file
        try {
            //System.out.println("Debug:ExportNslm:File is:"+sifFilePath );
            File fii = new File(sifFilePath);
            FileInputStream fis = new FileInputStream(fii);
            ObjectInputStream ois = new ObjectInputStream(fis);
            newModule = new Module();
            newModule.read(ois);   // throws ClassNotFoundException
            fis.close();
        }
        //  display  error messages
        catch (FileNotFoundException e) {
            String errstr;
            errstr = "ExportNslm:exportNslm: FileNotFoundException " + sifFilePath;
            warningPopup.display(errstr);
            return (null);
        } catch (ClassNotFoundException e) {
            String errstr;
            errstr = "ExportNslm:exportNslm: ClassNotFoundException " + sifFilePath;
            warningPopup.display(errstr);
            return (null);

        } catch (IOException e) {
            String errstr;
            errstr = "Error:ExportNslm:exportNslm: IOException " + sifFilePath;
            warningPopup.display(errstr);
            return (null);
        }
        return (newModule);
    }

    //--------------------------------------------------------------
    public File getOutputFile(Module selectedModule) {
        File fileSelected;
        if (selectedModule == null) {
            warningPopup.display("ExportNslm:no module selected");
            return null;
        }
        if (selectedModule.moduleName.equals("")) {
            warningPopup.display("ExportNslm:module name is empty");
            return null;
        }
        if (selectedModule.versionName.equals("")) {
            warningPopup.display("ExportNslm:module version is empty");
            return null;
        }
        String srcPath2;
        try {
            srcPath2 = SCSUtility.getSrcPathUsingNick(selectedModule.libNickName, selectedModule.moduleName, selectedModule.versionName);
        } catch (FileNotFoundException e) {
            warningPopup.display("ExportNslm: current module sif file not found: " + selectedModule.moduleName);
            return null;
        } catch (IOException e) {
            warningPopup.display("ExportNslm: current module IOException while looking for: " + selectedModule.moduleName);
            return null;
        }

        //System.out.println("Debug:ExportNslm:srcPath2 "+srcPath2);

        JFileChooser chooser = new JFileChooser();
        assert srcPath2 != null;
        chooser.setCurrentDirectory(new File(srcPath2));
        chooser.setSelectedFile(new File(selectedModule.moduleName + ".mod")); //export extension

        int option = chooser.showOpenDialog(parentFrame);//needs JFrame
        if (!(option == JFileChooser.APPROVE_OPTION)) { //cancel
            return null;
        }
        fileSelected = chooser.getSelectedFile();
        if (fileSelected == null) {
            warningPopup.display("ExportNslm:file selected was null");
            return null;
        }
        return (fileSelected);

    }

    //---------------------------------------------------------------
    public void writeNslm(String currDirStr, Module currModule) {
        //currModule could be null or not the one the user wants
        Module selectedModule = openModule(currDirStr, currModule);   //from EditorFrame class
        if (!selectedModule.hasNslm) {
            warningPopup.display("ExportNslm:This module does not hava a NSLM view.  Please open with NSLM Editor.");
            return;
        }
        File lastExportFile = getOutputFile(selectedModule);

        //lastExportFileStr=lastExportFile.getName();//just the file name
        if (lastExportFile == null) { //cancel pushed
            return;
        }

        lastExportFileStr = lastExportFile.toString();//full path

        //System.out.println("Debug:ExportNslm: "+lastExportFileStr+" "+lastExportFileStr2);
        int dot = lastExportFileStr.lastIndexOf('.');
        String backupFileStr = lastExportFileStr.substring(0, dot) + ".mbk"; //mod backup
        try { //todo: maybe we should keep 2 backups
            if (lastExportFile.exists()) {
                SCSUtility.copyStringFile(lastExportFileStr, backupFileStr);
            }
            FileOutputStream fos3 = new FileOutputStream(lastExportFile);
            PrintWriter pw3 = new PrintWriter(fos3);

            selectedModule.writeNslm(pw3);
            pw3.close();
        } catch (FileNotFoundException e) {
            warningPopup.display("ExportNslm: writeNslm FileNotFoundException");
        } catch (IOException e) {
            warningPopup.display("ExportNslm: writeNslm IOException");
        } catch (BadLocationException e) {
            warningPopup.display("ExportNslm: writeNslm BadLocationException");
        }
        messagePopup.display("ExportNslm: successfully wrote file: " + lastExportFileStr);
    }//end display
//------------
}//end exportNslm

