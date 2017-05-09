package org.github.solegaonkar.photography.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ProcessImage {
	private static final String signaturePad = "      ";

	public static void sign(BufferedImage image, String signature) {
		int ih, iw, sh, sw;
		int[] irgb;
		int[] srgb;
		ih = image.getHeight();
		iw = image.getWidth();

		// Did not dig too much into this. But it works. Most probably the font size has a 1:2 relation with the pixels.
		int fs = 2 * iw / ((signature.length() + signaturePad.length()) * 4);
		BufferedImage signatureImage = new BufferedImage(iw / 4, fs * 2, BufferedImage.TYPE_INT_RGB);
		sh = signatureImage.getHeight();
		sw = signatureImage.getWidth();

		Graphics2D g2d = signatureImage.createGraphics();
		g2d.setFont(new Font("Consolas", Font.ITALIC | Font.BOLD, fs));
		g2d.setColor(Color.WHITE);
		g2d.drawString(signature + signaturePad, 0, 5 * sh / 6);
		g2d.dispose();

		srgb = signatureImage.getRGB(0, 0, sw, sh, null, 0, sw);
		irgb = image.getRGB(iw - sw, ih - sh, sw, sh, null, 0, sw);

		for (int i = 0; i < srgb.length; i += 2) {
			irgb[i] |= srgb[i];
		}
		image.setRGB(iw - sw, ih - sh, sw, sh, irgb, 0, sw);
	}

	public static BufferedImage scale(BufferedImage image, ImageSize size) {
		float w = image.getWidth();
		float h = image.getHeight();

		float scale = (w > h) ? size.value / w : size.value / h;

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
	public static enum ImageSize {
		S00128(128), S00256(256), S00512(512), S01024(1024), S02048(2048), S04096(4096), S08192(8192), S16384(16384);

		private final int value;

		ImageSize(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static BufferedImage pencil(BufferedImage image) {
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

				img.setRGB(x, y, new Color(255 - Math.min(255, (r + g + b) / 3), 255 - Math.min(255, (r + g + b) / 3),
						255 - Math.min(255, (r + g + b) / 3)).getRGB());
			}
		}

		return img;
	}

	public static BufferedImage neon(BufferedImage image) {
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

	private static final float[] hsbWhite = new float[3];

	static {
		Color.RGBtoHSB(255, 255, 255, hsbWhite);
	}

	public static void monochrome(BufferedImage image) {

		int[] rgb = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());

		for (int i = 0; i < rgb.length; i++) {
			float r = (float) (rgb[i] & 0xff0000 >> 16);
			float g = (float) (rgb[i] & 0xff00 >> 8);
			float b = (float) (rgb[i] & 0xff);

			rgb[i] = Color.HSBtoRGB(hsbWhite[0], hsbWhite[1], (r > g ? (r > b ? r : b) : (g > b ? g : b)) / 255);
		}
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
	}

}
