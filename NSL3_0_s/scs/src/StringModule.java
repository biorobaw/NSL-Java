/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * StringModule  - A wrapper class
 *
 * @author Alexander
 * @version %I%, %G%
 * @param instring1
 * @param currModule
 * @since JDK8
 */

import java.lang.*;

class StringModule {
    String string1;
    Module module1;

    /**
     * Constructors of this class
     */

    StringModule() {
        string1 = "";
        module1 = null;
    }

    StringModule(String instring1, Module currModule) {
        string1 = instring1;
        module1 = currModule;
    }

    StringModule(String instring1, String libNickName, String moduleName, String versionName) {
        string1 = instring1;
        module1 = new Module(libNickName, moduleName, versionName);
    }

    //----------------------------------------------------
    public boolean equals(StringModule insm) {
        if (insm == null) return false;
        if (insm.string1 == null) {
            //System.out.println("Debug:StringModule: insm.string1: null");
            return false;
        }
        if (insm.module1 == null) {
            //System.out.println("Debug:StringModule: insm.module1:null");
            return false;
        }

        if (this.string1 == null) {
            //System.out.println("Debug:StringModule: this.string1: null");
            return false;
        }

        if (this.module1 == null) {
            //System.out.println("Debug:StringModule: this.module1:null");
            return false;
        }

        //System.out.println("StringModule: equals: true");
        //System.out.println("StringModule: equals: false");
        return ((this.string1).equals(insm.string1)) &&
                ((this.module1).mostlyEquals(insm.module1));
    }
    //--------------------------

    public boolean equals(Object insm) {
        if (insm == null) return false;
        if (insm instanceof StringModule) {
            if (((StringModule) insm).string1 == null) {
                return false;
            }
            if (((StringModule) insm).module1 == null) {
                return false;
            }

            return equals((StringModule) insm);
        } else {
            return false;
        }
    }
    //--------------------------
} //end StringModule







