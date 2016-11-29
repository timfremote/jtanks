package com.jtanks.view.gui;

import java.awt.Color;

public interface JTLabel {
    void setText(String text);
    String getText();
    
    void setForeground(Color c);
    Color getForegroundColor();
    
    void addTo(JTFrame frame);
}
