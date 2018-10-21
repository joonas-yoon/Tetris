package kr.ac.jbnu.se.tetris;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigurationKeyBinds extends JFrame {

	public int selectedKeyCode;

	private class Detector extends JFrame implements KeyListener {

		JPanel panel = new JPanel();
		JLabel detectedKey = new JLabel("[NONE]");

		JButton button;
		KeyBind key;

		Detector() {
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			panel.add(new JLabel("Press Any Key:"));
			panel.add(detectedKey);

			add(panel);
			pack();

			setContentPane(panel);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);

			addKeyListener(this);
		}
	
		public void createFrame(JButton btn, KeyBind targetKey){
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
			detectedKey.setText("[ " + key.getText() + " ]");
			button.setText(key.getText());
			
			System.out.println(">> " + Configurations.getInstance().getProperties());
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

	private JButton rightButton = new JButton();

	private JButton leftButton = new JButton();
	
	private JButton holdButton = new JButton();

	public ConfigurationKeyBinds() {
		setTitle("Config - Key Bindings");
		setSize(300, 300);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		leftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				detector.createFrame(leftButton, Configurations.getInstance().getProperties().keyMoveLeft);
			}
		});

		rightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				detector.createFrame(rightButton, Configurations.getInstance().getProperties().keyMoveRight);
			}
		});

		holdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				detector.createFrame(holdButton, Configurations.getInstance().getProperties().keyHold);
			}
		});

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		p1.add(new JLabel("Left:"));
		p1.add(leftButton);
		panel.add(p1);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		p2.add(new JLabel("Right:"));
		p2.add(rightButton);
		panel.add(p2);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER));
		p3.add(new JLabel("Hold:"));
		p3.add(holdButton);
		panel.add(p3);

		add(panel);

		setVisible(true);
		setLocationRelativeTo(null);
		
		updateText();
	}

	public void updateText() {
		leftButton.setText(Configurations.getInstance().getProperties().keyMoveLeft.getText());
		rightButton.setText(Configurations.getInstance().getProperties().keyMoveRight.getText());
		holdButton.setText(Configurations.getInstance().getProperties().keyHold.getText());
		
		System.out.println(Configurations.getInstance().getProperties());
	}
}
