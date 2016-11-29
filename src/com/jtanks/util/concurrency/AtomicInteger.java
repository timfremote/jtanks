package com.jtanks.util.concurrency;

// FIXME: Use in ArmsLocker, gamesRemaining, Tank score and health.
/**
 * Implementations of this class provide atomic access to an integer value. This access is consistent
 * (as explained in ConsistentInteger) as well as atomic - a thread can read the value and update it in one
 * atomic operation, using the atomicXXX() methods, and no other thread will update the value during the
 * operation. A thread can also use the compareAndSet method to ensure, when writing a new value, that the 
 * value has not changed since it was last read.
 * 
 * The JVM provides the canonical implementation in its
 * java.util.concurrent.atomic.AtomicInteger class; we provide this interface to allow testing. We also
 * simplify by ignoring the value returned by the JVM getAndXXX() methods, since we don't care what the
 * previous value was (as long as our update goes through consistently).
 * @author dsquirre
 */
public interface AtomicInteger extends ConsistentInteger {
    void atomicIncrement();
    void atomicDecrement();
    void atomicAdd(int x);
    void atomicSubtract(int x);
    boolean compareAndSet(int expected, int update);
}
