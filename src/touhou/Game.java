package touhou;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import javax.sound.sampled.Clip;

/**
 * Game class
 * @author Max Jiang
 * @since 2021-01-27
 */

@SuppressWarnings("serial")
public class Game extends JPanel implements KeyListener, Runnable{

    // Declare JFrame and scoreboard objects
    JFrame frame;
    int gameWidth = 500, gameHeight = 600;
    Scoreboard scoreboard;

    // Double buffering to reduce flickering
    Image offScreenImage;
    Graphics2D offScreenBuffer;
    
    // Player
    Player player;
    
    // Linked lists to store active bullets
    LinkedList<Projectile<RectangleHitbox>> playerBullets, nextPlayerBullets;
    LinkedList<Projectile<CircleHitbox>> circleEnemies, nextCircleEnemies;
    LinkedList<Projectile<RectangleHitbox>> rectangleEnemies, nextRectangleEnemies;
    
    // Entity and audio loaders
    EntityLoader entityloader;
    AudioLoader audioloader;

    // The result of the game
    volatile int gameResult = 0; // -2 for quit, -1 for lose, 1 for win, 0 for undetermined

    // Frames per second and next update time
    final double FPS = 120;
    double dtUpdate = 1000.0 / FPS;
    long startTime, timeElapsed, secondsElapsed;
    long frameCount = 0;

    // Player information
    int spellCards = 3;
    int lives = 3;

    // Main thread
    Thread mainThread;

    // Other game information
    Main main;
    String level;
    Clip bgm;
    int levelEnd;

    /**
     * Constructor for Game
     * @param _main main menu game is tied to
     * @param _level level that is being played
     */
    
