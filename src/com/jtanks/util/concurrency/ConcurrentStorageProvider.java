package com.jtanks.util.concurrency;

import com.jtanks.util.Point;

import java.util.Queue;
import java.util.Set;

public interface ConcurrentStorageProvider {
    AtomicDouble makeAtomicDouble();
    AtomicInteger makeAtomicInteger();
    AtomicCounter makeAtomicCounter(int max);

    ConsistentBoolean makeConsistentBoolean();
    ConsistentDouble makeConsistentDouble();
    ConsistentInteger makeConsistentInteger();
    ConsistentLong makeConsistentLong();
    ConsistentPoint makeConsistentPoint(Point p);

    <T> Set<T> makeImmutableSet(Set<T> s);
    
    <T> Set<T> makeConcurrentUpdateSet(Class<T> c);
    <T> Queue<T> makeConsistentQueue();
}
