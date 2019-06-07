/* Sccs %W% --- %G% --%U% */
/*
 * @(#)Notepad2.java	1.16 99/09/23
 *
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */


import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Sample application using the simple text editor component that
 * supports only one font.
 *
 * @author Timothy Prinzing
 * @author Amanda Alexander
 * @version Notepad 2.1
 */

@SuppressWarnings("Duplicates")
class Notepad2 extends JPanel {

    static final int notepadHeight = 50;
    static final int notepadWidth = 60;

    JFrame parentFrame;
    private static ResourceBundle resources;

    // for the find/search utilities
    public SearchDialog mySearchDialog;
    String lastFindStr = "";
    int lastFindIndex = -1;
    int lastLastFindIndex = -1;
    boolean forwardFindDirection = true;

    // for the replace utilities
    public ReplaceDialog myReplaceDialog;
    String lastExistingString = "";
    String lastReplacementString = "";
    int lastReplaceIndex = -1;
    int lastLastReplaceIndex = -1;
    boolean forwardReplaceDirection = true;
    boolean globalReplace = false;

    static {
        try {
            resources = ResourceBundle.getBundle("resources.Notepad2",
                    Locale.getDefault());
        } catch (MissingResourceException mre) {
            System.err.println("Notepad2:resources/Notepad2.properties not found");
            System.exit(1);
        }
    }

    //-------------------------------------------------------------------------------
    Notepad2(JFrame inparentFrame) {
        super(true); //is double buffered
        parentFrame = inparentFrame;
        warningPopup = new WarningDialog(parentFrame);
        okCancelWarningPopup = new WarningDialogOkCancel(parentFrame);
        messagePopup = new MessageDialog(parentFrame);
        okCancelMessagePopup = new MessageDialogOkCancel(parentFrame);


        // Force SwingSet to come up in the Cross Platform L&F
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Notepad2:Error loading L&F: " + exc);
        }

        Border etched = BorderFactory.createEtchedBorder();
        Border title = BorderFactory.createTitledBorder(etched, "Methods");
        setBorder(title);
        setLayout(new BorderLayout());

        // create the embedded JTextComponent
        editor1 = createEditor();
        editor1.setFont(new Font("monospaced", Font.PLAIN, 12));
        // aa -added next line
        setPlainDocument((PlainDocument) editor1.getDocument()); //sets doc1

        // install the command table
        commands = new Hashtable<>();
        Action[] actions = getActions();
        for (Action a : actions) {
            commands.put(a.getValue(Action.NAME), a);
            //System.out.println("Debug:Notepad2: actionName:"+a.getValue(Action.NAME));
        }
        //editor1.setPreferredSize(new Dimension(,));
        //get setting from user preferences
        if (UserPref.keymapType.equals("Word")) {
            editor1 = updateKeymapForWord(editor1);
        } else {
            editor1 = updateKeymapForEmacs(editor1);
        }

        scroller1 = new JScrollPane();
        viewport1 = scroller1.getViewport();
        viewport1.add(editor1);
        scroller1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        try {
            viewport1.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        } catch (MissingResourceException mre) {
            System.err.println("Notepad2:missing resource:" + mre.getMessage());
            // just use the viewport1 default
        }

        menuItems = new Hashtable<>();
        //JLabel notepadLabel=new JLabel("Methods");
        //add("North", notepadLabel);
        menubar = createMenubar();
        add("North", menubar);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add("North", createToolbar());
        panel.add("Center", scroller1);
        add("Center", panel);
        add("South", createStatusbar());


        //for the find/search utilities
        mySearchDialog = new SearchDialog(parentFrame);

