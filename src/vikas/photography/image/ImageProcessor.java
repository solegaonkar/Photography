package vikas.photography.image;

import java.awt.Color;
import java.io.File;

import javax.swing.JDialog;

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
			System.gc();
		}
	}

	private static final Processor	scale		= new ScaleImage(Size.S01024);
	private static final Processor	sign		= new SignImage("Vikas Solegaonkar");
	private static final Processor	enhance		= new EnhanceColor();
	private static final Processor	monochrome	= new Monochrome(new Color(255, 255, 255));
	private static final Processor	pencil		= new Pencil();
	private static final Processor	neon		= new Neon();

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	public void process(Photograph photograph) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		new ImageProcessor();
	}
}
