package com.jtanks.view.scoreboard.table;

import com.jtanks.model.TankStockist;
import com.jtanks.model.GameStatusReporter;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.scoreboard.StatusDisplay;

public class TableStatusDisplay implements StatusDisplay {
    private Iterable<? extends StatusLabel> labels;
    private int numberOfRows;

    public TableStatusDisplay(StatusTableConfiguration config, 
                              TankStockist tankStockist, GameStatusReporter gameStatusReporter) {
        this(new TableStatusLabels(config, tankStockist, gameStatusReporter), tankStockist);
    }
    
    public TableStatusDisplay(Iterable<? extends StatusLabel> labels, TankStockist tankStockist) {
        this.labels = labels;
        this.numberOfRows = 2 + tankStockist.getTanks().size();
    }

    public void addTo(JTFrame frame) {
        for (StatusLabel label : labels) {
            label.addTo(frame);
        }
    }

    public void update() {
        for (StatusLabel label : labels) {
            label.update();
        }
    }
    
    public int getNumberOfRows() { return numberOfRows; }
}
