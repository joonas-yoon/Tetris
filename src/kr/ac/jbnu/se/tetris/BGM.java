package kr.ac.jbnu.se.tetris;

import javax.sound.sampled.Clip;

public class BGM extends SoundPlayer {

	private static BGM instance = new BGM();

	private static String[] musicList = { "bgm.wav", "bgm0.wav", "bgm1.wav", "bgm2.wav", "bgm3.wav" };

	public static String[] musicTitle = { "8 Bit Fun! by HeatleyBros", "TETRIS by Famicom", "Legend Of Zelda Theme", "Hearthstone", "Still More Fighting by Final Fantasy VII" };

	public String defaultDirectory = "sounds";

	private int currentId = 0;

	private BGM() {

	}

	public static BGM getInstance() {
		return instance;
	}

	public void play() {
		setVolume(Configurations.getInstance().getProperties().getVolumeMusic());
		setId(Configurations.getInstance().getProperties().bgmId);

		String filename = musicList[currentId];
		super.play(defaultDirectory + "/" + filename, Clip.LOOP_CONTINUOUSLY);
	}

	public void setId(int newId) {
		currentId = (newId + musicList.length) % musicList.length;
		Configurations.getInstance().getProperties().bgmId = currentId;
		Configurations.getInstance().save();
	}

	public void change(int newId) {
		setId(newId);

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
	
	public String getCurrentTitle(){
		return musicTitle[currentId];
	}
}
