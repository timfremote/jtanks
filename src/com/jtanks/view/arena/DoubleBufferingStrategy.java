package com.jtanks.view.arena;

import com.jtanks.view.gui.JTCanvas;

public class DoubleBufferingStrategy implements RenderingStrategy {
    private double scalingFactor;

    public DoubleBufferingStrategy(double modelSize, double viewSize) {
        this.scalingFactor = viewSize / modelSize;
    }

    public Renderer makeRenderer(JTCanvas canvas) { return new DoubleBuffer(canvas); }
    public Drawable makeScaledDrawable(Drawable drawable) { return new ScaledDrawable(drawable, scalingFactor); }
}
