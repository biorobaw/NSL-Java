/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// This contains a cardlayout options window


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@SuppressWarnings("Duplicates")
public class OptionsFrame extends Dialog
        implements ActionListener, ItemListener {

    private Font localFont;
    private String[] fontlist;
    private ColorDemo demoboard;
    private EditorFrame parentFrame;

    public static String text_string = "DUMMY";
    public static boolean newPreferences = false;

    //options and choices
    public static Color grid_col = Color.red;
    Choice gridColChoice;//1
    public static Color drawBack_col = Color.black;
    Choice backgroundColChoice; //2
    public static Color noActionTaken_col = Color.lightGray;
    Choice noActionTakenColChoice; //3
    public static Color inPin_col = Color.white;
    Choice inPinColChoice;//4
    public static Color outPin_col = Color.white;
    Choice outPinColChoice;//5
    public static Color inPortFill_col = Color.cyan;
    Choice inPortFillColChoice;//6
    public static Color outPortFill_col = Color.cyan;
    Choice outPortFillColChoice;//7
    public static Color connection_col = Color.green;
    Choice connectionColChoice;//8
    public static Integer connectionWidth = 2;
    Choice connectionWidthChoice;//9
    public static Color line_col = Color.green;
    Choice lineColChoice;//10
    public static Color poly_col = Color.red;
    Choice polyColChoice;//11
    public static Color rect_col = Color.green;
    Choice rectColChoice;//12
    public static Color oval_col = Color.orange;
    Choice ovalColChoice;//13
    public static Color freeText_col = Color.green;
    Choice freeTextColChoice;//14
    public static String freeTextFontName = "Monospaced"; //aa:99/4/13
    Choice freeTextFontNameChoice;//15
    public static int freeTextSize = 10;//aa:99/4/13
    Choice freeTextSizeChoice;//16
    public static Font freeTextFont = new Font(freeTextFontName, Font.BOLD, freeTextSize);
    public static Color moduleText_col = Color.red;
    Choice moduleTextColChoice;//17
    public static String moduleTextFontName = "Monospaced";
    Choice moduleTextFontNameChoice;//18
    public static int moduleTextSize = 10;
    Choice moduleTextSizeChoice;//19
    public static Font moduleTextFont = new Font(moduleTextFontName, Font.BOLD, moduleTextSize);
    public static String moduleTextLocation = "CENTER"; //options: ABOVE, BELOW, RIGHT, LEFT of instance icon
    Choice moduleTextLocationChoice;//20
    public static Color instanceText_col = Color.green;
    Choice instanceTextColChoice;//21
    public static int instanceTextSize = 10;//aa:99/4/13
    Choice instanceTextSizeChoice;//22
    public static String instanceTextFontName = "Monospaced";//aa:00/4/13
    Choice instanceTextFontNameChoice;//23
    public static Font instanceTextFont = new Font(instanceTextFontName, Font.BOLD, instanceTextSize);
    public static String instanceTextLocation = "BELOW"; //options: CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon
    Choice instanceTextLocationChoice;//24
    public static Color highlight_col = Color.red;
    Choice highlightColChoice;//25

    //----------
    //note:in IconPanel and SchPanel: currBackgroundCol  will be either drawBack_col or noActionTaken_col

    boolean backgroundChangedFlag = false;

    // constructor
    //-----------------------------------------------------
    public OptionsFrame(EditorFrame efm, String title, boolean modal) {
        //    super("Options");
        super(efm, title, modal);
        setTitle("Options");

        parentFrame = efm;

        Label demoLabel = new Label("Sample Color ");
        add(demoLabel);

        demoboard = new ColorDemo();
        //fontlist = Toolkit.getDefaultToolkit().getFontList();
        fontlist = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        // setLayout( new FlowLayout());

        //number of rows = num options plus 2
        // however size of window set in SchematicPanel and IconPanel
        GridLayout gl = new GridLayout(27, 2); //see number of options above
        this.setLayout(gl);

        gl.setHgap(8);
        gl.setVgap(4);

        add(demoboard);

        setOptions(); //get UserPreference or this files defaults otherwise, and set options
        //------------------------
        Label gridColLabel = new Label("Grid Color");
        add(gridColLabel);

        gridColChoice = new Choice();
        SCSUtility.setColorMenu(gridColChoice);

        gridColChoice.select(SCSUtility.returnColorNameString(grid_col));
        gridColChoice.addItemListener(this);
        add(gridColChoice);
        //------------------------
        Label backgroundColLabel = new Label("Background Color");
        add(backgroundColLabel);

        backgroundColChoice = new Choice();
        SCSUtility.setColorMenu(backgroundColChoice);
        backgroundColChoice.select(SCSUtility.returnColorNameString(drawBack_col));
        backgroundColChoice.addItemListener(this);
        add(backgroundColChoice);

        //    System.out.println("Background col is:  " +    SCSUtility.returnColorNameString (  IconPanel.freeText_col )  );

        //------------------------ 
        Label noActionTakenColLabel = new Label("No Action Taken Color");
        add(noActionTakenColLabel);
        noActionTakenColChoice = new Choice();
        SCSUtility.setColorMenu(noActionTakenColChoice);

        noActionTakenColChoice.select(SCSUtility.returnColorNameString(noActionTaken_col));
        noActionTakenColChoice.addItemListener(this);
        add(noActionTakenColChoice);

        //------------------------
        Label inPinColLabel = new Label("InPin Color");
        add(inPinColLabel);

        inPinColChoice = new Choice();
        SCSUtility.setColorMenu(inPinColChoice);

        inPinColChoice.select(SCSUtility.returnColorNameString(IconInport.port_col));
        inPinColChoice.addItemListener(this);
        add(inPinColChoice);
        //------------------------

        Label outPinColLabel = new Label("outPin Color");
        add(outPinColLabel);

        outPinColChoice = new Choice();
        SCSUtility.setColorMenu(outPinColChoice);

        outPinColChoice.select(SCSUtility.returnColorNameString(IconOutport.port_col));
        outPinColChoice.addItemListener(this);
        add(outPinColChoice);
        //------------------------

        Label inPortFillColLabel = new Label("inPortFill Color");
        add(inPortFillColLabel);

        inPortFillColChoice = new Choice();
        SCSUtility.setColorMenu(inPortFillColChoice);

        inPortFillColChoice.select(SCSUtility.returnColorNameString(inPortFill_col));
        inPortFillColChoice.addItemListener(this);
        add(inPortFillColChoice);
        //------------------------

        Label outPortFillColLabel = new Label("outPortFill Color");
        add(outPortFillColLabel);

        outPortFillColChoice = new Choice();
        SCSUtility.setColorMenu(outPortFillColChoice);

        outPortFillColChoice.select(SCSUtility.returnColorNameString(outPortFill_col));
        outPortFillColChoice.addItemListener(this);
        add(outPortFillColChoice);
        //------------------------

        Label connectionColLabel = new Label("Connection Color");
        add(connectionColLabel);

        connectionColChoice = new Choice();
        SCSUtility.setColorMenu(connectionColChoice);

        connectionColChoice.select(SCSUtility.returnColorNameString(connection_col));
        connectionColChoice.addItemListener(this);
        add(connectionColChoice);
        //------------------------

        Label connectionWidthLabel = new Label("Connection Width");
        add(connectionWidthLabel);

        connectionWidthChoice = new Choice();
        connectionWidthChoice.add("1");
        connectionWidthChoice.add("2");
        connectionWidthChoice.add("3");
        connectionWidthChoice.add("4");

        connectionWidthChoice.select(connectionWidth.toString());
        //connectionWidthChoice.addItemListener(this);
        add(connectionWidthChoice);
        //------------------------

        Label lineColLabel = new Label("Line Color");
        add(lineColLabel);

        lineColChoice = new Choice();
        SCSUtility.setColorMenu(lineColChoice);

        lineColChoice.select(SCSUtility.returnColorNameString(line_col));
        lineColChoice.addItemListener(this);
        add(lineColChoice);
        //------------------------

        Label polyColLabel = new Label("Polygon Color");
        add(polyColLabel);

        polyColChoice = new Choice();
        SCSUtility.setColorMenu(polyColChoice);

        polyColChoice.select(SCSUtility.returnColorNameString(poly_col));
        polyColChoice.addItemListener(this);
        add(polyColChoice);
        //------------------------

        Label rectColLabel = new Label("Rectangle Color");
        add(rectColLabel);

        rectColChoice = new Choice();
        SCSUtility.setColorMenu(rectColChoice);
        rectColChoice.select(SCSUtility.returnColorNameString(rect_col));
        rectColChoice.addItemListener(this);
        add(rectColChoice);
        //------------------------

        Label ovalColLabel = new Label("Oval Color");
        add(ovalColLabel);

        ovalColChoice = new Choice();
        SCSUtility.setColorMenu(ovalColChoice);

        ovalColChoice.select(SCSUtility.returnColorNameString(oval_col));
        ovalColChoice.addItemListener(this);
        add(ovalColChoice);

        // System.out.println("oval   col is:  " +    SCSUtility.returnColorNameString (  oval_col )  );

        //------------------------

        Label freeTextColLabel = new Label("Free Text Color");
        add(freeTextColLabel);

        freeTextColChoice = new Choice();
        SCSUtility.setColorMenu(freeTextColChoice);

        freeTextColChoice.select(SCSUtility.returnColorNameString(freeText_col));
        freeTextColChoice.addItemListener(this);
        add(freeTextColChoice);

        //System.out.println("Text col is:  " +    SCSUtility.returnColorNameString (  freeText_col )  );

        //------------------------
        Label freeTextFontLabel = new Label("Free Text Font");
        add(freeTextFontLabel);

        freeTextFontNameChoice = new Choice();

        for (String s : fontlist) {
            freeTextFontNameChoice.add(s);
        }
        freeTextFontNameChoice.select(freeTextFontName);

        //freeTextFontNameChoice.addItemListener(this);
        add(freeTextFontNameChoice);
        //*------------------------

        Label freeTextSizeLabel = new Label("Free Text Size");
        add(freeTextSizeLabel);

        freeTextSizeChoice = new Choice();
        SCSUtility.setTextSizeMenu(freeTextSizeChoice);

        freeTextSizeChoice.select(Integer.toString(freeTextSize));
        // freeTextSizeChoice.addItemListener(this);	
        add(freeTextSizeChoice);

        //------------------------
        Label moduleTextColLabel = new Label("Module Text Color");
        add(moduleTextColLabel);

        moduleTextColChoice = new Choice();
        SCSUtility.setColorMenu(moduleTextColChoice);

        moduleTextColChoice.select(SCSUtility.returnColorNameString(moduleText_col));
        moduleTextColChoice.addItemListener(this);
        add(moduleTextColChoice);

        //System.out.println("Text col is:  " +    SCSUtility.returnColorNameString (  freeText_col )  );

        //------------------------
        Label moduleTextFontLabel = new Label("Module Text Font");
        add(moduleTextFontLabel);

        moduleTextFontNameChoice = new Choice();

        for (String s : fontlist) {
            moduleTextFontNameChoice.add(s);
        }
        moduleTextFontNameChoice.select(moduleTextFontName);
        //moduleTextFontNameChoice.addItemListener(this);
        add(moduleTextFontNameChoice);

        //*------------------------

        Label moduleTextSizeLabel = new Label("Module Text Size");
        add(moduleTextSizeLabel);

        moduleTextSizeChoice = new Choice();
        SCSUtility.setTextSizeMenu(moduleTextSizeChoice);

        moduleTextSizeChoice.select(Integer.toString(moduleTextSize));
        //moduleTextSizeChoice.addItemListener(this);
        add(moduleTextSizeChoice);
        //------------------------
        Label moduleTextLocationLabel = new Label("Module Text Location");
        add(moduleTextLocationLabel);
        moduleTextLocationChoice = new Choice();
        moduleTextLocationChoice.add("BELOW");
        moduleTextLocationChoice.add("CENTER");
        moduleTextLocationChoice.add("ABOVE");
        moduleTextLocationChoice.add("RIGHT");
        moduleTextLocationChoice.add("LEFT");

        moduleTextLocationChoice.select(moduleTextLocation);
        //moduleTextLocationChoice.addItemListener(this);

        add(moduleTextLocationChoice);

        //------------------------

        Label instanceTextColLabel = new Label("Instance Text Color");
        add(instanceTextColLabel);

        instanceTextColChoice = new Choice();
        SCSUtility.setColorMenu(instanceTextColChoice);


        //System.out.println("Text col is:  " +    SCSUtility.returnColorNameString (  freeText_col )  );

        instanceTextColChoice.select(SCSUtility.returnColorNameString(instanceText_col));
        instanceTextColChoice.addItemListener(this);
        add(instanceTextColChoice);
        //------------------------
        Label instanceTextFontLabel = new Label("Instance Text Font");
        add(instanceTextFontLabel);

        instanceTextFontNameChoice = new Choice();

        for (String s : fontlist) {
            instanceTextFontNameChoice.add(s);
        }
        instanceTextFontNameChoice.select(instanceTextFontName);

        //	instanceTextFontNameChoice.addItemListener(this);
        add(instanceTextFontNameChoice);
        //*------------------------

        Label instanceTextSizeLabel = new Label("Instance Text Size");
        add(instanceTextSizeLabel);

        instanceTextSizeChoice = new Choice();
        SCSUtility.setTextSizeMenu(instanceTextSizeChoice);
        instanceTextSizeChoice.select(Integer.toString(instanceTextSize));
        //	instanceTextSizeChoice.addItemListener(this);
        add(instanceTextSizeChoice);
        //------------------------
        Label instanceTextLocationLabel = new Label("Instance Text Location");
        add(instanceTextLocationLabel);
        instanceTextLocationChoice = new Choice();
        instanceTextLocationChoice.add("BELOW");
        instanceTextLocationChoice.add("CENTER");
        instanceTextLocationChoice.add("ABOVE");
        instanceTextLocationChoice.add("RIGHT");
        instanceTextLocationChoice.add("LEFT");

        instanceTextLocationChoice.select(instanceTextLocation);
        //	instanceTextLocationChoice.addItemListener(this);
        add(instanceTextLocationChoice);

        //------------------------
        Label highlightColLabel = new Label("Highlight Color");
        add(highlightColLabel);

        highlightColChoice = new Choice();
        SCSUtility.setColorMenu(highlightColChoice);

        highlightColChoice.select(SCSUtility.returnColorNameString(highlight_col));
        highlightColChoice.addItemListener(this);
        add(highlightColChoice);

        //--********************************************************
        Button okButton = new Button("OK");
        add(okButton);
        okButton.addActionListener(this);

        Button CancelButton = new Button("Cancel");
        add(CancelButton);

        CancelButton.addActionListener(this);

    }

    //------------------------------------------------------------
    //todo: need to first do a call to UserPref.getPreferences 
    public void setOptions() {
        grid_col = Color.red;
        drawBack_col = Color.black;
        noActionTaken_col = Color.lightGray;
        inPin_col = Color.white;
        outPin_col = Color.white;
        inPortFill_col = Color.cyan; //the outline is the inPin_col
        outPortFill_col = Color.cyan; //the outline is the outPin_col
        connection_col = Color.green;
        connectionWidth = 2;
        line_col = Color.green;
        poly_col = Color.red;
        rect_col = Color.green;
        oval_col = Color.orange;
        freeText_col = Color.green;
        freeTextFontName = "Monospaced"; //aa:99/4/13
        freeTextSize = 10;//aa:99/4/13
        moduleText_col = Color.red;
        moduleTextFontName = "Monospaced";
        moduleTextSize = 10;
        moduleTextLocation = "CENTER"; //options: ABOVE, BELOW, RIGHT, LEFT of instance icon
        instanceText_col = Color.green;
        instanceTextSize = 10;//aa:99/4/13
        instanceTextFontName = "Monospaced";//aa:00/4/13
        instanceTextFont = new Font(instanceTextFontName, Font.BOLD, instanceTextSize);
        instanceTextLocation = "BELOW"; //options: CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon
        highlight_col = Color.red;
    }

    //-----------------------------------------------------------
    /* Moved to SCSUtility 5/25/00  
       public   static Color  returnCol ( String  colstring ) {
    */
    //------------------------------------------------------------
    //------------------------------------------------------------------------
    public void getAndSetOptions() {

        highlight_col = SCSUtility.returnCol(highlightColChoice.getSelectedItem());
        grid_col = SCSUtility.returnCol(gridColChoice.getSelectedItem());

        drawBack_col = SCSUtility.returnCol(backgroundColChoice.getSelectedItem());
        noActionTaken_col = SCSUtility.returnCol(noActionTakenColChoice.getSelectedItem());
        inPin_col = SCSUtility.returnCol(inPinColChoice.getSelectedItem());
        IconInport.port_col = inPin_col;
        outPin_col = SCSUtility.returnCol(outPinColChoice.getSelectedItem());
        IconOutport.port_col = outPin_col;

        inPortFill_col = SCSUtility.returnCol(inPortFillColChoice.getSelectedItem());
        outPortFill_col = SCSUtility.returnCol(outPortFillColChoice.getSelectedItem());
        connection_col = SCSUtility.returnCol(connectionColChoice.getSelectedItem());
        connectionWidth = new Integer(connectionWidthChoice.getSelectedItem());
        line_col = SCSUtility.returnCol(lineColChoice.getSelectedItem());
        poly_col = SCSUtility.returnCol(polyColChoice.getSelectedItem());
        rect_col = SCSUtility.returnCol(rectColChoice.getSelectedItem());
        oval_col = SCSUtility.returnCol(ovalColChoice.getSelectedItem());

        freeText_col = SCSUtility.returnCol(freeTextColChoice.getSelectedItem());
        freeTextFontName = freeTextFontNameChoice.getSelectedItem();

        freeTextSize = new Integer(freeTextSizeChoice.getSelectedItem());

        freeTextFont = new Font(freeTextFontName, Font.BOLD, freeTextSize);

        moduleText_col = SCSUtility.returnCol(moduleTextColChoice.getSelectedItem());
        moduleTextFontName = moduleTextFontNameChoice.getSelectedItem();
        moduleTextSize = new Integer(moduleTextSizeChoice.getSelectedItem());
        moduleTextFont = new Font(moduleTextFontName, Font.BOLD, moduleTextSize);
        moduleTextLocation = moduleTextLocationChoice.getSelectedItem();
        //"CENTER", ABOVE, BELOW, RIGHT, LEFT of instance icon

        instanceText_col = SCSUtility.returnCol(instanceTextColChoice.getSelectedItem());
        instanceTextFontName = instanceTextFontNameChoice.getSelectedItem();
        instanceTextSize = new Integer(instanceTextSizeChoice.getSelectedItem());
        instanceTextFont = new Font(instanceTextFontName, Font.BOLD, instanceTextSize);
        instanceTextLocation = instanceTextLocationChoice.getSelectedItem();
        //"BELOW", CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon


    }

    //------------------------------------------------------------
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            String cmdName = event.getActionCommand();
            if (cmdName.equals("Cancel")) {
                dispose();
            }
            if (cmdName.equals("OK")) {
                //System.out.print("OptionsFrame:Icon OK pressed");
                getAndSetOptions();
                newPreferences = true;  //todo: write out preferences

                if (parentFrame instanceof IconEditorFrame) {
                    IconPanel.setMiscColors(drawBack_col, noActionTaken_col, parentFrame.noActionTaken);
                } else if (parentFrame instanceof SchEditorFrame) {
                    //System.out.print("Debug:OptionsFrame:Sch OK pressed");
                    SchematicPanel.setMiscColors(drawBack_col, noActionTaken_col, parentFrame.noActionTaken);
                } else {
                    System.out.println("Error: OptionsFrame: unknown EditorFrame type");
                }
                // todo: go thru everything visible and set the new colors
                dispose();
            }
        }
    }

    //---------------------------------------------------------

    public void itemStateChanged(ItemEvent event) {
        Choice choice1;
        if (event.getSource() instanceof Choice) {
            choice1 = (Choice) event.getSource();
            if (event.getStateChange() == ItemEvent.SELECTED) {
                //change sample only for color choices

                //System.out.print("Debug:OptionsFrame:"+ ( String) event.getItem() ) ;

                demoboard.setColorFunc((String) event.getItem());
                demoboard.repaint();
                //if you change the backgroundCol, then change the pin color
                //when OK is pressed.
                if (choice1 == backgroundColChoice) {
                    inPin_col = SCSUtility.returnOppositeCol(backgroundColChoice.getSelectedItem());
                    IconInport.port_col = inPin_col;
                    inPinColChoice.select(SCSUtility.returnColorNameString(inPin_col));
                    outPin_col = SCSUtility.returnOppositeCol(backgroundColChoice.getSelectedItem());
                    IconOutport.port_col = outPin_col;
                    outPinColChoice.select(SCSUtility.returnColorNameString(outPin_col));
                }
            }
        }


    }
} // end class OptionsFrame













