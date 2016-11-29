package com.jtanks.model;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.Point;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentBoolean;

import static junit.framework.Assert.assertEquals;

public class MineTest {
    private static final double SIZE = 4d;
    private static final Color RED = Color.RED;
    private static final Color BLUE = Color.BLUE;
    private static final Point ONE_POSITION = new Point(3.14, 2.58);
    private static final Point ANOTHER_POSITION = new Point(3.66, 2.931);
    private Mine mine;

    @Before public void setUp() {
        mine = new Mine(Color.RED, ONE_POSITION);
    }
    
    @Test public void storesColorAndPosition() {
        checkColorAndPosition(RED, ONE_POSITION);
        checkColorAndPosition(BLUE, ANOTHER_POSITION);
    }
    
    @Test public void hasExpectedSize() {
        assertEquals(SIZE, mine.getSize());
    }
    
    @Test public void disappearsWhenDestroyed() {
        assertEquals(false, mine.isDestroyed());
        mine.destroy();
        assertEquals(true, mine.isDestroyed());
    }

    private void checkColorAndPosition(Color color, Point position) {
        Mine mine = new Mine(color, position);
        assertEquals(color, mine.getColor());
        assertEquals(position, mine.getPosition());
    }
    
    @Test public void usesAppropriateThreadsafeStorage() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final ConsistentBoolean destroyed = context.mock(ConsistentBoolean.class);
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeConsistentBoolean(); will(returnValue(destroyed));
            oneOf(destroyed).set(false);
        }});
        mine = new Mine(Color.RED, ONE_POSITION, storageProvider);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(destroyed).set(true);
        }});
        mine.destroy();

        context.assertIsSatisfied();
    }
}
