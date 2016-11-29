package com.jtanks.view.scoreboard.table;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.model.StatusReporter;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTLabel;

public class GenericStatusLabelTest {
    private static final Color COLOR = Color.RED;
    private static final String TEXT = "text";
    private static final String NO_REPORTER_TEXT = "no reporter";

    Mockery context = new Mockery();
    private JTLabel label = context.mock(JTLabel.class);
    private JTFrame frame = context.mock(JTFrame.class);
    private StatusReporter reporter = context.mock(StatusReporter.class);
    private GUIStyle style = context.mock(GUIStyle.class);
    private GenericStatusLabel statusLabel;
    
    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (style).makeLabel(); will(returnValue(label));
        }});
        
        statusLabel = new GenericStatusLabel(style); 
    }

    @Test public void addsToFrame() {
        context.checking(new Expectations() {{
            oneOf (label).addTo(frame);
        }});
        statusLabel.addTo(frame);
        context.assertIsSatisfied();
    }
    
    @Test public void delegatesSetColor() {
        context.checking(new Expectations() {{
            oneOf (label).setForeground(COLOR);
        }});
        statusLabel.setColor(COLOR);
        context.assertIsSatisfied();
    }
    
    @Test public void updateUsesGetTextFrom() {
        statusLabel = new GenericStatusLabel(style) {
            @Override
            protected String getTextFrom(StatusReporter reporterArg) {
                junit.framework.Assert.assertEquals(reporter, reporterArg);
                return TEXT;
            }
        };
        context.checking(new Expectations() {{
            oneOf (label).setText(TEXT);
        }});
        statusLabel.connectTo(reporter);
        statusLabel.update();
        context.assertIsSatisfied();
    }
    
    @Test public void updateGivesErrorTextIfNoReporter() {
        context.checking(new Expectations() {{
            oneOf (label).setText(NO_REPORTER_TEXT);
        }});
        statusLabel.update();
    }
}
