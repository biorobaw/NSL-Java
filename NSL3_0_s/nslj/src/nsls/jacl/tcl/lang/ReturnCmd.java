/*
 * ReturnCmd.java --
 *
 *	This file implements the Tcl "return" command.
 *
 * Copyright (c) 1997 Cornell University.
 * Copyright (c) 1997 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 * RCS: @(#) $Id: ReturnCmd.java,v 1.1.1.1 1998/10/14 21:09:19 cvsadmin Exp $
 *
 */

package tcl.lang;

/*
 * This class implements the built-in "return" command in Tcl.
 */

@SuppressWarnings("unused")
class ReturnCmd implements Command {


    /*
     *----------------------------------------------------------------------
     *
     * cmdProc --
     *
     *	This procedure is invoked as part of the Command interface to
     *	process the "return" Tcl command.  See the user documentation
     *	for details on what it does.
     *
     * Results:
     *	None.
     *
     * Side effects:
     *	See the user documentation.
     *
     *----------------------------------------------------------------------
     */

    public void
    cmdProc(
            Interp interp,        // Current interpreter.
            TclObject[] argv)        // Argument list.
            throws
            TclException        // A standard Tcl exception.
    {
        interp.errorCode = null;
        interp.errorInfo = null;
        int returnCode, i;

        /*
         * Note: returnCode is the value given by the -code option. Don't
         * confuse this value with the compCode variable of the
         * TclException thrown by this method, which is always TCL.RETURN.
         */

        returnCode = TCL.OK;
        for (i = 1; i < argv.length - 1; i += 2) {
            switch (argv[i].toString()) {
                case "-code":
                    switch (argv[i + 1].toString()) {
                        case "ok":
                            returnCode = TCL.OK;
                            break;
                        case "error":
                            returnCode = TCL.ERROR;
                            break;
                        case "return":
                            returnCode = TCL.RETURN;
                            break;
                        case "break":
                            returnCode = TCL.BREAK;
                            break;
                        case "continue":
                            returnCode = TCL.CONTINUE;
                            break;
                        default:
                            try {
                                returnCode = TclInteger.get(interp, argv[i + 1]);
                            } catch (TclException e) {
                                throw new TclException(interp,
                                        "bad completion code \"" +
                                                argv[i + 1] +
                                                "\": must be ok, error, return, break, " +
                                                "continue, or an integer");
                            }
                            break;
                    }
                    break;
                case "-errorcode":
                    interp.errorCode = argv[i + 1].toString();
                    break;
                case "-errorinfo":
                    interp.errorInfo = argv[i + 1].toString();
                    break;
                default:
                    throw new TclException(interp, "bad option \"" + argv[i] +
                            "\": must be -code, -errorcode, or -errorinfo");
            }
        }
        if (i != argv.length) {
            interp.setResult(argv[argv.length - 1]);
        }

        interp.returnCode = returnCode;
        throw new TclException(TCL.RETURN);
    }

} // end ReturnCmd

