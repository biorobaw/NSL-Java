/* SCCS  @(#)NslZoomFrame.java	1.12---09/01/99--00:15:50 */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

package display;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("WeakerAccess")
public class NslZoomFrame extends Frame implements ActionListener {
    public NslZoomFrame(NslCanvas ndc) {
        Panel p = new Panel();
        p.setBackground(Color.lightGray);
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        Button b = (Button) p.add(new Button("Zoom in"));
        b.addActionListener(evt -> canvas.zoom_in());
        b = (Button) p.add(new Button("Zoom out"));
        b.addActionListener(evt -> canvas.zoom_out());
        add("North", p);

        canvas = ndc;
        canvas.set_canvas_type("Zoom");
        setTitle(canvas.nslGetName());
        canvas.addMouseListener(new ZoomMouseAdapter());
        canvas.addMouseMotionListener(new ZoomMouseAdapter());


        MenuBar mbar = new MenuBar();
        Menu m = new Menu("File");
        m.add(new MenuItem("Export Data"));
        m.add(new MenuItem("Print"));
        m.add(new MenuItem("Close"));
        mbar.add(m);
        m.addActionListener(this);

        setMenuBar(mbar);

        drawPanel = new Panel();

        gb = new GridLayout();
        drawPanel.setLayout(gb);
        drawPanel.add(canvas);
        add("Center", drawPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        });
    }

    private class ZoomMouseAdapter extends MouseAdapter implements MouseMotionListener {
        public void mousePressed(MouseEvent evt) {
            canvas.mousePressed(evt);
        }

        public void mouseReleased(MouseEvent evt) {
            canvas.mouseReleased(evt);
        }

        public void mouseDragged(MouseEvent evt) {
            canvas.mouseDragged(evt);
        }

        public void mouseMoved(MouseEvent evt) {
        }
    }

    public void actionPerformed(ActionEvent evt) {
        String action = evt.getActionCommand();
        switch (action) {
            case "Close":
                dispose();
                break;
            case "Print":
                canvas.Print();
                break;
            case "Export Data":
                System.out.println("NslZoomFrame:Exporting...");
                //NslOutFileProperty ndofp = new NslOutFileProperty(NslCanvas.Frame);
                NslOutFileProperty ndofp = new NslOutFileProperty(canvas.nslDisplayFrame);
                ndofp.setVisible(true);
                System.out.println("NslZoomFrame:Finished saving...");
                break;
        }
    }

    NslCanvas canvas;
    private Panel drawPanel;
    private GridLayout gb;
    private GridBagConstraints gbc;
}
