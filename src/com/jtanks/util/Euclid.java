package com.jtanks.util;

public class Euclid {
    private static final double RADIANS_90 = Math.PI / 2d;
    private static final double RADIANS_180 = Math.PI;
    private static final double RADIANS_270 = RADIANS_90 + RADIANS_180;

    public static double distanceBetweenPoints(Point firstPoint, Point secondPoint) {
        double xDifference = firstPoint.getX() - secondPoint.getX();
        double yDifference = firstPoint.getY() - secondPoint.getY();
        
        return Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
    }
    
    public static boolean circleContainsPoint(Point circleCenter, double radius, Point pointToTest) {
        return distanceBetweenPoints(circleCenter, pointToTest) <= radius;
    }
    
    public static boolean circlesIntersect(Point firstCircleCenter, double firstRadius, 
                                           Point secondCircleCenter, double secondRadius) {
        double distanceBetweenCircleCenters = distanceBetweenPoints(firstCircleCenter, secondCircleCenter);
        return distanceBetweenCircleCenters < firstRadius + secondRadius;
    }
    
    public static Point getPointAtDistanceAndHeading(Point fromPoint, double distance, double heading) {
        double xChange = distance * Math.cos(heading);
        double yChange = distance * Math.sin(heading);
        
        double launchX = fromPoint.getX() + xChange;
        double launchY = fromPoint.getY() - yChange;
        
        return new Point(launchX, launchY);
    }

    public static double getHeadingToPosition(Point fromPosition, Point toPosition) {
        double xDifference = toPosition.getX() - fromPosition.getX();
        double yDifference = toPosition.getY() - fromPosition.getY();
        double distanceBetweenPoints = distanceBetweenPoints(fromPosition, toPosition);
        double heading = Math.asin(Math.abs(yDifference) / distanceBetweenPoints);
        
        if (xDifference < 0 && yDifference > 0) {
            heading += RADIANS_90;
        } else if (xDifference < 0 && yDifference < 0) {
            heading += RADIANS_180;
        } else if (xDifference > 0 && yDifference < 0) {
            heading += RADIANS_270;
        }
        
        return Carmack.toGameHeadingFromRadians(heading);
    }
}
