package net.minecraft.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	public static final File CONFIG_FILE = new File(System.getProperty("user.home") + "/appdata/roaming/.Orizia/V2/orz.config");

	private static Properties config;

	public static void load() {
		config = new Properties();

		FileInputStream input = null;
		try {
			input = new FileInputStream(CONFIG_FILE);
			config.load(input);
		} catch (IOException e) {
			System.out.println("ATTENTION : Impossible de charger la config !");
			e.printStackTrace();
			setDefaults();
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
				}	
		}
	}

	public static String get(String key) {
		if (config == null) {
			System.out.println("ATTENTION : Tentative de get de la config, sans qu'elle soit chargee, pour la cle : " + key);
			return null;
		}

		return config.getProperty(key);
	}

	public static void set(String key, String value) {
		if (config == null) {
			System.out.println("ATTENTION : Tentative de get de la config, sans qu'elle soit chargee, pour la cle : " + key);
			return;
		}

		config.setProperty(key, value);
	}

	public static void save() {
		if (config == null) {
			System.out.println("ATTENTION : Tentative de sauvegarde de la config, sans qu'elle soit chargee");
			return;
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(CONFIG_FILE);
			config.store(out, "Configuration - Orizia");
		} catch (IOException e) {
			System.out.println("ATTENTION : Impossible de sauvegarder la config !");
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
	}

	private static void setDefaults() {
		System.out.println("Les valeurs par default seront utilisees.");

		config = new Properties();

		config.setProperty("hud.armor", "OFF");
		config.setProperty("sneak.toggle", "OFF");
		config.setProperty("sprint.toggle", "OFF");

		save();
	}

}