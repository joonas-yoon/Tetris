package kr.ac.jbnu.se.tetris;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	private Clip clip;

	public boolean isPlaying = false;

	public SoundPlayer() {

	}

	public void play(String filename, int loopCount) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
			clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
			clip.loop(loopCount);

			isPlaying = true;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public void pause() {
		if (clip.isOpen())
			clip.stop();
		isPlaying = false;
	}

	public void resume() {
		if (clip.isOpen())
			clip.start();
		isPlaying = true;
	}

	public void stop() {
		try {
			clip.stop();
			clip.close();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} finally {
			isPlaying = false;
		}
	}
}
