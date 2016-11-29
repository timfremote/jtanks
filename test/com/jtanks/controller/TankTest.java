package com.jtanks.controller;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.model.TestClock;
import com.jtanks.util.Point;

//FIXME: Add to concurrency test
public class TankTest {
    private static final double HEADING_FOR_345_TRIANGLE = 0.927295218002;
    private static final int DUE_EAST = 0;
    private static final double CANNON_LENGTH = 5;
    private static final double MINE_TUBE_LENGTH = 5d;
    private static final String NAME = "name";
    private static final double MAXIMUM_SPEED = 50d;
    private static final int MAXIMUM_HEALTH = 150;
    private static final int MAXIMUM_MINES = 2;
    private static final int MAXIMUM_MISSILES = 3;
    private static final long START_TIME = 1000000000;
    private static final Color COLOR = Color.YELLOW;
    private static final double SIZE = 5d;

    private static int ARENA_SIZE = 300;
    private static final double RANDOM_X = 32.122;
    private static final double RANDOM_Y = 50.24;
    private static final Point randomPoint = new Point(RANDOM_X, RANDOM_Y);
    private static final double RANDOM_HEADING = 1.234;
    private static final double[] RANDOM_VALUES = {RANDOM_X / ARENA_SIZE, RANDOM_Y / ARENA_SIZE,
                                                   RANDOM_HEADING / (2d * Math.PI)};
    private static final Point POSITION = new Point(1.23, 2.34);
    private static final Point POINT_ON_EDGE = new Point(2.73, 4.34);
    private static final int TRAIL_LENGTH = 200;
    private static final Point ORIGIN = new Point(0, 0);
	
    private TestClock clock = new TestClock();
    private GameConfiguration config;
    @SuppressWarnings("serial")
    private Random random = new Random() {
        private int index = 0;
        @Override
        public double nextDouble() {
            if (RANDOM_VALUES.length == index) { index = 0; }
            return RANDOM_VALUES[index++];
        }
    };
    private Tank tank, tank2, tank3, tank4, tank5;
    
	@Before public void setUp() {
	    config = new GameConfiguration();
	    config.virtualArenaSize = ARENA_SIZE;
	    config.missileCapacity = MAXIMUM_MISSILES;
	    config.mineCapacity = MAXIMUM_MINES;
	    config.maximumSpeed = MAXIMUM_SPEED;
	    config.maximumHealth = MAXIMUM_HEALTH;
	    config.cannonLength = CANNON_LENGTH;
	    config.mineTubeLength = MINE_TUBE_LENGTH;
	    
	    clock.currentTime = START_TIME;

	    tank = makeTank(COLOR);
	    tank2 = makeTank(Color.BLUE);
        tank3 = makeTank(Color.RED);
        tank4 = makeTank(Color.PINK);
        tank5 = makeTank(Color.CYAN);
	}

    private Tank makeTank(Color color) {
        return new Tank(color, clock, config, random);
    }
	
    @Test public void hasFixedColorAndSize() {
        assertEquals(COLOR, tank.getColor());
        assertEquals(SIZE, tank.getSize());
    }
    
    @Test public void startsWithCorrectInitialState() {
        assertEquals(MAXIMUM_MISSILES, tank.getMissileLocker().count());
        assertEquals(MAXIMUM_MINES, tank.getMineLocker().count());
        assertEquals(MAXIMUM_HEALTH, tank.getHealth());
        assertEquals(0, tank.getScore());
        assertEquals(ORIGIN, tank.getPosition());
        assertEquals(0d, tank.getSpeed());
        assertFalse(tank.isDestroyed());
        assertEquals(0d, tank.getHeading());
    }
    
    @Test public void resetRestoresState() {
        tank.getMissileLocker().removeAll();
        tank.getMineLocker().removeAll();
        tank.damage(50);
        tank.addToScore(10);
        tank.setPosition(new Point(0, 0));
        tank.setSpeedAsPercentage(100);
        tank.destroy();
        clock.currentTime = START_TIME + 60000;
        tank.reset();
        
        assertEquals(MAXIMUM_MISSILES, tank.getMissileLocker().count());
        assertEquals(MAXIMUM_MINES, tank.getMineLocker().count());
        assertEquals(MAXIMUM_HEALTH, tank.getHealth());
        assertEquals(10, tank.getScore());
        assertEquals(randomPoint, tank.getPosition());
        assertEquals(0d, tank.getSpeed());
        assertFalse(tank.isDestroyed());
        assertEquals(START_TIME + 60000, tank.lastMovedAt());
        assertEquals(RANDOM_HEADING, tank.getHeading());
    }
    
    @Test public void setsAndReadsSpeedAndTrimsExtremeValues() {
        tank.setSpeedAsPercentage(100d);
        assertEquals(MAXIMUM_SPEED, tank.getSpeed());
        tank.setSpeedAsPercentage(50d);
        assertEquals(MAXIMUM_SPEED / 2, tank.getSpeed());
        tank.setSpeedAsPercentage(0d);
        assertEquals(0d, tank.getSpeed());
        tank.setSpeedAsPercentage(-1d);
        assertEquals(0d, tank.getSpeed());
        tank.setSpeedAsPercentage(200d);
        assertEquals(MAXIMUM_SPEED, tank.getSpeed());
    }
    
    @Test public void takesDamageAndReportsHealth() {
        tank.damage(10);
        assertEquals(MAXIMUM_HEALTH - 10, tank.getHealth());
        tank.damage(10);
        assertEquals(MAXIMUM_HEALTH - 20, tank.getHealth());
    }
    
