package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class StageEditor extends JFrame {

	final static int VIEWPORT_WIDTH = 420;
	final static int VIEWPORT_HEIGHT = 800;

	Board stage;

	public StageEditor() {
		init();
	}
	
	public void init(){
		stage = new Board();
	}

	public void createFrame() {
		JPanel pn = new JPanel();
		pn.setLayout(new BorderLayout());

		stage.setBackground(Color.WHITE);
		stage.setBorder(new LineBorder(Color.DARK_GRAY));
		pn.add(stage, BorderLayout.CENTER);

		stage.requestFocusInWindow();

		pn.add(new JButton("SAVE"), BorderLayout.PAGE_END);

		setContentPane(pn);

		setTitle("Stage Editor");
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		setLocationRelativeTo(null);
	}
	
	public void showFrame(boolean show){
		
		for(Tetrominoes t : stage.getBlockState()){
			System.out.println(t);
		}
		setVisible(show);
	}
	
	@Override
	public void dispose(){
		setVisible(false);
	}
}
