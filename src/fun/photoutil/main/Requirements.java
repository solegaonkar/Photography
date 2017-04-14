package fun.photoutil.main;

/**
 * Application to automate most of the regular tasks in managing photographs.
 * 
 * @author vs0016025
 */
public class Requirements {
	/*
	 * 1. Show the startup and start working in the background
	 * 
	 * 2. Check the configuration JSON for pictures folder. Assume and create ./Photography if the record does not
	 * exist. In such a case, create the new entry in the configuration JSON
	 * 
	 * 3. One by one, pick up JPG files in the input folder and build the basic data - Pick the first file in the
	 * alphabetical order, check its MetaData and build LOW_RESOLUTION buffered images for all possible variants built
	 * based on all the instances of Processors in the class path. (Plugins)
	 * 
	 * 4. Display these images on the screen - in form of scrolling, tiled clickable buttons. Each tile has a X, that
	 * allows you to delete that configuration
	 * 
	 * 5. On clicking any button, show the particular image in MEDIUM_RESOLUTION, with sliders for all the parameters
	 * applied. Save / Add button will let you save or add this configuration for the particular image.
	 * 
	 * 6. By default, all the accepted configurations will be saved in a new JSON file for each image in the JSON
	 * folder.
	 * 
	 * 7. The JSON files can be used to review images so far. The MEDIUM_RESOLUTION images will be generated on demand
	 * as they are displayed. The display should be based on Category / Process / Year / Month.
	 * 
	 * 8. Processors: Monochrome, Edging, Signature, Sharpen, Saturation, Color offset (O/G, Y/B)
	 * 
	 */
}
