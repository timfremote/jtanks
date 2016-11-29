package com.jtanks.view.arena;

import com.jtanks.model.Mine;
import com.jtanks.model.MineStockist;

public class MinePainter implements Painter {
    private final MineStockist mineStockist;
    private EntityPainter entityPainter;

    public MinePainter(MineStockist mineStockist) {
        this.mineStockist = mineStockist;
    }

    public void paint(Drawable drawable) {
        if (null == entityPainter) { throw new IllegalStateException("No entity painter assigned"); }
        for (Mine mine : mineStockist.getMines()) {
            entityPainter.paintBaseShape(mine, drawable);
            entityPainter.paintSmallHighlight(mine, drawable);
        }
    }

    public void paintWith(EntityPainter entityPainter) { this.entityPainter = entityPainter; }
}
