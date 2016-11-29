package com.jtanks.util;

public class SystemClock implements Clock {    
    public long getCurrentTime() { return System.currentTimeMillis(); }

    public static double convertToSeconds(long timeInMillis) {
        return ((double) timeInMillis) / 1000.0;
    }
}
