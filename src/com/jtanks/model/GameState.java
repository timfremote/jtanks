package com.jtanks.model;

import com.jtanks.util.Clock;
import com.jtanks.util.SystemClock;
import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentInteger;
import com.jtanks.util.concurrency.ConsistentLong;
import com.jtanks.util.concurrency.jvm.JVMConcurrentStorageProvider;

public class GameState implements GameStatusReporter {
    private final Clock clock;
    private final long gameDurationInMillis;

    private final ConsistentLong gameEndTimeInMillis;
    private final ConsistentInteger gamesRemaining;

    public GameState(Clock clock, long gameDurationInMillis, int gamesToPlay) {
        this(clock, gameDurationInMillis, gamesToPlay, new JVMConcurrentStorageProvider());
    }

    public GameState(Clock clock, long gameDurationInMillis, int gamesToPlay, 
                     ConcurrentStorageProvider storageProvider) 
    {
        this.clock = clock;
        gamesRemaining = storageProvider.makeConsistentInteger();
        this.gameDurationInMillis = gameDurationInMillis;
        gamesRemaining.set(gamesToPlay);
        gameEndTimeInMillis = storageProvider.makeConsistentLong();
    }

    public void startNewGame() {
        gameEndTimeInMillis.set(clock.getCurrentTime() + gameDurationInMillis);
    }

    public double getTimeRemainingInSeconds() {
        long timeRemainingInMillis = gameEndTimeInMillis.get() - clock.getCurrentTime();
        double timeRemainingInSeconds = SystemClock.convertToSeconds(timeRemainingInMillis);
        return (timeRemainingInSeconds >= 0) ? timeRemainingInSeconds : 0;
    }

    public int getGamesRemaining() {
        return gamesRemaining.get();
    }

    public void finishGame() {
        gamesRemaining.set(getGamesRemaining() - 1);
    }
}
