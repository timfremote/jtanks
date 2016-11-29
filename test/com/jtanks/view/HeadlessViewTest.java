package com.jtanks.view;

import java.awt.Color;
import java.io.StringWriter;
import java.util.Set;
import java.util.TreeSet;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

import static junit.framework.Assert.assertEquals;

public class HeadlessViewTest {
    private static final String TANK_1_NAME = "Tank 1";
    private static final String TANK_2_NAME = "Tank 2";
    private static final int TANK_1_SCORE = 3;
    private static final int TANK_2_SCORE = 2;
    private static final String EXPECTED_SCORE_OUTPUT = TANK_1_NAME + " score: " + TANK_1_SCORE + "\n"
                                                      + TANK_2_NAME + " score: " + TANK_2_SCORE + "\n";
    
    private Mockery context = new Mockery();
    private TankStockist tankStockist = context.mock(TankStockist.class);
    private GameStatusReporter reporter = context.mock(GameStatusReporter.class);
    private GoListener listener1 = context.mock(GoListener.class, "listener1");
    private GoListener listener2 = context.mock(GoListener.class, "listener2");

    private Tank tank1 = new Tank(Color.RED, new GameConfiguration());
    private Tank tank2 = new Tank(Color.BLUE, new GameConfiguration());
    private Set<Tank> tanks = new TreeSet<Tank>();
    private StringWriter out = new StringWriter();
    
    @Before public void setUp() {
        tank1.setName(TANK_1_NAME);
        tank1.addToScore(TANK_1_SCORE);
        tank2.setName(TANK_2_NAME);
        tank2.addToScore(TANK_2_SCORE);
        tanks.add(tank1);
        tanks.add(tank2);
        context.checking(new Expectations() {{
            allowing (tankStockist).getTanks(); will(returnValue(tanks));
        }});
    }
    
    @Test public void callsGoListenersOnShow() {
        context.checking(new Expectations() {{
            oneOf (listener1).go();
            oneOf (listener2).go();
        }});
        
        View view = new HeadlessView(reporter, tankStockist, out);
        view.addGoListener(listener1);
        view.addGoListener(listener2);
        view.show();
        
        context.assertIsSatisfied();
    }
    
    @Test public void printsTimeLeftPeriodically() {
        View view = new HeadlessView(reporter, tankStockist, out);
        view.show();

        updateAndCheckTimeIsLogged(view, 44.999d, 3);
        updateAndCheckTimeIsNotLogged(view, 44.234d, 3);
        updateAndCheckTimeIsNotLogged(view, 42d, 3);
        updateAndCheckTimeIsNotLogged(view, 40d, 3);
        updateAndCheckTimeIsLogged(view, 39.999d, 3);
        updateAndCheckTimeIsNotLogged(view, 39.998d, 3);
    }
    
    @Test public void resetsWhenNewGameStarts() {
        View view = new HeadlessView(reporter, tankStockist, out);
        view.show();

        updateAndCheckTimeIsLogged(view, 5.647, 3);
        updateAndCheckTimeIsLogged(view, 0.643, 3);
        updateAndCheckTimeIsLogged(view, 44.843, 2);
    }
    
    @Test public void printsTankScoresWhenDone() {
        View view = new HeadlessView(reporter, tankStockist, out);
        view.show();
        view.done();
        
        assertEquals(EXPECTED_SCORE_OUTPUT, out.toString());
    }

    private void updateAndCheckTimeIsLogged(View view, double time, int gamesRemaining) {
        updateAndCheckLogging(view, time, gamesRemaining, "Time remaining: " + time + " seconds\n");
    }
    
    private void updateAndCheckTimeIsNotLogged(View view, double time, int gamesRemaining) {
        updateAndCheckLogging(view, time, gamesRemaining, "");
    }
    
    private void updateAndCheckLogging(View view, final double time, final int gamesRemaining,
                                       String expectedOutput) 
    {
        int endOfOriginalOutput = out.toString().length();
        
        context.checking(new Expectations() {{
            oneOf (reporter).getTimeRemainingInSeconds(); will(returnValue(time));
            oneOf (reporter).getGamesRemaining(); will(returnValue(gamesRemaining));
        }});
        
        view.update();
        
        context.assertIsSatisfied(); 
        String newOutput = out.toString().substring(endOfOriginalOutput);
        assertEquals(expectedOutput, newOutput);
    }
}
