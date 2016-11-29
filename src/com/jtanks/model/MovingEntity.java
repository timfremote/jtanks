package com.jtanks.model;

import com.jtanks.util.Point;

public interface MovingEntity extends BasicEntity {
    double getHeading();
    double getSpeed();

    void setHeading(double heading);
    void setPosition(Point position);
    
    long lastMovedAt();
}
