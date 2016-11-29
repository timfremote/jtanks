package com.jtanks.view.gui;

public interface JTButton {
    void setText(String text);
    void disable();
    void addListener(JTButtonListener listener);
    void addTo(JTFrame frame);
}
