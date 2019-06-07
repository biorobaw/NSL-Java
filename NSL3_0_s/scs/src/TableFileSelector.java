/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


// since java 1.2
//

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

@SuppressWarnings("Duplicates")
public class TableFileSelector extends JDialog implements ListSelectionListener, ActionListener {

    // TODO: if only model, then do not display the module files

    private boolean DEBUG = true;

    private final static int maxElementsInDir = 100;
    /*1 3 5 7 9 1 3 5 7 9 */
    private final static String blankString = "                    ";

    boolean returnFileFlag = true; // false if just viewing
    Frame parent;
    Module inModule = null;
    String inDirStr = "";

    Vector<String> libraries = new Vector<>();
    JList<String> libraryList;
    JList<String> moduleList;
    JList<String> versionList;
    JList finalfileList;
    JScrollPane libraryScrollPane, moduleScrollPane, versionScrollPane;
    //JScrollPane finalfileScrollPane;

    String[] libraryPaths;
    String[] libraryNickNames;

    String[] moduledata = new String[maxElementsInDir];
    String[] moduledataBlank = new String[maxElementsInDir];
    String[] versiondata = new String[maxElementsInDir];
    String[] versiondataBlank = new String[maxElementsInDir];
    //String   filedata[] = new String[maxElementsInDir] ;
    //String[] filedata = { "                    " };

//todo: change tfs to be like the other dialogs and do a display
    //instead of all these public variables

    // the following include the path and the name 
    public String libraryPath = "";
    public String modulePath = "";
    public String versionPath = "";
    public String srcPath = "";
    public String filePath = "";

    // the following do not have path info except for libraryName
    public String libraryName = "";
    public String moduleName = "";
    public String versionName = "";
    public String fileName = "";


    // the pushed is used to figure out whether ok or cancel pushed.
    public String pushed = "Cancel";

    /*-----------------------------------------------------*/

    /**
     * TableFileSelector
     *
     * @param fm               frame
     * @param modelOrModuleStr model or module
     * @param returnFileFlag   return file flag
     */
    public TableFileSelector(Frame fm, String modelOrModuleStr, boolean returnFileFlag) throws IOException {

        super(fm, "Select File From Table", true);
        inDirStr = "";
        inModule = null;
        initTableFileSelector(fm, modelOrModuleStr, returnFileFlag, inDirStr, null);
    }


    /*-----------------------------------------------------*/

    /**
     * TableFileSelector
     *
     * @param fm               frame
     * @param modelOrModuleStr model or module
     * @param returnFileFlag   return file flag
     * @param inDirStr         in dir stir
     */
    public TableFileSelector(Frame fm, String modelOrModuleStr, boolean returnFileFlag, String inDirStr) throws IOException {

        super(fm, "Select File From Table", true);
        inModule = null;
        initTableFileSelector(fm, modelOrModuleStr, returnFileFlag, inDirStr, null);
    }

    /*-----------------------------------------------------*/

    /**
     * TableFileSelector
     *
     * @param fm               frame
     * @param modelOrModuleStr model or module
     * @param returnFileFlag   return file flag
     * @param inDirStr         in dir stir
     * @param inModule         in module
     */
    public TableFileSelector(Frame fm, String modelOrModuleStr, boolean returnFileFlag, String inDirStr, Module inModule) throws IOException {

        super(fm, "Select File From Table", true);
        initTableFileSelector(fm, modelOrModuleStr, returnFileFlag, inDirStr, inModule);
    }

    /*-----------------------------------------------------*/

