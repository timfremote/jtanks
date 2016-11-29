package com.jtanks.controller.drivers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jtanks.controller.EntityVector;
import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.Radar;
import com.jtanks.controller.TankDriverTestFrame;
import com.jtanks.util.Point;

import com.jtanks.model.MovingEntity;
import com.jtanks.model.Tank;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestGreenTankDriver extends TankDriverTestFrame {
    private static final double HEADING = Math.PI;
    private static final double HEADING_2 = 0d;
    private ControlPanel controls;
    private Tank tank;
    private GreenTankDriver driver;

    @Before public void setUp() {
        TankRunner d2 = getMyTankRunner(Color.GREEN);
        controls = d2.getControlPanel();
        tank = d2.getTank();
        driver = new GreenTankDriver();
    }
    
    @Test public void testRandomMovement() {
        positionTank(tank, new Point(100, 100));
        
        TestRandomHeading randomHeading = new TestRandomHeading();
        randomHeading.setHeadings(HEADING, HEADING_2);
        driver.setRandomHeading(randomHeading);
        
        driver.operate(controls, 0);
        Assert.assertEquals(HEADING, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
        
        driver.operate(controls, 10);
        Assert.assertEquals(HEADING, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
        
        driver.operate(controls, 19);
        Assert.assertEquals(HEADING, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
        
        driver.operate(controls, 20);
        Assert.assertEquals(HEADING_2, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
    }
    
    @Test public void testWallAvoidance() {
        positionTank(tank, new Point(2.02, 2.04));
        
        TestRandomHeading randomHeading = new TestRandomHeading();
        randomHeading.setHeadings(HEADING, HEADING_2);
        driver.setRandomHeading(randomHeading);
        
        driver.operate(controls, 0);
        Assert.assertEquals(HEADING_2, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
    }
    
    @Test public void testMissileAvoidance() {
        positionTank(tank, new Point(100, 100));
        
        TestRandomHeading randomHeading = new TestRandomHeading();
        randomHeading.setHeadings(HEADING, HEADING_2);
        driver.setRandomHeading(randomHeading);
        
        TestRadar testRadar = new TestRadar();
        List<EntityVector> missileVectors = new ArrayList<EntityVector>();
        missileVectors.add(new TestEntityVector(98, 100));
        testRadar.setMissileVectors(missileVectors);
        driver.setRadar(testRadar);
        
        driver.operate(controls, 0);
        Assert.assertEquals(HEADING_2, tank.getHeading());
        Assert.assertEquals(40d, tank.getSpeed());
    }

    class TestRadar extends Radar {

        private List<EntityVector> missileVectors;

        public TestRadar() {
            super(tank, new Point(0, 0), 0, getGameRunner().getQuartermaster());
        }
        
        public List<EntityVector> getMissileVectors() {
            return missileVectors;
        }

        public void setMissileVectors(List<EntityVector> missileVectors) {
            this.missileVectors = missileVectors;
        }
    }
    
    class TestEntityVector extends EntityVector {

        public TestEntityVector(double x, double y) {
            super(new TestEntity(x, y));
        }
        
    }
    
    class TestEntity implements MovingEntity {
        private Point point;

        public TestEntity(double x, double y) {
            point = new Point(x, y);
        }

        public double getSize() {
            return 0;
        }

        public long lastMovedAt() {
            return 0;
        }

        public double getHeading() {
            // TODO Auto-generated method stub
            return 0;
        }

        public double getSpeed() {
            // TODO Auto-generated method stub
            return 0;
        }

        public void setHeading(double heading) {
            // TODO Auto-generated method stub
            
        }

        public void setPosition(Point position) {
            // TODO Auto-generated method stub
            
        }

        public void destroy() {
            // TODO Auto-generated method stub
            
        }

        public Color getColor() {
            // TODO Auto-generated method stub
            return null;
        }

        public Point getPosition() {
            return point;
        }

        public boolean isDestroyed() {
            // TODO Auto-generated method stub
            return false;
        }   
    }
}
