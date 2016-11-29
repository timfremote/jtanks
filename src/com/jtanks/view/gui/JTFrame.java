package com.jtanks.view.gui;

public interface JTFrame {
    void add(JTLabel label);
    void add(JTButton button);
    void add(JTCanvas canvas);
    void setLayout(JTGridLayout layout);
    void pack();
    void setLocation(int x, int y);
    int getHeight();
    int getWidth();
    int getX();
    int getY();
    void setSize(int width, int height);
    void setVisible(boolean b);
    void setTitle(String string);
}
