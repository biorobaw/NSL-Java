/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * StringBoolean  - A wrapper class
 * known to be used in Notepad2 and SearchDialog
 *
 * @author Alexander
 * @version %I%, %G%
 * @param mystring
 * @param myboolean
 * @since JDK8
 */

import java.lang.*;

class StringBoolean {
    String mystring = null;
    boolean myboolean = false;

    /**
     * Constructor of this class
     */

    public StringBoolean() {
    }

    public StringBoolean(String instring, boolean inboolean) {
        mystring = instring;
        myboolean = inboolean;
    }
}
