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
	private static Set<String>						allAlbums			= null;
	private static String							album				= null;
	private static ImageType						type				= null;

	private HashMap<ImageType, ArrayList<Record>>	images				= null;

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

				allAlbums = new HashSet<String>();
				for (ArrayList<Record> al : db.images.values()) {
					for (Record rec : al) {
						allAlbums.addAll(rec.albums);
					}
				}
			}
		} catch (Exception e) {
			CommonUtils.exception(e);
		}
	}

	public static Set<String> getAllAlbums() {
		load();
		return allAlbums;
	}

	public static void setAlbum(String album) {
		Database.album = album;
	}

	public static void setType(ImageType type) {
		Database.type = type;
	}

	public static AlbumRecords getRecords() {
		load();
		return new AlbumRecords(type, album);
	}

	public static void addRecord(ImageType type, Record record) {
		load();
		if (!db.images.containsKey(type))
			db.images.put(type, new ArrayList<Record>());
		db.images.get(type).add(record.getCopy());
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

	public static class AlbumRecords {
		private int					index	= -1;
		private String				album;
		private ArrayList<Record>	list	= null;

		public AlbumRecords(ImageType type, String album) {
			this.album = album;
			if (type == null) {
				list = new ArrayList<Record>();
				db.images.keySet().forEach(k -> list.addAll(db.images.get(k)));
			} else if (db.images.get(type) != null) {
				list = db.images.get(type);
			}
		}

		public Record reset() {
			index = -1;
			return next();
		}

		public Record next() {
			if (list != null) {
				for (int i = index + 1; i < list.size(); i++) {
					if (album == null || list.get(i).albums.contains(album)) {
						index = i;
						break;
					}
				}
				if (index >= 0)
					return list.get(index);
			}
			return null;
		}

		public Record prev() {
			if (list != null) {
				for (int i = index - 1; i >= 0; i--) {
					if (album == null || list.get(i).albums.contains(album)) {
						index = i;
						break;
					}
				}
				return list.get(index);
			}
			return null;
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

		private Set<String>			albums				= new HashSet<String>();
		private String				relativeFilePath	= null;
		private String				title				= null;
		private String				info				= null;
		private String				flickrPhotoId		= null;

		/**
		 * 
		 */
		public Record() {
			super();
		}

		/**
		 * @param albums
		 * @param relativeFilePath
		 * @param title
		 * @param info
		 * @param flickrUrl
		 */
		public Record(Set<String> albums, String relativeFilePath, String title, String info, String flickrPhotoId) {
			this();
			this.albums = albums;
			this.relativeFilePath = relativeFilePath;
			this.title = title;
			this.info = info;
			this.flickrPhotoId = flickrPhotoId;
		}

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
		 * @return the flickrPhotoId
		 */
		public String getFlickrPhotoId() {
			return flickrPhotoId;
		}

		/**
		 * @param flickrPhotoId
		 * the flickrPhotoId to set
		 */
		public void setFlickrPhotoId(String flickrPhotoId) {
			this.flickrPhotoId = flickrPhotoId;
		}

		public String getAlbumString() {
			StringBuilder sb = new StringBuilder();
			albums.forEach(s -> sb.append(s + ":"));
			return sb.toString();
		}

		public Record getCopy() {
			return new Record(albums, relativeFilePath, title, info, flickrPhotoId);
		}

		public String getAbsolutePath() {
			return Constants.BASE_DIRECTORY + File.separator + getRelativeFilePath();
		}
	}

	public static enum ImageType {
		Color, Monochrome, Neon, Pencil
	}
}
