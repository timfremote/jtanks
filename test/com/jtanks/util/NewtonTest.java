package com.jtanks.util;

import junit.framework.Assert;

import org.junit.Test;

public class NewtonTest {
    
    @Test public void earliestTimeThatObjectsWillIntersect() {
        Point positionAfterTimeAtVelocity = Newton.getPositionAfterTimeAtVelocity(new Point(0d, 0d), new Point(1d, 1d),  1);
        Assert.assertEquals(1d, positionAfterTimeAtVelocity.getX());
        Assert.assertEquals(1d, positionAfterTimeAtVelocity.getY());
    }

}
