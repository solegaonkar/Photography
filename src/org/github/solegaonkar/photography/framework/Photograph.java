package org.github.solegaonkar.photography.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.github.solegaonkar.photography.framework.Database.Record;

/**
 * A photograph is essentially an image + EXIF data + other MetaData like title/ description/ albums/...
 * 
 * @author vs0016025
 *
 */
public class Photograph {
	private BufferedImage			image		= null;
	private HashMap<String, String>	metadataMap	= new HashMap<String, String>();
	private File					file		= null;
	private Record					record		= null;

	/**
	 * Create a new object based on the chosen database record.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public Photograph(Record record) throws Exception {
		this.record = record;
		this.file = new File(record.getPath());
		load();
	}

	/**
	 * Load the Buffered Image and Metadata from the file.
	 * 
	 * @throws Exception
	 */
	private void load() throws Exception {
		image = ImageIO.read(file);
		// get all metadata stored in EXIF format (ie. from JPEG or TIFF).
		IImageMetadata imd = Sanselan.getMetadata(file);

		if (imd instanceof JpegImageMetadata) {
			for (Object o : ((JpegImageMetadata) imd).getItems()) {
				Item item = (Item) o;
				metadataMap.put(item.getKeyword(), item.getText());
			}
		}
	}

	/**
	 * @return the image
	 */
	public final BufferedImage getImage() {
		return image;
	}

	/**
	 * @return the metadataMap
	 */
	public final HashMap<String, String> getMetadataMap() {
		return metadataMap;
	}

	/**
	 * @return
	 * @see org.github.solegaonkar.photography.framework.Database.Record#getTitle()
	 */
	public final String getTitle() {
		return record.getTitle();
	}

	/**
	 * @param title
	 * @see org.github.solegaonkar.photography.framework.Database.Record#setTitle(java.lang.String)
	 */
	public final void setTitle(String title) {
		record.setTitle(title);
	}

	/**
	 * @return
	 * @see org.github.solegaonkar.photography.framework.Database.Record#getDescription()
	 */
	public final String getDescription() {
		return record.getDescription();
	}

	/**
	 * @param description
	 * @see org.github.solegaonkar.photography.framework.Database.Record#setDescription(java.lang.String)
	 */
	public final void setDescription(String description) {
		record.setDescription(description);
	}

	/**
	 * @return
	 * @see org.github.solegaonkar.photography.framework.Database.Record#getFlickrId()
	 */
	public final String getFlickrId() {
		return record.getFlickrId();
	}

	/**
	 * @return
	 * @see org.github.solegaonkar.photography.framework.Database.Record#getAlbums()
	 */
	public final HashSet<String> getAlbums() {
		return record.getAlbums();
	}

	/**
	 * @return
	 * @see org.github.solegaonkar.photography.framework.Database.Record#getRating()
	 */
	public int getRating() {
		return record.getRating();
	}

	/**
	 * @param rating
	 * @see org.github.solegaonkar.photography.framework.Database.Record#setRating(int)
	 */
	public void setRating(int rating) {
		record.setRating(rating);
	}

	
}
