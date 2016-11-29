package com.jtanks.util;

public class Newton {
    public static Point getVelocityVectorFromHeading(double velocity, double heading) {
        return new Point(velocity * Math.cos(heading), velocity * -Math.sin(heading)); 
    }
    
    public static Point getPositionAfterTimeAtVelocity(Point t, Point v, double time) {
        return new Point(t.getX() + (v.getX() * time), t.getY() + (v.getY() * time));
    } 
}
