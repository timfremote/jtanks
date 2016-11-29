package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.ArmsLocker;
import com.jtanks.model.Tank;
import com.jtanks.util.Point;

public class ControlPanel {
    private final Tank tank;
    private final GameRunner gameRunner;
    private final ArmsLocker missileLocker;
    private final ArmsLocker mineLocker;
    private final GameConfiguration config;

    public ControlPanel(TankEventHandler tankEventHandler, GameRunner gameRunner, GameConfiguration config) {
        this.config = config;
        this.tank = tankEventHandler.getTank();
        missileLocker = tank.getMissileLocker();
        mineLocker = tank.getMineLocker();
        this.gameRunner = gameRunner;
    }

    public synchronized void fire() {
        if (!missileLocker.isEmpty()) {
            missileLocker.removeItem();
            gameRunner.launchMissile(tank.getColor(), tank.findEndOfCannon(), tank.getHeading(), tank);
        }
    }

    public synchronized void dropMine() {
        if (!mineLocker.isEmpty()) {
            mineLocker.removeItem();
            gameRunner.dropMine(tank.getColor(), tank.findEndOfMineTube());
        }
    }

    public Radar scan() {
        return new Radar(tank, tank.getPosition(), config.radarRange, gameRunner.getQuartermaster());
    }

    public synchronized void drive(double speed, double heading) {
        tank.setSpeedAsPercentage(speed);
        tank.setHeading(heading);
    }

    public int getAvailableMissiles() { return missileLocker.count(); }
    public int getAvailableMines() { return mineLocker.count(); }

    public Point getPosition() { return tank.getPosition(); }
    public double getHeading() { return tank.getHeading(); }
    public double getSpeed() { return tank.getSpeed(); }
    public double getTankSize() { return tank.getSize(); }
}
