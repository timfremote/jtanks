package com.jtanks.view.scoreboard.table;

import java.util.List;

public interface StatusTableConfiguration {
    List<? extends StatusLabel> getHeaderLabels();
    List<? extends StatusLabel> getLabelsForTankRow();
    List<? extends StatusLabel> getFooterLabels();
}
