package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.ConsistentDouble;

public class VolatileDouble implements ConsistentDouble {
    private volatile double value;
    
    public double get() { return value;}
    public void set(double x) { value = x; }
}
