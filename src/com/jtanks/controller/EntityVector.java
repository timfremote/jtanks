package com.jtanks.controller;

import com.jtanks.model.MovingEntity;
import com.jtanks.util.Point;

public class EntityVector {
    private final Point point;
    private final double speed;
    private final double heading;

    public EntityVector(MovingEntity entity) {
        // FIXME: for this to work, should make id unique across all entities
        this.point = new Point(entity.getPosition());
        this.speed = entity.getSpeed();
        this.heading = entity.getHeading();
    }

    public EntityVector(Point point, double speed, double heading) {
        this.point = point;
        this.speed = speed;
        this.heading = heading;
    }

    public Point getPosition() {
        return point;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHeading() {
        return heading;
    }
}
