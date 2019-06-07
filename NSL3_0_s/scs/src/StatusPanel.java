/* SCCS  %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;
import java.awt.*;

class StatusPanel extends JPanel {
    protected Font StatusFont;
    protected FontMetrics StatusFontMetrics;
    protected static final int STATUSPANEL_HEIGHT = 50;
    protected static final Color BackgroundColor = Color.lightGray;
    protected static final Color TextColor = Color.black;
    protected String statusMessage = SCSUtility.blankString30;
    protected String warningMessage = SCSUtility.blankString30;

    JPanel statusPanel;
    JPanel warningPanel;
    JLabel statusLabel1;
    JLabel warningLabel1;

    JLabel statusLabel;
    JLabel warningLabel;

    /**
     * Constructor of this class.
     */
    public StatusPanel() {
        //default Flow
        setBackground(BackgroundColor);
        BoxLayout boxlay = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxlay);

        statusPanel = new JPanel();
        FlowLayout flowlay1 = new FlowLayout(FlowLayout.LEFT);
        statusPanel.setLayout(flowlay1);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel1 = new JLabel("Status:  ");
        statusPanel.add(statusLabel1);
        statusLabel = new JLabel(statusMessage);
        statusPanel.add(statusLabel);

        warningPanel = new JPanel();  //todo: this should be a scrollabel window
        FlowLayout flowlay2 = new FlowLayout(FlowLayout.LEFT);
        warningPanel.setLayout(flowlay2);
        warningPanel.setBorder(BorderFactory.createEtchedBorder());
        warningLabel1 = new JLabel("Warning: ");
        warningPanel.add(warningLabel1);
        warningLabel = new JLabel(warningMessage);
        warningPanel.add(warningLabel);

        //vertical box layout
        add(statusPanel);
        add(warningPanel);

	/*
      StatusFont = new Font("Monospaced",Font.BOLD  ,12);
      setFont(StatusFont);
      StatusFontMetrics = getFontMetrics(StatusFont);

      repaint();
	*/
    }

    /**
     * Calculate the preferred size of this panel.
     */
    /*
    public Dimension getPreferredSize() { //only if it is going to be scrollable
	//        int w = getSize().width;
	//        int h = STATUSPANEL_HEIGHT;
        return new Dimension(w, h);
    }
    */

    /**
     * Draw this status panel.
     */
    /*
    public void paint(Graphics g) {
      g.setColor(TextColor);

      g.setColor(Color.black) ;
      g.drawLine(2,2,getSize().width -2 , 2 );
      g.drawLine(2,2,2 ,  20  -2 ) ;
      g.setColor(Color.white);
      g.drawLine(2, 20  -2 ,getSize().width -2 ,    20  -2 );
      g.drawLine( getSize().width -2 ,2,  getSize().width -2 ,  20 -2 );

      g.setColor(Color.black) ;
      g.drawString(warningMessage, 4, StatusFontMetrics.getHeight() );
      
      g.setColor(Color.black) ;
      g.drawLine(2,22,getSize().width -2 , 22 );
      g.drawLine(2,22,2 , STATUSPANEL_HEIGHT -2 ) ;
      g.setColor(Color.lightGray);
      //      g.setColor(Color.white);
      g.drawLine(2, STATUSPANEL_HEIGHT -2 ,getSize().width -2 ,   STATUSPANEL_HEIGHT -2 );
      g.drawLine( getSize().width -2 ,2,  getSize().width -2 , STATUSPANEL_HEIGHT -2 );

      //      g.setColor(Color.yellow);
      g.fillRect(3,23,getSize().width -4, 18);
      g.setColor(Color.black) ;
      g.drawString(statusMessage  , 4, StatusFontMetrics.getHeight()+22);
    }
    */
    //-------------------------------------------------

    /**
     * Display the message in this status panel.
     */
    //-------------------------------------------------
    public void setStatusMessage(String msg) {
        statusMessage = msg;
        statusLabel.setText(statusMessage);
    }

    //-------------------------------------------------
    public String getStatusMessage() {
        return (statusMessage);
    }

    //-------------------------------------------------
    public void clearStatusMessage() {
        statusMessage = SCSUtility.blankString30;
        statusLabel.setText(statusMessage);
    }
    //-------------------------------------------------

    /**
     * /* clear warning message
     */
    //------------------------------------------
    public void clearWarningMessage() {
        warningMessage = SCSUtility.blankString30;
        warningLabel.setText(warningMessage);
    }

    //-------------------------------------------------

    /**
     * /* creates a sound and displays warning
     */
    //------------------------------------------
    public void setWarningMessage(String msg) {
        // creates a sound and displays warning
        //System.err.print("\07"); System.err.print("\07");

        Toolkit.getDefaultToolkit().beep();
        Toolkit.getDefaultToolkit().beep();

        System.err.flush();

        warningMessage = msg;
        warningLabel.setText(warningMessage);
    }

    //-------------------------------------------------
    public String getWarningMessage() {
        return (warningMessage);
    }


}//end StatusPanel