        //for the replace utilities
        myReplaceDialog = new ReplaceDialog(parentFrame);

    }
    //--------------------------------------------------------------------------
    // For testing purposes only
    /*
      public static void main(String[] args) {
      try {
      String vers = System.getProperty("java.version");
      if (vers.compareTo("1.1.2") < 0) {
      System.err.println("Notepad2:!!!WARNING: Swing must be run with a " +
      "1.1.2 or higher version VM!!!");
      }
      JFrame frame = new JFrame();
      frame.setTitle(resources.getString("Title"));
      frame.setBackground(Color.lightGray);
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add("Center", new Notepad2());
      frame.addWindowListener(new AppCloser());
      frame.pack();
      frame.setSize(500, 600);
      frame.show();
      } catch (Throwable t) {
      System.err.println("Notepad2:uncaught exception: " + t);
      t.printStackTrace();
      }
      }
    */
    //--------------------------------------------------------------------------

    /**
     * Fetch the list of actions supported by this
     * editor.  It is implemented to return the list
     * of actions supported by the embedded JTextComponent
     * augmented with the actions defined locally.
     */
    public Action[] getActions() {
        return TextAction.augmentList(editor1.getActions(), defaultActions);
    }

    /**
     * Create an editor1 to represent the given document.
     */
    protected JTextComponent createEditor() {
        JTextArea ed;
        ed = new JTextArea(notepadHeight, notepadWidth);
        ed.setCaretPosition(0); //aa
        return (ed);
    }

    /**
     * Fetch the editor1 contained in this panel
     */
    //protected JTextComponent getEditor() {
    public JTextComponent getEditor() {
        return editor1;
    }

    /**
     * set the document that this editor points to
     */
    public void setPlainDocument(PlainDocument doc) {
        doc1 = doc;
        // Add this as a listener for undoable edits.
        doc1.addUndoableEditListener(undoHandler);
        editor1.setDocument(doc1);
    }

    /**
     * To shutdown when run as an application.  This is a
     * fairly lame implementation.   A more self-respecting
     * implementation would at least check to see if a save
     * was needed.
     */
    protected static final class AppCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    /**
     * Find the hosting frame, for the file-chooser dialog.
     */
    protected Frame getFrame() {
        for (Container p = getParent(); p != null; p = p.getParent()) {
            if (p instanceof Frame) {
                return (Frame) p;
            }
        }
        return null;
    }

    /**
     * This is the hook through which all menu items are
     * created.  It registers the result with the menuitem
     * hashtable so that it can be fetched with getMenuItem().
     *
     * @see #getMenuItem
     */
    protected JMenuItem createMenuItem(String cmd) {
        JMenuItem mi = new JMenuItem(getResourceString(cmd + labelSuffix));
        URL url = getResource(cmd + imageSuffix);
        if (url != null) {
            mi.setHorizontalTextPosition(JButton.RIGHT);
            mi.setIcon(new ImageIcon(url));
        }
        String astr = getResourceString(cmd + actionSuffix);
        if (astr == null) {
            astr = cmd;
        }
        mi.setActionCommand(astr);
        Action myaction = getAction(astr); //if this is a known action
        if (myaction != null) {
            mi.addActionListener(myaction);
            myaction.addPropertyChangeListener(createActionChangeListener(mi));
            //System.out.println("myaction not null: astr:"+astr+" enabled:"+myaction.isEnabled());
            mi.setEnabled(myaction.isEnabled());
        } else {
            System.err.println("Error:Notepad2: createMenuItem: myaction is null: astr:" + astr);
            //causes the item to be greyed out
            mi.setEnabled(false);
        }
        menuItems.put(cmd, mi);
        return mi;
    }

    /**
     * Fetch the menu item that was created for the given
     * command.
     *
     * @param cmd Name of the action.
     * @returns item created for the given command or null
     * if one wasn't created.
     */
    protected JMenuItem getMenuItem(String cmd) {
        return menuItems.get(cmd);
    }

    protected Action getAction(String cmd) {
        return commands.get(cmd); //commands is Hashtable
    }

    protected String getResourceString(String nm) {
        String str;
        try {
            str = resources.getString(nm);
        } catch (MissingResourceException mre) {
            str = null;
        }
        return str;
    }

    protected URL getResource(String key) {
        String name = getResourceString(key);
        if (name != null) {
            return this.getClass().getResource(name);
        }
        return null;
    }

    protected Container getToolbar() {
        return toolbar;
    }

    protected JMenuBar getMenubar() {
        return menubar;
    }

    /**
     * Create a status bar
     */
    protected Component createStatusbar() {
        // need to do something reasonable here
        status = new StatusBar();
        return status;
    }

    /**
     * Resets the undo manager.
     */
    protected void resetUndoManager() {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    /**
     * Create the toolbar.  By default this reads the
     * resource file for the definition of the toolbar.
     */
    private Component createToolbar() {
        toolbar = new JToolBar();
        String[] toolKeys = SCSUtility.tokenize(getResourceString("toolbar"));
        for (String toolKey : toolKeys) {
            if (toolKey.equals("-")) {
                toolbar.add(Box.createHorizontalStrut(5));
            } else {
                toolbar.add(createTool(toolKey));
            }
        }
        toolbar.add(Box.createHorizontalGlue());
        return toolbar;
    }

    /**
     * Hook through which every toolbar item is created.
     */
    protected Component createTool(String key) {
        return createToolbarButton(key);
    }
    //------------------------------------------------------------

    /**
     * Create a button to go inside of the toolbar.  By default this
     * will load an image resource.  The image filename is relative to
     * the classpath (including the '.' directory if its a part of the
     * classpath), and may either be in a JAR file or a separate file.
     *
     * @param key The key in the resource file to serve as the basis
     *            of lookups.
     */
    protected JButton createToolbarButton(String key) {
        URL url = getResource(key + imageSuffix);
        JButton b = new JButton(new ImageIcon(url)) {
            public float getAlignmentY() {
                return 0.5f;
            }
        };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1, 1, 1, 1));

        String astr = getResourceString(key + actionSuffix);
        if (astr == null) {
            astr = key;
        }
        Action a = getAction(astr);
        if (a != null) {
            b.setActionCommand(astr);
            b.addActionListener(a);
        } else {
            b.setEnabled(false);
        }

        String tip = getResourceString(key + tipSuffix);
        if (tip != null) {
            b.setToolTipText(tip);
        }

        return b;
    }
    //------------------------------------------------------------
    /**
     * Take the given string and chop it up into a series
     * of strings on whitespace boundaries.  This is useful
     * for trying to get an array of strings out of the
     * resource file.
     */
    /*
    protected String[] tokenize(String input) {
	Vector v = new Vector();
	StringTokenizer t = new StringTokenizer(input);
	String cmd[];

	while (t.hasMoreTokens())
	    v.addElement(t.nextToken());
	cmd = new String[v.size()];
	for (int i = 0; i < cmd.length; i++)
	    cmd[i] = (String) v.elementAt(i);

	return cmd;
    }
    */
    //------------------------------------------------------------

    /**
     * Create the menubar for the app.  By default this pulls the
     * definition of the menu from the associated resource file.
     */
    protected JMenuBar createMenubar() {
        JMenuItem mi;
        JMenuBar mb = new JMenuBar();

        String[] menuKeys = SCSUtility.tokenize(getResourceString("menubar"));
        //System.out.println("Notepad2:menuKeys.length "+menuKeys.length);
        for (String menuKey : menuKeys) {
            JMenu m = createMenu(menuKey);
            if (m != null) {
                mb.add(m);
            }
        }
        return mb;
    }
    //------------------------------------------------------------

    /**
     * Create a menu for the app.  By default this pulls the
     * definition of the menu from the associated resource file.
     */
    protected JMenu createMenu(String key) {
        String[] itemKeys = SCSUtility.tokenize(getResourceString(key));
        JMenu menu = new JMenu(getResourceString(key + "Label"));
        for (String itemKey : itemKeys) {
            if (itemKey.equals("-")) {
                menu.addSeparator();
            } else {
                //System.out.println("Debug:Notepad2:itemkey: "+itemKeys[i]);
                JMenuItem mi = createMenuItem(itemKey);
                menu.add(mi);
            }
        }
        return menu;
    }

    //------------------------------------------------------------------
    // Yarked from JMenu, ideally this would be public.
    protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
        return new ActionChangedListener(b);
    }

    // Yarked from JMenu, ideally this would be public.
    private class ActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }

        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if (propertyName.equals("enabled")) {
                //System.out.println("Debug:Notepad2: ActionChangedListener enabled");
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState);
            }
        }
    }

    //------------------------------------------------------------------
    //from Java Swing 1.2 Orielly - Robert Eckstein
    //------------------------------------------------------------------
    protected JTextComponent updateKeymapForWord(JTextComponent textComp) {
        //create a new child keymap
        Keymap map = JTextComponent.addKeymap("NslmMap", textComp.getKeymap());

        //define the keystrokeds to be added
        KeyStroke next = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK, false);
        //add the new mappings used DefaultEditorKit actions
        map.addActionForKeyStroke(next, getAction(DefaultEditorKit.nextWordAction));

        KeyStroke prev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(prev, getAction(DefaultEditorKit.previousWordAction));

        KeyStroke selNext = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selNext, getAction(DefaultEditorKit.selectionNextWordAction));
        KeyStroke selPrev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selPrev, getAction(DefaultEditorKit.selectionPreviousWordAction));

        KeyStroke find = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(find, getAction("find"));

        KeyStroke findAgain = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(findAgain, getAction("findAgain"));


        //set the keymap for the text component
        textComp.setKeymap(map);
        return (textComp);
    }//end updateKeymapForWord

    //------------------------------------------------------------------
    //from Java Swing 1.2 Orielly - Robert Eckstein
    //------------------------------------------------------------------
    protected JTextComponent updateKeymapForEmacs(JTextComponent textComp) {
        //note: it does not look like a key can do more than one action
        // thus no modes.
        //todo: not all of these are correct. such as ctrlK
        //todo: add saving - ctrlXS

        //create a new child keymap
        Keymap map = JTextComponent.addKeymap("NslmMap", textComp.getKeymap());


        KeyStroke selNext = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selNext, getAction(DefaultEditorKit.selectionNextWordAction));

        KeyStroke selPrev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selPrev, getAction(DefaultEditorKit.selectionPreviousWordAction));

        KeyStroke next = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(next, getAction(DefaultEditorKit.forwardAction));
        KeyStroke prev = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(prev, getAction(DefaultEditorKit.backwardAction));

        KeyStroke selectionDown = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selectionDown, getAction(DefaultEditorKit.downAction));
        KeyStroke selectionUp = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selectionUp, getAction(DefaultEditorKit.upAction));

        KeyStroke pageDown = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(pageDown, getAction(DefaultEditorKit.pageDownAction));

        KeyStroke pageUp = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(pageUp, getAction(DefaultEditorKit.pageUpAction));

        KeyStroke endDoc = KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, InputEvent.META_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(endDoc, getAction(DefaultEditorKit.endAction));
        KeyStroke beginingDoc = KeyStroke.getKeyStroke(KeyEvent.VK_LESS, InputEvent.META_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(beginingDoc, getAction(DefaultEditorKit.beginAction));

        // the VK_SPACE and VK_W not working as in Emacs - space deleting
        //KeyStroke selectionStart=KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,InputEvent.CTRL_MASK,false);
        //map.addActionForKeyStroke(selectionStart,getAction(DefaultEditorKit.selectionForwardAction)); //todo: setCharPosAction
        //this is doing nothing because only one char to can be assigned to cut
        //KeyStroke cut1=KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_MASK,false);
        //map.addActionForKeyStroke(cut1,getAction(DefaultEditorKit.cutAction));

        //if we do save as XS, this will have to change
        KeyStroke cut = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(cut, getAction(DefaultEditorKit.cutAction));

        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(paste, getAction(DefaultEditorKit.pasteAction));

        KeyStroke moveToEndLine = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(moveToEndLine, getAction(DefaultEditorKit.endLineAction));

        //not emacs like
        KeyStroke selWord = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selWord, getAction(DefaultEditorKit.selectWordAction));

        KeyStroke selLine = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selLine, getAction(DefaultEditorKit.selectLineAction));

        KeyStroke delNext = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(delNext, getAction(DefaultEditorKit.deleteNextCharAction));

        KeyStroke insertLine = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(insertLine, getAction(DefaultEditorKit.insertBreakAction));

        KeyStroke searchBackward = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(searchBackward, getAction("findAgain"));

        KeyStroke searchForward = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(searchForward, getAction("findAgain"));

        //set the keymap for the text component
        textComp.setKeymap(map);
        return (textComp);
    }//end updateKeymapForEmacs

    //------------------------------------------------------------------
    // Declarations
    //---------------------------------------------------------------
    public JTextComponent editor1; //actually a JTextArea
    //private JTextComponent editor1;
    public PlainDocument doc1; //aa - added - changed from Document to PlainDocument
    public JScrollPane scroller1;//aa - added
    public JViewport viewport1;//aa - added


    private Hashtable<Object, Action> commands;
    private Hashtable<String, JMenuItem> menuItems;
    private JMenuBar menubar;
    private JToolBar toolbar;
    private JComponent status;
    //aa    private JFrame elementTreeFrame;
    //aa    protected ElementTreePanel elementTreePanel;

    protected FileDialog fileDialog;
    protected WarningDialog warningPopup;
    protected WarningDialogOkCancel okCancelWarningPopup;
    protected MessageDialog messagePopup;
    protected MessageDialogOkCancel okCancelMessagePopup;

    //-------------------------------------------------------------------------
    /**
     * Listener for the edits on the current document.
     */
    public UndoableEditListener undoHandler = new UndoHandler();
    //protected UndoableEditListener undoHandler = new UndoHandler();

    /**
     * UndoManager that we add edits to.
     */
    protected UndoManager undo = new UndoManager();

    /**
     * Suffix applied to the key used in resource file
     * lookups for an image.
     */
    public static final String imageSuffix = "Image";

    /**
     * Suffix applied to the key used in resource file
     * lookups for a label.
     */
    public static final String labelSuffix = "Label";

    /**
     * Suffix applied to the key used in resource file
     * lookups for an action.
     */
    public static final String actionSuffix = "Action";

    /**
     * Suffix applied to the key used in resource file
     * lookups for tooltip text.
     */
    public static final String tipSuffix = "Tooltip";

    public static final String openAction = "open";
    public static final String newAction = "new";
    public static final String saveAction = "save";
    public static final String exitAction = "exit";
    //    public static final String showElementTreeAction = "showElementTree";

    //--------
    class UndoHandler implements UndoableEditListener {

        /**
         * Messaged when the Document has created an edit, the edit is
         * added to <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    /**
     * FIXME - I'm not very useful yet
     */
    class StatusBar extends JComponent {

        public StatusBar() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        public void paint(Graphics g) {
            super.paint(g);
        }

    }

    // --- action supporting methods ----------------------------

    /**
     * Thread to load a file into the text storage model
     */
    class FileLoader extends Thread {

        FileLoader(File f, Document doc) {
            setPriority(4);
            this.f = f;
            this.doc = doc;
        }

        public void run() {
            try {
                // initialize the statusbar
                status.removeAll();
                JProgressBar progress = new JProgressBar();
                progress.setMinimum(0);
                progress.setMaximum((int) f.length());
                status.add(progress);
                status.revalidate();

                // try to start reading
                Reader in = new FileReader(f);
                char[] buff = new char[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
                    progress.setValue(progress.getValue() + nch);
                }

                // we are done... get rid of progressbar
                doc.addUndoableEditListener(undoHandler);
                status.removeAll();
                status.revalidate();

                resetUndoManager();
            } catch (IOException e) {
                System.err.println("Notepad2:FileLoader " + e.toString());
            } catch (BadLocationException e) {
                System.err.println("Notepad2:FileLoader " + e.getMessage());
            }
	    /* aa
	       if (elementTreePanel != null) {
	       SwingUtilities.invokeLater(new Runnable() {
	       public void run() {
	       elementTreePanel.setEditor(getEditor());
	       }
	       });
	       }
	    */
        }

        Document doc;
        File f;
    }
    // action implementations ----------------------------------

    private UndoAction undoAction = new UndoAction();
    private RedoAction redoAction = new RedoAction();

    /**
     * Actions defined by the Notepad2 class
     */
    /*
      private Action[] defaultActions = {
      new NewAction(),
      new OpenAction(),
      new ExitAction(),
      new ShowElementTreeAction(),
      undoAction,
      redoAction
      };
    */
    // first three for testing purposes only
    private Action[] defaultActions = {
            undoAction,
            redoAction,
            new FindAction(),
            new FindAgainAction(),
            new ReplaceAction(),
            new ReplaceAgainAction(),
            new InitSysAction(),
            new CallFromConstructorTopAction(),
            new CallFromConstructorBottomAction(),
            new MakeConnAction(),
            new InitModuleAction(),
            new InitTrainEpochsAction(),
            new InitTrainAction(),
            new SimTrainAction(),
            new EndTrainAction(),
            new EndTrainEpochsAction(),
            new InitRunEpochsAction(),
            new InitRunAction(),
            new SimRunAction(),
            new EndRunAction(),
            new EndRunEpochsAction(),
            new EndModuleAction(),
            new EndSysAction()
    };

    //-------------------------------------------------------------
    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.err.println("Notepad2:Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.err.println("Notepad2:Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    class OpenAction extends NewAction {

        OpenAction() {
            super(openAction);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            if (fileDialog == null) {
                fileDialog = new FileDialog(frame);
            }
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.setVisible(true);

            String file = fileDialog.getFile();
            if (file == null) {
                return;
            }
            String directory = fileDialog.getDirectory();
            File f = new File(directory, file);
            if (f.exists()) {
                Document oldDoc = getEditor().getDocument();
                if (oldDoc != null)
                    oldDoc.removeUndoableEditListener(undoHandler);
		/*
		  if (elementTreePanel != null) {
		  elementTreePanel.setEditor(null);
		  }
		*/
                getEditor().setDocument(new PlainDocument()); //aa - i did not do this
                frame.setTitle(file);
                Thread loader = new FileLoader(f, editor1.getDocument());
                loader.start();
            }
        }
    }

    //----------------------------------------------------------
    class NewAction extends AbstractAction {

        NewAction() {
            super(newAction);
            setEnabled(true);
        }

        NewAction(String nm) {
            super(nm);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            Document oldDoc = getEditor().getDocument();
            if (oldDoc != null)
                oldDoc.removeUndoableEditListener(undoHandler);
            getEditor().setDocument(new PlainDocument()); //aa - PlainDocument
            getEditor().getDocument().addUndoableEditListener(undoHandler);
            resetUndoManager();
            revalidate();
        }
    }
    //---------------------------------------------------

    /**
     * Really lame implementation of an exit command
     */
    class ExitAction extends AbstractAction {

        ExitAction() {
            super(exitAction);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * Action that brings up a JFrame with a JTree showing the structure
     * of the document.
     */
    /*  aa - removed
	class ShowElementTreeAction extends AbstractAction {

	ShowElementTreeAction() {
	super(showElementTreeAction);
	}

	ShowElementTreeAction(String nm) {
	super(nm);
	}

        public void actionPerformed(ActionEvent e) {
	if(elementTreeFrame == null) {
	// Create a frame containing an instance of 
	// ElementTreePanel.
	try {
	String    title = resources.getString
	("ElementTreeFrameTitle");
	elementTreeFrame = new JFrame(title);
	} catch (MissingResourceException mre) {
	elementTreeFrame = new JFrame();
	}

	elementTreeFrame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent weeee) {
	elementTreeFrame.setVisible(false);
	}
	});
	Container fContentPane = elementTreeFrame.getContentPane();

	fContentPane.setLayout(new BorderLayout());
	elementTreePanel = new ElementTreePanel(getEditor());
	fContentPane.add(elementTreePanel);
	elementTreeFrame.pack();
	}
	elementTreeFrame.show();
	}
	}
    */
    //----------------------------------------------------
    class FindAction extends AbstractAction {
        String name;
        boolean isFindAgain = false;

        FindAction() {  //only called by findAction
            super("find");
            setEnabled(true);
        }

        FindAction(String nm) { //called by findAgainAction
            super(nm);
            name = nm;
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event) {
            //Frame parentFrame = getFrame();
            JMenuItem mi;
            String label;

            if (warningPopup == null) {
                warningPopup = new WarningDialog(parentFrame);
            }
            if ((editor1 == null) || (doc1 == null)) {
                String errstr = "Notepad2:editor1 or document is null";
                warningPopup.display(errstr);
                return;
            }
            String actionStr = event.getActionCommand(); //is not makeing anysense
            //when keystrokes typed
            if ((event.getSource() instanceof JMenuItem)) {
                mi = (JMenuItem) event.getSource();
                label = mi.getText();
            } else if ((event.getSource() instanceof JTextArea)) {  //keystroke
                label = "FindAgain"; //just set it to findagain
            } else {
                System.err.println("Debug:Notepad2:" + actionStr);
                System.err.println("Debug:Notepad2:" + event.getSource().toString());
                String errstr = "Notepad2:FindAction: " + event.getSource().toString() + " not an instance of JMenuItem or JTextArea";
                warningPopup.display(errstr);
                return;
            }

            if (label.equals("FindAgain")) {
                isFindAgain = true;
            } else {
                isFindAgain = false;
                lastFindStr = "";
            }
            StringBoolean content = new StringBoolean(lastFindStr, forwardFindDirection);

            boolean okPressed = mySearchDialog.display(content);
            if (!okPressed) {
                return;
            }
            lastFindStr = content.mystring;
            forwardFindDirection = content.myboolean;

            if (forwardFindDirection) {
                lastFindIndex = searchForward(lastFindStr);

            } else {
                lastFindIndex = searchBackward(lastFindStr);
            }
        }

        //----------------------
        //return last location found
        public int searchForward(String lastFindStr) {
            try {  //forward
                //System.out.println("Debug:Notepad2:in try");
                int carPos = editor1.getCaretPosition();
                //System.out.println("Debug:Notepad2: carPos "+carPos);
                //int strLength=doc1.getEndPosition().getOffset();
                int strLength = doc1.getLength();
                //System.out.println("Debug:Notepad2: strLength "+strLength);
                //search Forward
                //todo: should we use the getText(pos,len,segment);
                String chunk1 = doc1.getText(carPos, (strLength - carPos)); //offset,length

                lastFindIndex = chunk1.indexOf(lastFindStr);
                //System.out.println("Debug:Notepad2: lastFindIndex "+lastFindIndex);
                if (lastFindIndex == -1) {
                    boolean okPressed = okCancelWarningPopup.display("Notepad2:string not found. Try backward?");
                    //handle backward
                    if (okPressed) {
                        forwardFindDirection = false;
                        lastFindIndex = searchBackward(lastFindStr);
                    }
                } else {
                    lastFindIndex = lastFindIndex + carPos;
                    lastLastFindIndex = lastFindIndex;
                    editor1.setCaretPosition(lastFindIndex); //ready to type in body
                    editor1.moveCaretPosition(lastFindIndex + lastFindStr.length()); //ready to type in body

                }
            } catch (BadLocationException badexception) {
                String errstr = "Notepad2:searchForward: " + badexception.getMessage();
                warningPopup.display(errstr);
                return (-1);
            }
            return (lastFindIndex);
        }

        //----------------------------------------------------
        //return last location found
        public int searchBackward(String lastFindStr) {
            try {  //backward
                if (isFindAgain) {//otherwise you find same string
                    int foo = lastLastFindIndex; //beginning of word
                    if (foo >= 0) {
                        editor1.setCaretPosition(foo);
                    }
                    System.out.println("Debug:Notepad2:searchBackward: foo: " + foo);
                }

                int carPos = editor1.getCaretPosition();
                //int strLength=doc1.getLength();
                //search backward
                //todo: should we use the getText(pos,len,segment);
                String chunk1 = doc1.getText(0, carPos);
                lastFindIndex = chunk1.lastIndexOf(lastFindStr);
                if (lastFindIndex == -1) {
                    boolean okPressed = okCancelWarningPopup.display("Notepad2:string not found. Try forward?");
                    //handle forward
                    if (okPressed) {
                        forwardFindDirection = true;
                        lastFindIndex = searchForward(lastFindStr);
                    }

                } else {
                    lastLastFindIndex = lastFindIndex;
                    editor1.setCaretPosition(lastFindIndex); //ready to type in body
                    editor1.moveCaretPosition(lastFindIndex + lastFindStr.length()); //ready to type in body

                }
            } catch (BadLocationException badexception) {
                String errstr = "Notepad2:searchBackward: " + badexception.getMessage();
                warningPopup.display(errstr);
                return (-1);
            }
            return (lastFindIndex);
        }

        //------------------------------
        public void scrollToCaret() { //not called - fixed with putting visible scrollbars on JScrollPane
            //
            Rectangle rect1 = scroller1.getViewport().getViewRect();
            double x1 = rect1.getX();
            double y1 = rect1.getY();
            double r1height = rect1.getHeight();
            double r1width = rect1.getWidth();
            Caret caret1 = editor1.getCaret();
            Point pt2 = caret1.getMagicCaretPosition(); //the end of the string
            double x2 = pt2.getX();
            double y2 = pt2.getY();
            if (((!(x2 > x1)) || (!(x2 < (x1 + r1width)))) || ((!(y2 > y1)) || (!(y2 < (y1 + r1height))))) {
                double newheight = r1height / 2;
                double newwidth = r1width / 2;
                double x3 = pt2.getX() - newwidth;
                double y3 = pt2.getY() - newheight;
                if (x3 < 0) x3 = 0;
                if (y3 < 0) y3 = 0;
                Rectangle rect3 = new Rectangle((int) x3, (int) y3, (int) newwidth, (int) newheight);
                editor1.scrollRectToVisible(rect3);
            }  //inview

        }//end scrollToCaret
    } //end FindAction

    //----------------------------
    class FindAgainAction extends FindAction {
        FindAgainAction() {
            super("findAgain");
        }
    }

    //----------------------------
    class ReplaceAction extends AbstractAction {
        String name;
        boolean isReplaceAgain = false;

        ReplaceAction() {  //only called by replaceAction
            super("replace");
            setEnabled(true);
        }

        ReplaceAction(String nm) { //called by replaceAgainAction
            super(nm);
            name = nm;
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event) {
            //Frame parentFrame = getFrame();
            JMenuItem mi;
            String label;

            if (warningPopup == null) {
                warningPopup = new WarningDialog(parentFrame);
            }
            if ((editor1 == null) || (doc1 == null)) {
                String errstr = "Notepad2:editor1 or document is null";
                warningPopup.display(errstr);
                return;
            }
            if (!(event.getSource() instanceof JMenuItem)) {
                String errstr = "Notepad2:ReplaceAction: not an instance of JMenuItem";
                warningPopup.display(errstr);
                return;
            }
            mi = (JMenuItem) event.getSource();
            label = mi.getText();
            if (label.equals("ReplaceAgain")) {
                isReplaceAgain = true;
            } else {
                isReplaceAgain = false;
                lastExistingString = "";
            }
            globalReplace = false;
            StringStringBooleanBoolean content = new StringStringBooleanBoolean(lastExistingString, lastReplacementString, forwardReplaceDirection, false);

            boolean okPressed = myReplaceDialog.display(content);
            if (!okPressed) {
                return;
            }
            if ((content.string1 == null) || (content.string1.equals(""))) return;
            if (content.string2 == null) return;
            lastExistingString = content.string1;
            lastReplacementString = content.string2;
            forwardReplaceDirection = content.boolean1;
            globalReplace = content.boolean2;

            if (forwardReplaceDirection) {
                lastReplaceIndex = replaceForward(lastExistingString, lastReplacementString, globalReplace);
            } else {
                lastReplaceIndex = replaceBackward(lastExistingString, lastReplacementString, globalReplace);
            }
        }

        //----------------------
        //return last location found
        public int replaceForward(String lastExistingString, String lastReplacementString, boolean globalReplace) {
            int numFound = 0;
            do {
                try {  //forward
                    //System.out.println("Debug:Notepad2:in try");
                    int carPos = editor1.getCaretPosition();
                    //System.out.println("Debug:Notepad2: carPos "+carPos);
                    int strLength = doc1.getLength();
                    //System.out.println("Debug:Notepad2: strLength "+strLength);
                    //replace Forward
                    //todo: should we use the getText(pos,len,segment);
                    String chunk1 = doc1.getText(carPos, (strLength - carPos)); //offset,length

                    lastReplaceIndex = chunk1.indexOf(lastExistingString);
                    //System.out.println("Debug:Notepad2: lastReplaceIndex "+lastReplaceIndex);
                    if (lastReplaceIndex == -1) {
                        boolean okPressed = okCancelWarningPopup.display("Notepad2:string not found. Try backward?");
                        //handle backward
                        if (okPressed) {
                            forwardReplaceDirection = false;
                            lastReplaceIndex = replaceBackward(lastExistingString, lastReplacementString, globalReplace);

                        } else { //cancel
                            return (lastReplaceIndex);
                        }
                    } else { //found a string
                        lastReplaceIndex = lastReplaceIndex + carPos;
                        lastLastReplaceIndex = lastReplaceIndex;
                        editor1.setCaretPosition(lastReplaceIndex); //ready to type in body
                        editor1.moveCaretPosition(lastReplaceIndex + lastExistingString.length()); //ready to type in body
                        numFound++;
                        if (globalReplace) {
                            editor1.replaceSelection(lastReplacementString);//replace currently selected text
                        } else {
                            boolean okPressed = okCancelMessagePopup.display("Notepad2:Replace?");
                            if (okPressed) {
                                editor1.replaceSelection(lastReplacementString);//replace currently selected text
                            }
                        }
                    } //end found
                }//end try
                catch (BadLocationException badexception) {
                    String errstr = "Notepad2:replaceForward: " + badexception.getMessage();
                    warningPopup.display(errstr);
                    return (-1);
                }
            } while (globalReplace);
            return (lastReplaceIndex);
        } //end replaceForward

        //----------------------------------------------------
        //return last location found
        public int replaceBackward(String lastExistingString, String lastReplacementString, boolean globalReplace) {
            int numFound = 0;
            do {
                try {  //backward
                    if ((isReplaceAgain) || (globalReplace)) {//otherwise you replace same string
                        int foo = lastLastReplaceIndex; //begining of word
                        if (foo >= 0) {
                            editor1.setCaretPosition(foo);
                        }
                        System.out.println("Debug:Notepad2:replaceBackward: foo: " + foo);
                    }

                    int carPos = editor1.getCaretPosition();
                    //replace backward
                    //todo: should we use the getText(pos,len,segment);
                    String chunk1 = doc1.getText(0, carPos);
                    lastReplaceIndex = chunk1.lastIndexOf(lastExistingString);
                    if (lastReplaceIndex == -1) {
                        if (numFound > 0) { //must be global replace
                            //using this instead of warningPopup because warning prints to status window
                            messagePopup.display("Notepad2:Found " + numFound + " instances.");
                            return (lastReplaceIndex);
                        } else { //numFound==0
                            boolean okPressed = okCancelWarningPopup.display("Notepad2:string not found. Try forward?");
                            //handle forward
                            if (okPressed) {
                                forwardReplaceDirection = false;
                                lastReplaceIndex = replaceForward(lastExistingString, lastReplacementString, globalReplace);

                            } else { //cancel
                                return (lastReplaceIndex);
                            }
                        } //end numFound==0
                    } else { //found
                        lastLastReplaceIndex = lastReplaceIndex;
                        editor1.setCaretPosition(lastReplaceIndex); //ready to type in body
                        editor1.moveCaretPosition(lastReplaceIndex + lastExistingString.length()); //ready to type in body
                        numFound++;
                        if (globalReplace) {
                            editor1.replaceSelection(lastReplacementString);//replace currently selected text
                        } else {
                            boolean okPressed = okCancelMessagePopup.display("Notepad2:Replace?");
                            if (okPressed) {
                                editor1.replaceSelection(lastReplacementString);//replace currently selected text
                            }
                        }
                    } //end found
                }//end try
                catch (BadLocationException badexception) {
                    String errstr = "Notepad2:replaceBackward: " + badexception.getMessage();
                    warningPopup.display(errstr);
                    return (-1);
                }
            } while (globalReplace);
            return (lastReplaceIndex);
        }
    }//end Replace

    //-----------------------------------
    class ReplaceAgainAction extends ReplaceAction {
        ReplaceAgainAction() {
            super("replaceAgain");
        }
    }

    //----------------------------------------------------
    class NotImplementedAction extends AbstractAction {

        String name = "Error:";

        NotImplementedAction() {
            super("notImplementedAction");
            setEnabled(true);
        }

        NotImplementedAction(String nm) {
            super(nm);
            name = nm;
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();

            if (warningPopup == null) {
                warningPopup = new WarningDialog(parentFrame);
            }
            String msg = name + " not implemented yet.";
            warningPopup.setMsg(msg);
            warningPopup.display();
        }
    }

    //--------------------------------
    class InsertMethodNameAction extends AbstractAction {
        String methodName = "X";
        String functionPhrase = "public void ";

        InsertMethodNameAction() {
            super("insertMethodNameAction");
            setEnabled(true);
        }

        InsertMethodNameAction(String nm) {
            super(nm);
            methodName = nm;
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            //Frame frame = getFrame();
            if (warningPopup == null) {
                warningPopup = new WarningDialog(parentFrame);
            }
            if ((editor1 == null) || (doc1 == null)) {
                String errstr = "Error:editor1 or document is null";
                warningPopup.display(errstr);
            }
            functionPhrase = functionPhrase + methodName + "() {\n" + "}\n" + "\n";

            try {
                int carPos;
                carPos = editor1.getCaretPosition();
                doc1.insertString(carPos, functionPhrase, null);
                carPos = editor1.getCaretPosition(); //new location after insert
                if (carPos > 3) {
                    editor1.setCaretPosition(carPos - 3); //ready to type in body
                }
            } catch (BadLocationException badexception) {
                System.err.println("Notepad2:InsertMethodName:" + badexception.getMessage());
            }

        }
    }//end InsertMethodName

    //---------------------------------------------------------------
    class InitSysAction extends InsertMethodNameAction {
        InitSysAction() {
            super("initSys");
        }
    }

    class CallFromConstructorTopAction extends InsertMethodNameAction {
        CallFromConstructorTopAction() {
            super("callFromConstructorTop");
        }
    }

    class CallFromConstructorBottomAction extends InsertMethodNameAction {
        CallFromConstructorBottomAction() {
            super("callFromConstructorBottom");
        }
    }

    class MakeConnAction extends InsertMethodNameAction {
        MakeConnAction() {
            super("makeConn");
        }
    }

    class InitModuleAction extends InsertMethodNameAction {
        InitModuleAction() {
            super("initModule");
        }
    }

    class InitTrainEpochsAction extends InsertMethodNameAction {
        InitTrainEpochsAction() {
            super("initTrainEpochs");
        }
    }

    class InitTrainAction extends InsertMethodNameAction {
        InitTrainAction() {
            super("initTrain");
        }
    }

    class SimTrainAction extends InsertMethodNameAction {
        SimTrainAction() {
            super("simTrain");
        }
    }

    class EndTrainAction extends InsertMethodNameAction {
        EndTrainAction() {
            super("endTrain");
        }
    }

    class EndTrainEpochsAction extends InsertMethodNameAction {
        EndTrainEpochsAction() {
            super("endTrainEpochs");
        }
    }

    class InitRunEpochsAction extends InsertMethodNameAction {
        InitRunEpochsAction() {
            super("initRunEpochs");
        }
    }

    class InitRunAction extends InsertMethodNameAction {
        InitRunAction() {
            super("initRun");
        }
    }

    class SimRunAction extends InsertMethodNameAction {
        SimRunAction() {
            super("simRun");
        }
    }

    class EndRunAction extends InsertMethodNameAction {
        EndRunAction() {
            super("endRun");
        }
    }

    class EndRunEpochsAction extends InsertMethodNameAction {
        EndRunEpochsAction() {
            super("endRunEpochs");
        }
    }

    class EndModuleAction extends InsertMethodNameAction {
        EndModuleAction() {
            super("endModule");
        }
    }

    class EndSysAction extends InsertMethodNameAction {
        EndSysAction() {
            super("endSys");
        }
    }
}





