package fun.photography.framework;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import fun.photography.processors.MonochromeCreator;
import fun.photography.processors.Resizer;
import fun.photography.processors.Signature;

/**
 * Entry point for the command line application. This application will find all the Procedures according to the
 * configuration file and pass the image to them with the appropriate parameters. Each will create an output that will
 * be stored in the specified folders.
 * 
 * The original image will be stored appropriately.
 * 
 * @author Vikas
 * 
 */
public class Main {
	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ImageWriteException
	 * @throws ImageReadException
	 */
	public static void main(String[] args) throws Exception{
		MonochromeCreator mc00 = new MonochromeCreator(new Color(255, 255, 255));
		MonochromeCreator mc01 = new MonochromeCreator(new Color(255, 0, 0));
		MonochromeCreator mc02 = new MonochromeCreator(new Color(0, 255, 0));
		MonochromeCreator mc03 = new MonochromeCreator(new Color(0, 0, 255));
		MonochromeCreator mc04 = new MonochromeCreator(new Color(255, 255, 0));
		MonochromeCreator mc05 = new MonochromeCreator(new Color(0, 255, 255));
		MonochromeCreator mc06 = new MonochromeCreator(new Color(255, 0, 255));
		MonochromeCreator mc07 = new MonochromeCreator(new Color(255, 127, 127));
		MonochromeCreator mc08 = new MonochromeCreator(new Color(127, 255, 127));
		MonochromeCreator mc09 = new MonochromeCreator(new Color(127, 127, 255));
		MonochromeCreator mc10 = new MonochromeCreator(new Color(255, 255, 127));
		MonochromeCreator mc11 = new MonochromeCreator(new Color(127, 255, 255));
		MonochromeCreator mc12 = new MonochromeCreator(new Color(255, 127, 255));

		Resizer r = new Resizer(1024);
		Signature s = new Signature("Vikas K Solegaonkar");

		Photograph image = new Photograph(new File("input.jpg"));
		CommonUtils.info("Processing");

		CommonUtils.info("Resize Completed");
		s.process(image);
		CommonUtils.info("Signature Completed");

		// image.save(new File("output.jpg"));
	}
}
