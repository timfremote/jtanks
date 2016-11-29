package com.jtanks.controller.drivers;

import java.util.Random;

public class RandomHeadingImpl implements RandomHeading {

    private final Random random = new Random();

    public double getHeading() {
        return random.nextDouble() * 2 * Math.PI;
    }
    
}
