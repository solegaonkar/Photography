package org.github.solegaonkar.photography.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;

/**
 * A photograph is essentially an image + metadata. It can be loaded from a file or can be obtained from Flickr. A
 * Flickr image is stored onto the disk, for easier access. Similarly, any image on disk is uploaded to Flickr. Thus,
 * the two are always kept in sync.
 * 
 * Each photograph also has an owner. Not every photograph belongs to the user. They could also come from Activity
 * Stream or the Favorites. The Activity Stream is not saved locally the Favorites are saved. Of course, the own photos
 * are saved locally.
 * 
 * Over time, some photographs may be removed from Flickr by the owners. In such a case, the URL will not work. When
 * this is discovered, the Flickr ID is set to REMOVED_FROM_FLICKR
 * 
 * @author vs0016025
 *
 */
public class Photograph {
	private BufferedImage	image		= null;
	private HashMap<String, String>	metadataMap	= new HashMap<String, String>();
	private File					file		= null;

	/**
	 * Create a new object based on the chosen database record.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public Photograph(String filePath) throws Exception {
		this.file = new File(filePath);
		load();
		readMetadata();
	}

	/**
	 * Provide the list of metadata items.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private final void readMetadata() throws Exception {
		// get all metadata stored in EXIF format (ie. from JPEG or TIFF).
		final IImageMetadata imd = Sanselan.getMetadata(file);

		if (imd instanceof JpegImageMetadata) {
			for (Object o : ((JpegImageMetadata) imd).getItems()) {
				Item item = (Item) o;
				metadataMap.put(item.getKeyword(), item.getText());
			}
		}
	}
	private void load() throws Exception {
		image = ImageIO.read(file);
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
}
