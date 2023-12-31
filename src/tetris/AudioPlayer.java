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
import java.nio.file.*;
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
	
	
	static String AUDIO = "/Users/felix/Documents/SingertrisOfficial/sound/";
	static String IMGS = "/Users/felix/Documents/SingertrisOfficial/img/";
	
	static String SONGS = AUDIO + "gamesongs/";
	static String SFX = AUDIO + "gamesfx/";
	
	static String BGIMGS = IMGS + "bg/";
	static String BOTIMGS = IMGS + "enemies/";
	static String TEXTURES = IMGS;
	
	static WorldImage SINGERHAPPY = new FromFileImage(BOTIMGS + "singerhappy.png");
	
	static WorldImage BGDEFAULT = new RectangleImage(0, 0, OutlineMode.SOLID, Color.WHITE);
	static WorldImage BGSTARRY = new FromFileImage(BGIMGS + "bgdefault.jpeg");
	
	
	static WorldImage SPECIAL_PLACEHOLDER = new RotateImage(new RectangleImage(35, 35, OutlineMode.OUTLINE, Color.WHITE), 45);
	static WorldImage S_CYCLONE = new FromFileImage(IMGS + "special-CYCLONE.png");
	static WorldImage A_S_CYCLONE = new FromFileImage(IMGS + "active-special-CYCLONE.png");
	static WorldImage S_FRENZY = new FromFileImage(IMGS + "special-FRENZY.png");
	static WorldImage A_S_FRENZY = new FromFileImage(IMGS + "active-special-FRENZY.png");
	
	static WorldImage AB_SHIELD_I = new FromFileImage(IMGS + "ability-SHIELD_I.png");
	static WorldImage A_AB_SHIELD_I = new FromFileImage(IMGS + "active-ability-SHIELD_I.png");
	
	
	static String PLACEPIECE = SFX + "placepiece.wav";
	static String RECIEVESMALL = SFX + "recievesmall.wav";
	static String LIGHTNINGMINI = SFX + "thundermini.wav";
	//double of path, artist + name
	
	static String CURTAIN_CALL = SONGS + "CurtainCall.wav";
	static List<Double<String, String>> SONG = List.of(new Double<String, String>(CURTAIN_CALL, "Curtain Call - Cansol"),
			new Double<String, String>(SONGS + "TwilightPiano.wav", "Twilight Piano - M-Murray"));
			
	static Double<String, String> BGSONG = FilePaths.SONG.get(new Random().nextInt(FilePaths.SONG.size()));
}

