/* SCCS  %W%---%G%--%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

package com.github.biorobaw.nslj.display;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Vector;


@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "Duplicates"})
public class NslPanel extends Panel {

    // Constants

    private static final int initialColumns = 1;

    // Variables

    private Vector<NslCanvas> canvasList;
    private Vector<NslNumericEditor> variableList;
    private GridLayout gl;
    private NslFrame frame;

    private int indexOfCurrentCanvas;

    // Constructors

    NslPanel(NslFrame frame) {
        super();
        this.frame = frame;
        indexOfCurrentCanvas = -1;
        canvasList = new Vector<>(8);
        variableList = new Vector<>(8);
        gl = new GridLayout(0, initialColumns);
        setLayout(gl);
    }

    // Methods

    public boolean setColumns(int num) {
        if (getComponentCount() < num) {
            return false;
        }

        gl.setColumns(num);
        validate();
        doLayout();
        return true;
    }

    public boolean setRows(int num) {
        if (getComponentCount() < 1) {
            return false;
        }
        gl.setRows(num);
        validate();
        doLayout();
        return true;
    }

    public void addVariable(String variableName,
                            NslVariableInfo variableInfo) {
        NslNumericEditor n = new NslNumericEditor(frame, variableName, variableInfo);
        variableList.addElement(n);

        //ScrollPane p = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        //setLayout();
        //n.doLayout();
        //n.add(new Label(variableName));
        //p.add(n);
        //n.validate();
        add(n);
        validate();
    }

    public boolean canvasExist(String n) {

        Enumeration<NslCanvas> E = canvasList.elements();
        NslCanvas c;

        while (E.hasMoreElements()) {
            c = E.nextElement();
            if (c != null && c.getWindowName().equals(n)) {
                return true;
            }
        }

        return false;
    }

    public NslCanvas getCanvas(String n) {

        Enumeration<NslCanvas> E = canvasList.elements();
        NslCanvas c;

        while (E.hasMoreElements()) {
            c = E.nextElement();
            if (c != null && c.getWindowName().equals(n)) {
                return c;
            }
        }

        return null;
    }

    /*public NslCanvas addCanvas(String variableName, NslVariableInfo variableInfo, 
			       String plotType, double min, double max) 
			       throws CanvasCreationException {
	NslCanvas c = null;
	try {
	    c = addCanvas(variableName, variableInfo, plotType);
	    c.set_min_max(min, max);
	} catch (Exception e) { 
	    throw new CanvasCreationException();
	}
finally {
 	    return c;
	}
    }*/

    public NslCanvas addCanvas(String variableName,
                               NslVariableInfo variableInfo,
                               String plotType, double min, double max)
            throws CanvasCreationException {
        NslCanvas canvas = null;

        try {
            String typeName;

            if (plotType.equals("Temporal")) {
                variableInfo.setHistory(true);
            }
            typeName = "display.Nsl" + plotType + "Canvas";
            Class<NslCanvas> canvasType = (Class<NslCanvas>) Class.forName(typeName);

            // todo: do we really need paramTypes?
            Class[] paramTypes = new Class[3];
            paramTypes[0] = Class.forName("display.NslFrame");
            paramTypes[1] = Class.forName("java.lang.String");
            paramTypes[2] = Class.forName("display.NslVariableInfo");

            Constructor<NslCanvas> canvasConstructor = canvasType.getConstructor(paramTypes);

            Object[] params = new Object[3];
            params[0] = frame;
            params[1] = variableName;
            params[2] = variableInfo;

            canvas = canvasConstructor.newInstance(params);
            canvas.set_min_max(min, max);

            String fn = frame.getFontName() == null ? "Times" : frame.getFontName();
            String bg = frame.getBackgroundColor() == null ? "white" : frame.getBackgroundColor();
            String fg = frame.getForegroundColor() == null ? "black" : frame.getForegroundColor();

            canvas.setFont(new Font(fn, Font.PLAIN, 12));
            canvas.setBackground(NslColor.getColor(bg));
            canvas.setForeground(NslColor.getColor(fg));

            canvas.mouseAdapter = new CanvasMouseAdapter();
            canvas.addMouseListener(canvas.mouseAdapter);

            canvasList.addElement(canvas);

            add(canvas);

            int plotCount = getComponentCount();

            if (plotCount > 0) {
                int colCount = (plotCount > 2) ? 3 : plotCount;
                setColumns(colCount);
            } else {
                setColumns(1);
            }

            validate();// the layout

        } catch (InvocationTargetException e) {
            System.err.println("NslPanel: [Error] Could not create plot for " + variableName);
            System.err.println("NslPanel: [Posibilities] not enough memory, tmax is 0, or newInstance statement malformed.");
            e.printStackTrace();
            throw new CanvasCreationException();
        } catch (Exception e) {
            System.err.println("NslPanel: [Error] Could not create plot for " + variableName);
            e.printStackTrace();
            throw new CanvasCreationException();
        } finally {
            return canvas;
        }
    }

    public NslCanvas addUserCanvas(String variableName,
                                   NslVariableInfo variableInfo,
                                   String plotType, double min, double max)
            throws CanvasCreationException {
        NslCanvas c = null;
        try {
            c = addUserCanvas(variableName,
                    variableInfo, plotType);
            c.set_min_max(min, max);
        } catch (Exception e) {
            throw new CanvasCreationException();
        } finally {
            return c;
        }
    }

    public NslCanvas addUserCanvas(String variableName,
                                   NslVariableInfo variableInfo,
                                   String plotType)
            throws CanvasCreationException {
        NslCanvas c = null;
        try {
            String typeName;
            typeName = "Nsl" + plotType + "Canvas";
            Class<NslCanvas> canvasType = (Class<NslCanvas>) Class.forName(typeName);
            //System.err.println("Creating canvas: "+variableName);
            Class[] paramTypes = new Class[4];
            paramTypes[0] = Class.forName("java.lang.String");
            paramTypes[1] = Class.forName("lang.NslModule");
            paramTypes[2] = Class.forName("display.NslFrame");
            paramTypes[3] = Class.forName("display.NslVariableInfo");
            Constructor<NslCanvas> canvasConstructor = canvasType.getConstructor(paramTypes);
            Object[] params = new Object[4];
            params[0] = variableName;
            params[1] = variableInfo != null ?
                    variableInfo.getNslVar().nslGetParentModule() :
                    null;
            params[2] = frame;
            params[3] = variableInfo;

            String fn = frame.getFontName() == null ? "Times" : frame.getFontName();
            String bg = frame.getBackgroundColor() == null ? "white" : frame.getBackgroundColor();
            String fg = frame.getForegroundColor() == null ? "black" : frame.getForegroundColor();

            //System.out.println("Font: "+fn);
            //System.out.println("Bg  : "+bg);
            //System.out.println("Fg  : "+fg);
            c = canvasConstructor.newInstance(params);
            c.setFont(new Font(fn, Font.PLAIN, 12));
            //c.setBackground(Color.white);
      /*if (Color.getColor(bg)==null) {
          System.out.println("Can't find color");
      }*/
            c.setBackground(NslColor.getColor(bg));
            c.setForeground(NslColor.getColor(fg));
            canvasList.addElement(c);
            c.mouseAdapter = new CanvasMouseAdapter();
            c.addMouseListener(c.mouseAdapter);
            add(c);
            int plotCount = getComponentCount();
            if (plotCount > 0) {
                int colCount = (plotCount > 2) ? 3 : plotCount;
                setColumns(colCount);
            } else {
                setColumns(1);
            }
            validate();// the layout
        } catch (InvocationTargetException e) {
            System.err.println("Error: Canvas " + plotType + " was not found");
            e.printStackTrace();
            throw new CanvasCreationException();
        } catch (Exception e) {
            System.err.println("Something weired happened!");
            e.printStackTrace();
            throw new CanvasCreationException();
        } finally {
            return c;
        }
    }

    public void removeCanvas() throws NoCanvasSelectedException {
        if (indexOfCurrentCanvas == -1) {
            System.out.println("NslPanel: Error: not canvas selected.");
            throw new NoCanvasSelectedException();
        }
        remove(canvasList.elementAt(indexOfCurrentCanvas));
        canvasList.removeElementAt(indexOfCurrentCanvas);
        indexOfCurrentCanvas = -1;
        frame.plotSelectionChanged("");
        GridLayout l = (GridLayout) getLayout();
        int compCount = getComponentCount();
        if (compCount > 0 && compCount < l.getColumns())
            l.setColumns(compCount);
        validate(); // the layout
    }

    public void print(Graphics g) {
        for (int i = 0; i < canvasList.size(); i++)
            (canvasList.elementAt(i)).printAll(g);
    }

    public void setCanvasProperties() throws NoCanvasSelectedException {
        try {
            String canvasType =
                    canvasList.elementAt(indexOfCurrentCanvas).getClass().getName() + "Property";
            Class<Dialog> canvasClass = (Class<Dialog>) Class.forName(canvasType);
            Class[] typeList = new Class[1];
            typeList[0] = frame.getClass();
            Constructor<Dialog> propertyConstructor =
                    canvasClass.getConstructor(typeList);
            Object[] param = new Object[1];
            param[0] = frame;
            Dialog dp = propertyConstructor.newInstance(param);
            dp.setVisible(true);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoCanvasSelectedException();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    public void changeCanvas(String newType) throws NoCanvasSelectedException {
        if (indexOfCurrentCanvas == -1) {
            System.out.println("NslPanel:Warning: No Canvas Selected.");
            throw new NoCanvasSelectedException();
        }
        NslCanvas c = canvasList.elementAt(indexOfCurrentCanvas);
        NslCanvas tmp = c.copy(newType);
        if (tmp == null) {
            System.out.println("NslPanel:Error: Could not create canvas type: " + newType);
            return;
        }
        //System.out.println("NslPanel: new canvas name: "+tmp.canvas_name);
        canvasList.setElementAt(tmp, indexOfCurrentCanvas);
        tmp.mouseAdapter = new CanvasMouseAdapter();
        tmp.addMouseListener(tmp.mouseAdapter);
        tmp.setBackground(Color.cyan);
        remove(c);
        add(tmp, indexOfCurrentCanvas);
    }

    private class CanvasMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent evt) {

            switch (evt.getClickCount()) {
                case 1: {
                    // Repaint the new active canvas and previous canvas
                    NslCanvas canvas = null;
                    if (indexOfCurrentCanvas != -1) { // unselect old canvas
                        canvas = canvasList.elementAt(indexOfCurrentCanvas);
                        if (canvas == evt.getSource()) {
                            String bg = frame.getBackgroundColor();
                            if (bg == null) {
                                bg = "white";
                            }
                            canvas.setBackground(NslColor.getColor(bg));
                            indexOfCurrentCanvas = -1;
                            frame.plotSelectionChanged(""); //99.5.11 aa
                            return;
                        }
                        canvas.setBackground(Color.white);
                    }
                    indexOfCurrentCanvas = canvasList.indexOf(evt.getSource());
                    if (indexOfCurrentCanvas == -1) {  //
                        System.out.println("NslPanel:current canvas is -1");
                        return;
                    } else {   // if another canvas is selected change plot type and highlight
                        canvas = canvasList.elementAt(indexOfCurrentCanvas);
                        canvas.setBackground(Color.cyan);
                        String type = canvas.getClass().getName();
                        int nameStart = type.lastIndexOf("Display") + 7; // for Display
                        int nameEnd = type.length() - 6; // for Canvas
                        frame.plotSelectionChanged(type.substring(nameStart, nameEnd));
                    }
                }
                break;

                case 2: {
                    try {
                        zoomCanvas();
                    } catch (Exception e) {
                        System.err.println("NslPanel:Exception:" + e.toString());
                    }
                }
                break;
            }
        }//end mouse pressed
    } //end adapter

    public void zoomCanvas() throws NoCanvasSelectedException {
        if (indexOfCurrentCanvas == -1) {
            throw new NoCanvasSelectedException();
        } else {
            NslCanvas c =
                    canvasList.elementAt(indexOfCurrentCanvas);
            String canvasName = c.getClass().getName();
            NslCanvas tmp = null;
            if (canvasName.equals("display.NslTemporalCanvas")) {
                tmp = c.copy("Temporal");
            } else if (canvasName.equals("display.NslAreaCanvas")) {
                tmp = c.copy("Area");
            } else {
                System.err.println("NslPanel:Error: Only know how to Zoom Temporal or Area Level graphs.");
                return;
            }

            c.set_draw_size();

            NslZoomFrame zoomFrame = new NslZoomFrame(tmp);

            zoomFrame.setSize(450, 450);
            zoomFrame.setVisible(true);

        }
    }

    public void init() {
        if (canvasList == null || variableList == null)
            return;
        for (int i = 0; i < canvasList.size(); i++) {
            (canvasList.elementAt(i)).initCanvas();
        }

        for (int i = 0; i < variableList.size(); i++) {
            (variableList.elementAt(i)).initCanvas();
        }

    }

    public void initEpoch() {
        if (canvasList == null || variableList == null)
            return;

        //System.err.println("Init epoch");

        for (int i = 0; i < canvasList.size(); i++) {
            (canvasList.elementAt(i)).initEpochCanvas();
        }

        for (int i = 0; i < variableList.size(); i++) {
            (variableList.elementAt(i)).initEpochCanvas();
        }

    }

    public void collect() {
        if (canvasList == null || variableList == null)
            return;
        for (int i = 0; i < canvasList.size(); i++) {
            (canvasList.elementAt(i)).collect();
        }

        for (int i = 0; i < variableList.size(); i++) {
            (variableList.elementAt(i)).collect();
        }

    }

    public NslCanvas getCurrentCanvas() throws NoCanvasSelectedException {
        try {
            return canvasList.elementAt(indexOfCurrentCanvas);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoCanvasSelectedException();
        }
    }

    public Vector<NslCanvas> getCanvasList() {
        return canvasList;
    }


} 
