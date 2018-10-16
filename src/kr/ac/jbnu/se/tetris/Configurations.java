package kr.ac.jbnu.se.tetris;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Configurations {

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
