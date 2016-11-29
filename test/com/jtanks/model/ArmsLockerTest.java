package com.jtanks.model;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.concurrency.AtomicCounter;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentLong;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

// FIXME: Concurrency test
public class ArmsLockerTest {
    private static final int CAPACITY = 3;
    private static final long START_TIME          = 1000000000;
    private static final long FIRST_DEPOSIT_TIME  = 1000030000;
    private static final long SECOND_DEPOSIT_TIME = 1000060000;
    private static final long THIRD_DEPOSIT_TIME  = 1000075000;
    
    private ArmsLocker locker;
    private TestClock clock = new TestClock();
    
    @Before public void setUp() {
        locker = new ArmsLocker(CAPACITY, clock);
    }
    
    @Test public void isFullToStart() {
        assertCountIs(CAPACITY);
    }
    
    @Test public void removeAllEmptiesLocker() {
        locker.removeAll();
        assertCountIs(0);
    }
    
    @Test public void restockFillsLocker() {
        locker.removeAll();
        assertCountIs(0);
        locker.restock();
        assertCountIs(CAPACITY);
    }

    @Test public void removingItemDecrementsCount() {
        locker.removeItem();
        assertCountIs(CAPACITY - 1);
    }
    
    @Test public void addingItemIncrementsCount() {
        locker.removeItem();
        assertCountIs(CAPACITY - 1);
        locker.depositItem();
        assertCountIs(CAPACITY);
    }
    
    @Test public void reportsWhenFull() {
        assertTrue(locker.isFull());
        locker.removeItem();
        assertFalse(locker.isFull());
    }
    
    @Test public void reportsWhenEmpty() {
        assertFalse(locker.isEmpty());
        locker.removeAll();
        assertTrue(locker.isEmpty());
        locker.depositItem();
        assertFalse(locker.isEmpty());
    }

    @Test public void cannotRemoveWhenEmpty() {
        locker.removeAll();
        locker.removeItem();
        assertCountIs(0);
    }

    @Test public void cannotAddWhenFull() {
        locker.depositItem();
        assertCountIs(CAPACITY);
    }
    
    @Test public void reportsTimeSinceLastDeposit() {
        locker.removeAll();
        clock.currentTime = FIRST_DEPOSIT_TIME;
        locker.depositItem();
        assertEquals(0, locker.getTimeSinceLastDeposit());
        clock.currentTime += 1000;
        locker.removeItem();
        assertEquals(1000, locker.getTimeSinceLastDeposit());
        clock.currentTime = SECOND_DEPOSIT_TIME;
        locker.depositItem();
        assertEquals(0, locker.getTimeSinceLastDeposit());
        clock.currentTime = THIRD_DEPOSIT_TIME;
        locker.restock();
        clock.currentTime += 2000;
        assertEquals(2000, locker.getTimeSinceLastDeposit());
    }
    
    @Test public void usesAppropriateThreadsafeStorage() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final AtomicCounter counter = context.mock(AtomicCounter.class);
        final ConsistentLong time = context.mock(ConsistentLong.class);
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeAtomicCounter(CAPACITY); will(returnValue(counter));
            oneOf(storageProvider).makeConsistentLong(); will(returnValue(time));
            oneOf(counter).setToMax();
            oneOf(time).set(START_TIME);
        }});
        clock.currentTime = START_TIME;
        ArmsLocker locker = new ArmsLocker(CAPACITY, clock, storageProvider);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(counter).decrement(); will(returnValue(true));
        }});
        locker.removeItem();
        context.assertIsSatisfied();
        
        context.checking(new Expectations() {{
            oneOf(counter).increment(); will(returnValue(true));
            oneOf(time).set(FIRST_DEPOSIT_TIME);
        }});
        clock.currentTime = FIRST_DEPOSIT_TIME;
        locker.depositItem();
        context.assertIsSatisfied();
    }
    
    // FIXME: Should test the logging here too
    @Test public void logsAndIgnoresUpdatesIfContentionOccurs() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final AtomicCounter counter = context.mock(AtomicCounter.class);
        final ConsistentLong time = context.mock(ConsistentLong.class);
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeAtomicCounter(CAPACITY); will(returnValue(counter));
            oneOf(storageProvider).makeConsistentLong(); will(returnValue(time));
            oneOf(counter).setToMax();
            oneOf(time).set(START_TIME);
        }});
        clock.currentTime = START_TIME;
        ArmsLocker locker = new ArmsLocker(CAPACITY, clock, storageProvider);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(counter).decrement(); will(returnValue(false));
        }});
        locker.removeItem();
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(counter).increment(); will(returnValue(false));
        }});
        clock.currentTime = FIRST_DEPOSIT_TIME;
        locker.depositItem();
        context.assertIsSatisfied();
    }
    
    private void assertCountIs(int expectedCount) {
        assertEquals(expectedCount, locker.count());
    }
}
