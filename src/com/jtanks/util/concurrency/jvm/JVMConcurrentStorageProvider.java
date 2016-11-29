package com.jtanks.util.concurrency.jvm;

import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import com.jtanks.util.Point;
import com.jtanks.util.concurrency.AtomicCounter;
import com.jtanks.util.concurrency.AtomicDouble;
import com.jtanks.util.concurrency.AtomicInteger;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentBoolean;
import com.jtanks.util.concurrency.ConsistentDouble;
import com.jtanks.util.concurrency.ConsistentInteger;
import com.jtanks.util.concurrency.ConsistentLong;
import com.jtanks.util.concurrency.ConsistentPoint;

public class JVMConcurrentStorageProvider implements ConcurrentStorageProvider {
    public AtomicDouble makeAtomicDouble() { return new JVMAtomicDouble(); }
    public AtomicInteger makeAtomicInteger() { return new JVMAtomicInteger(); }
    public AtomicCounter makeAtomicCounter(int max) { return new JVMAtomicCounter(max); }
    public <T> Set<T> makeConcurrentUpdateSet(Class<T> c) { return new CopyOnWriteArraySet<T>(); }
    public ConsistentBoolean makeConsistentBoolean() { return new VolatileBoolean(); }
    public ConsistentDouble makeConsistentDouble() { return new VolatileDouble(); }
    public ConsistentInteger makeConsistentInteger() { return new VolatileInteger(); }
    public ConsistentLong makeConsistentLong() { return new VolatileLong(); }
    public ConsistentPoint makeConsistentPoint(Point p) { return new VolatilePoint(p); }
    public <T> Queue<T> makeConsistentQueue() { return new ConcurrentLinkedQueue<T>(); }
    public <T> Set<T> makeImmutableSet(Set<T> s) { return Collections.unmodifiableSet(s); }
}
