package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Tetris extends JFrame {

	JLabel statusbar;
	JLabel scoreText;
	BGM bgm = BGM.getInstance();

	final int VIEWPORT_PADDING = 40;
	int VIEWPORT_WIDTH = 520;
	int VIEWPORT_HEIGHT = 640;

	BlockPreviewer[] nextBlocksPreview;
	BlockPreviewer holdBlockPreview;

	public Tetris() {
		statusbar = new JLabel(" 0");
		statusbar.setForeground(Color.WHITE);

		scoreText = new JLabel("score", JLabel.CENTER);
		scoreText.setBackground(Color.GRAY);
		scoreText.setForeground(Color.WHITE);
		scoreText.setOpaque(true);

		Board board = new Board(this);
		board.setBackground(Color.WHITE);
		board.setBorder(new LineBorder(Color.DARK_GRAY));
		board.start();

		JPanel info = new JPanel();
		info.setBackground(Color.WHITE);
		info.setBorder(new LineBorder(Color.BLACK));
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.setPreferredSize(new Dimension(100, getHeight()));

		int nextBlockCount = board.nextBlocks.getSize();
		nextBlocksPreview = new BlockPreviewer[nextBlockCount];
		for (int i = 0; i < nextBlockCount; ++i) {
			nextBlocksPreview[i] = new BlockPreviewer(board.nextBlocks.getBlock(i));
			Color borderColor = Color.LIGHT_GRAY;
			if (i == 0)
				borderColor = Color.ORANGE;
			nextBlocksPreview[i].setBorder(BorderFactory.createLineBorder(borderColor, 5));
			info.add(nextBlocksPreview[i]);
		}

		holdBlockPreview = new BlockPreviewer();
		holdBlockPreview.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());
		pn.setBorder(BorderFactory.createEmptyBorder(VIEWPORT_PADDING, 2 * VIEWPORT_PADDING, VIEWPORT_PADDING,
				2 * VIEWPORT_PADDING));
		pn.setMinimumSize(new Dimension(VIEWPORT_WIDTH - VIEWPORT_PADDING, VIEWPORT_HEIGHT - VIEWPORT_PADDING));

		JPanel scoreAndSettings = new JPanel(new GridLayout(1, 3));
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JButton settingButton = new JButton("[*]");
		settingButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		buttons.add(settingButton);
		buttons.setOpaque(false);
		buttons.setFocusable(false);

		JLabel emptyLabel = new JLabel();
		emptyLabel.setOpaque(false);

		// scoreAndSettings.add(buttons);
		scoreAndSettings.add(new JLabel());
		scoreAndSettings.add(scoreText);
		scoreAndSettings.add(emptyLabel);
		scoreAndSettings.setOpaque(false);

		pn.add(board, BorderLayout.CENTER);
		pn.add(holdBlockPreview, BorderLayout.LINE_START);
		pn.add(info, BorderLayout.LINE_END);
		pn.add(scoreAndSettings, BorderLayout.PAGE_START);
		pn.add(statusbar, BorderLayout.PAGE_END);

		board.requestFocusInWindow();

		pn.setBackground(Color.black);

		setContentPane(pn);

		setTitle("Tetris");
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	public JLabel getScoreText() {
		return scoreText;
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

	public void updateNextBlocks(BlockFactory nextBlocks) {
		if (nextBlocksPreview == null)
			return;

		int nextBlockCount = nextBlocks.getSize();
		for (int i = 0; i < nextBlockCount; ++i) {
			nextBlocksPreview[i].setBlock(nextBlocks.getBlock(i));
		}
	}
}