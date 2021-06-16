package touhou;
import java.awt.*;
import java.awt.image.BufferedImage; 

/**
 * Projectile class
 * @author Max Jiang
 * @since 2021-01-27
 */

public class Projectile<HitboxType extends Hitbox> {

    // projectile properties
    private double x, y;
    private double vel, acc;
    private double deg;
    private double angVel;
    double error;
    private boolean homing;

    public double originalLifetime;
    public double lifetime;

    private HitboxType hitbox;

    // isbullet used to determine if I even need to check collision against player bullets
    public boolean isBullet;
    // on hit, enemy loses damageTaken % of hp. 0 means projectile is immune to getting hit
    public double percentDamage;
    public double damageTaken;

    private Game game;

    Image sprite;
    int spriteSize;

    // all ids should be greater than 1
    // 0 represents no parent, meaning all bullets with id 0 will be loaded.
    public int id, parent;

    /**
     * Constructor for Projectile
     */

    public Projectile(double _x, double _y, double _vel, double _acc, double _deg,  double _angVel,  double _error, boolean _homing,
                      double _lifetime, boolean _isBullet, double _percentDamage, HitboxType _hitbox, Image _sprite, int _id, int _parent, Game _game){
        
        game = _game;

        // Parameters given in seconds, must convert to updates (ups updates / second)
        x = _x;
        y = _y;
        vel = _vel / game.FPS;
        acc = _acc / game.FPS / game.FPS;
        deg = (Math.random() * 2 * _error - _error ) + _deg;
        angVel = _angVel / game.FPS;
        error = _error;
        homing = _homing;
        
        originalLifetime = lifetime = _lifetime * game.FPS;
        
        hitbox = _hitbox;
        hitbox.deg = _deg;

        isBullet = _isBullet;
        percentDamage = _percentDamage;

        sprite = _sprite;

        id = _id;
        parent = _parent;

        // set rotation if is bullet
        if (isBullet && !homing){
            BufferedImage temp2 = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), 6);
            Graphics2D g = temp2.createGraphics();
            g.rotate(Math.toRadians(deg), sprite.getWidth(null) / 2, sprite.getHeight(null) / 2);
            g.drawImage(sprite, 0, 0, null);
            sprite = temp2;
        }
    }

    /**
     * returns hitbox of projectile
     * @return HitboxType returns hitbox of projectile
     */

    public HitboxType getHitbox(){
        hitbox.x = x;
        hitbox.y = y;
        hitbox.deg = deg;
        return hitbox;
    }

    /**
     * advances projectile by one frame
     * @return true if advancement is successful, false if projectile has run out of lifetime
     */

    public boolean advance(){
        lifetime--;
        if (lifetime < 0 || damageTaken >= originalLifetime)
            return false;
        if (homing){
            // if homing, set rotation
            deg = Math.toDegrees(Math.atan2((game.player.getCenterY()- y), (game.player.getCenterX() - x)));
            deg = (Math.random() * 2 * error - error ) + deg;
            BufferedImage temp2 = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), 6);
            Graphics2D g = temp2.createGraphics();
            g.rotate(Math.toRadians(deg), sprite.getWidth(null) / 2, sprite.getHeight(null) / 2);
            g.drawImage(sprite, 0, 0, null);
            sprite = temp2;
            homing = false;
        }
        vel += acc;
        deg = (deg + angVel + 360) % 360;
        x += Math.cos(Math.toRadians(deg)) * vel;
        y += Math.sin(Math.toRadians(deg)) * vel;
        return true;
    }

    public int getX(){
        // top left corner
        return (int) Math.round(x - sprite.getWidth(null) / 2.0);
    }

    public int getY(){
        // top left corner
        return (int) Math.round(y - sprite.getHeight(null) / 2.0);
    }

}
