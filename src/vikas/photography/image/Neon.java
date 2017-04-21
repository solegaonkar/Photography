package vikas.photography.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import vikas.photography.image.Photograph.Processor;

public class Neon implements Processor {

	@Override
	public BufferedImage process(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 1; x < image.getWidth() - 1; x++) {
			for (int y = 1; y < image.getHeight() - 1; y++) {
				Color[] nc = new Color[9];
				nc[0] = new Color(image.getRGB(x - 1, y - 1));
				nc[1] = new Color(image.getRGB(x, y - 1));
				nc[2] = new Color(image.getRGB(x + 1, y - 1));
				nc[3] = new Color(image.getRGB(x - 1, y));
				nc[4] = new Color(image.getRGB(x, y));
				nc[5] = new Color(image.getRGB(x + 1, y));
				nc[6] = new Color(image.getRGB(x - 1, y + 1));
				nc[7] = new Color(image.getRGB(x, y + 1));
				nc[8] = new Color(image.getRGB(x + 1, y + 1));

				int r = Math
						.abs((nc[6].getRed() + 2 * nc[7].getRed() + nc[8].getRed())
								- (nc[0].getRed() + 2 * nc[1].getRed() + nc[2].getRed()))
						+ Math.abs((nc[2].getRed() + 2 * nc[5].getRed() + nc[8].getRed())
								- (nc[0].getRed() + 2 * nc[3].getRed() + nc[6].getRed()));
				int g = Math
						.abs((nc[6].getGreen() + 2 * nc[7].getGreen() + nc[8].getGreen())
								- (nc[0].getGreen() + 2 * nc[1].getGreen() + nc[2].getGreen()))
						+ Math.abs((nc[2].getGreen() + 2 * nc[5].getGreen() + nc[8].getGreen())
								- (nc[0].getGreen() + 2 * nc[3].getGreen() + nc[6].getGreen()));
				int b = Math
						.abs((nc[6].getBlue() + 2 * nc[7].getBlue() + nc[8].getBlue())
								- (nc[0].getBlue() + 2 * nc[1].getBlue() + nc[2].getBlue()))
						+ Math.abs((nc[2].getBlue() + 2 * nc[5].getBlue() + nc[8].getBlue())
								- (nc[0].getBlue() + 2 * nc[3].getBlue() + nc[6].getBlue()));

				img.setRGB(x, y, new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b)).getRGB());
			}
		}

		return img;
	}

}
