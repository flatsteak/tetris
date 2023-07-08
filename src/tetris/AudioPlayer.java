package tetris;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javalib.worldimages.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AudioPlayer {
    private Clip clip;

    public void play(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void playLoop(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void playAccurateLoop(String filePath, GameState g) {
    	try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            g.stats.starttime = System.currentTimeMillis();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

class FilePaths {
	static String AUDIO = "/Users/felix/Downloads/Singertris/";
	static String IMGS = "/Users/felix/Downloads/Singertris/";
	
	static WorldImage SINGERHAPPY = new FromFileImage(IMGS + "singerhappy.png");
	
	static WorldImage BGDEFAULT = new RectangleImage(0, 0, OutlineMode.SOLID, Color.WHITE);
	static WorldImage BGSTARRY = new FromFileImage(IMGS + "bgdefault.jpeg");
	
	//double of path, artist + name
	static List<Double<String, String>> SONG = List.of(new Double<String, String>(AUDIO + "CurtainCall.wav", "Curtain Call - Cansol"),
			new Double<String, String>(AUDIO + "TwilightPiano.wav", "Twilight Piano - M-Murray"));
			
	static Double<String, String> BGSONG = FilePaths.SONG.get(new Random().nextInt(FilePaths.SONG.size()));
}

