/*  SCCS - %W% - %G% - %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

package nslj.src.system;


@SuppressWarnings({"WeakerAccess", "unused"})
public class NslSync {
    private boolean msgWait = false;

    public synchronized void nslWait() {
        msgWait = true;
        //System.out.println("I've been blocked");
        while (msgWait) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        //System.out.println("I've been unblocked");
    }

    public synchronized void nslNotify() {
        msgWait = false;
        notify();
    }

    public synchronized void nslNotify(boolean msg) {
        msgWait = msg;
        notify();
    }

    public synchronized void nslNotifyAll() {
        msgWait = false;
        notifyAll();
    }
}
