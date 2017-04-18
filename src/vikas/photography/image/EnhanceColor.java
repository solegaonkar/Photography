package vikas.photography.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import vikas.photography.image.Photograph.Processor;

/**
 * Normalize the brightness and increase saturation. First identify the max values for S and B. Then update the whole
 * image to set max to 1. Also use Math.pow - to increase saturation without overflowing.
 * 
 * @author vs0016025
 *
 */
public class EnhanceColor implements Processor {

	@Override
	public BufferedImage process(BufferedImage image) {
		final int[] rgb = new int[image.getHeight() * image.getWidth()];
		final float[][] hsb = new float[rgb.length][3];

		float maxB = 0.01f;
		float maxS = 0.01f;

		int i = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color c = new Color(image.getRGB(x, y));
				Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb[i]);
				maxS = (maxS > hsb[i][1]) ? maxS : hsb[i][1];
				maxB = (maxB > hsb[i][2]) ? maxB : hsb[i][2];
				i++;
			}
		}
		i = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y,
						Color.HSBtoRGB(hsb[i][0], (float) Math.pow(hsb[i][1] / maxS, 0.75f), hsb[i++][2] / maxB));
			}
		}
		return image;
	}

}
