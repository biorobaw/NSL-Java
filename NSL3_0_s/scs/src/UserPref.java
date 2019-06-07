/* SCCS  %W%---%G%--%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import java.io.*;
import java.util.Properties;


public class UserPref {

    public String trashDir;

    public String logoImage;
    public String IconDir;
    static public String drawBackgroundColor;
    static public String noActionTakenColor;
    static public String keymapType; //Word or Emacs keystrokes

    DataInputStream in;
    //BufferedReader  in;  

    public UserPref() {

        //String homedir = System.getProperty("user.home");
        String lib_pref = SCSUtility.scs_preferences_path;

        try {
            //	  in = new DataInputStream ( new FileInputStream( new File (homedir,"SCS_PREFERENCES"  ).getAbsolutePath() )  ) ;
            //	BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream (lib_pref) )  ) ;

            in = new DataInputStream(new FileInputStream(new File(lib_pref)));
        } catch (FileNotFoundException e) {
            System.err.println("Error: UserPref.java: FileNotFoundException: " + lib_pref);
            return;
        }

        Properties scsprop = new Properties();
        try {
            scsprop.load(in);
        } catch (IOException e) {
            System.err.println("Error: UserPref.java: IOException 2: " + lib_pref);
            return;
        }

        //scsprop.list(System.out); //aa. todo: find if this is what is printing to screen

        trashDir = scsprop.getProperty("scs_trashdir");
        logoImage = scsprop.getProperty("scs_home") + File.separator + "resources" + File.separator + "scslogo.gif";
        IconDir = scsprop.getProperty("scs_home") + File.separator + "resources";
        keymapType = scsprop.getProperty("scs_keymapType", "Word"); //or Emacs

        drawBackgroundColor = scsprop.getProperty("drawBackgroundColor", "black").toUpperCase();
        noActionTakenColor = scsprop.getProperty("noActionTakenColor", "lightGray").toUpperCase();
        //todo: need to put the rest of the options here

    }
}






