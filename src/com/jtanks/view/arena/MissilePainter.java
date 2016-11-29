package com.jtanks.view.arena;

import com.jtanks.model.Missile;
import com.jtanks.model.MissileStockist;

public class MissilePainter implements Painter {
    private final MissileStockist missileStockist;
    private EntityPainter entityPainter;

    public MissilePainter(MissileStockist missileStockist) {
        this.missileStockist = missileStockist;
    }

    public void paint(Drawable drawable) {
        if (null == entityPainter) { throw new IllegalStateException("No entity painter assigned"); }
        for (Missile missile : missileStockist.getMissiles()) {
            entityPainter.paintBaseShape(missile, drawable);
        }
    }

    public void paintWith(EntityPainter entityPainter) {
        this.entityPainter = entityPainter;
    }
}

