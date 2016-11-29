package com.jtanks.view.arena;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Tank;
import com.jtanks.model.TankStockist;

public class TankPainter implements Painter {
    private final TankStockist tankStockist;
    private EntityPainter entityPainter;
    private final GameConfiguration config;

    public TankPainter(TankStockist tankStockist, GameConfiguration config) {
        this.tankStockist = tankStockist;
        this.config = config;
    }

    public void paint(Drawable drawable) {
      for (Tank tank : tankStockist.getTanks()) {
            paintTank(drawable, tank);
        }
    }
    
    private void paintTank(Drawable drawable, Tank tank) {
        if (null == entityPainter) { throw new IllegalStateException("No entity painter assigned"); }

        entityPainter.paintRangeCircle(tank, drawable, config.radarRange);
        entityPainter.paintTrail(tank, drawable);  
        entityPainter.paintCannon(tank, drawable, config.cannonLength);
        entityPainter.paintBaseShape(tank, drawable);
    }

    public void paintWith(EntityPainter entityPainter) {
        this.entityPainter = entityPainter;
    }
}
