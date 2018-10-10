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
import javax.swing.JLabel;
import javax.swing.Timer;

public class Board extends TetrisGridPanel implements ActionListener {

	Timer timer;
	boolean isFallingFinished = false;
	boolean isStarted = false;
	boolean isPaused = false;
	int numLinesRemoved = 0;
	int curX = 0;
	int curY = 0;
	JLabel statusbar;
	Shape curPiece;
	Tetrominoes[] board;

	UIPane uipane;
	public BlockFactory nextBlocks = new BlockFactory(5);

	SoundPlayer sound = new SoundPlayer();
	BGM bgm = BGM.getInstance();

	Color comboFontColor = Color.BLACK;
	int comboOpacity = 100;
	int comboCount = 0;

	public Board(Tetris parent) {
		setFocusable(true);
		curPiece = new Shape();
		timer = new Timer(400, this);
		timer.start();

		statusbar = parent.getStatusBar();
		uipane = parent.getUIPane();
		board = new Tetrominoes[BoardWidth * BoardHeight];
		addKeyListener(new TAdapter());
		clearBoard();

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				if (comboOpacity - 5 >= 0) {
					comboOpacity -= 5;
					repaint();
				}
			}
		}, 100, 100);
	}

	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
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
	}

	private void refreshText() {
		if (isPaused)
			statusbar.setText("paused");
		else if (isStarted)
			statusbar.setText(String.valueOf(numLinesRemoved));
		else
			statusbar.setText("game over");
	}

	private void showComboMessage(Graphics g, String text) {
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g.create();

			Font font = new Font("Comic Sans MS", Font.BOLD, 48);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int x = ((getWidth() - fm.stringWidth(text)) / 2);
			int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

			Color newColor = new Color(comboFontColor.getRed(), comboFontColor.getGreen(), comboFontColor.getBlue(),
					comboOpacity * 255 / 100);
			g2d.setColor(newColor);

			y += comboOpacity / 5;
			g2d.drawString(text, x, y);

			g2d.dispose();
		}
	}

	private void processCombo(Graphics g) {
		// 0 for Test, 1 for production
		if (comboCount > 0) {
			showComboMessage(g, comboCount + " Combo!");
		} else {
			comboOpacity = 100;
			showComboMessage(g, "XX");
		}
	}

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
		timer.start();
		if (!bgm.isPlaying)
			bgm.play();
	}

	private void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			bgm.pause();
		} else {
			timer.start();
			bgm.resume();
		}

		refreshText();
		repaint();
	}

	private void gameover() {
		curPiece.setShape(Tetrominoes.NoShape);
		timer.stop();
		isStarted = false;
		refreshText();
		bgm.stop();
		sound.play("sounds/gameover.wav", 0);
	}

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

		if (curPiece.getShape() != Tetrominoes.NoShape) {
			drawShape(g, curX, curY, curPiece, false);
		}

		preview(g);

		processCombo(g);
	}

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

	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
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

	private void newPiece() {
		curPiece = nextBlocks.getNextBlock();
		nextBlocks.pop();
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

	private void preview(Graphics g) {
		if (!isStarted || isPaused || isFallingFinished)
			return;

		int bottomPredict = getYPosPredict(curPiece, curX, curY);
		drawShape(g, curX, bottomPredict, curPiece, true);
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
			refreshText();
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);

			comboCount += 1;
			comboOpacity = 100;

			repaint();
			sound.play("sounds/beep1.wav", 1);
		} else {
			comboCount = 0;
		}
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int keycode = e.getKeyCode();

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				if (keycode == KeyEvent.VK_ENTER) {
					start();
				}
				return;
			}

			if (keycode == 'p' || keycode == 'P') {
				pause();
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
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			case KeyEvent.VK_1:
				bgm.changePrev();
				break;
			case KeyEvent.VK_2:
				bgm.changeNext();
				break;
			}

		}
	}
}