package com.jtanks.view.gui;

import java.awt.Color;

public interface JTCanvas {
    void setBackground(Color color);
    void setSize(int width, int height);
    int getWidth();
    int getHeight();
    void addTo(JTFrame frame);
    void repaint();
    JTImage createImage();
    void draw(JTImage image, JTGraphics onscreen);
    void addListener(JTCanvasListener listener);
}