    /**
     * initTableFileSelector
     *
     * @param fm               frame
     * @param modelOrModuleStr model or module
     * @param returnFileFlag   return file flag
     * @param inDirStr         in dir stir
     * @param inModule         in module
     */
    public void initTableFileSelector(Frame fm, String modelOrModuleStr, boolean returnFileFlag, String inDirStr, Module inModule) throws IOException {

        parent = fm;
        try {
            libraries = SCSUtility.readLibraryNickAndPathsFile();
        } catch (FileNotFoundException e) {
            System.err.println("Error:TableFileSelector:constructor:2 File not found exception : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
            throw (new FileNotFoundException());
        } catch (IOException e) {
            System.err.println("Error:TableFileSelector:constructor:3 io exception with : " + SCSUtility.scs_library_paths_file);
            //todo: popup warning
            throw (new IOException());
        }
        if (libraries == null) {
            System.err.println("Error:TableFileSelector:constructor:1 please fix the library file : " + SCSUtility.scs_library_paths_file);
            throw (new IOException());
        }


        libraryPaths = SCSUtility.vectorToPathArray(libraries);
        if (libraryPaths == null) {
            System.err.println("Error:TableFileSelector:constructor:4 please fix the library file : " + SCSUtility.scs_library_paths_file);
            throw (new IOException());
        }

        libraryNickNames = SCSUtility.vectorToNickNameArray(libraries);
        if (libraryNickNames == null) {
            System.err.println("Error:TableFileSelector:constructor:5 please fix the library file : " + SCSUtility.scs_library_paths_file);
            throw (new IOException());
        }

        moduledataBlank[0] = blankString;
        versiondataBlank[0] = blankString;


        JPanel listPanel = new JPanel();
        JPanel listButtonPanel = new JPanel();

        //-----------------------

        // Create a JList that displays the strings in data[]


        JPanel listPanelAA = new JPanel();
        listPanelAA.setLayout(new BorderLayout());
        JLabel libLabel = new JLabel("Library");
        listPanelAA.add("North", libLabel);

        libraryList = new JList<>(libraryPaths);
        libraryList.addListSelectionListener(this);
        libraryList.setPreferredSize(new Dimension(300, 900));
        libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        libraryList.setValueIsAdjusting(true);
        //Create the scroll pane and add the table to it.
        libraryScrollPane = new JScrollPane(libraryList);
        listPanelAA.add("South", libraryScrollPane);

        JPanel listPanelBB = new JPanel();
        listPanelBB.setLayout(new BorderLayout());
        JLabel moduleLabel = new JLabel("Module");
        listPanelBB.add("North", moduleLabel);

        moduleList = new JList<>(moduledataBlank);
        moduleList.addListSelectionListener(this);
        // 99/4/23 aa
        // to be able to scroll the whole list after
        // trying everything except converting to vector
        // I found that if I changed the dimension from 400
        // to 900 I got the whole 100 elements in the module list.
        // 400 only scrolled 21 elements
        moduleList.setPreferredSize(new Dimension(200, 900));
        moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Create the scroll pane and add the table to it.
        moduleScrollPane = new JScrollPane(moduleList);
        //getContentPane().add(moduleScrollPane);
        listPanelBB.add("South", moduleScrollPane);

        JPanel listPanelCC = new JPanel();
        listPanelCC.setLayout(new BorderLayout());
        JLabel versionLabel = new JLabel("Version");
        listPanelCC.add("North", versionLabel);

        versionList = new JList<>(versiondataBlank);
        versionList.addListSelectionListener(this);
        versionList.setPreferredSize(new Dimension(100, 400));
        versionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Create the scroll pane and add the table to it.
        versionScrollPane = new JScrollPane(versionList);
        //    getContentPane().add(versionScrollPane);
        listPanelCC.add("South", versionScrollPane);

        //finalfileList = new JList(filedataBlank);
        //finalfileList.addListSelectionListener(this);
        //finalfileList.setPreferredSize(new Dimension(200,1200));
        //finalfileList.setSelectionMode(0);
        //Create the scroll pane and add the table to it.
        //finalfileScrollPane= new JScrollPane(finalfileList);
        //    getContentPane().add(finalfileScrollPane);
        //listPanel.add(finalfileScrollPane);

        listPanel.add(listPanelAA);
        listPanel.add(listPanelBB);
        listPanel.add(listPanelCC);
        getContentPane().add("Center", listPanel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        listButtonPanel.add(okButton);
        cancelButton.addActionListener(this);

        listButtonPanel.add(cancelButton);
        getContentPane().add("South", listButtonPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //now set the JList values
        if ((inDirStr != null) &&
                (!(inDirStr.equals("")))) {
            libraryList.setSelectedValue(inDirStr, true);//scroll
        }
        if ((inModule != null) &&
                (!(inModule.moduleName.equals("")))) {
            moduleList.setSelectedValue(inModule.moduleName, true);//scroll
        }
        if ((inModule != null) &&
                (!(inModule.versionName.equals("")))) {
            versionList.setSelectedValue(inModule.versionName, true);//scroll
        }
    }

    //-------------------------------------------------------------------
    public String getFilePath() {
        return filePath;
    }


    public String getVersionPath() {
        return versionPath;
    }


    public String getModulePath() {

        return modulePath;
    }

    public String getLibraryPath() {

        return libraryPath;
    }

    public String getSourcePath() {

        return srcPath;
    }

    //-------------------------------------------------------
    private String[] returnDirList(String parentdirpath) {

        // --- convert to function
        String[] resultList = new String[maxElementsInDir];
        resultList[0] = blankString;
        File albumdir = new File(parentdirpath);

        if (!albumdir.isDirectory())
            return resultList;
        int i = 0;
        String[] fileList = new String[maxElementsInDir];

        for (i = 0; i < maxElementsInDir; i++)
            fileList[i] = "none";

        fileList = albumdir.list();

        int count = 0;

        //    System.out.println("Dir list is: " + fileList );

        assert fileList != null;
        for (i = 0; i < fileList.length; i++) {
            File tempfile = new File(parentdirpath, fileList[i]);
            // System.out.println(tempfile.getAbsolutePath());
            if (tempfile.isDirectory() && (!albumdir.getName().equals(blankString))) {
                //if (  checktag(tempfile, tag) == true )
                {
                    resultList[count] = fileList[i];
                    count++;
                }
            }
        }
        //System.out.println("TableFileSelector: "+tag+" count: "+count);
        return (resultList);
    }

    //-------------------------------------------------------------
    /*

      private boolean checktag ( File  dir , String tagname ) {

      if ( new File( dir, tagname).isFile() == true )
      return true ;

      return false ;

      }
    */
    //-------------------------------------------------------------
    public String returnLibraryPath() {

        if (libraryList == null) {
            System.err.println("Error:TableFileSelector: library List is null.");
            return (null);
        } else {
            String temp42 = libraryList.getSelectedValue();
            if (temp42 == null) {
                System.err.println("Error:TableFileSelector: library List getSelectedValue is null.");
                return (null);
            } else {
                return (temp42);
            }
        }
    }

    //-------------------------------------------------------------
    public String getLibNickNameFromVector(String libPath) {
        //make sure libPath has been trimmed.
        String libNickName;
        //this is based on the fact that the vectors
        //libraryNickNames and libraryPaths should be the same order
        int hereIAm = -1;
        int sz = libraryPaths.length;
        for (int i = 0; i < sz; i++) {
            if (libraryPaths[i].equals(libPath)) {
                hereIAm = i;
                break;
            }
        }
        //hereIAm=libraryPaths.indexOf(libPath);
        if (hereIAm >= 0) {
            //libNickName=(String)libraryNickNames.elementAt(hereIAm);
            libNickName = libraryNickNames[hereIAm];
            return (libNickName);
        }
        return (null);
    }

    //-------------------------------------------------------------
    public String returnLibraryNickName() {

        if (libraryList == null) {
            System.err.println("Error:TableFileSelector: library List is null.");
            return (null);
        } else {
            String libPath = libraryList.getSelectedValue();
            if (libPath == null) {
                System.err.println("Error:TableFileSelector: library List getSelectedValue is null.");
                return (null);
            } else {
                libPath = libPath.trim();
                return (getLibNickNameFromVector(libPath));
            }
        }
    }

    //-------------------------------------------------------------
    public String returnModulePath() {

        String temp32 = returnLibraryPath();
        if (temp32 == null) {
            return (null);
        } else if (moduleList == null) {
            System.err.println("Error:TableFileSelector: module List is null.");
            return (null);
        } else {
            String temp33 = moduleList.getSelectedValue();
            if (temp33 == null) {
                System.err.println("Error:TableFileSelector: module List getSelectedValue is null.");
                return (null);
            } else {
                return (temp32 + File.separator + temp33);
            }
        }
    }

    //-------------------------------------------------------------
    public String returnVersionPath() {
        String temp82 = returnModulePath();
        if (temp82 == null) {
            return (null);
        } else if (versionList == null) {
            System.err.println("Error:TableFileSelector: version List is null.");
            return (null);
        } else {
            String temp83 = versionList.getSelectedValue();
            if (temp83 == null) {
                System.err.println("Error:TableFileSelector: version List getSelectedValue is null.");
                return (null);
            } else {
                return (temp82 + File.separator + temp83);
            }
        }
    }

    //-------------------------------------------------------------
    public String returnSourcePath() {
        String mytemp3 = returnVersionPath();
        if (mytemp3 == null) {
            return (null);
        } else {
            return (mytemp3 + File.separator + "src");
        }
    }

    //-------------------------------------------------------------
    public String returnFilePath() {
        String mytemp5 = returnSourcePath();
        if (mytemp5 == null) {
            return (null);
        } else {
            String mytemp6 = returnFileNameOnly();
            if (mytemp6 == null) {
                return (null);
            } else {
                return (mytemp5 + File.separator + mytemp6);
            }
        }
    }

    //-------------------------------------------------------------
    public String returnFileNameOnly() {
        if (moduleList == null) {
            System.err.println("Error:TableFileSelector:returnFileNameOnly: moduleList is null");
            return (null);
        } else {
            String mytemp11 = moduleList.getSelectedValue();
            return (mytemp11 + ".sif");
        }
        //return  (String) finalfileList.getSelectedValue() ;
    }

    //-------------------------------------------------------------
    public void valueChanged(ListSelectionEvent event) {
        if (event.getSource() instanceof JList) {
            JList templist = (JList) event.getSource();

            // library selected -- handle module pane
            if (templist.equals(libraryList)) {
                //System.out.println("library  selected-");

                if (libraryList.getSelectedValue() == null) return;
                //System.out.println("TableFileSelector: Path is " +  returnLibraryPath() );
                //System.out.println("TableFileSelector "+ libraryList.getSelectedValue() );
                File albumdir = new File(libraryList.getSelectedValue());

                moduledata = returnDirList(libraryList.getSelectedValue());
                moduleList.setListData(moduledata);
                moduleList.setVisibleRowCount(maxElementsInDir / 3);
                moduleList.repaint();
                moduleList.validate();
                moduleScrollPane.getViewport().setView(moduleList);
                //System.out.println(moduleList.getModel().getElementAt(22));

                versionList.setListData(versiondataBlank);
                versionList.repaint();
                versionList.validate();

                //finalfileList.setListData(filedataBlank );
                //finalfileList.repaint();
                //finalfileList.validate();

            }

            // module selected --  handle version pane

            if (templist.equals(moduleList)) {
                //System.out.println("Module selected--");
                if (moduleList.getSelectedValue() == null) return;
                //System.out.println("TableFileSelector: Path is  " +  returnModulePath() );
                //System.out.println(	"TableFileSelector "+moduleList.getSelectedValue() );
                versiondata = returnDirList(new File(libraryList.getSelectedValue(), moduleList.getSelectedValue()).getAbsolutePath());
                versionList.setListData(versiondata);

                versionList.setVisibleRowCount(maxElementsInDir / 3);
                versionList.validate();
                versionList.repaint();
                versionScrollPane.getViewport().setView(versionList);

            }
            // version selected -- handle file pane

            if (templist.equals(versionList)) {
                //System.out.println("version selected---");
                versionList.getSelectedValue();
                //System.out.println("Path is   " +  returnVersionPath() );

                //FileTagFilter  myfilter = new FileTagFilter () ;

                //filedata = new File((String) libraryList.getSelectedValue(), moduleList.getSelectedValue() + File.separator +  (String) versionList.getSelectedValue()  ).list(  myfilter  )  ;

                //finalfileList.setListData( filedata ) ;
                //  finalfileList.validate();
                //finalfileList.repaint() ;
                //validate();
            } //if ( templist.equals(versionList) == true ){
	   
		/*
		  if ( templist.equals(finalfileList) == true ){
		  //System.out.println("file  selected");
	     
		  if (   finalfileList.getSelectedValue()  == null ) return ;
		  //System.out.println("Path is " +  returnFilePath() );
		  }
		*/
            //System.out.println("TableFileList:templist:"+templist.toString() );

        }
    }


    //----------------------------------------------------
    /* *
     * Perform menu functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {

        MenuItem mi;
        String actionLabel = "none";
        JButton jmi;


        if (event.getSource() instanceof MenuItem) {
            mi = (MenuItem) event.getSource();
            actionLabel = mi.getLabel();
        }


        if (event.getSource() instanceof JButton) {
            //System.out.println("Debug:TableFileSelector:got here");
            jmi = (JButton) event.getSource();
            actionLabel = jmi.getText();  //new
            //System.out.println("Debug:TableFileSelector:got here too");
        }


        if (actionLabel.equals("OK")) {
            libraryName = libraryList.getSelectedValue();
            libraryPath = returnLibraryPath();
            SCSUtility.setCurrLibPath(libraryPath);
            moduleName = moduleList.getSelectedValue();
            modulePath = returnModulePath();
            versionName = versionList.getSelectedValue();
            versionPath = returnVersionPath();
            srcPath = returnSourcePath();
            filePath = returnFilePath();
            fileName = returnFileNameOnly();
            pushed = "Ok";
            setVisible(false);
            //dispose(); aa
            //System.out.println("Debug:TableFileSelector:got here 3");
            return;
        }


        if (actionLabel.equals("Cancel")) {
            pushed = "Cancel";
            setVisible(false);
            //dispose(); aa
        }
    }

}  //end class TableFileSelector


//-------------------------------------------------------------
/*  
    class FileTagFilter implements FilenameFilter 
    {
    public boolean accept(File dir,			    String name)
    {
	  
    if  ( name.indexOf("tag") == -1 )
    return true ;
    else
    return false ;

    }
    }  //end class FileTagFilter
*/






