package com.jtanks.view;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class TopDownViewTest {
    private final Mockery context = new Mockery();
    private final GameWindow window1 = context.mock(GameWindow.class, "window1");
    private final GameWindow window2 = context.mock(GameWindow.class, "window2");
    private final GoListener listener = context.mock(GoListener.class);
    
    @Test public void constructorCallsUpdate() {
        context.checking(new Expectations() {{ 
            oneOf (window1).update();
            oneOf (window2).update();
        }});
        new TopDownView(window1, window2);
        context.assertIsSatisfied();
    }
    
    @Test public void addsListenerToEachWindow() {
        TopDownView view = makeView();

        context.checking(new Expectations() {{ 
            oneOf (window1).addGoListener(listener);
            oneOf (window2).addGoListener(listener);
        }});
        view.addGoListener(listener);
        context.assertIsSatisfied();
    }
    
    @Test public void showsEachWindow() {
        TopDownView view = makeView();

        context.checking(new Expectations() {{ 
            oneOf (window1).show();
            oneOf (window2).show();
        }});
        view.show();
        context.assertIsSatisfied();
    }
    
    @Test public void updatesEachWindow() {
        TopDownView view = makeView();

        context.checking(new Expectations() {{ 
            oneOf (window1).update();
            oneOf (window2).update();
        }});
        view.update();
        context.assertIsSatisfied();
    }

    private TopDownView makeView() {
        context.checking(new Expectations() {{ 
            oneOf (window1).update();
            oneOf (window2).update();
        }});
        TopDownView view = new TopDownView(window1, window2);
        return view;
    }
}
