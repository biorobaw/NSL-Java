/* SCCS %W% --- %G% --%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * DeclarationDialog - A class representing the dialog popped up when creating a
 * new schematic port in SchEd, icon port in IconEd, or any variable in the
 * NSLM editor.
 *
 * @author Weifang Xie, Amanda Alexander
 * @version %I%, %G%
 * @param parentFrame          pointing to the parentFrame--Sch,Icon,or NSLM EditorFrame
 * @since JDK8
 */

/* aa: will change later to move IconActionListener and SchActionListener and
NslmActionListener
to their own modules. And go back to having 1 parentFrame of type EditorFrame
*/

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

@SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
public class DeclarationDialog extends JDialog implements ListSelectionListener, ActionListener {

    final static int sizex = 575;
    final static int sizey = 460;
    final static int locationx = 400;
    final static int locationy = 400;

    EditorFrame parentFrame;

    String dialogType;
    //    IconEditorFrame iconparent;
    //    SchEditorFrame schparent;
    //    NslmEditorFrame nslparent;
    //Frame parentFrame;

    String libPath = null; //this is a place holder since we do not
    //store the path in currVar, we only store the LibNickName

    Container myDeclPane;

    ActionListener listener;
    ActionListener okListener;

    JLabel classNameLabel2;
    JLabel varNameLabel;
    JPanel varScopeRadioPanel;
    JPanel varConstantRadioPanel;

    String otherTypeStr = "";
    JList<String> varCatagoryTypeJList;
    DefaultListModel<String> varCatagoryTypeModel = null;
    JPanel varDimensionsRadioPanel;
    JTextField varParamsJTF;
    JTextField varInitsJTF;
    JTextField varCommentJTF;
    JPanel portDirectionRadioPanel;
    JPanel portSignalTypeRadioPanel;
    JPanel portBufferingRadioPanel;

    //these are now passed in with - Declaration currVar
    Declaration currVar;  //should have the most up todate info except
    //for text fields
	/*
	  String varName;
	  String varScope;
	  Boolean varConstant;
	  String varType;    
	  String varParams;
	  String varInits;
	  String varDialogType; 
	  String varComment; 
	  public boolean portBuffering;
	  public char portIconDirection; //R or L or T or B
	  public char portSchDirection;//R or L or T or B
	  public char portSignalType;//I or E
        */

    // valid varDialogTypes:
    // InputPort
    // OutputPort
    // SubModule
    // BasicVariable

    final static int commentRows = 1;
    final static int commentWidth = 60;
    final static int commentColumns = 3;
    final static int numBuiltInCatagoryTypes = 11; //double,float,int, NslDouble,NslFloat,NslInt
    final static int numInputPortCatagoryTypes = 5; //NslDin
    final static int numOutputPortCatagoryTypes = 5; //NslDout
    final static int numPortCatagoryTypes = 10; //NslDout
    String portDirectionStr = "left->right";

    boolean blockingInputToOthers = true; //IconEditor and SchEditor needs blocking
    boolean okPressed = false; //tell the calling editor whether it should
    // add the variable or not

    //Buttons:
    //constants
    JRadioButton constantTrueButton = null;
    JRadioButton constantFalseButton = null;
    //scope
    JRadioButton privateButton = null;
    JRadioButton publicButton = null;
    JRadioButton protectedButton = null;
    JRadioButton otherButton = null;
    //dimensions
    JRadioButton zeroButton = null;
    JRadioButton oneButton = null;
    JRadioButton twoButton = null;
    JRadioButton threeButton = null;
    JRadioButton fourButton = null;
    JRadioButton higherDimButton = null;
    //port direction
    JRadioButton leftRightButton = null; //("left->right")
    JRadioButton rightLeftButton = null; //("right")
    JRadioButton topBottomButton = null; //("top")
    JRadioButton bottomTopButton = null; //("bottom")
    //portSignalType
    JRadioButton excitatoryButton = null;
    JRadioButton inhibitoryButton = null;
    //buffering
    JRadioButton bufferingTrueButton = null; //
    JRadioButton bufferingFalseButton = null; //

    JTextField libNickNameJTF = null;
    JTextField libPathNameJTF = null;
    JTextField moduleNameJTF = null;
    JTextField versionNameJTF = null;
    ButtonGroup getCurrentVersionButtons = null;

    WarningDialog warningPopup = null;

    // Note: This window will look different depending on the dialog type.
    // There will be one window for each type:
    // varDialogTypes:
    // InputPort
    // OutputPort
    // SubModule
    // BasicVariable

    // --------------------------------------------------------------------
    // For a dialogType.equals.BasicVariable the window will look like:
    // --------------------------------------------------------------------
    // className:  currClass or currModule or currModel name (for ref only)
    // varName:  varNameLabel
    // varConstant:  [true][false]
    // varScope:  selectionList -----------------------------------
    //                          |public||private||protected||other|
    //                          -----------------------------------
    // varCategoryType:  selectionList ----------------
    //                                |double        |
    //                                |float         |
    //                                |int           |
    //                                |boolean       |
    //                                |char          |
    //                                |charString    |
    //                                |NslDouble     |
    //                                |NslFloat      |
    //                                |NslInt        |
    //                                |NslBoolean    |
    //                                |NslString     |
    //                                |other         |
    //                                ----------------
    // varDimensions:  [0][1][2][3][4][higherDim]
    // varParams:  varParamsJTF
    // varInit:  varInitJTF
    // varComment: varCommentJTF
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // For a dialogType.equals.InputPort or OutputPort the window will look like:
    // --------------------------------------------------------------------
    // className:  currClass or currModule or currModel name (for ref only)
    // varName:  varNameLabel
    // varCategoryType:  selectionList ----------------
    //                                |NslXDouble     |
    //                                |NslXFloat      |
    //                                |NslXInt        |
    //                                |NslXBoolean    |
    //                                |NslXString     |
    //                                -----------------
    // varDimensions:  [0][1][2][3][4][higherDim]
    // varParams:  varParamsJTF
    // varComment: varCommentJTF
    // portIconDirection: [L->R] [R->L] [T->B] [B->T]
    // portsignalType: inhibitory or excitatory  //only for inputports
    // portBuffering (only output ports): true or false
    // -------------------------------------------------------------------

