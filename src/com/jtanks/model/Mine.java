package com.jtanks.model;

import java.awt.Color;

import com.jtanks.util.Point;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentBoolean;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

public class Mine implements BasicEntity {
    private static final double DIAMETER = 4d;

    private final Color color;
    private final Point position;
    private final ConsistentBoolean destroyed;

    public Mine(Color color, Point position) {
        this(color, position, new JVMConcurrentStorageProvider());
    }
    
    public Mine(Color color, Point position, ConcurrentStorageProvider storageProvider) {
        this.color = color;
        this.position = position;
        destroyed = storageProvider.makeConsistentBoolean();
        destroyed.set(false);
    }

    public double getSize() { return DIAMETER; }
    public Color getColor() { return color; }
    public Point getPosition() { return position; }
    public boolean isDestroyed() { return destroyed.get(); }
    public void destroy() { destroyed.set(true); }
}
