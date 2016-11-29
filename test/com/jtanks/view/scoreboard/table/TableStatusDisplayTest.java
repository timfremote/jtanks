package com.jtanks.view.scoreboard.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.scoreboard.table.StatusLabel;
import com.jtanks.view.scoreboard.table.TableStatusDisplay;

import static junit.framework.Assert.assertEquals;

@SuppressWarnings("serial")
public class TableStatusDisplayTest {
    Mockery context = new Mockery();
    private JTFrame frame = context.mock(JTFrame.class);
    private List<StatusLabel> labels = new ArrayList<StatusLabel>() {{
        add(context.mock(StatusLabel.class, "label1"));
        add(context.mock(StatusLabel.class, "label2"));
    }};
    private final Tank[] tanks = {new Tank(Color.RED, new GameConfiguration()), 
                                  new Tank(Color.BLUE, new GameConfiguration())};
    private TableStatusDisplay statusDisplay;

    @Before public void setUp() {
        TankStockist tankStockist = new TankStockist() {
            public Set<Tank> getTanks() { return new HashSet<Tank>(Arrays.asList(tanks)); }
        };
        statusDisplay = new TableStatusDisplay(labels, tankStockist);
    }
    
    @Test public void addsAllLabelsInOrderToJTFrame() {
        for (final StatusLabel label : labels) {
            context.checking(new Expectations() {{
                oneOf (label).addTo(frame);
            }});
        }
        statusDisplay.addTo(frame);
        context.assertIsSatisfied();
    }
    
    @Test public void updatesAllLabels() {
        for (final StatusLabel label : labels) {
            context.checking(new Expectations() {{
                oneOf (label).update();
            }});
        }
        statusDisplay.update();
        context.assertIsSatisfied();
    }
    
    @Test public void reportsCorrectNumberOfRows() {
        assertEquals(tanks.length + 2, statusDisplay.getNumberOfRows());
    }
}
