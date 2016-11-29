package com.jtanks.model;

import java.util.Set;

import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

public class ConcurrentQuartermaster implements Quartermaster {
    private final Set<Explosion> explosions;
    private final Set<Mine> mines;
    private final Set<Missile> missiles;
    private final Set<Tank> tanks;

    public ConcurrentQuartermaster(Set<Tank> tanks) {
        this(tanks, new JVMConcurrentStorageProvider());
    }

    public ConcurrentQuartermaster(Set<Tank> tanks, ConcurrentStorageProvider storageProvider) {
        explosions = storageProvider.makeConcurrentUpdateSet(Explosion.class);
        mines = storageProvider.makeConcurrentUpdateSet(Mine.class);
        missiles = storageProvider.makeConcurrentUpdateSet(Missile.class);
        this.tanks = storageProvider.makeImmutableSet(tanks);
    }

    public Set<Explosion> getExplosions() { return explosions; }
    public Set<Mine> getMines() { return mines;}
    public Set<Missile> getMissiles() { return missiles; }
    public Set<Tank> getTanks() { return tanks; }

    public void resetForNewRound() {
        explosions.removeAll(explosions);
        mines.removeAll(mines);
        missiles.removeAll(missiles);
    }
}
