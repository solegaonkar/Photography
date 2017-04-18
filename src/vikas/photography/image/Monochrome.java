package vikas.photography.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import vikas.photography.image.Photograph.Processor;

public class Monochrome implements Processor {
	private float[] hsbVals = new float[3];

	/**
	 * @param base
	 */
	public Monochrome(Color base) {
		Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), hsbVals);
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		int[] rgb = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());

		for (int i = 0; i < rgb.length; i++) {
			float r = (float) (rgb[i] & 0xff0000 >> 16);
			float g = (float) (rgb[i] & 0xff00 >> 8);
			float b = (float) (rgb[i] & 0xff);

			rgb[i] = Color.HSBtoRGB(hsbVals[0], hsbVals[1], (r > g ? (r > b ? r : b) : (g > b ? g : b)) / 255);
		}
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
		return image;
	}

}
