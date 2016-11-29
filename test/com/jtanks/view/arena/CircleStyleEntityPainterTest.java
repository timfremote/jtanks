package com.jtanks.view.arena;

import java.awt.Color;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.model.MovingEntityWithTrail;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

import static java.lang.Math.PI;

public class CircleStyleEntityPainterTest {
    private static final Color BASE_COLOR = Color.RED;
    private static final Color DULL_COLOR = BASE_COLOR.darker().darker();
    private static final Color BRIGHT_COLOR = BASE_COLOR.brighter().brighter();
    private static final Color HIGHLIGHT_COLOR = Color.DARK_GRAY;
    private static final int RANGE = 40;
    private static final double SIZE = 10d;
    private static final double HALF_SIZE = 5d;
    private static final Point POSITION = new Point(10d, 20d);
    private static final Point[] TRAIL = {new Point(9d, 19d), new Point(9d, 18d), new Point(9d, 20d)};
    private static final int CANNON_LENGTH = 4;
    private static final double EPSILON = 0.001;
    
    private Mockery context = new Mockery();
    private Drawable drawable = context.mock(Drawable.class);
    private MovingEntityWithTrail entity = context.mock(MovingEntityWithTrail.class);
    private CircleStyleEntityPainter painter;
    
    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (entity).getColor();         will(returnValue(BASE_COLOR));
            allowing (entity).getPosition();      will(returnValue(POSITION));
            allowing (entity).getSize();          will(returnValue(SIZE));
            allowing (entity).getTrail();         will(returnValue(Arrays.asList(TRAIL)));
        }});
        painter = new CircleStyleEntityPainter();
    }
    
    @Test public void fillsBaseCircleForLiveEntity() {
        final Sequence drawing = context.sequence("drawing");
        entityIsAlive();
        context.checking(new Expectations() {{
            oneOf (drawable).setColor(BASE_COLOR);       inSequence(drawing);
            oneOf (drawable).fillCircle(POSITION, SIZE); inSequence(drawing);
        }});
        painter.paintBaseShape(entity, drawable);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsEmptyBaseCircleForDeadEntity() {
        final Sequence drawing = context.sequence("drawing");
        entityIsDead();
        context.checking(new Expectations() {{
            oneOf (drawable).setColor(BASE_COLOR);       inSequence(drawing);
            oneOf (drawable).drawCircle(POSITION, SIZE); inSequence(drawing);
        }});
        painter.paintBaseShape(entity, drawable);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsHalfSizeGreyCircleForHighlight() {
        final Sequence drawing = context.sequence("drawing");
        context.checking(new Expectations() {{
            oneOf (drawable).setColor(HIGHLIGHT_COLOR);       inSequence(drawing);
            oneOf (drawable).fillCircle(POSITION, HALF_SIZE); inSequence(drawing);
        }});
        painter.paintSmallHighlight(entity, drawable);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsTrail() {
        final States colorSet = context.states("colorSet").startsAs("false");
        context.checking(new Expectations() {{
            oneOf (drawable).setColor(DULL_COLOR); then(colorSet.is("true"));            
        }});
        for (final Point trailPoint : TRAIL) {
            context.checking(new Expectations() {{
                oneOf (drawable).drawPoint(trailPoint); when(colorSet.is("true"));
            }});
        }
        painter.paintTrail(entity, drawable);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsRangeCircleForLiveEntity() {
        final Sequence drawing = context.sequence("drawing");
        entityIsAlive();
        context.checking(new Expectations() {{
            oneOf (drawable).setColor(DULL_COLOR);                 inSequence(drawing);
            oneOf (drawable).drawCircle(POSITION, RANGE * 2); inSequence(drawing);
        }});
        painter.paintRangeCircle(entity, drawable, RANGE);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsNoRangeCircleForDeadEntity() {
        entityIsDead();
        painter.paintRangeCircle(entity, drawable, RANGE);
        context.assertIsSatisfied();
    }
    
    @Test public void drawsCannon() {
        drawAndCheckCannon(0d, new Point(14d, 20d));
        drawAndCheckCannon(PI, new Point(6d, 20d));
        drawAndCheckCannon(PI/2, new Point(10d, 16d));
        drawAndCheckCannon(PI/4, new Point(12.828d, 17.171d));
        drawAndCheckCannon(-PI/8, new Point(13.695d, 21.531d));
    }

    private void drawAndCheckCannon(final double heading, final Point cannonEnd) {
        final Sequence drawing = context.sequence("drawing");
        context.checking(new Expectations() {{
            oneOf (entity).getHeading(); will(returnValue(heading)); 
            
            oneOf (drawable).setColor(BRIGHT_COLOR);        inSequence(drawing);
            oneOf (drawable).drawLine(with(equal(POSITION)), 
                                      with(aPointCloseTo(cannonEnd, EPSILON))); 
                                                            inSequence(drawing);
        }});
        painter.paintCannon(entity, drawable, CANNON_LENGTH);
        context.assertIsSatisfied();
    }

    private void entityIsAlive() { setIsDestroyedTo(false); }
    private void entityIsDead()  { setIsDestroyedTo(true); }

    private void setIsDestroyedTo(final boolean destroyedValue) {
        context.checking(new Expectations() {{
            allowing (entity).isDestroyed(); will(returnValue(destroyedValue));
        }});
    }
    
    public static class PointCloseToMatcher extends TypeSafeMatcher<Point> {
        private final Point target;
        private final double epsilon;

        public PointCloseToMatcher(Point target, double epsilon) {
            this.target = target;
            this.epsilon = epsilon;
        }

        @Override
        public boolean matchesSafely(Point given) {
            return (epsilon > Euclid.distanceBetweenPoints(given, target)); 
        }
        
        
        public void describeTo(Description description) {
            description.appendText("a point within ").appendValue(epsilon)
                       .appendText(" of ").appendValue(target);
        }
    }
    
    @Factory
    public static Matcher<Point> aPointCloseTo(Point target, double epsilon) {
        return new PointCloseToMatcher(target, epsilon);
    }
}
