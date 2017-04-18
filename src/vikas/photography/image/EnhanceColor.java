package vikas.photography.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import vikas.photography.image.Photograph.Processor;

public class EnhanceColor implements Processor {

	@Override
	public BufferedImage process(BufferedImage image) {
		final int[] rgb1 = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		final int[] rgb2 = new int[rgb1.length];
		final float[][] hsv = new float[rgb1.length][3];

		float maxV = 0.01f;
		float maxS = 0.01f;
		for (int i = 0; i < rgb1.length; i++) {
			Color.RGBtoHSB(rgb1[i] >> 16, (rgb1[i] & 0xff00) >> 8, rgb1[i] & 0xff, hsv[i]);
			maxS = (maxS > hsv[i][1]) ? maxS : hsv[i][1];
			maxV = (maxV > hsv[i][2]) ? maxV : hsv[i][2];
		}
		for (int i = 0; i < rgb2.length; i++) {
			rgb2[i] = Color.HSBtoRGB(hsv[i][0], hsv[i][1] / maxS, hsv[i][2] / maxV);
		}
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgb2, 0, image.getWidth());
		return image;
	}

}
