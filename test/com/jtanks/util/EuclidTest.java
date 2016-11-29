package com.jtanks.util;

import com.jtanks.util.Point;
import junit.framework.Assert;

import org.junit.Test;

public class EuclidTest {
    
    @Test public void getHeadingToPosition() {
        double headingToPosition = Euclid.getHeadingToPosition(new Point(0d, 0d), new Point(1d, 1d));
        Assert.assertEquals(45d, Math.toDegrees(Carmack.toVirtualHeadingFromRadians(headingToPosition)));
        
        headingToPosition = Euclid.getHeadingToPosition(new Point(0d, 0d), new Point(-1d, 1d));
        Assert.assertEquals(135d, Math.toDegrees(Carmack.toVirtualHeadingFromRadians(headingToPosition)));
        
        headingToPosition = Euclid.getHeadingToPosition(new Point(0d, 0d), new Point(-1d, -1d));
        Assert.assertEquals(225d, Math.toDegrees(Carmack.toVirtualHeadingFromRadians(headingToPosition)));
        
        headingToPosition = Euclid.getHeadingToPosition(new Point(0d, 0d), new Point(1d, -1d));
        Assert.assertEquals(315d, Math.toDegrees(Carmack.toVirtualHeadingFromRadians(headingToPosition)));
    }

}
