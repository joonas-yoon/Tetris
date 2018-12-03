package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameStaged extends Game {
	static final int FINAL_STAGE = 25;
	int currentStageLevel;
	int currentStagePoint;

	public GameStaged(Tetris parent) {
		super(parent);
	}

	public int getCurrentStageGoal() {
		return currentStageLevel * 10;
	}

	public double getCurrentStageClearPercent() {
		return (double) currentStagePoint / getCurrentStageGoal() * 100;
	}

	private void readyNextStage() {
		boolean levelUp = false;
		if (isGameCleared) {
			if (currentStageLevel + 1 <= FINAL_STAGE) {
				currentStageLevel += 1;
				levelUp = true;
			} else {
				statusbar.setText("finish");
			}

			isGameCleared = false;
		}

		if (!levelUp) {
			currentStageLevel = 1;
		}
		currentStagePoint = 0;
	}

	protected void init() {
		isFallingFinished = false;
		isStarted = false;
		isPaused = false;
		isGameOvered = false;

		curX = 0;
		curY = 0;

		gameSpeedLevel = 1;

		curPiece.setShape(Tetrominoes.NoShape);
		holdPiece.setShape(Tetrominoes.NoShape);
		updateCurrentHoldPiece();
	}

	protected void ready() {
		super.ready();
		readyNextStage();
	}

	public void start() {
		super.start();

		makeStage(currentStageLevel);
	}

	public void makeStage(int stage) {
		blocks = StageManager.getInstance().getStageBlocks(stage).clone();
	}

	protected void refreshText() {
		super.refreshText();

		if (isStarted) {
			statusbar.setText("[level: " + currentStageLevel + "] (" + (int) getCurrentStageClearPercent() + "%)");
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (!isStarted) {
			Dimension size = getSize();
			drawCenteredText(g, 0, -(int) size.getHeight() / 3, "Stage " + currentStageLevel, Color.WHITE);
		}
	}

	protected int removeFullLines() {
		int numFullLines = super.removeFullLines();

		if (numFullLines > 0) {
			currentStagePoint += numFullLines * comboManager.count;

			if (getCurrentStageClearPercent() >= 100.0) {
				gameClear();

				return numFullLines;
			}
		}

		return numFullLines;
	}

	protected void gameKeyPressed(KeyEvent e) {
		super.gameKeyPressed(e);

		int keycode = e.getKeyCode();

		switch (keycode) {
		case KeyEvent.VK_0:
			break;
		}
	}
}
