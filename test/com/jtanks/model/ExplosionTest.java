package com.jtanks.model;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.Point;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ExplosionTest {
    private static final long START_TIME  = 1000000000;
    private static final Color COLOR = new Color(255, 100, 255);
    private static final int LIFETIME = 500;
    private static final double START_SIZE = 10d;
    private static final double X = 31.41;
    private static final double Y = 2.58;
    
    private TestClock clock;
    private Point position;
    private Explosion explosion;
    
    @Before public void setUp() {
        clock = new TestClock();
        clock.currentTime = START_TIME;
        position = new Point(X, Y);
        explosion = new Explosion(position, clock);
    }

    @Test public void isPurple() {
        assertEquals(COLOR, explosion.getColor());
    }
    
    @Test public void hasStringRepresentation() {
        String representation = "Explosion at " + position.toString();
        assertEquals(representation, explosion.toString());
    }
    
    @Test public void staysInOnePlace() {
        assertEquals(position, explosion.getPosition());
        clock.currentTime += 1;
        assertEquals(position, explosion.getPosition());
        clock.currentTime += 1000;
        assertEquals(position, explosion.getPosition());
        clock.currentTime += 1000000;
        assertEquals(position, explosion.getPosition());
    }
    
    @Test public void destroysItselfWhenDoneExploding() {
        assertFalse(explosion.isDestroyed());
        clock.currentTime = START_TIME + 1;
        assertFalse(explosion.isDestroyed());
        clock.currentTime = START_TIME + 100;
        assertFalse(explosion.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME - 1;
        assertFalse(explosion.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME;
        assertTrue(explosion.isDestroyed());
        clock.currentTime = START_TIME + LIFETIME + 100;
        assertTrue(explosion.isDestroyed());
        clock.currentTime = START_TIME + 100000;
        assertTrue(explosion.isDestroyed());
    }
    
    @Test public void shrinksDuringLifetime() {
        assertEquals(START_SIZE, explosion.getSize());
        double previousSize = START_SIZE;
        for (long timeElapsed = 10; timeElapsed < LIFETIME; timeElapsed += 10) {
            clock.currentTime = START_TIME + timeElapsed;
            double currentSize = explosion.getSize();
            assertTrue(String.format("Should shrink over time: time elapsed is %d, previous size is %f, current size is %f",
                                     timeElapsed, previousSize, currentSize), 
                       currentSize < previousSize);
            previousSize = currentSize;
        }
    }
}
