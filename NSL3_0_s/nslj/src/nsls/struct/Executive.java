/* SCCS  @(#)Executive.java	1.14---09/01/99--00:19:27 */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// $Log: Executive.java,v $
// Revision 1.2  1998/02/03 00:23:50  erhan
// adapted new nsls structure
//

package nsls.struct;

//aa note: no nsls.jacl here because
// the cornell files expect cornell to be first
// and we did not want to remake them

// We will no use these anymore
// import cornell.Jacl.*;
// import cornell.Tcl.*;

import system.NslInterpreter;
import system.NslSystem;
import tcl.lang.Interp;
import tcl.lang.TCL;
import tcl.lang.TclException;

@SuppressWarnings({"WeakerAccess", "finally", "ReturnInsideFinallyBlock"})
public class Executive {

    static public NslInterpreter interpreter;
    static public NslSystem system;
    static public Interp interp;
    static String error;
    static public boolean readingFile = false;
    static public boolean initMade = false;

    public Executive(NslSystem s, NslInterpreter i) {

        system = s;
        interpreter = i;

        interp = new Interp();
        interp.createCommand("nsl", new s_Nsl());

        error = "";
    }

    public String execute_line(String in_script) {

        error = "";

        if (in_script.equals("nsl exit") || in_script.equals("nsl quit"))
            System.exit(0);
        try {
            interp.eval(in_script);
            error = interp.getResult().toString();
        } catch (TclException e) {
            int code = e.getCompletionCode();

            if (code == TCL.ERROR) {
                error = interp.getResult().toString();
            }
        } finally {
            return error;
        }
    }

    static public void sourceFile(String fileName) {
        try {
            readingFile = true;
            interp.evalFile(fileName);
            readingFile = false;
        } catch (TclException e) {
            int code = e.getCompletionCode();

            if (code == TCL.ERROR) {
                error = interp.getResult().toString();
                System.out.println(error);
            }
        }
    }
}
