package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends TetrisGridPanel {

	Tetrominoes[] blocks;

	public Board() {
		setSize(BoardWidth, BoardHeight);
	}

	public Board(int width, int height) {
		setSize(width, height);
	}

	class Editable extends MouseAdapter {
		int getX(MouseEvent e) {
			return e.getX() / squareWidth();
		}

		int getY(MouseEvent e) {
			return BoardHeight - (e.getY() + squareHeight() / 2) / squareHeight();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			toggleShapeAt(getX(e), getY(e), Tetrominoes.DeadShape);
		}
	}

	Editable mouseEvent = new Editable();

	public void editMode(boolean active) {
		if (active) {
			addMouseListener(mouseEvent);
		} else {
			removeMouseListener(mouseEvent);
		}
	}
	
	public void readonly(boolean flag){
		editMode(!flag);
	}

	public void toggleShapeAt(int x, int y, Tetrominoes shape) {
		if (getShapeAt(x, y) == Tetrominoes.NoShape) {
			setShapeAt(x, y, shape);
		} else {
			setShapeAt(x, y, Tetrominoes.NoShape);
		}
		repaint();
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);

		blocks = new Tetrominoes[BoardWidth * BoardHeight];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = Tetrominoes.NoShape;
		}
	}

	public Tetrominoes[] getBlockState() {
		return blocks;
	}

	protected void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
			blocks[i] = Tetrominoes.NoShape;
		}
	}

	Tetrominoes getShapeAt(int x, int y) {
		return blocks[(y * BoardWidth) + x];
	}

	void setShapeAt(int x, int y, Tetrominoes newShape) {
		blocks[(y * BoardWidth) + x] = newShape;
	}

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = getShapeAt(j, BoardHeight - i - 1);
				int x = j * squareWidth();
				int y = boardTop + i * squareHeight();
				if (shape != Tetrominoes.NoShape) {
					drawRect(g, x, y, shape, false);
				} else {
					drawRect(g, x, y, new Color(254, 254, 254, 50));
				}
			}
		}
	}

	protected int removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			for (int j = 0; j < BoardWidth; ++j) {
				if (getShapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j) {
						blocks[k * BoardWidth + j] = getShapeAt(j, k + 1);
					}
				}
			}
		}

		return numFullLines;
	}
}
