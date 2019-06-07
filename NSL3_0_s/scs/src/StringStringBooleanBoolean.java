/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * StringStringBooleanBoolean  - A wrapper class
 * known to be used in Notepad2 and SearchDialog
 *
 * @author Alexander
 * @version %I%, %G%
 * @param mystring
 * @param myboolean
 * @since JDK8
 */

import java.lang.*;

class StringStringBooleanBoolean {
    String string1 = null;
    String string2 = null;
    boolean boolean1 = false;
    boolean boolean2 = false;

    /**
     * Constructor of this class
     */

    public StringStringBooleanBoolean() {
    }

    public StringStringBooleanBoolean(String instring1, String instring2, boolean inboolean1, boolean inboolean2) {
        string1 = instring1;
        string2 = instring2;
        boolean1 = inboolean1;
        boolean2 = inboolean2;
    }
}



