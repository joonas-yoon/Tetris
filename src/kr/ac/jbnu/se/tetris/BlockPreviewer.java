package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class BlockPreviewer extends TetrisGridPanel {
	Shape block;

	public BlockPreviewer() {
		block = new Shape();
		block.setShape(Tetrominoes.NoShape);
	}
	
	public BlockPreviewer(Shape block) {
		setBlock(block); 
		setSize(5, 5);
	}
	
	public void setBlock(Shape block) {
		this.block = block;
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
