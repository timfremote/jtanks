package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.AtomicInteger;

public class JVMAtomicInteger implements AtomicInteger {
    private final java.util.concurrent.atomic.AtomicInteger value;
    
    public JVMAtomicInteger() { value = new java.util.concurrent.atomic.AtomicInteger(); } 

    public void atomicAdd(int x) { value.getAndAdd(x); }
    public void atomicDecrement() { value.getAndDecrement(); }
    public void atomicIncrement() { value.getAndIncrement(); }
    public void atomicSubtract(int x) { value.getAndAdd(-x); }
    public boolean compareAndSet(int expected, int update) { return value.compareAndSet(expected, update); }
    public int get() { return value.get(); }
    public void set(int x) { value.set(x); }
}
