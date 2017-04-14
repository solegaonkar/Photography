package fun.photography.processors;

import fun.photography.framework.Photograph;
import fun.photography.framework.Processor;

/**
 * Resize the image, maintaining the aspect ratio. Fit it into the max size.
 * 
 * @author vs0016025
 *
 */
public class Resizer implements Processor {
	private int max;

	/**
	 * The max width or height of the image.
	 * 
	 * @param max
	 */
	public Resizer(int max) {
		this.max = max;
	}

	@Override
	public void process(Photograph image) {
		int w = image.getWidth();
		int h = image.getHeight();

		if (w > h) {
			image.scale(max, max * h / w);
		} else {
			image.scale(max * w / h, max);
		}
	}
}
