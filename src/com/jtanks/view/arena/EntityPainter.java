package com.jtanks.view.arena;

import com.jtanks.model.BasicEntity;
import com.jtanks.model.MovingEntity;
import com.jtanks.model.MovingEntityWithTrail;

public interface EntityPainter {
    void paintBaseShape(BasicEntity entity, Drawable drawable);
    void paintSmallHighlight(BasicEntity mine, Drawable drawable);
    void paintTrail(MovingEntityWithTrail entity, Drawable drawable);
    void paintRangeCircle(BasicEntity entity, Drawable drawable, int diameter);
    void paintCannon(MovingEntity entity, Drawable drawable, double tankCannonLength);
}
