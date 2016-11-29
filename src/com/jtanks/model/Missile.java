package com.jtanks.model;

import java.awt.Color;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.util.Clock;
import com.jtanks.util.Point;
import com.jtanks.util.SystemClock;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentBoolean;
import com.jtanks.util.concurrency.ConsistentDouble;
import com.jtanks.util.concurrency.ConsistentLong;
import com.jtanks.util.concurrency.ConsistentPoint;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

// FIXME: Concurrency tests
public class Missile implements MovingEntity {
    private static final double SIZE = 2d;
    
    private final Color color;
    private final Tank creator;
    private final Clock clock;
    private final long endOfLife;
    private final double speed;

    private final ConsistentDouble heading;
    private final ConsistentPoint position;
    private final ConsistentLong lastMoved;
    private final ConsistentBoolean destroyed;

    public Missile(Color color, Point startPoint, double heading, Tank creator, GameConfiguration config) {
        this(color, startPoint, heading, creator, new SystemClock(), config, new JVMConcurrentStorageProvider());
    }
    
    public Missile(Color color, Point initialPosition, double heading, 
                   Tank creator, Clock clock, GameConfiguration config, ConcurrentStorageProvider storageProvider) {
        this.color = color;
        this.creator = creator;
        this.clock = clock;
        endOfLife = clock.getCurrentTime() + config.missileLifetimeInMillis;
        speed = config.missileSpeed;

        this.heading = storageProvider.makeConsistentDouble();
        setHeading(heading);
        position = storageProvider.makeConsistentPoint(initialPosition);
        lastMoved = storageProvider.makeConsistentLong();
        lastMoved.set(clock.getCurrentTime());
        destroyed = storageProvider.makeConsistentBoolean();
    }

    public Color getColor() { return color; }
    public Point getPosition() { return position.get(); }
    public double getHeading() { return heading.get(); }
    public double getSize() { return SIZE; }
    public double getSpeed() { return speed; }
    public Tank getCreator() { return creator; }

    public void destroy() { destroyed.set(true); }

    public boolean isDestroyed() {
        return destroyed.get() || (clock.getCurrentTime() >= endOfLife);
    }

    public void setHeading(double heading) { this.heading.set(heading); }
    public void setPosition(Point newPosition) { 
        position.set(newPosition); 
        lastMoved.set(clock.getCurrentTime());
    }
    public long lastMovedAt() { return lastMoved.get(); }
}
