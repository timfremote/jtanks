package com.jtanks.view.arena;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

public class TankPainterTest {
    protected static final int RANGE = 75;
    protected static final int CANNON_LENGTH = 5;
    
    private Mockery context = new Mockery();
    private Drawable drawable = context.mock(Drawable.class);
    private EntityPainter entityPainter = context.mock(EntityPainter.class);
    private TankStockist tankStockist = context.mock(TankStockist.class);

    private Set<Tank> tanks = new HashSet<Tank>();
    private TankPainter painter;
    
    @Before public void setUp() {
        GameConfiguration config = new GameConfiguration();
        config.cannonLength = CANNON_LENGTH;
        config.radarRange = RANGE;
        tanks.add(new Tank(Color.RED, config));
        tanks.add(new Tank(Color.BLUE, config));
        context.checking(new Expectations() {{
            allowing (tankStockist).getTanks(); will(returnValue(tanks));
        }});
        
        painter = new TankPainter(tankStockist, config);
    }
    
    @Test public void paintsEachTank() {
        painter.paintWith(entityPainter);
        for (Tank tank : tanks) {
            expectationsFor(tank);
        }
        painter.paint(drawable);
        context.assertIsSatisfied();
    }
    
    @Test(expected=IllegalStateException.class) public void throwsExceptionIfNoPainterAssigned() {
        painter.paint(drawable);
    }

    private void expectationsFor(final Tank tank) {
        final States baseDrawn = context.states("baseDrawn").startsAs("false");
        context.checking(new Expectations() {{
            oneOf (entityPainter).paintRangeCircle(tank, drawable, RANGE); 
            oneOf (entityPainter).paintCannon(tank, drawable, CANNON_LENGTH);       when(baseDrawn.is("false"));
            oneOf (entityPainter).paintTrail(tank, drawable);                       when(baseDrawn.is("false"));
            oneOf (entityPainter).paintBaseShape(tank, drawable);                  then(baseDrawn.is("true"));
        }});
    }
}
