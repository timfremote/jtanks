package com.jtanks.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.util.Point;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentBoolean;
import com.jtanks.util.concurrency.ConsistentDouble;
import com.jtanks.util.concurrency.ConsistentLong;
import com.jtanks.util.concurrency.ConsistentPoint;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

public class MissileTest {
    private static final Color RED = Color.RED;
    private static final Color BLUE = Color.BLUE;
    private static final Point ONE_POSITION = new Point(1.23, 5.678);
    private static final Point ANOTHER_POSITION = new Point(76.5, 2.333);
    private static final double ONE_HEADING = 4.565;
    private static final double ANOTHER_HEADING = 9.998;
    private static final Tank ONE_TANK = new Tank(RED, new GameConfiguration());
    private static final Tank ANOTHER_TANK = new Tank(BLUE, new GameConfiguration());
    private static final double SIZE = 2d;
    private static final long START_TIME = 1000000000;
    private static final double SPEED = 100d;
    private static final int LIFETIME = 3000;
    
    private Missile missile;
    private TestClock clock = new TestClock();
    private GameConfiguration config;
    
    @Before public void setUp() {
        config = new GameConfiguration();
        config.missileSpeed = SPEED;
        config.missileLifetimeInMillis = LIFETIME;
        
        clock.currentTime = START_TIME;
        missile = new Missile(RED, ONE_POSITION, ONE_HEADING, ONE_TANK, 
                              clock, config, new JVMConcurrentStorageProvider());
    }

    @Test public void storesInitialData() {
        checkInitialData(RED, ONE_POSITION, ONE_HEADING, ONE_TANK);
        checkInitialData(BLUE, ANOTHER_POSITION, ANOTHER_HEADING, ANOTHER_TANK);
    }
    
    @Test public void hasConstantSizeAndSpeed() {
        checkSizeAndSpeed();
        clock.currentTime += 1000;
        checkSizeAndSpeed();
        clock.currentTime += 60000;
        checkSizeAndSpeed();
    }

    private void checkSizeAndSpeed() {
        assertEquals(SIZE, missile.getSize());
        assertEquals(SPEED, missile.getSpeed());
    }
    
    @Test public void disappearsWhenDestroyed() {
        assertFalse(missile.isDestroyed());
        missile.destroy();
        assertTrue(missile.isDestroyed());
    }
    
    @Test public void selfDestructsAfterLifetimeExpires() {
        assertFalse("Should exist during lifetime", missile.isDestroyed());
        clock.currentTime = START_TIME + 1;
        assertFalse("Should exist during lifetime", missile.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME - 1;
        assertFalse("Should exist during lifetime", missile.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME;
        assertTrue("Should be gone after lifetime", missile.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME + 1;
        assertTrue("Should be gone after lifetime", missile.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME + 100000;
        assertTrue("Should be gone after lifetime", missile.isDestroyed());
    }
    
    @Test public void canBeMovedAndRedirected() {
        missile.setPosition(ANOTHER_POSITION);
        missile.setHeading(ANOTHER_HEADING);
        checkPositionAndHeading(missile, ANOTHER_POSITION, ANOTHER_HEADING);
    }
    
    @Test public void reportsTimeOfLastMove() {
        assertEquals(START_TIME, missile.lastMovedAt());
        clock.currentTime = START_TIME + 1;
        assertEquals(START_TIME, missile.lastMovedAt());
        missile.setPosition(ANOTHER_POSITION);
        assertEquals(START_TIME + 1, missile.lastMovedAt());
    }
    
    @Test public void usesAppropriateThreadsafeStorage() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final ConsistentDouble heading = context.mock(ConsistentDouble.class);
        final ConsistentPoint position = context.mock(ConsistentPoint.class);
        final ConsistentLong lastMoved = context.mock(ConsistentLong.class);
        final ConsistentBoolean destroyed = context.mock(ConsistentBoolean.class);
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeConsistentDouble(); will(returnValue(heading));
            oneOf(storageProvider).makeConsistentPoint(ONE_POSITION); will(returnValue(position));
            oneOf(storageProvider).makeConsistentLong(); will(returnValue(lastMoved));
            oneOf(storageProvider).makeConsistentBoolean(); will(returnValue(destroyed));
            oneOf(heading).set(ONE_HEADING);
            oneOf(lastMoved).set(START_TIME);
        }});
        clock.currentTime = START_TIME;
        Missile missile = new Missile(RED, ONE_POSITION, ONE_HEADING, ONE_TANK, clock, config, storageProvider);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(heading).set(ANOTHER_HEADING);
        }});
        missile.setHeading(ANOTHER_HEADING);
        context.assertIsSatisfied();
        
        context.checking(new Expectations() {{
            oneOf(position).set(ANOTHER_POSITION);
            oneOf(lastMoved).set(START_TIME + 1);
        }});
        clock.currentTime = START_TIME + 1;
        missile.setPosition(ANOTHER_POSITION);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(destroyed).set(true);
        }});
        missile.destroy();
        context.assertIsSatisfied();
    }
    
    private void checkInitialData(Color color, Point position, double heading, Tank creator) {
        Missile missile = new Missile(color, position, heading, creator, config);
        checkPositionAndHeading(missile, position, heading);
        assertEquals(color, missile.getColor());
        assertEquals(creator, missile.getCreator());
    }

    private void checkPositionAndHeading(Missile missile, Point position, double heading) {
        assertEquals(position, missile.getPosition());
        assertEquals(heading, missile.getHeading());
    }
}
