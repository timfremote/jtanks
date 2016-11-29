package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.ConsistentInteger;

public class VolatileInteger implements ConsistentInteger {
    private volatile int value;
    
    public int get() { return value; }
    public void set(int x) { value = x; }
}
