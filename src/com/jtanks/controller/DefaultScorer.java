package com.jtanks.controller;

import java.util.Collection;

import com.jtanks.model.Tank;

public class DefaultScorer implements Scorer {
    public void updateScoreForMissileHit(Tank tank) { 
        tank.addToScore(1);
    }

    public void updateScoreForEndOfRound(Collection<Tank> tanks) {
        for (Tank tank : tanks) {
            int numberOfTanksOutlived = tanksOutlived(tank, tanks);
            tank.addToScore(numberOfTanksOutlived);
        }
    }

    private int tanksOutlived(Tank inputTank, Collection<Tank> tanks) {
        int numberOutlived = 0;
        for (Tank tank : tanks) {
            if (inputTank.outlived(tank)) { numberOutlived++; }
        }
        return numberOutlived;
    }
} 
