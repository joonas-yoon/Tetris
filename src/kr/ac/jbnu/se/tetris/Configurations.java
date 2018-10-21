package kr.ac.jbnu.se.tetris;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Configurations extends JFrame {

	private static transient Configurations instance = new Configurations();

	private ConfigurationProperties properties = new ConfigurationProperties();

	private boolean loaded = false;

	private JPanel panel;

	private JButton keybindButton;

	private JButton closeButton;

	private JButton volumeMusicDownButton;

	private JButton volumeMusicUpButton;

	private JLabel volumeMusicText = new JLabel("<music: load..>");

	private JButton volumeEffectDownButton;

	private JButton volumeEffectUpButton;

	private JLabel volumeEffectText = new JLabel("<effect: load..>");

	private Configurations() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		keybindButton = new JButton("Key Setting");
		keybindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationKeyBinds window = new ConfigurationKeyBinds();
			}
		});

		closeButton = new JButton("Back");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});

		volumeMusicDownButton = new JButton("-");
		volumeMusicUpButton = new JButton("+");
		volumeMusicDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeMusic(-1);
				BGM.getInstance().setVolume(getProperties().getVolumeMusic());
				save();
			}
		});
		volumeMusicUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeMusic(+1);
				BGM.getInstance().setVolume(getProperties().getVolumeMusic());
				save();
			}
		});

		volumeEffectDownButton = new JButton("-");
		volumeEffectUpButton = new JButton("+");
		volumeEffectDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeEffect(-1);
				save();
			}
		});
		volumeEffectUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeEffect(+1);
				save();
			}
		});

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		p1.add(volumeMusicDownButton);
		p1.add(volumeMusicText);
		p1.add(volumeMusicUpButton);
		panel.add(p1);

		JPanel pEffect = new JPanel();
		pEffect.setLayout(new FlowLayout(FlowLayout.CENTER));
		pEffect.add(volumeEffectDownButton);
		pEffect.add(volumeEffectText);
		pEffect.add(volumeEffectUpButton);
		panel.add(pEffect);

		JPanel p2 = new JPanel();
		p2.add(keybindButton);
		panel.add(p2);

		JPanel p3 = new JPanel();
		p3.add(closeButton);
		panel.add(p3);

		add(panel);
	}

	public static Configurations getInstance() {
		return instance;
	}

	public ConfigurationProperties getProperties() {
		if (!loaded)
			load();
		return properties;
	}

	public void createFrame() {
		setTitle("Config");
		setSize(300, 500);

		// Set the new frame location
		setLocationRelativeTo(null);
		setContentPane(panel);
		setVisible(true);
		
		update();
	}

	public void closeFrame() {
		setVisible(false);
		dispose();
	}

	public void update() {
		if (properties != null) {
			volumeMusicText.setText("<music: " + properties.getVolumeMusic() + ">");
			volumeEffectText.setText("<Effect: " + properties.getVolumeEffect() + ">");
		}
	}

	synchronized public void save() {
		SerializationDemonstrator.serialize(properties, ConfigurationProperties.FILENAME);

		loaded = false;
	}

	synchronized public void load() {
		if (isLoaded())
			return;

		File f = new File(ConfigurationProperties.FILENAME);
		if (f.isFile() == false)
			return;
		try {
			properties = SerializationDemonstrator.deserialize(ConfigurationProperties.FILENAME, ConfigurationProperties.class);
		} catch(Exception e){
			e.printStackTrace();
			return;
		}

		update();

		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
