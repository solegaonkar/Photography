package org.github.solegaonkar.photography.framework;

import org.github.solegaonkar.photography.ui.UI;

public class Main {
	private static UI ui = new UI();
	private static Database db = Database.getDb();

	public static void main(String[] args) throws Exception {
		try {
			ui.setPhotograph(new Photograph(db.get()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ui.setVisible(true);
	}

	public static void next() {
		try {
			ui.setPhotograph(new Photograph(db.next()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();
	}

	public static void prev() {
		try {
			ui.setPhotograph(new Photograph(db.prev()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();
	}
}
