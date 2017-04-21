package vikas.photography.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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

import vikas.photography.framework.CommonUtils;

/**
 * A Utility container class that holds a BufferedImage. It delegates all the functions of the buffered Image and also
 * creates some of its own.
 * 
 * @author Vikas
 */
public class Photograph {
	private BufferedImage					wip						= null;
	private final BufferedImage				img;
	// Metadata in ImageIO format - easier to write back to the new file.
	private final IIOMetadata				md;
	// Metadata in Apache format - that is easier to read and deal with.
	private final HashMap<String, String>	metadata				= new HashMap<String, String>();

	private static final String				FILE_TYPE_SUFFIX		= "jpg";
	private static final SimpleDateFormat	metadataDateFormat		= new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	private static final SimpleDateFormat	outputFilePathFormat	= new SimpleDateFormat("yyyyMM/yyyyMMdd");

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
		md = reader.getImageMetadata(0);
		img = reader.read(0);

		reader.dispose();
		readMetadata(file);
	}

	public Photograph start() {
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		wip = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		return this;
	}

	/**
	 * Save the image to the given file - in JPG format.
	 * 
	 * @param file
	 * - the destination file
	 * @param quality
	 * - compression quality 0 (worst) : 1 (best)
	 * @throws IOException
	 */
	public final void save(String parentFolder, float quality) throws Exception {
		File file = new File(parentFolder, outputFilePathFormat.format(getOriginalDateTime()));
		file.getParentFile().mkdirs();
		file = getTargetFile(file.getAbsolutePath(), 0);
		
		ImageWriter writer = ImageIO.getImageWritersByFormatName(FILE_TYPE_SUFFIX).next();
		writer.setOutput(ImageIO.createImageOutputStream(new FileOutputStream(file)));
		JPEGImageWriteParam iwParam = (JPEGImageWriteParam) writer.getDefaultWriteParam();
		iwParam.setOptimizeHuffmanTables(true);
		iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwParam.setCompressionQuality(quality);
		writer.write(null, new IIOImage(wip, null, md), iwParam);
		writer.dispose();
	}

	private File getTargetFile(String base, int index) {
		File file = new File(String.format("%s.%02d.jpg", base, index));
		return file.exists() ? getTargetFile(base, index + 1) : file;
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

	public final Date getOriginalDateTime() {

		try {
			return metadataDateFormat.parse(metadata.get("Date Time Original").replaceAll("'", ""));
		} catch (ParseException e) {
			CommonUtils.exception(e);
			return null;
		}
	}

	public final Photograph apply(Processor processor) {
		wip = processor.process(wip);
		return this;
	}

	public static interface Processor {
		BufferedImage process(BufferedImage image);
	}
}
