package com.jtanks.config;

import java.util.Arrays;
import java.util.List;

import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.swing.SwingGUIStyle;
import com.jtanks.view.scoreboard.table.StandardTableLabels;
import com.jtanks.view.scoreboard.table.StatusLabel;
import com.jtanks.view.scoreboard.table.StatusTableConfiguration;

public class GameConfiguration implements StatusTableConfiguration {
    /********************* Tank attributes ************************/
    public int missileCapacity = 5;
    public int mineCapacity = 2;
    public double maximumSpeed = 40d;
    public int maximumHealth = 100;
    public int radarRange = 50;
    
    // FIXME: Should check these constraints on startup
    // cannonLength must be greater than tank radius - or you will shoot yourself when you fire!
    public double cannonLength = 4d;
    // mineTubeLength must be greater than (tank radius + mine radius) - or you will explode immediately on dropping a mine!
    public double mineTubeLength = 5d; 

    /********************* Missile attributes *********************/
    public double missileSpeed = 80d;
    public int missileLifetimeInMillis = 2000;
    
    /********************* Arena attributes ***********************/
    
    public int virtualArenaSize = 200;
    
    /********************* Position, size, and titles of game windows *********************/
    public String arenaTitle    = "JTanks... FIGHT!";
    public int arenaDimension   = 600;
    public int arenaXCoordinate = 0;
    public int arenaYCoordinate = 0;

    public int scoreBoardWidth       = 600;
    public int scoreBoardXCoordinate = 610;
    public int scoreBoardYCoordinate = 0;
    
    /********************* Scoreboard layout *********************/
    private StandardTableLabels labels;


    public GameConfiguration() { this(new SwingGUIStyle()); }
    public GameConfiguration(GUIStyle style) { this.labels = new StandardTableLabels(style); }

    public List<? extends StatusLabel> getHeaderLabels() {
        return Arrays.asList(labels.plainLabel("Driver name"),
                             labels.plainLabel("Health"),
                             labels.plainLabel("Score"),
                             labels.plainLabel("")); // Blank label fills out grid
    }

    public List<? extends StatusLabel> getLabelsForTankRow() {
        return Arrays.asList(labels.tankNameLabel(),
                             labels.healthMeter(),
                             labels.scoreMeter(),
                             labels.plainLabel("")); // Blank label fills out grid
    }

    public List<? extends StatusLabel> getFooterLabels() {
        return Arrays.asList(labels.plainLabel("Time remaining: "),
                             labels.timeRemainingMeter(),
                             labels.plainLabel("Games remaining: "),
                             labels.gamesRemainingMeter());
    }
}
