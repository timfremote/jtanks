package com.jtanks.controller.drivers;

import java.awt.Color;
import java.util.List;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Point;
import com.jtanks.controller.Radar;
import com.jtanks.util.Euclid;
import com.jtanks.util.Newton;

public class WhiteTankDriver implements TankDriver {
    private static final int TOLERANCE = 20;
    private static final double SPREADSHOT_EPSILON = Math.toRadians(15);
    private double lastFiredDirection;

    public Color getColor() {
        return Color.WHITE;
    }

    public String getDriverName() {
        return "White Driver";
    }

    public void operate(ControlPanel controls, long ticks) {
        controls.drive(0, 0);
        fireAtClosest(controls, ticks);
        dodgeBulletsAvoidingWalls(controls);
    }

    void dodgeBulletsAvoidingWalls(ControlPanel controls) {
        Radar scan = controls.scan();

        for (EntityVector missiles : scan.getMissileVectors()) {
            if (willHitUs(controls, missiles)) {
                moveToAvoidMissile(controls, missiles);
                return;
            }
         }

        if (tooCloseToWall(controls.getPosition())) {
            moveAwayFromWall(controls);
        }
    }

    private void moveAwayFromWall(ControlPanel controls) {
        Point position = controls.getPosition();
        if (position.getX() < TOLERANCE) {
            controls.drive(100, Math.toRadians(0));
            return;
        }
        if (position.getX() > 200 - TOLERANCE) {
            controls.drive(100, Math.toRadians(180));
            return;
        }
        if (position.getY() < TOLERANCE) {
            controls.drive(100, Math.toRadians(90));
            return;
        }
        if (position.getY() > 200 - TOLERANCE) {
            controls.drive(100, Math.toRadians(270));
            return;
        }
    }

    private void moveToAvoidMissile(ControlPanel controls, EntityVector missiles) {
        double missileHeading = missiles.getHeading();
        double heading = missileHeading + Math.toRadians(90);
        controls.drive(100, heading);
    }

    private boolean tooCloseToWall(Point point) {
        double x = point.getX();
        double y = point.getY();
        return x < TOLERANCE || x > 200 - TOLERANCE || y < TOLERANCE || y > 200 - TOLERANCE;
    }

    boolean willHitUs(ControlPanel controls, EntityVector missiles) {
        Point velocityVectorFromHeading = Newton.getVelocityVectorFromHeading(missiles.getSpeed(), missiles.getHeading());
        double time = 0.001;
        for (int i = 0; i < 20; i++) {
            time += 0.001D;
            Point predictedMissileLocation = Newton.getPositionAfterTimeAtVelocity(missiles.getPosition(), velocityVectorFromHeading, time);
            if (Euclid.circleContainsPoint(controls.getPosition(), 10, predictedMissileLocation)) {
                return true;
            }
        }
        return false;
    }

    void fireAtClosest(ControlPanel controls, long ticks) {
        Radar scan = controls.scan();
        EntityVector closest = findClosest(controls, scan);
        if (closest != null) {
            fireAt(closest, controls);
        }
    }

    private EntityVector findClosest(ControlPanel controls, Radar scan) {
        List<EntityVector> enemyTankVectors = scan.getEnemyTankVectors();

        double minDistance = 100000D;
        EntityVector closest = null;

        for (EntityVector ev : enemyTankVectors) {
            double distance = Euclid.distanceBetweenPoints(ev.getPosition(), controls.getPosition());
            if (minDistance > distance) {
                closest = ev;
                minDistance = distance;
            }
        }
        return closest;
    }

    private void fireAt(EntityVector closest, ControlPanel controls) {
        if (controls.getAvailableMissiles() > 3) {
            lastFiredDirection = Euclid.getHeadingToPosition(controls.getPosition(), closest.getPosition());
            controls.drive(0, lastFiredDirection);
            controls.fire();
            controls.drive(0, lastFiredDirection + SPREADSHOT_EPSILON);
            controls.fire();
            controls.drive(0, lastFiredDirection - SPREADSHOT_EPSILON);
            controls.fire();
        }
    }

}