    public Game(Main _main, String _level){

        // initialize parameters
        main = _main;
        level = _level;

        frame = new JFrame();
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        scoreboard = new Scoreboard(this);
        
        player = new Player(this);
        
        playerBullets = new LinkedList<>();
        nextPlayerBullets = new LinkedList<>();
        circleEnemies = new LinkedList<>();
        nextCircleEnemies = new LinkedList<>();
        rectangleEnemies = new LinkedList<>();
        nextRectangleEnemies = new LinkedList<>();

        try{
            entityloader = new EntityLoader(this, level);
        } catch (IOException e){
            e.printStackTrace();
        }
        audioloader = new AudioLoader();
        bgm = audioloader.play(level);

        // if user quits, set gameResult to quit (-2)
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                gameResult = -2;
            }
        });
        setFocusable(true);
        addKeyListener(this);

        // set end of level depending on which stage it is
        if (level.equalsIgnoreCase("stage1"))
            levelEnd = 9800;
        else if (level.equalsIgnoreCase("stage2"))
            levelEnd = 12000;
        else if (level.equalsIgnoreCase("stage3"))
            levelEnd = 11520;

        // JFrame methods
        frame.add(this);
        frame.add(scoreboard);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // start main thread
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
	public void run(){
        startTime = System.currentTimeMillis();
        secondsElapsed = 0;
		while (gameResult == 0){ // while result of game is not yet determined
            timeElapsed = System.currentTimeMillis() - startTime;
            if (timeElapsed > (frameCount + 1) * dtUpdate){ // it's time to update
                repaint();
            }
            if (timeElapsed > (secondsElapsed + 1) * 1000){ // one second has passed
                secondsElapsed++; 
            }
        }

        // stop background music
        bgm.stop();

        // report back to main depending on game result
        if (gameResult == 1){
            main.wonLevel();
        }
        else if (gameResult == -1){
            main.lostLevel();
        }
        else{
            main.quitLevel();
        }
    }

    /**
     * Updates the game by one frame
     */

    public void update(){

        // advance player and entity loader
        player.advance();
        entityloader.advance();

        // if the player is shooting
        if (player.isShooting){
            player.nextBullet--;
            if (player.nextBullet < 0){
                audioloader.play("plshoot"); // play audio
                for (int i = -1; i <= 1; ++i){
                    playerBullets.add(new Projectile<RectangleHitbox>(player.getCenterX(), player.getCenterY(), 500, 0, 270 + 2.5 * i, 0, 0, false, 1.5, true, 0,
                                      player.bulletSprite.getRectangleHitbox(player.bulletSize),  player.bulletSprite.img.getScaledInstance(player.bulletSize, player.bulletSize, 1), 0, 0, this));
                }   
                player.nextBullet = (int)(FPS / player.bulletsPerSecond);
            }
        }

        // Player bullets
        for (Projectile<RectangleHitbox> bullet : playerBullets){
            if (bullet.advance()){
                nextPlayerBullets.add(bullet);
            }
        }
        playerBullets.clear();
        for (Projectile<RectangleHitbox> bullet : nextPlayerBullets){
            playerBullets.add(bullet);
        }
        nextPlayerBullets.clear();

        // Circle Enemies
        for (Projectile<CircleHitbox> enemy : circleEnemies){
            if (player.invulnerableTime <= 0 && enemy.getHitbox().intersects(player.getHitbox())){
                loseLife();
            }
            if (!enemy.isBullet){ // if enemy is not a bullet, check if it is being hit by player bullets
                boolean hit = false; // each enemy can only be hit once each frame
                for (Projectile<RectangleHitbox> bullet : playerBullets){
                    hit |= bullet.getHitbox().intersects(enemy.getHitbox());
                    if (hit)
                        break;
                }
                if (hit){
                    enemy.damageTaken += enemy.originalLifetime * enemy.percentDamage;
                }
            }
            if (enemy.advance()){ // if enemy lifetime is not up
                nextCircleEnemies.add(enemy); // add to next frame
            } else if (enemy.id != 0) { // if lifetime is up
                // tell entity loader enemy with that id is removed
                entityloader.setUnactive(enemy.id);
            }
        }
        circleEnemies.clear();
        for (Projectile<CircleHitbox> enemy : nextCircleEnemies){ // swap nextCircleEnemies with circleEnemies
            circleEnemies.add(enemy);
        }
        nextCircleEnemies.clear();

        // Rectangle Enemies
        for (Projectile<RectangleHitbox> enemy : rectangleEnemies){
            if (player.invulnerableTime <= 0 && enemy.getHitbox().intersects(player.getHitbox())){
                loseLife();
            }
            if (enemy.advance()){ // if enemy lifetime is not up
                nextRectangleEnemies.add(enemy); // add to next frame
            } else if (enemy.id != 0) { // if lifetime is up
                // tell entity loader enemy with that id is removed
                entityloader.setUnactive(enemy.id);
            }
        }
        rectangleEnemies.clear();
        for (Projectile<RectangleHitbox> enemy : nextRectangleEnemies){ // swap nextRectangleEnemies with rectangleEnemies
            rectangleEnemies.add(enemy);
        }
        nextRectangleEnemies.clear();

    }

    /**
     * Called when player loses life
     */

    public void loseLife(){
        audioloader.play("pldead"); // play sound effect
        player.resetPosition(); // reset position
        player.invulnerableTime = 5.0 * FPS; // set grace period
        lives--; // decrement lives
        spellCards = 3; // reset spell cards
        if (lives == -1){ // if player has run out of lives
            gameResult = -1;
        }
    }

    /**
     * Draws the game
     * @param graphics Graphics object
     */

    public void paintComponent(Graphics graphics) {

        update(); // advance game by one frame first

        // Create buffer if it is null
        if (offScreenBuffer == null){
            offScreenImage = createImage(this.getWidth(), this.getHeight());
            offScreenBuffer = (Graphics2D) offScreenImage.getGraphics();
        }

        // cast graphics to Graphics2D
        Graphics2D g = (Graphics2D) graphics;
        offScreenBuffer.clearRect (0, 0, this.getWidth (), this.getHeight ());

        // draw gradient
        Paint oldpaint = offScreenBuffer.getPaint();
        offScreenBuffer.setPaint(new GradientPaint(0, 0, Color.BLACK, gameWidth, gameHeight, Color.GRAY));
        offScreenBuffer.fill(new Rectangle(0, 0, gameWidth, gameHeight));
        offScreenBuffer.setPaint(oldpaint);
        
        // draw player bullets
        for (Projectile<RectangleHitbox> bullet : playerBullets){
            offScreenBuffer.drawImage(bullet.sprite, bullet.getX(), bullet.getY(), this);
        }

        // draw player and player hitbox
        offScreenBuffer.drawImage(player.sprite, player.getX(), player.getY(), this);
        offScreenBuffer.setColor(Color.PINK);
        if (player.invulnerableTime <= 0)
            offScreenBuffer.fill(player.getHitbox().getShape());
        offScreenBuffer.setColor(Color.WHITE);

        // draw circle enemies
        for (Projectile<CircleHitbox> enemy : circleEnemies){
            offScreenBuffer.drawImage(enemy.sprite, enemy.getX(), enemy.getY(), this);
        }

        // draw rectangle enemies
        for (Projectile<RectangleHitbox> enemy : rectangleEnemies){
            offScreenBuffer.drawImage(enemy.sprite, enemy.getX(), enemy.getY(), this);
        }

        // if level has ended
        if (entityloader.frame >= levelEnd){
            offScreenBuffer.setFont(new Font("gulim", Font.PLAIN, 32));
            offScreenBuffer.drawString("Press Enter to progress!", 75, 300);
        }

        // move buffer to main screen
        g.drawImage(offScreenImage, 0, 0, this);
        scoreboard.repaint();
        frameCount++;
    }
    
    /**
     * Checks if various keys are pressed in the game
     * updates game accordingly
     * @param e KeyEvent object
     */
    
    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e); // tell player a key has been pressed
        if (e.getKeyCode() == KeyEvent.VK_X && player.invulnerableTime <= 0 && spellCards > 0){ // player cast spell card
            spellCards --;
            audioloader.play("spark");
            player.invulnerableTime = 5.0 * FPS;
        }
        if (e.getKeyCode() == KeyEvent.VK_C){ // toggle hitbox
            if (player.invulnerableTime <= 0)
                player.invulnerableTime = 10000000;
            else
                player.invulnerableTime = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && entityloader.frame >= levelEnd){ // advance to next level
            gameResult = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_A){ // add life
            lives++;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e); // tell player key has been released
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}