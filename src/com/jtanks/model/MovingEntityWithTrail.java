package com.jtanks.model;

import java.util.List;

import com.jtanks.util.Point;


public interface MovingEntityWithTrail extends MovingEntity {
    List<Point> getTrail();
}
