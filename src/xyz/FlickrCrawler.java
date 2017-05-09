package xyz;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

/*
 * - Java 7 is needed - insert your api- and secretkey
 *
 * start main with wanted tags as parameter, for example: FlickrCrawler.main(Sunset) and all pics will be saved in
 * original size or large to pics\sunset\...
 */
public class FlickrCrawler {

	private static String		path		= "";
	private static Preferences	userPrefs	= Preferences.userNodeForPackage(FlickrCrawler.class);

	// convert filename to clean filename
	public static String convertToFileSystemChar(String name) {
		String erg = "";
		Matcher m = Pattern.compile("[a-z0-9 _#&@\\[\\(\\)\\]\\-\\.]", Pattern.CASE_INSENSITIVE).matcher(name);
		while (m.find()) {
			erg += name.substring(m.start(), m.end());
		}
		if (erg.length() > 200) {
			erg = erg.substring(0, 200);
			System.out.println("cut filename: " + erg);
		}
		return erg;
	}

	public static boolean saveImage(Flickr f, Photo p) {
		String cleanTitle = convertToFileSystemChar(p.getTitle());

		File orgFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_o." + p.getOriginalFormat());
		File largeFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_b." + p.getOriginalFormat());

		if (orgFile.exists() || largeFile.exists()) {
			System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " skipped!");
			return false;
		}

		try {
			
			Photo nfo = f.getPhotosInterface().getInfo(p.getId(), null);
			
			System.out.println("Photo URL" + p.getPhotoUrl());
			System.out.println("Photo URL" + p.getLargeUrl());
			if (nfo.getOriginalSecret().isEmpty()) {
				ImageIO.write(p.getLargeImage(), p.getOriginalFormat(), largeFile);
				System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " was written to " + largeFile.getName());
			} else {
				p.setOriginalSecret(nfo.getOriginalSecret());
				ImageIO.write(p.getOriginalImage(), p.getOriginalFormat(), orgFile);
				System.out.println(p.getTitle() + "\t" + p.getOriginalUrl() + " was written to " + orgFile.getName());
			}
		} catch (FlickrException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) throws FlickrException {

		String apikey = "";
		String secret = "";

		Flickr flickr = new Flickr(apikey, secret, new REST());

		RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken("");
        auth.setTokenSecret("");
        requestContext.setAuth(auth);

        path = "pics";

		new File(path).mkdirs();

		for (int i = userPrefs.getInt(path, 0); i<2; i++) {
			userPrefs.putInt(path, i);
			System.out.println("\tcurrent page: " + userPrefs.getInt(path, 0));
			try {
				PhotoList<Photo> list = flickr.getPhotosInterface().getContactsPhotos(10, false, false, true);
				if (list.isEmpty())
					break;

				for (Photo photo : list) {
					saveImage(flickr, photo);
				}
			} catch (FlickrException e) {
				e.printStackTrace();
			}
		}
	}
}
