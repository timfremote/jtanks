package com.jtanks.view.gui.swing;

import java.awt.GridLayout;
import javax.swing.JFrame;

import com.jtanks.view.gui.JTGridLayout;

public class SwingJTGridLayout implements JTGridLayout {
    private GridLayout layout;
    
    public SwingJTGridLayout() { layout = new GridLayout(); }
    public void applyTo(JFrame frame) { frame.setLayout(layout); }
    public void setRows(int rows) { layout.setRows(rows); }
    public int getRows() { return layout.getRows(); }
}
