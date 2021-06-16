package touhou;
import java.awt.*;
import java.awt.event.*;

/**
 * Player class
 * @author Max Jiang
 * @since 2021-01-27
 */

public class Player {

    private int gameWidth, gameHeight;

    // basic position and movement properties
    private double x, y;
    private double xVel, yVel; 
    private boolean left, right, up, down;
    private double speed;
    private final double SQRT2 = 1.4142135623;

    // determines if player is in precise dodging
    private boolean precise;
    private double preciseMultiplier;
    
    // hitbox of player
    private CircleHitbox hitbox;
    public double invulnerableTime;

    // sprite loader to load player's sprite
    private SpriteLoader spriteloader = new SpriteLoader();

    // player shooting bullets
    public int bulletsPerSecond = 15;
    public int nextBullet = 0;
    public boolean isShooting = false;
    public Sprite bulletSprite = spriteloader.s.get("05blue");
    public int bulletSize = 16;

    // player sprite
    int spriteSize = 40;
    Image sprite = spriteloader.s.get("player").img.getScaledInstance(spriteSize, spriteSize, 1);

    /**
     * constructor for player
     * @param game Game object player is tied to
     */

    public Player(Game game){

        // initialize variables
        gameWidth = game.gameWidth;
        gameHeight = game.gameHeight;

        x = gameWidth / 2.0;
        y = 5 * gameHeight / 7.0;

        speed = 250 / game.FPS;
        xVel = yVel = 0.0;
        left = right = up = down = false;
        
        precise = false;
        preciseMultiplier = 1.0 / 2;

        hitbox = spriteloader.s.get("player").getCircleHitbox(spriteSize);
        invulnerableTime = 5.0 * game.FPS;
    }

    /**
     * Advances the player by one frame
     */

    public void advance(){
        xVel = yVel = 0.0;
        invulnerableTime --;
        if (left)
            xVel -= speed;
        if (right)
            xVel += speed;
        if (up)
            yVel -= speed;
        if (down)
            yVel += speed;
        if (xVel != 0 && yVel != 0){
            xVel /= SQRT2;
            yVel /= SQRT2;
        }
        if (precise){
            xVel *= preciseMultiplier;
            yVel *= preciseMultiplier;
        }
        x += xVel;
        if (x < 0)
            x = 0;
        if (x > gameWidth)
            x = gameWidth;
        y += yVel;
        if (y < 0)
            y = 0;
        if (y > gameHeight)
            y = gameHeight;
    }

    /**
     * Processes key press
     * @param e KeyEvent object
     */

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            left = true;
        }
        if (key == KeyEvent.VK_RIGHT){
            right = true;
        }
        if (key == KeyEvent.VK_UP){
            up = true;
        }
        if (key == KeyEvent.VK_DOWN){
            down = true;
        }
        if (key == KeyEvent.VK_SHIFT){
            precise = true;
        }
        if (key == KeyEvent.VK_Z){
            isShooting = true;
        }
    }

    /**
     * Processes key release
     * @param e KeyEvent object
     */
    
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            left = false;
        }
        if (key == KeyEvent.VK_RIGHT){
            right = false;
        }
        if (key == KeyEvent.VK_UP){
            up = false;
        }
        if (key == KeyEvent.VK_DOWN){
            down = false;
        }
        if (key == KeyEvent.VK_SHIFT){
            precise = false;
        }
        if (key == KeyEvent.VK_Z){
            nextBullet = 0;
            isShooting = false;
        }
    }
    
    public void keyTyped(KeyEvent e) {}

    /**
     * Updates the location of the player's hitbox and returns it
     * @return CircleHitbox the hitbox of player
     */
    
    public CircleHitbox getHitbox(){
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }
    
    public int getX(){
        // top left corner
        return (int) Math.round(x - sprite.getWidth(null) / 2.0);
    }

    public int getY(){
        // top left corner
        return (int) Math.round(y - sprite.getHeight(null) / 2.0);
    }

    public int getCenterX(){
        return (int) x;
    }

    public int getCenterY(){
        return (int) y;
    }

    /**
     * Resets position of player to default position
     */
    
    public void resetPosition(){
        x = gameWidth / 2.0;
        y = 5 * gameHeight / 7.0;
    }
}
