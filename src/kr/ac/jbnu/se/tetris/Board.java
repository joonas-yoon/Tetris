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
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Board extends TetrisGridPanel implements ActionListener {
	Tetris parent;

	Timer gameTimer;

	boolean isFallingFinished;
	boolean isStarted;
	boolean isPaused;
	boolean isGameOvered;
	boolean isGameCleared;

	int numLinesRemoved;
	int curX;
	int curY;
	JLabel statusbar;
	Shape curPiece;
	Tetrominoes[] board;

	public BlockFactory nextBlocks = new BlockFactory(5);

	SoundPlayer sound = new SoundPlayer();
	BGM bgm = BGM.getInstance();

	Color comboFontColor = Color.BLACK;
	int comboOpacity = 100;
	int comboCount;

	int gameSpeedLevel = 1;
	int[] gameSpeedDelay = { 400, 300, 200, 150, 100, 75, 50 };

	Shape holdPiece;

	long score;
	JLabel scoreText;

	public Board(Tetris parent) {
		setFocusable(true);
		requestFocusInWindow();

		curPiece = new Shape();
		holdPiece = new Shape();
		gameTimer = new Timer(getGameSpeed(), this);

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

		// scheduled long term
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				updateConfigurations();
			}
		}, 5000, 5000);

		this.parent = parent;

		ready();
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
	}

	protected void updateConfigurations() {
		bgm.setVolume(Configurations.getInstance().getProperties().getVolumeMusic());
		sound.setVolume(Configurations.getInstance().getProperties().getVolumeEffect());
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

	protected void init() {
		isFallingFinished = false;
		isStarted = false;
		isPaused = false;
		isGameOvered = false;
		isGameCleared = false;

		numLinesRemoved = 0;
		curX = 0;
		curY = 0;

		gameSpeedLevel = 1;
		score = 0;

		curPiece.setShape(Tetrominoes.NoShape);
		holdPiece.setShape(Tetrominoes.NoShape);
		updateCurrentHoldPiece();
	}

	protected void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
			board[i] = Tetrominoes.NoShape;
		}
	}

	protected void quit() {
		gameTimer.stop();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// UI/View
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void refreshText() {
		if (isPaused) {
			statusbar.setText("paused");
		} else if (isStarted) {
			statusbar.setText("(level: " + gameSpeedLevel + ") score: " + score);
		}
		scoreText.setText("SCORE: " + score);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Combo System
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void showComboMessage(Graphics g, String text) {
		Color newColor = new Color(comboFontColor.getRed(), comboFontColor.getGreen(), comboFontColor.getBlue(),
				comboOpacity * 255 / 100);
		drawCenteredText(g, 0, comboOpacity / 5, text, newColor);
	}

	protected void processCombo(Graphics g) {
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

	protected void updateCurrentHoldPiece() {
		if (parent.holdBlockPreview != null) {
			parent.holdBlockPreview.setBlock(holdPiece);
		}
	}

	protected void holdCurrentPiece() {
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

	protected void ready() {
		init();
		clearBoard();
		refreshText();

		isStarted = false;

		numLinesRemoved = 0;

		gameTimer.stop();
	}

	public void start() {
		if (isPaused) {
			return;
		}

		isStarted = true;
		isFallingFinished = false;

		newPiece();
		if (!bgm.isPlaying) {
			bgm.play();
		}
		gameTimer.start();
	}

	private void pause(boolean toggle) {
		if (!isStarted) {
			return;
		}

		if (toggle) {
			isPaused = !isPaused;
		} else {
			isPaused = true;
		}

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

	protected void gameover() {
		gameTimer.stop();
		isStarted = false;
		isPaused = isGameOvered = true;
		refreshText();
		bgm.stop();
		sound.play("sounds/gameover.wav", 0);

		registRank();
	}

	protected void gameClear() {
		gameTimer.stop();
		isPaused = isGameCleared = true;
		refreshText();
		bgm.stop();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Ranking
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void registRank() {
		Ranking.getInstance().requestSubmitScore(score);
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
				if (shape != Tetrominoes.NoShape) {
					drawRect(g, x, y, shape, false);
				} else {
					drawRect(g, x, y, new Color(254, 254, 254, 50));
				}
			}
		}

		if (isPaused || !isStarted) {
			drawTitleText(g);
		} else if (curPiece.getShape() != Tetrominoes.NoShape) {
			drawShape(g, curX, curY, curPiece, false);
		}

		preview(g);

		processCombo(g);
	}

	protected void drawTitleText(Graphics g) {
		Color oldColor = g.getColor();

		ArrayList<String> texts = new ArrayList<>();

		Color fgLayerColor;
		if (isGameOvered) {
			fgLayerColor = new Color(0, 0, 0, 255 * 2 / 3);
		} else if (isGameCleared) {
			fgLayerColor = new Color(30, 170, 80, 255 / 2);
		} else if (!isStarted) {
			fgLayerColor = new Color(255, 200, 10, 255 / 2);
		} else {
			fgLayerColor = new Color(0, 0, 0, 255 / 2);
		}

		Dimension size = getSize();
		g.setColor(fgLayerColor);
		g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());
		g.setColor(oldColor);

		if (isGameOvered) {
			texts.add("GAME OVER");
			texts.add("(press ENTER)");
		} else if (isGameCleared) {
			texts.add("CLEAR!");
			texts.add("(press ENTER)");
		} else if (!isStarted) {
			texts.add("READY");
			texts.add("(press ENTER)");
		} else {
			texts.add("PAUSED");
		}

		int offsetY = 0;
		for (String text : texts) {
			offsetY += drawCenteredText(g, 0, offsetY, text, Color.WHITE) * 1.25;
		}
	}

	protected int drawCenteredText(Graphics g, int offsetX, int offsetY, String text, Color fontColor) {
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g.create();

			int fontSize = (int) (Math.min(squareHeight(), squareWidth()) * 1.5);

			Font font = new Font("Comic Sans MS", Font.BOLD, fontSize);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int x = (getWidth() - fm.stringWidth(text)) / 2;
			int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
			g2d.setColor(fontColor);
			g2d.drawString(text, x + offsetX, y + offsetY);
			g2d.dispose();

			return fontSize;
		}
		return 0;
	}

	private void preview(Graphics g) {
		if (!isStarted || isPaused || isFallingFinished) {
			return;
		}

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
			if (!tryMoveOrFail(curPiece, curX, newY - 1)) {
				break;
			}
			--newY;
		}
		pieceDropped();
	}

	private void oneLineDown() {
		if (!tryMoveOrFail(curPiece, curX, curY - 1)) {
			pieceDropped();
		}
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}

		removeFullLines();

		sound.play("sounds/beep0.wav", 1);

		if (!isFallingFinished) {
			newPiece();
		}
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

		if (!tryMoveOrFail(curPiece, curX, curY)) {
			gameover();
		}
	}

	private boolean isMovableDownward(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight || shapeAt(x, y) != Tetrominoes.NoShape) {
				return false;
			}
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

	protected int removeFullLines() {
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
					for (int j = 0; j < BoardWidth; ++j) {
						board[k * BoardWidth + j] = shapeAt(j, k + 1);
					}
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

			setGameSpeed((int) (score / 10000));

			repaint();
			refreshText();
			sound.play("sounds/beep1.wav", 1);
		} else {
			comboCount = 0;
		}

		return numFullLines;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Key Event
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void gameKeyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();

		ConfigurationProperties configKey = Configurations.getInstance().getProperties();

		if (keycode == configKey.keyPaused.getCode()) {
			pause(true);
			return;
		}

		if (keycode == KeyEvent.VK_ESCAPE) {
			openSettingWindow();
			return;
		}

		if (isGameCleared || isGameOvered) {
			if (keycode == KeyEvent.VK_ENTER) {
				ready();
			}
			return;
		}

		if (!isStarted) {
			if (keycode == KeyEvent.VK_ENTER) {
				start();
			}
			return;
		}

		if (isPaused) {
			return;
		}

		if (keycode == configKey.keyMoveLeft.getCode()) {
			tryMoveOrFail(curPiece, curX - 1, curY);
		}

		if (keycode == configKey.keyMoveRight.getCode()) {
			tryMoveOrFail(curPiece, curX + 1, curY);
		}

		if (keycode == configKey.keyRotateRight.getCode()) {
			tryMoveOrFail(curPiece.rotateRight(), curX, curY);
		}

		if (keycode == configKey.keyRotateLeft.getCode()) {
			tryMoveOrFail(curPiece.rotateLeft(), curX, curY);
		}

		if (keycode == configKey.keyDrop.getCode()) {
			dropDown();
		}

		if (keycode == configKey.keyMoveDown.getCode()) {
			oneLineDown();
		}

		if (keycode == configKey.keyHold.getCode()) {
			holdCurrentPiece();
		}
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			gameKeyPressed(e);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Configurations
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void openSettingWindow() {
		pause(false);
		Configurations.getInstance().createFrame();
	}
}