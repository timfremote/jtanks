package com.jtanks.view.scoreboard.table;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.GameStatusReporter;
import com.jtanks.model.HealthReporter;
import com.jtanks.model.ScoreReporter;
import com.jtanks.model.Tank;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTLabel;

public class StandardTableLabelsTest {
    private static final String TEXT = "test";
    private static final String TANK_NAME = "tank name";
    private static final int SCORE           =  29;
    private static final String SCORE_STRING = "29";
    private static final int GAMES_REMAINING           =  2;
    private static final String GAMES_REMAINING_STRING = "2";

    private Mockery context =  new Mockery();
    private JTLabel label = context.mock(JTLabel.class);
    private GameStatusReporter gameStatusReporter = context.mock(GameStatusReporter.class);
    private HealthReporter healthReporter = context.mock(HealthReporter.class);
    private ScoreReporter scoreReporter = context.mock(ScoreReporter.class);
    private GUIStyle style = context.mock(GUIStyle.class);
    private Tank tank;
    private StandardTableLabels labels;

    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (style).makeLabel(); will(returnValue(label));
        }});
        tank = new Tank(Color.RED, new GameConfiguration());
        tank.setName(TANK_NAME);
        labels = new StandardTableLabels(style);
    }
    
    @Test public void plainLabelDisplaysSpecifiedText() {
        context.checking(new Expectations() {{
            oneOf (label).setText(TEXT); 
        }});
        
        StatusLabel plainLabel = labels.plainLabel(TEXT);
        plainLabel.connectTo(gameStatusReporter);
        plainLabel.update();
        context.assertIsSatisfied();
    }
    
    @Test public void tankNameLabelDisplaysTankName() {
        context.checking(new Expectations() {{
            oneOf (label).setText(TANK_NAME); 
        }});
        StatusLabel tankNameLabel = labels.tankNameLabel();
        tankNameLabel.connectTo(tank);
        tankNameLabel.update();
        context.assertIsSatisfied();
    }
    
    @Test public void healthMeterDisplaysHealth() {
        StatusLabel healthMeter = labels.healthMeter();
        healthMeter.connectTo(healthReporter);
        
        checkHealth(healthMeter, 37, "37");
        checkHealth(healthMeter, 1, "1");
        checkHealth(healthMeter, 0, "DEAD");
        checkHealth(healthMeter, -22, "DEAD"); // Model shouldn't allow, but view should handle
    }
    
    @Test public void scoreMeterDisplaysScore() {
        context.checking(new Expectations() {{
            allowing (scoreReporter).getScore(); will(returnValue(SCORE)); 
            oneOf (label).setText(SCORE_STRING);
        }});
        StatusLabel scoreMeter = labels.scoreMeter();
        scoreMeter.connectTo(scoreReporter);
        scoreMeter.update();
        context.assertIsSatisfied();
    }
    
    @Test public void timeRemainingMeterDisplaysTimeRemaining() {
        StatusLabel timeRemainingMeter = labels.timeRemainingMeter();
        timeRemainingMeter.connectTo(gameStatusReporter);
        
        checkTimeRemaining(timeRemainingMeter, 30.123, "30.123 s");
        checkTimeRemaining(timeRemainingMeter, 30.120, "30.120 s");
        checkTimeRemaining(timeRemainingMeter, 29.000, "29.000 s");
        checkTimeRemaining(timeRemainingMeter, 8.000,  " 8.000 s");
        checkTimeRemaining(timeRemainingMeter, .100,   " 0.100 s");
        checkTimeRemaining(timeRemainingMeter, .001,   " 0.001 s");
        checkTimeRemaining(timeRemainingMeter, 0,      " 0.000 s");
        checkTimeRemaining(timeRemainingMeter, -1.314, ""); // Model should not do this, but view should handle
    }
    
    @Test public void gamesRemainingMeterDisplaysGamesRemaining() {
        context.checking(new Expectations() {{
            allowing (gameStatusReporter).getGamesRemaining(); will(returnValue(GAMES_REMAINING)); 
            oneOf (label).setText(GAMES_REMAINING_STRING);
        }});
        StatusLabel gamesRemainingMeter = labels.gamesRemainingMeter();
        gamesRemainingMeter.connectTo(gameStatusReporter);
        gamesRemainingMeter.update();
        context.assertIsSatisfied();
    }

    private void checkHealth(StatusLabel healthMeter, final int health, final String healthString) {
        context.checking(new Expectations() {{
            oneOf (healthReporter).getHealth(); will(returnValue(health));
            oneOf (label).setText(healthString);
        }});
        healthMeter.update();
        context.assertIsSatisfied();
    }
    
    private void checkTimeRemaining(StatusLabel timeRemainingMeter, final double time, final String timeString) {
        context.checking(new Expectations() {{
            oneOf (gameStatusReporter).getTimeRemainingInSeconds(); will(returnValue(time));
            oneOf (label).setText(timeString);
        }});
        timeRemainingMeter.update();
        context.assertIsSatisfied();
    }
}
