/**
 * 
 */
package fun.photography.processors;

import vikas.photography.image.ImageFile;

/**
 * @author vs0016025
 *
 */
public abstract class Processing {
	
	public Processing(ImageFile file) {
		
	}
	
	public void apply() {
		
	}
	
	public static interface Processable {
		Processable process();
	}
}
