package org.github.solegaonkar.photography.framework;

import java.io.File;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * The utility class that stores all the data for this application. If required, this can be extended to connect to an
 * SQL/NoSQL DB, or whatever.
 * 
 * @author vs0016025
 *
 */
public final class Database {
	private static final File				folder			= new File("D:/eclipse/workspace/Photography/images");
	private static final LinkedList<String>	files			= new LinkedList<String>();
	private static final LinkedList<Info>	info			= new LinkedList<Info>();
	private static final TreeSet<Info>		availableImages	= new TreeSet<Info>();
	private static int						index			= 0;

	public static void start() throws Exception {
		listFilesForFolder(folder);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Save files and info
			}
		}));
	}

	private static void listFilesForFolder(File folder) throws Exception {
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getAbsolutePath().substring(fileEntry.getAbsolutePath().length() - 4)
						.equalsIgnoreCase(".jpg")) {
					files.add(fileEntry.getAbsolutePath());
				}
			}
		}
	}

	public static String next() {
		if (++index >= files.size())
			index = 0;
		return get();
	}

	public static String prev() {
		if (--index < 0)
			index = files.size() - 1;
		return get();
	}

	public static String get() {
		return files.get(index);
	}

	public static String getTitle() {
		return info.get(index).title;
	}

	public static String getDescription() {
		return info.get(index).description;
	}

	public static String getFlickrId() {
		return info.get(index).flickrId;
	}

	private static class Info {
		String	title		= null;
		String	description	= null;
		String	flickrId	= null;
	}
}
