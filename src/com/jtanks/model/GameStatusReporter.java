package com.jtanks.model;

public interface GameStatusReporter extends StatusReporter {
    double getTimeRemainingInSeconds();
    int getGamesRemaining();
}
