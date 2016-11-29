package com.jtanks.controller.drivers;

import java.awt.Color;

import com.jtanks.controller.ControlPanel;

public interface TankDriver {

    /**
     * Called on a never-ending loop, this is where you make your tank do 
     * whatever you want,  until you win or the tank is destroyed.
     * @param controls The ControlPanel that lets you tell your tank what to do.
     * @param ticks The number of ticks since the start of the battle.
     */
    void operate(ControlPanel controls, long ticks);

    Color getColor();

    String getDriverName();

}
