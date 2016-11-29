package com.jtanks.view.scoreboard.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.StatusReporter;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class TableStatusLabelsTest {
    private static final int NUMBER_OF_TANKS = 3;
    private static final Color[] TANK_COLORS = {Color.RED, Color.BLUE, Color.YELLOW};
    
    private Mockery context = new Mockery();
    private StatusTableConfiguration config = context.mock(StatusTableConfiguration.class);
    private TankStockist tankStockist = context.mock(TankStockist.class);
    private GameStatusReporter gameStatusReporter = context.mock(GameStatusReporter.class);
    private Set<Tank> tanks = new HashSet<Tank>();

    private StatusLabel[] headerLabels = new StatusLabel[4];
    private StatusLabel[][] tankLabels = new StatusLabel[NUMBER_OF_TANKS][4];
    private StatusLabel[] footerLabels = new StatusLabel[4];
    private List<StatusLabel> allLabels = new ArrayList<StatusLabel>();

    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (config).getHeaderLabels(); will(returnValue(Arrays.asList(headerLabels)));
            allowing (config).getFooterLabels(); will(returnValue(Arrays.asList(footerLabels)));
            allowing (tankStockist).getTanks(); will(returnValue(tanks));
            ignoring (gameStatusReporter);
        }});
        final Sequence tanks = context.sequence("tanks");
        for (int i = 0; i < NUMBER_OF_TANKS; i++) {
            final StatusLabel[] labelsForRow = tankLabels[i];
            context.checking(new Expectations() {{
                oneOf (config).getLabelsForTankRow(); will(returnValue(Arrays.asList(labelsForRow))); inSequence(tanks);
            }});
        }
        makeTanks();
        makeLabels();
    }
    
    private void makeTanks() {
        GameConfiguration gameConfig = new GameConfiguration();
        for (int i = 0; i < NUMBER_OF_TANKS; i ++) {
            tanks.add(new Tank(TANK_COLORS[i], gameConfig));
        }
    }

    private void makeLabels() {
        makeLabelsForRow(headerLabels);
        for (int i = 0; i < NUMBER_OF_TANKS; i++) {
            makeLabelsForRow(tankLabels[i]);
        }
        makeLabelsForRow(footerLabels);
    }
    
    private void makeLabelsForRow(StatusLabel[] labels) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = context.mock(StatusLabel.class, labels.toString() + i);
        }
        allLabels.addAll(Arrays.asList(labels));
    }
    
    @Test public void attachesReporterToLabels() {
        for (final StatusLabel label : headerLabels) {
            context.checking(new Expectations() {{
                oneOf (label).connectTo(gameStatusReporter);
            }});
        }
        Iterator<Tank> iterator = tanks.iterator();
        for (int i = 0; i < NUMBER_OF_TANKS; i++) {
            final Tank tank = iterator.next();
            for (final StatusLabel label : tankLabels[i]) {
                context.checking(new Expectations() {{
                    allowing (label).setColor(with(any(Color.class)));
                    oneOf (label).connectTo(tank);
                }});
            }
        }
        for (final StatusLabel label : footerLabels) {
            context.checking(new Expectations() {{
                oneOf (label).connectTo(gameStatusReporter);
            }});
        }
        new TableStatusLabels(config, tankStockist, gameStatusReporter);
        context.assertIsSatisfied();
    }
    
    @Test public void tankLabelsHaveDarkerTankColor() {
        Iterator<Tank> iterator = tanks.iterator();
        for (int i = 0; i < NUMBER_OF_TANKS; i++) {
            Tank tank = iterator.next();
            Color tankColor = tank.getColor();
            final Color expectedColor = tankColor.darker();
            for (final StatusLabel label : tankLabels[i]) {
                context.checking(new Expectations() {{
                    oneOf (label).setColor(expectedColor);
                }});
            }
        }
        for (final StatusLabel label : allLabels) {
            context.checking(new Expectations() {{
                allowing (label).connectTo(with(any(StatusReporter.class)));
            }});
        }

        new TableStatusLabels(config, tankStockist, gameStatusReporter);
        context.assertIsSatisfied();
    }
    
    @Test public void iteratesThroughAllLabels() {
        for (final StatusLabel label : allLabels) {
            context.checking(new Expectations() {{
                ignoring (label);
            }});
        }
        TableStatusLabels statusLabels = new TableStatusLabels(config, tankStockist, gameStatusReporter);
        Iterator<StatusLabel> allLabelsIterator = allLabels.iterator();
        for (StatusLabel label : statusLabels) {
            assertEquals(allLabelsIterator.next(), label);
        }
        assertFalse("Should not have any test labels left over", allLabelsIterator.hasNext());
    }
}
