package com.jtanks.util.concurrency;

// FIXME: Repeated Javadocs should be in package description
/**
 * Implementations of this interface provide consistent reads and updates of a double value. That is,
 * if one thread updates the value stored here, and another thread later reads the value, the reader is
 * guaranteed to see the value written by the writer. However, access is not atomic - that is, if one thread
 * reads the value, then later changes the value, there is nothing to prevent another thread from changing
 * the value between the read and the write. (This includes increment and decrement operations as well as
 * operators like +=, -=, and so on - these are a single Java statement but involve a read followed by a
 * write.)
 * @author dsquirre
 */
public interface ConsistentDouble {
    double get();
    void set(double x);
}
