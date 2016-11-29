package com.jtanks.controller.drivers;

import static com.jtanks.util.Euclid.distanceBetweenPoints;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.model.Mine;
import com.jtanks.util.Point;
import com.jtanks.controller.Radar;
import com.jtanks.util.Euclid;
import com.jtanks.util.Newton;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.*;

public class LevyTankDriver implements TankDriver {
    private static final Point CENTER = new Point(100, 100);
    private static final double NINETY_DEGREES = Math.PI / 2;
    private static final double VELOCITY = 100d;
    
    private RandomHeading randomHeading;
    private Integer stepsRemaining = 0;
    private CauchyDistribution distribution = new CauchyDistributionImpl(0,1);
    private Random rng = new Random();
    private Radar radar;
    private double heading;
    private long avoidUntil;
    
    public LevyTankDriver() {
        randomHeading = new RandomHeadingImpl();
    }

    public Color getColor() {
        return Color.LIGHT_GRAY;
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
        return "LevyTankDriver";
    }

    public void operate(ControlPanel controls, long ticks) {
        if (incoming(controls, ticks)) {
            // nothing
        } else if (ticks % 100 == 0) {
            EntityVector enemy = findTarget(controls);
            if (enemy != null) {
                shootAndRun(controls, enemy);
                stepsRemaining = 0;
            } else { 
                shootAtCenter(controls);
                if(!whereTheActionAt(controls, ticks) && stepsRemaining <= 0) {
                    changeHeading(controls);
                    stepsRemaining = getLevySteps();
                }
            }
         }
        
         --stepsRemaining;
       
         while (isUnsafeHeading(controls.getHeading(), controls.getPosition(), getRadar(controls))) {
             changeHeading(controls);
         }
    }
    
    private boolean whereTheActionAt(ControlPanel controls, long ticks) {
        if (!controls.scan().getEnemyTankVectors().isEmpty()) {
            return false;
        }
        
        Point nearest_missile = null;
        double distance_to_missile = Double.MIN_VALUE;
        double furthest_trajectory = 0.0;
        Point my_position = controls.getPosition();
        
        for(EntityVector missile : controls.scan().getMissileVectors()) {
            double trajectory = Euclid.getHeadingToPosition(my_position, missile.getPosition());
            double distance = Euclid.distanceBetweenPoints(missile.getPosition(), my_position);
            
            if(distance > distance_to_missile) {
                nearest_missile = missile.getPosition();
                furthest_trajectory = trajectory;
            }
         }   
        
        if(nearest_missile != null && avoidUntil < ticks) {
            controls.drive(100,furthest_trajectory);
            avoidUntil = ticks + stepsRemaining;
        }
        
        return true;
    }
    
 
    private boolean incoming(ControlPanel controls, long ticks) {
        Point my_position = controls.getPosition();
        
        double nearest_missile = Double.MAX_VALUE;
        double nearest_trajectory = Double.MAX_VALUE;
        
        boolean incoming = false;
        for(EntityVector missile : controls.scan().getMissileVectors()) {
           double trajectory = missile.getHeading() - Euclid.getHeadingToPosition(missile.getPosition(), my_position);
           double distance = Euclid.distanceBetweenPoints(missile.getPosition(), my_position);
           
           if(distance < nearest_missile && Math.abs(trajectory) < 0.2617) {
               nearest_missile = distance;
               nearest_trajectory = trajectory;
               incoming = true;
           }
        }   
        
        if(incoming && avoidUntil < ticks) {
            double headingOffset = 0.0;
            if(nearest_trajectory != 0.0) {
                headingOffset = -(rng.nextDouble() * Math.signum(nearest_trajectory));
            } else {
                headingOffset = rng.nextGaussian();
            }
            controls.drive(100, controls.getHeading() + headingOffset);
            avoidUntil = ticks + stepsRemaining + 50;
        }
        
        for(Mine mine : controls.scan().getMines()) {
            if(Euclid.circleContainsPoint(my_position, 3.0, mine.getPosition()))
              return true;
        }
        
        return incoming;
    }
    
 private void shootAndRun(ControlPanel controls, EntityVector enemy) {
        
        if(Euclid.distanceBetweenPoints(controls.getPosition(), enemy.getPosition()) < 4.0) {
          controls.dropMine();  
        }
        
        double enemyHeading = Euclid.getHeadingToPosition(controls.getPosition(), enemy.getPosition());
        controls.drive(0, enemyHeading);
        controls.fire();
        drive(controls);
    }
    
    private void shootAtCenter(ControlPanel controls) {
        double centerHeading = Euclid.getHeadingToPosition(controls.getPosition(), CENTER);
        controls.drive(0, centerHeading);
        controls.fire();
        drive(controls);
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
        heading = randomHeading.getHeading();
        controls.drive(100, heading);
    }
    
    private void drive(ControlPanel controls) {
        controls.drive(100, heading);
    }
    
    private int getLevySteps() {
        try {
            return (250 + (int) (2000 * distribution.cumulativeProbability(rng.nextDouble())));
        } catch (MathException e) {
            return 250;
        }   
    }
}
