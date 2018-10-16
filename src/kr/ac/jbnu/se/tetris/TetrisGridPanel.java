package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TetrisGridPanel extends JPanel {
	
	public static Color BLOCK_COLORS[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
			new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
			new Color(218, 170, 0), new Color(128, 128, 128) };

	int BoardWidth = 12;
	int BoardHeight = 23;

	public void setSize(int width, int height) {
		BoardWidth = width;
		BoardHeight = height;
	}

	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;
	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	void drawRect(Graphics g, int x, int y, Color color) {
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	void drawRect(Graphics g, int x, int y, Tetrominoes shape, boolean translucent) {
		Color color = BLOCK_COLORS[shape.ordinal()];
		if (translucent) {
			Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
			color = newColor;
		}
		drawRect(g, x, y, color);
	}

	void drawShape(Graphics g, int x, int y, Shape piece, boolean translucent) {
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		for (int i = 0; i < 4; ++i) {
			int rx = x + piece.x(i);
			int ry = y - piece.y(i);
			drawRect(g, rx * squareWidth(), boardTop + (BoardHeight - ry - 1) * squareHeight(), piece.getShape(),
					translucent);
		}
	}
}
