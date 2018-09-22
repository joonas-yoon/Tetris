package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TetrisGridPanel extends JPanel {

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

	void drawSquare(Graphics g, int x, int y, Tetrominoes shape, boolean translucent) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0) };

		Color color = colors[shape.ordinal()];
		if (translucent) {
			Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
			color = newColor;
		}
		drawRect(g, x, y, color);
	}
}
