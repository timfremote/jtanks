package com.jtanks.view.scoreboard;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.view.GoListener;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGridLayout;

public class ScoreBoardTest {
    private static final int NUMBER_OF_ROWS_IN_TABLE = 8;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 200;
    private static final int SCOREBOARD_X = 31;
    private static final int SCOREBOARD_Y = 41;

    Mockery context = new Mockery();
    private JTFrame frame = context.mock(JTFrame.class);
    private GoListener listener = context.mock(GoListener.class);
    private StatusDisplay statusDisplay = context.mock(StatusDisplay.class);
    private JTButton goButton = context.mock(JTButton.class);
    private GUIStyle style = context.mock(GUIStyle.class);
    private JTGridLayout gridLayout = context.mock(JTGridLayout.class);
    private GameConfiguration config = new GameConfiguration();

    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (statusDisplay).getNumberOfRows(); will(returnValue(NUMBER_OF_ROWS_IN_TABLE));
            allowing (frame).getHeight(); will(returnValue(HEIGHT));
            allowing (style).makeGridLayout(); will(returnValue(gridLayout));
            allowing (style).makeButton(); will(returnValue(goButton));
            allowing (style).makeFrame(); will(returnValue(frame));
        }});
        config.scoreBoardWidth = WIDTH;
        config.scoreBoardXCoordinate =SCOREBOARD_X;
        config.scoreBoardYCoordinate = SCOREBOARD_Y;
    }

    @Test public void setsSizeAndLayout() {
        final Sequence layout = context.sequence("layout");
        context.checking(new Expectations() {{
            ignoring (statusDisplay); ignoring (goButton);

            oneOf (gridLayout).setRows(NUMBER_OF_ROWS_IN_TABLE + 1); inSequence(layout);
            oneOf (frame).setLayout(gridLayout);                     inSequence(layout);
            oneOf (frame).setSize(WIDTH, HEIGHT);
        }});
        new ScoreBoard(statusDisplay, goButton, style, config);
        context.assertIsSatisfied();
    }
    
    @Test public void addsStatusDisplay() {
        context.checking(new Expectations() {{
            ignoring (gridLayout); ignoring (frame);  ignoring (goButton);
            oneOf (statusDisplay).addTo(frame);
        }});
        new ScoreBoard(statusDisplay, goButton, style, config);
        context.assertIsSatisfied(); 
    }
    
    @Test public void addsGoButton() {
        context.checking(new Expectations() {{
            ignoring (gridLayout); ignoring (frame); ignoring (statusDisplay);
            oneOf (goButton).addTo(frame);
        }});
        new ScoreBoard(statusDisplay, goButton, style, config);
        context.assertIsSatisfied();
    }
    
    @Test public void showsWindowAtConfiguredLocation() {
        final ScoreBoard scoreBoard = makeScoreBoard();
        
        context.checking(new Expectations() {{
            oneOf (frame).pack();
            oneOf (frame).setVisible(true);
            oneOf (frame).setLocation(SCOREBOARD_X, SCOREBOARD_Y);
        }});

        scoreBoard.show();
        context.assertIsSatisfied();
    }
    
    @Test public void updatesDisplay() {
        final ScoreBoard scoreBoard = makeScoreBoard();

        context.checking(new Expectations() {{
            oneOf (statusDisplay).update();
        }});

        scoreBoard.update();
        context.assertIsSatisfied();
    }
    
    @Test public void clickingGoButtonTriggersListener() {
        final ScoreBoard scoreBoard = makeScoreBoard();

        context.checking(new Expectations() {{
            oneOf (goButton).addListener(scoreBoard);
            oneOf (listener).go();
        }});
        
        scoreBoard.addGoListener(listener);
        scoreBoard.click();
        context.assertIsSatisfied();
    }

    private ScoreBoard makeScoreBoard() {
        context.checking(new Expectations() {{
            oneOf (gridLayout).setRows(NUMBER_OF_ROWS_IN_TABLE + 1);
            oneOf (frame).setLayout(gridLayout);                     
            oneOf (frame).setSize(WIDTH, HEIGHT);
            oneOf (statusDisplay).addTo(frame);
            oneOf (goButton).addTo(frame);
        }});
        return new ScoreBoard(statusDisplay, goButton, style, config);
    }
}
