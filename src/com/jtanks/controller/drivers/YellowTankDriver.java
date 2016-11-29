package com.jtanks.controller.drivers;

import java.awt.Color;

import com.jtanks.controller.ControlPanel;

public class YellowTankDriver implements TankDriver {
    private int facing = 1;
    private double headingDegrees = 0;
    private double speed = 80;

    public Color getColor() {
        return Color.YELLOW;
    }

    public String getDriverName() {
        return "YellowTankDriver";
    }

    public void operate(ControlPanel controls, long ticks) {
        if (ticks % 100 == 0) {
            facing = -1;
        }
        if (ticks % 500 == 0) {
            facing = 1;
        }
        
        if (0 == controls.getSpeed()) {
            controls.drive(speed, Math.toRadians(headingDegrees+=180));
        } else {
            controls.drive(speed, Math.toRadians(headingDegrees+=facing));
        }
        
        if (ticks % 200 == 0) {
            controls.fire();
        }
    }
}
