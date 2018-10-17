package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class ConfigurationProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	int volumeMusic = SoundPlayer.MAX_VOLUME;
	int volumeEffect = SoundPlayer.MAX_VOLUME;

	int keyMoveLeft = KeyEvent.VK_LEFT;
	int keyMoveRight = KeyEvent.VK_RIGHT;

	public ConfigurationProperties() {

	}

	public String toString() {
		return "{" + volumeMusic + ", " + volumeEffect + "}";
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
