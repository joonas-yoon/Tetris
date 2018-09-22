package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame {

	JLabel statusbar;
	BGM bgm = BGM.getInstance();
	
	int VIEWPORT_WIDTH = 640;
	int VIEWPORT_HEIGHT = 480;

	public Tetris() {

		statusbar = new JLabel(" 0");
		add(statusbar, BorderLayout.SOUTH);
		Board board = new Board(this);
		add(board);
		board.setBackground(Color.WHITE);
		board.start();

		setSize(VIEWPORT_WIDTH * 2 / 3, VIEWPORT_HEIGHT);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}