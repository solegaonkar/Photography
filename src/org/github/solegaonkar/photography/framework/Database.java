package org.github.solegaonkar.photography.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.Gson;

/**
 * The utility class that stores all the data for this application. If required, this can be extended to connect to an
 * SQL/NoSQL DB, or whatever.
 * 
 * @author vs0016025
 *
 */
public final class Database {
	private static final String	BaseDirectory	= System.getProperty("user.dir");
	private static final String	JsonFile		= BaseDirectory + File.separator + "db.json";
	private static final String	ImagesPath		= BaseDirectory + File.separator + "images";

	private static final Gson	gson			= new Gson();
	private static Database		db				= null;
	private static int			index			= 0;

	private ArrayList<Record>	records;

	static {
		try {
			File jf = new File(JsonFile);
			if (jf.exists()) {
				db = gson.fromJson(new FileReader(jf), Database.class);
				if (db == null)
					db = new Database();
				if (db.records == null)
					db.records = new ArrayList<Record>();
				// Cleanup any files that the user deleted from file system
				db.records.removeIf(a -> !new File(a.path).exists());
				// Look for any new files that may have come in.
			} else {
				db = new Database();
				db.records = new ArrayList<Record>();
			}
			scanJpgFiles(new File(ImagesPath));
		} catch (Exception e) {
			CommonUtils.exception(e);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(JsonFile));
					bw.write(gson.toJson(db));
					bw.close();
				} catch (Exception e) {
					CommonUtils.exception(e);
				}
			}
		}));
	}

	public static Database getDb() {
		return db;
	}

	private static void scanJpgFiles(File folder) throws Exception {
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				scanJpgFiles(fileEntry);
			} else {
				if (fileEntry.getAbsolutePath().substring(fileEntry.getAbsolutePath().length() - 4)
						.equalsIgnoreCase(".jpg")) {
					Record r = new Record(fileEntry.getAbsolutePath());
					if (!db.records.contains(r)) {
						db.records.add(r);
					}
				}
			}
		}
	}

	public Record next() {
		if (++index >= records.size())
			index = 0;
		return get();
	}

	public Record prev() {
		if (--index < 0)
			index = records.size() - 1;
		return get();
	}

	public Record get() {
		return records.isEmpty() ? null : records.get(index);
	}

	public HashSet<String> getAlbums() {
		HashSet<String> set = new HashSet<String>();
		records.forEach(r -> set.addAll(r.albums));
		return set;
	}

	static class Record {
		private String			path		= null;
		private String			title		= "Title";
		private String			description	= "Description";
		private String			flickrId	= "";
		private int				rating		= 1;
		private HashSet<String>	albums		= new HashSet<String>();

		public Record() {
		}

		public Record(String path) {
			this.path = path;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (path != null)
				return path.hashCode();
			else
				return 0;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (path == null || obj == null) {
				return false;
			} else if (obj instanceof String) {
				return path.equals(obj);
			} else if (obj instanceof Record) {
				Record other = (Record) obj;
				return path.equals(other.path);
			}
			return false;
		}

		/**
		 * @return the path
		 */
		public final String getPath() {
			return path;
		}

		/**
		 * @param path
		 * the path to set
		 */
		public final void setPath(String path) {
			this.path = path;
		}

		/**
		 * @return the title
		 */
		public final String getTitle() {
			return title;
		}

		/**
		 * @param title
		 * the title to set
		 */
		public final void setTitle(String title) {
			this.title = title;
		}

		/**
		 * @return the description
		 */
		public final String getDescription() {
			return description;
		}

		/**
		 * @param description
		 * the description to set
		 */
		public final void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the flickrId
		 */
		public final String getFlickrId() {
			return flickrId;
		}

		/**
		 * @param flickrId
		 * the flickrId to set
		 */
		public final void setFlickrId(String flickrId) {
			this.flickrId = flickrId;
		}

		/**
		 * @return the albums
		 */
		public final HashSet<String> getAlbums() {
			return albums;
		}

		/**
		 * @param albums
		 * the albums to set
		 */
		public final void setAlbums(HashSet<String> albums) {
			this.albums = albums;
		}

		/**
		 * @return the rating
		 */
		public int getRating() {
			return rating;
		}

		/**
		 * @param rating the rating to set
		 */
		public void setRating(int rating) {
			this.rating = rating;
		}
	}
}
