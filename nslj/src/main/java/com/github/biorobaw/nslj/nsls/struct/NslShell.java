/* SCCS  @(#)NslShell.java	1.9---09/01/99--00:19:28 */

/* Copyright: 
   Copyright (c) 1998 University of Southern California Brain Project.
   This software may be freely copied provided the top level
   COPYRIGHT file is included with each such copy.
   Emai:l nsl@java.usc.edu.
*/
/**
 * @author Nikunj Mehta
 */

package com.github.biorobaw.nslj.nsls.struct;

import tcl.lang.Interp;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public class NslShell extends TextArea {
    String command;
    String currentCommand;
    Executive callback;
    String prompt = "nsls% ";
    String prompt2 = "> ";
    String temp = "";
    int beginning = prompt.length();
    int start;
    boolean mouseEnable = true;
    boolean select = false;
    NslCommandList commands;
    MouseAdapter ma;
    SelectEventHandler my;
    boolean commandExecuted = false;
    boolean executing = false;

    public NslShell(Executive e) {
        super("", 15, 45, SCROLLBARS_VERTICAL_ONLY);
        setText(prompt);
        command = "";
        currentCommand = "";
        start = beginning;
        callback = e;

        addKeyListener(new ShellEventHandler());
        addMouseListener(ma = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!mouseEnable)
                    e.consume();
            }
        });
        my = new SelectEventHandler();
        addFocusListener(new ShellEventHandler());
        commands = new NslCommandList();
    }

    int lastPos;

    public void setSelectFlag(boolean value) {
        if (value) {
            /* lastPos = getCaretPosition();*/
            removeMouseListener(ma);
            addMouseListener(my);
        } else {
            select(0, 0);
            setCaretPosition(start);
            removeMouseListener(my);
            addMouseListener(ma);
        }
        select = value;
    }

    public void paste(String text) {
        //System.out.println("Start insertion");

        char[] array = text.toCharArray();

        int i = 0, len = text.length();
        StringBuilder temp;

        while (i < len) {
            temp = new StringBuilder();
            while (i < len && array[i] != '\n') {
                temp.append(array[i]);
                i++;
            }

            append(temp.toString());

            if (i < len && array[i] == '\n') {
                dispatchEvent(new KeyEvent(this,
                        KeyEvent.KEY_PRESSED,
                        System.currentTimeMillis(),
                        InputEvent.SHIFT_DOWN_MASK |
                                InputEvent.BUTTON1_DOWN_MASK |
                                InputEvent.CTRL_DOWN_MASK |
                                InputEvent.META_DOWN_MASK,
                        KeyEvent.VK_ENTER,
                        KeyEvent.CHAR_UNDEFINED));
            }

            i++;
            if (i < len && array[i] == '\r') {
                i++;
            }
        }
    }

    public void nslPrintln(String msg) {
        // check if the last element is a prompt
        // if it is clear it an paint it at the end
        String temp = "";
        int len = getText().length();
        if (start != len) {
            temp = getText().substring(start, len);
        }
        int tempLen = temp.length();
        String command = msg + "\n" + prompt + temp;
        //if (!commandExecuted) {
        // command += temp;
        //System.out.println("Substituing "+ getText().substring(len - 6 - tempLen ,len) +
        // 		       " with " + command);
        replaceRange(command, len - 6 - tempLen, len);
        start = getText().length() - tempLen;
	/*} else {
	    append("\n"+command);
    	    System.out.println("Adding enter " + command);
	    commandExecuted = false;
	    start = getText().length();
	}*/
        setCaretPosition(start + tempLen);
    }

    public void nslPrint(String msg) {
        String temp = "";
        int len = getText().length();
        if (start != len) {
            temp = getText().substring(start, len);
        }
        int tempLen = temp.length();
        String command = msg + prompt + temp;
        //if (!commandExecuted) {
        //    command += temp;
        //    System.out.println("Substituing "+ getText().substring(len - 6 - tempLen ,len) +
        //    		       "with" + command);
        replaceRange(command, len - 6 - temp.length(), len);
        start = getText().length() - tempLen;
	/*} else {	    
	    append("\n"+command);
    	    System.out.println("Adding enter " + command);
	    commandExecuted = false;
	    start = getText().length();
	}*/
        setCaretPosition(start + tempLen);
    }

    private class SelectEventHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if ((e.getModifiersEx() & (InputEvent.BUTTON2_DOWN_MASK | InputEvent.BUTTON3_DOWN_MASK)) != 0) {
                e.consume();
            }
        }
    }

    private class ShellEventHandler extends KeyAdapter implements FocusListener {
        String result;

        public void keyPressed(KeyEvent evt) {
            if (select) {
                evt.consume();
                return;
            }

            int mod = evt.getModifiersEx();
            if (mod == InputEvent.ALT_DOWN_MASK ||
                    mod == InputEvent.CTRL_DOWN_MASK ||
                    mod == InputEvent.META_DOWN_MASK)
                evt.consume();

            int evtCode = evt.getKeyCode();
            if (evtCode == KeyEvent.VK_ENTER) {
                command = getText().substring(start);
                currentCommand = currentCommand + command;
                if (callback != null) {
                    if (Interp.commandComplete(currentCommand)) {

                        append("\n" + prompt);
                        start = getText().length();
                        setCaretPosition(start);
                        // commandExecuted=true;
                        //System.out.println("Running "+currentCommand+" = "+commandExecuted);
                        result = callback.execute_line(currentCommand);
                        if (result.length() != 0) {
                            nslPrintln(result);
                        }

                        // commandExecuted = false;

                        if (!currentCommand.equals("")) {
                            commands.add(currentCommand);
                        }
                        currentCommand = "";

                    } else {
                        append("\n" + prompt2);
                        currentCommand = currentCommand + "\n";
                    }
                    start = getText().length();
                    setCaretPosition(start);
                }

                evt.consume();
            } else if (evtCode == KeyEvent.VK_BACK_SPACE || evtCode == KeyEvent.VK_LEFT) {
                if (getCaretPosition() < start + 1)
                    evt.consume();
            } else if (evtCode == KeyEvent.VK_UP) {
                command = commands.prev();
                //setText(getText().substring(0,start).concat(command));
                int len = getText().length();
                replaceRange(command, start, len);
                setCaretPosition(start);
                evt.consume();
            } else if (evtCode == KeyEvent.VK_DOWN) {
                command = commands.next();
                setText(getText().substring(0, start).concat(command));
                setCaretPosition(start);
                evt.consume();
            } else if (evtCode == KeyEvent.VK_PAGE_UP || evtCode == KeyEvent.VK_PAGE_DOWN) {
                evt.consume();
            } else if (evtCode == KeyEvent.VK_HOME) {
                setCaretPosition(start);
                evt.consume();
            }


        }

        public void focusGained(FocusEvent e) {
            mouseEnable = false;
            setCaretPosition(start);
        }

        public void focusLost(FocusEvent e) {
            mouseEnable = true;
/*    if (!select) {
          start = getCaretPosition();
      }*/
        }
    }
}
