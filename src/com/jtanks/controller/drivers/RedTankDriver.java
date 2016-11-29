package com.jtanks.controller.drivers;

import java.awt.Color;
import java.util.List;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Point;
import com.jtanks.util.Euclid;
import com.jtanks.util.Newton;

public class RedTankDriver  implements TankDriver {

    private boolean avoiding = false;

    public Color getColor() {
        return Color.RED;
    }
    
    public String getDriverName() {
        return "RedTankDriver";
    }

    public void operate(ControlPanel controls, long ticks) {        
//    	System.out.println(String.format("at ticks: %s", ticks));
    	
    	smartFire(controls);
        
        if (incoming(controls)) {
            reverseDirection(controls);
        } else {
            avoiding = false;
            double rand = Math.random();
            if (rand < 0.999) {
                straightAheadStrategy(controls);
            } else if (rand < 0.9995) {
                //layMines(tank);
            } else {
                turnAway(controls);
            }
        }

        avoidTheWall(controls);
    }

    private void turnAway(ControlPanel controls) {
        controls.drive(100, turn90Right(controls));
    }

    private void reverseDirection(ControlPanel controls) {
        if(!avoiding) {
            avoiding  = true;
            controls.drive(100, reversedDirection(controls));
        }
    }

    private double reversedDirection(ControlPanel controls) {
    	return headingForXRightInRadians(controls, 180);
    }
    
    private double headingForXRightInRadians(ControlPanel controls, int degrees) {
    	if (controls.getHeading() - Math.toRadians(degrees) < 0) {
            return Math.toRadians(360) + (controls.getHeading() - Math.toRadians(degrees));
        }
        
        return controls.getHeading() - Math.toRadians(degrees);
    }
    
    private double turn90Right(ControlPanel controls) {
        return headingForXRightInRadians(controls, 90);
    }

    private boolean incoming(ControlPanel controls) {
        return !controls.scan().getMissileVectors().isEmpty() || !controls.scan().getMines().isEmpty();
    }
    
    private boolean nextPositionPlusAspectHitsWall(ControlPanel controls, int turnAspect) {
    	Point nextPosition = getNextPositionWithAspect(controls, turnAspect);
    	double nextY = nextPosition.getY();
    	double nextX = nextPosition.getX();
    	
    	return nextY <= 0 || nextY >= 200 || nextX <= 0 || nextX >= 200;
    }

    private void avoidTheWall(ControlPanel controls) {
    	int turnAspect = 0;
        if (nextPositionPlusAspectHitsWall(controls, turnAspect)) {
        	while (nextPositionPlusAspectHitsWall(controls, turnAspect)) {
        		turnAspect = turnAspect + 3;
        	}
        	controls.drive(100, headingForXRightInRadians(controls, turnAspect));
        }
    }
    

    private void straightAheadStrategy(ControlPanel controls) {
        controls.drive(100, controls.getHeading());
    }

    private void smartFire(ControlPanel controls) {
        if (isThereATargetInRange(controls) && doWeHaveAFullClip(controls)) {
            fireAtTarget(controls, getBaddies(controls).get(0));
        }
    }

    private boolean doWeHaveAFullClip(ControlPanel controls) {
        return controls.getAvailableMissiles() >= 3;
    }

    private void fireAtTarget(ControlPanel controls, EntityVector baddie) {
        double oldHeading = controls.getHeading();
        
        double heading = Euclid.getHeadingToPosition(controls.getPosition(), baddie.getPosition());
        controls.drive(0, heading);
        controls.fire();
        controls.drive(0, heading + Math.toRadians(5));
        controls.fire();
        controls.drive(0, heading - Math.toRadians(5));
        controls.fire();
        
        controls.drive(100, oldHeading);
    }

    private boolean isThereATargetInRange(ControlPanel controls) {
        return !getBaddies(controls).isEmpty();
    }

    private List<EntityVector> getBaddies(ControlPanel controls) {
        return controls.scan().getEnemyTankVectors();
    }

    private Point getNextPositionWithAspect(ControlPanel controls, int turnAspect) {
        return Newton.getPositionAfterTimeAtVelocity(controls.getPosition(), Newton.getVelocityVectorFromHeading(controls.getSpeed(), headingForXRightInRadians(controls, turnAspect)), 0.03);
    }

}
