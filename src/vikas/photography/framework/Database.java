package vikas.photography.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The utility class that stores all the data for this application. If required, this can be extended to connect to an
 * SQL/NoSQL DB, or whatever.
 * 
 * @author vs0016025
 *
 */
public class Database implements Serializable {
	private static final long						serialVersionUID	= -8811747209971653973L;
	private static Database							db					= null;
	private static File								dbFile				= new File(Constants.DataFile);

	private HashMap<ImageType, ArrayList<Record>>	images				= null;
	private transient Set<String>					allAlbums			= null;

	/**
	 * Private constructor to help singleton.
	 */
	private Database() {
	}

	/**
	 * Check if the Database file is present in its path. If it is present, load it. Else, instantiate a new one.
	 * 
	 */
	private static void load() {
		try {
			if (db == null) {
				if (dbFile.exists()) {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile));
					db = (Database) ois.readObject();
					ois.close();
				} else {
					db = new Database();
					db.images = new HashMap<ImageType, ArrayList<Record>>();
					save();
				}

				db.allAlbums = new HashSet<String>();
				for (ArrayList<Record> al : db.images.values()) {
					for (Record rec : al) {
						db.allAlbums.addAll(rec.albums);
					}
				}
			}
		} catch (Exception e) {
			CommonUtils.exception(e);
		}
	}

	public static Set<String> getAllAlbums() {
		load();
		return db.allAlbums;
	}

	public static Stream<Record> getRecords(ImageType type, String album) {
		load();
		if (album == null)
			return db.images.get(type).stream();
		else
			return db.images.get(type).stream().filter(record -> record.albums.contains(album));
	}

	public static void addRecord(ImageType type, Record record) {
		load();
		if (!db.images.containsKey(type))
			db.images.put(type, new ArrayList<Record>());
		db.images.get(type).add(record);
		save();
	}

	/**
	 * Save the record to file
	 * 
	 */
	private static void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile));
			oos.writeObject(db);
			oos.close();
		} catch (Exception e) {
			CommonUtils.exception(e);
		}
	}

	/**
	 * A DB record for a photograph. This contains references to the various albums that hold the
	 * 
	 * @author vs0016025
	 *
	 */
	public static class Record implements Serializable {
		private static final long	serialVersionUID	= 3728161113619888877L;

		private Set<String>			albums = new HashSet<String>();
		private String				relativeFilePath;
		private String				title;
		private String				info;
		private String				flickrUrl;

		/**
		 * @return the albums
		 */
		public final Set<String> getAlbums() {
			return albums;
		}

		/**
		 * @return the relativeFilePath
		 */
		public final String getRelativeFilePath() {
			return relativeFilePath;
		}

		/**
		 * @param relativeFilePath
		 * the relativeFilePath to set
		 */
		public final void setRelativePath(String relativeFilePath) {
			this.relativeFilePath = relativeFilePath;
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
		 * @return the info
		 */
		public final String getInfo() {
			return info;
		}

		/**
		 * @param info
		 * the info to set
		 */
		public final void setInfo(String info) {
			this.info = info;
		}

		/**
		 * @return the flickrUrl
		 */
		public String getFlickrUrl() {
			return flickrUrl;
		}

		/**
		 * @param flickrUrl
		 * the flickrUrl to set
		 */
		public void setFlickrUrl(String flickrUrl) {
			this.flickrUrl = flickrUrl;
		}

	}

	public static enum ImageType {
		Color, Monochrome, Neon, Pencil
	}
}
