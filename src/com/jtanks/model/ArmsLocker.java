package com.jtanks.model;

import com.jtanks.util.Clock;
import com.jtanks.util.concurrency.AtomicCounter;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentLong;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

public class ArmsLocker {
    private final Clock clock;
    private final AtomicCounter stock;
    private final ConsistentLong lastDepositTime;

    public ArmsLocker(int capacity, Clock clock) {
        this(capacity, clock, new JVMConcurrentStorageProvider());
    }

    public ArmsLocker(int capacity, Clock clock, ConcurrentStorageProvider storageProvider) {
        this.clock = clock;
        stock = storageProvider.makeAtomicCounter(capacity);
        lastDepositTime = storageProvider.makeConsistentLong();
        restock();
    }

    public int count() { return stock.getCount(); }
    public boolean isEmpty() { return stock.isZero(); }
    public boolean isFull() { return stock.isMax(); }
    public void removeAll() { stock.setToZero(); }
    
    public void removeItem() { 
        if (!stock.decrement()) {
            System.out.println("Contention in thread " + Thread.currentThread().getName() + ". " 
                             + "Not removing item from ArmsLocker.");
        }
    }
    
    public void depositItem() { 
        if (stock.increment()) {
            updateLastDepositTime();
        } else {
            System.out.println("Contention in thread " + Thread.currentThread().getName() + ". " 
                             + "Not adding item to ArmsLocker.");
        }
    }

    public void restock() { 
        stock.setToMax();
        updateLastDepositTime();
    }

    public long getTimeSinceLastDeposit() {
        return clock.getCurrentTime() - lastDepositTime.get();
    }

    private void updateLastDepositTime() {
        lastDepositTime.set(clock.getCurrentTime());
    }
}
