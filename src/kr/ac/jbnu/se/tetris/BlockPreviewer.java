package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class BlockPreviewer extends TetrisGridPanel {
	Shape block;

	public BlockPreviewer() {
		Shape tmp = new Shape();
		tmp.setShape(Tetrominoes.NoShape);
		setBlock(tmp);
		resize();
	}
	
	public BlockPreviewer(Shape block) {
		setBlock(block);
		resize();
	}
	
	public void setBlock(Shape block) {
		this.block = block;
		resize();
		repaint();
	}
	
	public void resize() {
		int size = Math.max(block.getWidth(), block.getHeight()) + 2;
		setSize(size, size);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// draw grid on background
		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				int x = j * squareWidth();
				int y = i * squareHeight();
				drawRect(g, x, y, new Color(254, 254, 254, 50));
			}
		}

		if (block != null && block.getShape() != Tetrominoes.NoShape) {
			int x = BoardWidth / 2;
			int y = BoardHeight / 2;
			drawShape(g, x, y, block, false);
		}

		repaint();
	}
}
