package org.github.solegaonkar.photography.framework;

import org.github.solegaonkar.photography.ui.UI;

public class Main {
	private static UI frame = new UI();

	public static void main(String[] args) throws Exception {
		Database.start();
		try {
			loadPhoto(Database.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
	}

	public static void next() {
		try {
			loadPhoto(Database.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();
	}

	public static void prev() {
		try {
			loadPhoto(Database.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();
	}

	private static void loadPhoto(String file) throws Exception {
		frame.setPhotograph(new Photograph(file));
	}

}
