package com.jtanks.concurrency;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ConcurrencySoakTest {
    private static final int NUMBER_OF_ITERATIONS = 100;
    private static final int NUMBER_OF_THREADS = 5;
    private static final Object EXPECTED_SCORE = NUMBER_OF_ITERATIONS * NUMBER_OF_THREADS;   
    private static final int MAX_SLEEP_TIME_IN_MILLIS = 50;
    private static final long MAX_JOIN_TIME = 60000;

    private Tank tank;
    private List<Thread> threads = new ArrayList<Thread>();
    
    @Before public void setUp() {
        tank = new Tank(Color.RED, new GameConfiguration());
    }

    @Test public void canConsistentlyUpdateScoreInMultipleThreads() {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            startScoreIncrementingThread(i);
        }
        waitForAllThreads();
        assertEquals(EXPECTED_SCORE, tank.getScore());
    }

    private void startScoreIncrementingThread(final int index) {
        startIteratingThread(new Runnable() {
            public void run() {
                System.out.println("Incrementer " + index + " is incrementing tank score");
                tank.addToScore(1);
            }
        });
    }

    private void startIteratingThread(final Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                    runnable.run();
                    relax();
                }
            }
        };
        threads.add(thread);
        thread.start();
    }

    private void waitForAllThreads() {
        for (Thread thread : threads) {
            joinThread(thread);
        }
    }

    private void joinThread(Thread thread) {
        try {
            thread.join(MAX_JOIN_TIME);
        } catch (InterruptedException e) {
            fail("Thread unexpectedly interrupted during joinThread()");
        }
    }

    private void relax() {
        try {
            int timeToSleepInMillis = getRandomInteger(MAX_SLEEP_TIME_IN_MILLIS);
            Thread.sleep(timeToSleepInMillis);
        } catch (InterruptedException e) {
            fail("Thread unexpectedly interrupted during relax()");
        }
    }

    private int getRandomInteger(int max) {
        return (int) Math.floor(Math.random() * (double)max);
    }
}
