package com.jtanks.view.arena;

import java.awt.Color;

import com.jtanks.util.Point;

public class ScaledDrawable implements Drawable {
    private final Drawable drawable;
    private final double scalingFactor;

    public ScaledDrawable(Drawable drawable, double scalingFactor) {
        this.drawable = drawable;
        this.scalingFactor = scalingFactor;
    }

    public void drawLine(Point from, Point to) {
        drawable.drawLine(scale(from), scale(to));
    }

    public void drawCircle(Point centre, double diameter) {
        drawable.drawCircle(scale(centre), scale(diameter));
    }

    public void fillCircle(Point centre, double diameter) {
        drawable.fillCircle(scale(centre), scale(diameter));
    }
    
    public void drawPoint(Point point) {
        drawable.drawPoint(scale(point));
    }

    public void setColor(Color color) { drawable.setColor(color); }

    private Point scale(Point p) { 
        return new Point(scale(p.getX()), scale(p.getY())); 
    }
    
    private double scale(double c) { return scalingFactor * c; }
}
