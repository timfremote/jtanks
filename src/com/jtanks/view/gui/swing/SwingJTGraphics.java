package com.jtanks.view.gui.swing;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.jtanks.view.gui.JTGraphics;

public class SwingJTGraphics implements JTGraphics {
    private final Graphics graphics;

    public SwingJTGraphics(Graphics graphics) { this.graphics = graphics; }
    public void clearRect(int x, int y, int width, int height) { graphics.clearRect(x, y, width, height); }
    public void drawLine(int x1, int y1, int x2, int y2) { graphics.drawLine(x1, y1, x2, y2); }
    public void drawOval(int x, int y, int width, int height) { graphics.drawOval(x, y, width, height); }
    public void fillOval(int x, int y, int width, int height) { graphics.fillOval(x, y, width, height); }
    public void setColor(Color color) { graphics.setColor(color); }
    
    public void draw(Image image, Canvas canvas) {
        graphics.drawImage(image, 0, 0, canvas);
    }
}
