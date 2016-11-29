package com.jtanks.view.arena;

import com.jtanks.view.gui.JTCanvas;

public interface RenderingStrategy {
    Renderer makeRenderer(JTCanvas canvas);
    Drawable makeScaledDrawable(Drawable drawable);
}
