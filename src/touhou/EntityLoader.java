package touhou;
import java.util.*;
import java.io.*;
import java.awt.*;

/**
 * Loads the entities in game file
 * @author Max Jiang
 * @since 2021-01-27
 */

public class EntityLoader {

    private Game game;

    // objects used for input
    private BufferedReader in;
    private String line;
    private StringTokenizer st;

    public int frame = -5 * 120; // level starts at time = 0, to add space in beginning decrease initial frame number
    private int maxFrame = 180 * 120; // three minutes
    private double nextUpdate, dtUpdate;
    private LinkedList<Projectile<CircleHitbox>> cEnemies[];
    private LinkedList<Projectile<RectangleHitbox>> rEnemies[];
    private SpriteLoader sl = new SpriteLoader();
    private int isActive[];

    // temporary variables to store read in information
    private int time, times;
    private String command;
    private int timestep;
    private double x, y, vel, acc;
    private double cenx, ceny;
    private double deg, angVel, error;
    private boolean homing;
    private double initdeg, angstep, radius;
    private double lifetime, damagetaken;
    private String spriteid;
    private int spritesize, id, parentid;

    /*
    Fomatting:

    enemy/bullet time
    x y vel acc [vector]
    deg angVel error [homing]
    lifetime damagetaken
    spriteid spritesize
    id parentid

    rep time times timestep
    x y vel acc
    deg angVel error [homing]
    lifetime damagetaken
    spriteid spritesize
    id parentid
    
    circ time times timestep
    cenx ceny vel acc
    initdeg angstep radius [homing]
    lifetime damagetaken
    spriteid, spritesize
    id parentid
    */

    /**
     * Constructor for entity loader
     * @param _game Game object loader is tied to
     * @param levelFile level file to read from
     */

