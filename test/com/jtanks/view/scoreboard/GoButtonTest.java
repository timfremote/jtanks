package com.jtanks.view.scoreboard;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTButton;
import com.jtanks.view.gui.JTButtonListener;
import com.jtanks.view.gui.JTFrame;

public class GoButtonTest {
    Mockery context = new Mockery();
    JTFrame frame = context.mock(JTFrame.class);
    GUIStyle style = context.mock(GUIStyle.class);
    private JTButton button = context.mock(JTButton.class);
    private JTButtonListener listener = context.mock(JTButtonListener.class);
    
    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (style).makeButton(); will(returnValue(button));
        }});
    }

    @Test public void hasCorrectTextOnConstruction() {
        context.checking(new Expectations() {{ 
            oneOf (button).setText("Go");
        }});
        new GoButton(style);
        context.assertIsSatisfied();
    }
    
    @Test public void addsSelfAsListener() {
        final GoButton goButton = makeGoButton();

        context.checking(new Expectations() {{ 
            oneOf (button).addListener(goButton);
        }});
        goButton.addListener(listener);
        context.assertIsSatisfied();
    }
    
    @Test public void clickingDisablesButton() {
        GoButton goButton = makeGoButton();
        
        context.checking(new Expectations() {{
            oneOf (button).disable();
        }});
        goButton.click();
        context.assertIsSatisfied();
    }
    
    @Test public void callsListenersOnClick() {
        GoButton goButton = makeGoButton();
        
        context.checking(new Expectations() {{
            ignoring (button);
            oneOf (listener).click();
        }});
        goButton.addListener(listener);
        goButton.click();
        context.assertIsSatisfied();
    }
    
    @Test public void addsToFrame() {
        GoButton goButton = makeGoButton();
        
        context.checking(new Expectations() {{
            oneOf (button).addTo(frame);
        }});
        goButton.addTo(frame);
        context.assertIsSatisfied();
    }
    
    @Test(expected=UnsupportedOperationException.class) public void disableNotPermitted() {
        GoButton goButton = makeGoButton();
        goButton.disable();
    }
    
    @Test(expected=UnsupportedOperationException.class) public void setTextNotPermitted() {
        GoButton goButton = makeGoButton();
        goButton.setText("foo");
    }
    
    private GoButton makeGoButton() {
        context.checking(new Expectations() {{
            allowing (button).setText(with(any(String.class)));
        }});
        return new GoButton(style);
    }
}
