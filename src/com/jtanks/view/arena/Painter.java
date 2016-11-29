package com.jtanks.view.arena;

public interface Painter {
    void paint(Drawable drawable);
    void paintWith(EntityPainter entityPainter);
}
