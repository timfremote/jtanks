package com.jtanks.view.scoreboard.table;

import java.awt.Color;

import com.jtanks.model.StatusReporter;
import com.jtanks.view.gui.JTFrame;

public interface StatusLabel {
    void connectTo(StatusReporter reporter);
    void addTo(JTFrame frame);
    void update();
    void setColor(Color color);
}
