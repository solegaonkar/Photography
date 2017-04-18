package vikas.photography.image;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vikas.photography.image.Photograph.Processor;
import vikas.photography.image.ScaleImage.Size;

/**
 * An image file thrown at the application.
 * 
 * @author vs0016025
 *
 */
public class ImageFile {
	private final String		srcPath;
	private final List<String>	albums	= new ArrayList<String>();
	private final Photograph	photograph;

	/**
	 * Load the Photograph from the given file.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public ImageFile(File file) throws Exception {
		this.srcPath = file.getAbsolutePath();
		this.photograph = new Photograph(file).apply(scale).apply(sign);
	}

	private static final Processor	scale		= new ScaleImage(Size.S01024);
	private static final Processor	sign		= new SignImage("V i k a s   S o l e g a o n k a r");
	private static final Processor	monochrome	= new Monochrome(new Color(255, 255, 255));
	private static final Processor	enhance		= new EnhanceColor();

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	public void process() throws Exception {
		photograph.reset().apply(scale).apply(enhance).apply(sign).save(new File("enhance.jpg"), 1f);
		photograph.reset().apply(scale).apply(monochrome).apply(sign).save(new File("monochrome.jpg"), 1f);
	}

	public static void main(String[] args) throws Exception {
		new ImageFile(new File("input.jpg")).process();
	}
}
