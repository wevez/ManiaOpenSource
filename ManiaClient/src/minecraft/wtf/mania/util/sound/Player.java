package wtf.mania.util.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import wtf.mania.Mania;
import wtf.mania.util.sound.player.MP3Player;


public class Player {

	public static MP3Player player;
	//public static Song currentSong;
	public static boolean isPlaying = false;
	//public static List<Song> currentSongList = new CopyOnWriteArrayList<Song>();
	public static float vol;
	public static boolean paused;
	public static boolean playerStopped = true;
	public static boolean playerPlaying;
	
	public static void resume() {
		if (player != null) {setVolume(Mania.instance.music.volume);
			player.play();
			isPlaying = true;
			paused = false;
		}
	}
	
	public static void play(String url) {
		stop();
					URL u;
					try {
						u = new URL(url);
						player = new MP3Player(u);
					} catch (MalformedURLException e1) {
					}
					
					player.setRepeat(false);
					setVolume(Mania.instance.music.volume);
					new Thread() {
						@Override
						public void run() {
							try {
					player.play();
					isPlaying = true;
					paused = false;
				} catch (Exception e) {
				}
			}
		}.start();
	}
	
	public static synchronized void playSound(String path) {
		  new Thread(new Runnable() {
			  public void run() {
				  try {
					  Clip clip = AudioSystem.getClip();
					  AudioInputStream inputStream = AudioSystem.getAudioInputStream(Mania.class.getResourceAsStream("/assets/minecraft/mania/audio/" + path));
					  clip.open(inputStream);
					  clip.start();
					  while (clip.getFramePosition() < clip.getFrameLength()) {
					  }
					  clip.stop();
				  } catch (Exception e) {
					  System.err.println(e.getMessage());
				  }
			  }
		  }).start();
	}
	
	public static synchronized void playSound(String path, int time) {
		  new Thread(new Runnable() {
			  public void run() {
				  try {
					  Clip clip = AudioSystem.getClip();
					  AudioInputStream inputStream = AudioSystem.getAudioInputStream(Mania.class.getResourceAsStream("/assets/minecraft/mania/audio/" + path));
					  clip.open(inputStream);
					  clip.start();
					  clip.setFramePosition(time);
					  while (clip.getFramePosition() < clip.getFrameLength()) {
						  Thread.sleep(100);
					  }
					  clip.stop();
					  stop();
				  } catch (Exception e) {
					  System.err.println(e.getMessage());
				  }
			  }
		  }).start();
	}
	
	public static void pause() {
		if (player != null) {
			player.pause();
			isPlaying = false;
			paused = true;
		}
	}
	
	public static boolean isPlaying(){
		return playerPlaying;
	}
	
	public static boolean isStopped(){
		return playerStopped;
	}
	
	public static void stop() {
		if (player != null) {
			player.stop();
			player = null;
			isPlaying = false;
			paused = false;
		}
	}
	
	public static void setVolume(float volume) {
		//vol = volume;
		if (player != null) {
			player.setVolume((int)volume);
		}
	}
}
