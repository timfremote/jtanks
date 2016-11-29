package com.jtanks.controller.drivers;

import com.jtanks.controller.EntityVector;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import java.util.List;

public class MissileDodger {

    private static final double EPSILON = 1e-2;
    private boolean needsToDodge = false;

    private boolean equalToWithin(double lhs, double rhs, double epsilon) {
        return Math.abs(lhs - rhs) < epsilon;
    }

    public void update(List<EntityVector> missileVectors, Point position) {
        needsToDodge = false;

       if (missileVectors.isEmpty()) {
           return;
       }

       for (EntityVector missileVector : missileVectors) {
           if (aimedAtTank(missileVector, position)) {
               needsToDodge = true;
           }
       }

    }

    public boolean needsToDodge() {
        return needsToDodge;
    }

    private boolean aimedAtTank(EntityVector missileVector, Point position) {
       return equalToWithin(missileVector.getHeading(), Euclid.getHeadingToPosition(missileVector.getPosition(), position), EPSILON);
    }

}
