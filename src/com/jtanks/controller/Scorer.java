package com.jtanks.controller;

import java.util.Collection;

import com.jtanks.model.Tank;

public interface Scorer { 
    void updateScoreForMissileHit(Tank t);
    void updateScoreForEndOfRound(Collection<Tank> tanks);
}
