package com.jtanks.view.scoreboard;

import com.jtanks.view.gui.JTFrame;

public interface StatusDisplay {
    void addTo(JTFrame frame);
    void update();
    int getNumberOfRows();
}
