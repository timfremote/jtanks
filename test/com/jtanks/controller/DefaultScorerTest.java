package com.jtanks.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.model.TestClock;

import static junit.framework.Assert.assertEquals;

public class DefaultScorerTest {

    private static final Color TANK_COLOR = Color.RED;
    
    private final Scorer scorer = new DefaultScorer();
    private final List<Tank> tanks = new ArrayList<Tank>();

    private TestClock clock = new TestClock();
    
    @Before public void setupTanks() {
        GameConfiguration config = new GameConfiguration();
        for (int i = 0; i < 5; i++) {
            tanks.add(new Tank(TANK_COLOR, clock, config));
        }
    }

    @Test public void addsOneToScoreForMissileHit() {
        Tank tank = tanks.get(0);
        tank.addToScore(10);
        scorer.updateScoreForMissileHit(tank);
        assertEquals(11, tank.getScore());
    }
    
    @Test public void scoresOnePointForEachTankOutlived() {
        setTankAges(0, 8, 6, 3, 14);
        scorer.updateScoreForEndOfRound(tanks);
        checkTankScores(0, 3, 2, 1, 4);
    }
    
    @Test public void awardsIdenticalScoresForTanksThatDieAtTheSameTime() {
        setTankAges(10, 8, 8, 3, 12);
        scorer.updateScoreForEndOfRound(tanks);
        checkTankScores(3, 1, 1, 0, 4);
    }

    private void setTankAges(int... ages) {
        for (int time = 0; time < max(ages); time++) {
            clock.currentTime = time;
            for (int i = 0; i < ages.length; i++) {
                int age = ages[i];
                if (age == time) {
                    tanks.get(i).destroy();
                }
            }
        }
    }
    
    private int max(int[] array) {
        int[] copy = array.clone();
        Arrays.sort(copy);
        return copy[copy.length - 1];
    }

    private void checkTankScores(int... scores) {
        for (int i = 0; i < scores.length; i++) {
            int score = scores[i];
            Tank tank = tanks.get(i);
            assertEquals(score, tank.getScore());
        }
    }
}
