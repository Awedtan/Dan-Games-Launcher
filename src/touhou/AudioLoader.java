package touhou;
import java.util.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Loads audio for the game
 * @author Max Jiang
 * @since 2020-01-27
 */

public class AudioLoader {

    AudioInputStream sound;
    HashMap<String, File> files = new HashMap<String, File>();

    /**
     * Constructor for audio loader
     */

    public AudioLoader(){
        try{ // put all files into the hashmap
            files.put("plshoot", new File("audio/plshoot.wav"));
            files.put("pldead", new File("audio/pldead.wav"));
            files.put("eshoot", new File("audio/eshoot.wav"));
            files.put("select", new File("audio/select.wav"));
            files.put("spark", new File("audio/spark.wav"));
            files.put("stage1", new File("audio/stage1.wav"));
            files.put("stage2", new File("audio/stage2.wav"));
            files.put("stage3", new File("audio/stage3.wav"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Plays and returns an audio clip
     * @param file which audio clip to play
     * @return Clip the request audio clip
     */

    public Clip play(String file){
        try{
            sound = AudioSystem.getAudioInputStream(files.get(file));
            Clip curr = AudioSystem.getClip();
            curr.open(sound);
            curr.start();
            return curr;
        }
        catch (Exception e){
            return null;
        }
    }
}
