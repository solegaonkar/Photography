/**
 * 
 */
package fun.photography.processors;

import vikas.photography.image.ImageProcessor;

/**
 * @author vs0016025
 *
 */
public abstract class Processing {
	
	public Processing(ImageProcessor file) {
		
	}
	
	public void apply() {
		
	}
	
	public static interface Processable {
		Processable process();
	}
}
