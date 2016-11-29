package com.jtanks.view.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.jtanks.model.GameState;
import com.jtanks.model.Quartermaster;
import com.jtanks.view.GameWindow;
import com.jtanks.view.GoListener;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTButtonListener;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGridLayout;
import com.jtanks.view.gui.swing.SwingGUIStyle;
import com.jtanks.config.GameConfiguration;
import com.jtanks.view.scoreboard.table.TableStatusDisplay;

public class ScoreBoard implements JTButtonListener, GameWindow {
    private JTFrame frame;
    private StatusDisplay statusDisplay;
    private JTButton goButton;
    private List<GoListener> listeners = new ArrayList<GoListener>();
    private int initialX;
    private int initialY;

    public ScoreBoard(GameState gameState, Quartermaster quartermaster, GameConfiguration config) {
        this(gameState, quartermaster, new SwingGUIStyle(), config);
    }
    
    public ScoreBoard(GameState gameState, Quartermaster quartermaster, GUIStyle style, GameConfiguration config) {
        this(new TableStatusDisplay(config, quartermaster, gameState),
             new GoButton(style),
             style,
             config);
    }
    
    public ScoreBoard(StatusDisplay statusDisplay, JTButton goButton, 
                      GUIStyle style, GameConfiguration config) {
        this.frame = style.makeFrame();
        this.goButton = goButton; 
        this.statusDisplay = statusDisplay;

        JTGridLayout grid = style.makeGridLayout();
        grid.setRows(statusDisplay.getNumberOfRows() + 1);
        frame.setLayout(grid);
        frame.setSize(config.scoreBoardWidth, frame.getHeight());
        initialX = config.scoreBoardXCoordinate;
        initialY = config.scoreBoardYCoordinate;
        
        statusDisplay.addTo(frame);
        goButton.addTo(frame);
    }

    public void update() { 
        statusDisplay.update();
    }

    public void addGoListener(GoListener listener) { 
        if (0 == listeners.size()) {
            goButton.addListener(this);
        }
        listeners.add(listener); 
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(initialX, initialY);
    }

    public void click() {
        for (GoListener listener : listeners) {
            listener.go();
        }
    }
}