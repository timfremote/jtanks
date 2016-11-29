package com.jtanks.model;

import com.jtanks.util.Clock;

public class TestClock implements Clock {
    public long currentTime;
    public long getCurrentTime() { return currentTime; }
}
