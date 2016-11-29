package com.jtanks.view.arena;

import com.jtanks.model.Explosion;
import com.jtanks.model.ExplosionStockist;

public class ExplosionPainter implements Painter {
    private final ExplosionStockist explosionStockist;
    private EntityPainter entityPainter;

    public ExplosionPainter(ExplosionStockist explosionStockist) {
        this.explosionStockist = explosionStockist;
    }

    public void paint(Drawable drawable) {
        if (null == entityPainter) { throw new IllegalStateException("No entity painter assigned"); }
        for (Explosion explosion : explosionStockist.getExplosions()) {
            entityPainter.paintBaseShape(explosion, drawable);
        }
    }

    public void paintWith(EntityPainter entityPainter) {
        this.entityPainter = entityPainter;
    }
}
