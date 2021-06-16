package touhou;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Main class
 * Contains main menu
 * Run this for game
 * @author Max Jiang
 * @since 2021-01-27
 */

@SuppressWarnings("serial")
public class Main extends JPanel implements KeyListener{

    // Initial starting level
    public int startingLevel = 1;

    // Various variables
    public int menuWidth = 400, menuHeight = 300;
    Game game;
    int level;
    int option;
    boolean inGame;
    JFrame frame;

    /**
     * Constructor for Main
     */

    public Main(){

        // Set up JFrame and JPanel
        frame = new JFrame();
        setPreferredSize(new Dimension(400, 300));

        frame.add(this);
        frame.setFocusable(true);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.pack();
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Initialize global variables
        option = 0;
        inGame = false;
    }

    /**
     * Called when user beats the level
     * If previous level was the last level, display winner dialog
     * Otherwise, moves onto the next level
     */

    public void wonLevel(){
        game.frame.setVisible(false);
        game.frame.dispose();
        game.bgm.stop();
        if (level == 3){
            inGame = false;
            JOptionPane.showMessageDialog(frame, "You win! :)", "Thanks for playing!", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            level++;
            game = new Game(this, "stage" + level);
        }
    }

    /**
     * Called when user loses the level
     * Displays loser dialog
     */

    public void lostLevel(){
        game.frame.setVisible(false);
        game.frame.dispose();
        game.bgm.stop();
        inGame = false;
        JOptionPane.showMessageDialog(frame, "You lost on stage " + level + "! :(", "Maybe next time...", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Called when user quits a given level (by closing the JFrame)
     * No message dialog is displayed
     */

    public void quitLevel(){
        game.frame.setVisible(false);
        game.frame.dispose();
        game.bgm.stop();
        inGame = false;
    }

    /**
     * Draws the main menu
     * @param graphics Graphics object
     */

    public void paintComponent(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;

        // Set gradient of background
        
        Paint oldpaint = g.getPaint();
        g.setPaint(new GradientPaint(menuWidth / 2, 0, Color.BLACK, menuWidth / 2, menuHeight, Color.GRAY));
        g.fill(new Rectangle(0, 0, menuWidth, menuHeight));
        g.setPaint(oldpaint);

        // Set font and color and draw title

        g.setFont(new Font("gulim", Font.PLAIN, 64));
        g.setColor(Color.RED);
        g.drawString("Touhou", 90, 90);

        // Set font and color
        
        g.setFont(new Font("gulim", Font.PLAIN, 24));
        g.setColor(Color.WHITE);

        g.drawLine(150, 250, 200, 125);

        // Write options in white and selected option in red

        if (option == 0)
            g.setColor(Color.RED);
        g.drawString("Play", 225, 150);
        g.setColor(Color.WHITE);

        if (option == 1)
            g.setColor(Color.RED);
        g.drawString("About", 200, 200);
        g.setColor(Color.WHITE);

        if (option == 2)
            g.setColor(Color.RED);
        g.drawString("Quit", 175, 250);
        g.setColor(Color.WHITE);
    }

    /**
     * Checks if various keys are pressed in the menu
     * and processes key presses accordingly
     * @param e KeyEvent object
     */
    
    @Override
    public void keyPressed(KeyEvent e) { 
        if (inGame) return;
        if (e.getKeyCode() == KeyEvent.VK_DOWN){ // Scroll down if down arrow is pressed
            option++;
            option %= 3;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){ // Scroll up if up arrow is pressed
            if (option > 0)
                option--;
            else
                option = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_ENTER){ // If current option is selected
            if (option == 0){ // Play
                // Create new game
                level = startingLevel;
                inGame = true;
                option = -1;
                game = new Game(this, "stage" + level);
            }
            else if (option == 1){ // About
                // Show about information
                JOptionPane.showMessageDialog(frame,
                "Dodge the bullets and shoot down the enemies!\n\n" +
                "Controls:\n" +
                "Move - arrow keys\n" +
                "Select/Shoot - z\n" +
                "Precise dodging - shift\n" +
                "Spell card - x\n\n" +
                "Cheats:\n" +
                "Toggle hitbox - c\n" +
                "Add life - a\n\n" +
                "Made by Max Jiang for ICS3U1.\nThank you for playing!"
                , "About", JOptionPane.PLAIN_MESSAGE);
            }
            else if (option == 2){ // Quit
                // Close the frame
                frame.setVisible(false);
                frame.dispose();
            }
        }
        // Repaint updates
        repaint();
    }

    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }

    public static void run(){
        new Main(); // Create new Main object
    }


}
