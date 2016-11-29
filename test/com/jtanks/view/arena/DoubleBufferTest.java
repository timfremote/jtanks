package com.jtanks.view.arena;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.Point;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTGraphics;
import com.jtanks.view.gui.JTImage;

public class DoubleBufferTest {
    private static final int X1 = 31;
    private static final int Y1 = 41;
    private static final int X2 = 59;
    private static final int Y2 = 26;
    
    Mockery context = new Mockery();
    private JTCanvas canvas = context.mock(JTCanvas.class);
    private JTImage image = context.mock(JTImage.class);
    private JTGraphics offscreen = context.mock(JTGraphics.class, "offscreen");
    private JTGraphics onscreen = context.mock(JTGraphics.class, "onscreen");
    
    private DoubleBuffer buffer;
    
    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (canvas).createImage(); will(returnValue(image));
            allowing (image).getGraphics();  will(returnValue(offscreen));
        }});
        buffer = new DoubleBuffer(canvas);
    }

    @Test public void drawsOnscreenAndFlushesImage() {
        context.checking(new Expectations() {{
            oneOf (canvas).draw(image, onscreen);
            oneOf (image).flush();
        }});
        buffer.renderIn(onscreen);
        context.assertIsSatisfied();
    }

    @Test public void delegatesDrawMethodsToOffscreenGraphics() {
        Point point1 = new Point(X1, Y1);
        Point point2 = new Point(X2, Y2);
        
        context.checking(new Expectations() {{
            oneOf (offscreen).drawLine(X1, Y1, X2, Y2);
        }});
        buffer.drawLine(point1, point2);
        context.assertIsSatisfied();
        
        context.checking(new Expectations() {{
            oneOf (offscreen).drawOval(X1 - 1, Y1 - 1, 2, 2);
        }});
        buffer.drawCircle(point1, 2);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf (offscreen).fillOval(X1 - 1, Y1 - 1, 2, 2);
        }});
        buffer.fillCircle(point1, 2);
        context.assertIsSatisfied();

        context.checking(new Expectations() {{
            oneOf (offscreen).drawLine(X1, Y1, X1, Y1);
        }});
        buffer.drawPoint(point1);
        context.assertIsSatisfied();
        
        context.checking(new Expectations() {{
            oneOf (offscreen).setColor(Color.RED);
        }});
        buffer.setColor(Color.RED);
        context.assertIsSatisfied();
    }
}
