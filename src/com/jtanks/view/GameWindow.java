package com.jtanks.view;

public interface GameWindow {
    void show();
    void update();
    void addGoListener(GoListener listener);
}
