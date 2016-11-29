package com.jtanks.util.concurrency;

// FIXME: Use for Missile, Mine, and Tank destroyed members.
/**
 * Implementations of this interface provide consistent reads and updates of a boolean value. That is,
 * if one thread updates the value stored here, and another thread later reads the value, the reader is
 * guaranteed to see the value written by the writer. However, access is not atomic - that is, if one thread
 * reads the value, then later changes the value, there is nothing to prevent another thread from changing
 * the value between the read and the write.
 * @author dsquirre
 */
public interface ConsistentBoolean {
    boolean get();
    void set(boolean b);
}
