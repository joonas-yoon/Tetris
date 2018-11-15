package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Tetris extends JFrame {
	Configurations configs = Configurations.getInstance();

	JLabel statusbar;
	JLabel scoreText;
	BGM bgm = BGM.getInstance();

	static final int VIEWPORT_PADDING = 30;
	int VIEWPORT_WIDTH = 600;
	int VIEWPORT_HEIGHT = 800;

	BlockPreviewer[] nextBlocksPreview;
	BlockPreviewer holdBlockPreview;

	StartScreen parent;
	Board board;

	public Tetris(StartScreen parent) {
		this.parent = parent;
	}

	public void createFrame(boolean isStageMode) {
		statusbar = new JLabel(" 0");
		statusbar.setForeground(Color.WHITE);

		scoreText = new JLabel("score", JLabel.CENTER);
		scoreText.setBackground(Color.GRAY);
		scoreText.setForeground(Color.WHITE);
		scoreText.setOpaque(true);

		if (isStageMode) {
			board = new BoardStages(this);
		} else {
			board = new Board(this);
		}
		board.setBackground(Color.WHITE);
		board.setBorder(new LineBorder(Color.DARK_GRAY));
		board.ready();

		JPanel blockPreviewsPanel = new JPanel();
		blockPreviewsPanel.setBorder(new LineBorder(Color.BLACK));
		blockPreviewsPanel.setLayout(new BoxLayout(blockPreviewsPanel, BoxLayout.Y_AXIS));
		blockPreviewsPanel.setPreferredSize(new Dimension(100, 100));
		blockPreviewsPanel.setOpaque(false);

		ArrayList<BlockPreviewer> blockPanels = new ArrayList<>();

		holdBlockPreview = new BlockPreviewer();
		holdBlockPreview.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		blockPanels.add(holdBlockPreview);

		int nextBlockCount = board.nextBlocks.getSize();
		nextBlocksPreview = new BlockPreviewer[nextBlockCount];
		for (int i = 0; i < nextBlockCount; ++i) {
			nextBlocksPreview[i] = new BlockPreviewer(board.nextBlocks.getBlock(i));
			Color borderColor = i == 0 ? Color.RED : Color.LIGHT_GRAY;
			nextBlocksPreview[i].setBorder(BorderFactory.createLineBorder(borderColor, 5));
			blockPanels.add(nextBlocksPreview[i]);
		}

		for (BlockPreviewer bp : blockPanels) {
			JPanel tmpPane = new JPanel(new GridBagLayout());
			tmpPane.add(bp);
			blockPreviewsPanel.add(tmpPane);
		}

		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());
		pn.setBorder(BorderFactory.createEmptyBorder(VIEWPORT_PADDING, 3 * VIEWPORT_PADDING, VIEWPORT_PADDING,
				3 * VIEWPORT_PADDING));
		pn.setMinimumSize(new Dimension(VIEWPORT_WIDTH - VIEWPORT_PADDING, VIEWPORT_HEIGHT - VIEWPORT_PADDING));

		JPanel scoreAndSettings = new JPanel(new GridLayout(1, 3));
		scoreAndSettings.add(new JLabel());
		scoreAndSettings.add(scoreText);
		scoreAndSettings.add(new JLabel());
		scoreAndSettings.setOpaque(false);

		pn.add(board, BorderLayout.CENTER);
		pn.add(blockPreviewsPanel, BorderLayout.LINE_START);
		pn.add(scoreAndSettings, BorderLayout.PAGE_START);
		pn.add(statusbar, BorderLayout.PAGE_END);

		board.requestFocusInWindow();

		pn.setBackground(Color.black);

		setContentPane(pn);

		setTitle("Tetris");
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	@Override
	public void dispose() {
		parent.createFrame();
		if (board != null) {
			board.quit();
		}
		super.dispose();
	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	public JLabel getScoreText() {
		return scoreText;
	}

	public void updateNextBlocks(BlockFactory nextBlocks) {
		if (nextBlocksPreview == null) {
			return;
		}

		int nextBlockCount = nextBlocks.getSize();
		for (int i = 0; i < nextBlockCount; ++i) {
			nextBlocksPreview[i].setBlock(nextBlocks.getBlock(i));
		}
	}
}