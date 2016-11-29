package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.Point;
import com.jtanks.util.concurrency.ConsistentPoint;

public class VolatilePoint implements ConsistentPoint {
    private volatile Point p;
    
    public VolatilePoint(Point p) { set(p); }

    public Point get() { return p; }
    public void set(Point x) { p = x; }
}
