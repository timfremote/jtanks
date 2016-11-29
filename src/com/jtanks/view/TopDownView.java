package com.jtanks.view;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.GameState;
import com.jtanks.model.Quartermaster;
import com.jtanks.view.arena.ArenaDisplay;
import com.jtanks.view.scoreboard.ScoreBoard;

public class TopDownView implements View {
    private final GameWindow[] windows;
    
    public TopDownView(final GameState gameState, Quartermaster quartermaster, GameConfiguration config) {
        this(new ArenaDisplay(quartermaster, config),
             new ScoreBoard(gameState, quartermaster, config));
    }
    
    public TopDownView(final GameWindow ...windows) {
        this.windows = windows;
        update();
    }

    public void addGoListener(GoListener listener) {
        for (GameWindow window : windows) { window.addGoListener(listener); }
    }
    
    public void show() {
        for (GameWindow window : windows) { window.show(); }
    }

    public void update() {
        for (GameWindow window : windows) { window.update(); }
    }

    public void done() {
        // Does nothing now - could light some fireworks or something for the winner
    }
}