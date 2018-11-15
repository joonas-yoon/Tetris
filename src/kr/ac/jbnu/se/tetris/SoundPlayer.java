package kr.ac.jbnu.se.tetris;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {
	private Clip clip;

	public boolean isPlaying;

	static int MAX_VOLUME = 10;
	int volume = MAX_VOLUME;
	int oldVolume;

	String oldFilename = "";
	int oldLoopCount;

	public SoundPlayer() {
	}

	public SoundPlayer(int soundVolume) {
		setVolume(soundVolume);
	}

	public void play(String filename, int loopCount) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
			clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.loop(loopCount);

			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float percent = (float) volume / MAX_VOLUME;
			float dB = (float) (Math.log(percent) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);

			clip.start();

			isPlaying = true;

			oldFilename = filename;
			oldLoopCount = loopCount;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void replay() {
		if (!isPlaying) {
			return;
		}

		if (oldFilename != "") {
			stop();
			play(oldFilename, oldLoopCount);
		}
	}

	public void setVolume(int newVolume) {
		newVolume = Math.max(0, Math.min(newVolume, 10));
		if (volume != newVolume) {
			volume = newVolume;
			replay();
		}
	}

	public void mute() {
		oldVolume = volume;
		setVolume(0);
	}

	public void unmute() {
		setVolume(oldVolume);
	}

	public void pause() {
		if (clip == null) {
			return;
		}

		try {
			if (clip.isOpen()) {
				clip.stop();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			isPlaying = false;
		}
	}

	public void resume() {
		if (clip == null) {
			return;
		}

		try {
			if (clip.isOpen()) {
				clip.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			isPlaying = true;
		}
	}

	public void stop() {
		if (clip == null) {
			return;
		}

		try {
			clip.stop();
			clip.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			isPlaying = false;
		}
	}
}
