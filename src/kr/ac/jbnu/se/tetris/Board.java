package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.Timer;

public class Board extends TetrisGridPanel implements ActionListener {

	Tetris parent;

	Timer gameTimer;
	boolean isFallingFinished = false;
	boolean isStarted = false;
	boolean isPaused = false;
	int numLinesRemoved = 0;
	int curX = 0;
	int curY = 0;
	JLabel statusbar;
	Shape curPiece;
	Tetrominoes[] board;

	public BlockFactory nextBlocks = new BlockFactory(5);

	SoundPlayer sound = new SoundPlayer();
	BGM bgm = BGM.getInstance();

	Color comboFontColor = Color.BLACK;
	int comboOpacity = 100;
	int comboCount = 0;

	int gameSpeedLevel = 1;
	int[] gameSpeedDelay = { 400, 300, 200, 150, 100, 75, 50 };

	Shape holdPiece;

	long score = 0;
	JLabel scoreText;

	public Board(Tetris parent) {
		setFocusable(true);
		requestFocusInWindow();

		curPiece = new Shape();
		holdPiece = new Shape();
		gameTimer = new Timer(getGameSpeed(), this);
		gameTimer.start();

		statusbar = parent.getStatusBar();
		scoreText = parent.getScoreText();
		board = new Tetrominoes[BoardWidth * BoardHeight];
		addKeyListener(new TAdapter());
		clearBoard();

		long observeDelay = 100;
		long observePeriod = 100;
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				observe();
			}
		}, observeDelay, observePeriod);

		this.parent = parent;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// General
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void observe() {
		if (comboOpacity - 5 >= 0) {
			comboOpacity -= 5;
			repaint();
		}

		setGameSpeed((int) (score / 1000));
	}

	public void setGameSpeed(int level) {
		gameSpeedLevel = Math.max(1, Math.min(level, gameSpeedDelay.length));
		gameTimer.setDelay(getGameSpeed());
	}

	public int getGameSpeed() {
		return gameSpeedDelay[gameSpeedLevel - 1];
	}

	Tetrominoes shapeAt(int x, int y) {
		return board[(y * BoardWidth) + x];
	}

	private void init() {
		isFallingFinished = false;
		isStarted = false;
		isPaused = false;
		numLinesRemoved = 0;
		curX = 0;
		curY = 0;

		gameSpeedLevel = 1;
		score = 0;

		curPiece.setShape(Tetrominoes.NoShape);
		holdPiece.setShape(Tetrominoes.NoShape);
		updateCurrentHoldPiece();
	}

	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// UI/View
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void refreshText() {
		if (isPaused)
			statusbar.setText("paused");
		else if (isStarted)
			statusbar.setText(String.valueOf("(level: " + gameSpeedLevel + ") score: " + score));
		else
			statusbar.setText("game over");

		scoreText.setText("SCORE: " + score);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Combo System
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void showComboMessage(Graphics g, String text) {
		Color newColor = new Color(comboFontColor.getRed(), comboFontColor.getGreen(), comboFontColor.getBlue(),
				comboOpacity * 255 / 100);
		drawCenteredText(g, 0, comboOpacity / 5, text, newColor);
	}

	private void drawCenteredText(Graphics g, int offsetX, int offsetY, String text, Color fontColor) {
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g.create();

			int fontSize = (int) (Math.min(squareHeight(), squareWidth()) * 1.5);

			Font font = new Font("Comic Sans MS", Font.BOLD, fontSize);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int x = ((getWidth() - fm.stringWidth(text)) / 2);
			int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
			g2d.setColor(fontColor);
			g2d.drawString(text, x + offsetX, y + offsetY);
			g2d.dispose();
		}
	}

	private void processCombo(Graphics g) {
		// 0 for Test, 1 for production
		if (comboCount > 1) {
			showComboMessage(g, comboCount + " Combo!");
		} else {
			comboOpacity = 100;
			showComboMessage(g, "");
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Hold
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void updateCurrentHoldPiece() {
		if (parent.holdBlockPreview != null) {
			parent.holdBlockPreview.setBlock(holdPiece);
		}
	}

	private void holdCurrentPiece() {
		if (holdPiece.getShape() == Tetrominoes.NoShape) {
			holdPiece = curPiece;
			newPiece();
		} else {
			Shape tmpPiece = curPiece;
			curPiece = holdPiece;
			holdPiece = tmpPiece;
		}
		updateCurrentHoldPiece();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Game Life-cycle
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void start() {
		if (isPaused)
			return;

		init();

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();
		refreshText();

		newPiece();
		if (!bgm.isPlaying)
			bgm.play();
		gameTimer.start();
	}

	private void pause(boolean toggle) {
		if (!isStarted)
			return;

		if (toggle)
			isPaused = !isPaused;
		else
			isPaused = true;

		if (isPaused) {
			bgm.pause();
			gameTimer.stop();
		} else {
			bgm.resume();
			gameTimer.start();
		}

		refreshText();
		repaint();
	}

	private void gameover() {
		gameTimer.stop();
		isStarted = false;
		refreshText();
		bgm.stop();
		sound.play("sounds/gameover.wav", 0);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Paint
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
				int x = j * squareWidth();
				int y = boardTop + i * squareHeight();
				if (shape != Tetrominoes.NoShape)
					drawRect(g, x, y, shape, false);
				else
					drawRect(g, x, y, new Color(254, 254, 254, 50));
			}
		}

		if (isPaused) {
			Color oldColor = g.getColor();
			Color fgLayerColor = new Color(0, 0, 0, 255 / 2);
			g.setColor(fgLayerColor);
			g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());
			g.setColor(oldColor);
			drawCenteredText(g, 0, 0, "PAUSED", Color.WHITE);
		} else {
			if (curPiece.getShape() != Tetrominoes.NoShape) {
				drawShape(g, curX, curY, curPiece, false);
			}
		}

		preview(g);

		processCombo(g);
	}

	private void preview(Graphics g) {
		if (!isStarted || isPaused || isFallingFinished)
			return;

		int bottomPredict = getYPosPredict(curPiece, curX, curY);
		drawShape(g, curX, bottomPredict, curPiece, true);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Play
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void dropDown() {
		int newY = curY;
		while (newY > 0) {
			if (!tryMoveOrFail(curPiece, curX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	private void oneLineDown() {
		if (!tryMoveOrFail(curPiece, curX, curY - 1))
			pieceDropped();
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}

		removeFullLines();

		sound.play("sounds/beep0.wav", 1);

		if (!isFallingFinished)
			newPiece();
	}

	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	private void newPiece() {
		curPiece = nextBlocks.getNextBlock();
		nextBlocks.pop();

		parent.updateNextBlocks(nextBlocks);

		curX = BoardWidth / 2 + 1;
		curY = BoardHeight - 1 + curPiece.minY();

		if (!tryMoveOrFail(curPiece, curX, curY))
			gameover();
	}

	private boolean isMovableDownward(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)
				return false;
		}
		return true;
	}

	private boolean tryMoveOrFail(Shape newPiece, int newX, int newY) {
		if (isMovableDownward(newPiece, newX, newY)) {
			curPiece = newPiece;
			curX = newX;
			curY = newY;
			repaint();
			return true;
		}
		return false;
	}

	private int getYPosPredict(Shape newPiece, int startX, int startY) {
		Shape piece = newPiece;
		int newY = startY;
		while (isMovableDownward(piece, startX, newY - 1)) {
			newY -= 1;
		}
		return newY;
	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}

		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);

			comboCount += 1;
			comboOpacity = 100;

			score += numFullLines * 100 * comboCount;

			repaint();
			refreshText();
			sound.play("sounds/beep1.wav", 1);
		} else {
			comboCount = 0;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Key Event
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected void gameKeyPressed(KeyEvent e){
		int keycode = e.getKeyCode();

		if (isGameOvered) {
			if (keycode == KeyEvent.VK_ENTER) {
				start();
				return;
			}
		}

		if (keycode == KeyEvent.VK_P) {
			pause(true);
			return;
		}

		if (keycode == KeyEvent.VK_ESCAPE) {
			openSettingWindow();
			return;
		}

		if (isPaused)
			return;

		switch (keycode) {
		case KeyEvent.VK_LEFT:
			tryMoveOrFail(curPiece, curX - 1, curY);
			break;
		case KeyEvent.VK_RIGHT:
			tryMoveOrFail(curPiece, curX + 1, curY);
			break;
		case KeyEvent.VK_DOWN:
			tryMoveOrFail(curPiece.rotateRight(), curX, curY);
			break;
		case KeyEvent.VK_UP:
			tryMoveOrFail(curPiece.rotateLeft(), curX, curY);
			break;
		case KeyEvent.VK_SPACE:
			dropDown();
			break;
		case KeyEvent.VK_D:
			oneLineDown();
			break;
		case KeyEvent.VK_H:
			holdCurrentPiece();
			break;
		case KeyEvent.VK_1:
			bgm.changePrev();
			break;
		case KeyEvent.VK_2:
			bgm.changeNext();
			break;
		case KeyEvent.VK_3:
			gameClear();
			break;
		}

	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			gameKeyPressed(e);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Stage
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void generateRandomDummyBlock(int maxBlocks, int maxHeight) {
		Random rand = new Random();
		int blocks = rand.nextInt(maxBlocks + 1);
		for(int i=0; i<blocks; ++i) {
			int x = rand.nextInt(BoardWidth);
			int y = rand.nextInt(maxHeight);
			board[(y * BoardWidth) + x] = Tetrominoes.DeadShape;
		}
	}

	private void openSettingWindow() {
		pause(false);
		Configurations.getInstance().createFrame();
	}
}