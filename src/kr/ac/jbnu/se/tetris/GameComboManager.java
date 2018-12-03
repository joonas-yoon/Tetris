package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class GameComboManager {

	Color fontColor = Color.BLACK;
	int opacity;
	int count = 1;

	String text;
	Game game;

	public GameComboManager(Game target) {
		game = target;
		long observeDelay = 100;
		long observePeriod = 100;
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				if (opacity - 5 >= 0)
					opacity -= 5;
			}
		}, observeDelay, observePeriod);
	}

	public void showComboMessage(Graphics g) {
		if(opacity > 0){
			Color newColor = new Color(fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue(), opacity * 255 / 100);
			game.drawCenteredText(g, 0, opacity / 5, text, newColor);
			game.repaint();
		}
	}

	public void updateCombo(int removedLines) {
		if (removedLines > 0) {
			text = count + " Combo!";
			count += 1;
			opacity = 100;
		} else {
			text = "";
			count = 1;
		}
	}

	public long calculateScore(long score, int additional) {
		return score + additional * 100 * count;
	}
}
