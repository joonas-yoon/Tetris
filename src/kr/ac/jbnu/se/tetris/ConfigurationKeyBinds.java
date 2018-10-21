package kr.ac.jbnu.se.tetris;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ConfigurationKeyBinds extends JFrame {

	public int selectedKeyCode;

	private class Detector extends JFrame implements KeyListener {

		JPanel panel = new JPanel();
		JLabel infoText = new JLabel("Press Any Key");
		JButton button;
		KeyBind key;

		Detector() {
			panel.add(infoText);

			add(panel);
			pack();

			setContentPane(panel);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);

			addKeyListener(this);
		}

		public void createFrame(JButton btn, KeyBind targetKey) {
			button = btn;
			key = targetKey;

			setContentPane(panel);
			setLocationRelativeTo(null);
			requestFocusInWindow();
			setVisible(true);
		}

		@Override
		public void dispose() {
			Configurations.getInstance().save();
			super.dispose();
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			key.set(keyCode);
			button.setText(key.getText());
			this.dispose();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	Detector detector = new Detector();

	JLabel keyLeftText = new JLabel();

	ConfigurationProperties properties = Configurations.getInstance().getProperties();

	KeyBind[] configKeys = { properties.keyMoveLeft, properties.keyMoveRight, properties.keyMoveDown,
			properties.keyRotateLeft, properties.keyRotateRight, properties.keyPaused, properties.keyDrop,
			properties.keyHold };

	private JButton[] buttons = new JButton[ConfigurationProperties.KEY_BINDING_COUNT];

	public ConfigurationKeyBinds() {
		setTitle("Config - Key Bindings");

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(buttons.length, 2));

		String[] titles = { "Move Left", "Move Right", "Move Down", "Rotate Left", "Rotate Right", "Paused", "Drop",
				"Hold" };

		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			JButton btn = buttons[i];
			KeyBind key = configKeys[i];
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					detector.createFrame(btn, key);
				}
			});
			btn.setPreferredSize(new Dimension(100, 50));
			panel.add(new JLabel(titles[i], SwingConstants.CENTER));
			panel.add(btn);
		}
		add(panel);
		pack();

		setVisible(true);
		setLocationRelativeTo(null);

		updateText();
	}

	public void updateText() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(configKeys[i].getText());
		}
		revalidate();
	}
}
