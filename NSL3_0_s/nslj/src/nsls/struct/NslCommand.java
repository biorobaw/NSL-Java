/* SCCS  %W% --- %G% -- %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

package nslj.src.nsls.struct;


import nslj.src.lang.NslHierarchy;
import nslj.src.lang.NslModule;
import nslj.src.system.NslSystem;
import tcl.lang.*;

@SuppressWarnings({"WeakerAccess", "unused", "Duplicates", "finally", "ReturnInsideFinallyBlock"})
public abstract class NslCommand implements Command {

    protected Interp nslInterp;
    TclObject[] argv;
    protected String[] nslArgv;

    protected NslSystem system;

    NslModule _model;

    public NslCommand() {
        system = Executive.system;
        _model = system.nslGetModelRef();
    }

    public NslCommand(String name, NslHierarchy module) {
        system = Executive.system;
        _model = system.nslGetModelRef();
    }

    public NslModule nslGetModule(String name) {
        if (_model != null) {
            return _model.nslGetModuleRef(name);
        }
        return null;
    }

    public void cmdProc(Interp interp, TclObject[] arg) {

        nslInterp = interp;
        argv = arg;
        nslArgv = new String[argv.length];
        for (int i = 0; i < argv.length; i++) {
            nslArgv[i] = argv[i].toString();
        }

        try {
            nslCmdProc();
        } catch (TclException e) {
            String error = "";
            int code = e.getCompletionCode();
            if (code == TCL.ERROR) {
                error = nslInterp.getResult().toString();
            }

            nslSetResult(error);
        }

    }

    public int nslInteger(String value) {

        int temp2 = 0;

        try {
            TclObject temp = TclString.newInstance(value);
            temp2 = TclInteger.get(nslInterp, temp);
        } catch (TclException e) {
            String error = "";
            int code = e.getCompletionCode();
            if (code == TCL.ERROR) {
                error = nslInterp.getResult().toString();
            }

            nslSetResult(error);
        }

        return temp2;

    }

    public boolean nslBoolean(String value) {

        boolean temp2 = false;

        try {
            TclObject temp = TclString.newInstance(value);
            temp2 = TclBoolean.get(nslInterp, temp);
        } catch (TclException e) {
            String error = "";
            int code = e.getCompletionCode();
            if (code == TCL.ERROR) {
                error = nslInterp.getResult().toString();
            }

            nslSetResult(error);
        }

        return temp2;
    }

    public double nslDouble(String value) {

        double temp2 = 0;

        try {
            TclObject temp = TclString.newInstance(value);
            temp2 = TclDouble.get(nslInterp, temp);
        } catch (TclException e) {
            String error = "";
            int code = e.getCompletionCode();
            if (code == TCL.ERROR) {
                error = nslInterp.getResult().toString();
            }

            nslSetResult(error);
        }

        return temp2;
    }

    public float nslFloat(String value) {

        double temp2 = 0;

        try {
            TclObject temp = TclString.newInstance(value);
            temp2 = TclDouble.get(nslInterp, temp);
        } catch (TclException e) {
            String error = "";
            int code = e.getCompletionCode();
            if (code == TCL.ERROR) {
                error = nslInterp.getResult().toString();
            }

            nslSetResult(error);
        }

        return (float) temp2;
    }

    public void nslCreateCommand(String name, NslCommand command) {
        nslInterp.createCommand(name, command);
    }

    public void nslSetData(String name, NslCommandData data) {
        nslInterp.setAssocData(name, data);
    }

    public Object nslGetData(String name) {
        NslCommandData nd = (NslCommandData) nslInterp.getAssocData(name);
        return nd.getData();
    }

    public void nslSetResult(String msg) {
        nslInterp.setResult(msg);
    }

    public void nslReturnError(String msg) {
        nslInterp.setResult(msg);
    }

    public int nslGetNumberOfParameters() {
        return argv.length;
    }

    public int nslGetCmdIndex(String parameter, String[] opts, String msgLabel) {
        int result = -1;
        try {
            TclObject temp = TclString.newInstance(parameter);
            result = TclIndex.get(nslInterp, temp, opts, msgLabel, 0);
        } catch (TclException ignored) {
        } finally {
            return result;
        }

    }

    public abstract void nslCmdProc() throws TclException;

}

    
