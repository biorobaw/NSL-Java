/*
 * FconfigureCmd.java --
 *
 * Copyright (c) 1997 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 * RCS: @(#) $Id: FconfigureCmd.java,v 1.1.1.1 1998/10/14 21:09:20 cvsadmin Exp $
 *
 */

package tcl.lang;

/**
 * This class implements the built-in "fconfigure" command in Tcl.
 */

@SuppressWarnings("unused")
class FconfigureCmd implements Command {

    static final private String[] validCmds = {
            "-blocking",
            "-buffering",
            "-buffersize",
            "-eofchar",
            "-translation",
    };

    /**
     * This procedure is invoked to process the "fconfigure" Tcl command.
     * See the user documentation for details on what it does.
     *
     * @param interp the current interpreter.
     * @param argv   command arguments.
     */

    public void cmdProc(Interp interp, TclObject[] argv)
            throws TclException {

        Channel chan;        /* The channel being operated on this method */

        if ((argv.length < 2) || (((argv.length % 2) == 1) &&
                (argv.length != 3))) {
            throw new TclNumArgsException(interp, 1, argv,
                    "channelId ?optionName? ?value? ?optionName value?...");
        }

        chan = TclIO.getChannel(interp, argv[1].toString());
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \""
                    + argv[1].toString() + "\"");
        }

        //if (argv.length == 2) {
            /*
             * return list of all name/value pairs for this channelId
             */

        //}
        if (argv.length == 3) {
            /*
             * return value for supplied name
             */

            int index = TclIndex.get(interp, argv[2], validCmds,
                    "option", 0);

            switch (index) {
                case 0:
                case 2:
                case 1:
                case 3:
                case 4: {    /* -buffering, -blocking, -buffersize, -eofchar, -translation  */
                    break;
                }
                default: {
                    throw new TclRuntimeError("Fconfigure.cmdProc() error: " +
                            "incorrect index returned from TclIndex.get()");
                }
            }
        }
        for (int i = 3; i < argv.length; i += 2) {
            /*
             * Iterate through the list setting the name with the
             * corresponding value.
             */

            int index = TclIndex.get(interp, argv[i - 1], validCmds,
                    "option", 0);

            switch (index) {
                case 0:
                case 2:
                case 3: {    /* -blocking  */
                    break;
                }
                case 1: {    /* -buffering */
                    String arg = argv[i].toString();
                    switch (arg) {
                        case "full":
                        case "none":
                        case "line":
                            break;
                        default:
                            throw new TclException(interp,
                                    "bad value for -buffering: must be " +
                                            "one of full, line, or none");
                    }
                    break;
                }/* -buffersize, -eofchar */
                case 4: {    /* -translation */
                    String arg = argv[i].toString();
                    switch (arg) {
                        case "auto":
                        case "binary":
                        case "cr":
                        case "crlf":
                        case "lf":
                            break;
                        default:
                            throw new TclException(interp,
                                    "bad value for -translation: must be one " +
                                            "of auto, binary, cr, lf, crlf, or platform");
                    }
                    break;
                }
                default: {
                    throw new TclRuntimeError("Fconfigure.cmdProc() error: " +
                            "incorrect index returned from TclIndex.get()");
                }
            }
        }

        throw new TclException(interp,
                "fconfigure command not implemented yet");
    }
}
