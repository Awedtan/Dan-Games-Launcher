package touhou;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Spriteloader object
 * @author Max Jiang
 * @since 2021-01-27
 */

public class SpriteLoader{

    // hashmap to map sprite name to the sprite
    public HashMap<String, Sprite> s = new HashMap<String, Sprite>(); // sprites

    private BufferedImage sheet;
    private String smallColumn[] = {"black","dred","red","purple","pink","dblue","blue","daqua",
                            "aqua","dgreen","green","lime","dyellow","yellow","orange","white"};
    private String smallRow[] = {"00","01","02","03","04","05","06","07","08","09","10","11"};
    private double smallHbWidth[] = {0.5, 0.6, 0.7, 0.9, 0.4, 0.3, 0.3, 0.8, 0.5, 0.4, 0.5, 1};
    private double smallHbHeight[] = {0.75, 0.6, 0.7, 0.9, 0.7, 0.7, 0.6, 0.9, 0.7, 0.8, 0.5, 1};
    private boolean smallHbCircle[] = {false, true, true, true, false, false, false, false, false, false, true, true};

    /**
     * Constructor for spriteloader
     */

    public SpriteLoader(){
        // Load bullet sheet
        try {
			sheet = ImageIO.read(new File("spritesheets/bullet_sheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
        }
        for (int r = 0; r < smallRow.length; ++r){
            for (int c = 0; c < smallColumn.length; ++c){
                String key = smallRow[r] + smallColumn[c];
                s.put(key, new Sprite(sheet.getSubimage(5 + 16*c, 6 + 16*r, 16, 16), smallHbWidth[r], smallHbHeight[r], smallHbCircle[r]).fixRotation());
            }
        }

        // Load enemy sheet
        try {
			sheet = ImageIO.read(new File("spritesheets/enemy_sheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
        }

        s.put("player", new Sprite(sheet.getSubimage(199, 4, 30, 38), 0.2, 0.2, true, 0.8));

        // Stage 1 
        s.put("1blue", new Sprite(sheet.getSubimage(3, 2, 42, 30), 0.3, 0.3, true, 1.4));
        s.put("1gblue", new Sprite(sheet.getSubimage(50, 1, 44, 31), 0.3, 0.3, true, 1.4));
        s.put("1gred", new Sprite(sheet.getSubimage(100, 1, 42, 31), 0.3, 0.3, true, 1.4));
        s.put("1red", new Sprite(sheet.getSubimage(145, 2, 41, 29), 0.3, 0.3, true, 1.4));
        s.put("1boss", new Sprite(sheet.getSubimage(7, 107, 43, 40), 0.3, 0.3, true, 1.075));

        // Stage 2
        s.put("2blue", new Sprite(sheet.getSubimage(7, 35, 32, 29), 0.3, 0.3, true, 1.1));
        s.put("2red", new Sprite(sheet.getSubimage(55, 36, 32, 29), 0.3, 0.3, true, 1.1));
        s.put("2green", new Sprite(sheet.getSubimage(103, 36, 32, 29), 0.3, 0.3, true, 1.1));
        s.put("2yellow", new Sprite(sheet.getSubimage(150, 36, 32, 29), 0.3, 0.3, true, 1.1));
        s.put("2boss", new Sprite(sheet.getSubimage(61, 107, 43, 40), 0.3, 0.3, true, 1.075));

        // Stage 3
        s.put("3blue", new Sprite(sheet.getSubimage(6, 72, 32, 27), 0.3, 0.3, true, 1.185));
        s.put("3red", new Sprite(sheet.getSubimage(54, 73, 32, 27), 0.3, 0.3, true, 1.185));
        s.put("3yellow", new Sprite(sheet.getSubimage(102, 73, 32, 27), 0.3, 0.3, true, 1.185));
        s.put("3purple", new Sprite(sheet.getSubimage(150, 73, 32, 27), 0.3, 0.3, true, 1.185));
        s.put("3boss", new Sprite(sheet.getSubimage(117, 105, 63, 52), 0.3, 0.3, true, 1.21));
    }
}

