package com.jtanks.util.concurrency.jvm;

import com.jtanks.util.concurrency.AtomicCounter;
import com.jtanks.util.concurrency.AtomicInteger;

public class JVMAtomicCounter implements AtomicCounter {
    private final int max;
    private AtomicInteger count = new JVMAtomicInteger();

    public JVMAtomicCounter(int max) { this.max = max; }
    public int getCount() { return count.get(); }
    public boolean isMax() { return max == count.get(); }
    public boolean isZero() { return 0 == count.get(); }
    public void setToMax() { count.set(max); }
    public void setToZero() { count.set(0); }

    public boolean decrement() {
        int currentCount = count.get();
        if (0 >= currentCount) { return false; }
        return count.compareAndSet(currentCount, currentCount - 1);
    }

    public boolean increment() {
        int currentCount = count.get();
        if (max <= currentCount) { return false; }
        return count.compareAndSet(currentCount, currentCount + 1);
    }
}
