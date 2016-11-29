package com.jtanks.view.gui.swing;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JFrame;

import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGridLayout;
import com.jtanks.view.gui.JTLabel;

public class SwingJTFrame implements JTFrame {
    private JFrame frame;
    
    public SwingJTFrame() { 
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void add(Component component) { 
        frame.add(component); 
    }

    public void add(JTLabel label) { label.addTo(this); }
    public void add(JTButton button) { button.addTo(this); }
    public void add(JTCanvas canvas) { canvas.addTo(this); }
    public int getHeight() { return frame.getHeight(); }
    public int getWidth() { return frame.getWidth(); }
    public int getX() { return frame.getX(); }
    public int getY() { return frame.getY(); }
    public void pack() { frame.pack(); }
    public void setLayout(LayoutManager layout) { frame.setLayout(layout); }
    public void setLocation(int x, int y) { frame.setLocation(x, y); }
    public void setSize(int width, int height) { frame.setSize(width, height); }
    public void setVisible(boolean b) { frame.setVisible(b); }
    public void setTitle(String title) { frame.setTitle(title); }
    public void setLayout(JTGridLayout layout) { ((SwingJTGridLayout)layout).applyTo(frame); }
}
