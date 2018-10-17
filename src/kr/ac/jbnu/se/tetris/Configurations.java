package kr.ac.jbnu.se.tetris;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Configurations extends JFrame {

	private static transient Configurations instance = new Configurations();

	public static String FILENAME = "configs.dat";

	private ConfigurationProperties properties = new ConfigurationProperties();

	private boolean loaded = false;

	private Configurations() {
		// TODO Auto-generated constructor stub
	}

	public static Configurations getInstance() {
		return instance;
	}

	public ConfigurationProperties getProperties() {
		if (!loaded)
			load();
		return properties;
	}
	
	public void createFrame(){
		setTitle("Config");
		setSize(300, 500);

		// Get the screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		// Set the new frame location
		setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		
		// ConfigurationKeyBinds window = new ConfigurationKeyBinds();
		JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton keybindButton = new JButton("Key Setting");
        keybindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationKeyBinds window = new ConfigurationKeyBinds();
			}
		});
        
        JButton closeButton = new JButton("Back");
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});

        JButton volumeMusicDownButton = new JButton("-");
        JButton volumeMusicUpButton = new JButton("+");
        volumeMusicDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeMusic(-1);
				save();
			}
		});
        volumeMusicUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getProperties().increaseVolumeMusic(+1);
				save();
			}
		});

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		p1.add(volumeMusicDownButton);
		p1.add(new JLabel("<music: " + getProperties().getVolumeMusic() + ">"));
		p1.add(volumeMusicUpButton);
		panel.add(p1);
		

        JButton volumeEffectDownButton = new JButton("-");
        JButton volumeEffectUpButton = new JButton("+");
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

		JPanel pEffect = new JPanel();
		pEffect.setLayout(new FlowLayout(FlowLayout.CENTER));
		pEffect.add(volumeEffectDownButton);
		pEffect.add(new JLabel("<Effect: " + getProperties().getVolumeEffect() + ">"));
		pEffect.add(volumeEffectUpButton);
		panel.add(pEffect);

		JPanel p2 = new JPanel();
        p2.add(keybindButton);
        panel.add(p2);

		JPanel p3 = new JPanel();
        p3.add(closeButton);
        panel.add(p3);
        
        add(panel);
		
		setVisible(true);
	}
	
	public void closeFrame(){
		setVisible(false);
		dispose();
	}

	synchronized public void save() {
		try {
			FileOutputStream fos = new FileOutputStream(FILENAME);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(properties);
			oos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		loaded = false;
	}

	synchronized public void load() {
		File f = new File(FILENAME);
		if (f.isFile() == false)
			return;

		try {
			FileInputStream fis = new FileInputStream(FILENAME);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(bis);
			properties = (ConfigurationProperties) ois.readObject();
			ois.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
