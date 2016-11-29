package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.controller.GameRunner.WALLS;
import com.jtanks.model.ArmsLocker;
import com.jtanks.model.MovingEntity;
import com.jtanks.model.Tank;
import com.jtanks.util.Clock;
import com.jtanks.util.SystemClock;

public class TankEventHandler extends GenericEventHandler {
    // FIXME: should be configurable
    public static final int WALL_COLLISION_DAMAGE = 5;
    public static final int MINE_COLLISION_DAMAGE = 40;
    public static final int MISSILE_COLLISION_DAMAGE = 20;
    public static final long MISSILE_REPLENISH_TIME = 2000;
    public static final long MINE_REPLENISH_TIME = 4000;
    
    private final Tank tank;
    private MessageReceiver messageReceiver;
    private ArmsLocker missileLocker;
    private ArmsLocker mineLocker;
    
    public TankEventHandler(Tank tank, GameConfiguration config) {
        this(tank, new SystemClock(), config);
    }
    
    public TankEventHandler(Tank tank, Clock clock, GameConfiguration config) {
        super(clock, config);
        this.tank = tank;
        missileLocker = tank.getMissileLocker();
        mineLocker = tank.getMineLocker();
    }
    
    //FIXME: Should remove. Within controller, should work only with event handler
    public Tank getTank() {
        return tank;
    }
    
    public synchronized void notifyUpdated() {
        replenishWeapons();
    }
    
    protected synchronized void collideWithWall(WALLS wall) {
        tank.damage(WALL_COLLISION_DAMAGE);
        tank.setSpeedAsPercentage(0);
    }
    
    synchronized void hitByMissile() {
        tank.damage(MISSILE_COLLISION_DAMAGE);
    }
    
    synchronized void collideWithMine() {
        tank.damage(MINE_COLLISION_DAMAGE);
    }   
    
    protected void emitMessage(Message message) {
        messageReceiver.accept(message);
    }
    
    void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }
    
    private synchronized void replenishWeapons() {
        if (canHaveMoreMissiles() && dueForMoreMissiles()) 
        {
            missileLocker.depositItem();
        }
        
        if (canHaveMoreMines() && dueForMoreMines()) 
        {
            mineLocker.depositItem();
        }
    }

    private boolean canHaveMoreMissiles() {
        return !missileLocker.isFull();
    }
    
    private boolean dueForMoreMissiles() {
        return missileLocker.getTimeSinceLastDeposit() > MISSILE_REPLENISH_TIME;
    }
    
    private boolean canHaveMoreMines() {
        return !mineLocker.isFull();
    }
    
    private boolean dueForMoreMines() {
        return mineLocker.getTimeSinceLastDeposit() > MINE_REPLENISH_TIME;
    }

    @Override
    protected MovingEntity getEntity() { return tank; }
}
