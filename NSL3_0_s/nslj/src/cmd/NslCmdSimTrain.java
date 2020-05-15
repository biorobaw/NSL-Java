/* SCCS  @(#)NslCmdSimTrain.java	1.2---09/01/99--00:14:45 */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

//
// NslCmdSimTrain.java
//
//////////////////////////////////////////////////////////////////////

/**
 * Run current model
 */
package cmd;

import system.NslScheduler;

import java.util.StringTokenizer;

@SuppressWarnings("Duplicates")
public class NslCmdSimTrain extends NslCmd {

    /**
     * Setup class name and help engine
     */

    public NslCmdSimTrain() {
        _name = "simTrain";
        _simple_help_string = "simTrain [endTime]";
    }

    /**
     * Print complex help on standard out
     */

    public void printHelp() {
        System.out.println("Nsl command : simTrain");
        System.out.println("usage: simTrain [endTime]");
        System.out.println("All modules would be trained until endTime");
    }

    /**
     * Start to run the model in the current context until
     * simulation end time is reached. It will recursively
     * call the child modules to run.
     */

    public void execute() {
        NslScheduler scheduler = system.getScheduler();
        scheduler.train('N');
    }

    public void execute(String time_str) {
        execute();
    }

    public void execute(StringTokenizer st) {
        if (st.hasMoreTokens()) {
            String str = st.nextToken();
            try {
                execute(Double.valueOf(str));
                return;
            } catch (NumberFormatException e) {
                System.out.println("Run cmd: Invalid argument: " + str);
                return;
            }
        }
        execute();
    }

    void execute(double endTime) {
        NslScheduler scheduler = system.getScheduler();
        scheduler.train(endTime);
    }
}
//////////////////////////////////////////////////////////////////////




