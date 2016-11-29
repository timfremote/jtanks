package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.ConsistentLong;

public class VolatileLong implements ConsistentLong {
    private volatile long value;

    public long get() { return value; }
    public void set(long x) { value = x; }
}
