package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class UIPane extends TetrisGridPanel {
	Board board;

	private BlockFactory nextBlocks;

	public UIPane(Board parent) {
		board = parent;

		nextBlocks = board.nextBlocks;

		setSize(12, 8);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				int x = j * squareWidth();
				int y = i * squareHeight();
				drawRect(g, x, y, new Color(254, 254, 254, 50));
			}
		}

		for (int nth = 0; nth < 2; ++nth) {
			Shape nextBlock = nextBlocks.getBlock(nth);

			if (nextBlock != null && nextBlock.getShape() != Tetrominoes.NoShape) {
				int x = BoardWidth * (nth + 1) / 3 + nth - 1;
				int y = BoardHeight / 2;
				drawShape(g, x, y, nextBlock, false);
			}
		}

		repaint();
	}
}