    // --------------------------------------------------------------------
    // For a dialogType.equals.SubModule window will look like:
    //
    // varName:  varNameLabel
    // varParams:  varParamsJTF
    // varInit:  varInitJTF
    // varComment: varCommentJTF
    // choose a file or fill in below  - | button|
    // libNickName:
    // libPathName:
    // className:
    // version:
    // --------------------------------------------------------------------
    /*------------------------------------------------------------------*/
    /* Constructors */
    /*------------------------------------------------------------------*/

    /**
     * Constructor of this class, with the parentFrame set to fm.
     */
    public DeclarationDialog(EditorFrame fm, String dialogType) {
        super(fm, "Declaration Dialog", true); //blockingInputToOthers=true

        warningPopup = new WarningDialog(fm);

        parentFrame = fm;
        this.dialogType = dialogType;
        //	System.out.println("Debug:DeclarationDialog:top of constructor");
        currVar = new Declaration();
        //listener = (ActionListener)new DeclarationDialogActionListener();
        //	    okListener = (ActionListener)new OkDeclarationDialogActionListener();

        //System.out.println("Debug:DeclarationDialog:init "+dialogType);
        if (dialogType.equals("InputPort")) {
            setTitle("Input Port Insert Dialog");
        }
        if (dialogType.equals("OutputPort")) {
            setTitle("Output Port Insert Dialog");
        }
        //System.out.println("Debug:DeclarationDiaglog:constructor called..") ;
        GridBagLayout gblayout = new GridBagLayout();
        myDeclPane = getContentPane();
        myDeclPane.setLayout(gblayout);
        GridBagConstraints gbcon = new GridBagConstraints();
        //##############
        JLabel classNameLabel1 = new JLabel("Class Name: ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(classNameLabel1, gbcon);
        myDeclPane.add(classNameLabel1, gbcon);
        if (parentFrame.currModule == null) {
            classNameLabel2 = new JLabel("");
        } else {
            classNameLabel2 = new JLabel(parentFrame.currModule.moduleName);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(classNameLabel2, gbcon);
        myDeclPane.add(classNameLabel2, gbcon);
        //##############
        JLabel varNameLabel1 = new JLabel("Var Name(lowercase): ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(varNameLabel1, gbcon);
        myDeclPane.add(varNameLabel1, gbcon);
        varNameLabel = new JLabel("                        ");
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(varNameLabel, gbcon);
        myDeclPane.add(varNameLabel, gbcon);
        //##############
        boolean dialogTypeMatch = (dialogType.equals("InputPort")) ||
                (dialogType.equals("OutputPort")) ||
                (dialogType.equals("SubModule"));
        if (!dialogTypeMatch) {
            //	System.out.println("Debug:DeclarationDialog:middle2 of constructor");
            JLabel varConstantLabel1 = new JLabel("Var Constant: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(varConstantLabel1, gbcon);
            myDeclPane.add(varConstantLabel1, gbcon);
            varConstantRadioPanel = new JPanel();
            FlowLayout fl1 = new FlowLayout(FlowLayout.LEFT);
            varConstantRadioPanel.setLayout(fl1);
            ButtonGroup varConstantButtonGroup = new ButtonGroup();

            constantTrueButton = SCSUtility.addRadioButton(varConstantRadioPanel, varConstantButtonGroup, "True", currVar.varConstant, this);
            constantFalseButton = SCSUtility.addRadioButton(varConstantRadioPanel, varConstantButtonGroup, "False", (!(currVar.varConstant)), this);

            SCSUtility.setSecondColumn(gbcon, 1, 2);
            gblayout.setConstraints(varConstantRadioPanel, gbcon);
            myDeclPane.add(varConstantRadioPanel, gbcon);
        }
        //##############
        if (dialogTypeMatch) {
            currVar.varScope = "public";
        } else {
            //	System.out.println("Debug:DeclarationDialog:middle3 of constructor");
            JLabel varScopeLabel1 = new JLabel("Var Scope: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(varScopeLabel1, gbcon);
            myDeclPane.add(varScopeLabel1, gbcon);
            varScopeRadioPanel = new JPanel();

            FlowLayout fl2 = new FlowLayout(FlowLayout.LEFT);
            varScopeRadioPanel.setLayout(fl2);

            ButtonGroup varScopeButtonGroup = new ButtonGroup();
            boolean[] scopeArray = createScopeArray(currVar.varScope);

            //System.out.println("Debug:DeclarationDialog:scope bn:"+i+"  "+scopeArray[i]);

            privateButton = SCSUtility.addRadioButton(varScopeRadioPanel, varScopeButtonGroup, "private", scopeArray[0], this);
            publicButton = SCSUtility.addRadioButton(varScopeRadioPanel, varScopeButtonGroup, "public", scopeArray[1], this);
            protectedButton = SCSUtility.addRadioButton(varScopeRadioPanel, varScopeButtonGroup, "protected", scopeArray[2], this);
            otherButton = SCSUtility.addRadioButton(varScopeRadioPanel, varScopeButtonGroup, "other", scopeArray[3], this);
            SCSUtility.setSecondColumn(gbcon, 1, 4);
            gblayout.setConstraints(varScopeRadioPanel, gbcon);
            myDeclPane.add(varScopeRadioPanel, gbcon);
        }

        //##########
        //	System.out.println("Debug:DeclarationDialog:middle4 of constructor");
        if (!dialogType.equals("SubModule")) { //if not submodule
            JLabel varTypeLabel1 = new JLabel("Var Catagory Type: ");
            if ((dialogType.equals("InputPort")) ||
                    (dialogType.equals("OutputPort"))) {
                SCSUtility.setFirstColumn(gbcon, numInputPortCatagoryTypes);
            } else if (dialogType.equals("BasicVariable")) {
                SCSUtility.setFirstColumn(gbcon, numBuiltInCatagoryTypes);
            }
            gblayout.setConstraints(varTypeLabel1, gbcon);
            myDeclPane.add(varTypeLabel1, gbcon);
            //first create the model
            varCatagoryTypeModel = new DefaultListModel<>();
            if (dialogType.equals("InputPort")) {
                addPortCatagoryTypes(varCatagoryTypeModel, "Din");
            }
            if (dialogType.equals("OutputPort")) {
                addPortCatagoryTypes(varCatagoryTypeModel, "Dout");
            }
            if ((dialogType.equals("BasicVariable"))) {
                addBuiltInCatagoryTypes(varCatagoryTypeModel);
            }
            //now create the JList
            varCatagoryTypeJList = new JList<>(varCatagoryTypeModel);
            varCatagoryTypeJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (currVar.varCategoryType.equals("")) {
                varCatagoryTypeJList.setSelectedIndex(0); //highlight first
            } else {
                setSelectionCatagoryType(varCatagoryTypeJList, currVar.varCategoryType);
            }
            varCatagoryTypeJList.addListSelectionListener(this);

            if ((dialogType.equals("InputPort")) ||
                    (dialogType.equals("OutputPort"))) {
                //		SCSUtility.setSecondColumn(gbcon,numInputPortCatagoryTypes,2); //rows,columns
                SCSUtility.setSecondColumn(gbcon, numInputPortCatagoryTypes); //rows,columns
            } else {
                SCSUtility.setSecondColumn(gbcon, numBuiltInCatagoryTypes);
            }
            //	System.out.println("Debug:DeclarationDialog:middle5 of constructor");
            gblayout.setConstraints(varCatagoryTypeJList, gbcon);
            myDeclPane.add(varCatagoryTypeJList, gbcon);


            JLabel varDimensionsLabel1 = new JLabel("Num of Dimensions: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(varDimensionsLabel1, gbcon);
            myDeclPane.add(varDimensionsLabel1, gbcon);
            varDimensionsRadioPanel = new JPanel();

            FlowLayout fl3 = new FlowLayout(FlowLayout.LEFT);
            varDimensionsRadioPanel.setLayout(fl3);

            ButtonGroup varDimensionsButtonGroup = new ButtonGroup();
            boolean[] dimensionsArray = createDimensionsArray(currVar.varDimensions);
            zeroButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "0", dimensionsArray[0], this);
            oneButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "1", dimensionsArray[1], this);
            twoButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "2", dimensionsArray[2], this);
            threeButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "3", dimensionsArray[3], this);
            fourButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "4", dimensionsArray[3], this);
            if (currVar.varDimensions > 4) {
                higherDimButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "higherDim", true, this);
            } else {
                higherDimButton = SCSUtility.addRadioButton(varDimensionsRadioPanel, varDimensionsButtonGroup, "higherDim", false, this);
            }
            SCSUtility.setSecondColumn(gbcon, 1, 5);
            gblayout.setConstraints(varDimensionsRadioPanel, gbcon);
            myDeclPane.add(varDimensionsRadioPanel, gbcon);

        } //if not submodule
        //##############
        //	System.out.println("Debug:DeclarationDialog:middle6 of constructor");
        JLabel varParamsLabel1 = new JLabel("Var Parameters(with commas): ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(varParamsLabel1, gbcon);
        myDeclPane.add(varParamsLabel1, gbcon);
        if (currVar.varParams.equals("")) {
            varParamsJTF = new JTextField("                        ");
        } else {
            varParamsJTF = new JTextField(currVar.varParams);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(varParamsJTF, gbcon);
        myDeclPane.add(varParamsJTF, gbcon);
        //##############

        //	System.out.println("Debug:DeclarationDialog:middle7 of constructor");
        JLabel varInitsLabel1 = new JLabel("Var Initialization(with commas): ");
        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(varInitsLabel1, gbcon);
        myDeclPane.add(varInitsLabel1, gbcon);
        if (currVar.varInits.equals("")) {
            varInitsJTF = new JTextField("                        ");
        } else {
            varInitsJTF = new JTextField(currVar.varInits);
        }
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(varInitsJTF, gbcon);
        myDeclPane.add(varInitsJTF, gbcon);
        //##############

        //comment field
        //	System.out.println("Debug:DeclarationDialog:middle8 of constructor");
        JLabel varCommentLabel = new JLabel("Comments: ");
        SCSUtility.setFirstColumn(gbcon, commentRows);
        gblayout.setConstraints(varCommentLabel, gbcon);
        myDeclPane.add(varCommentLabel, gbcon);
        //varCommentTextArea=new JTextArea(commentRows,commentWidth);
        if (currVar.varComment.equals("")) {
            varCommentJTF = new JTextField("                        ");
        }
        varCommentJTF.setText(currVar.varComment);

        SCSUtility.setSecondColumn(gbcon, commentRows, commentColumns);
        gblayout.setConstraints(varCommentJTF, gbcon);
        myDeclPane.add(varCommentJTF, gbcon);

        //##############
        if (dialogType.equals("OutputPort")) {
            //	System.out.println("Debug:DeclarationDialog:middle9 of constructor");
            JLabel portBufferingLabel1 = new JLabel("Port Buffering: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(portBufferingLabel1, gbcon);
            myDeclPane.add(portBufferingLabel1, gbcon);
            portBufferingRadioPanel = new JPanel();

            FlowLayout fl4 = new FlowLayout(FlowLayout.LEFT);
            portBufferingRadioPanel.setLayout(fl4);

            ButtonGroup portBufferingButtonGroup = new ButtonGroup();

            bufferingTrueButton = SCSUtility.addRadioButton(portBufferingRadioPanel, portBufferingButtonGroup, "True", currVar.portBuffering, this);
            bufferingFalseButton = SCSUtility.addRadioButton(portBufferingRadioPanel, portBufferingButtonGroup, "False", (!(currVar.portBuffering)), this);

            SCSUtility.setSecondColumn(gbcon, 1, 2);
            gblayout.setConstraints(portBufferingRadioPanel, gbcon);
            myDeclPane.add(portBufferingRadioPanel, gbcon);
        }


        //##############
        if (dialogType.equals("InputPort")) {
            //	System.out.println("Debug:DeclarationDialog:middle10 of constructor");
            JLabel portSignalTypeLabel1 = new JLabel("Port Signal Type:");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(portSignalTypeLabel1, gbcon);
            myDeclPane.add(portSignalTypeLabel1, gbcon);
            portSignalTypeRadioPanel = new JPanel();

            FlowLayout fl5 = new FlowLayout(FlowLayout.LEFT);
            portSignalTypeRadioPanel.setLayout(fl5);

            ButtonGroup portSignalTypeButtonGroup = new ButtonGroup();
            boolean[] signalTypeArray = createSignalTypeArray(currVar.portSignalType);
            excitatoryButton = SCSUtility.addRadioButton(portSignalTypeRadioPanel, portSignalTypeButtonGroup, "Excitatory", signalTypeArray[0], this);
            inhibitoryButton = SCSUtility.addRadioButton(portSignalTypeRadioPanel, portSignalTypeButtonGroup, "Inhibitory", signalTypeArray[1], this);

            SCSUtility.setSecondColumn(gbcon, 1, 2);
            gblayout.setConstraints(portSignalTypeRadioPanel, gbcon);
            myDeclPane.add(portSignalTypeRadioPanel, gbcon);
        }
        //##############

        if ((dialogType.equals("OutputPort")) ||
                (dialogType.equals("InputPort"))) {
            //	System.out.println("Debug:DeclarationDialog:middle11 of constructor");
            JLabel portDirectionLabel1 = new JLabel("Port Direction: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(portDirectionLabel1, gbcon);
            myDeclPane.add(portDirectionLabel1, gbcon);
            portDirectionRadioPanel = new JPanel();

            FlowLayout fl6 = new FlowLayout(FlowLayout.LEFT);
            portDirectionRadioPanel.setLayout(fl6);

            ButtonGroup portDirectionButtonGroup = new ButtonGroup();
            boolean[] directionsArray = createDirectionsArray(currVar.portIconDirection);
            leftRightButton = SCSUtility.addRadioButton(portDirectionRadioPanel, portDirectionButtonGroup, "left->right", directionsArray[0], this);
            rightLeftButton = SCSUtility.addRadioButton(portDirectionRadioPanel, portDirectionButtonGroup, "right->left", directionsArray[1], this);
            topBottomButton = SCSUtility.addRadioButton(portDirectionRadioPanel, portDirectionButtonGroup, "top->bottom", directionsArray[2], this);
            bottomTopButton = SCSUtility.addRadioButton(portDirectionRadioPanel, portDirectionButtonGroup, "bottom->top", directionsArray[3], this);

            SCSUtility.setSecondColumn(gbcon, 1, 4);
            gblayout.setConstraints(portDirectionRadioPanel, gbcon);
            myDeclPane.add(portDirectionRadioPanel, gbcon);

        }

        if (dialogType.equals("SubModule")) {
            JLabel chooseLabel1 = new JLabel("Choose a file or fill in below: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(chooseLabel1, gbcon);
            myDeclPane.add(chooseLabel1, gbcon);

            JPanel bpanel = new JPanel();
            JButton btn = new JButton("Choose File");
            btn.addActionListener(this);
            bpanel.add(btn);
            //myDeclPanel.add("East", bpanel);
            SCSUtility.setSecondColumn(gbcon, 1);
            gblayout.setConstraints(bpanel, gbcon);
            myDeclPane.add(bpanel, gbcon);
            //----------------------------------------------------------
            JLabel libNickNameLabel1 = new JLabel("Lib Nick Name: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(libNickNameLabel1, gbcon);
            myDeclPane.add(libNickNameLabel1, gbcon);

            libNickNameJTF = new JTextField("", 30);
            SCSUtility.setSecondColumn(gbcon, 1);
            gblayout.setConstraints(libNickNameJTF, gbcon);
            myDeclPane.add(libNickNameJTF, gbcon);
            //----------------------------------------------------------
            JLabel libPathNameLabel1 = new JLabel("Lib Path Name: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(libPathNameLabel1, gbcon);
            myDeclPane.add(libPathNameLabel1, gbcon);

            libPathNameJTF = new JTextField("", 30);
            SCSUtility.setSecondColumn(gbcon, 1);
            gblayout.setConstraints(libPathNameJTF, gbcon);
            myDeclPane.add(libPathNameJTF, gbcon);
            //----------------------------------------------------------
            JLabel moduleNameLabel1 = new JLabel("Module Name: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(moduleNameLabel1, gbcon);
            myDeclPane.add(moduleNameLabel1, gbcon);

            moduleNameJTF = new JTextField("", 30);
            SCSUtility.setSecondColumn(gbcon, 1);
            gblayout.setConstraints(moduleNameJTF, gbcon);
            myDeclPane.add(moduleNameJTF, gbcon);

            //----------------------------------------------------------
            JLabel versionNameLabel1 = new JLabel("Version: ");
            SCSUtility.setFirstColumn(gbcon, 1);
            gblayout.setConstraints(versionNameLabel1, gbcon);
            myDeclPane.add(versionNameLabel1, gbcon);

            versionNameJTF = new JTextField("1_1_1", 10);
            SCSUtility.setSecondColumn(gbcon, 1);
            gblayout.setConstraints(versionNameJTF, gbcon);
            myDeclPane.add(versionNameJTF, gbcon);
            //----------------------------------------------------------

            getCurrentVersionButtons = SCSUtility.addOnOffButtonPanel(myDeclPane, gblayout, gbcon, "Let Version Float: ", true, this);

            //----------------------------------------------------------
        } //end if SubModule
        //##############
        //	System.out.println("Debug:DeclarationDialog:middle12 of constructor");
        JPanel okCancelPanel = new JPanel();
        Border etched = BorderFactory.createEtchedBorder();
        okCancelPanel.setBorder(etched);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(this);
        okCancelPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        okCancelPanel.add(cancelButton);

        SCSUtility.setSecondColumn(gbcon, 1, 2);
        gblayout.setConstraints(okCancelPanel, gbcon);
        myDeclPane.add(okCancelPanel, gbcon);

        addWindowListener(new DWAdapter());
        //	System.out.println("Debug:DeclarationDialog:bottom of constructor");
    } /* end constructor */


    /**
     * -------------------------------------------------------------
     * addBuiltInCatagoryTypes- (varCatagoryTypeModel,"Din")
     */
    // varCategoryType:  selectionList ----------------
    //                                |double        |
    //                                |float         |
    //                                |int           |
    //                                |boolean       |
    //                                |char          |
    //                                |charString    |
    //                                |NslDouble     |
    //                                |NslFloat      |
    //                                |NslInt        |
    //                                |NslBoolean    |
    //                                |NslString     |
    //                                |other         |
    //                                ----------------
    public void addBuiltInCatagoryTypes(DefaultListModel<String> model) {
        String nameStr;
        String prefix = "Nsl";
        nameStr = "double";
        model.addElement(nameStr);
        nameStr = "float";
        model.addElement(nameStr);
        nameStr = "int";
        model.addElement(nameStr);
        nameStr = "boolean";
        model.addElement(nameStr);
        nameStr = "char";
        model.addElement(nameStr);
        nameStr = "charString";
        model.addElement(nameStr);
        nameStr = prefix + "Double";
        model.addElement(nameStr);
        nameStr = prefix + "Float";
        model.addElement(nameStr);
        nameStr = prefix + "Int";
        model.addElement(nameStr);
        nameStr = prefix + "Boolean";
        model.addElement(nameStr);
        nameStr = prefix + "String";
        model.addElement(nameStr);
        nameStr = "other";
        model.addElement(nameStr);
    }

    /**
     * -------------------------------------------------------------
     * addPortCatagoryTypes- (varCatagoryTypeModel,"Din")
     */
    public void addPortCatagoryTypes(DefaultListModel<String> model, String inOrOutStr) {
        String nameStr;
        String prefix = "Nsl" + inOrOutStr;
        nameStr = prefix + "Double";
        model.addElement(nameStr);
        nameStr = prefix + "Float";
        model.addElement(nameStr);
        nameStr = prefix + "Int";
        model.addElement(nameStr);
        nameStr = prefix + "Boolean";
        model.addElement(nameStr);
        nameStr = prefix + "String";
        model.addElement(nameStr);
    }

    /**
     * -------------------------------------------------------------
     * createScopeArray
     */
    public boolean[] createScopeArray(String scope) {
        boolean[] blist = new boolean[4];

        blist[0] = (scope.equals("private"));
        blist[1] = (scope.equals("public"));
        blist[2] = (scope.equals("protected"));
        //other
        blist[3] = (!blist[0]) && (!blist[1]) && (!blist[2]); //other

        //		for (int i=0; i<4; i++) {
        //		    System.out.println("Debug:DeclarationDialog:C:scope bn:"+i+"  "+blist[i]);
        //		}

        return (blist);
    }

    /**
     * -------------------------------------------------------------
     * createSignalTypeArray
     */
    public boolean[] createSignalTypeArray(char signalType) {
        boolean[] blist = new boolean[2];

        blist[0] = (signalType == 'E');
        blist[1] = (signalType == 'I');
        return (blist);
    }

    /**
     * -------------------------------------------------------------
     * createDimensionsArray
     */
    public boolean[] createDimensionsArray(int varDimensions) {
        //0,1,2,3,4,higher
        boolean[] blist = new boolean[6];
        int i;
        for (i = 0; i < 6; i++) {
            blist[i] = (varDimensions == i);
        }
        return (blist);
    }

    /**
     * -------------------------------------------------------------
     * createDirectionsArray
     */
    public boolean[] createDirectionsArray(String direction) {
        boolean[] blist = new boolean[4];
        int i;

        blist[0] = (direction.equals("left->right"));
        blist[1] = (direction.equals("right->left"));
        blist[2] = (direction.equals("top->bottom"));
        blist[3] = (direction.equals("bottom->top"));

        return (blist);
    }

    /**
     * -------------------------------------------------------------
     * createDirectionsArray
     */
    public boolean[] createDirectionsArray(char direction) {
        boolean[] blist = new boolean[4];
        int i;

        blist[0] = (direction == 'L');
        blist[1] = (direction == 'R');
        blist[2] = (direction == 'T');
        blist[3] = (direction == 'B');

        return (blist);
    }

    /**
     * -------------------------------------------------------------
     */
    public String figurePortIconDirectionStr(char direction) {
        String outstr = "";
        if (direction == 'L') { //("left->right")
            outstr = "left->right";
        } else if (direction == 'R') { //("right->left")
            outstr = "right->left";
        } else if (direction == 'T') { //("top->bottom")
            outstr = "top->bottom";
        } else if (direction == 'B') { //("bottom->top")
            outstr = "bottom->top";
        }
        return (outstr);
    }
    //portSignalType

    /**
     * -------------------------------------------------------------
     */
    public char figurePortIconDirectionChar(String direction) {
        char outchar = '0'; //not found
        switch (direction) {
            case "left->right":
                outchar = 'L';
                break;
            case "right->left":
                outchar = 'R';
                break;
            case "top->bottom":
                outchar = 'T';
                break;
            case "bottom->top":
                outchar = 'B';
                break;
        }
        return (outchar);
    }

    /**
     * -------------------------------------------------------------
     * set selection catagory type
     */
    public void setSelectionCatagoryType(JList<String> mylist, String catagoryType) {
        int i;
        boolean found = false;
        mylist.setSelectedValue(catagoryType, true);//should scroll

    } //end setSelectionCatagoryType

    /**
     * -------------------------------------------------------------
     * valueChanged- Handle valueChanged event for all JLists
     */
    public void valueChanged(ListSelectionEvent event) {
        //	    System.out.println("Debug:DeclarationDialog:valueChanged1");
        JList<String> list;
        JButton button;
        String selected;
        if (event.getSource() instanceof JList) {
            //	    System.out.println("Debug:DeclarationDialog:valueChanged JList");
            list = (JList<String>) event.getSource();
            selected = list.getSelectedValue();
            if (list == varCatagoryTypeJList) {
                currVar.varCategoryType = selected;
                if (currVar.varCategoryType.equals("other")) {
                    if ((currVar.varType != null) && (!(currVar.varType.equals("")))) {
                        otherTypeStr = (String) JOptionPane.showInputDialog(null, "other: ", "Var Type", JOptionPane.QUESTION_MESSAGE, null, null, currVar.varType);
                    } else {
                        otherTypeStr = JOptionPane.showInputDialog
                                (null, "other: ", "Var Type", JOptionPane.QUESTION_MESSAGE);
                    }
                    if (otherTypeStr != null) {
                        if (otherTypeStr.equals("")) {
                            currVar.varType = "";
                        } else {
                            currVar.varType = otherTypeStr.trim();
                        }
                    }  //cancel done

                }
            }
        }
        //	    System.out.println("Debug:DeclarationDialog:valueChanged end");
    }  //end valueChanged

    /**
     * ----------------------------------------------
     * setDeclarationInfo
     * this method sets the information from var to
     * the panel's textfields and buttons
     *
     * @return worked boolean
     */
    private boolean setDeclarationInfo(Declaration inVar) {

        //System.out.println("Debug:DeclarationDialog:setDeclarationInfo1");
        if (inVar == null) {
            return (true);
        }

        if (!(dialogType.equals(inVar.varDialogType))) {
            String errstr = "DeclarationDialog: dialogType and input variable dialog type do not agree.";
            warningPopup.display(errstr);
            return false;
        }
        currVar.varDialogType = inVar.varDialogType; //not displayed

        if (parentFrame.currModule == null) {
            classNameLabel2.setText("");
        } else {
            classNameLabel2.setText(parentFrame.currModule.moduleName);
        }

        if (inVar.varName != null) {
            currVar.varName = inVar.varName;
            varNameLabel.setText(inVar.varName);
        }


        if ((!dialogType.equals("InputPort")) &&
                (!dialogType.equals("OutputPort")) &&
                (!dialogType.equals("SubModule"))) {
            if (inVar.varScope == null) {
                currVar.varScope = "private";
            } else {
                currVar.varScope = inVar.varScope;
            }
            boolean[] scopeArray = createScopeArray(currVar.varScope);
            //		    for (int i=0; i<4; i++) {
            //			System.out.println("Debug:DeclarationDialog:scope bn:"+i+"  "+scopeArray[i]);
            //		    }
            privateButton.setSelected(scopeArray[0]);
            publicButton.setSelected(scopeArray[1]);
            protectedButton.setSelected(scopeArray[2]);
            otherButton.setSelected(scopeArray[3]); //other

            //constant
            currVar.varConstant = inVar.varConstant; //boolean
            constantTrueButton.setSelected(currVar.varConstant);
            constantFalseButton.setSelected(!(currVar.varConstant));
        }  //skip
        //skip


        if (!(dialogType.equals("SubModule"))) {
            if ((inVar.varCategoryType == null) ||
                    (inVar.varCategoryType.equals(""))) {
                if (dialogType.equals("InputPort")) {
                    currVar.varCategoryType = "NslDinDouble";
                    currVar.varType = "NslDinDouble0";
                }
                if (dialogType.equals("OutputPort")) {
                    currVar.varCategoryType = "NslDoutDouble";
                    currVar.varType = "NslDoutDouble0";
                }
                if (dialogType.equals("BasicVariable")) {
                    currVar.varCategoryType = "double";
                    currVar.varType = "double";
                } else { //not possible
                    currVar.varCategoryType = "other";
                    currVar.varType = "Error";
                }
            } else {
                currVar.varCategoryType = inVar.varCategoryType;
                currVar.varType = inVar.varType;
            }
            setSelectionCatagoryType(varCatagoryTypeJList, currVar.varCategoryType);
        } //end if Not SubModule

        if (!(dialogType.equals("SubModule"))) {
            currVar.varDimensions = inVar.varDimensions;
            boolean[] dimensions = createDimensionsArray(currVar.varDimensions);
            zeroButton.setSelected(dimensions[0]);
            oneButton.setSelected(dimensions[1]);
            twoButton.setSelected(dimensions[2]);
            threeButton.setSelected(dimensions[3]);
            fourButton.setSelected(dimensions[4]);
            higherDimButton.setSelected(dimensions[5]);
        }
        if (inVar.varParams != null) {
            currVar.varParams = inVar.varParams; //no default
            varParamsJTF.setText(currVar.varParams);
        }
        if (inVar.varInits != null) {  //no default
            currVar.varInits = inVar.varInits;
            varInitsJTF.setText(currVar.varInits);
        }

        if (inVar.varComment != null) {  //no default
            currVar.varComment = inVar.varComment;
            varCommentJTF.setText(currVar.varComment);
        }
        if (dialogType.equals("OutputPort")) {
            currVar.portBuffering = inVar.portBuffering;  //boolean
            bufferingTrueButton.setSelected(currVar.portBuffering);
            bufferingFalseButton.setSelected(!(currVar.portBuffering));
        }
        if (dialogType.equals("InputPort")) {
            currVar.portSignalType = inVar.portSignalType;  //char
            boolean[] signalTypeArray = createSignalTypeArray(currVar.portSignalType);
            excitatoryButton.setSelected(signalTypeArray[0]);
            inhibitoryButton.setSelected(signalTypeArray[1]);
        }
        if ((dialogType.equals("OutputPort")) ||
                (dialogType.equals("InputPort"))) {
            //assuming that if IconDirection set then SchDirection is set
            portDirectionStr = figurePortIconDirectionStr(inVar.portIconDirection);
            currVar.portIconDirection = inVar.portIconDirection;
            currVar.portSchDirection = inVar.portSchDirection;
            boolean[] directionsArray = createDirectionsArray(currVar.portIconDirection);
            leftRightButton.setSelected(directionsArray[0]);
            rightLeftButton.setSelected(directionsArray[1]);
            topBottomButton.setSelected(directionsArray[2]);
            bottomTopButton.setSelected(directionsArray[3]);
        }
        //	    System.out.println("Debug:DeclarationDialog:setDeclarationInfo2");
        if (dialogType.equals("SubModule")) {
            if (!(inVar.varType.equals(""))) {
                currVar.varCategoryType = inVar.varCategoryType;  //also module type
                currVar.varType = inVar.varType;
                moduleNameJTF.setText(currVar.varType);
            }
            if (!(inVar.modLibNickName.equals(""))) {
                currVar.modLibNickName = inVar.modLibNickName;
                libNickNameJTF.setText(currVar.modLibNickName);
            }
            //do libPath below
            if (!(inVar.modVersion.equals(""))) {
                currVar.modVersion = inVar.modVersion;
                versionNameJTF.setText(currVar.modVersion);
            }
            currVar.modGetCurrentVersion = inVar.modGetCurrentVersion;
            SCSUtility.setOnOffButtonGroup(getCurrentVersionButtons, currVar.modGetCurrentVersion);

            if (!(currVar.modLibNickName.equals(""))) {
                try {
                    libPath = SCSUtility.getLibPathName(currVar.modLibNickName);
                } catch (FileNotFoundException fnfe) {
                    String errstr = "DeclarationDialog:Library not found for " + currVar.modLibNickName;
                    warningPopup.display(errstr);
                    return false;
                } catch (IOException ioe) {
                    String errstr = "DeclarationDialog:IO error while looking for " + currVar.modLibNickName;
                    warningPopup.display(errstr);
                    return false;
                }
                return libPath != null;
            }
        }
        return true;
    }

    /**
     * ----------------------------------------------
     * updateCurrVar
     * this method gets the information from either currVar or from
     * the panel's textfield
     */
    private void updateCurrVar() {

        // currVar.varName=varNameLabel.getText().trim().replace(' ','_');
        if (!(dialogType.equals("SubModule"))) {
            currVar.varCategoryType = varCatagoryTypeJList.getSelectedValue();
            if (!currVar.varCategoryType.equals("other")) {
                if (currVar.varCategoryType.startsWith("Nsl")) {
                    currVar.varType = currVar.varCategoryType + currVar.varDimensions;
                } else { //native
                    currVar.varType = currVar.varCategoryType;
                }
            }  //currVar.varType set in actionPerformed for other
            // and in setDeclInfo

        }
        currVar.varParams = varParamsJTF.getText().trim();
        currVar.varInits = varInitsJTF.getText().trim();
        currVar.varComment = varCommentJTF.getText().trim();
        if (dialogType.equals("SubModule")) {
            //path does not get copied to the variable
            currVar.modLibNickName = libNickNameJTF.getText().trim();
            currVar.varType = moduleNameJTF.getText().trim();
            currVar.modVersion = versionNameJTF.getText().trim();
            currVar.modGetCurrentVersion = SCSUtility.getOnOffValue(getCurrentVersionButtons);
        }
    }

    /**
     * ----------------------------------------------
     * getDeclarationInfo
     * note1: updateCurrVar should have already been called in
     * actionPerformed
     * note2: must copy field by field so that the
     * variable passed in contains the new data.
     * just setting outVar=currVar will not work,
     * nor will will outVar=duplicate(currVar);
     * In both cases you try to change what outVar points
     * to which can only happen in the class calling DeclarationDialog.display
     */
    private void getDeclarationInfo(Declaration outVar) {
        outVar.varName = currVar.varName;
        outVar.varScope = currVar.varScope;
        outVar.varConstant = currVar.varConstant;
        outVar.varCategoryType = currVar.varCategoryType;
        outVar.varDimensions = currVar.varDimensions;
        outVar.varType = currVar.varType;
        outVar.varParams = currVar.varParams;
        outVar.varInits = currVar.varInits;
        outVar.varDialogType = currVar.varDialogType;
        outVar.varComment = currVar.varComment;
        if ((currVar.varDialogType.equals("InputPort")) ||
                (currVar.varDialogType.equals("OutputPort"))) {
            outVar.portBuffering = currVar.portBuffering;
            outVar.portIconDirection = currVar.portIconDirection;
            outVar.portSchDirection = currVar.portSchDirection;
        }
        if (currVar.varDialogType.equals("InputPort")) {
            outVar.portSignalType = currVar.portSignalType;
        }
        if (dialogType.equals("SubModule")) {
            //path does not get copied to the variable
            outVar.modLibNickName = currVar.modLibNickName;
            outVar.varType = currVar.varType;
            outVar.modVersion = currVar.modVersion;
            outVar.modGetCurrentVersion = currVar.modGetCurrentVersion;
        }
    }

    /**
     * -------------------------------------------------------------
     */
    public boolean display(Declaration var) {
        boolean worked;
        worked = setDeclarationInfo(var);
        //	    System.out.println("Debug:DeclarationDialog:set worked "+worked);
        if (!worked) {
            return (false); //cancel
        } else {
            okPressed = false;
            setLocation(new Point(locationx, locationy));
            setSize(sizex, sizey);
            setVisible(true); //this is blocking so we must use show?
            //show();
            //		System.out.println("Debug:DeclarationDialog:after setVisible");
            if (okPressed) {
                //		    System.out.println("Debug:DeclarationDialog:after setVisible2");
                getDeclarationInfo(var);
                //		    System.out.println("Debug:DeclarationDialog:after getDeclar");
            }
            //setVisible(false);
            return (okPressed);
        }
    }

    /**
     * -------------------------------------------------------------
     */

    public void actionPerformed(ActionEvent event) {
        //todo: change to button panels as in CoreJave book 509
        JRadioButton jradiobutton;
        JButton jbutton;
        String buttonlabel;

        // now do buttons
        //System.out.println("Debug:DeclarationDialog:actionPerformed1");

        if (event.getSource() instanceof JRadioButton) {
            //System.out.println("Debug:DeclarationDialog:actionPerformed Jradiobutton");
            jradiobutton = (JRadioButton) event.getSource();
            buttonlabel = jradiobutton.getText();
            //	    System.out.println("Debug:DeclarationDialog:actionPerformed Jradiobutton"+buttonlabel);
            //constant
            if (jradiobutton == constantTrueButton) {
                currVar.varConstant = true;
                //	    System.out.println("Debug:DeclarationDialog:actionPerformed Jradiobutton"+buttonlabel);
                return;
            }
            if (jradiobutton == constantFalseButton) {
                //	    System.out.println("Debug:DeclarationDialog:actionPerformed Jradiobutton"+buttonlabel);
                currVar.varConstant = false;
                return;
            }
            //scope
            if (jradiobutton == privateButton) {
                currVar.varScope = buttonlabel;
                return;
            }
            if (jradiobutton == publicButton) {
                currVar.varScope = buttonlabel;
                return;
            }
            if (jradiobutton == protectedButton) {
                currVar.varScope = buttonlabel;
                return;
            }
            if (jradiobutton == otherButton) {
                if (currVar == null) return;
                String scopeValue;
                //center in middle due to the null frame first param
                if ((currVar.varScope != null) && (currVar.scopeIsOther())) {
                    scopeValue = (String) JOptionPane.showInputDialog
                            (null, "other: ", "Scope", JOptionPane.QUESTION_MESSAGE, null, null, currVar.varScope);
                } else {
                    scopeValue = JOptionPane.showInputDialog
                            (null, "other: ", "Scope", JOptionPane.QUESTION_MESSAGE);
                }
                if (scopeValue != null) {
                    if (scopeValue.equals("")) {
                        currVar.varScope = "";
                    } else {
                        currVar.varScope = scopeValue;
                    }
                }  //cancel

                return;
            }
            //dimensions
            if (jradiobutton == zeroButton) {
                currVar.varDimensions = 0;
                return;
            }
            if (jradiobutton == oneButton) {
                currVar.varDimensions = 1;
                return;
            }
            if (jradiobutton == twoButton) {
                currVar.varDimensions = 2;
                return;
            }
            if (jradiobutton == threeButton) {
                currVar.varDimensions = 3;
                return;
            }
            if (jradiobutton == fourButton) {
                currVar.varDimensions = 4;
                return;
            }
            if (jradiobutton == higherDimButton) {
                //center in middle due to the null frame first param
                String higherDimValue;
                if (currVar == null) {
                    return; //
                } //currVar.higherDimValue must start initialized
                higherDimValue = (String) JOptionPane.showInputDialog
                        (null, "higher dimension: ", "Dimension", JOptionPane.QUESTION_MESSAGE, null, null, Integer.toString(currVar.varDimensions));
                if (higherDimValue != null) {
                    if (higherDimValue.equals("")) {
                        currVar.varDimensions = 0;
                    } else {
                        currVar.varDimensions = Integer.parseInt(higherDimValue.trim());
                    }
                }  //cancel pressed

                return;
            }
            //port direction
            if ((jradiobutton == leftRightButton) ||
                    (jradiobutton == rightLeftButton) ||
                    (jradiobutton == topBottomButton) ||
                    (jradiobutton == bottomTopButton)) {
                char char1 = figurePortIconDirectionChar(buttonlabel);
                currVar.portIconDirection = char1;
                currVar.portSchDirection = char1;
                return;
            }
            //portSignalType
            if (jradiobutton == excitatoryButton) {
                currVar.portSignalType = 'E';
                return;
            }
            if (jradiobutton == inhibitoryButton) {
                currVar.portSignalType = 'I';
                return;
            }
            //buffering
            if (jradiobutton == bufferingTrueButton) { //
                currVar.portBuffering = true;
                return;
            }
            if (jradiobutton == bufferingFalseButton) { //
                currVar.portBuffering = false;
                return;
            }
            if ((buttonlabel.equals("On")) ||
                    (buttonlabel.equals("Off"))) {
                currVar.modGetCurrentVersion = SCSUtility.getOnOffValue(getCurrentVersionButtons);
                return;
            }
            String errstr = "DeclarationDialog:Unknown Jradiobutton name: " + buttonlabel;
            warningPopup.display(errstr);
            return;
        } //end if JRadioButton

        //#####################################################
        if (event.getSource() instanceof JButton) {
            //System.out.println("Debug:DeclarationDialog:actionPerformed JButton");
            jbutton = (JButton) event.getSource();
            buttonlabel = jbutton.getText();
            //	    System.out.println("Debug:DeclarationDialog:actionPerformed JButton"+buttonlabel);

            if (buttonlabel.equals("Ok")) {
                //System.out.println("DeclarationDialog: ok pushed");
                boolean worked = verifyInput();  //calls updateCurrVar
                if (worked) {
                    okPressed = true;
                    setVisible(false);//2/26
                }
                return;
            }
            if (buttonlabel.equals("Cancel")) {
                //System.out.println("DeclarationDialog: cancel pushed");
                okPressed = false;
                setVisible(false);//2/26
                return;
            }
            if ((dialogType.equals("SubModule")) &&
                    (buttonlabel.equals("Choose File"))) {
                //System.out.println("DeclarationDialog: choose file pushed");
                TableFileSelector tfs;
                try {
                    tfs = new TableFileSelector(parentFrame, "Module", true);
                } catch (FileNotFoundException e) {
                    warningPopup.display("DeclarationDialog:1:File not found.");
                    return;
                } catch (IOException e) {
                    warningPopup.display("DeclarationDialog:1:Input/Output exception");
                    return;
                }
                tfs.setLocation(new Point(300, 200));
                tfs.pack();
                tfs.setSize(700, 300);
                tfs.setVisible(true);
                if (tfs.pushed.equals("Ok")) {
                    currVar.modLibNickName = tfs.returnLibraryNickName();
                    libNickNameJTF.setText(currVar.modLibNickName);
                    libPath = tfs.returnLibraryPath();
                    libPathNameJTF.setText(libPath);
                    currVar.varType = tfs.moduleName;
                    moduleNameJTF.setText(currVar.varType);
                    currVar.modVersion = tfs.versionName;
                    versionNameJTF.setText(currVar.modVersion);
                    //System.out.println("Debug:DeclarationDialog:);
                }
                return; //until next action performed
            }//end or choose file

            String errstr = "DeclarationDialog:Unknown JButton name: " + buttonlabel;
            warningPopup.display(errstr);
            return;
        }  //end = JButton
        //if you get here - unknown source
        String errstr = "DeclarationDialog:Unknown button or source";
        warningPopup.display(errstr);
        //	    System.out.println("Debug:DeclarationDialog:actionPerformed - end");
    } //end actionPerformed

    //---------------------------------------------------------------
    // verifyInput
    // @return worked if true
    public boolean verifyInput() {
        String errstr;
        boolean worked = true;
        updateCurrVar(); //make currVar uptodate including text fields
        //should be checked in calling method
        if (currVar.varName.equals("")) {
            errstr = "DeclarationDialog: Please provide a variable name.";
            warningPopup.display(errstr);
            worked = false;
        }
        //todo: check for bad letters
        if ((currVar.varDimensions > 0) && (currVar.varParams.equals(""))) {
            //this needs the tokenizer to check the parameters
            errstr = "DeclarationDialog: Please provide the parameters for the dimensions specified.";
            warningPopup.display(errstr);
            worked = false;
        }
        return (worked);
    } //end verifyInput

    //---------------------------------------------------------------
    // inner class
    class DWAdapter extends WindowAdapter {
        /**
         * Handle windowClosing event.
         */
        public void windowClosing(WindowEvent event) {
            int selected = JOptionPane.showConfirmDialog(null, "Do you really want to close the window?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (selected == JOptionPane.OK_OPTION) {
                Window w = event.getWindow();
                w.setVisible(false);
                //w.dispose();  //i think we want to leave the window here
            }  //cancel was selected: do not close the window

        } //end windowClosing

    } //end inner class
    //-----------------------------------------------

} //end class DeclarationDialog




	
		

