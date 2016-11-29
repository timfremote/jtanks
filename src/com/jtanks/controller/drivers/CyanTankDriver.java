package com.jtanks.controller.drivers;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Carmack;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import java.awt.Color;

public class CyanTankDriver implements TankDriver {

    enum Wall {
        TOP(Carmack.RADIANS_90),
        RIGHT(Carmack.RADIANS_0),
        BOTTOM(Carmack.RADIANS_270),
        LEFT(Carmack.RADIANS_180);

        private final double heading;

        private Wall(double heading) {
            this.heading = heading;
        }

        public double heading() {
            return heading;
        }
    };

    @SuppressWarnings("unused")
    private final GameConfiguration config;

    public CyanTankDriver(GameConfiguration config) {
        this.config = config;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    @Override
    public String getDriverName() {
        return "Cyan Driver";
    }

    @Override
    public void operate(ControlPanel controls, long ticks) {

        Point position = controls.getPosition();

        double shortestDistance = Double.POSITIVE_INFINITY;
        double heading = Carmack.RADIANS_0;
        double sumOfSpeeds = 0;
        int numberOfMovingTanks = 0;

        for (EntityVector vector : controls.scan().getEnemyTankVectors()) {

            double distance = Euclid.distanceBetweenPoints(position, vector.getPosition());

            if (vector.getSpeed() > 0) {
                sumOfSpeeds = sumOfSpeeds + vector.getSpeed();
                numberOfMovingTanks++;

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    heading = Euclid.getHeadingToPosition(position, vector.getPosition());
                }
            }
        }

        if (numberOfMovingTanks > 0) {
            controls.drive(sumOfSpeeds / numberOfMovingTanks, heading);
            controls.fire();
        } else {
            controls.drive(0, heading);
        }

    }

}
