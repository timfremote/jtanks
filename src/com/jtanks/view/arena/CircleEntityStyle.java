package com.jtanks.view.arena;

public class CircleEntityStyle implements EntityStyle {
    public EntityPainter makeEntityPainter() { return new CircleStyleEntityPainter(); }
}
