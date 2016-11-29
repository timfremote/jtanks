package com.jtanks.controller.drivers;

import com.jtanks.controller.EntityVector;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class MissileDodgerTest {
    Point tankPosition = new Point(1.0, 2.0);
    MissileDodger missileDodger = new MissileDodger();

    @Test public void
    requiresNoActionIfNoThreat() {
        List<EntityVector> missileVectors = Arrays.asList();

        missileDodger.update(missileVectors, tankPosition);

        assertThat(missileDodger.needsToDodge(), equalTo(false));
    }

    @Test public void
    requiresActionIfThreatened() {
        Point missilePosition = new Point(3.0, 4.0);
        List<EntityVector> missileVectors = Arrays.asList(new EntityVector(missilePosition, 100, Euclid.getHeadingToPosition(missilePosition, tankPosition)));

        missileDodger.update(missileVectors, tankPosition);

        assertThat(missileDodger.needsToDodge(), equalTo(true));
    }

    @Test public void
    requiresNoActionIfMissilesPassingBy() {
        Point missilePosition = new Point(3.0, 4.0);
        List<EntityVector> missileVectors = Arrays.asList(new EntityVector(missilePosition, 100, Euclid.getHeadingToPosition(missilePosition, new Point(1.1, 2))));

        missileDodger.update(missileVectors, tankPosition);

        assertThat(missileDodger.needsToDodge(), equalTo(false));
    }

}
