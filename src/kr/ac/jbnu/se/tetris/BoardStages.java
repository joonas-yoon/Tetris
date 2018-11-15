package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BoardStages extends Board {
	static final int FINAL_STAGE = 25;
	int currentStageLevel;
	int currentStagePoint;

	public BoardStages(Tetris parent) {
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
		int[][] stageInfo = { { (int) (blockCount(BoardWidth, BoardHeight / 5) * 0.20), BoardHeight / 5 },
				{ (int) (blockCount(BoardWidth, BoardHeight / 5) * 0.30), BoardHeight / 5 },
				{ (int) (blockCount(BoardWidth, BoardHeight / 4) * 0.40), BoardHeight / 3 },
				{ (int) (blockCount(BoardWidth, BoardHeight / 3) * 0.50), BoardHeight / 3 },
				{ (int) (blockCount(BoardWidth, BoardHeight / 2) * 0.50), BoardHeight / 2 } };

		int stageId = (stage - 1) / stageInfo.length;

		generateRandomDummyBlock(stageInfo[stageId][0], stageInfo[stageId][1]);
	}

	protected void refreshText() {
		super.refreshText();

		if (isStarted) {
			statusbar.setText("[level: " + currentStageLevel + "] (" + (int) getCurrentStageClearPercent() + "%)");
		}
	}

	private void generateRandomDummyBlock(int maxBlocks, int maxHeight) {
		Random rand = new Random();
		int blocks = rand.nextInt(maxBlocks + 1);
		for (int i = 0; i < blocks; ++i) {
			int x = rand.nextInt(BoardWidth);
			int y = rand.nextInt(maxHeight);
			board[(y * BoardWidth) + x] = Tetrominoes.DeadShape;
		}
	}

	private int blockCount(int width, int height) {
		return width * height;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Paint
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void paint(Graphics g) {
		super.paint(g);

		if (!isStarted) {
			Dimension size = getSize();
			drawCenteredText(g, 0, -(int) size.getHeight() / 3, "Stage " + currentStageLevel, Color.WHITE);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Play
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected int removeFullLines() {
		int numFullLines = super.removeFullLines();

		if (numFullLines > 0) {

			currentStagePoint += numFullLines * comboCount;

			if (getCurrentStageClearPercent() >= 100.0) {
				gameClear();

				return numFullLines;
			}

		}

		return numFullLines;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Key Event
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void gameKeyPressed(KeyEvent e) {
		super.gameKeyPressed(e);

		int keycode = e.getKeyCode();

		switch (keycode) {
		case KeyEvent.VK_0:
			break;
		}
	}
}
