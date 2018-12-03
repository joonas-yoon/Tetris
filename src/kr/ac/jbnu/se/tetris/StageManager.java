package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class StageManager extends JFrame {
	
	private static transient StageManager instance = new StageManager();

	final static int VIEWPORT_WIDTH = 420;
	final static int VIEWPORT_HEIGHT = 800;

	int boardWidth, boardHeight;

	int selectedStageId;
	Board currentBoard;
	private Board[] stages = new Board[GameStaged.FINAL_STAGE];

	JButton clearButton;
	JButton nextButton;
	JButton prevButton;

	private StageManager() {
		init();
		boardWidth = Board.DEFAULT_WIDTH;
		boardHeight = Board.DEFAULT_HEIGHT;

		for (int i = 0; i < GameStaged.FINAL_STAGE; i++) {
			Board stage = generateRandomStage(i);
			stage.readonly(false);
			stage.setBackground(Color.WHITE);
			stage.setBorder(new LineBorder(Color.DARK_GRAY));
			stages[i] = stage;
		}

		selectedStageId = 0;
		currentBoard = stages[0];
		currentBoard.blocks = stages[0].blocks;
	}
	
	public static StageManager getInstance() {
		return instance;
	}

	public void init() {
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentBoard.clearBoard();
			}
		});

		nextButton = new JButton("Next Stage");
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedStageId += 1;
				if (selectedStageId > GameStaged.FINAL_STAGE - 1)
					selectedStageId = GameStaged.FINAL_STAGE - 1;
				currentBoard.blocks = getStage(selectedStageId).blocks;
				currentBoard.repaint();
			}
		});

		prevButton = new JButton("Prev Stage");
		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedStageId -= 1;
				if (selectedStageId < 0)
					selectedStageId = 0;
				currentBoard.blocks = getStage(selectedStageId).blocks;
				currentBoard.repaint();
			}
		});
	}

	public void createFrame() {
		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());

		pn.add(currentBoard, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.add(prevButton);
		buttons.add(clearButton);
		buttons.add(nextButton);
		pn.add(buttons, BorderLayout.PAGE_END);

		pn.requestFocusInWindow();
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

	private Board getStage(int stageID) {
		return stages[stageID];
	}
	
	public Tetrominoes[] getStageBlocks(int stageID) {
		return stages[stageID].blocks;
	}

	// stage: [0, GameStaged.FINAL_STAGE)
	public Board generateRandomStage(int stage) {
		int totalBlocks = boardWidth * boardHeight;
		int[] density = { 20, 30, 40, 60, 80 };
		int blocks = (int) (totalBlocks * (density[stage / density.length] / 100.0));
		int height = boardHeight * ((stage % density.length) + 1) / density.length;
		height = Math.max(5, height);
		return generateRandomBlocks(blocks, height);
	}

	private Board generateRandomBlocks(int maxBlocks, int maxHeight) {
		Random rand = new Random();
		int blocks = rand.nextInt(maxBlocks + 1);
		Board temp = new Board(boardWidth, boardHeight);
		for (int i = 0; i < blocks; ++i) {
			int x = rand.nextInt(boardWidth);
			int y = rand.nextInt(maxHeight);
			temp.setShapeAt(x, y, Tetrominoes.DeadShape);
		}

		for (int y = 0; y < maxHeight; y++) {
			for (int x = 0; x < boardWidth; x++)
				System.out.print(temp.getShapeAt(x, y) + " ");
			System.out.println("");
		}
		System.out.println("------------------------------");
		return temp;
	}
}
