package com.jtanks.controller;

import com.jtanks.model.GameState;
import com.jtanks.view.GoListener;
import com.jtanks.view.View;

public class TournamentRunner implements GoListener {
    private static final long RELAX_TIME = 5;
    
    private final GameRunner gameRunner;
    private final View view;
    private final GameState gameState;

    public TournamentRunner(GameRunner gameRunner, View view, GameState gameState) {
        this.gameRunner = gameRunner;
        this.view = view;
        this.gameState = gameState;
    }
    
    public void go() {
        Thread tournamentRunner = createTournamentRunnerThread();
        Thread updateViewThread = createUpdateViewThread();
        
        tournamentRunner.start();
        updateViewThread.start();
    }
    
    private Thread createTournamentRunnerThread() {
        Thread tournamentRunner = new Thread(new Runnable() {
            
            public void run() {
                try {
                    while (gameState.getGamesRemaining() > 0) {
                        gameRunner.reset();
                        gameRunner.startTankDrivers();
                        
                        while (gameRunner.isGameRunning()) {
                            gameRunner.updateArena();
                            relax();
                        }
                        gameRunner.finishRound();
                        gameState.finishGame();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        
        tournamentRunner.setName("Tournament runner");
        return tournamentRunner;
    }
    
    public Thread createUpdateViewThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (gameState.getGamesRemaining() > 0) {
                        view.update();
                        relax();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        
        thread.setName("Update view");
        return thread;
    }
    
    public void relax() {
        try {
            Thread.sleep(RELAX_TIME);
        } catch (InterruptedException ex) {}
    }
}