    @Test public void destroysTankWhenHealthReachesZeroAndTakesNoMoreDamage() {
        tank.damage(MAXIMUM_HEALTH);
        assertEquals(0, tank.getHealth());
        assertTrue(tank.isDestroyed());
        tank.damage(1);
        assertEquals(0, tank.getHealth());
        tank.reset();
        tank.damage(MAXIMUM_HEALTH + 1);
        assertEquals(0, tank.getHealth());
        assertTrue(tank.isDestroyed());
    }
    
    @Test public void setsAndReadsHeading() {
        tank.setHeading(Math.PI);
        assertEquals(Math.PI, tank.getHeading());
        tank.setHeading(Math.PI / 2d);
        assertEquals(Math.PI / 2d, tank.getHeading());
    }
    
    @Test public void setsNameOnce() {
        tank.setName(NAME);
        assertEquals(NAME, tank.getName());
        tank.setName("another name");
        assertEquals(NAME, tank.getName());
    }
    
    @Test public void setsPosition() {
        tank.setPosition(POSITION);
        assertEquals(POSITION, tank.getPosition());
    }
    
    @Test public void updatesTrailWhenMovedAndLimitsLengthOfTrail() {
        for (int i = 0; i < TRAIL_LENGTH; i++) {
            Point position = new Point(POSITION.getX() + (i * 0.001), POSITION.getY());
            tank.setPosition(position);
        }
        List<Point> trail = tank.getTrail();
        assertEquals(TRAIL_LENGTH, trail.size());
        
        Iterator<Point> iter = trail.iterator();
        for (int i = 0; i < TRAIL_LENGTH; i++) {
            Point position = new Point(POSITION.getX() + (i * 0.001), POSITION.getY());
            assertEquals(position, iter.next());
        }
        
        tank.setPosition(randomPoint);
        Iterator<Point> iter2 = trail.iterator();
        for (int i = 1; i < TRAIL_LENGTH; i++) {
            Point position = new Point(POSITION.getX() + (i * 0.001), POSITION.getY());
            assertEquals(position, iter2.next());
        }
        assertEquals(randomPoint, iter2.next());
    }

    @Test public void recordsLastMovedTime() {
        clock.currentTime = START_TIME;
        tank.setPosition(POSITION);
        assertEquals(START_TIME, tank.lastMovedAt());
        clock.currentTime = START_TIME + 1000;
        tank.setPosition(POSITION);
        assertEquals(START_TIME + 1000, tank.lastMovedAt());
    }
    
    @Test public void disappearsWhenDestroyed() {
        assertFalse(tank.isDestroyed());
        tank.destroy();
        assertTrue(tank.isDestroyed());
    }
    
	@Test public void saysWhetherThisTankOutlivedAnotherOne() {
	    tank.destroy();
	    clock.currentTime += 1;
	    tank2.destroy();
	    tank3.destroy();
	    assertTrue("Later destroyed time should mean outlived() returns true", tank2.outlived(tank));
        assertFalse("Earlier destroyed time should mean outlived() returns false", tank.outlived(tank2));
        assertFalse("Equal destroyed times should mean outlived() returns false", tank3.outlived(tank2));
        assertTrue("Live tank should outlive destroyed tank", tank4.outlived(tank3));
        assertFalse("Two live tanks should not outlive each other", tank4.outlived(tank5));
        assertFalse("Two live tanks should not outlive each other", tank5.outlived(tank4));
        
        tank.reset();
        tank2.reset();

        assertFalse("Reset tanks should be alive, therefore not outlive each other", tank2.outlived(tank));
        assertFalse("Reset tanks should be alive, therefore not outlive each other", tank.outlived(tank2));
	}
	
	@Test public void addsToScore() {
        tank.addToScore(5);
        assertEquals(5, tank.getScore());
        tank.addToScore(5);
        assertEquals(10, tank.getScore());
	}
	
	@Test public void comparesTanksByName() {
	    tank.setName("Abercrombie");
	    tank2.setName("Baker");
	    assertTrue("Should put tanks in alphabetical order", tank.compareTo(tank2) < 0);
	}
	
	@Test public void detectsPointCollisions() {
	    tank.setPosition(POSITION);
	    assertFalse(tank.pointIsInsideBody(new Point(99, 99)));
        assertTrue(tank.pointIsInsideBody(POSITION));
        assertTrue(tank.pointIsInsideBody(POINT_ON_EDGE));
	}
	
	@Test public void locatesEndOfCannon() {
        tank.setPosition(POSITION);
        tank.setHeading(DUE_EAST);
        assertPointsApproximatelyEqual(new Point(POSITION.getX() + CANNON_LENGTH, POSITION.getY()), 
                                       tank.findEndOfCannon());
        
        tank.setHeading(HEADING_FOR_345_TRIANGLE);
        assertPointsApproximatelyEqual(new Point(POSITION.getX() + 3, POSITION.getY() - 4), 
                                       tank.findEndOfCannon());
	}
	
	@Test public void locatesEndOfMineTube() {
	    tank.setPosition(POSITION);
        tank.setHeading(DUE_EAST);
        assertPointsApproximatelyEqual(new Point(POSITION.getX() - CANNON_LENGTH, POSITION.getY()), 
                                       tank.findEndOfMineTube());

        tank.setHeading(HEADING_FOR_345_TRIANGLE);
        assertPointsApproximatelyEqual(new Point(POSITION.getX() - 3, POSITION.getY() + 4), 
                                       tank.findEndOfMineTube());
	}

	private static final double DELTA = 0.0001;
    private void assertPointsApproximatelyEqual(Point point1, Point point2) {
        assertEquals(point1.getX(), point2.getX(), DELTA);
        assertEquals(point1.getY(),  point2.getY(), DELTA);
    }
}
