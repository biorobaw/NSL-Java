/* SCCS %W% -- %G% -- %U% */ // -- 01/29/01
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * NslmEditorFrame - A class representing the main GUI for Nslm Text Editor.
 * It's composed of several parts:
 * - a menu bar at the top
 * - a declaration section for editing variables of leaf module's Nslm
 * codes
 * <p>
 * - an Other AllMethods section for editing other methods of the module
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @param parentFrame    the frame from which this was launched
 * @param currModule    current active module to be manipulated
 * <p>
 * <p>
 * *var       moduleNameTF	the ModuleName text field
 * *var       scopeMenu	the popup menu for Scope button
 * *var       typeMenu	the popup menu for Type button
 * *var       typeChoice	the popup menu for choices of different types
 * *var       declarationList	 	the list where to display a list of variables that come after the ports
 * *var       allMethods		the text area for other allMethods
 * *var       currNSLM		pointing to the current Nslm structure
 * *var       myStatusPanel   the panel for printing status information
 * @since JDK8
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//------------------------------------------------------------------------
//  NSLM Editor
//------------------------------------------------------------------------
// |File| |Help|
//------------------------------------------------------------------------
//  NslmEditorPanel
//------------------------------------------------------------------------
//  Comments1: comments1ScrollableTextArea
//  --------------------------------- 
//  |Scrollable Text Area           |
//  |                               |
//  |                               |
//  ---------------------------------
//  Library Nick Name: libNickNameJTF
//  Library Path: libPathJTF
//  Class Name:    nslClassNameJTF
//  Class Version: classVersionJTF
//  Class Type:    classTypeChoice
//  Class Arguments:    classArgsJTF
//  Verbatim NSLC[on][off]   
//  Verbatim NSLJ[on][off]   
//  Doubled Buffered Ports [on][off]
//  Specific Versions of SubModules [on][off]
//  Comments2: comments2ScrollableTextArea
//  --------------------------------- 
//  |Scrollable Text Area           |
//  |                               |
//  |                               |
//  ---------------------------------
//  *************************************************************************
//  Variables                             
//  -----------------------------------  
//  [Add Constant][Add InputPort][Add OutputPort][Add SubModule][Add Var]
//  |Change||ChangeName||Copy||Delete||Up||Down||Top||Bottom|  
//  --------------------------------------------------------  
//  |Scrollable Lst                                        |  
//  |Scrollable Lst                                        |  
//  |Scrollable Lst                                        |  
//  |Scrollable Lst                                        |  
//  |Scrollable Lst                                        |  
//  --------------------------------------------------------  
//  *************************************************************************
//  Methods: <use notepad editor from 1.3 sdk>//
//  ------------------------------------------------------------------------
//  |Edit||Search||InsertMethod|
//  ------------------------------------------------------------------------
//  --------------------------------- ------------ ------------ 
//  |Scrollable Text Area - much bigger than this             |
//  | initialized with initSys and makeConn info              |
//  |                                                         |
//  |                                                         |
//  -----------------------------------------------------------
//  StatusBar for seeing how much of a file has been read
//  *************************************************************************
//------------------------------------------------------------------------
// Warning/Error:
//------------------------------------------------------------------------
// Status:
//------------------------------------------------------------------------


