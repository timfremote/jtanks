package com.jtanks.util.concurrency;

import com.jtanks.util.Point;

// FIXME: Use for Tank and Missile positions
/**
 * Implementations of this interface provide consistent reads and updates of a Point. That is,
 * if one thread updates the value stored here, and another thread later reads the value, the reader is
 * guaranteed to see the value written by the writer (and not a partially updated value, for instance). 
 * However, access is not atomic - that is, if one thread reads the value, then later changes the value, 
 * there is nothing to prevent another thread from changing the value between the read and the write.
 * @author dsquirre
 */
public interface ConsistentPoint {
    Point get();
    void set(Point x);
}
