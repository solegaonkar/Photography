package vikas.photography.image;

import java.awt.Color;
import java.io.File;
import java.util.Date;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Constants;
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
		CommonUtils.log("Reading Images from: " + Constants.InputPath);
		for (File file : new File(Constants.InputPath).listFiles()) {
			process(new Photograph(file));
		}
	}

	private static final Processor	scale		= new ScaleImage(Size.S01024);
	private static final Processor	sign		= new SignImage("Vikas Solegaonkar");
	private static final Processor	enhance		= new EnhanceColor();
	private static final Processor	monochrome	= new Monochrome(new Color(255, 255, 255));
	private static final Processor	neon		= new Neon();
	private static final Processor	pencil		= new Pencil();

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	public void process(Photograph photograph) throws Exception {
		photograph.start().apply(scale).apply(enhance).apply(sign).save(Constants.EnhancedPath, 1f);
		photograph.start().apply(scale).apply(monochrome).apply(sign).save(Constants.MonochromePath, 1f);
		photograph.start().apply(scale).apply(neon).apply(sign).save(Constants.NeonPath, 1f);
		photograph.start().apply(scale).apply(pencil).apply(sign).save(Constants.PencilPath, 1f);
	}

	public static void main(String[] args) throws Exception {
		new ImageProcessor();
	}
}
