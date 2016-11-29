package com.jtanks.controller.drivers;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import java.awt.Color;
import java.util.List;

import static com.jtanks.util.Euclid.distanceBetweenPoints;

public class BlueTankDriver implements TankDriver {
    private static final Point CENTER = new Point(100, 100);
	private static final int DISTANCE_BIG = 160;
	private static final int DISTANCE_SMALL = 40;
    private double headingDegrees = 0;
    private final double speed = 80;
    private boolean firstTime = true;

    public Color getColor() {
        return Color.BLUE;
    }

    public String getDriverName() {
        return "Mr. Blue";
//        return new BlueDriverName().name();
    }

    public void operate(ControlPanel controls, long ticks) {
    	if (firstTime) {
    		moveToCenter(controls);
    		firstTime = false;
    		return;
    	}

    	if (ticks % 100 == 0) {
	    	EntityVector enemy = findTarget(controls);
	    	if (enemy != null) {
	    		shootAndRun(controls, enemy);
	    	} else {
	    		headingDegrees+=getRandomHeading();
	    		if (controls.getAvailableMissiles() > 2) {
	    			shootAtCenter(controls);
	    		}
	    	}
	    	safeMove(controls);
    	}
    }

	private void moveToCenter(ControlPanel controls) {
		controls.drive(speed,  Euclid.getHeadingToPosition(controls.getPosition(), CENTER));
    }

    private EntityVector findTarget(ControlPanel controls) {
        List<EntityVector> enemyTankVectors = controls.scan().getEnemyTankVectors();
    	if (!enemyTankVectors.isEmpty()) {
    		return findClosestEnemy(controls, enemyTankVectors);
    	}
    	return null;
    }

	private EntityVector findClosestEnemy(ControlPanel controls, List<EntityVector> enemyTankVectors) {
		EntityVector closestEnemy = enemyTankVectors.get(0);
		for (EntityVector enemy : enemyTankVectors) {
			if (distanceBetweenPoints(controls.getPosition(), closestEnemy.getPosition()) >
				distanceBetweenPoints(controls.getPosition(), enemy.getPosition())) {
				closestEnemy = enemy;
			}
		}
		return closestEnemy;
	}

    private void safeMove(ControlPanel controls) {
    	Point position = controls.getPosition();
    	if (!controls.scan().getMines().isEmpty()) {
    		headingDegrees -= 160;
    		controls.drive(speed, Math.toRadians(headingDegrees));
		} else if (position.getX() <= DISTANCE_SMALL || position.getX() > DISTANCE_BIG || position.getY() <= DISTANCE_SMALL || position.getY() > DISTANCE_BIG) {
			controls.dropMine();
    		moveToCenter(controls);
    	} else {
    		controls.drive(speed, Math.toRadians(headingDegrees));
    	}
    }

    private void shootAndRun(ControlPanel controls, EntityVector enemy) {
    	double enemyHeading = Euclid.getHeadingToPosition(controls.getPosition(), enemy.getPosition());
    	controls.drive(0, enemyHeading);
    	controls.fire();
    	controls.drive(0, enemyHeading+1);
    	controls.fire();
    	controls.drive(0, enemyHeading-1);
    	controls.fire();
    }

    private void shootAtCenter(ControlPanel controls) {
    	double centerHeading = Euclid.getHeadingToPosition(controls.getPosition(), CENTER);
    	controls.drive(0, centerHeading);
    	controls.fire();
    }

    private double getRandomHeading() {
    	return Math.random()*360;
    }
}
