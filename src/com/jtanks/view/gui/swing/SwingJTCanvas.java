package com.jtanks.view.gui.swing;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGraphics;
import com.jtanks.view.gui.JTImage;
import com.jtanks.view.gui.JTCanvasListener;

public class SwingJTCanvas implements JTCanvas {
    Canvas canvas;
    private List<JTCanvasListener> listeners = new ArrayList<JTCanvasListener>();
    
    public SwingJTCanvas() {
        canvas = new Canvas() {
            private static final long serialVersionUID = 1L;

            @Override
            public void update(Graphics g) {
                JTGraphics graphics = new SwingJTGraphics(g);
                callListeners(graphics);
            }
        };
        canvas.setIgnoreRepaint(true);
    }
    
    public void setBackground(Color color) { canvas.setBackground(color); }
    public void setSize(int width, int height) { canvas.setSize(width, height); }
    public int getWidth() { return canvas.getWidth(); }
    public int getHeight() { return canvas.getHeight(); }
    public void repaint() { canvas.repaint(); }
    public void addTo(JTFrame frame) { 
        ((SwingJTFrame)frame).add(canvas); 
    }

    public JTImage createImage() {
        Image image = canvas.createImage(getWidth(), getHeight());
        return new SwingJTImage(image);
    }

    public void draw(JTImage image, JTGraphics onscreen) {
        ((SwingJTImage)image).draw(onscreen, canvas);
    }

    public void addListener(JTCanvasListener listener) {
        listeners.add(listener);
    }

    private void callListeners(JTGraphics g) {
        for (JTCanvasListener listener : listeners) {
            listener.update(g);
        }
    }
}
