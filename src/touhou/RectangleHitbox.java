package touhou;
import java.awt.geom.*;

/**
 * RectangleHitbox object
 * @author Max Jiang
 * @since 2021-07-27 
 */

public class RectangleHitbox extends Hitbox{
    
    // x, y is the center of the rectangle.
    // deg is in degrees
    public double w, h;

    /**
     * Initializer for rectangle hitbox
     */

    public RectangleHitbox(double xpos, double ypos, double width, double height){
        x = xpos;
        y = ypos;
        w = width;
        h = height;
    }

    /**
     * Initializer for rectangle hitbox
     */

    public RectangleHitbox(double xpos, double ypos, double width, double height, double angle){
        x = xpos;
        y = ypos;
        w = width;
        h = height;
        deg = angle;
    }

    public boolean intersects(RectangleHitbox other){
        // I did not use this so i did not code i.
        return false;
    }

    /**
     * Returns if hitbox intersects with another.
     * @see intersects in CircleHitbox
     */

    public boolean intersects(CircleHitbox other){
        return other.intersects(this);
    }

    /**
     * returns shape of hitbox
     * @return Rectangle2D.Double shape of hitbox
     */
    
    public Rectangle2D.Double getShape(){
        return new Rectangle2D.Double(x - w/2, y - h/2, w, h);
    }
}
