package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.controller.GameRunner.WALLS;
import com.jtanks.model.Missile;
import com.jtanks.model.MovingEntity;
import com.jtanks.util.Clock;
import com.jtanks.util.SystemClock;

public class MissileEventHandler extends GenericEventHandler {
    private final Missile missile;
    
    public MissileEventHandler(Missile missile, GameConfiguration config) {
        this(missile, new SystemClock(), config);
    }

    public MissileEventHandler(Missile missile, Clock clock, GameConfiguration config) {
        super(clock, config);
        this.missile = missile;
    }
    
    public Missile getMissile() {
        return missile;
    }
    
    @Override protected synchronized void collideWithWall(WALLS wall) {
        switch(wall) {
        case TOP:
        case BOTTOM:
            missile.setHeading((2 * Math.PI) - missile.getHeading());
            break;
        case LEFT:
        case RIGHT:
            missile.setHeading(Math.PI - missile.getHeading());
            break;
        default:
            break;
        }
    }
    
    @Override public synchronized void notifyUpdated() {
        // Nothing to do
    }

    @Override
    protected MovingEntity getEntity() { return missile; }

}
