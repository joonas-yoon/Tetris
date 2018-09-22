package kr.ac.jbnu.se.tetris;

import javax.sound.sampled.Clip;

public class BGM extends SoundPlayer {

	private static BGM instance = new BGM();

	public String[] musicList = { "bgm.wav", "bgm0.wav", "bgm1.wav", "bgm2.wav", "bgm3.wav" };

	private BGM() {

	}

	public static BGM getInstance() {
		return instance;
	}

	public String defaultDirectory = "sounds";

	public int currentId = 0;

	public boolean isPlaying = false;

	public void play() {
		String filename = musicList[currentId];
		super.play(defaultDirectory + "/" + filename, Clip.LOOP_CONTINUOUSLY);
		isPlaying = true;
	}

	public void stop() {
		super.stop();
		isPlaying = false;
	}

	public void change(int newId) {
		currentId = (newId + musicList.length) % musicList.length;
		if (isPlaying) {
			stop();
			play();
		}
	}

	public void changeNext() {
		change(currentId + 1);
	}

	public void changePrev() {
		change(currentId - 1);
	}
}
