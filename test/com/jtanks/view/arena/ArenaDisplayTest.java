package com.jtanks.view.arena;

import java.awt.Color;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.jtanks.config.GameConfiguration;
import com.jtanks.view.gui.GUIStyle;
import com.jtanks.view.gui.JTCanvas;
import com.jtanks.view.gui.JTFrame;
import com.jtanks.view.gui.JTGraphics;

public class ArenaDisplayTest {
    private static final int ARENA_SIZE = 600;
    private static final String TITLE = "arena title";
    private static final int ARENA_X = 0;
    private static final int ARENA_Y = 0;
    
    private Mockery context = new Mockery();
    
    private GUIStyle guiStyle = context.mock(GUIStyle.class); // Policy
    private JTCanvas canvas = context.mock(JTCanvas.class); // Part

    private EntityStyle entityStyle = context.mock(EntityStyle.class); // Policy
    private EntityPainter entityPainter = context.mock(EntityPainter.class); // Part (local to each Painter)

    private RenderingStrategy renderingStrategy = context.mock(RenderingStrategy.class); // Policy
    private Renderer renderer = context.mock(Renderer.class); // Part (local to update())
    private Drawable drawable = context.mock(Drawable.class); // Part (local to update())
    
    private GameConfiguration config = new GameConfiguration();
    
    
    // Non-peers - used only in methods
    private JTFrame frame = context.mock(JTFrame.class); 
    private Painter painter1 = context.mock(Painter.class, "painter1");
    private Painter painter2 = context.mock(Painter.class, "painter2");
    private JTGraphics graphics = context.mock(JTGraphics.class);
    
    @Before public void setUp() {
        context.checking(new Expectations() {{
            allowing (guiStyle).makeCanvas(); will(returnValue(canvas));
            allowing (guiStyle).makeFrame(); will(returnValue(frame));
            allowing (entityStyle).makeEntityPainter(); will(returnValue(entityPainter));
            allowing (renderingStrategy).makeRenderer(canvas); will(returnValue(renderer));
            allowing (renderingStrategy).makeScaledDrawable(renderer); will(returnValue(drawable));
        }});
        config.arenaXCoordinate = ARENA_X;
        config.arenaYCoordinate = ARENA_Y;
        config.arenaDimension = ARENA_SIZE;
        config.arenaTitle = TITLE;
    }
    
    @Test public void setsUpCanvas() {
        context.checking(new Expectations() {{
            oneOf (canvas).setBackground(Color.BLACK);
            oneOf (canvas).setSize(ARENA_SIZE, ARENA_SIZE); 
        }});
        new ArenaDisplay(guiStyle, entityStyle, renderingStrategy, config);
        context.assertIsSatisfied();
    }  
    
    @Test public void passesRepaintToCanvas() {
        ArenaDisplay arenaDisplay = makeArenaDisplay();

        context.checking(new Expectations() {{
            oneOf (canvas).repaint();
        }});
        arenaDisplay.update();
        context.assertIsSatisfied();
    }
    
    @Test public void showAddsListenerAndMakesVisibleWithTitle() {
        final ArenaDisplay arenaDisplay = makeArenaDisplay();

        context.checking(new Expectations() {{ 
            oneOf (canvas).addListener(arenaDisplay);
            oneOf (canvas).addTo(frame);
            oneOf (frame).pack();
            oneOf (frame).setLocation(ARENA_X, ARENA_Y);
            oneOf (frame).setVisible(true);
            oneOf (frame).setTitle(TITLE);
        }});
        arenaDisplay.show();
        context.assertIsSatisfied();
    }

    @Test(expected = IllegalStateException.class)
    public void canOnlyDisplayOnce() {
        ArenaDisplay arenaDisplay = makeArenaDisplay();

        context.checking(new Expectations() {{ 
            ignoring (canvas);
            ignoring (frame);
        }});

        arenaDisplay.show();
        arenaDisplay.show();
    }
    
    @Test public void updatePaintsEachPainterThenRenders() {
        ArenaDisplay arenaDisplay = makeArenaDisplay(painter1, painter2);

        context.checking(new Expectations() {{ 
            ignoring (canvas);
            oneOf (painter1).paint(drawable);
            oneOf (painter2).paint(drawable);
            oneOf (renderer).renderIn(graphics);
        }});
        arenaDisplay.update(graphics);
        context.assertIsSatisfied();
    }
    
    private ArenaDisplay makeArenaDisplay(final Painter ...painters) {
        context.checking(new Expectations() {{
            allowing (canvas).setBackground(with(any(Color.class)));
            allowing (canvas).setSize(with(any(Integer.class)), with(any(Integer.class))); 
            for (Painter painter : painters) {
                oneOf (painter).paintWith(entityPainter);
            }
        }});
        return new ArenaDisplay(guiStyle, entityStyle, renderingStrategy, config, painters);
    }
}
