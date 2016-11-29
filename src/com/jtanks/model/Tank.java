package com.jtanks.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jtanks.config.GameConfiguration;

import com.jtanks.util.Clock;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;
import com.jtanks.util.SystemClock;

public class Tank implements HealthReporter, ScoreReporter, MovingEntityWithTrail, Comparable<Tank> {
    private static final double SIZE = 5d;
    private static final int TRAIL_LENGTH = 200;

    // Immutable - set on construction and never changed
    private final Clock clock;
    private final GameConfiguration config;
    private final Random random;
    private final ArmsLocker missileLocker;
    private final ArmsLocker mineLocker;    
    private final Color color;
    
    // FIXME: enforce and test this
    // Threadsafe attributes 
    private double speed = 0d; // May be updated by tank thread as well as tournament-runner thread (when tank hits wall, for example)
    private List<Point> trail = new CopyOnWriteArrayList<Point>(); // Read by view thread, updated by tournament-runner thread

    // FIXME: should be updated by tank thread only - don't try to set randomly on start
    private double heading = 0d;
    
    // FIXME: enforce and test this
    // Not threadsafe - must be updated by main thread only
    private String name;

    // FIXME: enforce and test this
    // Not threadsafe - must be updated by tournament-runner thread only
    private int score = 0;
    private int health;
    private Point position = new Point(0, 0);
    private long lastMovedTime;
    private boolean destroyed = false;
    private long destroyedTime = Long.MAX_VALUE;
    
    public Tank(Color color, GameConfiguration config) {
        this(color, new SystemClock(), config);
    }

    public Tank(Color color, Clock clock, GameConfiguration config) {
        this(color, clock, config, new Random());
    }
    
    public Tank(Color color, Clock clock, GameConfiguration config, Random random) {
        this.color = color;
        this.clock = clock;
        this.config = config;
        this.random = random;
        
        health = config.maximumHealth;
        missileLocker = new ArmsLocker(config.missileCapacity, clock);
        mineLocker = new ArmsLocker(config.mineCapacity, clock);
    }
    
    public void reset() {
        setSpeedAsPercentage(0);
        destroyedTime = Long.MAX_VALUE;
        destroyed = false;
        health = config.maximumHealth;
        moveToRandomPosition();
        missileLocker.restock();
        mineLocker.restock();
    }
    
    // Unchanging attributes
    public double getSize() { return SIZE; }
    public Color getColor() { return color; }
    
    // Armaments
    public ArmsLocker getMissileLocker() { return missileLocker; }
    public ArmsLocker getMineLocker() { return mineLocker; }
    
    // Health
    public int getHealth() { return health; }
    public void damage(int damage) {
        if (health <= 0) { return; }
        health -= damage;
        if (health <= 0) { 
            health = 0;
            destroy(); 
        }
    }

    // Speed
    public double getSpeed() { return speed;  }
    public void setSpeedAsPercentage(double percentageOfFullSpeed) {
        if (percentageOfFullSpeed < 0d) {
            percentageOfFullSpeed = 0d;
        } else if (percentageOfFullSpeed > 100d) {
            percentageOfFullSpeed = 100d;
        }
        speed = config.maximumSpeed * (percentageOfFullSpeed / 100d);
    }
    
    // Name
    public String getName() { return name; }
    public void setName(String name) {
        if (null == this.name) {
            this.name = name;
        }
    }
    
    // Score
    public int getScore() { return score; }
    public void addToScore(int addend) { score += addend; }
    
    // Position
    public Point getPosition() { return position; }
    public void setPosition(Point position) {
        lastMovedTime = clock.getCurrentTime();
        this.position = position;
        addPositionToTrail();
    }
    
    // FIXME: Eliminate this - should be the responsibility of controller
    private void moveToRandomPosition() {
        Point randomPosition = new Point(random.nextDouble() * config.virtualArenaSize, 
                                         random.nextDouble() * config.virtualArenaSize);
        setPosition(randomPosition);

        setHeading(random.nextDouble() * 2d * Math.PI);
    }
    
    // Heading
    public double getHeading() { return heading;  }
    public void setHeading(double heading) { this.heading = heading; }
    
    // Trail
    public List<Point> getTrail() { return trail; }
    private void addPositionToTrail() {
        trail.add(getPosition());
        if (trail.size() > TRAIL_LENGTH) {
            trail.remove(0);
        }
    }
    
    // Destroyed
    public void destroy() {
        destroyedTime = clock.getCurrentTime();
        destroyed = true;
    }
    public boolean isDestroyed() { return destroyed; }
    
    // Last moved
    public long lastMovedAt() { return lastMovedTime; }

    // Queries
    public boolean pointIsInsideBody(Point point) {
        return Euclid.circleContainsPoint(getPosition(), getSize() / 2d, point);
    }

    public Point findEndOfCannon() {
        return Euclid.getPointAtDistanceAndHeading(getPosition(), config.cannonLength, getHeading());
    }
    
    public Point findEndOfMineTube() {
        return Euclid.getPointAtDistanceAndHeading(
                getPosition(), config.mineTubeLength, Math.PI + getHeading());
    }
	
    public boolean outlived(Tank tank) {
        return destroyedTime > tank.destroyedTime;
    }

    public int compareTo(Tank x) {
        Tank tank = (Tank) x;
        return getName().compareTo(tank.getName());
    }
}
