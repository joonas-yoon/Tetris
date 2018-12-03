package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class StageEditor extends JFrame {

	final static int VIEWPORT_WIDTH = 420;
	final static int VIEWPORT_HEIGHT = 800;

	Board board;

	public StageEditor() {
		init();
	}

	public void init() {
		board = new Board();
		board.readonly(false);
	}

	public void createFrame() {
		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());

		board.setBackground(Color.WHITE);
		board.setBorder(new LineBorder(Color.DARK_GRAY));
		pn.add(board, BorderLayout.CENTER);

		board.requestFocusInWindow();

		pn.add(new JButton("SAVE"), BorderLayout.PAGE_END);

		setContentPane(pn);

		setTitle("Stage Editor");
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		setLocationRelativeTo(null);
	}

	public void showFrame(boolean show) {
		setVisible(show);
	}

	@Override
	public void dispose() {
		setVisible(false);
	}
}
