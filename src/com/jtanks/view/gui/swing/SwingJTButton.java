package com.jtanks.view.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTButtonListener;
import com.jtanks.view.gui.JTFrame;

public class SwingJTButton implements JTButton {
    private JButton button;

    public SwingJTButton() {
        button = new JButton();
    }
    
    public void addListener(final JTButtonListener listener) { 
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.click();
            }
        }); 
    }
    
    public void disable() { button.setEnabled(false); }
    public void setText(String text) { button.setText(text); }
    public void addTo(JTFrame frame) { 
        ((SwingJTFrame)frame).add(button); 
    }
}
