package touhou;
import java.awt.geom.*;
import java.awt.*;

/**
 * CircleHitbox object
 * @author Max Jiang
 * @since 2021-01-27
 */

public class CircleHitbox extends Hitbox{
    
    public double r;
    private Point p[];

    /**
     * Constructor for CircleHitbox
     * @param xpos x position of circle center
     * @param ypos y position of circle center
     * @param radius radius of circle
     */

    public CircleHitbox(double xpos, double ypos, double radius){
        x = xpos;
        y = ypos;
        r = radius;
        p = new Point[4];
        for (int i = 0; i < 4; ++i)
            p[i] = new Point();
    }

    /**
     * checks if this hitbox intersects another hitbox
     * @param other other circle hitbox
     * @return true if the two hitboxes intersect, false otherwise
     */

    public boolean intersects(CircleHitbox other){
        // check if the two centers are less than the two radiuses added together
        return (r + other.r) * (r + other.r) >= (x - other.x) * (x - other.x) + (y - other.y) * (y-other.y);
    }

    /**
     * checks if this hitbox interescts another hitbox
     * @param other other rectangle hitbox
     * @return true if the two hitboxes intersect, false otherwise
     */

    public boolean intersects(RectangleHitbox other){
        // rotate poitns of rectangle accordingly 
        p[0].setPoint(other.x - other.w / 2, other.y - other.h / 2, other.x, other.y, other.deg);
        p[1].setPoint(other.x + other.w / 2, other.y - other.h / 2, other.x, other.y, other.deg);
        p[2].setPoint(other.x + other.w / 2, other.y + other.h / 2, other.x, other.y, other.deg);
        p[3].setPoint(other.x - other.w / 2, other.y + other.h / 2, other.x, other.y, other.deg);

        // check if circle lies within rectangle
        for (int i = 1; i <= 4; ++i){
            if (Line2D.ptSegDist(p[i % 4].x, p[i % 4].y, p[i - 1].x, p[i - 1].y, x, y) <= r){
                return true;
            }
        }
        // check if rectangle edges lie within circle
        for (int i = 1; i <= 4; ++i){
            if ((p[i - 1].x - p[i % 4].x) * (y - p[i % 4].y) - (x - p[i % 4].x) * (p[i - 1].y - p[i % 4].y) >= 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Debugger to see if hitboxes are workign correctly
     * @param other rectangle hitbox to debug
     * @param g graphics object to draw debugging circles on
     */

    public void debug(RectangleHitbox other, Graphics2D g){
        p[0].setPoint(other.x - other.w / 2, other.y - other.h / 2, other.x, other.y, other.deg);
        p[1].setPoint(other.x + other.w / 2, other.y - other.h / 2, other.x, other.y, other.deg);
        p[2].setPoint(other.x + other.w / 2, other.y + other.h / 2, other.x, other.y, other.deg);
        p[3].setPoint(other.x - other.w / 2, other.y + other.h / 2, other.x, other.y, other.deg);
        for (int i=0;i<=3;++i){
            g.setColor(Color.RED);
            g.drawOval((int)p[i].x-15,(int)p[i].y-15,30,30);
        }
    }

    /**
     * Returns Ellipse2D.Double version of shape
     * @return Ellipse2D.Double requested shape
     */

    public Ellipse2D.Double getShape(){
        return new Ellipse2D.Double(x - r, y - r, r + r, r + r);
    }

    /**
     * Point class to make computations and transformations easier
     */

    private class Point{

        double x, y;
        public Point(){
            x = y = 0;
        }

        /**
         * Uses a standard transformation matrix to rotate a point about another center point
         * by a specified number of degrees 
         * @param xpos x coordinate of point
         * @param ypos y coordinate of point
         * @param cx x coordinate of center of rotation
         * @param cy y coordinate of center of rotation
         * @param deg degrees to rotate by, in degrees
         */

        public void setPoint(double xpos, double ypos, double cx, double cy, double deg){
            xpos -= cx;
            ypos -= cy;
            double sin = Math.sin(Math.toRadians(deg));
            double cos = Math.cos(Math.toRadians(deg));
            x = cos * xpos - sin * ypos;
            y = sin * xpos + cos * ypos;
            x += cx;
            y += cy;
        }
    }
}
