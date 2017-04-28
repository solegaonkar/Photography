package vikas.photography.flickr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.groups.GroupsInterface;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Database.Record;

/**
 * A utility class that provides a good interface to the FlickrAPI
 * 
 * @author vs0016025
 *
 */
public class FlickrUtil {
	static final String						nsid				= "";
	static final String						userName			= "Vikas Solegaonkar";
	static final Flickr						flickr				= connect();
	static final ActivityInterface			activityInterface	= flickr.getActivityInterface();
	static final FavoritesInterface			favoritesInterface	= flickr.getFavoritesInterface();
	static final GroupsInterface			groupsInterface		= flickr.getGroupsInterface();
	static final PhotosInterface			photosInterface		= flickr.getPhotosInterface();
	static final PhotosetsInterface			photosetInterface	= flickr.getPhotosetsInterface();
	static final PeopleInterface			peopleInterface		= flickr.getPeopleInterface();
	static final Uploader					uploader			= flickr.getUploader();
	static final Collection<Photoset>		albums				= loadPhotoSets();
	static final HashMap<String, Photoset>	albumMap			= loadAlbumMap();
	static final GroupList<Group>			groups				= loadUserGroups();

	public Set<String> getAlbumNames() {
		return albumMap.keySet();
	}

	/**
	 * Upload the photograph to Flickr; add it to the corresponding albums - creating new albums if required.
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void upload(Record record) throws Exception {
		UploadMetaData metaData = new UploadMetaData();
		metaData.setPublicFlag(true);
		metaData.setDescription(record.getInfo());
		metaData.setContentType(Flickr.CONTENTTYPE_PHOTO);
		metaData.setSafetyLevel(Flickr.SAFETYLEVEL_SAFE);
		metaData.setTitle(makeSafeFilename(record.getTitle()));

		File f = new File(record.getAbsolutePath());

		String photoId = uploader.upload(f, metaData);
		if (photoId != null) {
			for (String album : record.getAlbums()) {
				if (!albumMap.containsKey(album)) {
					photosetInterface.create(album, album, photoId);
				} else {
					photosetInterface.addPhoto(albumMap.get(album).getId(), photoId);
				}
			}
		}
		record.setFlickrPhotoId(photoId);
	}

	/**
	 * Get the corresponding image from Flickr
	 * 
	 * @param photoId
	 * @return The BufferedImage object
	 * @throws Exception
	 */
	public static BufferedImage getImage(String photoId) throws Exception {
		return photosInterface.getImage(photosInterface.getPhoto(photoId).getLargeUrl());
	}

	private String makeSafeFilename(String input) {
		byte[] fname = input.getBytes();
		byte[] bad = new byte[] { '\\', '/', '"', '*' };
		byte replace = '_';
		for (int i = 0; i < fname.length; i++) {
			for (byte element : bad) {
				if (fname[i] == element) {
					fname[i] = replace;
				}
			}
			if (fname[i] == ' ')
				fname[i] = '_';
		}
		return new String(fname);
	}

	private static Flickr connect() {
		String apikey = "";
		String secret = "";

		Flickr.debugRequest = false;
		Flickr.debugStream = false;
		Flickr flickr = new Flickr(apikey, secret, new REST());

		RequestContext requestContext = RequestContext.getRequestContext();
		Auth auth = new Auth();
		auth.setPermission(Permission.DELETE);
		auth.setToken("");
		auth.setTokenSecret("");
		requestContext.setAuth(auth);

		return flickr;
	}

	private static Collection<Photoset> loadPhotoSets() {
		try {
			return photosetInterface.getList(nsid).getPhotosets();
		} catch (FlickrException e) {
			CommonUtils.exception(e);
		}
		return null;
	}

	private static GroupList<Group> loadUserGroups() {
		try {
			return peopleInterface.getGroups(nsid);
		} catch (FlickrException e) {
			CommonUtils.exception(e);
		}
		return null;
	}

	private static HashMap<String, Photoset> loadAlbumMap() {
		HashMap<String, Photoset> map = new HashMap<String, Photoset>();
		for (Photoset ps : albums) {
			map.put(ps.getTitle(), ps);
		}
		return map;
	}
}
