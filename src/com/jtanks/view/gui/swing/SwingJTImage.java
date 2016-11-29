package com.jtanks.view.gui.swing;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

import com.jtanks.view.gui.JTGraphics;
import com.jtanks.view.gui.JTImage;

public class SwingJTImage implements JTImage {
    private final Image image;

    public SwingJTImage(Image image) { this.image = image; }
    public void flush() { image.flush(); }
    
    public JTGraphics getGraphics() { 
        Graphics imageGraphics = image.getGraphics();
        return new SwingJTGraphics(imageGraphics); 
    }
    
    public void draw(JTGraphics g, Canvas canvas) {
        ((SwingJTGraphics)g).draw(image, canvas);
    }
}
