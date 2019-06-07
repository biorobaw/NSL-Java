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

public class PortOptionsFrame extends Dialog
        implements ActionListener, ItemListener {

    private Font localFont;
    private String[] fontlist;
    private ColorDemo demoboard;


    Choice InputPortChoice;
    Choice OutputPortChoice;


    // constructor

    public PortOptionsFrame(Frame fm, String title, boolean type) {

        super(fm, title, true);
        setTitle("Change Port Shape Options");

        //    setLayout( new FlowLayout());
        GridLayout gl = new GridLayout(3, 2);
        this.setLayout(gl);

        gl.setHgap(10);
        gl.setVgap(5);

//------------------------
        Label InputPortLabel = new Label("Input Port Shape");
        add(InputPortLabel);

        InputPortChoice = new Choice();

        InputPortChoice.add("DEFAULT");

        add(InputPortChoice);
        //--------------------
        InputPortChoice.addItemListener(this);

        Label OutputPortLabel = new Label("Output Port Shape");

        add(OutputPortLabel);

        OutputPortChoice = new Choice();
        OutputPortChoice.add("DEFAULT");
        OutputPortChoice.add("CROSS");
        // OutputPortChoice.add("ARROW");
        add(OutputPortChoice);

        OutputPortChoice.addItemListener(this);

        //------------------------
        Button okButton = new Button("OK");
        add(okButton);
        okButton.addActionListener(this);

        Button CancelButton = new Button("Cancel");
        add(CancelButton);

        CancelButton.addActionListener(this);

    }

    public void actionPerformed(ActionEvent event) {

        Button bn;

        if (event.getSource() instanceof Button) {

            String cmdName = event.getActionCommand();

            if (cmdName.equals("OK")) {
                //System.out.print("Debug:PortOptionsFrame:OK pressed");

                IconInport.input_port_shape = InputPortChoice.getSelectedItem();
                IconOutport.output_port_shape = OutputPortChoice.getSelectedItem();

                dispose();
            }

            if (cmdName.equals("Cancel")) {

                dispose();
            }


        }
    }


    public void itemStateChanged(ItemEvent event) {

        //  if (event.getSource() instanceof Choice ) {

//       if ( event.getStateChange() == ItemEvent.SELECTED )
// 	{
// 	  System.out.print( ( String) event.getItem() ) ;

// 	  demoboard.setColorFunc( ( String) event.getItem() );

// 	  demoboard.repaint() ;
// 	}
//     }


    }


}
