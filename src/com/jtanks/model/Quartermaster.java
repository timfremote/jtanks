package com.jtanks.model;

public interface Quartermaster extends TankStockist, MineStockist, MissileStockist, ExplosionStockist {
    void resetForNewRound();
}
