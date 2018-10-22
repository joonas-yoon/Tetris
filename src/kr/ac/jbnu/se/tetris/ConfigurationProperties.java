package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class ConfigurationProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	public transient static String FILENAME = "configs.dat";

	int volumeMusic = SoundPlayer.MAX_VOLUME;
	int volumeEffect = SoundPlayer.MAX_VOLUME;
	int bgmId = 0;

	KeyBind keyMoveLeft = new KeyBind(KeyEvent.VK_LEFT);
	KeyBind keyMoveRight = new KeyBind(KeyEvent.VK_RIGHT);
	KeyBind keyMoveDown = new KeyBind(KeyEvent.VK_D);
	KeyBind keyRotateLeft = new KeyBind(KeyEvent.VK_UP);
	KeyBind keyRotateRight = new KeyBind(KeyEvent.VK_DOWN);
	KeyBind keyPaused = new KeyBind(KeyEvent.VK_P);
	KeyBind keyDrop = new KeyBind(KeyEvent.VK_SPACE);
	KeyBind keyHold = new KeyBind(KeyEvent.VK_H);

	public transient static final int KEY_BINDING_COUNT = 8;

	public ConfigurationProperties() {

	}

	public String toString() {
		String ret = "{";
		ret += "volumeMusic: " + volumeMusic + ",";
		ret += "volumeEffect: " + volumeEffect + ",";
		ret += "keyMoveLeft: " + keyMoveLeft.getText() + ",";
		ret += "keyMoveRight: " + keyMoveRight.getText() + ",";
		ret += "keyMoveDown: " + keyMoveDown.getText() + ",";
		ret += "keyRotateLeft: " + keyRotateLeft.getText() + ",";
		ret += "keyRotateRight: " + keyRotateRight.getText() + ",";
		ret += "keyPaused: " + keyPaused.getText() + ",";
		ret += "keyDrop: " + keyDrop.getText() + ",";
		ret += "keyHold: " + keyHold.getText();
		ret += "}";
		return ret;
	}

	public void increaseVolumeMusic(int dv) {
		volumeMusic = Math.max(0, Math.min(SoundPlayer.MAX_VOLUME, volumeMusic + dv));
	}

	public int getVolumeMusic() {
		return volumeMusic;
	}

	public void increaseVolumeEffect(int dv) {
		volumeEffect = Math.max(0, Math.min(SoundPlayer.MAX_VOLUME, volumeEffect + dv));
	}

	public int getVolumeEffect() {
		return volumeEffect;
	}
}