    @SuppressWarnings("unchecked")
    public EntityLoader(Game _game, String levelFile) throws FileNotFoundException, IOException{

        game = _game;
        dtUpdate = nextUpdate = _game.FPS / 120.0;

        // initialize linked lists and arrays

        cEnemies = new LinkedList[maxFrame + 1];
        rEnemies = new LinkedList[maxFrame + 1];
        for (int i = 0; i <= maxFrame; ++i){
            cEnemies[i] = new LinkedList<Projectile<CircleHitbox>>();
            rEnemies[i] = new LinkedList<Projectile<RectangleHitbox>>();
        }
        isActive = new int[maxFrame + 1];
        isActive[0] = 1;

        // set up reader
        in = new BufferedReader(new FileReader("stages/" + levelFile + ".txt"));

        while ((line = in.readLine()) != null){
            st = new StringTokenizer(line);
            command = st.nextToken();
            if (command.equalsIgnoreCase("enemy") || command.equalsIgnoreCase("bullet")){ // single bullet/enemy

                // read in information
                time = Integer.parseInt(st.nextToken());
                
                st = new StringTokenizer(in.readLine());
                x = Double.parseDouble(st.nextToken());
                y = Double.parseDouble(st.nextToken());
                vel = Double.parseDouble(st.nextToken());
                acc = Double.parseDouble(st.nextToken());
                boolean vector = st.hasMoreTokens();

                st = new StringTokenizer(in.readLine());
                deg = Double.parseDouble(st.nextToken());
                angVel = Double.parseDouble(st.nextToken());
                error = Double.parseDouble(st.nextToken());
                if (st.hasMoreTokens()){
                    homing = true;
                } else homing = false;


                st = new StringTokenizer(in.readLine());
                lifetime = Double.parseDouble(st.nextToken());
                damagetaken = Double.parseDouble(st.nextToken());

                if (vector){
                    deg = Math.toDegrees(Math.atan2(acc - y, vel - x));
                    vel = Math.sqrt((x - vel) * (x - vel) + (y - acc) * (y - acc)) / lifetime;
                    acc = 0;
                }

                st = new StringTokenizer(in.readLine());
                spriteid = st.nextToken();
                spritesize = Integer.parseInt(st.nextToken());

                st = new StringTokenizer(in.readLine());
                id = Integer.parseInt(st.nextToken());
                parentid = Integer.parseInt(st.nextToken());

                // create the projectile
                if (sl.s.get(spriteid).circle){
                    cEnemies[time].add(
                        new Projectile<CircleHitbox>(x, y, vel, acc, deg, angVel, error, homing,
                        lifetime, command.equalsIgnoreCase("bullet"), damagetaken / 100,
                        sl.s.get(spriteid).getCircleHitbox(spritesize),
                        sl.s.get(spriteid).img.getScaledInstance((int)(spritesize * (command.equalsIgnoreCase("bullet") ? 1:sl.s.get(spriteid).xScale)), spritesize, Image.SCALE_DEFAULT),
                        id, parentid, game)
                    );
                }
                else{
                    rEnemies[time].add(
                        new Projectile<RectangleHitbox>(x, y, vel, acc, deg, angVel, error, homing,
                        lifetime, command.equalsIgnoreCase("bullet"), damagetaken / 100,
                        sl.s.get(spriteid).getRectangleHitbox(spritesize),
                        sl.s.get(spriteid).img.getScaledInstance(spritesize, spritesize, Image.SCALE_DEFAULT),
                        id, parentid, game)
                    );
                }

            }
            else if (command.equalsIgnoreCase("rep")){ // repeat

                // read in information
                time = Integer.parseInt(st.nextToken());
                times = Integer.parseInt(st.nextToken());
                timestep = Integer.parseInt(st.nextToken());
                
                st = new StringTokenizer(in.readLine());
                x = Double.parseDouble(st.nextToken());
                y = Double.parseDouble(st.nextToken());
                vel = Double.parseDouble(st.nextToken());
                acc = Double.parseDouble(st.nextToken());

                st = new StringTokenizer(in.readLine());
                deg = Double.parseDouble(st.nextToken());
                angVel = Double.parseDouble(st.nextToken());
                error = Double.parseDouble(st.nextToken());
                if (st.hasMoreTokens()){
                    homing = true;
                } else homing = false;

                st = new StringTokenizer(in.readLine());
                lifetime = Double.parseDouble(st.nextToken());
                damagetaken = Double.parseDouble(st.nextToken());

                st = new StringTokenizer(in.readLine());
                spriteid = st.nextToken();
                spritesize = Integer.parseInt(st.nextToken());

                st = new StringTokenizer(in.readLine());
                id = Integer.parseInt(st.nextToken());
                parentid = Integer.parseInt(st.nextToken());

                // create projectiles 
                for (int i = 0; i < times; ++i){
                    if (sl.s.get(spriteid).circle){
                        cEnemies[time + i * timestep].add(
                            new Projectile<CircleHitbox>(x, y, vel, acc, deg, angVel, error, homing,
                            lifetime, true, damagetaken / 100,
                            sl.s.get(spriteid).getCircleHitbox(spritesize),
                            sl.s.get(spriteid).img.getScaledInstance(spritesize, spritesize, Image.SCALE_DEFAULT),
                            id, parentid, game)
                        );
                    }
                    else{
                        rEnemies[time + i * timestep].add(
                            new Projectile<RectangleHitbox>(x, y, vel, acc, deg, angVel, error, homing,
                            lifetime, true, damagetaken / 100,
                            sl.s.get(spriteid).getRectangleHitbox(spritesize),
                            sl.s.get(spriteid).img.getScaledInstance(spritesize, spritesize, Image.SCALE_DEFAULT),
                            id, parentid, game)
                        );
                    }
                }

            }
            else if (command.equalsIgnoreCase("circ")){ // circle

                // read in information
                time = Integer.parseInt(st.nextToken());
                times = Integer.parseInt(st.nextToken());
                timestep = Integer.parseInt(st.nextToken());
                
                st = new StringTokenizer(in.readLine());
                cenx = Double.parseDouble(st.nextToken());
                ceny = Double.parseDouble(st.nextToken());
                vel = Double.parseDouble(st.nextToken());
                acc = Double.parseDouble(st.nextToken());

                st = new StringTokenizer(in.readLine());
                initdeg = Double.parseDouble(st.nextToken());
                angstep = Double.parseDouble(st.nextToken());
                radius = Double.parseDouble(st.nextToken());
                if (st.hasMoreTokens()){
                    homing = true;
                } else homing = false;

                st = new StringTokenizer(in.readLine());
                lifetime = Double.parseDouble(st.nextToken());
                damagetaken = Double.parseDouble(st.nextToken());

                st = new StringTokenizer(in.readLine());
                spriteid = st.nextToken();
                spritesize = Integer.parseInt(st.nextToken());

                st = new StringTokenizer(in.readLine());
                id = Integer.parseInt(st.nextToken());
                parentid = Integer.parseInt(st.nextToken());

                // set initial rotation
                x = Math.cos(Math.toRadians(initdeg)) * radius + cenx;
                y = Math.sin(Math.toRadians(initdeg)) * radius + ceny;
                deg = initdeg;

                // create projectiles
                for (int i = 0; i < times; ++i){
                    if (sl.s.get(spriteid).circle){
                        cEnemies[time + i * timestep].add(
                            new Projectile<CircleHitbox>(x, y, vel, acc, deg, 0, 0, homing,
                            lifetime, true, damagetaken / 100,
                            sl.s.get(spriteid).getCircleHitbox(spritesize),
                            sl.s.get(spriteid).img.getScaledInstance(spritesize, spritesize, Image.SCALE_DEFAULT),
                            id, parentid, game)
                        );
                    }
                    else{
                        rEnemies[time + i * timestep].add(
                            new Projectile<RectangleHitbox>(x, y, vel, acc, deg, 0, 0, homing,
                            lifetime, true, damagetaken / 100,
                            sl.s.get(spriteid).getRectangleHitbox(spritesize),
                            sl.s.get(spriteid).img.getScaledInstance(spritesize, spritesize, Image.SCALE_DEFAULT),
                            id, parentid, game)
                        );
                    }
                    // rotate by angstep
                    x -= cenx;
                    y -= ceny;
                    double tempx = x;
                    double tempy = y;
                    double sin = Math.sin(Math.toRadians(angstep));
                    double cos = Math.cos(Math.toRadians(angstep));
                    x = cos * tempx - sin * tempy + cenx;
                    y = sin * tempx + cos * tempy + ceny;
                    deg += angstep;
                }

            }
            else if (command.equalsIgnoreCase("skip")){ // skip to a specific part of level
                time = Integer.parseInt(st.nextToken());
                frame = time - 2 * 120;
            }
            else if (command.equalsIgnoreCase("#")){ // comment

            }

            // read trailing empty line
            in.readLine();
        }
    }

    /**
     * Advances by one frame
     * Adds any planned projectiles onto game
     */

    public void advance(){
        // check if frame is supposed to be updated
        nextUpdate--;
        if (nextUpdate > 0)
            return;
        nextUpdate += dtUpdate;
        
        frame++;
        // if still in before stage period, return
        if (frame < 0)
            return;
        
        // if frame exceeds maximum number of frames
        if (frame > maxFrame){
            if (frame == maxFrame + 1)
            System.out.println("Level has finished.");
            return;
        }
        
        // add circle and rectangle enemies onto the main game
        for (Projectile<CircleHitbox> obj : cEnemies[frame]){
            if (isActive[obj.parent] == 0)
                continue;
            isActive[obj.id]++;
            game.circleEnemies.add(obj);
        }

        for (Projectile<RectangleHitbox> obj : rEnemies[frame]){
            if (isActive[obj.parent] == 0)
                continue;
            isActive[obj.id]++;
            game.rectangleEnemies.add(obj);
        }

    }

    public void setUnactive(int _id){
        isActive[_id]--;
    }
}

