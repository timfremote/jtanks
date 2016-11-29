package com.jtanks.view.gui.swing;

import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGridLayout;
import com.jtanks.view.gui.JTLabel;

public class SwingGUIStyle implements GUIStyle {
    public JTGridLayout makeGridLayout() { return new SwingJTGridLayout(); }
    public JTButton makeButton() { return new SwingJTButton(); }
    public JTLabel makeLabel() { return new SwingJTLabel(); }
    public JTFrame makeFrame() { return new SwingJTFrame(); }
    public JTCanvas makeCanvas() { return new SwingJTCanvas(); }
}
