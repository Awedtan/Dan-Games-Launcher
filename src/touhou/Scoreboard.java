package touhou;
import java.awt.*;

import javax.swing.JPanel;

/**
 * Scoreboard object
 * @author Max Jiang
 * @since 2021-01-27
 */

@SuppressWarnings("serial")
public class Scoreboard extends JPanel{

    int scoreboardWidth = 300, scoreboardHeight = 600;

    Image offScreenImage;
    Graphics2D offScreenBuffer;

    Game game;

    /**
     * Constructor for Scoreboard
     * @param mainGame game object scoreboard is tied to
     */

    public Scoreboard(Game mainGame){

        setPreferredSize(new Dimension(scoreboardWidth, scoreboardHeight));

        game = mainGame;
    }

    /**
     * Draw the scoreboard
     * @param graphics Graphics object
     */

    public void paintComponent(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;
        
        // create buffer if null

        if (offScreenBuffer == null){
            offScreenImage = createImage(this.getWidth(), this.getHeight());
            offScreenBuffer = (Graphics2D) offScreenImage.getGraphics();
        }

        // draw gradient

        Paint oldpaint = offScreenBuffer.getPaint();
        offScreenBuffer.setPaint(new GradientPaint(scoreboardWidth, 0, Color.BLACK, 0, scoreboardHeight, new Color(90, 0, 0)));
        offScreenBuffer.fill(new Rectangle(0, 0, scoreboardWidth, scoreboardHeight));
        offScreenBuffer.setPaint(oldpaint);

        // set font
        
        offScreenBuffer.setFont(new Font("gulim", Font.PLAIN, 32));
        offScreenBuffer.setColor(Color.WHITE);

        // display text
        
        offScreenBuffer.drawString("Stage " + game.level.charAt(5), 10, 50);
        offScreenBuffer.drawString(String.format("Player lives: %d", game.lives), 10, 100);
        offScreenBuffer.drawString(String.format("Spell Cards: %d", game.spellCards), 10, 150);
        
        offScreenBuffer.setFont(new Font("gulim", Font.PLAIN, 20));
        offScreenBuffer.drawString(String.format("%.0f FPS", 1000.0 * game.frameCount / game.timeElapsed), 10, 580);

        g.drawImage(offScreenImage, 0, 0, this);
    }

}
