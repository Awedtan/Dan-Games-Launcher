package helletbull;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;

// touhou 9.6 embroidery of google max
public class HMain {
	
	static boolean enableController = false; // Enable this for controller support
	static boolean debug = false; // Enable this to turn on a bunch of dev features
	
	static JFrame frame;
	static MenuPanel menuPanel = new MenuPanel();
	static GamePanel gamePanel;
	static ScorePanel scorePanel;
	
	public static void showGame() {
		// Opens new frame with game panel
		Game.fuckyou = false;
		frame.dispose();
		Player.score = 0;
		frame = new JFrame("Hellet Bull") {
			{
				addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent we) {
						try {
							helletbull.Game.end(false);
							Game.fuckyou = true;
						} catch (Exception ex) {
							System.out.println("p0p");
						}
					}
				});
			}
		};
		gamePanel = new GamePanel();
		
		frame.add(gamePanel);
		frame.pack();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void showMenu() {
		// Opens new frame with menu panel
		Game.fuckyou = false;
		frame.dispose();
		
		frame = new JFrame("Hellet Bull") {
			{
				addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent we) {
						try {
							helletbull.Game.end(false);
							Game.fuckyou = true;
						} catch (Exception ex) {
							System.out.println("p0p");
						}
					}
				});
			}
		};
		menuPanel = new MenuPanel();
		
		frame.add(menuPanel);
		frame.pack();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void showScores() {
		// Opens new frame with scores panel
		Game.fuckyou = false;
		frame.dispose();
		
		frame = new JFrame("Hellet Bull") {
			{
				addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent we) {
						try {
							helletbull.Game.end(false);
							Game.fuckyou = true;
						} catch (Exception ex) {
							System.out.println("p0p");
						}
					}
				});
			}
		};
		scorePanel = new ScorePanel();
		
		frame.add(scorePanel);
		frame.pack();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void run() {
		initializeImages();
		initializeBullets(); // Bullets are ellipses that move around and hit the player. Can spawn other bullets upon death
		initializeRoutines(); // Bullet routines tell individual enemies when to spawn bullets
		initializeEnemies(); // Enemies are ellipses that spawn bullets. Have built-in movement paths
		initializeScripts(); // Scripts control when and where enemies are spawned. Also specify the bullet routine for each enemy
		Game.fuckyou = false;
		frame = new JFrame("Hellet Bull") {
			{
				addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent we) {
						try {
							helletbull.Game.end(false);
							Game.fuckyou = true;
						} catch (Exception ex) {
							System.out.println("p0p");
						}
					}
				});
			}
		};
		
		// if (enableController)
		// Controller.initialize();
		menuPanel = new MenuPanel();
		frame.add(menuPanel);
		frame.pack();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void initializeBullets() {
		// Reads in data from bullet files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/bullets");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
				String str = input.readLine();
				
				while (str.indexOf('?') == -1) {
					String[] accumulate = new String[18];
					int count = 0;
					
					if (str.indexOf("bullet") == 0)
						while (str.indexOf(';') == -1) {
							accumulate[count] = str;
							str = input.readLine();
							count++;
						}
					
					str = input.readLine();
					if (accumulate[0] != null)
						Parser.parseBullet(accumulate);
				}
				
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void initializeEnemies() {
		// Reads in data from enemy files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/enemies");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
				String str = input.readLine();
				
				while (str.indexOf('?') == -1) {
					String[] accumulate = new String[9];
					int count = 0;
					
					if (str.indexOf("enemy") == 0)
						while (str.indexOf(';') == -1) {
							accumulate[count] = str;
							str = input.readLine();
							count++;
						}
					
					str = input.readLine();
					
					if (accumulate[0] != null)
						Parser.parseEnemy(accumulate);
				}
				
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void initializeImages() {
		// Collects and loads images
		// Puts them in a map
		
		MediaTracker tracker = new MediaTracker(menuPanel);
		File folder = new File("images");
		
		for (File f : folder.listFiles()) {
			
			Image img = Toolkit.getDefaultToolkit().getImage(f.toString());
			// System.out.println(img.getHeight(null));
			tracker.addImage(img, 0);
			Game.imageMap.put(f.getName(), img);
			// System.out.println(f.toString());
		}
		
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
		}
		
		Player.sprite = Game.getImage("player");
	}
	
	public static void initializeRoutines() {
		// Reads in data from routine files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/routines");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
				String str = input.readLine();
				
				while (str.indexOf('?') == -1) {
					ArrayList<String> accumulate = new ArrayList<String>();
					
					if (str.indexOf("routine") == 0)
						while (str.indexOf(';') == -1) {
							if (str.length() > 0)
								accumulate.add(str);
							str = input.readLine();
						}
					
					str = input.readLine();
					
					if (accumulate.size() != 0)
						Parser.parseRoutine(accumulate);
				}
				
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void initializeScripts() {
		// Reads in data from script files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/scripts");
			
			for (File f : folder.listFiles()) {
				
				Scanner input = new Scanner(f);
				String str = input.nextLine().trim();
				
				while (str.indexOf('!') == -1)
					if (input.hasNext())
						str = input.nextLine().trim();
					else {
						input.close();
						return;
					}
				
				while (str.indexOf('?') == -1) {
					ArrayList<String> accumulate = new ArrayList<String>();
					
					if (str.indexOf("script") == 0)
						while (str.indexOf(';') == -1) {
							if (str.length() > 1)
								accumulate.add(str);
							str = input.nextLine();
						}
					else if (Game.scriptMap.containsKey(str))
						Game.scriptQueue.addLast(Game.scriptMap.get(str).clone());
					
					str = input.nextLine();
					
					if (accumulate.size() != 0)
						Parser.parseScript(accumulate);
				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
}