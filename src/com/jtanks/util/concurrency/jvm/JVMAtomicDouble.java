package com.jtanks.util.concurrency.jvm;

import java.util.concurrent.atomic.AtomicLong;

import com.jtanks.util.concurrency.AtomicDouble;

public class JVMAtomicDouble implements AtomicDouble {
    private final AtomicLong value;

    public JVMAtomicDouble() { value = new AtomicLong(); }
    
    public boolean compareAndSet(double expected, double update) {
        long expectedLong = Double.doubleToLongBits(update);
        long updateLong = Double.doubleToLongBits(update);
        return value.compareAndSet(expectedLong, updateLong);
    }

    public double get() { return Double.longBitsToDouble(value.get()); }
    public void set(double x) { value.set(Double.doubleToLongBits(x)); }
}
