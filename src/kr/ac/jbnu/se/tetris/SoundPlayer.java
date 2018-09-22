package kr.ac.jbnu.se.tetris;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	private static SoundPlayer instance = new SoundPlayer();
	
	private SoundPlayer() {
		
	}
	
	public static SoundPlayer getInstance() {
		return instance;
	}
	
	public void play(String filename, int loopCount) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(loopCount);
		} catch (Exception ex) {
		}
	}
}
