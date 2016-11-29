package com.jtanks.controller.drivers;

import com.jtanks.config.GameConfiguration;
import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.GameRunner;
import com.jtanks.controller.TankEventHandler;
import com.jtanks.model.Tank;

public class TankRunner {
    private static final long RELAX_TIME = 5;
    
    private final ControlPanel controls;
    private final TankDriver driver;
    
    private boolean isRunning = false;
    private long ticks = 0;
    private boolean isGameOver;

    private Tank tank;

    public TankRunner(TankEventHandler tankEventHandler, TankDriver driver, 
                      GameRunner gameRunner, GameConfiguration config) {
        controls = new ControlPanel(tankEventHandler, gameRunner, config);
        tank = tankEventHandler.getTank();
        tank.setName(driver.getDriverName());
        this.driver = driver;
    }
    
    public ControlPanel getControlPanel() { return controls; }
    
    public void run() {
        if (isRunning()) {
            throw new IllegalStateException("Cannot call run more than once!");
        }
        
        isRunning = true;
         
        while (!isGameOver && !tank.isDestroyed()) {
            driver.operate(controls, ticks++);
            // FIXME: Unite with the similar method in GameRunner
            try {
                Thread.sleep(RELAX_TIME);
            } catch (InterruptedException ex) { }
        }
    }
    
    private synchronized boolean isRunning() {
        return isRunning ;
    }

    public void gameOver() {
        isGameOver = true;
    }

    public String getDriverName() {
        return driver.getDriverName();
    }

    // FIXME: Shouldn't need this
    public Tank getTank() { return tank; }
}
