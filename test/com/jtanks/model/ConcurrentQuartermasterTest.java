package com.jtanks.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.util.Point;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;

import static junit.framework.Assert.assertEquals;

public class ConcurrentQuartermasterTest {
    private ConcurrentQuartermaster quartermaster;
    private Explosion explosion;
    private Mine mine;
    private Missile missile;
    private Set<Tank> tanks;

    @Before public void setUp() {
        explosion = new Explosion(new Point(0, 0));
        mine = new Mine(Color.RED, new Point(0, 0));
        missile = new Missile(Color.RED, new Point(0, 0), 0, null, new GameConfiguration());
        tanks = new HashSet<Tank>();
        tanks.add(new Tank(Color.RED, new GameConfiguration()));
        tanks.add(new Tank(Color.BLUE, new GameConfiguration()));
        quartermaster = new ConcurrentQuartermaster(tanks);
    }
    
    @Test public void resetsAllButTanksSet() {
        quartermaster.getExplosions().add(explosion);
        quartermaster.getMines().add(mine);
        quartermaster.getMissiles().add(missile);
        quartermaster.resetForNewRound();
        assertEquals(0, quartermaster.getExplosions().size());
        assertEquals(0, quartermaster.getMines().size());
        assertEquals(0, quartermaster.getMissiles().size());
        assertEquals(tanks.size(), quartermaster.getTanks().size());
    }
    
    @Test public void preservesChangesToReturnedExplosionSet() {
        assertEquals(0, quartermaster.getExplosions().size());
        quartermaster.getExplosions().add(explosion);
        assertEquals(1, quartermaster.getExplosions().size());
        quartermaster.getExplosions().remove(explosion); 
        assertEquals(0, quartermaster.getExplosions().size());
    }

    @Test public void preservesChangesToReturnedMinesSet() {
        assertEquals(0, quartermaster.getMines().size());
        quartermaster.getMines().add(mine);
        assertEquals(1, quartermaster.getMines().size());
        quartermaster.getMines().remove(mine); 
        assertEquals(0, quartermaster.getMines().size());
    }
    
    @Test public void preservesChangesToReturnedMissilesSet() {
        assertEquals(0, quartermaster.getMissiles().size());
        quartermaster.getMissiles().add(missile);
        assertEquals(1, quartermaster.getMissiles().size());
        quartermaster.getMissiles().remove(missile); 
        assertEquals(0, quartermaster.getMissiles().size());
    }
    
    @Test public void usesAppropriateThreadsafeStorage() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final Set<Explosion> returnedExplosionSet = new HashSet<Explosion>();
        final Set<Mine> returnedMineSet = new HashSet<Mine>();
        final Set<Missile> returnedMissileSet = new HashSet<Missile>();
        final Set<Tank> returnedTankSet = new HashSet<Tank>();
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeConcurrentUpdateSet(Explosion.class); will(returnValue(returnedExplosionSet));
            oneOf(storageProvider).makeConcurrentUpdateSet(Mine.class); will(returnValue(returnedMineSet));
            oneOf(storageProvider).makeConcurrentUpdateSet(Missile.class); will(returnValue(returnedMissileSet));
            oneOf(storageProvider).makeImmutableSet(tanks); will(returnValue(returnedTankSet));
        }});
        Quartermaster quartermaster = new ConcurrentQuartermaster(tanks, storageProvider);
        assertEquals(returnedExplosionSet, quartermaster.getExplosions());
        assertEquals(returnedMineSet, quartermaster.getMines());
        assertEquals(returnedMissileSet, quartermaster.getMissiles());
        assertEquals(returnedTankSet, quartermaster.getTanks());
        context.assertIsSatisfied();
    }
}
