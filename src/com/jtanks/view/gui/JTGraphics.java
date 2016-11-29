package com.jtanks.view.gui;

import java.awt.Color;

public interface JTGraphics {
    void clearRect(int x, int y, int width, int height);
    void drawLine(int x1, int y1, int x2, int y2);
    void drawOval(int x, int y, int width, int height);
    void fillOval(int x, int y, int width, int height);
    void setColor(Color color);
}
