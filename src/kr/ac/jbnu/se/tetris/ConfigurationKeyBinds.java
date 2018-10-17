package kr.ac.jbnu.se.tetris;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigurationKeyBinds extends JFrame {

	public ConfigurationKeyBinds() {
		setTitle("Config - Key Bindings");
		setSize(300, 300);
		
		JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton leftButton = new JButton(KeyEvent.getKeyText(Configurations.getInstance().getProperties().keyMoveLeft));
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
            	System.out.println("Left");
            }
        });
        
        JButton rightButton = new JButton(KeyEvent.getKeyText(Configurations.getInstance().getProperties().keyMoveRight));
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
            	System.out.println("Right");
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
		
		add(panel);

		setVisible(true);
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);

		// Get the screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		// Calculate the frame location
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;

		// Set the new frame location
		setLocation(x, y);
	}

	public void setSize(Dimension size) {
		setSize(size.width, size.height);
	}
}
