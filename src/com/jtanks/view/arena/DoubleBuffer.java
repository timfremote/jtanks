package com.jtanks.view.arena;

import java.awt.Color;

import com.jtanks.util.Point;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTGraphics;
import com.jtanks.view.gui.JTImage;

public class DoubleBuffer implements Renderer {
    private final JTImage image;
    private final JTGraphics offscreen;
    private final JTCanvas canvas;

    public DoubleBuffer(JTCanvas canvas) {
        this.canvas = canvas;
        image = canvas.createImage(); 
        offscreen = image.getGraphics();
    }

    public void renderIn(JTGraphics onscreen) {
        canvas.draw(image, onscreen);
        image.flush();
    }

    public void drawLine(Point from, Point to) { 
        offscreen.drawLine(round(from.getX()), round(from.getY()), 
                           round(to.getX()), round(to.getY())); 
    }
    
    public void drawCircle(Point centre, double diameter) { 
        double upperLeftX = centre.getX() - (diameter/2);
        double upperLeftY = centre.getY() - (diameter/2);
        offscreen.drawOval(round(upperLeftX), round(upperLeftY), round(diameter), round(diameter)); 
    }
    
    public void fillCircle(Point centre, double diameter) { 
        double upperLeftX = centre.getX() - (diameter/2);
        double upperLeftY = centre.getY() - (diameter/2);
        offscreen.fillOval(round(upperLeftX), round(upperLeftY), round(diameter), round(diameter));
    }
    
    public void drawPoint(Point point) {
        drawLine(point, point);
    }

    public void setColor(Color color) { offscreen.setColor(color); }

    private int round(double x) { return (int) Math.round(x); }
}
