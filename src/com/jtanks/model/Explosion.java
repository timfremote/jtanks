package com.jtanks.model;

import java.awt.Color;

import com.jtanks.util.Clock;
import com.jtanks.util.Point;
import com.jtanks.util.SystemClock;

public class Explosion implements BasicEntity {
    private static final double START_SIZE = 10d;
    private static final int LIFETIME = 500;
    private static final Color COLOR = new Color(255, 100, 255);
    
    private final Point position;
    private final Clock clock;
    private final long creationTime;
    private final long endOfLife;

    public Explosion(Point position) {
        this(position, new SystemClock());
    }
    
    public Explosion(Point position, Clock clock) {
        this.position = position;
        this.clock = clock;
        creationTime = clock.getCurrentTime();
        endOfLife = creationTime + LIFETIME;
    }

    public Color getColor() { return COLOR; }
    public Point getPosition() { return position; }

    public boolean isDestroyed() {
        return (clock.getCurrentTime() >= endOfLife);
    }
    
    public double getSize() { 
        long lifeRemaining = endOfLife - clock.getCurrentTime();
        double shrinkage = (double)lifeRemaining / (double)LIFETIME;
        return START_SIZE * (shrinkage);
    }
    
    @Override
    public String toString() {
        return "Explosion at " + position;
    }
}
