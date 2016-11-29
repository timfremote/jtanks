package com.jtanks.controller.drivers;

import static junit.framework.Assert.assertEquals;

import java.awt.Color;
import java.util.List;

import com.jtanks.controller.ControlPanel;
import com.jtanks.controller.EntityVector;
import com.jtanks.util.Point;
import com.jtanks.model.Tank;
import com.jtanks.controller.TankDriverTestFrame;
import com.jtanks.util.Carmack;
import junit.framework.Assert;

public class TestYellowTankDriver extends TankDriverTestFrame {
    // FIXME: Figure out what this is supposed to test
    /*@Test*/ public void testDriveAndScan() {
        TankRunner myTankRunner = getMyTankRunner(Color.YELLOW);
        ControlPanel controls = myTankRunner.getControlPanel();
        Tank myTank = myTankRunner.getTank();

        System.out.println("My tank is " + myTank);
        
        positionTank(myTank, new Point(1, 1));
        
        controls.drive(2.5d, Carmack.toGameHeadingFromDegrees(90));

        advanceTime(1000);
        
        Assert.assertEquals("X", 1d, myTank.getPosition().getX(), 0.0001d);
        Assert.assertEquals("Y", 2d, myTank.getPosition().getY(), 0.0001d);
        
        for (Tank tank : getOtherTanks()) {
            positionTank(tank, new Point(0, 0));
        }
        
        List<EntityVector> enemyTankVectors = controls.scan().getEnemyTankVectors();
        assertEquals(getOtherTanks().size(), enemyTankVectors.size());
    }
}
