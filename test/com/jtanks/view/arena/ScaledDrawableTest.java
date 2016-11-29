package com.jtanks.view.arena;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.util.Point;

public class ScaledDrawableTest {
    private static final double SCALING_FACTOR = 3d;
    private static final double DIAMETER = 3;
    private static final double SCALED_DIAMETER = 9;
    private static final Color COLOR = Color.RED;

    private Mockery context = new Mockery();
    private Drawable mockDrawable = context.mock(Drawable.class);
    private ScaledDrawable scaledDrawable;
    private Point point1, point2;
    private Point scaledPoint1, scaledPoint2;

    @Before public void setUp() {
        scaledDrawable = new ScaledDrawable(mockDrawable, SCALING_FACTOR);
        point1 = new Point(1, 2);
        scaledPoint1 = new Point(3, 6);
        point2 = new Point(3, 4);
        scaledPoint2 = new Point(9, 12);
    }

    @Test public void scalesWhenDrawingLine() {
        context.checking(new Expectations() {{
            oneOf (mockDrawable).drawLine(scaledPoint1, scaledPoint2);
        }});
        scaledDrawable.drawLine(point1, point2);
        context.assertIsSatisfied();
    }
    
    @Test public void scalesWhenDrawingemptyCircle() {
        context.checking(new Expectations() {{
            oneOf (mockDrawable).drawCircle(scaledPoint1, SCALED_DIAMETER);
        }});
        scaledDrawable.drawCircle(point1, DIAMETER);
        context.assertIsSatisfied();
    }
    
    @Test public void scalesWhenDrawingFilledCircle() {
        context.checking(new Expectations() {{
            oneOf (mockDrawable).fillCircle(scaledPoint1, SCALED_DIAMETER);
        }});
        scaledDrawable.fillCircle(point1, DIAMETER);
        context.assertIsSatisfied();
    }
    
    @Test public void scalesWhenDrawingPoint()  {
        context.checking(new Expectations() {{
            oneOf (mockDrawable).drawPoint(scaledPoint1);
        }});
        scaledDrawable.drawPoint(point1);
        context.assertIsSatisfied();
    }
    
    @Test public void delegatesSettingColor() {
        context.checking(new Expectations() {{
            oneOf (mockDrawable).setColor(COLOR);
        }});
        scaledDrawable.setColor(COLOR);
        context.assertIsSatisfied();
    }    
}
