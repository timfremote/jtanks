package com.jtanks.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jtanks.controller.drivers.TankRunner;
import com.jtanks.model.Tank;
import com.jtanks.util.Clock;
import com.jtanks.util.Point;

import org.junit.Before;

public class TankDriverTestFrame {
    private TestGameRunner gameRunner;
    protected TestClock testClock;

    @Before public void setUpTestArena() {
        testClock = new TestClock();
        gameRunner = new TestGameRunner(testClock);
    }
    
    public TestGameRunner getGameRunner() {
        return gameRunner;
    }
    
    public TankRunner getMyTankRunner(Color color) {
        for (TankRunner tankRunner : gameRunner.getTankRunners()) {
            if (tankRunner.getTank().getColor().equals(color)) { return tankRunner; }
        }
        throw new IllegalArgumentException("No tank of color " + color);
    }
    
    public List<Tank> getOtherTanks() {
        List<Tank> otherTanks = new ArrayList<Tank>();
        otherTanks.addAll(gameRunner.getQuartermaster().getTanks());
        // FIXME: Shouldn't rely on order in which objects are returned
        otherTanks.remove(0);
        return otherTanks;
    }
    
    public void positionTank(Tank tank, Point point) {
        tank.setPosition(point);
    }
    
    public void advanceTime(long millis) {
        testClock.rollForward(millis);
        gameRunner.updateArena();
    }
    
    private class TestClock implements Clock {
        private long theTime = 0;
        
        public void rollForward(long millis) {
            theTime += millis;
        }

        public long getCurrentTime() {
            return theTime;
        }
    }
}
