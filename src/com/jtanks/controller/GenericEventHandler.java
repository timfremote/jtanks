package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.controller.GameRunner.WALLS;
import com.jtanks.model.MovingEntity;
import com.jtanks.util.Clock;
import com.jtanks.util.Point;

public abstract class GenericEventHandler {
    private final GameConfiguration config;
    private final Clock clock;

    public abstract void notifyUpdated();
    protected abstract void collideWithWall(WALLS walls);
    protected abstract MovingEntity getEntity();
    
    public GenericEventHandler(Clock clock, GameConfiguration config) {
        this.clock = clock;
        this.config = config;
    }
    
    synchronized void updatePosition() {
        MovingEntity entity = getEntity();
        
        Point newPosition;
        if (entity.isDestroyed()) { 
            newPosition = entity.getPosition();
        } else {
            newPosition = move(entity);
        }
        
        entity.setPosition(newPosition);
    }
    
    private Point move(MovingEntity entity) {
        long timeDelta = clock.getCurrentTime() - entity.lastMovedAt();
        double metersMoved = entity.getSpeed() * (timeDelta / 1000d); // speed in m/s  

        double heading = entity.getHeading();
        double xChange = metersMoved * Math.cos(heading);
        double yChange = metersMoved * Math.sin(heading);
        
        Point position = entity.getPosition();
        double newX = position.getX() + xChange;
        double newY = position.getY() - yChange;
        
        if (newX < 0) {
            newX = 0;
            collideWithWall(WALLS.LEFT);
        }
        if (newX > config.virtualArenaSize) {
            newX = config.virtualArenaSize;
            collideWithWall(WALLS.RIGHT);
        }
        if (newY < 0) {
            newY = 0;
            collideWithWall(WALLS.TOP);
        }
        if (newY > config.virtualArenaSize) {
            newY = config.virtualArenaSize;
            collideWithWall(WALLS.BOTTOM);
        }

        return new Point(newX, newY);
    }
}
