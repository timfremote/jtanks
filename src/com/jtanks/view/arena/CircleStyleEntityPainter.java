package com.jtanks.view.arena;

import java.awt.Color;
import java.util.List;

import com.jtanks.model.BasicEntity;
import com.jtanks.model.MovingEntity;
import com.jtanks.model.MovingEntityWithTrail;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

public class CircleStyleEntityPainter implements EntityPainter {
    public void paintBaseShape(BasicEntity entity, Drawable drawable) {
        Point position = entity.getPosition();
        double size = entity.getSize();
        Color color = entity.getColor();
        
        drawable.setColor(color);
        if (entity.isDestroyed()) {
            drawable.drawCircle(position, size);
        } else{
            drawable.fillCircle(position, size);
        }
    }

    public void paintSmallHighlight(BasicEntity entity, Drawable drawable) {
        Point position = entity.getPosition();
        double size = entity.getSize();
        
        drawable.setColor(Color.DARK_GRAY);
        drawable.fillCircle(position, size/2);
    }

    public void paintTrail(MovingEntityWithTrail entity, Drawable drawable) {
        List<Point> trailPoints = entity.getTrail();
        Color color = entity.getColor().darker().darker();
        
        drawable.setColor(color);
        for (Point trailPoint : trailPoints) {
            drawable.drawPoint(trailPoint);
        }
    }

    public void paintRangeCircle(BasicEntity entity, Drawable drawable, int range) {
        if (entity.isDestroyed()) { return; }

        Point position = entity.getPosition();
        Color color = entity.getColor().darker().darker();
        
        drawable.setColor(color);
        drawable.drawCircle(position, range * 2);
    }

    public void paintCannon(MovingEntity entity, Drawable drawable, double cannonLength) {
        Point position = entity.getPosition();
        Color color = entity.getColor().brighter().brighter();
        double heading = entity.getHeading();

        Point cannonEnd = Euclid.getPointAtDistanceAndHeading(position, cannonLength, heading);
        drawable.setColor(color);
        drawable.drawLine(position, cannonEnd);
    }
}
