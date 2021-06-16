package touhou;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Sprite object
 * @author Max Jiang
 * @since 2021-01-27
 */

public class Sprite{
    public BufferedImage img;
    // for circles, hbLength = hbHeight = radius, represents ratios
    public double hbWidth, hbHeight;
    public double xScale;
    public boolean circle;

    /**
     * Constructor for Sprite
     */

    public Sprite(BufferedImage _img, double _hbWidth, double _hbHeight, boolean _circle){
        img = _img;
        hbWidth = _hbWidth;
        hbHeight = _hbHeight;
        circle = _circle;
        if (circle)
            hbHeight = hbWidth;
    }

    /**
     * Constructor for sprite with unique x scaling
     */

    public Sprite(BufferedImage _img, double _hbWidth, double _hbHeight, boolean _circle, double _xScale){
        img = _img;
        hbWidth = _hbWidth;
        hbHeight = _hbHeight;
        circle = _circle;
        if (circle)
            hbHeight = hbWidth;
        xScale = _xScale;
    }

    /**
     * Set rotation to x+ direction
     * this is because sprite sheet is not consistent with
     * direction conventions in program
     */

    public Sprite fixRotation(){
        double temp = hbWidth;
        hbWidth = hbHeight;
        hbHeight = temp;
        BufferedImage temp2 = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
        Graphics2D g = temp2.createGraphics();
        g.rotate(Math.PI/2, img.getWidth() / 2, img.getHeight() / 2);
        g.drawRenderedImage(img, null);
        img = temp2;
        return this;
    }

    /**
     * Returns circle hitbox of sprite
     * @param size specified size
     * @return RectangleHitbox hitbox of sprite scaled to size
     */

    public CircleHitbox getCircleHitbox(double size){
        if (!circle){
            System.out.println("ERROR! Trying to get circle hitbox of rectangle projectile.");
        }
        return new CircleHitbox(0, 0, hbWidth * size / 2);
    }

    /**
     * Returns rectangle hitbox of sprite
     * @param size specified size
     * @return RectangleHitbox hitbox of sprite scaled to size
     */

    public RectangleHitbox getRectangleHitbox(double size){
        if (circle){
            System.out.println("ERROR! Trying to get rectangle hitbox of circle projectile.");
        }
        return new RectangleHitbox(0, 0, hbWidth * size, hbHeight * size);
    }
}