package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.controller.drivers.BlueTankDriver;
import com.jtanks.controller.drivers.CyanTankDriver;
import com.jtanks.controller.drivers.GreenTankDriver;
import com.jtanks.controller.drivers.LevyTankDriver;
import com.jtanks.controller.drivers.OrangeTankDriver;
import com.jtanks.controller.drivers.RedTankDriver;
import com.jtanks.controller.drivers.TankDriver;
import com.jtanks.controller.drivers.TankRunner;
import com.jtanks.controller.drivers.WhiteTankDriver;
import com.jtanks.controller.drivers.YellowTankDriver;
import com.jtanks.model.Explosion;
import com.jtanks.model.GameState;
import com.jtanks.model.Mine;
import com.jtanks.model.Missile;
import com.jtanks.model.Quartermaster;
import com.jtanks.model.Tank;
import com.jtanks.util.Clock;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GameRunner {
    public enum WALLS {
        TOP, LEFT, BOTTOM, RIGHT;
    }

    private final ArrayList<TankEventHandler> tankHandlers;
    private ArrayList<TankRunner> tankRunners;
    private List<MissileEventHandler> missileHandlers = Collections.synchronizedList(new ArrayList<MissileEventHandler>());

    private Thread runGameThread;

    private final Scorer scorer;
    private final GameState gameState;

    private final Quartermaster quartermaster;
    private final GameConfiguration config;
    private final Clock clock;
    private final TankDriver[] drivers;

    public GameRunner(Scorer scorer, GameState gameState, Quartermaster quartermaster,
                      Clock clock, GameConfiguration config) {

        this.scorer = scorer;
        this.gameState = gameState;
        this.quartermaster = quartermaster;
        this.clock = clock;
        this.config = config;
        Set<Tank> tanks = quartermaster.getTanks();
        tankHandlers = new ArrayList<TankEventHandler>();
        for (Tank tank : tanks) {
            tankHandlers.add(new TankEventHandler(tank, clock, config));
        }
        drivers = new TankDriver[] { new RedTankDriver(),
                                     new BlueTankDriver(),
                                     new YellowTankDriver(),
                                     new GreenTankDriver(),
                                     new WhiteTankDriver(),
                                     new OrangeTankDriver(),
                                     new CyanTankDriver(new com.jtanks.controller.drivers.GameConfiguration(config)),
                                     new LevyTankDriver()
        };
        initialize();
    }

    private void initialize() {
        missileHandlers = Collections.synchronizedList(new ArrayList<MissileEventHandler>());
        tankRunners = new ArrayList<TankRunner>();
        for (TankDriver driver : drivers) {
            for (TankEventHandler tankHandler : tankHandlers) {
                if (tankHandler.getTank().getColor().equals(driver.getColor())) {
                    tankRunners.add(new TankRunner(tankHandler, driver, this, config));
                }
            }
        }
        quartermaster.resetForNewRound();
    }

    // FIXME: Shouldn't need to expose this
    public List<TankRunner> getTankRunners() {
        return tankRunners;
    }

    public void reset() {
        try {
            if (runGameThread != null) {
                runGameThread.join();
            }
        } catch (InterruptedException ex) {
        }

        for (final TankRunner tankRunner : tankRunners) {
            tankRunner.gameOver();
        }

        for (final TankEventHandler tank : tankHandlers) {
            tank.getTank().reset();
        }

        gameState.startNewGame();
        initialize(); // FIXME: Actually need something to reset all the drivers here
    }

    public boolean isGameRunning() {
        return (gameState.getTimeRemainingInSeconds() > 0) &&
               (getLiveTanks().size() > 1);
    }
    // FIXME: Duplicates code in Radar
    private Set<Tank> getLiveTanks() {
        Set<Tank> liveTanks = new HashSet<Tank>();
        for (Tank tank : quartermaster.getTanks()) {
            if (!tank.isDestroyed()) {
                liveTanks.add(tank);
            }
        }
        return liveTanks;
    }

    void updateArena() {
    	updateMissiles();
        updateMines();
        removeFinishedExplosions();
    }

	void updateMines() {
        for (TankEventHandler tank : tankHandlers) {
            boolean wasTankDestroyed = tank.getTank().isDestroyed();

            Collection<Mine> mines = quartermaster.getMines();
            for (Mine mine : mines) {
                if (Euclid.circlesIntersect(
                        tank.getTank().getPosition(),
                        tank.getTank().getSize() / 2d,
                        mine.getPosition(),
                        mine.getSize() / 2d))
                {
                    tank.collideWithMine();
                    mine.destroy();
                    mines.remove(mine);
                    boom(mine.getPosition());
                }
            }

            //FIXME: Should be in an updateTanks() method!!
            tank.updatePosition();
            tank.notifyUpdated();

            if (!wasTankDestroyed && tank.getTank().isDestroyed()) {
                boom(tank.getTank().getPosition());
            }
        }
	}

	synchronized void updateMissiles() {
	    Collection<Missile> missiles = quartermaster.getMissiles();
	    synchronized (missileHandlers) {
	        Iterator<MissileEventHandler> i = missileHandlers.iterator();
	        while (i.hasNext()) {
	            MissileEventHandler missileHandler = i.next();
	            missileHandler.updatePosition();
	            Missile missile = missileHandler.getMissile();

	            for (TankEventHandler tank : tankHandlers) {
	                if (tank.getTank().pointIsInsideBody(missile.getPosition())) {
	                    tank.hitByMissile();
	                    scorer.updateScoreForMissileHit(missile.getCreator());
	                    missile.destroy();
	                }
	            }

	            missileHandler.notifyUpdated();

	            if (missile.isDestroyed()) {
	                boom(missile.getPosition());
	                missiles.remove(missile);
	                i.remove();
	            }
	        }
        }
	}

    void removeFinishedExplosions() {
        Collection<Explosion> explosions = quartermaster.getExplosions();
        for (Explosion e : explosions) {
            if (e.isDestroyed()) {
                explosions.remove(e);
            }
        }
    }

    public void startTankDrivers() {
        for (final TankRunner tankRunner : tankRunners) {
            Thread tankDriverThread = new Thread() {
                @Override
                public void run() {
                    tankRunner.run();
                }
            };
            tankDriverThread.setName("Tank " + tankRunner.getDriverName());
            tankDriverThread.start();
        }
    }

    void launchMissile(Color color, Point point, double heading, Tank tank) {
        Missile missile = new Missile(color, point, heading, tank, config);
        quartermaster.getMissiles().add(missile);
        missileHandlers.add(new MissileEventHandler(missile, clock, config));
    }

    void dropMine(Color color, Point point) {
        Mine mine = new Mine(color, point);
        quartermaster.getMines().add(mine);
    }

    private void boom(Point position) {
        Explosion explosion = new Explosion(position);
        quartermaster.getExplosions().add(explosion);
    }

	public void setMessageReceiver(MessageReceiver messageReceiver) {
		for (TankEventHandler tank : tankHandlers) {
			tank.setMessageReceiver(messageReceiver);
		}
	}

    public Quartermaster getQuartermaster() {
        return quartermaster;
    }

    public void finishRound() {
        scorer.updateScoreForEndOfRound(quartermaster.getTanks());
    }
}
