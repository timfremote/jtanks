package com.jtanks.view.arena;

import java.awt.Color;

import com.jtanks.util.Point;

public interface Drawable {
    void setColor(Color color);
    void fillCircle(Point centre, double diameter);
    void drawCircle(Point centre, double diameter);
    void drawLine(Point from, Point to);
    void drawPoint(Point point);
}
