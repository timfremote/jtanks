package com.jtanks.view.scoreboard.table;

import java.awt.Color;

import com.jtanks.model.StatusReporter;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTLabel;

public class GenericStatusLabel implements StatusLabel {
    private StatusReporter reporter;
    private JTLabel label;

    public GenericStatusLabel(GUIStyle style) {
        label = style.makeLabel();
    }

    public void update() {
        String text = getText();
        label.setText(text);
    }

    public void addTo(JTFrame frame) { label.addTo(frame); }
    public void connectTo(StatusReporter reporter) { this.reporter = reporter; }
    public void setColor(Color color) { label.setForeground(color); }
    
    protected String getTextFrom(StatusReporter reporter) {
        return "getTextFrom not overridden";
    }
    
    private String getText() {
        if (null == reporter) { return "no reporter"; }
        return getTextFrom(reporter);
    }
}
