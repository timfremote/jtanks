package com.jtanks.view.scoreboard.table;

import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.HealthReporter;
import com.jtanks.model.ScoreReporter;
import com.jtanks.model.StatusReporter;
import com.jtanks.model.Tank;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.swing.SwingGUIStyle;
import com.jtanks.view.scoreboard.table.StatusLabel;

public class StandardTableLabels {
    private final GUIStyle style;
    
    public StandardTableLabels() {
        this(new SwingGUIStyle());
    }
    
    public StandardTableLabels(GUIStyle style) {
        this.style = style;
    }
    
    public StatusLabel plainLabel(final String text) {
        return new GenericStatusLabel(style) {
            @Override
            protected String getTextFrom(StatusReporter reporter) {
                return text;
            }
        };
    }

    public StatusLabel tankNameLabel() {
        return new GenericStatusLabel(style) {
            protected String getTextFrom(StatusReporter reporter) {
                Tank tank = (Tank) reporter;
                return tank.getName();
            }
        };
    }
    
    public StatusLabel healthMeter() {
        return new GenericStatusLabel(style) {
            protected String getTextFrom(StatusReporter reporter) {
                HealthReporter healthReporter = (HealthReporter) reporter;
                int health = healthReporter.getHealth();
                return (0 >= health) ? "DEAD" : String.valueOf(health);
            }
        };
    }
    
    public StatusLabel scoreMeter() {
        return new GenericStatusLabel(style) {
            protected String getTextFrom(StatusReporter reporter) {
                ScoreReporter scoreReporter = (ScoreReporter) reporter;
                int score = scoreReporter.getScore();
                return String.valueOf(score);
            }
        };
    }
    
    public StatusLabel timeRemainingMeter() {
        return new GenericStatusLabel(style) {
            protected String getTextFrom(StatusReporter reporter) {
                GameStatusReporter gameStatusReporter = (GameStatusReporter) reporter;
                double time = gameStatusReporter.getTimeRemainingInSeconds();
                return (0 > time) ? "" : String.format("%6.3f s", time);
            }
        };
    }
    
    public StatusLabel gamesRemainingMeter() {
        return new GenericStatusLabel(style) {
            protected String getTextFrom(StatusReporter reporter) {
                GameStatusReporter gameStatusReporter = (GameStatusReporter) reporter;
                int gamesRemaining = gameStatusReporter.getGamesRemaining();
                return String.valueOf(gamesRemaining);
            }
        };
    }
}
