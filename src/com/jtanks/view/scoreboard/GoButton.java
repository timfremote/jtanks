package com.jtanks.view.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTButtonListener;
import com.jtanks.view.gui.JTFrame;

public class GoButton implements JTButton, JTButtonListener {
    private JTButton button;
    private List<JTButtonListener> listeners = new ArrayList<JTButtonListener>();
    
    public GoButton(GUIStyle style) {
        button = style.makeButton();
        button.setText("Go");
    }

    public void addListener(JTButtonListener listener) {
        if (0 == listeners.size()) {
            button.addListener(this); 
        }
        listeners.add(listener);
    }
    
    public void addTo(JTFrame frame) { button.addTo(frame);  }

    public void disable() {
        throw new UnsupportedOperationException("Can't directly disable a go button");
    }

    public void setText(String text) {
        throw new UnsupportedOperationException("Can't modify go button text");
    }

    public void click() {
        button.disable();
        for (JTButtonListener listener : listeners) {
            listener.click();
        }
    }
}
