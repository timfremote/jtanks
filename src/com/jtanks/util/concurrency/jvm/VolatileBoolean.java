package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.ConsistentBoolean;

public class VolatileBoolean implements ConsistentBoolean {
    private volatile boolean value;

    public boolean get() { return value; }
    public void set(boolean b) { value = b; }
}
