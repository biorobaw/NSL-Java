/* SCCS  %W% --- %G% -- %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

package com.github.biorobaw.nslj.display;

import com.github.biorobaw.nslj.lang.NslModule;

import java.awt.*;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class NslUserPanel extends ScrollPane {
    private String name;
    private NslModule module;
    private GridBagConstraints gc;
    private GridBagLayout g;
    private Panel p;

    public NslUserPanel(String name, NslModule module) {
        super();

        this.name = name;
        this.module = module;

        gc = new GridBagConstraints();
        g = new GridBagLayout();
        p = new Panel(g);

        p.setBackground(Color.white);

        add(p);

        System.out.println("Creating panel " + name + " for " + module.nslGetName());
    }

    public void addComponent(Component c) {
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.NONE;
        p.add(c, gc);
    }

    public String nslGetName() {
        return name;
    }
}

