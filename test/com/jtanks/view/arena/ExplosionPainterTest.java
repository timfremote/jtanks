package com.jtanks.view.arena;

import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.model.Explosion;
import com.jtanks.model.ExplosionStockist;
import com.jtanks.util.Point;

public class ExplosionPainterTest {
    private Mockery context = new Mockery();
    private Drawable drawable = context.mock(Drawable.class);
    private ExplosionStockist explosionStockist = context.mock(ExplosionStockist.class);
    private EntityPainter entityPainter = context.mock(EntityPainter.class);
    private final Set<Explosion> explosions = new HashSet<Explosion>();
    private ExplosionPainter painter;
    
    @Before public void setUp() {
        explosions.add(new Explosion(new Point(20, 30)));
        explosions.add(new Explosion(new Point(25, 90)));

        context.checking(new Expectations() {{
            allowing (explosionStockist).getExplosions(); will(returnValue(explosions));
        }});
        
        painter = new ExplosionPainter(explosionStockist);
    }
    
    @Test public void paintsEachExplosion() {
        painter.paintWith(entityPainter);
        expectationsForDrawing((Explosion) explosions.toArray()[0]);
        expectationsForDrawing((Explosion) explosions.toArray()[1]);
        painter.paint(drawable);
        context.assertIsSatisfied();
    }
    
    @Test(expected=IllegalStateException.class) public void throwsExceptionIfNoPainterAssigned() {
        painter.paint(drawable);
    }

    private void expectationsForDrawing(final Explosion explosion) {
        context.checking(new Expectations() {{
            oneOf (entityPainter).paintBaseShape(explosion, drawable);
        }});
    }
}

