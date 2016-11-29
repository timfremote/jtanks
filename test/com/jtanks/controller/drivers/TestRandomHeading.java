package com.jtanks.controller.drivers;

public class TestRandomHeading implements RandomHeading {

    private double[] headings;
    private int index;

    public void setHeadings(double... heading) {
        this.headings = heading;
        index = 0;
    }
    
    public double getHeading() {
        return headings[index++];
    }
    
}
