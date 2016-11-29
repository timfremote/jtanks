package com.jtanks.model;

import java.awt.Color;

import com.jtanks.util.Point;

public interface BasicEntity {
    Color getColor();
    Point getPosition();
    double getSize();
    boolean isDestroyed();
}
