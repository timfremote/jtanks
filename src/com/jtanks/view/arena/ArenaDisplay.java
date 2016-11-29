package com.jtanks.view.arena;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jtanks.config.GameConfiguration;
import com.jtanks.model.Quartermaster;
import com.jtanks.view.GameWindow;
import com.jtanks.view.GoListener;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.swing.SwingGUIStyle;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGraphics;
import com.jtanks.view.gui.JTCanvasListener;

// FIXME: Loads of peers here (have to mock 12 objects in test!) Must be a better way.
public class ArenaDisplay implements JTCanvasListener, GameWindow {
    private final JTCanvas canvas;
    private final List<Painter> painters = new ArrayList<Painter>();
    private final RenderingStrategy renderingStrategy;
    private final GUIStyle style;
    private final GameConfiguration config;
    private JTFrame frame;

	public ArenaDisplay(Quartermaster quartermaster, GameConfiguration config) {
        this(new SwingGUIStyle(), 
             new CircleEntityStyle(), 
             new DoubleBufferingStrategy(config.virtualArenaSize, (double)config.arenaDimension),
             config,
             new MinePainter(quartermaster),
             new MissilePainter(quartermaster),
             new TankPainter(quartermaster, config),
             new ExplosionPainter(quartermaster));
	}
	
	public ArenaDisplay(GUIStyle guiStyle, EntityStyle entityStyle, 
	                    RenderingStrategy renderingStrategy, GameConfiguration config,
	                    Painter ...painters) {
        this.style = guiStyle;
        this.config = config;
        this.canvas = guiStyle.makeCanvas();
        canvas.setBackground(Color.BLACK);
        canvas.setSize(config.arenaDimension, config.arenaDimension);

        this.renderingStrategy = renderingStrategy;
        
	    EntityPainter entityPainter = entityStyle.makeEntityPainter();
	    for (Painter painter : painters) {
	        this.painters.add(painter);
	        painter.paintWith(entityPainter);
	    }
	}

    public void update() { canvas.repaint(); }
    
	public void update(JTGraphics onscreen) {
	    Renderer renderer = renderingStrategy.makeRenderer(canvas);
	    Drawable drawable = renderingStrategy.makeScaledDrawable(renderer);
        for (Painter painter : painters ) {
            painter.paint(drawable);
        }
        renderer.renderIn(onscreen);
    }

    public void show() {
        if (null != frame) { throw new IllegalStateException("Cannot show ArenaDisplay twice!"); }
        frame = style.makeFrame();
        canvas.addListener(this);
        canvas.addTo(frame);
        frame.setTitle(config.arenaTitle);
        frame.setLocation(config.arenaXCoordinate, config.arenaYCoordinate);
        frame.pack();
        frame.setVisible(true);
    }

    public void addGoListener(GoListener listener) {
        // Nothing to do
    }
}