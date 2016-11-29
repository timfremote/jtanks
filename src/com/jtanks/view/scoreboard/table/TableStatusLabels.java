package com.jtanks.view.scoreboard.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.StatusReporter;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

public class TableStatusLabels implements Iterable<StatusLabel> {
    private GameStatusReporter gameStatusReporter;
    private final StatusTableConfiguration config;
    private List<StatusLabel> labels;

    public TableStatusLabels(StatusTableConfiguration config, TankStockist tankStockist,
                             GameStatusReporter gameStatusReporter) {
        this.config = config;
        this.gameStatusReporter = gameStatusReporter;
        
        labels = new ArrayList<StatusLabel>();
        labels.addAll(makeHeaderLabels());
        for (Tank tank : tankStockist.getTanks()) {
            labels.addAll(makeTankLabels(tank));
        }
        labels.addAll(makeFooterLabels());
    }

    public Iterator<StatusLabel> iterator() { 
        return labels.iterator();
    }

    private List<? extends StatusLabel> makeHeaderLabels() {
        List<? extends StatusLabel> headerLabels = config.getHeaderLabels();
        attachReporterToList(gameStatusReporter, headerLabels);
        return headerLabels;
    }

    private List<? extends StatusLabel> makeTankLabels(Tank tank) {
        List<? extends StatusLabel> tankLabels = config.getLabelsForTankRow();
        attachReporterToList(tank, tankLabels);
        for (StatusLabel label : tankLabels) {
            label.setColor(tank.getColor().darker());
        }
        return tankLabels;
    }

    private List<? extends StatusLabel> makeFooterLabels() {
        List<? extends StatusLabel> footerLabels = config.getFooterLabels();
        attachReporterToList(gameStatusReporter, footerLabels);
        return footerLabels;
    }

    private void attachReporterToList(StatusReporter reporter, List<? extends StatusLabel> labels) {
        for (StatusLabel label : labels) {
            label.connectTo(reporter);
        }
    }
}
