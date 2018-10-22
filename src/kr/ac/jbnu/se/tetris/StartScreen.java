package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartScreen extends JFrame {

	JPanel panel;
	JButton classicModeButton;
	JButton stageModeButton;
	JButton settingButton;
	JButton rankingButton;
	JLabel title;
	GridBagConstraints gbc = new GridBagConstraints();

	ImageIcon settingsOrgIcon = new ImageIcon("images/icons/settings.png");
	ImageIcon rankingOrgIcon = new ImageIcon("images/icons/ranking.png");
	Image settingsOrg = settingsOrgIcon.getImage();
	Image rankingOrg = rankingOrgIcon.getImage();
	Image settingsImg = settingsOrg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	Image rankingImg = rankingOrg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

	Tetris game;

	int width = 360;
	int height = 500;

	private BufferedImage backgroundImage;

	StartScreen() {
		game = new Tetris(this);

		BGM.getInstance().play();

		try {
			backgroundImage = ImageIO.read(new File("images/intro.png"));
			width = backgroundImage.getWidth();
			height = backgroundImage.getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, this);
			}
		};
		panel.setLayout(new GridBagLayout());

		gbc.insets = new Insets(30, -20, 30, -20);

		title = new JLabel("TETRIS");
		gbc.gridx = 2;
		gbc.gridy = 0;
		title.setFont(new Font("Consolas", Font.BOLD, 50));
		title.setForeground(Color.BLACK);
		panel.add(title, gbc);

		classicModeButton = new JButton("Classic Mode");
		gbc.gridx = 1;
		gbc.gridy = 3;
		classicModeButton.setFocusPainted(false);
		panel.add(classicModeButton, gbc);

		stageModeButton = new JButton("Stage Mode");
		gbc.gridx = 3;
		gbc.gridy = 3;
		stageModeButton.setFocusPainted(false);
		panel.add(stageModeButton, gbc);

		gbc.insets = new Insets(50, 0, 30, 0);

		settingButton = new JButton(new ImageIcon(settingsImg));
		gbc.gridx = 1;
		gbc.gridy = 5;
		settingButton.setBorderPainted(false);
		settingButton.setContentAreaFilled(false);
		settingButton.setFocusPainted(false);
		panel.add(settingButton, gbc);

		rankingButton = new JButton(new ImageIcon(rankingImg));
		gbc.gridx = 3;
		gbc.gridy = 5;
		rankingButton.setBorderPainted(false);
		rankingButton.setContentAreaFilled(false);
		rankingButton.setFocusPainted(false);
		panel.add(rankingButton, gbc);

		classicModeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startGame(false);
			}
		});

		stageModeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startGame(true);
			}
		});

		settingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Configurations.getInstance().createFrame();
			}
		});

		rankingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Ranking.getInstance().load();
				Ranking.getInstance().showRanking();
			}
		});

		createFrame();
	}

	public void createFrame() {
		setTitle("Tetris");
		setVisible(true);
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(panel);
	}

	public void startGame(boolean isStageMode) {
		game.createFrame(isStageMode);
		dispose();
	}

	public void quit() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public static void main(String[] args) {
		StartScreen s = new StartScreen();
	}
}
