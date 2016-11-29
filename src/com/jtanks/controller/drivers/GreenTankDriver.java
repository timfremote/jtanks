package com.jtanks.controller.drivers;

import java.awt.Color;
import java.util.List;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Point;
import com.jtanks.controller.Radar;
import com.jtanks.util.Euclid;
import com.jtanks.util.Newton;

public class GreenTankDriver implements TankDriver {
    private static final double NINETY_DEGREES = Math.PI / 2;
    private static final double VELOCITY = 100d;
    
    private RandomHeading randomHeading;
    private Radar radar;
    
    public GreenTankDriver() {
        randomHeading = new RandomHeadingImpl();
    }

    public Color getColor() {
        return Color.GREEN;
    }

    protected void setRandomHeading(RandomHeading randomHeading) {
        this.randomHeading = randomHeading;
    }
    
    protected void setRadar(Radar radar) {
        this.radar = radar;
    }
    
    private Radar getRadar(ControlPanel controls) {
        return radar != null ? radar : controls.scan();
    }
    
    public String getDriverName() {
        return "GreenTankDriver";
    }

    public void operate(ControlPanel controls, long ticks) {
        if (ticks % 20 == 0) {
            changeHeading(controls);
        }
        
        while (isUnsafeHeading(controls.getHeading(), controls.getPosition(), getRadar(controls))) {
            changeHeading(controls);
        }
    }

    private boolean isUnsafeHeading(double heading, Point currentPosition, Radar radar) {
        Point velocityVector = Newton.getVelocityVectorFromHeading(VELOCITY, heading);
        Point nextPosition = Newton.getPositionAfterTimeAtVelocity(currentPosition, velocityVector, 0.005);
        return isNearWall(nextPosition) || isThreatenedByMissile(heading, currentPosition, radar); 
    }

    private boolean isThreatenedByMissile(double heading, Point position, Radar radar) {
        List<EntityVector> missileVectors = radar.getMissileVectors();
        for (EntityVector missileVector : missileVectors) {
            double awayFromMissile = Euclid.getHeadingToPosition(missileVector.getPosition(), position);
            awayFromMissile = awayFromMissile % 2 * Math.PI;
            if (Math.abs(heading - awayFromMissile) > NINETY_DEGREES) return true;
        }
        return false;
    }

    private boolean isNearWall(Point nextPosition) {
        return (nextPosition.getX() < 2 || nextPosition.getX() > 198
                || nextPosition.getY() < 2 || nextPosition.getY() > 198);
    }

    private void changeHeading(ControlPanel controls) {
        controls.drive(100, randomHeading.getHeading());
    }
}
