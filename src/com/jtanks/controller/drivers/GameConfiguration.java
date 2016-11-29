package com.jtanks.controller.drivers;

import java.awt.Rectangle;


public class GameConfiguration {

    private final com.jtanks.config.GameConfiguration oldConfig;

    public GameConfiguration(com.jtanks.config.GameConfiguration oldConfig) {
        this.oldConfig = oldConfig;
    }

    public Rectangle getEdges() {
        return new Rectangle(oldConfig.arenaXCoordinate,
                             oldConfig.arenaYCoordinate,
                             oldConfig.arenaXCoordinate + oldConfig.arenaDimension,
                             oldConfig.arenaYCoordinate + oldConfig.arenaDimension);
    }

}
