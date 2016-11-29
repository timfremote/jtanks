package com.jtanks.model;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.concurrency.ConcurrentStorageProvider;
import com.jtanks.util.concurrency.ConsistentInteger;
import com.jtanks.util.concurrency.ConsistentLong;

import static junit.framework.Assert.assertEquals;

public class GameStateTest {
    private static final long FIRST_GAME_START_TIME  = 1000000000;
    private static final long SECOND_GAME_START_TIME = 1000070000;
    private static final long GAME_DURATION_IN_MILLIS = 60000;
    private static final int TOTAL_GAMES_TO_PLAY = 4;
    
    private TestClock clock = new TestClock();
    private GameState gameState;
    
    @Before public void setUp() {
        gameState = new GameState(clock, GAME_DURATION_IN_MILLIS, TOTAL_GAMES_TO_PLAY);
    }
    
    @Test public void reportsTimeRemainingDuringGameAndZeroAfter() {
        clock.currentTime = FIRST_GAME_START_TIME;
        gameState.startNewGame();
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 0,       60d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 1,       59.999d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 1000,    59d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 1500,    58.5d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 30000,   30d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 59999,   0.001d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 60000,   0d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 61000,   0d);
        checkTimeRemaining(gameState, FIRST_GAME_START_TIME + 66000,   0d);

        clock.currentTime = SECOND_GAME_START_TIME;
        gameState.startNewGame();
        checkTimeRemaining(gameState, SECOND_GAME_START_TIME + 0,       60d);
        checkTimeRemaining(gameState, SECOND_GAME_START_TIME + 30000,   30d);
        checkTimeRemaining(gameState, SECOND_GAME_START_TIME + 60000,   0d);
        checkTimeRemaining(gameState, SECOND_GAME_START_TIME + 1000000, 0d);
    }
    
    @Test public void reportsAndUpdatesGamesRemaining() {
        assertEquals(TOTAL_GAMES_TO_PLAY, gameState.getGamesRemaining());
        gameState.finishGame();
        assertEquals(3, gameState.getGamesRemaining());
        gameState.finishGame();
        assertEquals(2, gameState.getGamesRemaining());
        gameState.finishGame();
        assertEquals(1, gameState.getGamesRemaining());
        gameState.finishGame();
        assertEquals(0, gameState.getGamesRemaining());
    }
    
    @Test public void usesAppropriateThreadsafeStorage() {
        Mockery context = new Mockery();
        final ConcurrentStorageProvider storageProvider = context.mock(ConcurrentStorageProvider.class);
        final ConsistentInteger gamesRemaining = context.mock(ConsistentInteger.class);
        final ConsistentLong time = context.mock(ConsistentLong.class);
        
        context.checking(new Expectations() {{
            oneOf(storageProvider).makeConsistentInteger(); will(returnValue(gamesRemaining));
            oneOf(storageProvider).makeConsistentLong(); will(returnValue(time));
            oneOf(gamesRemaining).set(TOTAL_GAMES_TO_PLAY);
        }});
        gameState = new GameState(clock, GAME_DURATION_IN_MILLIS, TOTAL_GAMES_TO_PLAY, storageProvider);
        context.assertIsSatisfied();
        
        context.checking(new Expectations() {{
            oneOf(time).set(FIRST_GAME_START_TIME + GAME_DURATION_IN_MILLIS);
        }});
        clock.currentTime = FIRST_GAME_START_TIME;
        gameState.startNewGame();
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(gamesRemaining).get(); will(returnValue(TOTAL_GAMES_TO_PLAY));
            oneOf(gamesRemaining).set(TOTAL_GAMES_TO_PLAY - 1); 
        }});
        gameState.finishGame();
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(time).set(SECOND_GAME_START_TIME + GAME_DURATION_IN_MILLIS);
        }});
        clock.currentTime = SECOND_GAME_START_TIME;
        gameState.startNewGame();
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf(gamesRemaining).get(); will(returnValue(TOTAL_GAMES_TO_PLAY - 1));
            oneOf(gamesRemaining).set(TOTAL_GAMES_TO_PLAY - 2); 
        }});
        gameState.finishGame();
        context.assertIsSatisfied();
    }

    private void checkTimeRemaining(GameState gameState, final long currentTime, 
                                    double expectedTimeRemainingInSeconds) {
        clock.currentTime = currentTime;
        assertEquals(expectedTimeRemainingInSeconds, gameState.getTimeRemainingInSeconds());
    }
}
