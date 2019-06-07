/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * LibraryPathEditor - Pops up a window to edit the SCS_LIBRARY_PATHS file.
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;


class LibraryPathEditor extends Dialog
        implements ActionListener {

    TextArea textArea = new TextArea(10, 80);
    boolean done = false;
    Image image;
    String status = "none";


    String lib_path = SCSUtility.scs_library_paths_path;

    public LibraryPathEditor(Frame fm) {
        super(fm, "Warning Dialog", true);

        BorderLayout bl = new BorderLayout();


        setLayout(bl);
        add("North", textArea);


        Panel bpanel = new Panel();

        Button okButton = new Button("Cancel");
        bpanel.add(okButton);
        okButton.addActionListener(this);

        Button saveButton = new Button("Save");
        bpanel.add(saveButton);
        saveButton.addActionListener(this);

        add("South", bpanel);
    }


    public void mydisplay() {

        setSize(750, 270);
        setTitle("SCS_LIBRARY_PATHS file");
        dummypaint();
        setVisible(true);
        //   requestFocus();
        //   setForegroundColor(Color.black);


    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        //    dummypaint();
    }


    public void dummypaint() {

        String line;
        String testdir = "";

        System.out.println("Debug:LibraryPathEditor: Your Library " + SCSUtility.scs_library_paths_file + " should be located in: " + SCSUtility.user_home);

        try {
            //	DataInputStream in = new DataInputStream ( new FileInputStream( new File (SCSUtility.user_home,"SCS_LIBRARY_PATHS"  ).getAbsolutePath() )  ) ;
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(lib_path)));

            while (!done) {
                line = in.readLine();

                if (line == null)
                    done = true;
                    //	    System.out.println("Debug:LibraryPathEditor:line: "+line);
                else
                //	    g.drawString(line , 50 , 50 );
                {
                    textArea.append(line + "\n");
                    //System.out.println("Debug:LibraryPathEditor:line: "+line);

                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error:LibraryPathEditor:dummyPaint:FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:LibraryPathEditor:dummyPaint: IOException");
        }
        //      System.out.println("Debug:LibraryPathEditor: Could not locate  module  file" + modulename + ".sif");

    }
//--------------------------------------------------------

    /**
     * Perform functions according to action events.
     */
    public void actionPerformed(ActionEvent event) {
        Button btn;

        if (event.getSource() instanceof Button) {
            btn = (Button) event.getSource();

            if (btn.getLabel().equals("Cancel")) {
                status = "Cancel";
                dispose();
                return;
            }


            //--------------------------

            if (btn.getLabel().equals("Save")) {

                try {
                    String temp55 = SCSUtility.user_home + File.separator + SCSUtility.scs_library_paths_file;
                    File file55 = new File(temp55);
                    String temp66 = file55.getAbsolutePath();
                    FileOutputStream fout = new FileOutputStream(temp66);

                    PrintStream pout = new PrintStream(fout);
                    //System.out.println("Debug:LPE:Save clicked ");

                    pout.print(textArea.getText());
                    pout.close();

                    //System.out.println("Debug:LibraryPathEditor:File saved: "+temp66);
                    dispose();

                } catch (FileNotFoundException e) {
                    System.err.println("Error:LibraryPathEditor:Open Icon: FileNotFoundException");
                    dispose();
                }

            }
        }
    }

    /**
     * Handle window events.
     */
    class DWAdapter extends WindowAdapter {
        /**
         * Handle windowClosing event.
         */
        public void windowClosing(WindowEvent event) {
            dispose();
        }
    }
}
