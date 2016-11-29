package com.jtanks.controller.drivers;

import java.awt.Rectangle;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class GameConfigurationTest {

    @Test public void
    shouldReturnEdgesOfGameField() {
        com.jtanks.config.GameConfiguration oldConfig = new com.jtanks.config.GameConfiguration();
        oldConfig.arenaXCoordinate = 100;
        oldConfig.arenaYCoordinate = 101;
        oldConfig.arenaDimension = 102;

        GameConfiguration config = new GameConfiguration(oldConfig);

        assertThat(config.getEdges(), is(new Rectangle(100, 101, 202, 203)));
    }

}
