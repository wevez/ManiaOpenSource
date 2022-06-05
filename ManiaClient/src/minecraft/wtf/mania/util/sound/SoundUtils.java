package wtf.mania.util.sound;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import wtf.mania.MCHook;
import wtf.mania.Mania;

public class SoundUtils implements MCHook {
	
	public static synchronized void playSound(String path, int time) {
		try {
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(mc.mcDataDir, "mania/audio/" + path));
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
	}

}
