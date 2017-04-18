package vikas.photography.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fun.photoutil.main.CommonUtils;
import vikas.photography.image.Photograph.Processor;

/**
 * Scale down the image.
 * 
 * @author vs0016025
 *
 */
public class ScaleImage implements Processor {
	private float	scale	= 0f;
	private Size	size	= null;

	public ScaleImage(float scale) {
		this.scale = scale;
	}

	public ScaleImage(Size size) {
		this.size = size;

	}

	@Override
	public BufferedImage process(BufferedImage image) {
		float w = image.getWidth();
		float h = image.getHeight();

		if (scale == 0.0f) {
			CommonUtils.log("Scaling to Size:" + size.value);
			scale = (w > h) ? size.value / w : size.value / h;
		}
		CommonUtils.log("Scaling by: " + scale);

		BufferedImage resizedImage = new BufferedImage((int) (image.getWidth() * scale),
				(int) (image.getHeight() * scale), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
		g.dispose();
		return resizedImage;
	}

	/**
	 * Standard sizes of the output image.
	 * 
	 * @author vs0016025
	 *
	 */
	public static enum Size {
		S00128(128), S00256(256), S00512(512), S01024(1024), S02048(2048), S04096(4096), S08192(8192), S16384(16384);

		private final int value;

		Size(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
