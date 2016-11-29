package com.jtanks.view.arena;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.model.Mine;
import com.jtanks.model.MineStockist;
import com.jtanks.util.Point;

public class MinePainterTest {
    private Mockery context = new Mockery();
    private Drawable drawable = context.mock(Drawable.class);
    private EntityPainter entityPainter = context.mock(EntityPainter.class);
    private MineStockist mineStockist = context.mock(MineStockist.class);

    private Set<Mine> mines = new HashSet<Mine>();
    private MinePainter painter;
    
    @Before public void setUp() {
        mines.add(new Mine(Color.RED, new Point(5, 10)));
        mines.add(new Mine(Color.BLUE, new Point(15, 110)));
        context.checking(new Expectations() {{
            allowing (mineStockist).getMines(); will(returnValue(mines));
        }});
        
        painter = new MinePainter(mineStockist);
    }
    
    @Test public void paintsEachMine() {
        painter.paintWith(entityPainter);
        for (Mine mine : mines) {
            expectationsFor(mine);
        }
        painter.paint(drawable);
        context.assertIsSatisfied();
    }
    
    @Test(expected=IllegalStateException.class) public void throwsExceptionIfNoPainterAssigned() {
        painter.paint(drawable);
    }

    private void expectationsFor(final Mine mine) {
        context.checking(new Expectations() {{
            Sequence drawing = context.sequence("drawing");
            oneOf (entityPainter).paintBaseShape(mine, drawable);     inSequence(drawing);
            oneOf (entityPainter).paintSmallHighlight(mine, drawable); inSequence(drawing);
        }});
    }
}