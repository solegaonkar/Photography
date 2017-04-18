package vikas.photography.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;

/**
 * A Utility container class that holds a BufferedImage. It delegates all the functions of the buffered Image and also
 * creates some of its own.
 * 
 * @author Vikas
 */
public class Photograph {
	private BufferedImage					wip;
	private final BufferedImage				img;
	// Metadata in ImageIO format - easier to write back to the new file.
	private final IIOMetadata				md;
	// Metadata in Apache format - that is easier to read and deal with.
	private final HashMap<String, String>	metadata			= new HashMap<String, String>();

	private static final String				FILE_TYPE_SUFFIX	= "jpg";

	/**
	 * Create an instance from the given source file.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Photograph(File file) throws Exception {
		ImageReader reader = ImageIO.getImageReadersBySuffix(FILE_TYPE_SUFFIX).next();
		reader.setInput(ImageIO.createImageInputStream(new FileInputStream(file)));
		// The index is meaningful in TIFF images that can have multiple pages.
		md = reader.getImageMetadata(0);
		img = reader.read(0);
		wip = reader.read(0);

		reader.dispose();
		readMetadata(file);
		save(new File("TestSave.jpg"), 1f);
	}

	public Photograph reset() {
		 ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 wip = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		 return this;
	}
	/**
	 * Save the image to the given file - in JPG format.
	 * 
	 * For some reason, the StackOverflow link
	 * (http://stackoverflow.com/questions/8972357/manipulate-an-image-without-deleting-its-exif-data) ends this with
	 * ImageIO.write(image, "jpg", ios);
	 * 
	 * Doubt if that is required - also the code seems to work pretty well without it. Check up if there is a problem
	 * somewhere.
	 * 
	 * It cribbed for missing Huffman Tables.. Fixed by adding the setOptimizedHuffmanTable(true)
	 * 
	 * @param file
	 * - the destination file
	 * @param quality
	 * - compression quality 0 (worst) : 1 (best)
	 * @throws IOException
	 */
	public final void save(File file, float quality) throws Exception {
		ImageWriter writer = ImageIO.getImageWritersByFormatName(FILE_TYPE_SUFFIX).next();
		writer.setOutput(ImageIO.createImageOutputStream(new FileOutputStream(file)));
		JPEGImageWriteParam iwParam = (JPEGImageWriteParam) writer.getDefaultWriteParam();
		iwParam.setOptimizeHuffmanTables(true);
		iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwParam.setCompressionQuality(quality);
		writer.write(null, new IIOImage(wip, null, md), iwParam);
		writer.dispose();
	}

	/**
	 * Get the Image pixels
	 * 
	 * @return
	 */
	public final int[] getRGB() {
		return wip.getRGB(0, 0, wip.getWidth(), wip.getHeight(), null, 0, wip.getWidth());
	}

	/**
	 * Update the Image.
	 * 
	 * @param rgbArray
	 */
	public final void setRGB(int[] rgbArray) {
		if (rgbArray.length == wip.getWidth() * wip.getHeight())
			wip.setRGB(0, 0, wip.getWidth(), wip.getHeight(), rgbArray, 0, wip.getWidth());
	}

	/**
	 * Provide the list of metadata items.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private final void readMetadata(final File file) throws Exception {
		// get all metadata stored in EXIF format (ie. from JPEG or TIFF).
		final IImageMetadata md = Sanselan.getMetadata(file);

		if (md instanceof JpegImageMetadata) {
			for (Object o : ((JpegImageMetadata) md).getItems()) {
				Item item = (Item) o;
				metadata.put(item.getKeyword(), item.getText());
			}
		}
	}

	/**
	 * Get the metadata map
	 * 
	 * @return
	 */
	public final Map<String, String> getMetadata() {
		return metadata;
	}

	public final Photograph apply(Processor processor) {
		wip = processor.process(wip);
		return this;
	}

	public static interface Processor {
		BufferedImage process(BufferedImage image);
	}

}