@SuppressWarnings("Duplicates")
class NslmEditorFrame extends EditorFrame
        implements ListSelectionListener, ActionListener {
    //graphics
    // Module currModule; inherited from EditorFrame
    SchEditorFrame parentFrame;
    NSLM currNslm;

    //graphic variables
    // layout
    //                                     title
    JMenuBar nslmMenuBar;            // nslmMenuBar - must be AWT type
    JPanel myNslmEditorPanel;   //
    JScrollPane myNslmPane;       // myNslmPane contains myNslmEditorPanel
    //StatusPanel myStatusPanel=null;     // defined in EditorFrame

    final static int commentRows = 5;
    final static int commentWidth = 40;
    final static int declScrollListHeight = 6;
    final static int methodsScrollListHeight = 40;

//todo:should query the environment to see how big the users screen is

    final static int NSLM_PANEL_MAX_XWIDTH = 800;
    final static int NSLM_PANEL_MAX_YHEIGHT = 600;

    final static int DECL_PANEL_MAX_XWIDTH = 800;
    final static int DECl_PANEL_MAX_YHEIGHT = 600;

    final static int METHODS_PANEL_MAX_XWIDTH = 800;
    final static int METHODS_PANEL_MAX_YHEIGHT = 600;

    GridBagLayout gblayout;
    GridBagConstraints gbcon;

    private boolean initAdded = false;

    JTextArea commentBlock1TextArea;
    JLabel libNickNameJLabel;
    JLabel libPathNameJLabel;
    String libPath;
    JLabel classNameJLabel;
    JLabel classVersionJLabel;
    JTextField classTypeJTF;
    JTextField classArgsJTF;
    JTextField extendsWhatJTF;
    JTextField whatsParamsJTF;

    ButtonGroup verbatimNSLCButtons = null;
    ButtonGroup verbatimNSLJButtons = null;
    ButtonGroup bufferingButtons = null;
    ButtonGroup getCurrentVersionButtons = null;
    JTextArea commentBlock2TextArea;


    JScrollPane declPane; //these 3 for the declaration list
    JList<String> declJList;
    DefaultListModel<String> declJListModel;
    //    String declJListSelected= "";
    //    int declJListIndex= -1;
    JPanel declPanel;
    Notepad2 methodsPanel = null;


    // these are public now since they just do a setVisible(false) now
    // instead of a dispose
    DeclarationDialog addVarDialog = null;
    DeclarationDialog addInputPortDialog = null;
    DeclarationDialog addOutputPortDialog = null;
    DeclarationDialog addSubModuleDialog = null;
    //Declaration var=null;

    //String currDirStr="";
    //WarningDialog warningPopup; should be in inherited EditorFrame
    //WarningDialogOkCancel okCancelPopup=null;  from EditorFrame

    //for export
    File lastExportFile = null;
    String lastExportFileStr = "";
    MessageDialog messagePopup;

    TextViewer myTextViewer = null;

    //----------------------------------------------------------

    /**
     * Constructor of this class, with the parent set to fm, and current module
     * set to curr.
     */
    public NslmEditorFrame(SchEditorFrame fm, Module module) {

        super("Nslm"); // initialize the EditorFrame
        setLocation(new Point(100, 100));
        //setSize(800,1000); //not working
        //todo: query the screen size

        parentFrame = fm;
        okCancelPopup = new WarningDialogOkCancel(this);
        setTitle("NSLM Editor");
        if (module != null) {
            boolean found = executiveFrame.windowsAndModulesOpen.contains("Nslm", module);
            if (!found) {
                currModule = module;
            } else {//found
                warningPopup.display("IconEditorFrame: that module already open in an Icon Editor");
                currModule = null;
            }
        } else {
            currModule = null;
        }

        if (module != null) {
            setTitle("NSLM Editor: Module " + currModule.moduleName);
        } else {
            setTitle("NSLM Editor");
        }

        //The setting of the background was too anoying for now - on off button /// panel did not get the get the background color.
        // Need seperate background color for NSLM Editor window -not black
        //	NslmEditorPanel.drawBack_col = SCSUtility.returnCol(UserPref.drawBackgroundColor );
        //	NslmEditorPanel.noActionTakenBack_col = SCSUtility.returnCol(UserPref.noActionTakenBackgroundColor );
        //	NslmEditorPanel.currBackgroundCol=NslmEditorPanel.noActionTakenBack_col;


        // Graphics -----------------------------
        BorderLayout borderlayout1 = new BorderLayout();   //layout of frame
        getContentPane().setLayout(borderlayout1);
        // nslmMenuBar -----------------------------
        //     File and Help
        //     add to Center and will appear by default in a vertical manner
        nslmMenuBar = createNslmMenuBar();
        setJMenuBar(nslmMenuBar);
        //getContentPane().setMenuBar(nslmMenuBar);
        //add(nslmMenuBar,BorderLayout.NORTH); //add to this frame
        // myNslmEditorPanel  -----------------------------
        myNslmEditorPanel = new JPanel();
        createNslmPanel(myNslmEditorPanel);

        myNslmPane = new JScrollPane(myNslmEditorPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().add(myNslmPane, BorderLayout.CENTER); //add to this frame
        myNslmEditorPanel.setVisible(false);
        //myStatusPanel  -----------------------------
        myStatusPanel = new StatusPanel();
        getContentPane().add(myStatusPanel, BorderLayout.SOUTH); //add to this frame

        //        myNslmEditorPanel.setBackground(NslmEditorPanel.currBackgroundCol);

        addWindowListener(new DWAdapter()); //from EditorFrame
        messagePopup = new MessageDialog(this);

        //-------
        // Now see if there was already a module open from Sch or Icon
        if (currModule == null) {
            return;
        }
        if (currModule.myNslm == null) {
            //System.out.println("Debug:NslmEditorFrame: constructor and non null currModule");
            boolean worked = executiveFrame.windowsAndModulesOpen.addInPieces("Nslm", currModule);
            finishNew();
            setTitle("Nslm Editor: " + currModule.moduleName);
            return;
        }
        //a NSLM view exists by that name
        //System.out.println("Debug:NslmEditorFrame: constructor and non null currIcon");
        boolean worked = executiveFrame.windowsAndModulesOpen.addInPieces("Nslm", currModule);
        finishOpen();
        setTitle("Nslm Editor: " + currModule.moduleName);


    } // end constructor

    //----------------------------------------------------------------

    /**
     * Create the NSLM menu bar.
     */
    public JMenuBar createNslmMenuBar() {
        JMenuItem mi;
        JMenu fileMenu;
        JMenu helpMenu;
        JMenuBar myMenuBar;

        //System.out.println("Debug:NslmEditorFrame:createNslmMenuBar 1");
        myMenuBar = new JMenuBar();
        //myMenuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

        fileMenu = new JMenu("File");

        fileMenu.add(mi = new JMenuItem("New"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("Open"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("Save"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("Save as"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("Close"));
        mi.addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(mi = new JMenuItem("Export NSLM"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("View NSLM"));
        mi.addActionListener(this);
        fileMenu.add(mi = new JMenuItem("Import NSLM"));
        mi.addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(mi = new JMenuItem("Print"));
        mi.addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(mi = new JMenuItem("Close NSLM Editor"));
        mi.addActionListener(this);

        myMenuBar.add(fileMenu);


        helpMenu = new JMenu("Help");
        helpMenu.addActionListener(this);
        helpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);

        myMenuBar.add(helpMenu);
        //myMenuBar.setHelpMenu(helpMenu); 1.3 does not have yet
        //System.out.println("Debug:NslmEditorFrame:createNslmMenuBar 2");

        return (myMenuBar);
    }

    //--------------------------------------------------------------------
    public JPanel createNslmPanel(JPanel myNslmEditorPanel) {

        boolean error = false;
        GridBagLayout gblayout;
        GridBagConstraints gbcon;

        gblayout = new GridBagLayout();  //setLayout ??
        myNslmEditorPanel.setLayout(gblayout);
        gbcon = new GridBagConstraints(); //64,8,1,1

        // the following are non-editable fields
        //rows must be the same for both column1 and column2
        JLabel libraryNickNameJLabel1 = new JLabel("Library Nick Name: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(libraryNickNameJLabel1, gbcon);
        myNslmEditorPanel.add(libraryNickNameJLabel1, gbcon);
        if (currModule == null) {
            libNickNameJLabel = new JLabel(SCSUtility.blankString20);
        } else {
            libNickNameJLabel = new JLabel(currModule.libNickName);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(libNickNameJLabel, gbcon);
        myNslmEditorPanel.add(libNickNameJLabel, gbcon);

        // the following are non-editable fields
        JLabel libraryPathNameJLabel1 = new JLabel("Library Path Name: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(libraryPathNameJLabel1, gbcon);
        myNslmEditorPanel.add(libraryPathNameJLabel1, gbcon);
        if (currModule == null) {
            libPath = SCSUtility.blankString20;
        } else {
            try {
                libPath = SCSUtility.getLibPathName(currModule.libNickName);
            } catch (FileNotFoundException fnfe) {
                String errStr = "FileNotFoundException: " + currModule.libNickName;
                JOptionPane.showMessageDialog
                        (null, errStr, "Error", JOptionPane.WARNING_MESSAGE);
                //todo: should keep throwing the error
                error = true;
                return (null);
            } catch (IOException ioe) {
                String errStr = "FileNotFoundException: " + currModule.libNickName;
                JOptionPane.showMessageDialog
                        (null, errStr, "Error", JOptionPane.WARNING_MESSAGE);
                //todo: should keep throwing the error
                error = true;
                return (null);
            }
        }

        if (currModule == null) {
            libPathNameJLabel = new JLabel(SCSUtility.blankString20);
        } else {
            libPathNameJLabel = new JLabel(libPath);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(libPathNameJLabel, gbcon);
        myNslmEditorPanel.add(libPathNameJLabel, gbcon);

        JLabel classNameJLabel1 = new JLabel("Class Name: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(classNameJLabel1, gbcon);
        myNslmEditorPanel.add(classNameJLabel1, gbcon);
        if (currModule == null) {
            classNameJLabel = new JLabel(SCSUtility.blankString20);
        } else {
            classNameJLabel = new JLabel(currModule.moduleName);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(classNameJLabel, gbcon);
        myNslmEditorPanel.add(classNameJLabel, gbcon);

        JLabel classVersionJLabel1 = new JLabel("Class Version: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(classVersionJLabel1, gbcon);
        myNslmEditorPanel.add(classVersionJLabel1, gbcon);
        if (currModule == null) {
            classVersionJLabel = new JLabel(SCSUtility.blankString20);
        } else {
            classVersionJLabel = new JLabel(currModule.versionName);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(classVersionJLabel, gbcon);
        myNslmEditorPanel.add(classVersionJLabel, gbcon);

        //todo: This is suppose to be a selector
        JLabel classTypeJLabel1 = new JLabel("Class Type: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(classTypeJLabel1, gbcon);
        myNslmEditorPanel.add(classTypeJLabel1, gbcon);
        if (currModule == null) {
            classTypeJTF = new JTextField("NslModule");
        } else {
            classTypeJTF = new JTextField(currModule.moduleType);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(classTypeJTF, gbcon);
        myNslmEditorPanel.add(classTypeJTF, gbcon);

        JLabel classArgsJLabel1 = new JLabel("Class Arguments: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(classArgsJLabel1, gbcon);
        myNslmEditorPanel.add(classArgsJLabel1, gbcon);
        if (currModule == null) {
            classArgsJTF = new JTextField(SCSUtility.blankString20);
        } else {
            classArgsJTF = new JTextField(currModule.arguments);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(classArgsJTF, gbcon);
        myNslmEditorPanel.add(classArgsJTF, gbcon);


        JLabel extendsWhatJLabel1 = new JLabel("Extends What: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(extendsWhatJLabel1, gbcon);
        myNslmEditorPanel.add(extendsWhatJLabel1, gbcon);
        if ((currModule == null) || (currModule.myNslm == null)) {
            extendsWhatJTF = new JTextField(SCSUtility.blankString20);
        } else {
            extendsWhatJTF = new JTextField(currModule.myNslm.extendsWhat);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(extendsWhatJTF, gbcon);
        myNslmEditorPanel.add(extendsWhatJTF, gbcon);

        JLabel whatsParamsJLabel1 = new JLabel("What's Params: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(whatsParamsJLabel1, gbcon);
        myNslmEditorPanel.add(whatsParamsJLabel1, gbcon);
        if ((currModule == null) || (currModule.myNslm == null)) {
            whatsParamsJTF = new JTextField(SCSUtility.blankString20);
        } else {
            whatsParamsJTF = new JTextField(currModule.myNslm.whatsParams);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(whatsParamsJTF, gbcon);
        myNslmEditorPanel.add(whatsParamsJTF, gbcon);


        //first comment
        //rows must be the same for both column1 and column2
        JLabel commentBlock1Label = new JLabel("CommentBlock1: ");
        SCSUtility.setFirstColumn(gbcon, commentRows);
        gblayout.setConstraints(commentBlock1Label, gbcon);
        myNslmEditorPanel.add(commentBlock1Label, gbcon);
        commentBlock1TextArea = new JTextArea(commentRows, commentWidth);
        //commentBlock1TextArea.setPreferredSize(new Dimension(,));
        JScrollPane comment1ScrollPane = new JScrollPane(commentBlock1TextArea);
        //SCSUtility.setSecondColumn(gbcon,commentRows,commentColumns);
        SCSUtility.setSecondColumn(gbcon, commentRows);
        gblayout.setConstraints(comment1ScrollPane, gbcon);
        myNslmEditorPanel.add(comment1ScrollPane, gbcon);


        //verbatimNSLC on or off
        if ((currModule == null) || (currModule.myNslm == null)) {
            verbatimNSLCButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Verbatim NSLC:", false, this);
        } else {
            verbatimNSLCButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Verbatim NSLC:", currModule.myNslm.verbatimNSLC, this);
        }
        if ((currModule == null) || (currModule.myNslm == null)) {
            verbatimNSLJButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Verbatim NSLJ:", false, this);
        } else {
            verbatimNSLJButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Verbatim NSLJ:", currModule.myNslm.verbatimNSLJ, this);
        }
        if (currModule == null) {
            bufferingButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Double Buffered Ports:", false, this);
        } else {
            bufferingButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Double Buffered Ports:", currModule.buffering, this);
        }

        if (currModule == null) {
            getCurrentVersionButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Use Newest Submodules:", true, this);
        } else {
            getCurrentVersionButtons = SCSUtility.addOnOffButtonPanel(myNslmEditorPanel, gblayout, gbcon, "Use Newest Submodules:", currModule.getCurrentVersion, this);
        }

        //second comment

        JLabel commentBlock2JLabel = new JLabel("CommentBlock2: ");
        SCSUtility.setFirstColumn(gbcon, commentRows);
        gblayout.setConstraints(commentBlock2JLabel, gbcon);
        myNslmEditorPanel.add(commentBlock2JLabel, gbcon);
        commentBlock2TextArea = new JTextArea(commentRows, commentWidth);
        if ((currModule == null) || (currModule.myNslm == null)) {
            commentBlock2TextArea.setText(SCSUtility.blankString20);
        } else {
            commentBlock2TextArea.setText(currModule.myNslm.comment2);
        }
        //commentBlock2TextArea.setPreferredSize(new Dimension(,));
        JScrollPane comment2ScrollPane = new JScrollPane(commentBlock2TextArea);
        //SCSUtility.setSecondColumn(gbcon,commentRows,commentColumns);
        SCSUtility.setSecondColumn(gbcon, commentRows);
        gblayout.setConstraints(comment2ScrollPane, gbcon);
        myNslmEditorPanel.add(comment2ScrollPane, gbcon);


        //declaration panel
        declPanel = createDeclarationPanel();
        gbcon.fill = GridBagConstraints.BOTH;
        gbcon.anchor = GridBagConstraints.CENTER;
        gbcon.weightx = 100;
        gbcon.weighty = 100;
        gbcon.gridx = 0; //setFirstColumn
        gbcon.gridy = GridBagConstraints.RELATIVE;
        gbcon.gridwidth = GridBagConstraints.REMAINDER;
        gbcon.gridheight = (3 + declScrollListHeight);
        gblayout.setConstraints(declPanel, gbcon);
        myNslmEditorPanel.add(declPanel, gbcon);

        //method panel
        methodsPanel = createMethodsPanel();
        if (methodsPanel == null) {
            warningPopup.display("NslmEditorFrame:createMethodsPanel did not work");
        } else {
            gbcon.fill = GridBagConstraints.BOTH;
            gbcon.anchor = GridBagConstraints.CENTER;
            gbcon.weightx = 100;
            gbcon.weighty = 100;
            gbcon.gridx = 0; //first column
            gbcon.gridy = GridBagConstraints.RELATIVE;
            gbcon.gridwidth = GridBagConstraints.REMAINDER;
            gbcon.gridheight = (3 + methodsScrollListHeight);
            gblayout.setConstraints(methodsPanel, gbcon);
            myNslmEditorPanel.add(methodsPanel, gbcon);
        }

        //myNslmEditorPanel.setPreferredSize(new Dimension(NSLM_PANEL_MAX_XWIDTH,NSLM_PANEL_MAX_YHEIGHT));
        return (myNslmEditorPanel);
    }

    //------------------------------------------------------------------------
    public void fillDeclarationModel(DefaultListModel<String> declJListModel) {
        //This is getting called when the NSLM Editor is first open in create
        // and again in init of data.
        //First time thru declJListModel is 0, second time it is not.
        if ((currModule != null) && (currModule.variables != null)) {
            int sizeOfDeclList = declJListModel.size();
            if (sizeOfDeclList > 0) { //clear the old list
                //System.out.println("Debug:NslmEditorFrame: fillDeclarationModel size1:"+sizeOfDeclList);
                declJListModel.clear();
            }
            int sizeOfVarList = currModule.variables.size();
            //System.out.println("Debug:NslmEditorFrame: fillDeclarationModel size2:"+sizeOfVarList);
            for (int i = 0; i < sizeOfVarList; i++) {
                Declaration var = (Declaration) currModule.variables.elementAt(i);
                declJListModel.addElement(var.varName);
            }
        }//end if ((currModule != null) && (currModule.myNslm != null))
    }

    //------------------------------------------------------------------------
    public JPanel createDeclarationPanel() {

        Declaration var = null;

        Border etched = BorderFactory.createEtchedBorder();
        Border title = BorderFactory.createTitledBorder(etched, "Declarations");

        JPanel declPanel = new JPanel();
        BoxLayout boxlay = new BoxLayout(declPanel, BoxLayout.Y_AXIS); //i think this does setLayout
        declPanel.setLayout(boxlay);
        declPanel.setBorder(title);

        JPanel declButtonBar = createDeclarationButtonBar(); //flow layout
        declPanel.add(declButtonBar);

        declJListModel = new DefaultListModel<String>();

        //fillDeclarationModel 2
        fillDeclarationModel(declJListModel);

        declJList = new JList<>(declJListModel);
        declJList.setVisibleRowCount(declScrollListHeight);
        declJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        declJList.addListSelectionListener(this);

        //declPanel.setPreferredSize(new Dimension(,));
        JScrollPane declPane = new JScrollPane(declJList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //JScrollPane declPane=new JScrollPane(declJList);


        declPanel.add(declPane);
        return (declPanel);
    }

    //--------------------------------------------------------------------

    /**
     * Create the declaration menu bar.
     * Two rows of buttons
     */
    public JPanel createDeclarationButtonBar() {
        JPanel buttonBar = new JPanel();
        BoxLayout boxlay = new BoxLayout(buttonBar, BoxLayout.Y_AXIS); //i think this does setLayout
        buttonBar.setLayout(boxlay);
        buttonBar.setBorder(new BevelBorder(BevelBorder.RAISED));

        //alignment = left
        FlowLayout flowlay1 = new FlowLayout(FlowLayout.LEFT); //left justified
        FlowLayout flowlay2 = new FlowLayout(FlowLayout.LEFT); //left justified
        JPanel buttonTopBar = new JPanel(flowlay1); //left justified
        JPanel buttonLowerBar = new JPanel(flowlay2);

        //JButton addVarTopButton=new JButton("Add Top Var");
        JButton addInputPortButton = new JButton("Add InputPort");
        JButton addOutputPortButton = new JButton("Add OutputPort");
        JButton addSubModuleButton = new JButton("Add SubModule");
        JButton addVarBottomButton = new JButton("Add Other Variable");

        //buttonTopBar.add(addVarTopButton);
        buttonTopBar.add(addInputPortButton);
        buttonTopBar.add(addOutputPortButton);
        buttonTopBar.add(addSubModuleButton);
        buttonTopBar.add(addVarBottomButton);

        //addVarTopButton.addActionListener(this);
        addInputPortButton.addActionListener(this);
        addOutputPortButton.addActionListener(this);
        addSubModuleButton.addActionListener(this);
        addVarBottomButton.addActionListener(this);

        //lower bar

        JButton changeButton = new JButton("Change");
        changeButton.addActionListener(this);
        buttonLowerBar.add(changeButton);

        JButton changeNameButton = new JButton("Change Name");
        changeNameButton.addActionListener(this);
        buttonLowerBar.add(changeNameButton);

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(this);
        buttonLowerBar.add(copyButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        buttonLowerBar.add(deleteButton);

        JButton upButton = new JButton("Up");
        upButton.addActionListener(this);
        buttonLowerBar.add(upButton);

        JButton downButton = new JButton("Down");
        downButton.addActionListener(this);
        buttonLowerBar.add(downButton);

        JButton topButton = new JButton("Top");
        topButton.addActionListener(this);
        buttonLowerBar.add(topButton);

        JButton bottomButton = new JButton("Bottom");
        bottomButton.addActionListener(this);
        buttonLowerBar.add(bottomButton);

        buttonBar.add(buttonTopBar);
        buttonBar.add(buttonLowerBar);
        return (buttonBar);
    }

    //-----------------------------------------------------------------
    public Notepad2 createMethodsPanel() {
        //System.out.println("Debug:NslmEditorFrame: in createMethodsPanel");
        methodsPanel = new Notepad2(this);//pass in the frame
        // System.out.println("Debug:NslmEditorFrame: in createMethodsPanel - worked");
        return (methodsPanel);

    }

    //--------------------------------------------------------------------
    // @return false
    public boolean setNslmInfo(Module inModule) {
        if (inModule == null) {
            String errstr = "NslmEditorFrame:setNslmInfo inModule null";
            warningPopup.display(errstr);
            return (false);
        }
        if (inModule.myNslm == null) {
            String errstr = "NslmEditorFrame:setNslmInfo inModule.myNslm null";
            warningPopup.display(errstr);
            return (false);
        }

        commentBlock1TextArea.setText(inModule.myNslm.comment1);

        // the following are non-editable fields
        libNickNameJLabel.setText(inModule.libNickName);

        String pathName;
        try {
            pathName = SCSUtility.getLibPathName(inModule.libNickName);
        } catch (IOException e) {
            return (false);
        }

        libPathNameJLabel.setText(pathName);

        classNameJLabel.setText(inModule.moduleName);

        //let user change this
        classVersionJLabel.setText(inModule.versionName);

        //let user change this
        classTypeJTF.setText(inModule.moduleType);

        //let user change this
        classArgsJTF.setText(inModule.arguments);

        extendsWhatJTF.setText(inModule.myNslm.extendsWhat);

        whatsParamsJTF.setText(inModule.myNslm.whatsParams);


        //on or off

        boolean worked1 = SCSUtility.setOnOffButtonGroup(verbatimNSLCButtons, inModule.myNslm.verbatimNSLC);
        boolean worked2 = SCSUtility.setOnOffButtonGroup(verbatimNSLJButtons, inModule.myNslm.verbatimNSLC);
        boolean worked3 = SCSUtility.setOnOffButtonGroup(bufferingButtons, inModule.buffering);
        boolean worked4 = SCSUtility.setOnOffButtonGroup(getCurrentVersionButtons, inModule.getCurrentVersion);

        commentBlock2TextArea.setText(inModule.myNslm.comment2);

        //declaration panel 1
        fillDeclarationModel(declJListModel);

        //method panel -- methods is a Document type
        if (inModule.myNslm.methods != null) {
            inModule.myNslm.methods.removeUndoableEditListener(methodsPanel.undoHandler);
            methodsPanel.setPlainDocument(inModule.myNslm.methods);
            //methodsPanel.getEditor().getDocument().addUndoableEditListener(methodsPanel.undoHandler);
            //doc().insertString(doc.getLength(),new String());
        }
        return (true);
    } //end setNslmInfo

    /**
     * -------------------------------------------------------------
     */
    public boolean display(Module inModule) {
        boolean worked;
        worked = setNslmInfo(inModule);
        if (!worked) {
            return (false); //cancel
        } else {
            //show();
            myNslmEditorPanel.setVisible(true);
            //worked=getNslmInfo(inModule); has to be done in save or saveas
        }
        return (true);
    }

    //--------------------------------------------------
    public DeclarationDialog getDialogWindow(String dialogType) {
        DeclarationDialog myDialog;
	/*
	if (dialogType.equals("VarBefore")) {  //REMOVED
	    if (addVarDialog==null) {
		addVarDialog=new DeclarationDialog(this,dialogType);
		myDialog=addVarDialog;
	    } else {
		myDialog=addVarDialog;
	    }
	}
	*/
        switch (dialogType) {
            case "InputPort":
                if (addInputPortDialog == null) {
                    addInputPortDialog = new DeclarationDialog(this, dialogType);
                    myDialog = addInputPortDialog;
                } else {
                    myDialog = addInputPortDialog;
                }
                break;
            case "OutputPort":
                if (addOutputPortDialog == null) {
                    addOutputPortDialog = new DeclarationDialog(this, dialogType);
                    myDialog = addOutputPortDialog;
                } else {
                    myDialog = addOutputPortDialog;
                }
                break;
            case "BasicVariable":
                if (addVarDialog == null) {
                    addVarDialog = new DeclarationDialog(this, dialogType);
                    myDialog = addVarDialog;
                } else {
                    myDialog = addVarDialog;
                }
                break;
            case "SubModule":
                if (addSubModuleDialog == null) {
                    addSubModuleDialog = new DeclarationDialog(this, dialogType);
                    myDialog = addSubModuleDialog;
                } else {
                    myDialog = addSubModuleDialog;
                }
                break;
            default:
                String errMsg = "NslmEditorFrame:getDialogWindow: unknown dialogType :" + dialogType;
                warningPopup.display(errMsg);
                myDialog = null;
                break;
        }
        return (myDialog);
    }

    /**
     * Perform functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        //JRadioButton bn;
        JButton bn;
        JMenuItem jmi;
        String selectedStr;
        String selectedVarStr;
        int jlistIndex = 0;
        int insertAt;

        //System.out.println("NslmEditorFrame:actionPerformed top");
        //System.out.println("NslmEditorFrame:actionPerformed event.getSource: "+event.getSource().toString());
        //###########-------------------------------
        // MenuItems from File Menu
        //###########------------------------------

        if (event.getSource() instanceof JMenuItem) {
            //System.out.println("NslmEditorFrame:actionPerformed JMenuItem");

            jmi = (JMenuItem) event.getSource();
            selectedStr = jmi.getText(); //Swing
            //selectedStr=jmi.getLabel(); //AWT
            //System.out.println("NslmEditorFrame:actionPerformed: "+selectedStr);
            //###########----------------
            // file actions
            //###########--------------------------
            if (selectedStr.equals("New")) {
                Module tempModule = newModule("Module");
                //System.out.println("NslmEditorFrame:actionPerformed: "+selectedStr);
                if (tempModule == null) {
                    //means current module is open and not exited
                    //warningPopup.display("NslmEditorFrame:actionPerformed new module is null");
                    return;
                }
                currModule = tempModule;
                //System.out.println("Debug:NslmEditorFrame:actionPerformed here1");
                finishNew();
                return;
            }

            if (selectedStr.equals("Open")) {
                //System.out.println("NslmEditorFrame:actionPerformed Open");
                Module tempModule = openModule();
                if (tempModule == null) {
                    return;
                }
                currModule = tempModule;
                finishOpen();
                return;
            }

            if (selectedStr.equals("Save")) {
                //System.out.println("NslmEditorFrame:actionPerformed Save");
                //System.out.println("Debug:IconEditorFrame:Curr Dir is" + currModule.moduleName );
                getNslmInfo(); //make sure currModule is current
                if (!(saveModule())) {//error messages given from saveModule in EditorFrame
                    return;
                }
                return;
            }
            //##########
            if (selectedStr.equals("Save as")) {
                getNslmInfo();  //make sure currModule is current
                //System.out.println("NslmEditorFrame:actionPerformed Saveas");
                if (!(saveAsModule())) {//error messages given from saveAsModule
                    return;
                }
                return;
            } //end Save as
            //###########-------

            if (selectedStr.equals("Print")) {
                //currently, only working with Postscript printers
                //System.out.println("NslmEditorFrame:actionPerformed Print");
                PrintJob pjob = getToolkit().getPrintJob(this, "Printing NSLM", null);
                if (pjob != null) {
                    Graphics pg = pjob.getGraphics();
                    if (pg != null) {
                        myNslmEditorPanel.print(pg);
                        pg.dispose();
                    }
                    pjob.end();
                }
                return;
            }
            //###########-------
            if (selectedStr.equals(("Export NSLM"))) {
                exportNslm();
                return;
            }
            // --------------------------------------------------

            if (selectedStr.equals("View NSLM")) {//note:we also have NSLM Viewer
                String lastViewedFileStr = "";
                if ((currModule != null) && (currModule.moduleName != null) && (!(currModule.moduleName.equals("")))) {
                    lastViewedFileStr = currModule.moduleName + ".mod";
                }
                if (myTextViewer == null) {
                    myTextViewer = new TextViewer(this);
                }
                //todo:this should be firstlib not user_home
                if (currDirStr == null) currDirStr = SCSUtility.getCurrLibPath();
                if (currDirStr.equals("")) currDirStr = SCSUtility.getCurrLibPath();
                if (currDirStr.equals("")) currDirStr = SCSUtility.user_home;
                myTextViewer.display(currDirStr, lastViewedFileStr);

                return;
            }
            // --------------------------------------------------
            if (selectedStr.equals(("Import NSLM"))) {
                warningPopup.display("NslmEditorFrame: Not implemented yet.");
                return;
            }

            //###########-------
            if (selectedStr.equals("Close")) {
                //System.out.println("NslmEditorFrame:actionPerformed Close");
                if (!(closeModule())) {
                    return;
                }
                currNslm = null;
                currModule = null;
                declJListModel.clear();

                //myNslmEditorPanel.currBackgroundCol=NslmEditorPanel.noActionTakenBack_col;
                //myNslmEditorPanel.setBackground(NslmEditorPanel.currBackgroundCol);
                myNslmEditorPanel.setVisible(false);
                return;
            }
            //###########-------

            if (selectedStr.equals("Close NSLM Editor")) {
                //System.out.println("NslmEditorFrame:actionPerformed Close Editor");

                if (!(closeModule())) {
                    return;
                }
                declJListModel.clear();
                currNslm = null;
                currModule = null;
                dispose();
                parentFrame.mySchematicPanel.requestFocus();
                return;
            }
        }

        // ---------------------------------------------------------------
        // Button events
        // ---------------------------------------------------------------
        if (event.getSource() instanceof JRadioButton) {
            //	    System.out.println("NslmEditorFrame:actionPerformed JRadioButton");
            JRadioButton rbn = (JRadioButton) event.getSource(); //actually a radio button
            String bntext = rbn.getText();
            //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);

            if ((bntext.equals("On")) ||
                    (bntext.equals("Off"))) {
                //		System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                currModule.myNslm.verbatimNSLC = SCSUtility.getOnOffValue(verbatimNSLCButtons);
                currModule.myNslm.verbatimNSLJ = SCSUtility.getOnOffValue(verbatimNSLJButtons);
                currModule.buffering = SCSUtility.getOnOffValue(bufferingButtons);
                currModule.getCurrentVersion = SCSUtility.getOnOffValue(getCurrentVersionButtons);
                return;
            }
        }
        if (event.getSource() instanceof JButton) {
            //	    System.out.println("NslmEditorFrame:actionPerformed JButton");
            bn = (JButton) event.getSource(); //actually a radio button
            String bntext = bn.getText();
            //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
	    /*
	    if (bntext.equals("Add Top Var")) {
		//	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
		finishAdd("VarBefore");
		return;
	    }
	    */
            if (bntext.equals("Add InputPort")) {
                //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                finishAdd("InputPort");
                return;
            }
            if (bntext.equals("Add OutputPort")) {
                //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                finishAdd("OutputPort");
                return;
            }
            if (bntext.equals("Add SubModule")) {
                //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                finishAdd("SubModule");
                return;
            }
            if (bntext.equals("Add Other Variable")) {
                //	    System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                finishAdd("BasicVariable");
                return;
            }
            //====All buttons============================
            if ((bntext.equals("Change")) ||
                    (bntext.equals("Change Name")) ||
                    (bntext.equals("Copy")) ||
                    (bntext.equals("Delete")) ||
                    (bntext.equals("Top")) ||
                    (bntext.equals("Bottom")) ||
                    (bntext.equals("Up")) ||
                    (bntext.equals("Down"))) {

                jlistIndex = declJList.getSelectedIndex();
                //System.out.println("NslmEditorFrame:actionPerformed jlistIndex "+jlistIndex);
                if (jlistIndex == -1) {
                    String errstr = "NslmEditorFrame:Must select a variable first.";
                    JOptionPane.showMessageDialog
                            (null, errstr, "Warning", JOptionPane.WARNING_MESSAGE);

                    return;
                }
            }
            //====Change============================
            if (bntext.equals("Change")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                int ivector;
                selectedVarStr = declJList.getSelectedValue();
                if ((selectedVarStr == null) || (selectedVarStr.equals(""))) {
                    String errstr = "NslmEditorFrame: no variable selected.";
                    warningPopup.display(errstr);
                } else if ((currModule != null) && (currModule.variables != null)) {
                    ivector = currModule.findVarIndex(selectedVarStr);
                    if (ivector < 0) {
                        String errstr = "NslmEditorFrame: variable:" + selectedVarStr + " not found.";
                        warningPopup.display(errstr);

                    } else {
                        finishChange(jlistIndex, ivector);
                    }
                }
                return;
            }
            //====ChangeName============================
            if (bntext.equals("Change Name")) {
                int ivector;
                selectedVarStr = declJList.getSelectedValue();
                if ((selectedVarStr == null) || (selectedVarStr.equals(""))) {
                    String errstr = "NslmEditorFrame: no variable selected.";
                    warningPopup.display(errstr);
                } else if ((currModule != null) && (currModule.variables != null)) {
                    ivector = currModule.findVarIndex(selectedVarStr);
                    if (ivector < 0) {
                        String errstr = "NslmEditorFrame: variable:" + selectedVarStr + " not found.";
                        warningPopup.display(errstr);

                    } else {
                        finishChangeName(jlistIndex, ivector);
                    }
                }
                return;
            }
            //====Copy============================
            if (bntext.equals("Copy")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                int ivector;
                selectedVarStr = declJList.getSelectedValue();
                if ((selectedVarStr == null) || (selectedVarStr.equals(""))) {
                    String errstr = "NslmEditorFrame: no variable selected.";
                    warningPopup.display(errstr);
                } else if ((currModule != null) && (currModule.variables != null)) {
                    ivector = currModule.findVarIndex(selectedVarStr);
                    if (ivector < 0) {
                        String errstr = "NslmEditorFrame: variable:" + selectedVarStr + " not found.";
                        warningPopup.display(errstr);

                    } else {
                        finishCopy(jlistIndex, ivector);
                    }
                }
                return;
            }

            //================delete variable===================
            if (bntext.equals("Delete")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                selectedVarStr = declJList.getSelectedValue();
                String errstr = "NslmEditorFrame: Do you really want to remove the variable: " + selectedVarStr + "?";
                boolean okPressed = okCancelPopup.display(errstr);
                if (!okPressed) {
                    return;
                } else {
                    if ((currModule != null) && (currModule.variables != null)) {
                        boolean worked = currModule.deleteVariable(selectedVarStr);
                        if (!worked) {
                            String errstr2 = "NslmEditorFrame: variable:" + selectedVarStr + " not found.";
                            warningPopup.display(errstr2);
                        } else {
                            declJListModel.removeElement(selectedVarStr);
                            //System.out.println("Debug:NslmEditorFrame:delete");
                        }
                    }
                    return;
                }
            } // if bntext.equals("Delete")
            //================Move to Top ===================
            if (bntext.equals("Top")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                selectedVarStr = declJList.getSelectedValue();
                if ((currModule != null) && (currModule.variables != null)) {
                    declJListModel.removeElementAt(jlistIndex);
                    declJListModel.add(0, selectedVarStr);
                    currModule.moveVariable(0, selectedVarStr);
                    //System.out.println("Debug:NslmEditorFrame:top");
                }
                return;
            }// if bntext.equals("Top")
            //================Move to Bottom ===================
            if (bntext.equals("Bottom")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                selectedVarStr = declJList.getSelectedValue();
                if ((currModule != null) && (currModule.variables != null)) {
                    declJListModel.removeElementAt(jlistIndex);
                    insertAt = declJListModel.size();
                    if (insertAt < 0) insertAt = 0;
                    declJListModel.addElement(selectedVarStr);
                    currModule.moveVariable(insertAt, selectedVarStr);
                    //System.out.println("Debug:NslmEditorFrame:bottom");
                }
                return;
            }// if bntext.equals("Bottom")
            //================Move Up ===================
            if (bntext.equals("Up")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                int declsize1 = declJListModel.size();
                if (declsize1 == 0) {
                    return;
                } else {
                    selectedVarStr = declJList.getSelectedValue();
                    if ((currModule != null) && (currModule.variables != null)) {
                        declJListModel.removeElementAt(jlistIndex);
                        insertAt = jlistIndex - 1;
                        if (insertAt < 0) insertAt = 0;
                        declJListModel.add(insertAt, selectedVarStr);
                        currModule.moveVariable(insertAt, selectedVarStr);
                        //System.out.println("Debug:NslmEditorFrame:up");
                    }
                    return;
                }
            }// if bntext.equals("Up")
            //================Move Down ===================
            if (bntext.equals("Down")) {
                //System.out.println("NslmEditorFrame:actionPerformed "+bntext);
                int declsize1 = declJListModel.size();
                if (jlistIndex == (declsize1 - 1)) { //then last element already
                    return;
                } else {
                    selectedVarStr = declJList.getSelectedValue();
                    if ((currModule != null) && (currModule.variables != null)) {
                        declJListModel.removeElementAt(jlistIndex);
                        int declsize2 = declsize1 - 1;
                        insertAt = jlistIndex + 1;
                        if (insertAt >= declsize2) { //add to end
                            declJListModel.addElement(selectedVarStr);
                            currModule.moveVariable(insertAt, selectedVarStr);
                            //System.out.println("Debug:NslmEditorFrame:down");
                        } else {
                            declJListModel.add(insertAt, selectedVarStr);
                            currModule.moveVariable(insertAt, selectedVarStr);
                            //System.out.println("Debug:NslmEditorFrame:down");
                        }
                    }
                    return;
                } // else if not last element
            }// if bntext.equals("Down")
        }// 	if (event.getSource() instanceof JButton)
        System.err.println("NslmEditorFrame:actionPerformed: unknown source or button");
    }// end actionPerformed

    //--------------------------------------------------
    public void finishNew() {
        //System.out.println("Debug:NslmEditorFrame:finishNew 1");

        if (!currModule.hasNslm) {//if created with schEd or iconEd
            currModule.myNslm = new NSLM();
            currModule.hasNslm = true;
        }
        currNslm = currModule.myNslm;
        //aa: todo: this is where the editable window should be displayed
        //	NslmEditorPanel.currBackgroundCol=NslmEditorPanel.drawBack_col; //static
        //	myNslmEditorPanel.setBackground(NslmEditorPanel.currBackgroundCol);

        this.display(currModule);


        //System.out.println("Debug:NslmEditorFrame:finishNew 2");

        this.noActionTaken = false;
        this.savedInEd = false;

    }

    //--------------------------------------------------
    public void finishOpen() {
        if (!currModule.hasNslm) {//if created with schEd or iconEd
            currModule.myNslm = new NSLM();
            currModule.hasNslm = true;
        }
        currNslm = currModule.myNslm;
        //	NslmEditorPanel.currBackgroundCol=NslmEditorPanel.drawBack_col;
        //	myNslmEditorPanel.setBackground( NslmEditorPanel.currBackgroundCol);

        boolean error = this.display(currModule);

        this.noActionTaken = false;
        this.savedInEd = false;

    }

    //--------------------------------------------------
    public void finishAdd(String dialogType) {
        //System.out.println("Debug:NslmEditorFrame:finishAdd");
        Declaration var;

        DeclarationDialog myDialog = getDialogWindow(dialogType);
        if (myDialog == null) {
            //should not occur
            System.err.println("Error:NslmEditorFrame: myDialog is null");
            return;
        }
        var = currModule.fillVariableName(this, dialogType, "Variable Instance Name(first letter lower case): ");
        if (var == null) return;

        //System.out.println("Debug:NslmEditorFrame: myDialog not null");
        boolean okPressed = myDialog.display(var);
        // since we just do a setVisible(false) now with no dispose,
        // we can just check if the user did a quit or not
        if (okPressed) {
            //now check if a duplicate
            int foundi = currModule.findVarIndex(var.varName);
            if (foundi == -1) {
                var.inNslm = true;
                currModule.addVariable(var);
                declJListModel.addElement(var.varName);
                declJList.setSelectedValue(var.varName, true);//should scroll
            } //otherwise it has been added already
        }
    }

    //--------------------------------------------------
    public void finishChange(int ilist, int ivector) {
        //System.out.println("Debug:NslmEditorFrame:finishChange");
        Declaration var = (Declaration) currModule.variables.elementAt(ivector);
        DeclarationDialog myDialog = getDialogWindow(var.varDialogType);

        //note: ilist and ivector should be the same

        if (myDialog != null) {
            boolean okPressed = myDialog.display(var);
            //don't have to do anything since the name did not change
        }
    }

    //--------------------------------------------------
    public void finishChangeName(int ilist, int ivector) {
        //System.out.println("Debug:NslmEditorFrame:finishChangeName");
        Declaration var = (Declaration) currModule.variables.elementAt(ivector);
        String namestr;
        namestr = (String) JOptionPane.showInputDialog
                (null, "Variable Name: ", "Variable Name", JOptionPane.QUESTION_MESSAGE, null, null, var.varName);
        if (namestr == null) return; //cancel
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.equals("")) return; //blank
        if (namestr.equals(var.varName)) return; //same name
        int index = currModule.findVarIndex(namestr);
        if (index == -1) {//if not found, change name
            var.varName = namestr;
            //System.out.println("Debug:NslmEditorFrame: ilist:"+ilist);
            declJListModel.setElementAt(namestr, ilist);
        } else {  //another variable by that name - cannot change
            String errstr = "NslmEditorFrame: duplicate names - cannot do.";
            warningPopup.display(errstr);
        }
    }

    //--------------------------------------------------
    public void finishCopy(int ilist, int ivector) {
        //System.out.println("Debug:NslmEditorFrame:finishChangeName");
        Declaration var = (Declaration) currModule.variables.elementAt(ivector);
        String namestr;
        namestr = (String) JOptionPane.showInputDialog
                (null, "Copies Name: ", var.varName, JOptionPane.QUESTION_MESSAGE);
        if (namestr == null) return; //cancel
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.equals("")) return; //blank
        if (namestr.equals(var.varName)) return; //same name
        int index = currModule.findVarIndex(namestr);
        if (index == -1) {//if not found, make copy
            declJListModel.addElement(namestr);
            Declaration newvar = var.duplicate();
            newvar.varName = namestr;
            currModule.addVariable(newvar);
        } else {  //another variable by that name - cannot change
            String errstr = "NslmEditorFrame: duplicate names - cannot do.";
            warningPopup.display(errstr);
        }
    }

    //--------------------------------------------------------------
    public void exportNslm() {
        ExportNslm export1 = new ExportNslm(this);
        export1.writeNslm(currDirStr, currModule);
    }
    //--------------------------------------------------------------

    /**
     * getNslmInfo
     */
    public void getNslmInfo() {
        //todo: perform checking here to see that the info is ok
        currModule.moduleType = classTypeJTF.getText();
        currModule.arguments = classArgsJTF.getText();
        currModule.myNslm.extendsWhat = extendsWhatJTF.getText();
        currModule.myNslm.whatsParams = whatsParamsJTF.getText();
        currModule.myNslm.comment1 = commentBlock1TextArea.getText();
        currModule.myNslm.comment2 = commentBlock2TextArea.getText();
        currModule.myNslm.methods = (PlainDocument) methodsPanel.getEditor().getDocument();
    }

    //--------------------------------------------------------------

    /**
     * Handle List Change or valueChanged
     */
    public void valueChanged(ListSelectionEvent event) {
        String selectionStr = "";


        if (event.getSource() instanceof JList) {
            JList selected = (JList) event.getSource();
            selectionStr = (String) selected.getSelectedValue();

            if (selected == declJList) {
                //get the selected name
                //declJListSelected=selectionStr;  //do nothing for now
                return;
            }
        }
    }
    //------------------------------------------------


    //--------------------------------------------------------------
    /**
     * Handle List Change or contentsChanged
     */
/********
 public void contentsChanged(ListDataEvent event) {
 String selectionStr= "";

 if (event.getSource() instanceof DefaultListModel) {
 JList selected=(JList)event.getSource();

 return;
 }
 }
 */
} // end NslmEditorFrame class













    
