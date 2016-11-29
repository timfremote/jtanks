package com.jtanks.util;

public class Carmack {
    public static final double RADIANS_0 = 0d;
    public static final double RADIANS_45 = Math.PI / 4d;
    public static final double RADIANS_90 = Math.PI / 2d;
    public static final double RADIANS_135 = RADIANS_45 + RADIANS_90;
    public static final double RADIANS_180 = Math.PI;
    public static final double RADIANS_225 = RADIANS_135 + RADIANS_90;
    public static final double RADIANS_270 = 3d * Math.PI / 2;
    public static final double RADIANS_315 = RADIANS_225 + RADIANS_90;
    public static final double RADIANS_360 = 2d * Math.PI;

    public static enum DIRECTION {
        NORTH, SOUTH, WEST, EAST;
    }

    public static double toGameHeadingFromDegrees(double virtualDegrees) {
        return toGameHeadingFromRadians(Math.toRadians(virtualDegrees));
    }

    public static double toGameHeadingFromRadians(double virtualRadians) {
        return RADIANS_360 - virtualRadians;
    }

    public static double toVirtualHeadingFromDegrees(double gameDegrees) {
        return toVirtualHeadingFromRadians(Math.toRadians(gameDegrees));
    }

    public static double toVirtualHeadingFromRadians(double gameRadians) {
        return RADIANS_360 - gameRadians;
    }

    public static DIRECTION getGeneralDirectionFromGameRadians(double gameRadians) {
        double virtualRadians = toVirtualHeadingFromRadians(gameRadians);

        while (virtualRadians < RADIANS_0) {
            virtualRadians += RADIANS_360;
        }

        while (virtualRadians > RADIANS_360) {
            virtualRadians -= RADIANS_360;
        }

        if (virtualRadians >= RADIANS_45 && virtualRadians < RADIANS_135) {
            return DIRECTION.NORTH;
        }

        if (virtualRadians >= RADIANS_135 && virtualRadians < RADIANS_225) {
            return DIRECTION.WEST;
        }

        if (virtualRadians >= RADIANS_225 && virtualRadians < RADIANS_315) {
            return DIRECTION.SOUTH;
        }

        if ((virtualRadians >= RADIANS_315 && virtualRadians <= RADIANS_360)
                || (virtualRadians >= 0 && virtualRadians < RADIANS_45)) {
            return DIRECTION.EAST;
        }

        return DIRECTION.NORTH;
    }
}
