package com.jtanks.view.gui.swing;

import java.awt.Color;

import javax.swing.JLabel;

import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTLabel;

public class SwingJTLabel implements JTLabel {
    private JLabel label;

    public SwingJTLabel() { label = new JLabel(); }
    
    public void setText(String text) { label.setText(text); }
    public String getText() { return label.getText(); }
 
    public void setForeground(Color c) { label.setForeground(c); }
    public Color getForegroundColor() { return label.getForeground(); }

    public void addTo(JTFrame frame) { 
        ((SwingJTFrame)frame).add(label); 
    }
}
