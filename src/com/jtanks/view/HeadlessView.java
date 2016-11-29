package com.jtanks.view;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

public class HeadlessView implements View {
    private static final double TIME_BETWEEN_OUTPUTS = 5d;
    
    private List<GoListener> listeners = new ArrayList<GoListener>();
    private final GameStatusReporter reporter;
    private final TankStockist tankStockist;
    private final Writer out;

    private double timeAtLastPrint;
    private int gamesRemainingAtLastPrint;

    public HeadlessView(GameStatusReporter reporter, TankStockist tankStockist, Writer out) {
        this.reporter = reporter;
        this.tankStockist = tankStockist;
        this.out = out;
        timeAtLastPrint = Double.MAX_VALUE;
    }

    public void addGoListener(GoListener listener) {
        listeners.add(listener);
    }

    public void show() {
        for (GoListener listener : listeners) {
            listener.go();
        }
    }

    public void update() {
        double time = reporter.getTimeRemainingInSeconds();
        int gamesRemaining = reporter.getGamesRemaining();

        if (shouldNotPrint(time, gamesRemaining)) {
            return;
        }

        print("Time remaining: " + time + " seconds\n");

        timeAtLastPrint = time;
        gamesRemainingAtLastPrint = gamesRemaining;
    }

    public void done() {
        Collection<Tank> tanks = tankStockist.getTanks();
        for (Tank tank : tanks) {
            print(tank.getName() + " score: " + tank.getScore() + "\n");
        }
    }

    private void print(String text) {
        try {
            out.append(text);
            out.flush();
        } catch (IOException e) {
            // Do nothing - just keep going
        }
    }

    private boolean shouldNotPrint(double time, int gamesRemaining) {
        double timeSinceLastPrinted = timeAtLastPrint - time;
        return gamesRemaining == gamesRemainingAtLastPrint
               &&
               timeSinceLastPrinted < TIME_BETWEEN_OUTPUTS;
    }
}
