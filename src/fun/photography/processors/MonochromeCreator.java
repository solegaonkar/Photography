package fun.photography.processors;

import java.awt.Color;

import fun.photography.framework.Photograph;
import fun.photography.framework.Processor;

/**
 * 
 * @author vs0016025
 */
public class MonochromeCreator implements Processor {
	private float[] hsbVals = new float[3];

	/**
	 * @param base
	 */
	public MonochromeCreator(Color base) {
		super();
		Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), hsbVals);
	}

	@Override
	public void process(Photograph image) {
		int[] rgb = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());

		for (int i = 0; i < rgb.length; i++) {
			float r = (float) (rgb[i] & 0xff0000 >> 16);
			float g = (float) (rgb[i] & 0xff00 >> 8);
			float b = (float) (rgb[i] & 0xff);

			rgb[i] = Color.HSBtoRGB(hsbVals[0], hsbVals[1], (r > g ? (r > b ? r : b) : (g > b ? g : b)) / 255);
		}
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
	}

}
