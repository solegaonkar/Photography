package vikas.photography.image;

import java.awt.Color;
import java.io.File;
import java.util.Date;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.PhotographyConstants;
import vikas.photography.image.Photograph.Processor;
import vikas.photography.image.ScaleImage.Size;

/**
 * An image file thrown at the application.
 * 
 * @author vs0016025
 *
 */
public class ImageProcessor {

	/**
	 * Load the Photograph from the given file.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public ImageProcessor() throws Exception {
		CommonUtils.log("Reading Images from: " + PhotographyConstants.InputPath);
		for (File file : new File(PhotographyConstants.InputPath).listFiles()) {
			process(new Photograph(file));
		}
	}

	private static final Processor	scale		= new ScaleImage(Size.S01024);
	private static final Processor	sign		= new SignImage("Vikas Solegaonkar");
	private static final Processor	monochrome	= new Monochrome(new Color(255, 255, 255));
	private static final Processor	enhance		= new EnhanceColor();

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	public void process(Photograph photograph) throws Exception {
		Date timeStamp = photograph.getOriginalDateTime();
		photograph.start().apply(scale).apply(enhance).apply(sign).save(PhotographyConstants.EnhancedPath, timeStamp,
				1f);
		photograph.start().apply(scale).apply(monochrome).apply(sign).save(PhotographyConstants.MonochromePath,
				timeStamp, 1f);
	}

	public static void main(String[] args) throws Exception {
		new ImageProcessor();
	}
}
