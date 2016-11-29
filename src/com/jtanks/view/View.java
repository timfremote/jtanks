package com.jtanks.view;

public interface View {
    void addGoListener(GoListener listener);
    void update();
    void show();
    void done();
}
