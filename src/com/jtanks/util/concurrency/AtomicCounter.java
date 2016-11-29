package com.jtanks.util.concurrency;

public interface AtomicCounter {
    int getCount();
    void setToMax();
    void setToZero();
    boolean isZero();
    boolean isMax();
    boolean decrement();
    boolean increment();
}
