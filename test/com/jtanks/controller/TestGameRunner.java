package com.jtanks.controller;

import java.awt.Color;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.ConcurrentQuartermaster;
import com.jtanks.model.GameState;
import com.jtanks.model.Tank;
import com.jtanks.util.Clock;
import com.jtanks.util.Point;

public class TestGameRunner extends GameRunner {
    public TestGameRunner(Clock clock) {
        this(clock, new GameConfiguration());
    }
    
    public TestGameRunner(Clock clock, GameConfiguration config) {
        super(new DefaultScorer(), new GameState(clock, 0, 0), 
              new ConcurrentQuartermaster(Main.getTanks(config)), 
              clock, config);
    }

    @Override
	public void launchMissile(Color color, Point point, double heading, Tank tank) {
        super.launchMissile(color, point, heading, tank);
    }
    
    @Override
	public void dropMine(Color color, Point point) {
        super.dropMine(color, point);
    }
}
