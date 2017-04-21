package vikas.photography.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import vikas.photography.image.Photograph.Processor;

public class SignImage implements Processor {
	private String signature;

	/**
	 * @param signature
	 */
	public SignImage(String signature) {
		this.signature = signature;
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		int ih, iw, sh, sw;
		int[] irgb;
		int[] srgb;
		ih = image.getHeight();
		iw = image.getWidth();

		// Did not dig too much into this. But it works. Most probably the font size has a 1:2 relation with the pixels.
		int fs = 2 * iw / ((signature.length() + 8));
		BufferedImage signatureImage = new BufferedImage(iw, fs * 2, BufferedImage.TYPE_INT_RGB);
		sh = signatureImage.getHeight();
		sw = signatureImage.getWidth();

		Graphics2D g2d = signatureImage.createGraphics();
		g2d.setFont(new Font("Consolas", Font.ITALIC, fs));
		g2d.setColor(Color.WHITE);
		g2d.drawString("   " + signature, 0, 5 * sh / 6);
		g2d.dispose();
		
		signatureImage = new Neon().process(signatureImage);

		srgb = signatureImage.getRGB(0, 0, sw, sh, null, 0, sw);
		irgb = image.getRGB(iw - sw, ih - sh, sw, sh, null, 0, sw);

		float[] hsb = new float[3];
		Color c;
		for (int i = 0; i < srgb.length; i++) {
			if ((srgb[i] & 0xffffff) > 0) {
				irgb[i] |= srgb[i];
				c = new Color(irgb[i]);
				Color.RGBtoHSB(c.getRGB(), c.getGreen(), c.getBlue(), hsb);
				hsb[2] = hsb[2] < 0.8f ? hsb[2] + 0.2f : hsb[2] - 0.2f;
				irgb[i] = Color.HSBtoRGB(hsb[0], 0, hsb[2]);
			}
		}
		image.setRGB(iw - sw, ih - sh, sw, sh, irgb, 0, sw);
		return image;
	}

}
