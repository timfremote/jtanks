package com.jtanks.controller;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.ConcurrentQuartermaster;
import com.jtanks.model.GameState;
import com.jtanks.model.Quartermaster;
import com.jtanks.model.Tank;
import com.jtanks.util.SystemClock;
import com.jtanks.view.HeadlessView;
import com.jtanks.view.TopDownView;
import com.jtanks.view.View;

import java.awt.Color;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final int DEFAULT_NUMBER_OF_GAMES = 3;
    private static final int MAXIMUM_GAME_TIME = 1 * 40000;

    public static void main(String[] args) {
        Thread.currentThread().setName("Main");

        GameConfiguration config = new GameConfiguration();
        Scorer scorer = new DefaultScorer();
        GameState gameState = new GameState(new SystemClock(), MAXIMUM_GAME_TIME, DEFAULT_NUMBER_OF_GAMES);

        Quartermaster quartermaster = new ConcurrentQuartermaster(getTanks(config));

        GameRunner gameRunner = new GameRunner(scorer, gameState, quartermaster, new SystemClock(), config);
        View view = getView(args, gameState, quartermaster, config);

        TournamentRunner tournamentRunner = new TournamentRunner(gameRunner, view, gameState);
        view.addGoListener(tournamentRunner);
        view.show();
        while (gameState.getGamesRemaining() > 0) { tournamentRunner.relax(); }
        view.done();
    }

    public static Set<Tank> getTanks(GameConfiguration config) {
        Tank[] tanks = {new Tank(Color.RED, config),
                        new Tank(Color.BLUE, config),
                        new Tank(Color.YELLOW, config),
                        new Tank(Color.GREEN, config),
                        new Tank(Color.WHITE, config),
                        new Tank(Color.CYAN, config),
                        new Tank(Color.ORANGE, config),
                        new Tank(Color.LIGHT_GRAY, config)
                        };
        return new HashSet<Tank>(Arrays.asList(tanks));
    }

    private static View getView(String[] args, GameState gameState,
                                Quartermaster quartermaster, GameConfiguration config)
    {
        View view;
        if (isHeadless(args)) {
            System.out.println("Running headless");
            view = new HeadlessView(gameState, quartermaster, new OutputStreamWriter(System.out));
        } else {
            System.out.println("Running with graphics");
            view = new TopDownView(gameState, quartermaster, config);
        }
        return view;
    }

    private static boolean isHeadless(String[] args) {
        if (0 == args.length) { return false; }
        return (args[0].equalsIgnoreCase("--headless"));
    }
}
