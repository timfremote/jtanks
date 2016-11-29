package com.jtanks.util.concurrency;

// FIXME: Use for Tank speed and heading, and for Missile heading. 
/**
 * Implementations of this class provide atomic access to a double value. This access is consistent
 * (as explained in ConsistentDouble) as well as atomic - a thread can use the compareAndSet() method
 * to ensure that when it is updating an AtomicDouble, the value it read has not changed since read time.
 *
 * Strangely, the JVM's java.util.concurrent.atomic package does not provide a canonical implementation of
 * this interface.
 * @author dsquirre
 */
public interface AtomicDouble extends ConsistentDouble {
    boolean compareAndSet(double expected, double update);
}
