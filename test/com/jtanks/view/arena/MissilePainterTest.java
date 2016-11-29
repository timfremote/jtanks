package com.jtanks.view.arena;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Missile;
import com.jtanks.model.MissileStockist;
import com.jtanks.util.Point;

public class MissilePainterTest {
    private Mockery context = new Mockery();
    private Drawable drawable = context.mock(Drawable.class);
    private EntityPainter entityPainter = context.mock(EntityPainter.class);
    private MissileStockist missileStockist = context.mock(MissileStockist.class);
    
    protected Set<Missile> missiles;
    private MissilePainter painter;
    
    @Before public void setUp() {
        missiles = new HashSet<Missile>(); 
        missiles.add(new Missile(Color.RED, new Point(0, 0), 0, null, new GameConfiguration()));
        missiles.add(new Missile(Color.BLUE, new Point(10, 20), 180, null, new GameConfiguration()));
        context.checking(new Expectations() {{
            allowing (missileStockist).getMissiles(); will(returnValue(missiles));
        }});
        painter = new MissilePainter(missileStockist);
    }
    
    @Test public void paintsEachMissile() {
        painter.paintWith(entityPainter);
        expectationsForDrawing((Missile) missiles.toArray()[0]);
        expectationsForDrawing((Missile) missiles.toArray()[1]);
        painter.paint(drawable);
        context.assertIsSatisfied();
    }
    
    @Test(expected=IllegalStateException.class) public void throwsExceptionIfNoPainterAssigned() {
        painter.paint(drawable);
    }

    private void expectationsForDrawing(final Missile missile) {
        context.checking(new Expectations() {{
            oneOf (entityPainter).paintBaseShape(missile, drawable);
        }});
    }
}


