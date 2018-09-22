package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Tetris extends JFrame {

	JLabel statusbar;
	BGM bgm = BGM.getInstance();

	UIPane uipane;

	int VIEWPORT_WIDTH = 480;
	int VIEWPORT_HEIGHT = 640;

	public Tetris() {
		statusbar = new JLabel(" 0");

		Board board = new Board(this);
		board.setBackground(Color.WHITE);
		board.setBorder(new LineBorder(Color.DARK_GRAY));
		board.start();

		uipane = new UIPane(board);
		uipane.setBackground(Color.WHITE);
		uipane.setBorder(new LineBorder(Color.RED));

		JPanel info = new JPanel();
		info.setBackground(Color.BLACK);
		info.setBorder(new LineBorder(Color.BLACK));
		info.setLayout(new FlowLayout());

		JPanel pn = new JPanel();

		GridBagConstraints[] gbc = new GridBagConstraints[4];

		GridBagLayout gbl = new GridBagLayout();
		pn.setLayout(gbl);

		for (int i = 0; i < 4; i++) {
			gbc[i] = new GridBagConstraints();
			gbc[i].fill = GridBagConstraints.BOTH;
			gbc[i].weightx = 1;
			gbc[i].weighty = 1;
		}

		gbc[0].gridx = 0;
		gbc[0].gridy = 0;
		gbc[0].gridheight = 2;
		gbc[0].weightx = 2;
		gbc[0].weighty = 5;
		pn.add(board, gbc[0]);

		gbc[1].gridx = 1;
		gbc[1].gridy = 0;
		pn.add(uipane, gbc[1]);

		gbc[2].gridx = 1;
		gbc[2].gridy = 1;
		gbc[2].weightx = 1;
		gbc[2].weighty = 4;
		pn.add(info, gbc[2]);

		gbc[3].gridx = 0;
		gbc[3].gridy = 2;
		gbc[3].gridwidth = 2;
		gbc[3].weightx = 3;
		gbc[3].weighty = 0.1;
		pn.add(statusbar, gbc[3]);

		setContentPane(pn);
		setTitle("Tetris");
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	public UIPane getUIPane() {
		return uipane;
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}